package EntidadesCompartidas;

import java.util.LinkedList;
import EntidadesCompartidas.*;

/**
 *
 * @author Andres
 */
public class Publicacion {

    private int Id;

    private String Nombre;

    private String Direccion;

    private String e_mail;

    private Rubro Rubro;

    private String Descripcion;

    private String Modo_Pago;

    private String URL_Imagen;

    private int Id_Cliente;

    private int Estado;

    private String Telefono;

    private LinkedList<Productos_Servicios> List_Productos;

    private String Coordenadas;

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String Coordenadas) {
        this.Coordenadas = Coordenadas;
    }

    public String getPathName() {
        return PathName;
    }

    public void setPathName(String PathName) {
        this.PathName = PathName;
    }

    private String PathName;

    public LinkedList<Productos_Servicios> getList_Productos() {
        return List_Productos;
    }

    public void setList_Productos(LinkedList<Productos_Servicios> List_Productos) {
        this.List_Productos = List_Productos;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public EntidadesCompartidas.Rubro getRubro() {
        return Rubro;
    }

    public void setRubro(EntidadesCompartidas.Rubro Rubro) {
        this.Rubro = Rubro;
    }

    public String getURL_Imagen() {
        return URL_Imagen;
    }

    public void setURL_Imagen(String URL_Imagen) {
        this.URL_Imagen = URL_Imagen;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getModo_Pago() {
        return Modo_Pago;
    }

    public void setModo_Pago(String Modo_Pago) {
        this.Modo_Pago = Modo_Pago;
    }
}
