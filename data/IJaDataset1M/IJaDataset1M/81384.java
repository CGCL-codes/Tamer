package Servicios;

import org.jasypt.util.password.*;

/**
 * Describe a un usuario, para ser autenticado.
 * 
 * @author Jorge
 * @author Jonathan
 */
public class Usuario {

    private String usuario;

    private String contrasena;

    private String nombres;

    private String apellidos;

    private boolean encriptado = false;

    /**
     * Inicializa los datos del usuario.
     * @param usuario
     * @param contrasena
     * @param nombres
     * @param apellidos 
     */
    public Usuario(String usuario, String contrasena, String nombres, String apellidos) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    /**
     * Inicia los datos del usuario solo con usuario y contrasena.
     * @param usuario
     * @param contrasena 
     */
    public Usuario(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombres = "";
        this.apellidos = "";
    }

    /**
     * 
     * @return Regresa los apellidos del usuario.
     */
    public String obtenerApellidos() {
        return apellidos;
    }

    /**
     * 
     * @return Regresa la contrasena del usuario.
     */
    public String obtenerContrasena() {
        return contrasena;
    }

    /**
     * 
     * @return Regresa los nombres del usuario
     */
    public String obtenerNombres() {
        return nombres;
    }

    /**
     * 
     * @return Regresa el usuario utilizado para registrar al usuario.
     */
    public String obtenerUsuario() {
        return usuario;
    }

    /**
     * Encripta la contrasena del usuario utilizando un algoritmo irreversible.
     * @return Regresa si la operacion fue exitosa o no.
     */
    public boolean encriptar() {
        PasswordEncryptor encriptador = new StrongPasswordEncryptor();
        contrasena = encriptador.encryptPassword(contrasena);
        encriptado = true;
        return true;
    }

    /**
     * Valida si dos usuarios son iguales.
     * @param usuario
     * @param contrasenaEncriptada
     * @return 
     */
    public boolean validar(String usuario, String contrasenaEncriptada) {
        if (!this.usuario.equals(usuario)) return false;
        if (encriptado) {
            return contrasena.equals(contrasenaEncriptada);
        }
        PasswordEncryptor encriptador = new StrongPasswordEncryptor();
        if (!encriptador.checkPassword(contrasena, contrasenaEncriptada)) return false; else {
            contrasena = contrasenaEncriptada;
            encriptado = true;
            return true;
        }
    }
}
