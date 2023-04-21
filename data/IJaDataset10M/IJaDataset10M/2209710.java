package Tarea18sept0120230322Jorge;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge Ramos
 */
public class Control implements Runnable, ActionListener {

    Thread Hilo1;

    Maquina miMaquina;

    Random rnd = new Random();

    int ContMonedas = 0;

    public Control(Maquina miMaquina) {
        this.miMaquina = miMaquina;
    }

    public void Arrancar() {
        Hilo1 = new Thread(this);
        Hilo1.start();
    }

    public void run() {
        Thread hiloActual = Thread.currentThread();
        int i = rnd.nextInt(9);
        int j = rnd.nextInt(9);
        int k = rnd.nextInt(9);
        do {
            do {
                try {
                    miMaquina.setTexto1(i + "");
                    miMaquina.setTexto2(j + "");
                    miMaquina.setTexto3(k + "");
                    i++;
                    j++;
                    k++;
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (i <= 9 && j <= 9 && k <= 9 && Hilo1 == hiloActual);
            i = rnd.nextInt(9);
            j = rnd.nextInt(9);
            k = rnd.nextInt(9);
        } while (Hilo1 == hiloActual);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Arrancar")) {
            Arrancar();
            ContMonedas += 1;
            miMaquina.setResultado("Resultados");
        } else if (e.getActionCommand().equals("Parar")) {
            Stop();
            if (miMaquina.getText1().equals(miMaquina.getText2()) && miMaquina.getText1().equals(miMaquina.getText3())) {
                miMaquina.setResultado("Ganaste: " + ContMonedas);
                ContMonedas = 0;
            }
        } else {
            miMaquina.setResultado("La máquina tiene: $" + ContMonedas);
            ContMonedas = 0;
        }
    }

    public void Stop() {
        if (Hilo1 != null) {
            Hilo1 = null;
        }
    }
}
