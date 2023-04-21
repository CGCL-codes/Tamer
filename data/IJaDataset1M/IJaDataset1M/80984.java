package es.aeat.eett.rubik.map;

/**
 * @author f00992
 *
 */
public interface Zone {

    /**
	 * @return Returns the id.
	 */
    public String getId();

    /**
	 * @return Returns the nameCapa.
	 */
    public String[] getNameCapas();

    /**
	 * @param nameCapas The nameCapas to set.
	 */
    public void setNameCapas(String[] nameCapas);

    /**
	 * @return Returns the valores.
	 */
    public String[] getValores();

    /**
	 * @param valores The valores to set.
	 */
    public void setValores(String[] valores);

    /**
	 * inicializa nameCapas y valores a null;
	 * liberando la memorioa usadoar por los arrays.
	 */
    public void clear();
}
