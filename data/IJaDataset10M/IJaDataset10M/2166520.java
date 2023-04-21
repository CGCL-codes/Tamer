package modelo;

/**
 * @author Usuario
 *
 */
public class AproximacionPorDosRectas {

    private Recta recta1 = null;

    private Recta recta2 = null;

    private double maximoX = 0;

    private double medioX = 0;

    private double minimoX = 0;

    /**
	 * 
	 */
    public AproximacionPorDosRectas(double xCero, double yCero, double xUno, double yUno, double xDos, double yDos) {
        this.recta1 = new Recta(xCero, yCero, xUno, yUno);
        this.recta2 = new Recta(xUno, yUno, xDos, yDos);
        this.minimoX = xCero;
        this.medioX = xUno;
        this.maximoX = xDos;
    }

    public double getValorEn(double x) {
        if (x <= medioX) {
            if (x > minimoX) return this.recta1.calcularRecta(x); else return this.recta1.calcularRecta(minimoX);
        } else {
            if (x < maximoX) return this.recta2.calcularRecta(x); else return this.recta2.calcularRecta(maximoX);
        }
    }
}
