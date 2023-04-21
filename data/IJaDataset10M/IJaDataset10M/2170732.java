package infotup;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author USER
 */
public class Empresa {

    private String IdEmpresa;

    private String Nombre;

    private String Direccion;

    private String Departamento;

    private String Provincia;

    private String Distrito;

    private String Telefono;

    private String Fax;

    private String Email;

    public String getDepartamento() {
        return Departamento;
    }

    public String getDireccion() {
        return Direccion;
    }

    public String getDistrito() {
        return Distrito;
    }

    public String getEmail() {
        return Email;
    }

    public String getFax() {
        return Fax;
    }

    public String getIdEmpresa() {
        return IdEmpresa;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getProvincia() {
        return Provincia;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setDepartamento(String Departamento) {
        this.Departamento = Departamento;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public void setDistrito(String Distrito) {
        this.Distrito = Distrito;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public void setIdEmpresa(String IdEmpresa) {
        this.IdEmpresa = IdEmpresa;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setProvincia(String Provincia) {
        this.Provincia = Provincia;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public Empresa() {
    }

    public Empresa(String IdEmpresa, String Nombre, String Direccion, String Departamento, String Provincia, String Distrito, String Telefono, String Fax, String Email) {
        this.IdEmpresa = IdEmpresa;
        this.Nombre = Nombre;
        this.Direccion = Direccion;
        this.Departamento = Departamento;
        this.Provincia = Provincia;
        this.Distrito = Distrito;
        this.Telefono = Telefono;
        this.Fax = Fax;
        this.Email = Email;
    }

    public Vector buscarEmpresa(String nombre) {
        Conexion c = new Conexion();
        return c.buscarEmpresa(nombre);
    }

    public void modificarEmpresa() {
    }

    public void eliminarEmpresa() {
    }

    public boolean registrarEmpresa(String Nombre, String Telefono, String Email) {
        {
            try {
                this.Nombre = "";
                this.Telefono = Telefono;
                this.Email = "";
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public void mostrarEmpresa() {
    }

    public boolean compruebaNombreEmpresaNoBlanco(String Nombre) {
        {
            try {
                if (this.Nombre != null && !this.Nombre.isEmpty()) return true; else return false;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean compruebaDireccionEmpresaNoBlanco(String direccion) {
        {
            try {
                if (this.Direccion != null && !this.Direccion.isEmpty()) return true; else return false;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean compruebaNTelefono(String nt) {
        try {
            Integer.parseInt(nt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean compruebaFax(String f) {
        try {
            Integer.parseInt(f);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean compruebaMail(String m) {
        if (m != null || !m.isEmpty()) {
            Pattern pat = null;
            Matcher mat = null;
            pat = Pattern.compile("[a-zA-Z0-9]+[.[a-zA-Z0-9_-]+]*@[a-z0-9][\\w\\.-]*[a-z0-9]\\.[a-z][a-z\\.]*[a-z]$");
            mat = pat.matcher(m);
            if (mat.find()) {
                System.out.println("[" + mat.group() + "]");
                return true;
            } else {
                return false;
            }
        } else return true;
    }
}
