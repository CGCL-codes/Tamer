package secd.componentes;

import java.awt.Graphics;
import java.awt.Point;

public class And extends Componente {

    public And(Point point, int lado) {
        setCentro(point);
        setLado(lado);
        this.posicionaPatillas(point);
    }

    public void ejecutaLogicaInterna() throws Exception {
        setEjecutandose(true);
        Cable e0 = conexiones_entrantes.get(0);
        Cable e1 = conexiones_entrantes.get(1);
        if (e0 != null) e0.getComponenteOrigen().ejecutaLogicaInterna();
        if (e1 != null) e1.getComponenteOrigen().ejecutaLogicaInterna();
        setEjecutandose(false);
        aplicaTablaVerdad();
    }

    public void valorEntradasCambiado() throws Exception {
        if (!isEjecutandose()) {
            Cable s0 = conexiones_salientes.get(0);
            if (s0 != null) {
                ValorLogico valor_anterior = s0.getValor();
                aplicaTablaVerdad();
                if (valor_anterior != s0.getValor()) s0.getComponenteDestino().valorEntradasCambiado();
            }
        }
    }

    public void aplicaTablaVerdad() {
        Cable e0 = conexiones_entrantes.get(0);
        Cable e1 = conexiones_entrantes.get(1);
        Cable s0 = conexiones_salientes.get(0);
        if (s0 == null) return;
        ValorLogico valor_e0, valor_e1;
        if (e0 != null) valor_e0 = e0.getValor(); else valor_e0 = ValorLogico.U;
        if (e1 != null) valor_e1 = e1.getValor(); else valor_e1 = ValorLogico.U;
        if (valor_e0 == ValorLogico.CERO || valor_e1 == ValorLogico.CERO) s0.setValor(ValorLogico.CERO); else if (valor_e0 == ValorLogico.UNO && valor_e1 == ValorLogico.UNO) s0.setValor(ValorLogico.UNO); else s0.setValor(ValorLogico.U);
    }

    public Componente getCopiaComponente() {
        And copia = new And(this.centro, this.grados);
        copia.escala = this.escala;
        copia.orientacion_entrada = this.orientacion_entrada;
        copia.lado = this.lado;
        copia.posicionaPatillas(copia.centro);
        copia.grados = this.grados;
        return copia;
    }

    public void paint(Graphics g) {
        super.paint(g);
        int unidad = (int) ((double) lado / 6.0);
        int separacion_patillas = lado / 6;
        int centro_x = (int) centro.getX();
        int centro_y = (int) centro.getY();
        if (grados == 0) {
            g.drawLine(centro_x - 3 * unidad, centro_y - separacion_patillas, centro_x - 2 * unidad, centro_y - separacion_patillas);
            g.drawLine(centro_x - 3 * unidad, centro_y + separacion_patillas, centro_x - 2 * unidad, centro_y + separacion_patillas);
            g.drawLine(centro_x + 2 * unidad, centro_y, centro_x + 3 * unidad, centro_y);
            g.drawLine(centro_x - 2 * unidad, centro_y - 2 * unidad, centro_x, centro_y - 2 * unidad);
            g.drawLine(centro_x - 2 * unidad, centro_y + 2 * unidad, centro_x, centro_y + 2 * unidad);
            g.drawLine(centro_x - 2 * unidad, centro_y - 2 * unidad, centro_x - 2 * unidad, centro_y + 2 * unidad);
            g.drawArc(centro_x - 2 * unidad, centro_y - 2 * unidad, 4 * unidad, 4 * unidad, 90, -180);
        } else if (grados == 90) {
            g.drawLine(centro_x - separacion_patillas, centro_y - 3 * unidad, centro_x - separacion_patillas, centro_y - 2 * unidad);
            g.drawLine(centro_x + separacion_patillas, centro_y - 3 * unidad, centro_x + separacion_patillas, centro_y - 2 * unidad);
            g.drawLine(centro_x, centro_y + 2 * unidad, centro_x, centro_y + 3 * unidad);
            g.drawLine(centro_x + 2 * unidad, centro_y - 2 * unidad, centro_x + 2 * unidad, centro_y);
            g.drawLine(centro_x - 2 * unidad, centro_y - 2 * unidad, centro_x - 2 * unidad, centro_y);
            g.drawLine(centro_x - 2 * unidad, centro_y - 2 * unidad, centro_x + 2 * unidad, centro_y - 2 * unidad);
            g.drawArc(centro_x - 2 * unidad, centro_y - 2 * unidad, 4 * unidad, 4 * unidad, 180, 180);
        } else if (grados == 180) {
            g.drawLine(centro_x + 3 * unidad, centro_y - separacion_patillas, centro_x + 2 * unidad, centro_y - separacion_patillas);
            g.drawLine(centro_x + 3 * unidad, centro_y + separacion_patillas, centro_x + 2 * unidad, centro_y + separacion_patillas);
            g.drawLine(centro_x - 2 * unidad, centro_y, centro_x - 3 * unidad, centro_y);
            g.drawLine(centro_x + 2 * unidad, centro_y - 2 * unidad, centro_x, centro_y - 2 * unidad);
            g.drawLine(centro_x + 2 * unidad, centro_y + 2 * unidad, centro_x, centro_y + 2 * unidad);
            g.drawLine(centro_x + 2 * unidad, centro_y - 2 * unidad, centro_x + 2 * unidad, centro_y + 2 * unidad);
            g.drawArc(centro_x - 2 * unidad, centro_y - 2 * unidad, 4 * unidad, 4 * unidad, 90, 180);
        } else if (grados == 270) {
            g.drawLine(centro_x - separacion_patillas, centro_y + 3 * unidad, centro_x - separacion_patillas, centro_y + 2 * unidad);
            g.drawLine(centro_x + separacion_patillas, centro_y + 3 * unidad, centro_x + separacion_patillas, centro_y + 2 * unidad);
            g.drawLine(centro_x, centro_y - 2 * unidad, centro_x, centro_y - 3 * unidad);
            g.drawLine(centro_x + 2 * unidad, centro_y + 2 * unidad, centro_x + 2 * unidad, centro_y);
            g.drawLine(centro_x - 2 * unidad, centro_y + 2 * unidad, centro_x - 2 * unidad, centro_y);
            g.drawLine(centro_x - 2 * unidad, centro_y + 2 * unidad, centro_x + 2 * unidad, centro_y + 2 * unidad);
            g.drawArc(centro_x - 2 * unidad, centro_y - 2 * unidad, 4 * unidad, 4 * unidad, 0, 180);
        }
    }

    public String getName() {
        return "And";
    }

    public void posicionaPatillas(Point centro_componente) {
        int unidad = (int) ((double) lado / 6.0);
        int centro_x = centro_componente.x;
        int centro_y = centro_componente.y;
        entradas.clear();
        salidas.clear();
        if (grados == 0) {
            entradas.add(new Point(centro_x - 3 * unidad, centro_y - unidad));
            entradas.add(new Point(centro_x - 3 * unidad, centro_y + unidad));
            salidas.add(new Point(centro_x + 3 * unidad, centro_y));
        } else if (grados == 90) {
            entradas.add(new Point(centro_x - 1 * unidad, centro_y - 3 * unidad));
            entradas.add(new Point(centro_x + 1 * unidad, centro_y - 3 * unidad));
            salidas.add(new Point(centro_x, centro_y + 3 * unidad));
        } else if (grados == 180) {
            entradas.add(new Point(centro_x + 3 * unidad, centro_y - unidad));
            entradas.add(new Point(centro_x + 3 * unidad, centro_y + unidad));
            salidas.add(new Point(centro_x - 3 * unidad, centro_y));
        } else {
            entradas.add(new Point(centro_x - 1 * unidad, centro_y + 3 * unidad));
            entradas.add(new Point(centro_x + 1 * unidad, centro_y + 3 * unidad));
            salidas.add(new Point(centro_x, centro_y - 3 * unidad));
        }
    }
}
