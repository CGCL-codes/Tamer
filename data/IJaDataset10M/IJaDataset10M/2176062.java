package edu.uah.lazarillo.comm.in;

public class Matriz {

    private int[][] matriz = null;

    private int x = -1;

    private int y = -1;

    public Matriz(int x, int y) {
        matriz = new int[x][y];
        this.x = x;
        this.y = y;
    }

    public int get(int x, int y) throws IndexOutOfBoundsException {
        if (x < this.x && y < this.y) if (x > -1 && y > -1) return matriz[x][y];
        throw new IndexOutOfBoundsException();
    }

    public void set(int x, int y, int value) throws IndexOutOfBoundsException {
        if (x < this.x && y < this.y) if (x > -1 && y > -1) {
            matriz[x][y] = value;
            return;
        }
        throw new IndexOutOfBoundsException("x:" + x + " y:" + y + " size:" + this.x + ":" + this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
