package ar.uba.fi.algo3.titiritero.audio;

import java.util.concurrent.ArrayBlockingQueue;

public class Reproductor implements Runnable {

    private ArrayBlockingQueue<Elemento> cola;

    private boolean encendido;

    private Piano piano = null;

    public Reproductor() {
        this.cola = new ArrayBlockingQueue<Elemento>(30);
        this.encendido = false;
        this.piano = new Piano();
    }

    @Override
    public void run() {
        while (encendido) {
            System.out.println(this.cola.size());
            Elemento elemento = this.cola.poll();
            if (elemento != null) {
                System.out.print("ping");
                piano.reproducir(elemento.getNota(), elemento.getDuracion());
            } else {
                try {
                    System.out.print("silencio");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void encender() {
        this.encendido = true;
    }

    public void reproducir(Elemento elemento) {
        System.out.println(this.cola.size());
        try {
            this.cola.put(elemento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void apagar() {
        this.encendido = false;
    }
}
