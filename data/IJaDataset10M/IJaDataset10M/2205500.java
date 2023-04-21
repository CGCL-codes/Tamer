package com.centropresse.dto;

public class Spinta extends Caratteristica {

    /**
	 * 
	 */
    private static final long serialVersionUID = 503167216056065415L;

    private String id_spinta;

    private String min;

    private String max;

    public Spinta() {
    }

    public Spinta(String id_spinta, String min, String max) {
        this.id_spinta = id_spinta;
        this.min = min;
        this.max = max;
    }

    /**
	 * @return the id_spinta
	 */
    public String getId_spinta() {
        return id_spinta;
    }

    /**
	 * @param id_spinta the id_spinta to set
	 */
    public void setId_spinta(String id_spinta) {
        this.id_spinta = id_spinta;
    }

    /**
	 * @return the max
	 */
    public String getMax() {
        return max;
    }

    /**
	 * @param max the max to set
	 */
    public void setMax(String max) {
        this.max = max;
    }

    /**
	 * @return the min
	 */
    public String getMin() {
        return min;
    }

    /**
	 * @param min the min to set
	 */
    public void setMin(String min) {
        this.min = min;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        final String eol = System.getProperty("line.separator");
        str.append(eol);
        str.append("\tid_spinta..:[" + this.getId_spinta() + "]" + eol);
        str.append("\tmin........:[" + this.getMin() + "]" + eol);
        str.append("\tmin........:[" + this.getMax() + "]" + eol);
        str.append("\t---" + eol);
        return str.toString();
    }
}
