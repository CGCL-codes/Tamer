package khl.ooo.util;

import java.util.Date;

public class Datum implements Cloneable {

    private int dag, maand, jaar;

    /** Een constructor zonder parameters (object datum gelijk aan de systeemdatum)
	 * @param /
	 * @throws DagException
	 * @throws MaandException
	 */
    @SuppressWarnings("deprecation")
    public Datum() {
        Date sysdatum = new Date();
        int dag = sysdatum.getDate();
        int maand = sysdatum.getMonth() + 1;
        int jaar = sysdatum.getYear() + 1900;
        this.set(dag, maand, jaar);
    }

    /**Een constructor met een datum object als parameter
	 * @param datum (Datum)
	 * @throws DatumException 
	 * @throws MaandException 
	 * @throws DagException */
    public Datum(Datum datum) {
        if ((datum != null) && (datum instanceof Datum)) {
            datum.clone();
        } else throw new IllegalArgumentException();
    }

    /**Een constructor met parameters dag, maand en jaar (3 gehele getallen)
	 * @param dag (int)
	 * @param maand (int)
	 * @param jaar (int)
	 * @throws DagException
	 * @throws MaandException */
    public Datum(int dag, int maand, int jaar) {
        this.set(dag, maand, jaar);
    }

    /**Een constructor met een String als parameter. 
	  	In deze String zit de datum in DDMMJJJJ formaat maar tussen de dag, maand en jaar staat een scheidingsteken (Vb 12/05/2009)
	  	Opmerking: maand moet ingegeven worden met 2 cijfers (Vb 05), jaartal met 4 cijfers (Vb 0567), dagnr met 1 of 2 cijfers
	 * @param datum (String)
	 * @throws DatumStringException 
	 * @throws MaandException 
	 * @throws DagException  */
    public Datum(String datum) {
        if ((datum != null) && (datum != "") && (datum instanceof String)) {
            if (datum.length() == 9 || datum.length() == 10) {
                if (datum.length() == 9) datum = "0" + datum;
                if (datum.substring(2, 3).equals("/") && datum.substring(5, 6).equals("/")) {
                    int dag = Integer.valueOf(datum.substring(0, 2));
                    int maand = Integer.valueOf(datum.substring(3, 5));
                    int jaar = Integer.valueOf(datum.substring(6, 10));
                    this.set(dag, maand, jaar);
                } else throw new IllegalArgumentException();
            } else throw new IllegalArgumentException();
        } else throw new IllegalArgumentException();
    }

    /**
	 * 
	 * @param dag
	 * @param maand
	 * @param jaar
	 * @return
	 */
    public boolean set(int dag, int maand, int jaar) {
        boolean ok = false;
        int maxdagen = 0;
        this.jaar = jaar;
        if (maand > 12 || maand < 1) {
            throw new IllegalArgumentException("maand klopt niet");
        } else {
            this.maand = maand;
        }
        maxdagen = getMaxdagen(maand);
        if (dag > maxdagen) {
            if (dag == 29 && maand == 2 && !this.isSchrikkeljaar() || dag < 1) throw new IllegalArgumentException("Dag klopt niet"); else {
                this.dag = dag;
                ok = true;
            }
        }
        return ok;
    }

    /**
	 * 
	 * @param maand
	 * @return
	 */
    private int getMaxdagen(int maand) {
        int maxdagen;
        if (maand == 4 || maand == 6 || maand == 9 || maand == 11) maxdagen = 30; else if (maand == 2) {
            if (this.isSchrikkeljaar()) maxdagen = 29; else maxdagen = 28;
        } else if (maand == 1 || maand == 3 || maand == 5 || maand == 7 || maand == 8 || maand == 10 || maand == 12) maxdagen = 31; else throw new IllegalArgumentException();
        return maxdagen;
    }

    public int getDag() {
        return this.dag;
    }

    public int getMaand() {
        return this.maand;
    }

    public int getJaar() {
        return this.jaar;
    }

    public boolean isSchrikkeljaar() {
        return (jaar % 4 == 0) && ((jaar % 100 != 0) || (jaar % 400 == 0));
    }

    /** 
	 * toString methodes
	 * 	toString: geeft datum object terug als volgt: 4 februari 2009
	 * 	toStringAmerikaans: geeft een datum in Amerikaans formaat terug (vb 2009/2/4)
	 * 	toStringInEuropees�: geeft een datum in Europees formaat terug (vb 4/2/2009) 
	 * */
    public String toString() {
        String output = "";
        output += this.getDag() + " " + this.getMonthName() + " " + this.getJaar();
        return output;
    }

    private String getMonthName() {
        Maanden[] maanden = Maanden.values();
        return maanden[this.getMaand() - 1].getName();
    }

    public String toStringAmerikaans() {
        String output = "";
        output += this.getJaar() + "/" + this.getMaand() + "/" + this.getDag();
        return output;
    }

    public String toStringEuropees() {
        String output = "";
        output += this.getDag() + "/" + this.getMaand() + "/" + this.getJaar();
        return output;
    }

    public boolean equals(Datum datum) {
        if ((this != null) && (datum != null) && (datum instanceof Datum)) return this.getDag() == ((Datum) datum).getDag() && this.getMaand() == ((Datum) datum).getMaand() && this.getJaar() == ((Datum) datum).getJaar(); else return false;
    }

    public int compareTo(Datum datum) {
        if ((this == null) || (datum == null) || (!(datum instanceof Datum))) throw new IllegalArgumentException(); else {
            if (this.equals(datum)) return 0; else if (this.getJaar() > datum.getJaar()) return 1; else if (this.getJaar() < datum.getJaar()) return -1; else {
                if (this.getMaand() > datum.getMaand()) return 1; else if (this.getMaand() < datum.getMaand()) return -1; else {
                    if (this.getDag() > datum.getDag()) return 1; else return -1;
                }
            }
        }
    }

    public boolean isKleinerDan(Datum datum) {
        if ((this == null) || (datum == null) || (!(datum instanceof Datum))) throw new IllegalArgumentException(); else {
            if (this.compareTo(datum) == 1) return true; else return false;
        }
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return o;
    }
}
