package usuarios;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class UsuarioInternoConPass extends Usuario implements Serializable {

    protected String pass;

    public UsuarioInternoConPass() {
        super();
        this.pass = new String();
    }

    public UsuarioInternoConPass(String nombre, String direccion, String correo, String DNI, String pass) throws Exception {
        super(nombre, direccion, correo, DNI);
        this.pass = new String();
    }

    /**
	 * Permite establecer el correo del usuario interno con pass
	 * 
	 * @param correo
	 * @throws Exception
	 * @note Exigimos que el mail pertenezca al dominio "@congreso.es"
	 */
    public void setCorreo(String correo) throws Exception {
        boolean[] flags = new boolean[3];
        for (int i = 0; i < 3; i++) flags[i] = false;
        flags[0] = correo.contains("@");
        if (flags[0]) flags[1] = correo.substring(correo.indexOf("@") + 1, correo.length()).contains("@");
        if (flags[0] && !flags[1]) flags[2] = correo.substring(correo.lastIndexOf("@") + 1, correo.length()).equals("congreso.es");
        if (flags[2]) this.correo = correo; else throw new Exception("Dirección de correo invalida");
    }

    /**
	 * Devuelve la contraseña del ponente
	 * 
	 * @return
	 */
    public String getPass() {
        return pass;
    }

    /**
	 * Permite establecer la contraseña del ponente
	 * 
	 * @param pass
	 */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
