package it.unipg.bipod.applicationLogic;

import it.unipg.bipod.dataAccess.*;
import it.unipg.bipod.dataModel.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * SegretarioApplication gestisce le operazioni che un segretario puo' compiere su BiPoD.<br>
 * Ogni istanza di SegretarioApplication e' associata a una sessione di accesso di un segretario
 * al sistema.<br>
 * Tutti i metodi di questa classe lanciano un'eccezione di tipo {@link ApplicationException}
 * quando l'operazione non può essere compiuta a causa di un errore. Gli errori dovuti
 * a motivi "fisiologici" (come ad esempio record già presenti nelle basi di dati o simili)
 * sono sottoclassi di {@link ApplicationException}, mentre gli altri sono istanze di
 * {@link ApplicationException} contenenti una breve descrizione sulla natura dell'errore.
 * In generale la descrizione delle eccezioni non contiene dati sensibili, percui puo'
 * essere anche direttamente visualizata all'utente.<br>
 * Tutti i metodi si aspettano che gli argomenti siano già ben formati, quindi non
 * effettuano nessun controllo in questo senso.<br>
 * Molte delle informazioni utilizzate da questa classe vengono ricavate tramite {@code ResManager}.
 * La descrizione di ogni metodo fornisce un elenco delle proprieta' richieste per
 * il suo funzionamento.<br>
 * 
 * @author Lorenzo Porzi
 * @see Application
 * @see AuthManager
 */
public class SegretarioApplication extends Application {

    private Segretario segretario;

    private Registrazione registrazione;

    /**
	 * Costruisce un SegretarioApplication associato a un Segretario ed a una Registrazione.<br>
	 * Il SegretarioApplication risultante utilizzera' il DataManager specificato.
	 * 
	 * @param dataManager il DataManager che questo oggetto utilizzara' per svolgere
	 * le sue operazioni
	 * @param segretario il segretario da associare a questo oggetto
	 * @param registrazione la registrazione da associare a questo oggetto
	 * @throws IOException
	 */
    SegretarioApplication(DataManager dataManager, Segretario segretario, Registrazione registrazione) throws IOException {
        super(dataManager);
        this.segretario = segretario;
        this.registrazione = registrazione;
    }

    /**
	 * Esegue la registrazione di un nuovo docente a BiPoD.<br>
	 * Viene inserita un'entita' Docente nella base di dati condivisa e un'entita'
	 * Registrazione nella base di dati locale. Inoltre viene generata una nuova password
	 * inviata immediatamente tramite email al docente.<br>
	 * Nel caso in cui nella base di dati condivisa esista gia' un Docente avente lo stesso nomeUtente
	 * del docente che si sta tentando di registrare il metodo lancia una StatoDocenteException.
	 * In quel caso si dovrà utilizzare il metodo nuovaRegistrazione() che crea solamente una nuova
	 * registrazione per un docente gia' registrato per altri servizi. Diversi errori possibili vengono segnalati lanciando
	 * una StatoDocenteException con il seguente campo statoDocente:
	 * <ul>
	 *   <li>PRESENTE : il docente e' gia' presente nella base di dati condivisa.</li>
	 *   <li>REGISTRATO : il docente e' gia' registrato a BiPoD ma non ha un record nella base di dati condivisa.</li>
	 * </ul>
	 * Questo metodo utilizza la proprieta' passwordLength: lunghezza della password generata.<br>
	 * Questo metodo utilizza la proprieta' newDocenteMailSubject: soggetto dell'email inviata al docente.<br>
	 * Questo metodo utilizza la risorsa newDocenteMail: testo dell'email da inviare al docente.
	 * 
	 * @param docente il docente da registrare.
	 * @throws StatoDocenteException quando si verifica un errore relativo allo stato del docente.
	 * Ulteriori informazioni sul tipo di errore generato si possono ricavare dal campo statoDocente
	 * dell'eccezione sollevata.
	 * @throws ApplicationException
	 */
    public void nuovoDocente(Docente docente) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            String nomeUtente = String.valueOf(docente.getNome().charAt(0)).toLowerCase() + ".";
            nomeUtente += docente.getCognome().toLowerCase();
            docente.setNomeUtente(nomeUtente);
            String password = AuthManager.creaPassword(Integer.parseInt(getSettings().getProperty("passwordLength", "8")));
            String passwordHash = AuthManager.digest(password);
            docente.setPassword(passwordHash);
            if (dataManager.getUtenteFromNomeUtente(docente.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.PRESENTE);
            if (dataManager.getRegistrazioneFromUtente(docente.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.REGISTRATO);
            dataManager.insertEntity(docente);
            Registrazione registrazione = new Registrazione(0, docente.getNomeUtente(), new Timestamp(System.currentTimeMillis()), "docente", true);
            dataManager.insertEntity(registrazione);
            Logger.log(dataManager, Operazione.NUOVA_REGISTRAZIONE, getRegistrazione(), docente.getNomeUtente() + " aggiunto e registrato al servizio");
            String testoEmail = getResManager().getResource("newDocenteMail");
            testoEmail = testoEmail.replaceAll("<password>", password);
            testoEmail = testoEmail.replaceAll("<nomeUtente>", docente.getNomeUtente());
            String soggetto = getSettings().getProperty("newDocenteMailSubject");
            if (!Mailer.sendMail(testoEmail, soggetto, docente.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Registra un docente gia' presente nella base di dati condivisa a BiPoD.<br>
	 * Viene inserita una nuova entita' registrazione relativa al docente che si sta
	 * registrando nella base di dati locale. Diversi errori possibili vengono segnalati lanciando
	 * una StatoDocenteException con il seguente campo statoDocente:
	 * <ul>
	 *   <li>NON_PRESENTE : il docente non e' presente nella base di dati condivisa.</li>
	 *   <li>PRESENTE_COME_SEGRETARIO : il nome utente fornito e' quello di un segretario.</li>
	 *   <li>REGISTRATO : il docente e' gia' registrato a bipod.</li>
	 * </ul>
	 * Questo metodo utilizza la risorsa newRegistrazioneMail: testo dell'email da inviare al docente.<br>
	 * Questo metodo utilizza la proprieta' newRegistrazioneMailSubject: soggetto dell'email inviata al docente.
	 * 
	 * @param nomeUtente
	 * @throws ApplicationException
	 * @throws StatoDocenteException
	 */
    public void registraDocente(String nomeUtente) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Utente docenteAsUtente = dataManager.getUtenteFromNomeUtente(nomeUtente);
            if (docenteAsUtente == null) throw new StatoDocenteException(StatoDocenteException.NON_PRESENTE);
            if (docenteAsUtente instanceof Segretario) throw new StatoDocenteException(StatoDocenteException.PRESENTE_COME_SEGRETARIO);
            Docente docente = (Docente) docenteAsUtente;
            if (dataManager.getRegistrazioneFromUtente(nomeUtente) != null) throw new StatoDocenteException(StatoDocenteException.REGISTRATO);
            Registrazione registrazione = new Registrazione(0, nomeUtente, new Timestamp(System.currentTimeMillis()), "docente", true);
            dataManager.insertEntity(registrazione);
            Logger.log(dataManager, Operazione.NUOVA_REGISTRAZIONE, getRegistrazione(), nomeUtente + " registrato al servizio");
            String testoEmail = getResManager().getResource("newRegistrazioneMail");
            String soggetto = getSettings().getProperty("newRegistrazioneMailSubject");
            if (!Mailer.sendMail(testoEmail, soggetto, docente.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Elimina un docente dalla base di dati.<br>
	 * Questo metodo elimina sia l'entità Docente passata come argomento sia la
	 * registrazione ad essa collegata.
	 * 
	 * @param docente il Docente da eliminare.
	 * @throws ApplicationException se si verifica un errore nell'accesso alla base di dati.
	 */
    public void cancellaDocente(Docente docente) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Registrazione registrazione = dataManager.getRegistrazioneFromUtente(docente.getNomeUtente());
            dataManager.removeEntity(registrazione);
            dataManager.removeEntity(docente);
            Logger.log(dataManager, Operazione.CANCELLA_REGISTRAZIONE, getRegistrazione(), segretario.getNomeUtente() + " cancella la registrazione di " + docente.getNomeUtente());
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Elimina un segretario dalla base di dati.<br>
	 * Questo metodo elimina sia l'entità Segretario passata come argomento sia la
	 * registrazione ad essa collegata.
	 * 
	 * @param segretario il Segretario da eliminare.
	 * @throws ApplicationException se si verifica un errore nell'accesso alla base di dati.
	 */
    public void cancellaSegretario(Segretario segretario) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Registrazione registrazione = dataManager.getRegistrazioneFromUtente(segretario.getNomeUtente());
            dataManager.removeEntity(registrazione);
            dataManager.removeEntity(segretario);
            Logger.log(dataManager, Operazione.CANCELLA_REGISTRAZIONE, getRegistrazione(), this.segretario.getNomeUtente() + " cancella la registrazione di " + segretario.getNomeUtente());
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Esegue la registrazione di un nuovo segretario a BiPoD.<br>
	 * Viene inserita un'entita' Segretario nella base di dati condivisa e un'entita'
	 * Registrazione nella base di dati locale. Inoltre viene generata una nuova password
	 * inviata immediatamente tramite email al segretario.<br>
	 * Nel caso in cui nella base di dati condivisa esista gia' un Segretario avente lo stesso nomeUtente
	 * del segretario che si sta tentando di registrare il metodo lancia una StatoDocenteException.
	 * In quel caso si dovrà utilizzare il metodo nuovaRegistrazione() che crea solamente una nuova
	 * registrazione per un segretario gia' registrato per altri servizi. Diversi errori possibili vengono segnalati lanciando
	 * una StatoDocenteException con il seguente campo statoDocente:
	 * <ul>
	 *   <li>PRESENTE : il docente e' gia' presente nella base di dati condivisa.</li>
	 *   <li>REGISTRATO : il docente e' gia' registrato a BiPoD ma non ha un record nella base di dati condivisa.</li>
	 * </ul>
	 * Questo metodo utilizza la proprieta' passwordLength: lunghezza della password generata.<br>
	 * Questo metodo utilizza la proprieta' newSegretarioMailSubject: soggetto dell'email inviata al segretario.<br>
	 * Questo metodo utilizza la risorsa newSegretarioMail: testo dell'email da inviare al segretario.
	 * 
	 * @param segretario il segretario da registrare.
	 * @throws StatoDocenteException quando si verifica un errore relativo allo stato del segretario.
	 * Ulteriori informazioni sul tipo di errore generato si possono ricavare dal campo statoDocente
	 * dell'eccezione sollevata.
	 * @throws ApplicationException
	 */
    public void nuovoSegretario(Segretario segretario) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            String nomeUtente = String.valueOf(segretario.getNome().charAt(0)).toLowerCase() + ".";
            nomeUtente += segretario.getCognome().toLowerCase();
            segretario.setNomeUtente(nomeUtente);
            String password = AuthManager.creaPassword(Integer.parseInt(getSettings().getProperty("passwordLength", "8")));
            String passwordHash = AuthManager.digest(password);
            segretario.setPassword(passwordHash);
            if (dataManager.getUtenteFromNomeUtente(segretario.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.PRESENTE);
            if (dataManager.getRegistrazioneFromUtente(segretario.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.REGISTRATO);
            dataManager.insertEntity(segretario);
            Registrazione registrazione = new Registrazione(0, segretario.getNomeUtente(), new Timestamp(System.currentTimeMillis()), "segretario", true);
            dataManager.insertEntity(registrazione);
            Logger.log(dataManager, Operazione.NUOVA_REGISTRAZIONE, getRegistrazione(), segretario.getNomeUtente() + " aggiunto e registrato al servizio");
            String testoEmail = getResManager().getResource("newSegretarioMail");
            testoEmail = testoEmail.replaceAll("<password>", password);
            testoEmail = testoEmail.replaceAll("<nomeUtente>", segretario.getNomeUtente());
            String soggetto = getSettings().getProperty("newSegretarioMailSubject");
            if (!Mailer.sendMail(testoEmail, soggetto, segretario.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Pubblica un nuovo bando.<br>
	 * Il sistema avvisa tutti i docenti attivi dell'avvenuta pubblicazione.<br>
	 * Questo metodo utilizza la risorsa newBandoMail: testo dell'email da inviare al docente.<br>
	 * Questo metodo utilizza la proprieta' newBandoMailSubject: soggetto dell'email inviata al docente.
	 * 
	 * @param insegnamenti gli insegnamenti appartenenti al bando da pubblicare
	 * @param dataPubblicazione la data di pubblicazione (puo' essere diversa da quella di inserimento)
	 * @param dataScadenza la data di scadenza
	 * @return il bando pubblicato
	 * @throws ApplicationException
	 * @throws IllegalArgumentException se la data di pubblicazione è dopo la data di scadenza o dopo la
	 * data attuale
	 */
    public Bando pubblicaBando(Insegnamento[] insegnamenti, Timestamp dataPubblicazione, Timestamp dataScadenza) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (dataPubblicazione.after(dataScadenza) || dataPubblicazione.before(now)) throw new IllegalArgumentException();
            Bando bando = new Bando(0, dataPubblicazione, dataScadenza, now);
            dataManager.insertEntity(bando);
            for (Insegnamento insegnamento : insegnamenti) insegnamento.setBando(bando.getIdBando());
            for (Insegnamento insegnamento : insegnamenti) dataManager.insertEntity(insegnamento);
            Logger.log(dataManager, Operazione.NUOVO_BANDO, getRegistrazione(), "Inserito un nuovo bando da " + segretario.getNomeUtente());
            Object[] registrazioniAsObject = dataManager.getAll(Registrazione.class);
            String messaggio = getResManager().getResource("newBandoMail");
            String soggetto = getSettings().getProperty("newBandoMailSubject");
            Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            messaggio = messaggio.replaceAll("<scadenza>", dateFormat.format(dataScadenza));
            for (Object registrazioneAsObject : registrazioniAsObject) {
                Registrazione registrazione = (Registrazione) registrazioneAsObject;
                if (registrazione.getRuolo().equals("segretario") || !registrazione.isAttiva()) continue;
                Docente docente = (Docente) dataManager.getUtenteFromNomeUtente(registrazione.getUtente());
                if (!Mailer.sendMail(messaggio, soggetto, docente.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
            }
            return bando;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Aggiunge un nuovo corso alla base di dati.
	 * 
	 * @param nome il nome del corso da inserire.
	 * @throws StatoCorsoException quando si tenta di inserire un corso che gia' esiste
	 * @throws ApplicationException
	 */
    public void nuovoCorso(String nome) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Corso corso;
            if ((corso = dataManager.getCorsoFromNome(nome)) != null) throw new StatoCorsoException(StatoCorsoException.CORSO_PRESENTE);
            corso = new Corso(0, nome);
            dataManager.insertEntity(corso);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Rimuove un corso dalla base di dati.
	 * 
	 * @param corso il corso da rimuovere
	 * @throws StatoCorsoException quando si tenta di cancellare un corso a cui afferiscono
	 * insegnamenti.
	 * @throws ApplicationException
	 */
    public void cancellaCorso(Corso corso) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            if (dataManager.getInsegnamentoFromCorso(corso.getIdCorso()).length > 0) {
                throw new StatoCorsoException(StatoCorsoException.NOME_CORSO_UTILIZZATO);
            }
            dataManager.removeEntity(corso);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Genera una bozza di verbale per il consiglio di facolta'.<br>
	 * L'output avviene in formato RichTextFormat, attraverso un {@code OutputStream}
	 * passato come parametro.
	 * 
	 * @param insegnamenti un array contenente gli insegnamenti da riportare nel verbale
	 * @param docentiScelti un array contenente per ogni insegnamento il docente ad esso assegnato
	 * @param outStream lo stream su cui scrivere il documento
	 * @throws ApplicationException
	 */
    public void generaVerbale(InsegnamentoDocente[] assegnamenti, OutputStream outStream) throws ApplicationException {
        try {
            HashSet<Integer> idCorsiSet = new HashSet<Integer>();
            for (InsegnamentoDocente insegnamento : assegnamenti) idCorsiSet.add(insegnamento.getInsegnamento().getCorso());
            Integer[] idCorsi = new Integer[idCorsiSet.size()];
            idCorsiSet.toArray(idCorsi);
            DatiVerbale datiVerbale = new DatiVerbale();
            DatiVerbale.CorsoInsegnamento[] corsi = new DatiVerbale.CorsoInsegnamento[idCorsi.length];
            for (int i = 0; i < corsi.length; ++i) corsi[i] = datiVerbale.addCorso(getDataManager().getCorsoFromId(idCorsi[i]));
            for (DatiVerbale.CorsoInsegnamento corso : corsi) {
                for (InsegnamentoDocente insegnamento : assegnamenti) if (insegnamento.getInsegnamento().getCorso() == corso.getCorso().getIdCorso()) {
                    DatiVerbale.InsegnamentoDocenti insegnamentoDati = corso.addInsegnamento(insegnamento.getInsegnamento());
                    insegnamentoDati.setAssegnatario(insegnamento.getDocente());
                    Docente[] richiedenti = getDataManager().getDocenteFromInsegnamento(insegnamento.getInsegnamento().getIdInsegnamento());
                    for (Docente richiedente : richiedenti) insegnamentoDati.addRichiedente(richiedente);
                }
            }
            RtfGenerator.generaVerbale(datiVerbale, outStream);
        } catch (DataAccessException e) {
            throw new ApplicationException("Impossibile generare il verbale");
        }
    }

    /**
	 * Imposta lo stato di attivazione di un utente del sistema.
	 * 
	 * @param registrazione la registrazione di cui modificare lo stato di attivazione.
	 * @param attiva il nuovo stato di attivazione da impostare.
	 * @throws ApplicationException
	 */
    public void impostaStatoAttivazione(Registrazione registrazione, boolean attiva) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            registrazione.setAttiva(attiva);
            dataManager.updateEntity(registrazione);
            Logger.log(dataManager, Operazione.MODIFICA_REGISTRAZIONE, getRegistrazione(), "Modificato lo stato di attivazione di " + registrazione.getUtente());
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Aggiorna i dati relativi a un Docente.<br>
	 * Modifica il record della base di dati avente lo stesso nomeUtente
	 * dell'oggetto specificato, aggiornando tutti i campi in modo da rispecchiare quelli
	 * dell'argomento.
	 * 
	 * @param docente il docente i cui dati vanno aggiornati.
	 * @throws ApplicationException
	 */
    public void aggiornaDocente(Docente docente) throws ApplicationException {
        try {
            getDataManager().updateEntity(docente);
            Logger.log(getDataManager(), Operazione.MODIFICA_REGISTRAZIONE, getRegistrazione(), "Modificati i dati di " + docente.getNomeUtente());
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Aggiorna i dati relativi a un Segretario.<br>
	 * Modifica il record della base di dati avente lo stesso nomeUtente
	 * dell'oggetto specificato, aggiornando tutti i campi in modo da rispecchiare quelli
	 * dell'argomento.
	 * 
	 * @param segretario il segretario i cui dati vanno aggiornati.
	 * @throws ApplicationException
	 */
    public void aggiornaSegretario(Segretario segretario) throws ApplicationException {
        try {
            getDataManager().updateEntity(segretario);
            Logger.log(getDataManager(), Operazione.MODIFICA_REGISTRAZIONE, getRegistrazione(), "Modificati i dati di " + segretario.getNomeUtente());
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }

    /**
	 * Recupera tutti i bandi presenti nella base di dati.
	 * 
	 * @return un array contenente tutti i bandi.
	 * @throws ApplicationException
	 */
    public Bando[] getBandi() throws ApplicationException {
        try {
            Object[] bandi = getDataManager().getAll(Bando.class);
            return Arrays.copyOf(bandi, bandi.length, Bando[].class);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutti gli insegnamenti appartenenti a un certo bando.
	 * 
	 * @param bando il bando.
	 * @return un array di {@link InsegnamentoCorso} contenente gli insegnamenti recuperati.
	 * @throws ApplicationException
	 */
    public InsegnamentoCorso[] getInsegnamenti(Bando bando) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Insegnamento[] insegnamenti = dataManager.getInsegnamentoFromBando(bando.getIdBando());
            InsegnamentoCorso[] insegnamentiCorso = new InsegnamentoCorso[insegnamenti.length];
            for (int i = 0; i < insegnamenti.length; ++i) {
                Corso corso = dataManager.getCorsoFromId(insegnamenti[i].getCorso());
                insegnamentiCorso[i] = new InsegnamentoCorso(insegnamenti[i], corso, bando.getDataPubblicazione());
            }
            return insegnamentiCorso;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutti i corsi.
	 * 
	 * @return un array contenente tutti i corsi.
	 * @throws ApplicationException
	 */
    public Corso[] getCorsi() throws ApplicationException {
        try {
            Object[] corsi = getDataManager().getAll(Corso.class);
            return Arrays.copyOf(corsi, corsi.length, Corso[].class);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutte le richieste pervenute per un certo insegnamento.<br>
	 * Le richieste vengono presentate come oggetti {@link RichiestaRegistrazioneDocente}.
	 * 
	 * @param insegnamento l'insegnamento.
	 * @return un array di {@link RichiestaRegistrazioneDocente} con i dati estratti.
	 * @throws ApplicationException
	 */
    public RichiestaRegistrazioneDocente[] getRichieste(Insegnamento insegnamento) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Richiesta[] richieste = dataManager.getRichiestaFromInsegnamento(insegnamento.getIdInsegnamento());
            Docente docente;
            Registrazione registrazione;
            RichiestaRegistrazioneDocente[] richiesteDocente = new RichiestaRegistrazioneDocente[richieste.length];
            for (int i = 0; i < richieste.length; ++i) {
                registrazione = dataManager.getRegistrazioneFromId(richieste[i].getDocente());
                docente = (Docente) dataManager.getUtenteFromNomeUtente(registrazione.getUtente());
                richiesteDocente[i] = new RichiestaRegistrazioneDocente(docente, registrazione, richieste[i]);
            }
            return richiesteDocente;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutte le richieste effettuate da un docente.<br>
	 * Le richieste vengono presentate come oggetti {@link RichiestaRegistrazioneDocente}.
	 * 
	 * @param docente il docente.
	 * @return un array di {@link RichiestaRegistrazioneDocente} con i dati estratti.
	 * @throws ApplicationException
	 */
    public RichiestaRegistrazioneDocente[] getRichieste(Docente docente) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Registrazione registrazione = dataManager.getRegistrazioneFromUtente(docente.getNomeUtente());
            Richiesta[] richieste = dataManager.getRichiestaFromRegistrazione(registrazione.getIdRegistrazione());
            RichiestaRegistrazioneDocente[] richiesteDocente = new RichiestaRegistrazioneDocente[richieste.length];
            for (int i = 0; i < richieste.length; ++i) {
                richiesteDocente[i] = new RichiestaRegistrazioneDocente(docente, registrazione, richieste[i]);
            }
            return richiesteDocente;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutte le operazioni effettuate nel periodo compreso tra le date specificate.
	 * 
	 * @param dataIniziale la data iniziale da cui cercare le operazioni.
	 * @param dataFinale la data finale delle operazioni da restituire.
	 * @return un array di {@link OperazioneUtente} con le operazioni estratte (insieme agli
	 * utenti che le hanno effettuate).
	 * @throws ApplicationException
	 */
    public OperazioneUtente[] getOperazioni(Timestamp dataIniziale, Timestamp dataFinale) throws ApplicationException {
        try {
            if (dataFinale.before(dataIniziale)) throw new IllegalArgumentException();
            DataManager dataManager = getDataManager();
            Object[] operazioniAsObject = dataManager.getAll(Operazione.class);
            Operazione[] operazioni = Arrays.copyOf(operazioniAsObject, operazioniAsObject.length, Operazione[].class);
            Vector<OperazioneUtente> op = new Vector<OperazioneUtente>();
            for (int i = 0; i < operazioni.length; ++i) {
                Registrazione registrazione = dataManager.getRegistrazioneFromId(operazioni[i].getUtente());
                Utente utente = dataManager.getUtenteFromNomeUtente(registrazione.getUtente());
                if (operazioni[i].getDataEsecuzione().before(dataIniziale) || operazioni[i].getDataEsecuzione().after(dataFinale)) continue;
                op.add(new OperazioneUtente(operazioni[i], utente));
            }
            OperazioneUtente[] operazioniUtente = new OperazioneUtente[op.size()];
            op.toArray(operazioniUtente);
            return operazioniUtente;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera una registrazione a partire dal nomeUtente dell'utente associato.
	 * 
	 * @param nomeUtente il nomeUtente dell'utente da cercare.
	 * @return la registrazione trovata oppure {@code null}
	 * @throws ApplicationException
	 */
    public Registrazione getRegistrazione(String nomeUtente) throws ApplicationException {
        try {
            return getDataManager().getRegistrazioneFromUtente(nomeUtente);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera un docente a partire dal suo nomeUtente.
	 * 
	 * @param nomeUtente il nomeUtente del docente da cercare.
	 * @return il docente trovato oppure {@code null}.
	 * @throws ApplicationException
	 */
    public Docente getDocente(String nomeUtente) throws ApplicationException {
        try {
            Utente utente = getDataManager().getUtenteFromNomeUtente(nomeUtente);
            if (utente instanceof Docente) return (Docente) utente;
            return null;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera un bando dalla base di dati.
	 * 
	 * @param idBando l'id del bando da recuperare.
	 * @return il bando recuperato oppure {@code null}.
	 * @throws ApplicationException
	 */
    public Bando getBando(int idBando) throws ApplicationException {
        try {
            return getDataManager().getBandoFromId(idBando);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutti i docenti presenti nella base di dati.
	 * 
	 * @return un array contenente i dati recuperati.
	 * @throws ApplicationException
	 */
    public Docente[] getDocenti() throws ApplicationException {
        try {
            Object[] docenti = getDataManager().getAll(Docente.class);
            return Arrays.copyOf(docenti, docenti.length, Docente[].class);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera tutti i segretari presenti nella base di dati.
	 * 
	 * @return un array contenente i dati recuperati.
	 * @throws ApplicationException
	 */
    public Segretario[] getSegretari() throws ApplicationException {
        try {
            Object[] segretari = getDataManager().getAll(Segretario.class);
            return Arrays.copyOf(segretari, segretari.length, Segretario[].class);
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Recupera l'insegnamento a cui si riferisce la richiesta specificata.
	 * 
	 * @param richiesta la richiesta di cui recuperare l'insegnamento.
	 * @return un oggetto {@link InsegnamentoCorso} con i dati recuperati.
	 * @throws ApplicationException
	 */
    public InsegnamentoCorso getInsegnamento(Richiesta richiesta) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            Insegnamento insegnamento = dataManager.getInsegnamentoFromId(richiesta.getInsegnamento());
            Corso corso = dataManager.getCorsoFromId(insegnamento.getCorso());
            Bando bando = dataManager.getBandoFromId(insegnamento.getBando());
            InsegnamentoCorso insegnamentoCorso = new InsegnamentoCorso(insegnamento, corso, bando.getDataPubblicazione());
            return insegnamentoCorso;
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        }
    }

    /**
	 * Ritorna l'entita' segretario associata a questo oggetto.
	 * 
	 * @return un oggetto {@link Segretario}
	 */
    public Segretario getSegretario() {
        return segretario;
    }

    /**
	 * Ritorna l'entita' registrazione associata a questo oggetto.
	 * 
	 * @return un oggetto {@link Registrazione}
	 */
    public Registrazione getRegistrazione() {
        return registrazione;
    }
}
