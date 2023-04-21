package presentacion;

public class Usuario {

    private String nombre = null;

    private String apellido = null;

    private String cedula = null;

    private String correo = null;

    private int edad = -1;

    private String usuario = null;

    private String password = null;

    private String banco = null;

    private String documento = null;

    private String tipoCuenta = null;

    private int numeroCuenta = -1;

    private int clave = -1;

    public Usuario() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getEdad() {
        return edad;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBanco() {
        return banco;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public int getClave() {
        return clave;
    }
}
