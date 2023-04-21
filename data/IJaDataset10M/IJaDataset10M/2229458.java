package negocio;

import datos.*;

public class GestorIU {

    public static String _dni;

    /**
	 * 
	 * 
	 * 
	 * @param dni
	 * @param clave
	 * @return
	 */
    public static int identificarUsuario(String dni, String clave) {
        _dni = dni;
        String[] claves = new String[3];
        int codigo = 0;
        claves = IdentificarUsuario.obtenerClaves(dni);
        if (claves[0].compareTo("null") == 0 && claves[1].compareTo("null") == 0 && claves[2].compareTo("null") == 0) codigo = -1; else if (claves[0].compareTo(clave) == 0) codigo = 1; else if (claves[1].compareTo(clave) == 0) codigo = 2; else if (claves[2].compareTo(clave) == 0) codigo = 3;
        return codigo;
    }

    public static void main(String args[]) {
        String dni = "11223344";
        String clave = "soygay";
        System.out.println(identificarUsuario(dni, clave));
    }
}
