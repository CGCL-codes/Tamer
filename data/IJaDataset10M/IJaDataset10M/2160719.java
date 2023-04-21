package metso.paradigma.core.business.model;

import java.io.Serializable;

public class Orario implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1393939635198850184L;

    private int ora;

    private int minuto;

    public Orario() {
    }

    public Orario(int ora, int minuto) {
        this.ora = ora;
        this.minuto = minuto;
    }

    public Orario(String orario) {
        Orario ora = Orario.parse(orario);
        setOra(ora.getOra());
        setMinuto(ora.getMinuto());
    }

    public void setOra(int ora) {
        this.ora = ora;
    }

    public int getOra() {
        return ora;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getMinuto() {
        return minuto;
    }

    public static Orario durata(Orario oraInizio, Orario oraFine) {
        int ore = 0;
        int minuti = 0;
        if (oraInizio.getOra() > oraFine.getOra()) {
            ore = 24 - oraInizio.getOra() + oraFine.getOra();
        } else {
            ore = oraFine.getOra() - oraInizio.getOra();
        }
        if (oraInizio.getMinuto() > oraFine.getMinuto()) {
            minuti = 60 - oraInizio.getMinuto() + oraFine.getMinuto();
            ore--;
        } else {
            minuti = oraFine.getMinuto() - oraInizio.getMinuto();
        }
        return new Orario(ore, minuti);
    }

    public static Orario parse(String orario) {
        Orario result = null;
        int idx = orario.indexOf(':');
        if (idx > -1) {
            result = new Orario(Integer.parseInt(orario.substring(0, idx)), Integer.parseInt(orario.substring(idx + 1, orario.length())));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (ora < 10) {
            builder.append("0");
        }
        builder.append(ora);
        builder.append(":");
        if (minuto < 10) {
            builder.append("0");
        }
        builder.append(minuto);
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Orario) {
            Orario orario = (Orario) obj;
            return orario.getMinuto() == this.getMinuto() && orario.getOra() == this.getOra();
        }
        return false;
    }
}
