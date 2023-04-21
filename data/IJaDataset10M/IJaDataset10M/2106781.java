package it.unipg.bipod.dataAccess;

import java.util.Properties;
import java.util.Vector;
import java.io.*;
import it.unipg.bipod.ResManager;
import it.unipg.bipod.dataModel.*;

/**
 * DataManager costituisce il punto di accesso per lo strato di accesso ai dati.<br>
 * Fornisce metodi per cercare, aggiungere, eliminare e modificare entita'. Per le operazioni
 * vere e proprie si appoggia a un LocalDataManager per l'accesso ai dati locali
 * del sistema e ad uno SharedDataManager per l'accesso ai dati condivisi con
 * altre applicazioni.<br>
 * DataManager utilizza alcune impostazioni tratte dal file data.ini:
 * <ul>
 *   <li><b>localDataManager</b>: il nome dell'implementazione di LocalDataManager da utilizzare.</li>
 *   <li><b>sharedDataManager</b>: il nome dell'implementazione di SharedDataManager da utilizzare.</li>
 * </ul>
 * 
 * @author Lorenzo Porzi
 * @see LocalDataManager
 * @see SharedDataManager
 *
 */
public class DataManager {

    private Properties settings;

    private LocalDataManager local;

    private SharedDataManager shared;

    /**
	 * Crea un {@code DataManager} caricando le impostazioni dal file "data.ini".
	 * 
	 * @throws DataAccessException
	 */
    public DataManager() throws DataAccessException {
        try {
            settings = ResManager.getInstance().getDataSettings();
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
        try {
            local = (LocalDataManager) Class.forName(settings.getProperty("localDataManager")).newInstance();
            local.connect(settings);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        try {
            shared = (SharedDataManager) Class.forName(settings.getProperty("sharedDataManager")).newInstance();
            shared.connect(settings);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
	 * Cerca nella base di dati locale il bando avente l'id specificato.
	 * 
	 * @param idBando l'id del bando da cercare.
	 * @return il bando ottenuto.
	 * @throws DataAccessException
	 */
    public Bando getBandoFromId(int idBando) throws DataAccessException {
        Bando bando = new Bando();
        if (local.getEntityFromId(bando, idBando)) return bando; else return null;
    }

    /**
	 * Cerca nella base di dati locale il corso avente l'id specificato.
	 * 
	 * @param idCorso l'id del corso da cercare.
	 * @return il corso ottenuto.
	 * @throws DataAccessException
	 */
    public Corso getCorsoFromId(int idCorso) throws DataAccessException {
        Corso corso = new Corso();
        if (local.getEntityFromId(corso, idCorso)) return corso; else return null;
    }

    /**
	 * Cerca nella base di dati locale l'insegnamento avente l'id specificato.
	 * 
	 * @param idInsegnamento l'id dell'insegnamento da cercare.
	 * @return l'insegnamento ottenuto.
	 * @throws DataAccessException
	 */
    public Insegnamento getInsegnamentoFromId(int idInsegnamento) throws DataAccessException {
        Insegnamento insegnamento = new Insegnamento();
        if (local.getEntityFromId(insegnamento, idInsegnamento)) return insegnamento; else return null;
    }

    /**
	 * Cerca nella base di dati locale l'operazione avente l'id specificato.
	 * 
	 * @param idOperazione l'id dell'operazione da cercare.
	 * @return l'operazione ottenuta.
	 * @throws DataAccessException
	 */
    public Operazione getOperazioneFromId(int idOperazione) throws DataAccessException {
        Operazione operazione = new Operazione();
        if (local.getEntityFromId(operazione, idOperazione)) return operazione; else return null;
    }

    /**
	 * Cerca nella base di dati locale la registrazione avente l'id specificato.
	 * 
	 * @param idRegistrazione l'id della registrazione da cercare.
	 * @return la registrazione ottenuta.
	 * @throws DataAccessException
	 */
    public Registrazione getRegistrazioneFromId(int idRegistrazione) throws DataAccessException {
        Registrazione registrazione = new Registrazione();
        if (local.getEntityFromId(registrazione, idRegistrazione)) return registrazione; else return null;
    }

    /**
	 * Cerca nella base di dati locale la richiesta avente l'id specificato.
	 * 
	 * @param idRichiesta l'id della richiesta da cercare.
	 * @return la richiesta ottenuta.
	 * @throws DataAccessException
	 */
    public Richiesta getRichiestaFromId(int idRichiesta) throws DataAccessException {
        Richiesta richiesta = new Richiesta();
        if (local.getEntityFromId(richiesta, idRichiesta)) return richiesta; else return null;
    }

    /**
	 * Cerca nella base di dati condivisa l'utente avente il nomeUtente specificato.
	 * 
	 * @param nomeUtente il nomeUtente dell'entita' Utente da cercare.
	 * @return l'entita' Utente trovato oppure {@code null}.
	 * @throws DataAccessException
	 */
    public Utente getUtenteFromNomeUtente(String nomeUtente) throws DataAccessException {
        return shared.getUtenteFromNomeUtente(nomeUtente);
    }

    /**
	 * Recupera tutte le entita' del tipo specificato.
	 * 
	 * @param entityClass la classe dell'entita' da recuperare.
	 * @return un array di {@code Object} contenente le entita' recuperate.
	 * @throws DataAccessException
	 */
    public Object[] getAll(Class<?> entityClass) throws DataAccessException {
        if (entityClass.equals(Docente.class)) {
            return shared.getAllDocente();
        } else if (entityClass.equals(Segretario.class)) {
            return shared.getAllSegretario();
        } else if (entityClass.equals(Utente.class)) {
            return shared.getAllUtente();
        } else {
            return local.getAll(entityClass);
        }
    }

    /**
	 * Cerca nella base di dati locale gli Insegnamenti aventi il campo Bando specificato.
	 * 
	 * @param idBando l'id del Bando.
	 * @return un array contenente gli Insegnamenti trovati.
	 * @throws DataAccessException
	 */
    public Insegnamento[] getInsegnamentoFromBando(int idBando) throws DataAccessException {
        return local.getInsegnamentoFromBando(idBando);
    }

    /**
	 * Cerca nella base di dati locale gli Insegnamenti aventi il campo Corso specificato.
	 * 
	 * @param idCorso l'id del Corso.
	 * @return un array contenente gli Insegnamenti trovati.
	 * @throws DataAccessException
	 */
    public Insegnamento[] getInsegnamentoFromCorso(int idCorso) throws DataAccessException {
        return local.getInsegnamentoFromCorso(idCorso);
    }

    /**
	 * Cerca nella base di dati locale le Operazioni aventi il campo Utente specificato.
	 * 
	 * @param idUtente l'id dell'Utente.
	 * @return un array contenente le Operazioni trovate.
	 * @throws DataAccessException
	 */
    public Operazione[] getOperazioneFromRegistrazione(int idUtente) throws DataAccessException {
        return local.getOperazioneFromRegistrazione(idUtente);
    }

    /**
	 * Cerca nella base di dati locale la Registrazione avente il campo Utente specificato.
	 * 
	 * @param utente il campo Utente da cercare.
	 * @return la Registrazione trovata.
	 * @throws DataAccessException
	 */
    public Registrazione getRegistrazioneFromUtente(String utente) throws DataAccessException {
        return local.getRegistrazioneFromUtente(utente);
    }

    /**
	 * Cerca nella base di dati locale le Richieste aventi il campo Docente specificato.
	 * 
	 * @param idDocente l'id del Docente.
	 * @return un array contenente le Richieste trovate.
	 * @throws DataAccessException
	 */
    public Richiesta[] getRichiestaFromRegistrazione(int idDocente) throws DataAccessException {
        return local.getRichiestaFromRegistrazione(idDocente);
    }

    /**
	 * Cerca nella base di dati locale le Richieste aventi il campo Insegnamento specificato.
	 * 
	 * @param idInsegnamento l'id dell'Insegnamento.
	 * @return un array contenente le Richieste trovate.
	 * @throws DataAccessException
	 */
    public Richiesta[] getRichiestaFromInsegnamento(int idInsegnamento) throws DataAccessException {
        return local.getRichiestaFromInsegnamento(idInsegnamento);
    }

    /**
	 * Cerca nelle basi di dati tutti i Docenti che hanno fatto richiesta per un certo insegnamento.
	 * 
	 * @param idInsegnamento l'id dell'Insegnamento.
	 * @return un array contenente i Docenti trovati.
	 * @throws DataAccessException
	 */
    public Docente[] getDocenteFromInsegnamento(int idInsegnamento) throws DataAccessException {
        Vector<Docente> docenti = new Vector<Docente>();
        Registrazione[] r = local.getRegistrazioneFromInsegnamento(idInsegnamento);
        for (Registrazione reg : r) docenti.add((Docente) shared.getUtenteFromNomeUtente(reg.getUtente()));
        Docente[] out = new Docente[docenti.size()];
        docenti.toArray(out);
        return out;
    }

    /**
	 * Memorizza un'entita'. Il metodo riconosce automaticamente se accedere alla base
	 * di dati locale o a quella condivisa.
	 * 
	 * @param entity L'entita' da memorizzare.
	 * @return {@code true} se l'operazione va a buon fine, {@code false} altrimenti.
	 * @throws DataAccessException
	 */
    public boolean insertEntity(Object entity) throws DataAccessException {
        if (entity instanceof Utente) {
            return shared.insertUtente((Utente) entity);
        } else {
            return local.insertEntity(entity);
        }
    }

    /**
	 * Aggiorna un'entita'. Il metodo riconosce automaticamente se accedere alla base
	 * di dati locale o a quella condivisa.<br>
	 * Più precisamente il metodo aggiorna il record avente chiave primaria uguale
	 * a quella dell'entita' specificata rendendo tutti i campi uguali a
	 * quelli dell'argomento.
	 * 
	 * @param entity l'entita' da aggiornare.
	 * @return {@code true} se l'operazione va a buon fine, {@code false} altrimenti.
	 * @throws DataAccessException
	 */
    public boolean updateEntity(Object entity) throws DataAccessException {
        if (entity instanceof Utente) {
            return shared.updateUtente((Utente) entity);
        } else {
            return local.updateEntity(entity);
        }
    }

    /**
	 * Cancella un'entita'. Il metodo stabilisce tramite introspezione se l'entita'
	 * appartiene alla base di dati locale o a quella condivisa.
	 * 
	 * @param entity L'entita' da cancellare.
	 * @return {@code true} se l'operazione va a buon fine, {@code false} altrimenti.
	 * @throws DataAccessException
	 */
    public boolean removeEntity(Object entity) throws DataAccessException {
        if (entity instanceof Utente) {
            return shared.removeUtente(((Utente) entity).getNomeUtente());
        } else {
            return local.removeEntity(entity);
        }
    }

    /**
	 * Cerca un entita' corso avente il campo nome specificato.
	 * 
	 * @param nome il nome da controllare.
	 * @return il corso trovato oppure {@code null}.
	 */
    public Corso getCorsoFromNome(String nome) throws DataAccessException {
        return local.getCorsoFromNome(nome);
    }

    /**
	 * Cerca un entita' {@link Docente} avente l'email specificata.
	 * 
	 * @param email l'email del docente da cercare.
	 * @return il docente trovato oppure {@code null}.
	 * @throws DataAccessException
	 */
    public Utente getUtenteFromEmail(String email) throws DataAccessException {
        Utente[] utenti = shared.getAllUtente();
        for (Utente utente : utenti) if (utente.getEmail().equals(email)) return utente;
        return null;
    }
}
