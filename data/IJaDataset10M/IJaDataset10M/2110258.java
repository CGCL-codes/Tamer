package configuracion;

import java.util.Date;

public class configGeneral {

    private static configGeneral instancia = null;

    Date FechaCierre;

    String Pais;

    String Idioma;

    String BaseDatos;

    private configGeneral() {
    }

    private static synchronized void createInstance() {
        if (instancia == null) {
            instancia = new configGeneral();
        }
    }

    public static configGeneral getInstance() {
        if (instancia == null) {
            createInstance();
        }
        return instancia;
    }

    public Date getFechaCierre() {
        return FechaCierre;
    }

    public void setFechaCierre(Date fecha) {
        this.FechaCierre = fecha;
    }

    public String getBaseDatos() {
        return BaseDatos;
    }

    public void setBaseDatos(String Base) {
        this.BaseDatos = Base;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        this.Pais = pais;
    }

    public String getIdioma() {
        return Idioma;
    }

    public void setIdioma(String Idioma) {
        this.Idioma = Idioma;
    }
}
