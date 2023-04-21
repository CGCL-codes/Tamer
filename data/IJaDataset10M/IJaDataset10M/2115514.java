package com.jdiv.samples.tutor1b.applet;

import java.util.Random;
import com.jdiv.JProcess;

public class Asteroid extends JProcess {

    private int codigo;

    public Asteroid(int codigo) {
        super();
        this.codigo = codigo;
    }

    public void begin() {
    }

    public void loop() {
        x = 0;
        y = 0;
        graph = codigo;
        angle = (int) (Math.random() * 360000) - 180000;
        while (true) {
            graph++;
            if (graph == (codigo + 20)) graph = codigo;
            advance(4);
            frame();
            if (collision("Disparo")) break;
            Main.corrige_coordenadas(this);
        }
        Main.puntuacion.setNum(Main.puntuacion.getNum() + 5);
        for (graph = 43; graph < 62; graph++) {
            frame();
        }
    }
}
