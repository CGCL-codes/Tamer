package modelo;

import java.awt.Color;
import java.awt.Graphics;
import modelo.Ponto;

public class Ponto extends FiguraGeometrica {

    private int x, y;

    protected Color cor;

    public Ponto() {
        super();
    }

    public Ponto(int x, int y) {
        this(Color.BLACK, x, y);
    }

    public Ponto(Color c, int x, int y) {
        super(c);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    /**
	 * Metodo responsavel por desenha um ponto na tela
	 * @param g
	 */
    public boolean isInPoint(int x, int y) {
        if (this.x == x && this.y == y) return true; else return false;
    }

    public boolean isInPoint(Ponto p) {
        return isInPoint(p.getX(), p.getY());
    }

    /**
	 * M�otodo que calcula a dist�ncia de um ponto at� o ponto P
	 * @param p
	 * @return o valor (inteiro) da dist�ncia entre os dois pontos
	 */
    public int distancia(Ponto p) {
        return (int) Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }
}
