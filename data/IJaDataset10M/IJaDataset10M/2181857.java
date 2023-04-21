package com.pronefa.sig.controladores;

import com.pronefa.sig.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import edu.umn.gis.mapscript.pointObj;
import java.awt.Toolkit;
import librerias.Xutil;

public class WheelMouseEventos implements MouseWheelListener {

    public void mouseWheelMoved(MouseWheelEvent e) {
        MapSIG a = MapSIG.refer;
        if (a.zoom_in_boton.isSelected()) {
            pointObj pix = new pointObj(e.getX(), e.getY(), 0);
            if (e.getWheelRotation() < 0) {
                a.mapa.zoomPoint(2, pix, a.mapa.getWidth(), a.mapa.getHeight(), a.mapa.getExtent(), null);
            } else {
                a.mapa.zoomPoint(-2, pix, a.mapa.getWidth(), a.mapa.getHeight(), a.mapa.getExtent(), null);
            }
            Toolkit.getDefaultToolkit().beep();
            a.generarImagen();
        }
    }
}
