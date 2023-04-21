package clickandlearn.conquis.minijuegos.puzzle;

import java.awt.Point;
import org.newdawn.slick.*;

public class PiezaPuzzle {

    private int Id;

    private Image imagenPieza;

    private Point posActual, posAnterior;

    private boolean enganchada;

    private TableroPuzzle tableroPuzzle;

    private InGameState juegoPropietario;

    private int desviacionX, desviacionY;

    private boolean mouseEncima;

    public PiezaPuzzle(InGameState juegoProp, TableroPuzzle tablero, int iDent, Image imgPieza, Point pos) {
        this.juegoPropietario = juegoProp;
        this.Id = iDent;
        this.imagenPieza = imgPieza;
        this.posActual = new Point(pos);
        this.posAnterior = new Point(pos);
        this.enganchada = false;
        this.tableroPuzzle = tablero;
        this.desviacionX = 0;
        this.desviacionY = 0;
        this.mouseEncima = false;
    }

    public void sigueMouse(Point puntero) {
        if (enganchada) {
            this.posActual.x = puntero.x - desviacionX;
            this.posActual.y = puntero.y - desviacionY;
        }
    }

    public int getIdent() {
        return this.Id;
    }

    public Point getPosicionActual() {
        return this.posActual;
    }

    public void setPosicion(Point p) {
        this.posActual.x = p.x;
        this.posActual.y = p.y;
    }

    public void suelta() {
        if (this.enganchada) {
            int idCercana = this.tableroPuzzle.cercaDe(this);
            if (idCercana == -1) {
                if (!this.juegoPropietario.esPosPermitida(this.posActual)) {
                    this.posActual.x = this.posAnterior.x;
                    this.posActual.y = this.posAnterior.y;
                }
                this.sincronizaPosiciones();
            } else {
                this.posActual.x = this.tableroPuzzle.getHuecoPieza(idCercana).x;
                this.posActual.y = this.tableroPuzzle.getHuecoPieza(idCercana).y;
                this.sincronizaPosiciones();
            }
            this.enganchada = false;
        }
        if (this.juegoPropietario.estaResuelto()) {
            this.juegoPropietario.playMusicaResuelto();
            this.juegoPropietario.acaba();
        }
    }

    public void engancha() {
        if (!enganchada) this.enganchada = true;
    }

    public void dibuja() {
        if (this.mouseEncima) this.resaltaPieza();
        this.imagenPieza.draw(this.posActual.x, this.posActual.y);
    }

    public boolean clickeada(Point p) {
        if (p.x > this.posActual.x && p.x <= this.posActual.x + this.tableroPuzzle.getAnchoPieza()) if (p.y > this.posActual.y && p.y <= this.posActual.y + this.tableroPuzzle.getAltoPieza()) {
            this.desviacionX = p.x - this.posActual.x;
            this.desviacionY = p.y - this.posActual.y;
            return true;
        }
        return false;
    }

    public boolean estaEnganchada() {
        return this.enganchada;
    }

    public void mouseOverOn() {
        this.mouseEncima = true;
    }

    public void mouseOverOff() {
        this.mouseEncima = false;
    }

    public void sincronizaPosiciones() {
        this.posAnterior.x = this.posActual.x;
        this.posAnterior.y = this.posActual.y;
    }

    public void resaltaPieza() {
        this.imagenPieza.drawFlash(this.posActual.x - 1, this.posActual.y - 1, this.imagenPieza.getWidth() + 2, this.imagenPieza.getHeight() + 2, Color.blue);
    }

    public String toString() {
        String r = new String();
        r += "Objeto PiezaPuzzle\n";
        r += "ID -> " + this.Id + "\n";
        r += "Posicion Actual " + this.posActual.toString() + "\n";
        r += "Posicion Anterior " + this.posAnterior.toString();
        return r;
    }
}
