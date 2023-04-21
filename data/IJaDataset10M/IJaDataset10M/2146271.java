package pVaqueria;

import java.util.Random;
import pConfig.CConfig;
import pInterficie.CInterficieCLI;

public class CVaca extends Thread {

    private Random generador;

    private int quantitat_llet;

    private boolean acabar;

    private boolean munyible;

    private long darrer_cop_munyida;

    private long munyint_des_de;

    private int id_vaca;

    public CVaca(int id) {
        quantitat_llet = 0;
        generador = new Random(System.currentTimeMillis());
        acabar = false;
        munyible = false;
        darrer_cop_munyida = System.currentTimeMillis();
        munyint_des_de = System.currentTimeMillis();
        id_vaca = id;
    }

    public void MunyibotConnectat(int id) {
        munyint_des_de = System.currentTimeMillis();
        CInterficieCLI.Imprimir("Se m'ha connectat el munyibot " + id + " en el moment " + munyint_des_de);
    }

    public void MunyibotDesconnectat(int id) {
        darrer_cop_munyida = System.currentTimeMillis();
        CInterficieCLI.Imprimir("Se m'ha desconnectat el munyibot " + id + " en el moment " + darrer_cop_munyida);
    }

    public long TempsMunyint() {
        return System.currentTimeMillis() - munyint_des_de;
    }

    public long TempsSenseMunyir() {
        return System.currentTimeMillis() - darrer_cop_munyida;
    }

    public synchronized int ConsLlet() {
        return quantitat_llet;
    }

    public synchronized void Munyir() {
        quantitat_llet -= CConfig.llet_a_munyir;
        CInterficieCLI.Imprimir("Som la vaca " + id_vaca + " i me queden" + quantitat_llet + " litres.");
        if (quantitat_llet < 0) quantitat_llet = 0;
        if (quantitat_llet < CConfig.llet_maxima) {
            munyible = false;
        }
    }

    public boolean QuedaLlet() {
        return quantitat_llet > 0;
    }

    public boolean Munyible() {
        return munyible;
    }

    public void Acabar() {
        CInterficieCLI.Imprimir("Som la vaca " + id_vaca + " i vaig a acabar.");
        acabar = true;
    }

    public void run() {
        CInterficieCLI.Imprimir("La vaca " + id_vaca + " comença l'execució.");
        while (!acabar) {
            GenLlet();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
            }
        }
        CInterficieCLI.Imprimir("La vaca " + id_vaca + " diu adeu!.");
    }

    private void GenLlet() {
        quantitat_llet += (generador.nextInt(CConfig.llet_a_generar) + 1);
        CInterficieCLI.Imprimir("Som la vaca " + id_vaca + " i tinc " + quantitat_llet + " litres.");
        if (quantitat_llet > CConfig.llet_maxima) {
            CInterficieCLI.Imprimir("Som la vaca " + id_vaca + " i som munyible.");
            munyible = true;
        }
    }

    public int Id() {
        return id_vaca;
    }
}
