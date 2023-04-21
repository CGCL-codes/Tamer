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

/**
 * Classe che implementa un solutore VNS.
 * Seguendo l'algoritmo VNS, tutti i pianoturni di un range saranno
 * migliorati per formare nuovi pianoturni.
 */
public class SolutoreVNS implements ISolutore {

    protected Logger log = LogManager.getLogger(getClass());

    private boolean test;

    private Random rand;

    private PianoTurno migliore;

    private long start;

    private static final int SIZE = 10;

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

    public SolutoreVNS() {
        this.setRand(new Random(System.nanoTime()));
        this.setTest(false);
    }

    public SolutoreVNS(boolean stamp) {
        this.setRand(new Random(System.nanoTime()));
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
        while (noMiglioramenti < SIZE) {
            noMiglioramenti++;
            lista = this.migliora(lista);
            if (this.isTest()) {
                System.out.println("figlio finale: " + lista.get(0).getPenalita());
            }
            if ((this.getMigliore() == null) || (lista.get(0).getPenalita() < this.getMigliore().getPenalita())) {
                noMiglioramenti = 0;
                this.setMigliore(lista.get(0).clone());
            }
            if (this.getMigliore().getPenalita() == 0) {
                noMiglioramenti = SIZE;
            }
        }
        if (this.isTest()) {
            System.out.print("\n");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(this.getStart());
            System.out.print("Inizio algoritmo VNS: ");
            visualTime(gc);
            GregorianCalendar gc2 = new GregorianCalendar();
            gc2.setTime(new Date());
            System.out.print("Fine algoritmo VNS: ");
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
        log.info("Inizializzazione solutore VNS");
        for (int i = 0; i < SIZE; i++) {
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
    public ArrayList<PianoTurno> migliora(ArrayList<PianoTurno> lista) throws DalException {
        for (PianoTurno pianoTurno : lista) {
            pianoTurno = mutazione(pianoTurno);
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
            ope = this.scambioO(ope);
            figlio = this.scambioV(figlio);
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
	 * Metodo che implementa l'operatore di mutazione scambio.
	 * In questo operatore verranno scambiati 2 turni di 2 giorni diversi
	 * @param ope 
	 * OperatorePianoTurno da modificare
	 * @return 
	 * OperatorePianoTurno modificato
	 */
    private OperatorePianoTurno scambioO(OperatorePianoTurno ope) {
        int idxG1 = this.getRand().nextInt(ope.getPianoTurno().numeroGiorni());
        int idxG2 = this.getRand().nextInt(ope.getPianoTurno().numeroGiorni());
        while (idxG1 == idxG2) {
            idxG2 = this.getRand().nextInt(ope.getPianoTurno().numeroGiorni());
        }
        Turno turno1 = ope.getTurniOperatore().get(idxG1).getTurno();
        Turno turno2 = ope.getTurniOperatore().get(idxG2).getTurno();
        ope.getTurniOperatore().get(idxG2).setTurno(turno1);
        ope.getTurniOperatore().get(idxG1).setTurno(turno2);
        return ope;
    }

    /**
	 * Metodo che implementa l'operatore di mutazione scambio.
	 * In questo operatore verranno scambiati 2 turni di 2 giorni diversi
	 * @param ope 
	 * OperatorePianoTurno da modificare
	 * @return 
	 * OperatorePianoTurno modificato
	 * @throws DalException 
	 */
    private PianoTurno scambioV(PianoTurno piano) throws DalException {
        int idxG = this.getRand().nextInt(piano.numeroGiorni());
        int idxO1 = this.getRand().nextInt(piano.getOperatoriPianoTurno().size());
        int idxO2 = this.getRand().nextInt(piano.getOperatoriPianoTurno().size());
        while (idxO1 == idxO2) {
            idxO2 = this.getRand().nextInt(piano.getOperatoriPianoTurno().size());
        }
        OperatorePianoTurno ope1 = piano.getOperatoriPianoTurno().get(idxO1);
        OperatorePianoTurno ope2 = piano.getOperatoriPianoTurno().get(idxO2);
        List<String> listaStringTurni1 = ope1.getOperatore().getContrattoReparto().getTurniValidi();
        List<String> listaStringTurni2 = ope2.getOperatore().getContrattoReparto().getTurniValidi();
        Turno turno1 = ope1.getTurniOperatore().get(idxG).getTurno();
        Turno turno2 = ope2.getTurniOperatore().get(idxG).getTurno();
        if (turno1.getIdTurno() != turno2.getIdTurno()) {
            if (listaStringTurni1.contains(turno2.getCodice()) && listaStringTurni2.contains(turno1.getCodice())) {
                ope1.getTurniOperatore().get(idxG).setTurno(turno2);
                ope2.getTurniOperatore().get(idxG).setTurno(turno1);
            }
        }
        return piano;
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
