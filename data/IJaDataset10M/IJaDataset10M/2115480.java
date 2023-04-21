package org.opensih.Test;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.opensih.ControladoresCU.IMantenimiento;
import org.opensih.ControladoresCU.IPacienteCtrl;
import org.opensih.ControladoresCU.IPlant;
import org.opensih.ControladoresCU.IPuntaje;
import org.opensih.JMX.InvocadorService2;
import org.opensih.JMX.InvocadorServiceBean2;
import org.opensih.Modelo.Cie10;
import org.opensih.Modelo.Cie9;
import org.opensih.Modelo.DocClin;
import org.opensih.Modelo.Paciente;
import org.opensih.Modelo.Participacion;
import org.opensih.Modelo.Plantilla;
import org.opensih.Modelo.Sala;
import org.opensih.Modelo.SaqAsse;
import org.opensih.Modelo.Seccion;
import org.opensih.Modelo.Servicio;
import org.opensih.Modelo.Tabla_Pago;
import org.opensih.Modelo.TecnicoMedico;
import org.opensih.Modelo.UnidadEjecutora;
import org.opensih.Seguridad.IUsuarios;
import org.opensih.Seguridad.IUtils;
import org.opensih.Seguridad.Usuario;
import org.opensih.Test.webservices.ejb.InterfaceInvocadorMigr;
import org.opensih.Utils.Constantes.ActStatusVoc;
import org.opensih.Utils.Constantes.ParticipationFunctionVoc;
import org.opensih.Utils.Converters.Encoder;
import org.opensih.Utils.Converters.UEConverter;
import com.novell.ldap.LDAPConnection;

@Stateful
@Name("migrador")
public class Migrador implements IMigrador {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @EJB
    IMantenimiento logica;

    @EJB
    IUsuarios usuarios;

    @EJB
    IUtils utils;

    @EJB
    IPuntaje logicaCP;

    @EJB
    IPacienteCtrl logicaPac;

    @EJB
    InterfaceInvocadorMigr migr;

    @EJB
    IPlant logicaP;

    UnidadEjecutora ue;

    List<UnidadEjecutora> ues;

    Map<String, UnidadEjecutora> uesMap;

    String idDoc;

    String i, j;

    @Create
    @Begin(join = true)
    public void inicio() {
        ues = logica.listarUnidades();
        Map<String, UnidadEjecutora> results2 = new TreeMap<String, UnidadEjecutora>();
        for (UnidadEjecutora p : ues) {
            String nom = p.toString();
            results2.put(nom, p);
        }
        ue = null;
        uesMap = results2;
    }

    public void migracionUsuarios() {
        if (ue != null) {
            List<String> res = migr.usuarios();
            int i = 1;
            InvocadorService2 inv = InvocadorServiceBean2.getInstance();
            String ldapHost = inv.getLdap_Host();
            String password = inv.getLdap_Password();
            int ldapPort = LDAPConnection.DEFAULT_PORT;
            int ldapVersion = LDAPConnection.LDAP_V3;
            String loginDN = "cn=Manager,dc=usuariosDescOp";
            try {
                LDAPConnection lc = new LDAPConnection();
                lc.connect(ldapHost, ldapPort);
                lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
                int correctos = 0, incorrectos = 0;
                for (String s : res) {
                    if (crearUsuario(s, lc)) {
                        correctos++;
                    } else incorrectos++;
                    System.out.println("Usuario:" + i);
                    i++;
                }
                System.out.println("RESULTADOS DE LA MIGRACION DE USUARIOS");
                System.out.println("CORRECTOS:" + correctos);
                System.out.println("INCORRECTOS:" + incorrectos);
                i--;
                System.out.println("TOTAL:" + i);
                FacesMessages.instance().add("FINALIZO MIGRACION DE USUARIOS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
                lc.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarDocs() {
        if (ue != null) {
            List<String> res = migr.docClins(Integer.parseInt(i), Integer.parseInt(i) + 99);
            int x = Integer.parseInt(i);
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearDesc(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("DescOP:" + x);
                x++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE DOCUMENTOS CLINICOS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            x = x - Integer.parseInt(i);
            System.out.println("TOTAL:" + x);
            FacesMessages.instance().add("FINALIZO MIGRACION DE DOCUMENTOS CLINICOS DESDE:" + i + " HASTA: " + Integer.parseInt(i) + 99 + ". CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + x);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarDoc() {
        if (ue != null) {
            String res = migr.doc(idDoc);
            if (crearDesc(res)) {
                System.out.println("RESULTADO CORRECTO");
            } else {
                System.out.println("INCORRECTOS");
            }
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarPlantillas() {
        if (ue != null) {
            List<String> res = migr.plantillas();
            int i = 1;
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearPlant(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("Plantilla:" + i);
                i++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE PLANTILLAS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            i--;
            System.out.println("TOTAL:" + i);
            FacesMessages.instance().add("FINALIZO MIGRACION DE PLANTILLAS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarJefes() {
        if (ue != null) {
            List<String> res = migr.jefes();
            int i = 1;
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearTablaPago(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("TablaPago:" + i);
                i++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE PAGOS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            i--;
            System.out.println("TOTAL:" + i);
            FacesMessages.instance().add("FINALIZO MIGRACION DE PAGOS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarTecnicos() {
        if (ue != null) {
            List<String> res = migr.tecnicos();
            int i = 1;
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearTecnico(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("Tecnico:" + i);
                i++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE TECNICOS MEDICOS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            i--;
            System.out.println("TOTAL:" + i);
            FacesMessages.instance().add("FINALIZO MIGRACION DE TECNICOS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarSalas() {
        if (ue != null) {
            List<String> res = migr.salas();
            int i = 1;
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearSala(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("Sala:" + i);
                i++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE SALAS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            i--;
            System.out.println("TOTAL:" + i);
            FacesMessages.instance().add("FINALIZO MIGRACION DE SALAS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void migrarServicios() {
        if (ue != null) {
            List<String> res = migr.servicios();
            int i = 1;
            int correctos = 0, incorrectos = 0;
            for (String s : res) {
                if (crearServicio(s)) {
                    correctos++;
                } else incorrectos++;
                System.out.println("Servicio:" + i);
                i++;
            }
            System.out.println("RESULTADOS DE LA MIGRACION DE SERVICIOS");
            System.out.println("CORRECTOS:" + correctos);
            System.out.println("INCORRECTOS:" + incorrectos);
            i--;
            System.out.println("TOTAL:" + i);
            FacesMessages.instance().add("FINALIZO MIGRACION DE SERVICIOS. CORRECTOS:" + correctos + ". INCORRECTOS:" + incorrectos + ". TOTAL:" + i);
        } else {
            FacesMessages.instance().add("Seleccionar Unidad Ejecutora.");
        }
    }

    public void control() {
        try {
            String ue = migr.controlUE();
            FacesMessages.instance().add("Esta conectado correctamente con la maquina virtual del " + ue);
        } catch (Exception e) {
            FacesMessages.instance().add("Problemas con el Web Services. Comprobar la " + "configuracion en JMX de la IP de la version 1.");
        }
    }

    @SuppressWarnings("unchecked")
    private boolean crearUsuario(String user, LDAPConnection lc) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(user));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            Usuario u = new Usuario();
            String ci = raiz.getChild("ci", ns).getText();
            u.setNombre((raiz.getChild("nombre", ns).getText()));
            u.setApellido((raiz.getChild("apellido", ns).getText()));
            u.setCi((ci));
            u.setPassword(raiz.getChild("password", ns).getText());
            if (raiz.getChild("habilitado", ns).getText().equals("SI")) u.setEstaHabilitado(true); else u.setEstaHabilitado(false);
            List<Element> roles = raiz.getChildren("privilegio", ns);
            List<String> roles2 = new LinkedList<String>();
            String roles3 = "";
            for (Element r : roles) {
                String rol = (r.getText());
                roles3 += rol;
                rol = rol.equals("SuperAdmin") ? "superadmin" : rol.equals("Admin") ? "admin" : rol.equals("Administrativo") ? "" : rol.equals("TecnicoMedico") ? "tecnicoMedico" : rol.equals("JefeServicio") ? "tecnicoMedico" : rol.equals("Basico") ? "" : "";
                if (!rol.equals("") && !roles2.contains(rol)) roles2.add(rol);
            }
            u.setRoles(roles2);
            String hab = "";
            if (u.isEstaHabilitado()) hab = "SI"; else hab = "NO";
            String checksum = utils.MD5(u.getCi() + u.getNombre() + hab + u.getPassword() + u.getApellido() + roles3);
            if (checksum.equals(raiz.getChild("checksum", ns).getText())) {
                Usuario aux = usuarios.obtenerUsuario(ci, ue.getCodigo(), lc);
                if (!roles2.isEmpty() && aux == null) {
                    usuarios.persistirUsuario(u, lc);
                    usuarios.asociarUsuario(u, ue.getCodigo(), lc);
                } else if (!roles2.isEmpty()) {
                    u.getRoles().addAll(aux.getRoles());
                    usuarios.modificarDatosUsuario(u, lc);
                    usuarios.mergearAsociacionUsuario(u, ue.getCodigo(), lc);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean crearDesc(String cda) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(cda));
            DocClin d = new DocClin();
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            d.setVersion(1);
            d.setEstado(ActStatusVoc.Completed);
            d.setID(raiz.getChild("id", ns).getAttributeValue("extension"));
            d.setSetID(d.getID());
            d.setUe(ue);
            d.setFecha(sdf.parse(raiz.getChild("effectiveTime", ns).getAttributeValue("value")));
            Element fechas = raiz.getChild("documentationOf", ns).getChild("serviceEvent", ns).getChild("effectiveTime", ns);
            d.setFini(sdf.parse(fechas.getChild("low", ns).getAttributeValue("value")));
            d.setFfin(sdf.parse(fechas.getChild("high", ns).getAttributeValue("value")));
            String susp = raiz.getChild("estado", ns).getText();
            if (susp.equals("completed")) d.setSuspendida("NO"); else d.setSuspendida("SI");
            Element pat = raiz.getChild("recordTarget", ns).getChild("patientRole", ns);
            String idPac = pat.getAttributeValue("root") + "_" + pat.getAttributeValue("extension");
            Paciente pac = em.find(Paciente.class, idPac);
            if (pac == null) {
                String ext = pat.getChild("id", ns).getAttributeValue("extension");
                String root = pat.getChild("id", ns).getAttributeValue("root");
                pac = logicaPac.buscarPaciente(ext, root);
                if (pac == null) {
                    pac = new Paciente();
                    pac.setRoot_extension(root + "_" + ext);
                }
            }
            d.setPaciente(pac);
            Element autor = raiz.getChild("author", ns).getChild("assignedAuthor", ns);
            List<Element> id = autor.getChildren("id", ns);
            String root_ext = id.get(1).getAttributeValue("root") + "_" + id.get(1).getAttributeValue("extension");
            TecnicoMedico tec = logica.buscarTecnico(root_ext);
            d.setAutor(tec);
            Element serv = autor.getChild("representedOrganization", ns);
            Servicio s = logica.buscarServ(serv.getChild("id", ns).getAttributeValue("extension").trim(), ue.getCodigo());
            d.setServicio(s);
            Element resp = raiz.getChild("legalAuthenticator", ns).getChild("assignedEntity", ns);
            id = resp.getChildren("id", ns);
            root_ext = id.get(1).getAttributeValue("root") + "_" + id.get(1).getAttributeValue("extension");
            tec = logica.buscarTecnico(root_ext);
            Participacion p = new Participacion();
            String orgResp = raiz.getChild("legalAuthenticator", ns).getChild("functionCode", ns).getAttributeValue("code");
            if (orgResp.equals("Fac. Medicina")) {
                orgResp = "Fac Medicina/Residente";
            }
            p.setOrigen(orgResp);
            p.setTecnico(tec);
            d.setResponsable(p);
            List<Participacion> parts = new LinkedList<Participacion>();
            List<Element> perfs = raiz.getChild("documentationOf", ns).getChild("serviceEvent", ns).getChildren("performer", ns);
            for (Element e : perfs) {
                Element part = e.getChild("assignedEntity", ns);
                id = part.getChildren("id", ns);
                root_ext = id.get(1).getAttributeValue("root") + "_" + id.get(1).getAttributeValue("extension");
                tec = logica.buscarTecnico(root_ext);
                p = new Participacion();
                String code = e.getChild("functionCode", ns).getAttributeValue("code");
                String orgPart = code.split("_")[1];
                if (orgPart.equals("Fac. Medicina")) {
                    orgPart = "Fac Medicina/Residente";
                }
                p.setOrigen(orgPart);
                p.setFuncion(ParticipationFunctionVoc.crear(code.split("_")[0]));
                p.setTecnico(tec);
                parts.add(p);
            }
            d.setParticipantes(parts);
            String Categoria = "";
            String Oport_Proc = "";
            String Reintervencion = "";
            String TipoAnestesia = "";
            String Asa = "";
            String Conversion = "";
            List<String> dpo = new LinkedList<String>();
            List<String> dop = new LinkedList<String>();
            List<String> pp = new LinkedList<String>();
            List<String> ir = new LinkedList<String>();
            String texto = "";
            List<String> procedimientos = new LinkedList<String>();
            List<String> medicaciones = new LinkedList<String>();
            List<String> muestras = new LinkedList<String>();
            List<Element> items;
            String sutura = "";
            String material = "";
            String complicaciones = "";
            String posicion = "", posicion2 = "";
            String desinfco = "", desinfco2 = "";
            String rmb = "", egreso = "", grdcont = "";
            String sala_cama = raiz.getChild("componentOf", ns).getChild("encompassingEncounter", ns).getChild("location", ns).getChild("healthCareFacility", ns).getChild("location", ns).getChild("name", ns).getText();
            List<Element> components = raiz.getChild("component", ns).getChild("structuredBody", ns).getChildren("component", ns);
            for (Element e : components) {
                Element section = e.getChild("section", ns);
                String titulo = section.getChild("title", ns).getText();
                if (titulo.equals("Informacion General")) {
                    items = section.getChild("text", ns).getChild("table", ns).getChild("tbody", ns).getChildren("tr", ns);
                    for (Element e2 : items) {
                        if (e2.getChild("th", ns).getText().equals("Categoria")) {
                            Categoria = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("Oportunidad")) {
                            Oport_Proc = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("Reintervencion")) {
                            Reintervencion = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("TipoAnestesia")) {
                            TipoAnestesia = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("Asa")) {
                            Asa = e2.getChild("td", ns).getText();
                            if (Asa.equals("0")) {
                                Asa = "S/C";
                            }
                        } else if (e2.getChild("th", ns).getText().equals("Conversion")) {
                            Conversion = e2.getChild("td", ns).getText();
                        }
                    }
                } else if (titulo.equals("Diagnosticos Pre Operatorios")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        dpo.add(agregarDiag(e2.getText()));
                    }
                } else if (titulo.equals("Diagnosticos Operatorios")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        dop.add(agregarDiag(e2.getText()));
                    }
                } else if (titulo.equals("Procedimientos Propuestos")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        pp.add(agregarProc(e2.getText()));
                    }
                } else if (titulo.equals("Procedimientos Realizados")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        ir.add(agregarProc(e2.getText()));
                    }
                } else if (titulo.equals("Descripcion Operatoria")) {
                    texto = section.getChild("text", ns).getText();
                } else if (titulo.equals("Complicaciones o eventos adversos intra-operatorios")) {
                    complicaciones = section.getChild("text", ns).getText();
                } else if (titulo.equals("Procedimientos Intra-Operatorios no Quirurgicos")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        procedimientos.add(e2.getText().trim());
                    }
                    if (procedimientos.isEmpty()) {
                        procedimientos.add("NO");
                    }
                } else if (titulo.equals("Medicacion en block")) {
                    items = section.getChild("text", ns).getChild("list", ns).getChildren("item", ns);
                    for (Element e2 : items) {
                        medicaciones.add(e2.getText().trim());
                    }
                    if (medicaciones.isEmpty()) {
                        medicaciones.add("NO");
                    }
                } else if (titulo.equals("Muestras a Analizar")) {
                    items = section.getChild("text", ns).getChild("table", ns).getChild("tbody", ns).getChildren("tr", ns);
                    for (Element e2 : items) {
                        String tipo = e2.getChild("th", ns).getText();
                        String valor = e2.getChild("td", ns).getText();
                        if (valor.contains("SI - Tipo de pieza:")) {
                            valor = valor.substring("SI - Tipo de pieza:".length()).trim();
                            muestras.add(tipo + ": " + valor);
                        } else if (valor.contains("SI - Tipo de muestra:")) {
                            valor = valor.substring("SI - Tipo de muestra:".length()).trim();
                            muestras.add(tipo + ": " + valor);
                        }
                    }
                    if (muestras.isEmpty()) {
                        muestras.add("NO");
                    }
                } else if (titulo.equals("Posicion del Paciente")) {
                    posicion = section.getChild("text", ns).getText().trim();
                    if (posicion.startsWith("No Especificada")) {
                        posicion2 = posicion.substring("No Especificada".length()).trim();
                        posicion = "No Especificada";
                    } else if (posicion.startsWith("Decubito Antitren")) {
                        posicion2 = posicion.substring("Decubito Antitren".length()).trim();
                        posicion = "Decubito Antitrendelemburg";
                    } else if (posicion.startsWith("Decubito Dorsal")) {
                        posicion2 = posicion.substring("Decubito Dorsal".length()).trim();
                        posicion = "Decubito Dorsal";
                    } else if (posicion.startsWith("Decubito Trendelemburg")) {
                        posicion2 = posicion.substring("Decubito Trendelemburg".length()).trim();
                        posicion = "Decubito Trendelemburg";
                    } else if (posicion.startsWith("Decubito Ventral")) {
                        posicion2 = posicion.substring("Decubito Ventral".length()).trim();
                        posicion = "Decubito Ventral";
                    } else if (posicion.startsWith("En banco de plaza")) {
                        posicion2 = posicion.substring("En banco de plaza".length()).trim();
                        posicion = "En banco de plaza";
                    } else if (posicion.startsWith("Ginecologica")) {
                        posicion2 = posicion.substring("Ginecologica".length()).trim();
                        posicion = "Ginecologica";
                    } else if (posicion.startsWith("Lateral")) {
                        posicion2 = posicion.substring("Lateral".length()).trim();
                        posicion = "Lateral";
                    } else if (posicion.startsWith("Lloys Davies")) {
                        posicion2 = posicion.substring("Lloys Davies".length()).trim();
                        posicion = "Lloys Davies";
                    } else if (posicion.startsWith("Semisentado")) {
                        posicion2 = posicion.substring("Semisentado".length()).trim();
                        posicion = "Semisentado";
                    } else if (posicion.startsWith("Sentado")) {
                        posicion2 = posicion.substring("Sentado".length()).trim();
                        posicion = "Sentado";
                    } else if (posicion.startsWith("Sims")) {
                        posicion2 = posicion.substring("Sims".length()).trim();
                        posicion = "Sims";
                    } else if (posicion.startsWith("Otra")) {
                        posicion2 = posicion.substring("Otra".length()).trim();
                        posicion = "Otra";
                    } else {
                        posicion2 = posicion;
                        posicion = "Otra";
                    }
                } else if (titulo.equals("Producto utilizado para la desinfeccion del Campo Operatorio")) {
                    desinfco = section.getChild("text", ns).getText().trim();
                    if (desinfco.startsWith("No Especificado")) {
                        desinfco2 = desinfco.substring("No Especificado".length()).trim();
                        desinfco = "No Especificado";
                    } else if (desinfco.startsWith("Alcohol")) {
                        desinfco2 = desinfco.substring("Alcohol".length()).trim();
                        desinfco = "Alcohol";
                    } else if (desinfco.startsWith("Alcohol y Clorhexidina")) {
                        desinfco2 = desinfco.substring("Alcohol y Clorhexidina".length()).trim();
                        desinfco = "Alcohol y Clorhexidina";
                    } else if (desinfco.startsWith("Clorhexidina")) {
                        desinfco2 = desinfco.substring("Clorhexidina".length()).trim();
                        desinfco = "Clorhexidina";
                    } else if (desinfco.startsWith("Yodofon")) {
                        desinfco2 = desinfco.substring("Yodofon".length()).trim();
                        desinfco = "Yodofon";
                    } else if (desinfco.startsWith("Alcohol + Yodo")) {
                        desinfco2 = desinfco.substring("Alcohol + Yodo".length()).trim();
                        desinfco = "Alcohol + Yodo";
                    } else if (desinfco.startsWith("Otro")) {
                        desinfco2 = desinfco.substring("Otro".length()).trim();
                        desinfco = "Otro";
                    } else {
                        desinfco2 = desinfco;
                        desinfco = "Otro";
                    }
                } else if (titulo.equals("Utilizacion de Sutura Mecanica")) {
                    sutura = section.getChild("text", ns).getText();
                    if (sutura.length() > 5) {
                        sutura = sutura.substring(5);
                    } else if (sutura.length() == 5) {
                        sutura = "";
                    }
                } else if (titulo.equals("Utilizacion de Material Protesico")) {
                    material = section.getChild("text", ns).getText();
                    if (material.length() > 5) {
                        material = material.substring(5);
                    } else if (material.length() == 5) {
                        material = "";
                    }
                } else if (titulo.equals("Otros")) {
                    items = section.getChild("text", ns).getChild("table", ns).getChild("tbody", ns).getChildren("tr", ns);
                    for (Element e2 : items) {
                        if (e2.getChild("th", ns).getText().equals("RecMatBlanco")) {
                            rmb = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("EgresoBQ")) {
                            egreso = e2.getChild("td", ns).getText();
                        } else if (e2.getChild("th", ns).getText().equals("GradCont")) {
                            grdcont = e2.getChild("td", ns).getText();
                        }
                    }
                }
            }
            String generalidad = "";
            if (d.esSuspendida()) generalidad = "<root>" + "<Oportunidad>" + Encoder.parseXML(Oport_Proc.split(" - ")[0]) + "</Oportunidad>" + "<Coordinacion>" + Encoder.parseXML(((Oport_Proc.split(" - ").length > 1) ? Oport_Proc.split(" - ")[1] : "")) + "<SalaInternacion name=\"" + Encoder.parseXML(((sala_cama.split("_").length > 0) ? sala_cama.split("_")[0] : "")) + "\">" + "</SalaInternacion>" + "<Cama>" + Encoder.parseXML(((sala_cama.split("_").length > 1) ? sala_cama.split("_")[1] : "")) + "</Cama>" + "</Coordinacion>" + "<Block name=\"" + "" + "\">" + "</Block>" + "<SalaBlock>" + "</SalaBlock>" + "<Reintervencion>" + Encoder.parseXML(Reintervencion) + "</Reintervencion>" + "<Anestesia>" + "</Anestesia>" + "<Asa>" + "</Asa>" + "<Categoria/>" + "</root>"; else {
                generalidad = "<root>" + "<Oportunidad>" + Encoder.parseXML(Oport_Proc.split(" - ")[0]) + "</Oportunidad>" + "<Coordinacion>" + Encoder.parseXML(((Oport_Proc.split(" - ").length > 1) ? Oport_Proc.split(" - ")[1] : "")) + "<SalaInternacion name=\"" + Encoder.parseXML(((sala_cama.split("_").length > 0) ? sala_cama.split("_")[0] : "")) + "\">" + "</SalaInternacion>" + "<Cama>" + Encoder.parseXML(((sala_cama.split("_").length > 1) ? sala_cama.split("_")[1] : "")) + "</Cama>" + "</Coordinacion>" + "<Block name=\"" + "" + "\">" + "</Block>" + "<SalaBlock>" + "</SalaBlock>" + "<Reintervencion>" + Encoder.parseXML(Reintervencion) + "</Reintervencion>" + "<Anestesia>" + Encoder.parseXML(TipoAnestesia) + "</Anestesia>" + "<Asa>" + Encoder.parseXML(Asa) + "</Asa>" + "</root>";
            }
            Seccion sec = new Seccion();
            sec.setTexto(generalidad);
            sec.setTitulo("Generalidades");
            d.setGeneralidades(sec);
            String diagProc = "";
            if (d.esSuspendida()) diagProc = "<root>" + "<Diagnosticos>" + ((!dpo.isEmpty()) ? listarCods(dpo) : "") + "</Diagnosticos>" + "<Procedimientos>" + ((!pp.isEmpty()) ? listarCods(pp) : "") + "</Procedimientos>" + "<Conversion>" + "<Especificacion dato=\"\">" + "</Especificacion>" + "</Conversion>" + "<Categoria/>" + "</root>"; else {
                diagProc = "<root>" + "<Diagnosticos>" + ((!dpo.isEmpty()) ? listarCods(dpo) : "") + ((!dop.isEmpty()) ? listarCods2(dop) : "") + "</Diagnosticos>" + "<Procedimientos>" + ((!pp.isEmpty()) ? listarCods(pp) : "") + ((!ir.isEmpty()) ? listarCods2(ir) : "") + "</Procedimientos>" + "<Conversion>" + Encoder.parseXML(Conversion) + "<Especificacion dato=\"\">" + "</Especificacion>" + "</Conversion>" + "<Categoria>" + Encoder.parseXML(Categoria) + "</Categoria>" + "</root>";
            }
            Seccion sec2 = new Seccion();
            sec2.setTexto(diagProc);
            sec2.setTitulo("Diagnosticos y Procedimientos");
            d.setDiagnosticosProcedimientos(sec2);
            String descop = "<root>" + "<Texto>" + Encoder.parseXML(texto) + "</Texto>" + "</root>";
            Seccion sec3 = new Seccion();
            sec3.setTexto(descop);
            sec3.setTitulo("Descripcion Operatoria");
            d.setDescripcionOperatoria(sec3);
            String masInfo = "";
            if (d.esSuspendida()) {
                masInfo = "<root>" + "<Pdco/>" + "<Posicion/>" + "<Complicaciones/>" + "<Sutura/>" + "<MaterialP/>" + "<proceNoQuirug/>" + "<medicBlock/>" + "<muestras/>" + "<Rmb/>" + "<Egreso/>" + "<GrdCont/>" + "</root>";
            } else {
                masInfo = "<root>" + "<Pdco prod=\"" + Encoder.parseXML(desinfco) + "\" esp=\"" + Encoder.parseXML(desinfco2) + "\" />" + "<Posicion pos=\"" + Encoder.parseXML(posicion) + "\" esp=\"" + Encoder.parseXML(posicion2) + "\" />" + "<Complicaciones>" + Encoder.parseXML(complicaciones) + "</Complicaciones>" + "<Sutura>" + Encoder.parseXML(sutura) + "</Sutura>" + "<MaterialP>" + Encoder.parseXML(material) + "</MaterialP>" + "<proceNoQuirug>" + listarProc(procedimientos) + "</proceNoQuirug>" + "<medicBlock>" + listarMedB(medicaciones) + "</medicBlock>" + "<muestras>" + listarMuestras(muestras) + "</muestras>" + "<Rmb>" + Encoder.parseXML(rmb) + "</Rmb>" + "<Egreso>" + Encoder.parseXML(egreso.substring(2, egreso.length())) + "</Egreso>" + "<GrdCont>" + Encoder.parseXML(grdcont) + "</GrdCont>" + "</root>";
            }
            Seccion sec4 = new Seccion();
            sec4.setTexto(masInfo);
            sec4.setTitulo("Mas Informacion");
            d.setMasInfo(sec4);
            DocClin aux = em.find(DocClin.class, d.getID());
            if (aux != null) {
                d.setID(d.getID() + "000");
                d.setSetID(d.getSetID() + "000");
                System.out.println("SE REPITE ID DE DESCRIPCION");
            }
            if (aux == null || !aux.getUe().getCodigo().equals(d.getUe().getCodigo())) {
                em.merge(d);
                em.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean crearPlant(String plant) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(plant));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            Plantilla p = logicaP.buscarPlantilla(raiz.getChild("autor", ns).getText(), raiz.getChild("nombre", ns).getText());
            if (p == null) {
                p = new Plantilla();
            }
            p.setNombre((raiz.getChild("nombre", ns).getText()));
            p.setTexto((raiz.getChild("texto", ns).getText()));
            TecnicoMedico tec = logica.buscarTecnico2((raiz.getChild("autor", ns).getText()));
            if (tec != null) {
                p.setTecn(tec);
                em.merge(p);
                em.flush();
                return true;
            } else return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean crearTablaPago(String jefe) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(jefe));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            List<Tabla_Pago> ts = em.createQuery("SELECT t FROM Tabla_Pago t WHERE t.idDoc=:id").setParameter("id", raiz.getChild("IdDoc", ns).getText()).getResultList();
            if (ts.isEmpty() || !ts.get(0).getUe().equals(ue.getNombre())) {
                Tabla_Pago t = new Tabla_Pago();
                t.setAsa((raiz.getChild("Asa", ns).getText()));
                if (!raiz.getChild("CiAn1", ns).getText().equals("")) t.setCiAn1((raiz.getChild("CiAn1", ns).getText())); else t.setCiAn1(null);
                if (!raiz.getChild("CiAn2", ns).getText().equals("")) t.setCiAn2((raiz.getChild("CiAn2", ns).getText())); else t.setCiAn2(null);
                if (!raiz.getChild("CiAy1", ns).getText().equals("")) t.setCiAy1((raiz.getChild("CiAy1", ns).getText())); else t.setCiAy1(null);
                if (!raiz.getChild("CiAy2", ns).getText().equals("")) t.setCiAy2((raiz.getChild("CiAy2", ns).getText())); else t.setCiAy2(null);
                if (!raiz.getChild("CiAy3", ns).getText().equals("")) t.setCiAy3((raiz.getChild("CiAy3", ns).getText())); else t.setCiAy3(null);
                if (!raiz.getChild("CiCir1", ns).getText().equals("")) t.setCiCir1((raiz.getChild("CiCir1", ns).getText())); else t.setCiCir1(null);
                if (!raiz.getChild("CiCir2", ns).getText().equals("")) t.setCiCir2((raiz.getChild("CiCir2", ns).getText())); else t.setCiCir2(null);
                if (!raiz.getChild("CiJefe", ns).getText().equals("")) t.setCiJefe((raiz.getChild("CiJefe", ns).getText())); else t.setCiJefe(null);
                if (!raiz.getChild("CiJefeAn", ns).getText().equals("")) t.setCiJefeAn((raiz.getChild("CiJefeAn", ns).getText())); else t.setCiJefeAn(null);
                if (!raiz.getChild("CiPaciente", ns).getText().equals("")) t.setCiPaciente((raiz.getChild("CiPaciente", ns).getText())); else t.setCiPaciente(null);
                if (!raiz.getChild("CiResp", ns).getText().equals("")) t.setCiResp((raiz.getChild("CiResp", ns).getText())); else t.setCiResp(null);
                t.setCodigo((raiz.getChild("Codigo", ns).getText()));
                t.setFinCirugia(sdf.parse(raiz.getChild("FinCirugia", ns).getText()));
                if (!ts.isEmpty()) t.setIdDoc((raiz.getChild("IdDoc", ns).getText()) + "000"); else t.setIdDoc((raiz.getChild("IdDoc", ns).getText()));
                t.setInicioCirugia(sdf.parse(raiz.getChild("InicioCirugia", ns).getText()));
                if (!raiz.getChild("NombreAn1", ns).getText().equals("")) t.setNombreAn1((raiz.getChild("NombreAn1", ns).getText())); else t.setNombreAn1(null);
                if (!raiz.getChild("NombreAn2", ns).getText().equals("")) t.setNombreAn2((raiz.getChild("NombreAn2", ns).getText())); else t.setNombreAn2(null);
                if (!raiz.getChild("NombreAy1", ns).getText().equals("")) t.setNombreAy1((raiz.getChild("NombreAy1", ns).getText())); else t.setNombreAy1(null);
                if (!raiz.getChild("NombreAy2", ns).getText().equals("")) t.setNombreAy2((raiz.getChild("NombreAy2", ns).getText())); else t.setNombreAy2(null);
                if (!raiz.getChild("NombreAy3", ns).getText().equals("")) t.setNombreAy3((raiz.getChild("NombreAy3", ns).getText())); else t.setNombreAy3(null);
                if (!raiz.getChild("NombreCir1", ns).getText().equals("")) t.setNombreCir1((raiz.getChild("NombreCir1", ns).getText())); else t.setNombreCir1(null);
                if (!raiz.getChild("NombreCir2", ns).getText().equals("")) t.setNombreCir2((raiz.getChild("NombreCir2", ns).getText())); else t.setNombreCir2(null);
                if (!raiz.getChild("NombreJefe", ns).getText().equals("")) t.setNombreJefe((raiz.getChild("NombreJefe", ns).getText())); else t.setNombreJefe(null);
                if (!raiz.getChild("NombreJefeAn", ns).getText().equals("")) t.setNombreJefeAn((raiz.getChild("NombreJefeAn", ns).getText())); else t.setNombreJefeAn(null);
                if (!raiz.getChild("NombrePaciente", ns).getText().equals("")) t.setNombrePaciente((raiz.getChild("NombrePaciente", ns).getText())); else t.setNombrePaciente(null);
                if (!raiz.getChild("NombreResp", ns).getText().equals("")) t.setNombreResp((raiz.getChild("NombreResp", ns).getText())); else t.setNombreResp(null);
                t.setOtros((raiz.getChild("Otros", ns).getText()));
                t.setPtosAnestesista(Double.parseDouble(raiz.getChild("PtosAnestesista", ns).getText()));
                t.setPtosAyudante(Double.parseDouble(raiz.getChild("PtosAyudante", ns).getText()));
                t.setPtosCirujano(Double.parseDouble(raiz.getChild("PtosCirujano", ns).getText()));
                t.setServicio((raiz.getChild("Servicio", ns).getText()));
                t.setUe(ue.getNombre());
                String checksum = utils.MD5(t.getAsa() + ((t.getCiAn1() == null) ? "" : t.getCiAn1()) + ((t.getCiAn2() == null) ? "" : t.getCiAn2()) + ((t.getCiAy1() == null) ? "" : t.getCiAy1()) + ((t.getCiAy2() == null) ? "" : t.getCiAy2()) + ((t.getCiAy3() == null) ? "" : t.getCiAy3()) + ((t.getCiCir1() == null) ? "" : t.getCiCir1()) + ((t.getCiCir2() == null) ? "" : t.getCiCir2()) + ((t.getCiJefe() == null) ? "" : t.getCiJefe()) + ((t.getCiJefeAn() == null) ? "" : t.getCiJefeAn()) + ((t.getCiPaciente() == null) ? "" : t.getCiPaciente()) + ((t.getCiResp() == null) ? "" : t.getCiResp()) + t.getCodigo() + (raiz.getChild("IdDoc", ns).getText()) + ((t.getNombreAn1() == null) ? "" : t.getNombreAn1()) + ((t.getNombreAn2() == null) ? "" : t.getNombreAn2()) + ((t.getNombreAy1() == null) ? "" : t.getNombreAy1()) + ((t.getNombreAy2() == null) ? "" : t.getNombreAy2()) + ((t.getNombreAy3() == null) ? "" : t.getNombreAy3()) + ((t.getNombreCir1() == null) ? "" : t.getNombreCir1()) + ((t.getNombreCir2() == null) ? "" : t.getNombreCir2()) + ((t.getNombreJefe() == null) ? "" : t.getNombreJefe()) + ((t.getNombreJefeAn() == null) ? "" : t.getNombreJefeAn()) + ((t.getNombrePaciente() == null) ? "" : t.getNombrePaciente()) + ((t.getNombreResp() == null) ? "" : t.getNombreResp()) + t.getOtros() + t.getPtosAnestesista() + t.getPtosAyudante() + t.getPtosCirujano() + t.getServicio() + sdf.format(t.getFinCirugia()) + sdf.format(t.getInicioCirugia()));
                if (checksum.equals(raiz.getChild("checksum", ns).getText())) {
                    em.merge(t);
                    em.flush();
                    return true;
                } else {
                    System.out.println("ERROR: nro: " + t.getIdDoc());
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean crearTecnico(String tecnico) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(tecnico));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            TecnicoMedico tec = logica.buscarTecnico((raiz.getChild("ci", ns).getText()));
            if (tec != null) {
                if (!ue.getTecnicosMedicos().contains(tec)) {
                    if (raiz.getChild("habilitado", ns).getText().equals("SI")) {
                        ue.getTecnicosMedicos().add(tec);
                        em.merge(ue);
                        em.flush();
                    }
                }
                return true;
            } else {
                tec = new TecnicoMedico();
                tec.setRoot_extension((raiz.getChild("ci", ns).getText()));
                tec.setNombre((raiz.getChild("nombre", ns).getText()));
                tec.setApellido((raiz.getChild("apellido", ns).getText()));
                tec.setRol((raiz.getChild("rol", ns).getText()));
                tec.setNro_caja((raiz.getChild("caja", ns).getText()));
                String hab = "NO";
                if (raiz.getChild("habilitado", ns).getText().equals("SI")) {
                    ue.getTecnicosMedicos().add(tec);
                    hab = "SI";
                }
                String checksum = utils.MD5(tec.getNombre() + hab + tec.getRol() + tec.getNro_caja() + tec.getApellido() + tec.getRoot_extension());
                if (checksum.equals(raiz.getChild("checksum", ns).getText())) {
                    if (raiz.getChild("habilitado", ns).getText().equals("SI")) em.merge(ue); else em.merge(tec);
                    em.flush();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean crearServicio(String servicio) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(servicio));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            Servicio s = logica.buscarServ((raiz.getChild("nombre", ns).getText().trim()), ue.getCodigo());
            if (s == null) {
                s = new Servicio();
                s.setNombre((raiz.getChild("nombre", ns).getText().trim()));
                s.setUe(ue);
                s.setHabilitado(false);
                if ((raiz.getChild("jefe", ns).getText()) != null && !(raiz.getChild("jefe", ns).getText()).equals("")) {
                    TecnicoMedico tec = logica.buscarTecnico2((raiz.getChild("jefe", ns).getText()));
                    s.setJefe(tec);
                }
                if (raiz.getChild("habilitado", ns).getText().equals("SI")) s.setHabilitado(true);
                String checksum = utils.MD5(((s.getJefe() == null) ? "" : s.getJefe().getExtension()) + s.getNombre());
                if (checksum.equals(raiz.getChild("checksum", ns).getText())) {
                    em.merge(s);
                    em.flush();
                    return true;
                }
            } else return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean crearSala(String sala) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(sala));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            boolean existe = logica.existeSala(raiz.getChild("nombre", ns).getText(), ue.getCodigo());
            if (!existe) {
                Sala s = new Sala();
                s.setNombre((raiz.getChild("nombre", ns).getText()));
                s.setUe(ue);
                String checksum = utils.MD5(s.getNombre());
                if (checksum.equals(raiz.getChild("checksum", ns).getText())) {
                    em.merge(s);
                    em.flush();
                    return true;
                }
            } else return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Converter getConverterUe() {
        return new UEConverter(ues);
    }

    public Map<String, UnidadEjecutora> getUes() {
        return uesMap;
    }

    public void seteo() {
    }

    @Destroy
    @Remove
    public void fin() {
    }

    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String listarCods(List<String> cods) {
        String s = "";
        for (String aux : cods) s += "<pre" + codeToXML(aux) + " />";
        return s;
    }

    public String listarCods2(List<String> cods) {
        String s = "";
        for (String aux : cods) s += "<post" + codeToXML(aux) + " />";
        return s;
    }

    public String codeToXML(String cod) {
        String a[] = cod.split("\\|");
        if (a.length == 1) return " ampliacion=\"" + Encoder.parseXML(a[0].trim()) + "\""; else if (a.length == 2) return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\""; else return " code=\"" + Encoder.parseXML(a[0].trim()) + "\"" + " descripcion=\"" + Encoder.parseXML(a[1].trim()) + "\"" + " ampliacion=\"" + Encoder.parseXML(a[2].trim()) + "\"";
    }

    public String listarProc(List<String> list) {
        String s = "";
        for (String aux : list) s += "<procs>" + Encoder.parseXML(aux) + "</procs>";
        return s;
    }

    public String listarMedB(List<String> list) {
        String s = "";
        for (String aux : list) s += "<med>" + Encoder.parseXML(aux) + "</med>";
        return s;
    }

    public String listarMuestras(List<String> list) {
        String s = "";
        for (String aux : list) s += "<m>" + Encoder.parseXML(aux) + "</m>";
        return s;
    }

    @SuppressWarnings("unchecked")
    public String agregarDiag(String cod) {
        List<Cie10> c = em.createQuery("from Cie10 c where c.cod = :cod").setParameter("cod", cod).getResultList();
        if (!c.isEmpty()) {
            cod += " | " + c.get(0).getDescrip();
        }
        return cod;
    }

    @SuppressWarnings("unchecked")
    public String agregarProc(String cod) {
        List<SaqAsse> c = em.createQuery("from SaqAsse c where c.cod = :cod").setParameter("cod", cod).getResultList();
        if (!c.isEmpty()) {
            cod += " | " + c.get(0).getDescrip();
        } else {
            List<Cie9> b = em.createQuery("from Cie9 c where c.cod = :cod").setParameter("cod", cod).getResultList();
            if (!b.isEmpty()) {
                cod += " | " + b.get(0).getDescrip();
            }
        }
        return cod;
    }
}
