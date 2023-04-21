package metso.paradigma.core.dal.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import metso.dal.DalException;
import metso.paradigma.core.business.model.Formazione;
import metso.paradigma.core.business.model.Formazioni;
import metso.paradigma.core.business.model.Operatore;
import metso.paradigma.core.business.model.OperatorePianoTurno;
import metso.paradigma.core.business.model.Orario;
import metso.paradigma.core.business.model.PianoTurno;
import metso.paradigma.core.business.model.Qualifica;
import metso.paradigma.core.business.model.Reparto;
import metso.paradigma.core.business.model.Turno;
import metso.paradigma.core.business.model.TurnoOperatore;
import metso.paradigma.core.business.regole.ContrattoReparto;
import metso.paradigma.core.business.regole.Contratto;
import metso.paradigma.core.dal.PianoTurnoDao;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PianoTurnoDaoTest extends BaseDaoTest {

    private static ContrattoReparto contrattoReparto;

    private static Contratto contratto;

    private static PianoTurnoDao pianoTurnoDao;

    private static Operatore operatoreForTest;

    private static Reparto repartoForTest;

    private static Turno turnoForTest;

    private static TurnoOperatore turnoOperatoreForTest;

    private static OperatorePianoTurno operatorePianoTurnoForTest;

    private static PianoTurno pianoTurnoForTest;

    private static Reparto repartoPerSostituzione;

    private static Qualifica qualificaForTest;

    @BeforeClass
    public static void SetUp() throws Exception {
        configurePath();
        pianoTurnoDao = new PianoTurnoDao();
        repartoForTest = createRepartoForTest();
        repartoPerSostituzione = createRepartoForTestOperatore("sostituzione");
        contratto = createRegoleContrattuali();
        contrattoReparto = createContrattoReparto(contratto);
        qualificaForTest = createQualificaForTest();
        operatoreForTest = createOperatoreForTest();
        turnoForTest = createTurnoForTest();
        turnoOperatoreForTest = createTurnoOperatoreForTest();
        operatorePianoTurnoForTest = createOperatorePianoTurnoForTest();
        pianoTurnoForTest = createPianoTurnoForTest();
        operatorePianoTurnoForTest.setPianoTurno(pianoTurnoForTest);
        pianoTurnoDao.salva(turnoForTest);
        pianoTurnoDao.salva(repartoForTest);
        pianoTurnoDao.salva(qualificaForTest);
        pianoTurnoDao.salva(operatoreForTest);
        pianoTurnoDao.salva(repartoPerSostituzione);
    }

    @AfterClass
    public static void tearDownAfterTest() throws DalException {
        pianoTurnoDao.cancella(turnoForTest);
        pianoTurnoDao.cancella(operatoreForTest);
        pianoTurnoDao.cancella(qualificaForTest);
        pianoTurnoDao.cancella(repartoForTest);
        pianoTurnoDao.cancella(repartoPerSostituzione);
    }

    @Test
    public void testLoadReparto() throws DalException {
        Reparto reparto = null;
        Reparto reparto2 = null;
        int idReparto = 130000;
        int idReparto2 = 130001;
        reparto = pianoTurnoDao.caricaPerId(Reparto.class, idReparto);
        Assert.assertNotNull(reparto);
        Assert.assertEquals("43400", reparto.getCodice());
        Assert.assertEquals("Cardiologia", reparto.getDescrizione());
        Assert.assertEquals(10, reparto.getCoperture().size());
        Assert.assertEquals(2, reparto.getCopertureGiornaliere().size());
        Assert.assertEquals(20, reparto.getOperatori().size());
        reparto2 = pianoTurnoDao.caricaPerId(Reparto.class, idReparto2);
        Assert.assertNotNull(reparto2);
        Assert.assertEquals("42201", reparto2.getCodice());
        Assert.assertEquals("Terapia intensiva cardiochirurgica", reparto2.getDescrizione());
        Assert.assertEquals(0, reparto2.getCoperture().size());
        Assert.assertEquals(0, reparto2.getCopertureGiornaliere().size());
    }

    @Test
    public void testLoadTurno() throws DalException {
        Turno turno = null;
        turno = pianoTurnoDao.loadTurno("T");
        Assert.assertEquals(turnoForTest.getDescrizione(), turno.getDescrizione());
        Assert.assertEquals(turnoForTest.getOraInizio(), turno.getOraInizio());
        Assert.assertEquals(turnoForTest.getOraFine(), turno.getOraFine());
        Assert.assertEquals(turnoForTest.getDurata(), turno.getDurata());
    }

    @Test
    public void testLoadTurnoOperatore() throws DalException {
        TurnoOperatore turnoOperatore = null;
        int idTurnoOperatore = 130101;
        turnoOperatore = pianoTurnoDao.loadTurnoOperatore(idTurnoOperatore);
        GregorianCalendar gc = new GregorianCalendar(2008, 8, 01);
        Date dataTurnoOperatore = gc.getTime();
        Assert.assertEquals(dataTurnoOperatore, turnoOperatore.getDataTurno());
    }

    @Test
    public void testLoadPianoTurno() throws DalException {
        PianoTurno pTurno = null;
        Turno turno = null;
        Collection<OperatorePianoTurno> operatoriPianoTurno = null;
        int idPianoTurno = 130003;
        GregorianCalendar gc = new GregorianCalendar(2008, 8, 01);
        Date date = gc.getTime();
        pTurno = pianoTurnoDao.caricaPerId(PianoTurno.class, idPianoTurno);
        List<Turno> lista = pianoTurnoDao.loadListaTurniOrdinati();
        pTurno.setTurniAmmissibili(lista);
        operatoriPianoTurno = pTurno.getOperatoriPianoTurno();
        Assert.assertNotNull(pTurno);
        Assert.assertEquals("01/09/2008 - 30/09/2008", pTurno.getDescrizione());
        Assert.assertEquals(3, operatoriPianoTurno.size());
        Assert.assertEquals(3, pTurno.getTurniOperatoreDate(date).size());
        Assert.assertEquals(9240, pTurno.getPenalitaCopertura());
        Assert.assertEquals(1260, pTurno.getPenalitaCoperturaGiorno());
        Assert.assertEquals(7140, pTurno.getPenalitaCoperturaSkill());
        Assert.assertEquals(17640, pTurno.getPenalita());
        for (OperatorePianoTurno operatorePianoTurno : operatoriPianoTurno) {
            Assert.assertEquals(30, operatorePianoTurno.getTurniOperatore().size());
        }
        turno = pianoTurnoDao.loadTurno("M");
        Assert.assertTrue(pTurno.isTurnoAssigned(turno, date));
        turno = pianoTurnoDao.loadTurno("M5");
        Assert.assertFalse(pTurno.isTurnoAssigned(turno, date));
    }

    @Test
    public void testLoadPianiTurnoReparto() throws DalException {
        List<PianoTurno> pianiTurno = new ArrayList<PianoTurno>();
        int idReparto = 130000;
        pianiTurno = pianoTurnoDao.loadPianiTurnoReparto(idReparto);
        Assert.assertEquals(1, pianiTurno.size());
    }

    @Test
    public void testUpdateLoadByIdOperatore() throws DalException {
        int idOperatore = 130001;
        Operatore operatore = null;
        Formazioni formazioni = null;
        Set<Formazione> formazioniBase = null;
        Formazione formazione1 = new Formazione();
        Formazione formazione2 = new Formazione();
        formazione1.setNome("laurea");
        formazione2.setNome("diploma");
        operatore = pianoTurnoDao.caricaPerId(Operatore.class, idOperatore);
        String temp = operatore.getCognome();
        formazioni = operatore.getSchedaOperatore().getFormazioniOperatore();
        formazioniBase = formazioni.getFormazioneBase();
        Assert.assertEquals(1, formazioniBase.size());
        formazioniBase.add(formazione1);
        formazioniBase.add(formazione2);
        formazioni.setFormazioneBase(formazioniBase);
        operatore.setCognome("Verdi");
        operatore.getSchedaOperatore().setFormazioniOperatore(formazioni);
        pianoTurnoDao.aggiorna(operatore);
        operatore = pianoTurnoDao.caricaPerId(Operatore.class, idOperatore);
        Assert.assertEquals("Verdi", operatore.getCognome());
        Assert.assertEquals(3, operatore.getSchedaOperatore().getFormazioniOperatore().getFormazioneBase().size());
        formazioniBase.remove(formazione1);
        formazioni.setFormazioneBase(formazioniBase);
        operatore.getSchedaOperatore().setFormazioniOperatore(formazioni);
        pianoTurnoDao.aggiorna(operatore);
        operatore = pianoTurnoDao.caricaPerId(Operatore.class, idOperatore);
        Assert.assertEquals(2, operatore.getSchedaOperatore().getFormazioniOperatore().getFormazioneBase().size());
        formazioniBase.remove(formazione2);
        formazioni.setFormazioneBase(formazioniBase);
        operatore.getSchedaOperatore().setFormazioniOperatore(formazioni);
        operatore.setCognome(temp);
        pianoTurnoDao.aggiorna(operatore);
        operatore = pianoTurnoDao.caricaPerId(Operatore.class, idOperatore);
        Assert.assertEquals(temp, operatore.getCognome());
        Assert.assertEquals(1, operatore.getSchedaOperatore().getFormazioniOperatore().getFormazioneBase().size());
    }

    @Test
    public void testSearchOperatori() throws DalException {
        Reparto repartoCompetenza = pianoTurnoDao.caricaPerId(Reparto.class, 130000);
        Reparto reparto = pianoTurnoDao.caricaPerId(Reparto.class, 130001);
        Operatore op = new Operatore();
        op.setCognome("ma");
        List<Operatore> operatori = null;
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(19, operatori.size());
        Assert.assertTrue(operatori.get(0).getCognome().toLowerCase().contains("ma"));
        op = new Operatore();
        op.setNome("Guido");
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        Assert.assertEquals("Vespa", operatori.get(0).getCognome());
        op = new Operatore();
        op.setCodiceFiscale("SS");
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        Assert.assertEquals("Corona", operatori.get(0).getCognome());
        Assert.assertEquals("Veronica", operatori.get(0).getNome());
        op = new Operatore();
        op.setRepartoCompetenza(repartoCompetenza);
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(20, operatori.size());
        op = new Operatore();
        op.setNome("Gui");
        op.setCognome("Ves");
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        Assert.assertEquals("Vespa", operatori.get(0).getCognome());
        op = new Operatore();
        op.setCodiceFiscale("MMMMMMMMM");
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        op = new Operatore();
        op.setCodiceFiscale("UUUUU");
        op.setCognome("Man");
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        Assert.assertEquals("Manca", operatori.get(0).getCognome());
        Assert.assertEquals("Francesco", operatori.get(0).getNome());
        op = new Operatore();
        op.setCodiceFiscale("AAA");
        op.setCognome("Sac");
        op.setNome("Salva");
        op.setRepartoCompetenza(repartoCompetenza);
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(0, operatori.size());
        op = new Operatore();
        op.setCodiceFiscale("AAA");
        op.setCognome("Sac");
        op.setNome("Salva");
        op.setRepartoCompetenza(reparto);
        operatori = pianoTurnoDao.searchOperatori(op);
        Assert.assertNotNull(operatori);
        Assert.assertEquals(1, operatori.size());
        Assert.assertEquals("Saccone", operatori.get(0).getCognome());
        Assert.assertEquals("Salvatore", operatori.get(0).getNome());
        Assert.assertEquals("AAAAAAA19", operatori.get(0).getCodiceFiscale());
    }

    private static PianoTurno createPianoTurnoForTest() throws ParseException {
        PianoTurno pianoTurno = new PianoTurno();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        pianoTurno.setDataInizio(sdf.parse("01/01/2009"));
        pianoTurno.setDataFine(sdf.parse("31/01/2009"));
        pianoTurno.setDescrizione("01/01/2009 - 31/01/2009");
        pianoTurno.getOperatoriPianoTurno().add(operatorePianoTurnoForTest);
        pianoTurno.setReparto(repartoForTest);
        return pianoTurno;
    }

    private static Reparto createRepartoForTest() {
        Reparto reparto = new Reparto();
        reparto.setCodice("otlrngtr");
        reparto.setDescrizione("Otorinolaringoiatra");
        reparto.setNomeDipartimento("nomeDipartimento");
        return reparto;
    }

    private static Reparto createRepartoForTestOperatore(String descrizione) {
        Reparto reparto = new Reparto();
        reparto.setCodice("codice");
        reparto.setDescrizione(descrizione);
        reparto.setNomeDipartimento("nomeDipartimento");
        return reparto;
    }

    private static Operatore createOperatoreForTest() {
        Operatore operatore = new Operatore();
        operatore.setCodiceFiscale("codicefiscale");
        operatore.setCognome("cognome");
        operatore.setNome("Nome");
        operatore.setRepartoCompetenza(repartoForTest);
        operatore.setQualifica(qualificaForTest);
        operatore.setContrattoReparto(contrattoReparto);
        return operatore;
    }

    private static Qualifica createQualificaForTest() {
        Qualifica qualifica = new Qualifica();
        qualifica.setCodice("cod");
        qualifica.setDescrizione("descr");
        return qualifica;
    }

    private static Turno createTurnoForTest() {
        Turno turno = new Turno();
        turno.setCodice("T");
        turno.setDescrizione("Mattina");
        turno.setOraInizio(new Orario(07, 00));
        turno.setOraFine(new Orario(14, 00));
        return turno;
    }

    private static OperatorePianoTurno createOperatorePianoTurnoForTest() {
        OperatorePianoTurno opPianoTurno = new OperatorePianoTurno();
        opPianoTurno.setOperatore(operatoreForTest);
        opPianoTurno.getTurniOperatore().add(turnoOperatoreForTest);
        return opPianoTurno;
    }

    private static TurnoOperatore createTurnoOperatoreForTest() throws ParseException, DalException {
        TurnoOperatore turnoOperatore = new TurnoOperatore();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        turnoOperatore.setTurno(pianoTurnoDao.loadTurno("T"));
        turnoOperatore.setDataTurno(sdf.parse("01/01/2009"));
        return turnoOperatore;
    }

    private static Contratto createRegoleContrattuali() {
        Contratto newContratto = new Contratto();
        newContratto.setId(430);
        newContratto.setNome("430");
        return newContratto;
    }

    private static ContrattoReparto createContrattoReparto(Contratto regoleContrattuali) {
        ContrattoReparto contrattoReparto = new ContrattoReparto();
        contrattoReparto.setId(100000 + regoleContrattuali.getId());
        contrattoReparto.setReparto(repartoForTest);
        contrattoReparto.setContratto(regoleContrattuali);
        return contrattoReparto;
    }
}
