package utilidad;

import cuentaBancaria.CuentaBancariaParametros;
import cuentaBancaria.CuentaBancariaUtil;
import cuentaBancaria.CuentaBancariaVO;
import direccion.DireccionUtil;
import direccion.DireccionVO;
import java.util.*;
import java.sql.*;
import java.util.Date;
import municipio.MunicipioUtil;
import municipio.MunicipioVO;
import parroquia.ParroquiaUtil;
import parroquia.ParroquiaVO;
import provincia.ProvinciaUtil;
import provincia.ProvinciaVO;
import telefono.TelefonoUtil;
import telefono.TelefonoVO;
import usuario.UsuarioUtil;
import usuario.UsuarioVO;
import usuario.UsuarioParametros;

public class CImportacionECulturais {

    public static void eliminarGuionDNI() {
        UsuarioParametros param = new UsuarioParametros();
        ArrayList<UsuarioVO> lista = UsuarioUtil.listado(param);
        for (UsuarioVO usuario : lista) {
            String dni = usuario.getDni();
            boolean modificar = false;
            if (dni != null) {
                modificar = (dni.indexOf("-") != -1);
                dni = dni.replaceAll("-", "");
            }
            if (modificar) {
                usuario.setDni(dni);
                UsuarioUtil.actualizar(usuario);
            }
        }
    }

    public static void generarImportacionesUsuarios() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sqlSelect = "";
            sqlSelect += "SELECT * FROM eculturais ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            st = con.createStatement();
            rs = st.executeQuery(sqlSelect);
            int numUserDuplicados = 0;
            rs.beforeFirst();
            int i = 1;
            ProvinciaVO provincia = ProvinciaUtil.buscarProvincia(1);
            while (rs.next()) {
                try {
                    UsuarioVO objUsuario = new UsuarioVO();
                    objUsuario.setIdCentroGestion(3);
                    objUsuario.setCodigoTarjeta(rs.getString("Núm ficha").toUpperCase().trim());
                    objUsuario.setNombre(rs.getString("Nome").toUpperCase().trim());
                    objUsuario.setApellidos(rs.getString("Apelidos").toUpperCase().trim());
                    String dni = rs.getString("DNI");
                    if ((dni != null) && (dni.trim().length() < 10)) {
                        objUsuario.setDni(dni.toUpperCase().trim());
                    }
                    UsuarioVO usuarioExistente = UsuarioUtil.buscarUsuario(objUsuario.getNombre(), objUsuario.getApellidos());
                    if (usuarioExistente.getId() != -1) {
                        System.out.println("Usuario Duplicado: " + objUsuario.getNombreCompleto());
                        numUserDuplicados++;
                        comprobarCuentaBancaria(usuarioExistente, rs);
                        throw new MiExcepcion("Usuario duplicado");
                    }
                    objUsuario.setSs(rs.getString("Sergas"));
                    objUsuario.setFechaNacimiento(rs.getDate("Data de nacemento"));
                    objUsuario.setFechaAlta(rs.getDate("Data de alta"));
                    String telefonos = rs.getString("Teléfono");
                    if ((telefonos == null) || telefonos.length() < 9) {
                        telefonos = "";
                    }
                    String mobil = rs.getString("mobil");
                    if ((mobil != null) && mobil.length() > 8) {
                        if (telefonos.length() > 0) {
                            telefonos += ", ";
                        }
                        telefonos += mobil;
                    }
                    objUsuario.setTelefonoPpl(telefonos);
                    String direccion = rs.getString("Enderezo");
                    direccion.DireccionVO d = new DireccionVO();
                    if ((direccion != null) && (direccion.trim().length() > 0)) {
                        d = new direccion.DireccionVO();
                        d.setPais("ESPAÑA");
                        d.setIdComunidad(13);
                        d.setIdProvincia(1);
                        d.setIdMunicipio(getIdMunicipio(rs.getString("Poboación")));
                        d.setIdParroquia(getIdParroquia(rs.getString("Parroquia")));
                        d.setTipoVial("");
                        d.setVial(direccion.trim());
                        d.setCp(rs.getString("C#P#"));
                        d.setTexto(formatearTexto(d, provincia));
                        DireccionUtil.insertar(d);
                    }
                    if (d.getId() != -1) {
                        objUsuario.setIdDireccion(d.getId());
                    }
                    UsuarioUtil.insertar(objUsuario);
                    if ((objUsuario.getTelefonoPpl().length() > 0) && (objUsuario.getId() != -1)) {
                        String tel = rs.getString("Teléfono");
                        if ((tel != null) && (!tel.isEmpty()) && (tel.length() > 4)) {
                            TelefonoVO objTelefono = new TelefonoVO();
                            objTelefono.setEntidad(TelefonoUtil.ENTIDAD_USUARIO);
                            objTelefono.setIdEntidad(objUsuario.getId());
                            objTelefono.setModo(TelefonoUtil.MODO_TELEFONO);
                            objTelefono.setPpl(true);
                            objTelefono.setNumero(tel);
                            objTelefono.setTipo("FIXO");
                            TelefonoUtil.insertar(objTelefono);
                        }
                        tel = rs.getString("mobil");
                        if ((tel != null) && (!tel.isEmpty()) && (tel.length() > 4)) {
                            TelefonoVO objTelefono = new TelefonoVO();
                            objTelefono.setEntidad(TelefonoUtil.ENTIDAD_USUARIO);
                            objTelefono.setIdEntidad(objUsuario.getId());
                            objTelefono.setModo(TelefonoUtil.MODO_TELEFONO);
                            objTelefono.setPpl(true);
                            objTelefono.setNumero(tel);
                            objTelefono.setTipo("MÓBIL");
                            TelefonoUtil.insertar(objTelefono);
                        }
                    }
                    insertarCuentaBancaria(objUsuario, rs);
                } catch (Exception e) {
                }
            }
            System.out.println("Num Usuarios Duplicados " + numUserDuplicados);
        } catch (Exception e) {
            System.out.println("EXCEPCION EN GenerarImportaciones : CImportacion " + e.toString());
        } finally {
            ConexionBD.cerrarConexiones(rs, st, con);
        }
    }

    public static ArrayList<String> generarConjunto(String cadena) {
        ArrayList<String> lista = new ArrayList<String>();
        HashSet<String> conjunto = new HashSet<String>();
        if ((cadena != null) && (!cadena.isEmpty())) {
            conjunto = new HashSet(Arrays.asList(cadena.split("/")));
        }
        for (String c : conjunto) {
            lista.add(c.trim());
        }
        return lista;
    }

    public static Date calcularFechaNacimiento(Integer edad) {
        Integer ano = 2008 - edad;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.YEAR, ano);
        return cal.getTime();
    }

    public static String formatearTexto(DireccionVO direccion, ProvinciaVO provincia) {
        String texto;
        texto = "";
        if (direccion.getVial().isEmpty() == false) {
            texto += direccion.getVial() + " ";
        }
        if (direccion.getNumero().isEmpty() == false) {
            texto += direccion.getNumero() + ", ";
        }
        if (direccion.getPiso().isEmpty() == false) {
            texto += direccion.getPiso();
        }
        if (direccion.getPuerta().isEmpty() == false) {
            texto += "-" + direccion.getPuerta() + " ";
        }
        texto += "\n";
        if (direccion.getCp().isEmpty() == false) {
            texto += "CP: " + direccion.getCp() + ". ";
        }
        if (direccion.getIdParroquia() != -1) {
            ParroquiaVO parroquia = ParroquiaUtil.buscarParroquia(direccion.getIdParroquia());
            texto += "" + parroquia.getNombre() + ", ";
        }
        if (direccion.getIdMunicipio() != -1) {
            MunicipioVO municipio = MunicipioUtil.buscarMunicipio(direccion.getIdMunicipio());
            texto += "" + municipio.getNombre();
        }
        if (direccion.getIdProvincia() != -1) {
            texto += " (" + provincia.getNombre() + ")";
        }
        return texto;
    }

    public static void comprobarCuentaBancaria(UsuarioVO usuario, ResultSet rs) throws Exception {
        CuentaBancariaParametros param = new CuentaBancariaParametros();
        param.entidad = CuentaBancariaUtil.ENTIDAD_USUARIO;
        param.idEntidad = usuario.getId();
        if (CuentaBancariaUtil.listado(param).size() == 0) {
            insertarCuentaBancaria(usuario, rs);
        }
    }

    public static void insertarCuentaBancaria(UsuarioVO usuario, ResultSet rs) throws Exception {
        if ((rs.getString("Núm Conta") != null) && (usuario.getId() != -1)) {
            CuentaBancariaVO objCuentaBancaria = new CuentaBancariaVO();
            objCuentaBancaria.setEntidad(CuentaBancariaUtil.ENTIDAD_USUARIO);
            objCuentaBancaria.setIdEntidad(usuario.getId());
            objCuentaBancaria.setPpl(true);
            objCuentaBancaria.setTitular(rs.getString("Titular"));
            objCuentaBancaria.setDniTitular(rs.getString("Nif"));
            objCuentaBancaria.setEntidadBancaria(rs.getString("Banco"));
            objCuentaBancaria.setNombreSucursal(rs.getString("Oficina"));
            String ban = rs.getString("Núm Conta").substring(0, 4);
            String suc = rs.getString("Núm Conta").substring(5, 9);
            String dc = rs.getString("Núm Conta").substring(10, 12);
            String nc = rs.getString("Núm Conta").substring(13, 23);
            objCuentaBancaria.setBanco(ban);
            objCuentaBancaria.setSucursal(suc);
            objCuentaBancaria.setDigitosControl(dc);
            objCuentaBancaria.setNumeroCuenta(nc);
            CuentaBancariaUtil.insertar(objCuentaBancaria);
        }
    }

    public static int getIdMunicipio(String nombre) {
        int idMunicipio = -1;
        if (nombre != null) {
            if (nombre.equalsIgnoreCase("A Capela")) {
                idMunicipio = 1737;
            }
            if (nombre.equalsIgnoreCase("Ares")) {
                idMunicipio = 1737;
            }
            if (nombre.equalsIgnoreCase("Arteixo")) {
                idMunicipio = 702;
            }
            if (nombre.equalsIgnoreCase("As Pontes")) {
                idMunicipio = 5305;
            }
            if (nombre.equalsIgnoreCase("Cabanas")) {
                idMunicipio = 1458;
            }
            if (nombre.equalsIgnoreCase("Fene")) {
                idMunicipio = 2730;
            }
            if (nombre.equalsIgnoreCase("Ferrol")) {
                idMunicipio = 2742;
            }
            if (nombre.equalsIgnoreCase("Mugardos")) {
                idMunicipio = 4472;
            }
            if (nombre.equalsIgnoreCase("Narón")) {
                idMunicipio = 4533;
            }
            if (nombre.equalsIgnoreCase("Neda")) {
                idMunicipio = 4630;
            }
            if (nombre.equalsIgnoreCase("San Sadurnino")) {
                idMunicipio = 6075;
            }
            if (nombre.equalsIgnoreCase("San Sadurniño")) {
                idMunicipio = 6075;
            }
            if (nombre.equalsIgnoreCase("Sillobre")) {
                idMunicipio = 2730;
            }
            if (nombre.equalsIgnoreCase("Valdoviño")) {
                idMunicipio = 7219;
            }
        }
        return idMunicipio;
    }

    public static int getIdParroquia(String nombre) {
        int idParroquia = -1;
        if (nombre != null) {
            if (nombre.equalsIgnoreCase("Barallobre")) {
                idParroquia = 1;
            }
            if (nombre.equalsIgnoreCase("Fene")) {
                idParroquia = 2;
            }
            if (nombre.equalsIgnoreCase("Limodre")) {
                idParroquia = 3;
            }
            if (nombre.equalsIgnoreCase("Magalofes")) {
                idParroquia = 4;
            }
            if (nombre.equalsIgnoreCase("Maniños")) {
                idParroquia = 5;
            }
            if (nombre.equalsIgnoreCase("Perlío")) {
                idParroquia = 6;
            }
            if (nombre.equalsIgnoreCase("San Valentín")) {
                idParroquia = 7;
            }
            if (nombre.equalsIgnoreCase("Sillobre")) {
                idParroquia = 8;
            }
        }
        return idParroquia;
    }
}
