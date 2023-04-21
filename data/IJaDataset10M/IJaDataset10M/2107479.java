package aplicaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import commons.IAplicacionLocal;

public class FileTransfer implements IAplicacionLocal {

    private static final String[] PARAMS = new String[] { "String archivo" };

    private int puerto;

    private String nombre;

    private String[] parametros;

    public String darNombre() {
        return nombre;
    }

    public int darPuerto() {
        return puerto;
    }

    public String[] darParametros() {
        return PARAMS;
    }

    public String usarAplicacion(String[] args) {
        try {
            FileInputStream file = new FileInputStream(args[0]);
            byte[] b = new byte[file.available()];
            file.read(b);
            file.close();
            String result = new String(b);
            return result.replaceAll("\r\n", "�");
        } catch (Exception e) {
            return "Error fatal: " + e.getMessage();
        }
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
}
