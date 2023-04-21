package clickandlearn.conquis.minijuegos.piratas;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RelojTiempo {

    private float centroX, centroY;

    private float radio;

    private long tiempoMaxMS;

    private long tiempoConsumido;

    private final Color color_reloj = Color.magenta;

    private final Color color_manilla = Color.pink;

    private float angulo;

    public RelojTiempo(float centrox, float centroy, float radius, long timeMax) {
        this.centroX = centrox;
        this.centroY = centroy;
        this.radio = radius;
        this.angulo = 359;
        this.tiempoConsumido = 0;
        this.tiempoMaxMS = timeMax;
    }

    public void pinta(Graphics g) {
        float ancho_anterior = g.getLineWidth();
        Color color_anterior = g.getColor();
        g.setLineWidth(3);
        this.actualizaAngulo();
        g.setColor(new Color(1, 0, 0, 0.2f));
        g.fillArc(this.centroX - radio, this.centroY - radio, radio * 2, radio * 2, 270, this.angulo - 90);
        g.setAntiAlias(true);
        g.setColor(this.color_reloj);
        g.drawOval(this.centroX - radio, this.centroY - radio, radio * 2, radio * 2);
        float px = 0, py = 0;
        px = (float) (this.centroX + radio * 8 / 10 * Math.cos(Math.toRadians(angulo) - Math.PI / 2));
        py = (float) (this.centroY + radio * 8 / 10 * Math.sin(Math.toRadians(angulo) - Math.PI / 2));
        g.setColor(color_manilla);
        g.drawLine(centroX, centroY, px, py);
        g.setAntiAlias(false);
        g.setLineWidth(ancho_anterior);
        g.setColor(color_anterior);
    }

    public boolean addTime(long plus) {
        if (this.tiempoConsumido + plus >= this.tiempoMaxMS) {
            this.tiempoConsumido = tiempoMaxMS;
            return true;
        } else {
            this.tiempoConsumido += plus;
            return false;
        }
    }

    public void actualizaAngulo() {
        this.angulo = 360 * (1 - ((float) this.tiempoConsumido / (float) this.tiempoMaxMS));
    }
}
