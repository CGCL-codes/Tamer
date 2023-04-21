package es.realtimesystems.simplemulticast;

public class PruebaSemaforo extends Thread {

    Semaforo sem = null;

    run2 r = null;

    public PruebaSemaforo() {
    }

    public synchronized void dormir() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            Log.exit(-1);
        }
    }

    public synchronized void despertar() {
        this.notify();
    }

    public void run() {
        int i = 0;
        run2 r = new run2(this, this.sem);
        r.start();
        while (true) {
            i++;
            Log.log("1: Iteracion " + i, "1: Hola**");
            dormir();
        }
    }

    public static void main(String[] args) {
        PruebaSemaforo prueba = new PruebaSemaforo();
        prueba.start();
    }
}

class run2 extends Thread {

    Semaforo sem = null;

    PruebaSemaforo obj = null;

    run2(PruebaSemaforo obj, Semaforo sem) {
        this.sem = sem;
        this.obj = obj;
    }

    public void run() {
        int i = 0;
        while (true) {
            i++;
            Log.log("2: Iteracion " + i, "2: Hola**");
            Temporizador.sleep(1000);
            obj.despertar();
        }
    }
}
