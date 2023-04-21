package utilitarios;

import java.lang.Math;

public class PuntoXY {

    private double coordX;

    private double coordY;

    public PuntoXY(double x, double y) {
        this.coordX = x;
        this.coordY = y;
    }

    public double getCoordX() {
        return this.coordX;
    }

    public double getCoordY() {
        return this.coordY;
    }

    /** 
	 * Devuelve la distancia entre (x1,y1) y (x2,y2).
	 * Formula: sqrt((x1-x2)^2 - (y1-y2)^2)
	 * @param otroPunto
	 * @return double
	 */
    public double distancia(PuntoXY otroPunto) {
        double x = (this.coordX - otroPunto.getCoordX());
        double y = (this.coordY - otroPunto.getCoordY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
	 * Suma un punto con otro, y devuelve uno nuevo con 
	 * la suma de las coordenadas de ambos.
	 * @param otroPunto
	 * @return
	 */
    public PuntoXY sumarPunto(PuntoXY otroPunto) {
        double coordX = this.coordX + otroPunto.coordX;
        double coordY = this.coordY + otroPunto.coordY;
        return new PuntoXY(coordX, coordY);
    }

    public PuntoXY sumarDireccion(Direccion direccion) {
        return sumarPunto(direccion.getVector());
    }

    public PuntoXY productoEscalar(double unEscalar) {
        double coordX = this.coordX * unEscalar;
        double coordY = this.coordY * unEscalar;
        return new PuntoXY(coordX, coordY);
    }

    public boolean esIgualA(PuntoXY posicion) {
        return (this.coordX == posicion.getCoordX() && this.coordY == posicion.getCoordY());
    }
}
