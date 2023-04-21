package propres;

import java.io.Serializable;

public class Persona implements Serializable {

    private String Nombre;

    private String Apellidos;

    private int DNI;

    private String Domicilio;

    public Persona() {
    }

    public Persona Alta_Persona() {
        Persona A = new Persona();
        return A;
    }

    public String Get_Nombre() {
        return Nombre;
    }

    public String Get_Apellidos() {
        return Apellidos;
    }

    public int Get_DNI() {
        return DNI;
    }

    public String Get_Domicilio() {
        return Domicilio;
    }

    public void Set_Nombre(String Nom) {
        Nombre = Nom;
    }

    public void Set_Apellidos(String Ap) {
        Apellidos = Ap;
    }

    public void Set_DNI(int dni) {
        DNI = dni;
    }

    public void Set_Domicilio(String Dom) {
        Domicilio = Dom;
    }
}
