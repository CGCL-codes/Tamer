package metso.paradigma.core.solutore;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import metso.dal.DalException;
import metso.paradigma.core.business.model.OperatorePianoTurno;
import metso.paradigma.core.business.model.PianoTurno;
import metso.paradigma.core.business.model.Turno;
import metso.paradigma.core.business.model.TurnoOperatore;

/**
 * Classe che implementa un solutore genetico.
 * Seguendo l'algoritmo genetico, due pianoturni di una popolazione che
 * risultano i migliori saranno incrociati per formare pianoturni figli
 * che a loro volta saranno modificati e andranno a formare la popolazione
 * successiva. 
 */
public class SolutoreGenetico implements ISolutore {

    protected Logger log = LogManager.getLogger(getClass());

    private static int POPOLAZIONE = 20;

    private static int NUM_GENERAZIONI = 30;

    private boolean test;

    private Random rand;

    private PianoTurno migliore;

    private long start;

    private int popolazione;

    private int numGenerazioni;

    public int getNumGenerazioni() {
        return numGenerazioni;
    }

    public void setNumGenerazioni(int numGenerazioni) {
        this.numGenerazioni = numGenerazioni;
    }

    public int getPopolazione() {
        return popolazione;
    }

    public void setPopolazione(int popolazione) {
        this.popolazione = popolazione;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public PianoTurno getMigliore() {
        return migliore;
    }

    public void setMigliore(PianoTurno migliore) {
        this.migliore = migliore;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public SolutoreGenetico() {
        this.setRand(new Random(System.nanoTime()));
        this.setPopolazione(POPOLAZIONE);
        this.setNumGenerazioni(NUM_GENERAZIONI);
        this.setTest(false);
    }

    public SolutoreGenetico(int pop, int gen, boolean stamp) {
        this.setRand(new Random(System.nanoTime()));
        this.setPopolazione(pop);
        this.setNumGenerazioni(gen);
        this.setTest(stamp);
    }

    /**
	 * Metodo che rende un pianoTurno popolato
	 * @param inizializzato 
	 * pianoTurno su cui lavorare
	 * @return pianoTurno
	 * pianoTurno popolato
	 * @throws DalException 
	 */
    public PianoTurno risolvi(PianoTurno inizializzato) throws DalException {
        if (this.isTest()) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            this.setStart(gc.getTimeInMillis());
            System.out.print("Inizio algoritmo : ");
            visualTime(gc);
        }
        ArrayList<PianoTurno> lista = this.inizializza(inizializzato);
        int noMiglioramenti = 0;
        while (noMiglioramenti < this.getNumGenerazioni()) {
            noMiglioramenti++;
            lista = this.incrocia(lista);
            if (this.isTest()) {
                System.out.println("figlio finale: " + lista.get(0).getPenalita());
            }
            if ((this.getMigliore() == null) || (lista.get(0).getPenalita() < this.getMigliore().getPenalita())) {
                noMiglioramenti = 0;
                this.setMigliore(lista.get(0).clone());
            }
            if (this.getMigliore().getPenalita() == 0) {
                noMiglioramenti = this.getNumGenerazioni();
            }
        }
        if (this.isTest()) {
            System.out.print("\n");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(this.getStart());
            System.out.print("Inizio algoritmo genetico: ");
            visualTime(gc);
            GregorianCalendar gc2 = new GregorianCalendar();
            gc2.setTime(new Date());
            System.out.print("Fine algoritmo genetico: ");
            visualTime(gc2);
            int millis = (int) (gc2.getTimeInMillis() - gc.getTimeInMillis());
            int sec = millis / 1000;
            int min = sec / 60;
            sec -= (min * 60);
            System.out.println("Durata: " + min + ":" + sec);
            System.out.print("\n");
        }
        return this.getMigliore();
    }

    /**
	 * Metodo che rende una lista di pianiTurno generati random a partire
	 * dal pianoTurno iniziale
	 * @param inizializzato 
	 * pianoTurno iniziale
	 * @return 
	 * lista di pianiTurno popolati random
	 * @throws DalException 
	 */
    public ArrayList<PianoTurno> inizializza(PianoTurno inizializzato) throws DalException {
        ArrayList<PianoTurno> lista = new ArrayList<PianoTurno>();
        log.info("Inizializzazione solutore genetico");
        log.info("sta creando i solutori random!!");
        for (int i = 0; i < this.getPopolazione(); i++) {
            SolutoreRandom solutore = new SolutoreRandom();
            PianoTurno cloned = solutore.risolvi(inizializzato.clone());
            lista.add(cloned);
        }
        if (this.isTest()) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            System.out.print("Inizializzazione completata e inizio ordinamento: ");
            visualTime(gc);
        }
        lista = this.ordinaPiani(lista);
        if (this.isTest()) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            System.out.print("Ordinamento completato: ");
            visualTime(gc);
            for (PianoTurno pianoTurno : lista) {
                System.out.println("piano: " + pianoTurno.getPenalita());
            }
        }
        return lista;
    }

    /**
	 * Metodo per visualizzare il tempo, usato per test
	 * @param gc
	 */
    private void visualTime(GregorianCalendar gc) {
        System.out.println(gc.get(GregorianCalendar.HOUR_OF_DAY) + ":" + gc.get(GregorianCalendar.MINUTE) + ":" + gc.get(GregorianCalendar.SECOND) + "," + gc.get(GregorianCalendar.MILLISECOND));
    }

    /**
	 * Metodo che presi due pianiTurno migliori incrocia tutti i loro 
	 * operatoriPianoTurno  
	 * @param 
	 * lista pianoTurno ordinati in ordine crescente di penalita
	 * @return 
	 * lista pianoTurni figli della generazione corrente
	 * @throws DalException 
	 */
    public ArrayList<PianoTurno> incrocia(ArrayList<PianoTurno> lista) throws DalException {
        PianoTurno pa = lista.get(0);
        PianoTurno ma = lista.get(1);
        PianoTurno figlio1 = pa.clone();
        PianoTurno figlio2 = pa.clone();
        for (int i = 0; i < this.getPopolazione() / 2 && figlio1.getPenalita() > 0 && figlio2.getPenalita() > 0; i++) {
            pa = lista.get(0);
            ma = lista.get(1);
            if (this.isTest()) {
                System.out.print("\n");
                System.out.println("padre: " + pa.getPenalita());
                System.out.println("madre: " + ma.getPenalita());
                System.out.print("\n");
            }
            figlio1 = pa.clone();
            figlio2 = pa.clone();
            for (int idxOpe = 0; idxOpe < pa.getOperatoriPianoTurno().size(); idxOpe++) {
                figlio1.getOperatoriPianoTurno().get(idxOpe).setTurniOperatore(new ArrayList<TurnoOperatore>());
                figlio2.getOperatoriPianoTurno().get(idxOpe).setTurniOperatore(new ArrayList<TurnoOperatore>());
                int selection = pa.numeroGiorni() / 2;
                for (int idxGio = 0; idxGio < selection; idxGio++) {
                    figlio1.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().add(pa.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().get(idxGio));
                    figlio2.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().add(ma.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().get(idxGio));
                }
                for (int idxGio = selection; idxGio < pa.numeroGiorni(); idxGio++) {
                    figlio1.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().add(ma.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().get(idxGio));
                    figlio2.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().add(pa.getOperatoriPianoTurno().get(idxOpe).getTurniOperatore().get(idxGio));
                }
            }
            figlio1.calcolaPenalitaTotale();
            figlio2.calcolaPenalitaTotale();
            ArrayList<PianoTurno> listaMigliori = new ArrayList<PianoTurno>();
            listaMigliori.add(pa);
            listaMigliori.add(ma);
            listaMigliori.add(figlio1);
            listaMigliori.add(figlio2);
            listaMigliori = this.ordinaPiani(listaMigliori);
            figlio1 = listaMigliori.get(0).clone();
            figlio2 = listaMigliori.get(1).clone();
            if (this.isTest()) {
                System.out.print("\n");
                System.out.println("figlio 1 :" + figlio1.getPenalita());
                System.out.println("figlio 2 :" + figlio2.getPenalita());
                System.out.print("\n");
            }
            lista.remove(pa);
            lista.remove(ma);
            if (figlio1.getPenalita() > 0 && figlio2.getPenalita() > 0) {
                if (this.isTest()) {
                    System.out.println("prima mutazione figlio 1: " + figlio1.getPenalita());
                }
                figlio1 = this.mutazione(figlio1);
                if (this.isTest()) {
                    System.out.println("dopo mutazione figlio 1: " + figlio1.getPenalita());
                }
            }
            if (figlio1.getPenalita() > 0 && figlio2.getPenalita() > 0) {
                if (this.isTest()) {
                    System.out.println("\nprima mutazione figlio 2: " + figlio2.getPenalita());
                }
                figlio2 = this.mutazione(figlio2);
                if (this.isTest()) {
                    System.out.println("dopo mutazione figlio 2: " + figlio2.getPenalita());
                }
            }
            lista.add(figlio1);
            lista.add(figlio2);
            if (this.isTest()) {
                System.out.print("\n");
                for (PianoTurno pianoTurno : lista) {
                    System.out.println("piano: " + pianoTurno.getPenalita());
                }
                System.out.print("\n");
            }
        }
        return this.ordinaPiani(lista);
    }

    /**
	 * Metodo che implementa gli operatori di mutazione.
	 * @param figlio 
	 * pianoTurno da modificare
	 * @return 
	 * pianoTurno modificato
	 * @throws DalException 
	 */
    private PianoTurno mutazione(PianoTurno figlio) throws DalException {
        PianoTurno save = figlio.clone();
        for (int idOpe = 0; idOpe < figlio.getOperatoriPianoTurno().size() && figlio.getPenalita() > 0; idOpe++) {
            OperatorePianoTurno ope = figlio.getOperatoriPianoTurno().get(idOpe);
            HashMap penCop = figlio.getListaPenalitaCopertura();
            Set listaGioCop = penCop.keySet();
            Integer gio = (Integer) listaGioCop.iterator().next();
            ope = this.altera(ope, gio);
            figlio.calcolaPenalitaTotale();
        }
        if (figlio.getPenalita() > save.getPenalita()) {
            return save;
        } else {
            return figlio;
        }
    }

    /**
	 * Metodo che implementa l'operatore di mutazione alterazione.
	 * In questo OperatorePianoTurno si sostituira il turno di un giorno
	 * con un altro turno a caso che possa eseguire in base 
	 * al suo contratto
	 * @param ope 
	 * OperatorePianoTurno da modificare
	 * @return 
	 * OperatorePianoTurno modificato
	 * @throws DalException 
	 */
    private OperatorePianoTurno altera(OperatorePianoTurno ope, int idxGio) throws DalException {
        List<String> listaTurniCodici = ope.getOperatore().getContrattoReparto().getTurniValidi();
        List<Turno> turniPossibili = new ArrayList<Turno>();
        for (Turno turno : ope.getPianoTurno().getTurniAmmissibili()) {
            if (listaTurniCodici.contains(turno.getCodice())) {
                turniPossibili.add(turno);
            }
        }
        int idxTur = this.getRand().nextInt(turniPossibili.size());
        while (ope.getTurniOperatore().get(idxGio).getTurno().getCodice().equals(turniPossibili.get(idxTur).getCodice())) {
            idxTur = this.getRand().nextInt(turniPossibili.size());
        }
        ope.getTurniOperatore().get(idxGio).setTurno(turniPossibili.get(idxTur));
        return ope;
    }

    /**
	 * Metodo che ordina una lista di pianiTurno in base alla penalita
	 * @param 
	 * lista da ordinare
	 * @return 
	 * lista ordinata
	 * @throws DalException 
	 */
    private ArrayList<PianoTurno> ordinaPiani(ArrayList<PianoTurno> lista) throws DalException {
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                PianoTurno piano1 = lista.get(i);
                PianoTurno piano2 = lista.get(j);
                if (piano1.getPenalita() > piano2.getPenalita()) {
                    PianoTurno temp = piano1;
                    lista.set(i, piano2);
                    lista.set(j, temp);
                }
            }
        }
        return lista;
    }
}
