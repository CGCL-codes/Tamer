package logica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.qizx.api.QizxException;
import logica.gestoreMacchina.Macchina;
import logica.gestoreMemoria.MemoriaPiena;
import logica.gestoreMemoria.NotSwappable;
import logica.gestoreMemoria.Ram;
import logica.gestoreMemoria.Struttura;
import logica.gestoreMemoria.StrutturaNonTrovata;
import logica.gestoreMemoria.Swap;
import dati.Ambiente;
import dati.Istante;
import gestoreObserver.Observer;
import logica.gestorePolitiche.Aging;
import logica.gestorePolitiche.FirstFit;
import logica.gestorePolitiche.InterfacciaPolitiche;
import logica.gestoreProcessi.*;

public class TestSimulazione {

    /**
	 * classe che simula GUI
	 * @author Renati Samuele
	 *
	 */
    public class Sim implements Observer {

        Ambiente amb;

        Macchina macchina;

        List<Processo> processi;

        Ram ram;

        Swap swap;

        InterfacciaPolitiche polall;

        InterfacciaPolitiche polrim;

        Simulazione simulazione;

        SimulazioneTemporizzata simulazioneTemporizzata;

        TabellaProcessi tabproc;

        public Sim() throws QizxException, IOException {
            macchina = new Macchina(1000, 1000, 11, 11, 16, true, 1);
            polall = new Aging(0, false);
            ram = new Ram(0, 40, 10);
            swap = new Swap(0, 100, 10);
            swap.setResizable(true);
            polrim = new FirstFit();
            ram.setAllocazione(null);
            ram.setRimpiazzo(polall);
            amb = new Ambiente("prova");
            tabproc = new TabellaProcessi();
            InterfacciaProcesso p1 = tabproc.addProcesso(1, "p1", 4, ram, swap, 0, 1000, 1000, 1000, amb);
            InterfacciaProcesso p2 = tabproc.addProcesso(2, "p2", 2, ram, swap, 1, 1000, 1000, 1000, amb);
            InterfacciaProcesso p3 = tabproc.addProcesso(3, "p3", 1, ram, swap, 3, 1000, 1000, 1000, amb);
            p1.pushDati(100, true);
            p1.pushCodice(10);
            p2.pushDati(200, true);
            p2.pushCodice(10);
            p3.pushDati(150, true);
            p3.pushCodice(10);
            try {
                p1.allocaTutto();
                p2.allocaTutto();
                p3.allocaTutto();
            } catch (MemoriaPiena e1) {
                e1.printStackTrace();
            }
            try {
                p1.pushAttivita(4, 1, 1);
                p1.pushAttivita(1, 1, 2);
                p1.pushAttivita(2, 1, 3);
                p2.pushAttivita(18, 1, 1);
                p2.pushAttivita(19, 1, 2);
                p3.pushAttivita(45, 1, 1);
                p1.pushAttivita(1, 1, 4);
            } catch (IstanteErrato e) {
                e.printStackTrace();
            }
            tabproc.schedulaProcessi();
            simulazione = new Simulazione();
            simulazione.setAmbiente(amb);
            simulazione.setEseguito(false);
            simulazione.setIstanteAttuale(0);
            simulazione.setMacchina(macchina);
            simulazione.setNomeSimulazione("prova");
            simulazione.setRam(ram);
            simulazione.setSwap(swap);
            simulazione.setTabellaProcessi(tabproc);
            System.out.println("RAM INIZIALE");
            System.out.println(ram);
            System.out.println("SWAP INIZIALE");
            System.out.println(swap);
        }

        public void attacca() {
            amb.attach((Observer) this);
        }

        @Override
        public void update() {
            Istante a = null;
            try {
                a = (Istante) amb.getState(simulazione.getIstanteAttuale());
            } catch (QizxException e) {
                e.printStackTrace();
            }
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            ArrayList<Struttura> mod = (ArrayList<Struttura>) a.getStruttureMemoria();
            System.out.println("STRUTTURE MODIFICATE ISTANTE :" + simulazione.getIstanteAttuale());
            for (int i = 0; i < mod.size(); i++) {
                System.out.println(mod.get(i));
            }
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
    }

    public static void main(String[] args) {
        try {
            Sim a = new TestSimulazione().new Sim();
            a.attacca();
            SimulazioneTemporizzata simtemp = new SimulazioneTemporizzata("prova", a.simulazione);
            simtemp.eseguiTutto();
            simtemp.simulazione.setEseguito(true);
            simtemp.parti(simtemp.simulazione.getTabellaProcessi().getTempoNecessario(), 1000);
            System.out.println("RAM FINALE");
            System.out.println(a.ram);
            System.out.println("SWAP FINALE");
            System.out.println(a.swap);
        } catch (QizxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MemoriaPiena e) {
            e.printStackTrace();
        } catch (StrutturaNonTrovata e) {
            e.printStackTrace();
        } catch (NotSwappable e) {
            e.printStackTrace();
        }
    }
}
