package MNGimnasio;

import java.util.Date;

public class Deportista {

    private float cedula;

    private String nombre;

    private String apellido;

    private Date fecha_nacimiento;

    private String genero;

    private String email;

    private String direccion;

    private String barrio;

    private String telefono;

    private String celular;

    private String profesion;

    private Date fecha_ingreso;

    private ObjetivoDesarrollo mObjetivoDesarrollo;

    public Deportista() {
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String val) {
        this.apellido = val;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String val) {
        this.barrio = val;
    }

    public float getCedula() {
        return cedula;
    }

    public void setCedula(float val) {
        this.cedula = val;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String val) {
        this.direccion = val;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String val) {
        this.email = val;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date val) {
        this.fecha_nacimiento = val;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String val) {
        this.genero = val;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String val) {
        this.nombre = val;
    }

    public void MostrarDeportista() {
    }

    public void MostrarObjetivosDesarrollo() {
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String val) {
        this.celular = val;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date val) {
        this.fecha_ingreso = val;
    }

    public ObjetivoDesarrollo getObjetivoDesarrollo() {
        return mObjetivoDesarrollo;
    }

    public void setObjetivoDesarrollo(ObjetivoDesarrollo val) {
        this.mObjetivoDesarrollo = val;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String val) {
        this.profesion = val;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String val) {
        this.telefono = val;
    }
}
