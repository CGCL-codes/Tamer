package cliente;

import java.util.Date;
import java.util.ResourceBundle;
import utilidad.clasesBase.*;

/**
 *
 * @author Egalcom
 */
public class ClienteVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("cliente/Bundle");

    private int codigo;

    private String tipo;

    private String nombre;

    private String apellidos;

    private String apodo;

    private String cifNif;

    private Date fechaNacimiento;

    private int idDireccion;

    private String direccionCorta;

    private String nombreMunicipio;

    private String telefonoPpl;

    private String emailPpl;

    private String fotoPpl;

    private int idFormaPago;

    private int idDescuento;

    private int numPlazos;

    private int diasPlazo;

    private String vtosFijos;

    public ClienteVO() {
        inicializarComponetes();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombreCompleto() {
        String nombreCompleto = nombre;
        if ((apellidos != null) && (!apellidos.isEmpty())) nombreCompleto = apellidos + ", " + nombre;
        return nombreCompleto;
    }

    public String getCifNif() {
        return cifNif;
    }

    public void setCifNif(String cifNif) {
        this.cifNif = cifNif;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getDireccionCorta() {
        return direccionCorta;
    }

    public void setDireccionCorta(String direccionCorta) {
        this.direccionCorta = direccionCorta;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getTelefonoPpl() {
        return telefonoPpl;
    }

    public void setTelefonoPpl(String telefonoPpl) {
        this.telefonoPpl = telefonoPpl;
    }

    public String getEmailPpl() {
        return emailPpl;
    }

    public void setEmailPpl(String emailPpl) {
        this.emailPpl = emailPpl;
    }

    public String getFotoPpl() {
        return fotoPpl;
    }

    public void setFotoPpl(String fotoPpl) {
        this.fotoPpl = fotoPpl;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public int getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    public int getNumPlazos() {
        return numPlazos;
    }

    public void setNumPlazos(int numPlazos) {
        this.numPlazos = numPlazos;
    }

    public int getDiasPlazo() {
        return diasPlazo;
    }

    public void setDiasPlazo(int diasPlazo) {
        this.diasPlazo = diasPlazo;
    }

    public String getVtosFijos() {
        return vtosFijos;
    }

    public void setVtosFijos(String vtosFijos) {
        this.vtosFijos = vtosFijos;
    }

    @Override
    public void inicializarComponetes() {
        super.inicializarComponetes();
        this.tipo = "";
        this.codigo = 0;
        this.nombre = "";
        this.apellidos = "";
        this.apodo = "";
        this.cifNif = "";
        this.fechaNacimiento = null;
        this.telefonoPpl = "";
        this.emailPpl = "";
        this.fotoPpl = "";
        this.idFormaPago = -1;
        this.idDescuento = -1;
        this.numPlazos = 1;
        this.diasPlazo = 0;
        this.vtosFijos = "";
        this.idDireccion = -1;
        this.direccionCorta = "";
        this.nombreMunicipio = "";
    }
}
