package hello;

import java.util.Date;
import net.sourceforge.floggy.persistence.Persistable;

public class Falta implements Persistable {

    private double numeroDeAulas;

    private String justificativa;

    private Date data;

    public Falta(double numeroDeAulas, String justificativa, Date data) {
        this.numeroDeAulas = numeroDeAulas;
        this.justificativa = justificativa;
        this.data = data;
    }

    public Falta() {
    }

    public Date getData() {
        return this.data;
    }

    public String getJustificativa() {
        return this.justificativa;
    }

    public double getNumeroDeAulas() {
        return this.numeroDeAulas;
    }
}
