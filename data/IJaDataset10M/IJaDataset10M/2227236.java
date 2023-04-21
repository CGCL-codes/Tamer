package ClasesKinesiologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Adrian
 */
public class Paciente implements Serializable {

    private String apeYNom;

    private String DNI;

    private List<Ficha> fichas;

    private Date fechaNac;

    private String Localidad;

    private String Domicilio;

    private String Telefono;

    private String OS;

    public void setOS(String O) {
        OS = O;
    }

    public void setApe(String nom) {
        apeYNom = nom;
    }

    public void setDoc(String nom) {
        DNI = nom;
    }

    public void setNac(Date nom) {
        fechaNac = nom;
    }

    public void setLoc(String nom) {
        Localidad = nom;
    }

    public void setDom(String nom) {
        Domicilio = nom;
    }

    public void setTel(String nom) {
        Telefono = nom;
    }

    public Paciente(String nom, String doc, Date nac, String loc, String dom, String tel, String o) {
        apeYNom = nom;
        DNI = doc;
        fechaNac = nac;
        Localidad = loc;
        Domicilio = dom;
        Telefono = tel;
        OS = o;
        fichas = new ArrayList<Ficha>();
    }

    public void agregarFicha(Ficha f) {
        fichas.add(f);
    }

    public String getApe() {
        return apeYNom;
    }

    public String getOS() {
        return OS;
    }

    public Date getFN() {
        return this.fechaNac;
    }

    public String getDoc() {
        return DNI;
    }

    public String getLoc() {
        return Localidad;
    }

    public String getDom() {
        return Domicilio;
    }

    public String getTel() {
        return Telefono;
    }
}
