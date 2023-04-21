package it.unipg.bipod.dataAccess;

import it.unipg.bipod.dataModel.*;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;
import java.util.Hashtable;
import javax.naming.*;
import javax.sql.DataSource;

/**
 * DBSharedDataManager fornisce una implementazione di SharedDataManger che gestisce i
 * daticondivisi di BiPoD tramite database relazionale.<br>
 * La connessione al database viene presa dalla DataSource di Glassfish specificata
 * nel campo "dataSource" delle impostazioni passate al metodo {@code connect()}.<br>
 * Tutti i metodi di DBSharedDataManager lanciano una DataAccessException se riscontrano
 * un qualche errore in fase di accesso al database.<br>
 * Gli accessi al database vengono eseguiti tramite PreparedStatements: la prima volta
 * che un'operazione viene eseguita si crea un nuovo PreparedStatement che viene poi
 * salvato in una cache dalla quale viene recuperato ogni volta che quell'operazione
 * va eseguita nuovamente.
 * 
 * @author Lorenzo Porzi
 * @see SharedDataManager
 *
 */
class DBSharedDataManager implements SharedDataManager {

    private Connection connection;

    private Hashtable<String, PreparedStatement> statements;

    /**
	 * Crea un DBSharedDataManager. Prima di essere utilizzato è necessario iniziallizzare
	 * la connesione al database tramite il metodo {@code connect()}.
	 */
    public DBSharedDataManager() {
        statements = new Hashtable<String, PreparedStatement>();
    }

    /**
	 * Si connette al database sulla base delle impostazioni passate come argomento.<br>
	 * In particolare l'argomento deve contenere un campo "dataSource" con il nome
	 * del DataSource a cui accedere.
	 * 
	 * @param settings un {@code Properties} di impostazioni.
	 * @throws DataAccessException
	 */
    @Override
    public void connect(Properties settings) throws DataAccessException {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(settings.getProperty("dataSource"));
            connection = dataSource.getConnection();
            addStatement("getUtente", "SELECT * FROM Utente WHERE nomeUtente = ?");
            addStatement("addUtente", "INSERT INTO Utente (nomeUtente, email, nome, cognome, password, qualifica, ssd) VALUES (?, ?, ?, ?, ?, ?, ?)");
            addStatement("updateUtente", "UPDATE Utente SET email = ?, nome = ?, cognome = ?, password = ?, qualifica = ?, ssd = ? WHERE nomeUtente = ?");
            addStatement("removeUtente", "DELETE FROM Utente WHERE nomeUtente = ?");
            addStatement("allDocente", "SELECT * FROM Utente WHERE NOT qualifica = 'segretario'");
            addStatement("allSegretario", "SELECT nomeUtente, email, nome, cognome, password FROM Utente WHERE qualifica = 'segretario'");
            addStatement("allUtente", "SELECT * FROM Utente");
        } catch (NamingException e) {
            throw new DataAccessException(e);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Utente getUtenteFromNomeUtente(String nomeUtente) throws DataAccessException {
        try {
            PreparedStatement getUtente = statements.get("getUtente");
            getUtente.setString(1, nomeUtente);
            ResultSet resultSet = getUtente.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("qualifica").equals("segretario")) {
                    Segretario segretario = new Segretario(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password"));
                    return segretario;
                } else {
                    Docente docente = new Docente(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password"), resultSet.getString("ssd"), resultSet.getString("qualifica"));
                    return docente;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Docente[] getAllDocente() throws DataAccessException {
        try {
            PreparedStatement allDocente = statements.get("allDocente");
            ResultSet resultSet = allDocente.executeQuery();
            Vector<Docente> vectorDocenti = new Vector<Docente>();
            while (resultSet.next()) {
                vectorDocenti.add(new Docente(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password"), resultSet.getString("ssd"), resultSet.getString("qualifica")));
            }
            Docente[] docenti = new Docente[vectorDocenti.size()];
            vectorDocenti.toArray(docenti);
            return docenti;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Segretario[] getAllSegretario() throws DataAccessException {
        try {
            PreparedStatement allSegretario = statements.get("allSegretario");
            ResultSet resultSet = allSegretario.executeQuery();
            Vector<Segretario> vectorSegretari = new Vector<Segretario>();
            while (resultSet.next()) {
                vectorSegretari.add(new Segretario(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password")));
            }
            Segretario[] segretari = new Segretario[vectorSegretari.size()];
            vectorSegretari.toArray(segretari);
            return segretari;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Utente[] getAllUtente() throws DataAccessException {
        try {
            PreparedStatement allUtente = statements.get("allUtente");
            ResultSet resultSet = allUtente.executeQuery();
            Vector<Utente> vectorUtenti = new Vector<Utente>();
            while (resultSet.next()) {
                if (resultSet.getString("qualifica").equals("segretario")) {
                    vectorUtenti.add(new Segretario(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password")));
                } else {
                    vectorUtenti.add(new Docente(resultSet.getString("nomeUtente"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getString("password"), resultSet.getString("ssd"), resultSet.getString("qualifica")));
                }
            }
            Utente[] utenti = new Utente[vectorUtenti.size()];
            vectorUtenti.toArray(utenti);
            return utenti;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean insertUtente(Utente utente) throws DataAccessException {
        try {
            PreparedStatement addUtente = statements.get("addUtente");
            addUtente.setString(1, utente.getNomeUtente());
            addUtente.setString(2, utente.getEmail());
            addUtente.setString(3, utente.getNome());
            addUtente.setString(4, utente.getCognome());
            addUtente.setString(5, utente.getPassword());
            if (utente instanceof Segretario) {
                addUtente.setString(6, "segretario");
                addUtente.setString(7, "");
            } else if (utente instanceof Docente) {
                addUtente.setString(6, ((Docente) utente).getQualifica());
                addUtente.setString(7, ((Docente) utente).getSsd());
            }
            int resultSize = addUtente.executeUpdate();
            if (resultSize > 0) return true; else return false;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean removeUtente(String nomeUtente) throws DataAccessException {
        try {
            PreparedStatement removeUtente = statements.get("removeUtente");
            removeUtente.setString(1, nomeUtente);
            int resultSize = removeUtente.executeUpdate();
            if (resultSize > 0) return true; else return false;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean updateUtente(Utente utente) throws DataAccessException {
        try {
            PreparedStatement updateUtente = statements.get("updateUtente");
            updateUtente.setString(7, utente.getNomeUtente());
            updateUtente.setString(1, utente.getEmail());
            updateUtente.setString(2, utente.getNome());
            updateUtente.setString(3, utente.getCognome());
            updateUtente.setString(4, utente.getPassword());
            if (utente instanceof Segretario) {
                updateUtente.setString(5, "segretario");
                updateUtente.setString(6, "");
            } else if (utente instanceof Docente) {
                updateUtente.setString(5, ((Docente) utente).getQualifica());
                updateUtente.setString(6, ((Docente) utente).getSsd());
            }
            int resultSize = updateUtente.executeUpdate();
            if (resultSize > 0) return true; else return false;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
	 * Aggiunge un prepared statement alla tabella
	 * 
	 * @param nome La key da associare allo statement
	 * @param sql Il comando sql del prepared statement
	 * @return il PreparedStatement aggiunto
	 */
    private PreparedStatement addStatement(String nome, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statements.put(nome, statement);
        return statement;
    }
}
