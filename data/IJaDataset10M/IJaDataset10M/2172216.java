package es.deusto.eside.aike.laberinto;

public class Coordenada implements Comparable {

    private int x;

    private int y;

    public Coordenada(int posX, int posY) {
        this.x = posX;
        this.y = posY;
    }

    public Coordenada getArriba() {
        return new Coordenada(x, y - 1);
    }

    public Coordenada getAbajo() {
        return new Coordenada(x, y + 1);
    }

    public Coordenada getDerecha() {
        return new Coordenada(x + 1, y);
    }

    public Coordenada getIzquierda() {
        return new Coordenada(x - 1, y);
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

    @Override
    public int compareTo(Object o) {
        Coordenada c = (Coordenada) o;
        if (this.x == c.x) return this.y - c.y; else return this.x - c.x;
    }

    @Override
    public String toString() {
        return "x=" + this.x + ";y=" + this.y;
    }
}
