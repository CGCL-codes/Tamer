package Modelo;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Reloj {

    protected int horas, minutos, segundos, decimas;

    protected ArrayList<ObserverReloj> observersReloj_;

    protected boolean parado;

    protected static Timer tiempo = null;

    public interface ObserverReloj {

        public void tiempoCambiado(int horas, int minutos, int segundos, int decimas);

        public void tiempoAgotado();
    }

    public void addObserverReloj(ObserverReloj o) {
        observersReloj_.add(o);
    }

    public void deleteObserverReloj(ObserverReloj o) {
        observersReloj_.remove(o);
    }

    private void emitTiempoCambiado() {
        for (int i = 0; i < observersReloj_.size(); i++) {
            ObserverReloj observer = observersReloj_.get(i);
            observer.tiempoCambiado(horas, minutos, segundos, decimas);
        }
    }

    private void emitTiempoAgotado() {
        for (int i = 0; i < observersReloj_.size(); i++) {
            ObserverReloj observer = observersReloj_.get(i);
            observer.tiempoAgotado();
        }
    }

    public Reloj(int h, int m, int s) {
        observersReloj_ = new ArrayList<ObserverReloj>();
        parado = true;
        horas = h;
        minutos = m;
        segundos = s;
        if (h < 0) h = 0;
        if (m < 0 || m > 59) m = 0;
        if (s < 0 || s > 59) s = 0;
        decimas = 0;
    }

    public Reloj(int ms) {
        observersReloj_ = new ArrayList<ObserverReloj>();
        parado = true;
        if (ms > 0) {
            segundos = ms / 1000;
            horas = segundos / 3600;
            int resto = segundos % 3600;
            minutos += resto / 60;
            segundos = resto % 60;
        } else {
            horas = 0;
            minutos = 0;
            segundos = 0;
            decimas = 0;
        }
    }

    public void start() {
        if (tiempo == null) {
            tiempo = new Timer();
        }
        parado = false;
        tiempo.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                tareasRun();
            }
        }, 0, 100);
    }

    protected void tareasRun() {
        if (!parado) {
            if (decimas != 0) {
                decimas--;
            } else {
                if (segundos != 0) {
                    segundos--;
                } else {
                    if (minutos != 0) {
                        minutos--;
                    } else {
                        horas--;
                        minutos = 59;
                    }
                    segundos = 59;
                }
                decimas = 9;
            }
            if (horas < 0 || minutos < 0 || segundos < 0) {
                horas = 0;
                minutos = 0;
                segundos = 0;
                decimas = 0;
            }
            this.emitTiempoCambiado();
            if (horas == 0 && minutos == 0 && segundos == 0) {
                this.emitTiempoAgotado();
            }
        }
    }

    public void setStopped(boolean b) {
        parado = b;
    }

    public void setTime(int h, int m, int s, int d) {
        horas = h;
        if (h < 0) h = 0;
        if (m < 0 || m > 59) m = 0; else minutos = m;
        if (s < 0 || s > 59) s = 0; else segundos = s;
        decimas = d;
    }

    public int getHours() {
        return horas;
    }

    public int getMinutes() {
        return minutos;
    }

    public int getSeconds() {
        return segundos;
    }

    public int getTenths() {
        return decimas;
    }
}
