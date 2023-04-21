package metso.paradigma.core.solutore.test;

import java.text.SimpleDateFormat;
import junit.framework.Assert;
import metso.dal.DalException;
import metso.paradigma.core.business.model.Operatore;
import metso.paradigma.core.business.model.OperatorePianoTurno;
import metso.paradigma.core.business.model.PianoTurno;
import metso.paradigma.core.business.model.Reparto;
import metso.paradigma.core.business.model.TurnoOperatore;
import metso.paradigma.core.configuration.ConfigurationHelper;
import metso.paradigma.core.dal.PianoTurnoDao;
import metso.paradigma.core.solutore.SolutoreRandom;
import org.junit.Test;

/**
 * Classe che implementa il test del solutore random.
 */
public class SolutoreRandomTest {

    /**
	 * Metodo che testa l'algoritmo random
	 * @throws Exception
	 */
    @Test
    public void testRisolvi() throws Exception {
        SolutoreRandom solutore = new SolutoreRandom();
        PianoTurno pianoTurno = this.inizializza();
        ConfigurationHelper.setPath("test");
        pianoTurno = solutore.risolvi(pianoTurno);
        visualizza(pianoTurno);
        Assert.assertNotNull(pianoTurno);
    }

    /**
	 * Metodo che inizializza un pianoTurno
	 * @return piano inizializzato
	 * @throws Exception
	 */
    private PianoTurno inizializza() throws Exception {
        PianoTurnoDao dao = new PianoTurnoDao();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        PianoTurno pianoTurno = new PianoTurno();
        pianoTurno.setDataInizio(sdf.parse("01/09/2008"));
        pianoTurno.setDataFine(sdf.parse("30/11/2008"));
        pianoTurno.setReparto(dao.caricaPerId(Reparto.class, 130000));
        for (Operatore operatore : pianoTurno.getReparto().getOperatori()) {
            OperatorePianoTurno operatorePianoTurno = new OperatorePianoTurno();
            operatorePianoTurno.setOperatore(operatore);
            operatorePianoTurno.setPianoTurno(pianoTurno);
            pianoTurno.getOperatoriPianoTurno().add(operatorePianoTurno);
        }
        pianoTurno.setTurniAmmissibili(dao.loadListaTurniOrdinati());
        return pianoTurno;
    }

    /**
	 * Metodo usato in fase di test per visualizzare il risultato
	 * @param pianoTurno popolato
	 * @throws DalException 
	 */
    private void visualizza(PianoTurno pianoTurno) throws DalException {
        System.out.println("Finale: ");
        for (OperatorePianoTurno ope : pianoTurno.getOperatoriPianoTurno()) {
            System.out.println(ope.getOperatore().getNome() + " " + ope.getOperatore().getCognome() + " " + ope.getOperatore().getContratto().getId() + ": " + ope.getPenalita());
            for (TurnoOperatore tur : ope.getTurniOperatore()) {
                System.out.print(tur.getTurno().getCodice() + "\t");
            }
            System.out.print("\n");
        }
        System.out.println("\n");
        System.out.println("TOT: " + pianoTurno.calcolaPenalitaTotale());
        System.out.println("OPE: " + pianoTurno.getPenalitaOperatori());
        System.out.println("COP: " + pianoTurno.getPenalitaCopertura());
        System.out.println("COPGIO: " + pianoTurno.getPenalitaCoperturaGiorno());
        System.out.println("COPSKI: " + pianoTurno.getPenalitaCoperturaSkill());
    }
}
