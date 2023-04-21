package model;

public abstract class Impuesto implements java.io.Serializable {

    protected String nombre = null;

    /**
	 * Dado un monto y una cantidad de hectareas
	 *  devuelve el impuesto que debe aplicarse
	 * @return
	 */
    public abstract double getValorFinal(double monto, double cantha);

    public abstract double getTaxRate();

    public Impuesto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreImpuesto() {
        return nombre;
    }
}
