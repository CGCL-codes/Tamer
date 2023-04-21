package de.banh.bibo.model.provider.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import de.banh.bibo.exceptions.CannotDeleteEntityException;
import de.banh.bibo.exceptions.CannotInsertEntityException;
import de.banh.bibo.exceptions.CannotModifyTableException;
import de.banh.bibo.exceptions.CannotSearchEntityException;
import de.banh.bibo.exceptions.CannotUpdateEntityException;
import de.banh.bibo.exceptions.InitializationException;
import de.banh.bibo.model.Exemplarzustand;
import de.banh.bibo.model.ExemplarzustandManager;

public class PgExemplarzustandManager extends PgBaseManager implements ExemplarzustandManager {

    private static Logger logger = Logger.getLogger(PgExemplarzustandManager.class.getName());

    private List<Exemplarzustand> zustaende;

    public PgExemplarzustandManager(PgManager manager) {
        super(manager);
    }

    @Override
    public Exemplarzustand createExemplarzustand(String bezeichnung) {
        return new PgExemplarzustand(bezeichnung);
    }

    private synchronized void clearZustaende() {
        zustaende = null;
    }

    private void loadZustaendeFromDb() throws CannotSearchEntityException {
        if (zustaende == null) {
            synchronized (this) {
                if (zustaende == null) {
                    try {
                        logger.info("Alle Exemplarzustände ermitteln");
                        Statement stm = createStatement();
                        ResultSet rs = stm.executeQuery("select id, bezeichnung, standard" + " from exemplarzustaende order by bezeichnung");
                        zustaende = new LinkedList<Exemplarzustand>();
                        while (rs.next()) {
                            PgExemplarzustand zustand = new PgExemplarzustand(rs.getString(2));
                            zustand.setId(rs.getLong(1));
                            zustand.setStandard(rs.getBoolean(3));
                            zustaende.add(zustand);
                        }
                        rs.close();
                        stm.close();
                        freeConnection();
                    } catch (InitializationException e) {
                        freeConnection();
                        logger.throwing(getClass().getName(), "loadZustaendeFromDb", e);
                        throw new CannotSearchEntityException("Initialisierung zum Ermitteln aller Exemplarzustände fehlgeschlagen!", e);
                    } catch (SQLException e) {
                        freeConnection();
                        logger.throwing(getClass().getName(), "loadZustaendeFromDb", e);
                        throw new CannotSearchEntityException("Ermitteln aller Exemplarzustände!", e);
                    }
                }
            }
        }
    }

    @Override
    public List<Exemplarzustand> getExemplarzustaende() throws CannotSearchEntityException {
        logger.info("Alle Exemplarzustände ermitteln");
        loadZustaendeFromDb();
        return zustaende;
    }

    @Override
    public List<Exemplarzustand> getExemplarzustaende(String suchtext) throws CannotSearchEntityException {
        try {
            logger.info("Exemplarzustände mit dem Suchtext " + suchtext + " ermitteln.");
            StringBuffer sql = new StringBuffer("select id, bezeichnung, standard from exemplarzustaende ");
            if (!suchtext.equals("*") && suchtext.length() > 0) sql.append("where suchvektor @@ to_tsquery('german',?) ");
            sql.append("order by 2");
            PreparedStatement stm = prepareStatement(sql.toString());
            if (!suchtext.equals("*") && suchtext.length() > 0) stm.setString(1, prepareTSearchSuchText(suchtext));
            ResultSet rs = stm.executeQuery();
            List<Exemplarzustand> liste = new LinkedList<Exemplarzustand>();
            while (rs.next()) {
                PgExemplarzustand zustand = new PgExemplarzustand(rs.getString(2));
                zustand.setId(rs.getLong(1));
                zustand.setStandard(rs.getBoolean(3));
                liste.add(zustand);
            }
            rs.close();
            stm.close();
            freeConnection();
            return liste;
        } catch (InitializationException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "getExemplarzustaende", e);
            throw new CannotSearchEntityException("Initialisierung zum Ermitteln aller Exemplarzustände fehlgeschlagen!", e);
        } catch (SQLException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "getExemplarzustaende", e);
            throw new CannotSearchEntityException("Ermitteln aller Exemplarzustände!", e);
        }
    }

    @Override
    public Exemplarzustand getExemplarzustandById(long id) throws CannotSearchEntityException {
        logger.info("Exemplarzustand mit der ID " + id + " ermitteln.");
        if (id == -1) return null;
        for (Exemplarzustand zustand : getExemplarzustaende()) {
            if (zustand.getId() == id) return zustand;
        }
        return null;
    }

    private void updateExemplarzustand(Exemplarzustand zustand) throws CannotUpdateEntityException {
        try {
            logger.info("Update des Exemplarzustands " + zustand.toString());
            PreparedStatement pstm = prepareStatement("update exemplarzustaende set bezeichnung = ?, " + "standard = ? where id = ?");
            pstm.setString(1, zustand.getBezeichnung());
            pstm.setBoolean(2, zustand.isStandard());
            pstm.setLong(3, zustand.getId());
            int n = pstm.executeUpdate();
            freeConnection();
            if (n == 0) {
                throw new CannotUpdateEntityException("Kein Exemplarzustand wurde geupdated!");
            }
            clearZustaende();
        } catch (InitializationException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "updateExemplarzustand", e);
            throw new CannotUpdateEntityException("Initialisierung zum Update des Exemplarzustands " + zustand.toString() + " fehlgeschlagen!", e);
        } catch (SQLException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "updateExemplarzustand", e);
            throw new CannotUpdateEntityException("Update des Exemplarzustands " + zustand.toString() + " fehlgeschlagen!", e);
        }
    }

    private void insertExemplarzustand(Exemplarzustand zustand) throws CannotInsertEntityException {
        try {
            logger.info("Einfügen des Exemplarzustands " + zustand.toString());
            PreparedStatement pstm = prepareStatement("insert into exemplarzustaende (bezeichnung, standard)" + " values(?,?) returning id");
            pstm.setString(1, zustand.getBezeichnung());
            pstm.setBoolean(2, zustand.isStandard());
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                ((PgExemplarzustand) zustand).setId(rs.getLong(1));
                rs.close();
                freeConnection();
            } else {
                freeConnection();
                throw new CannotInsertEntityException("Exemplarzustand " + zustand.toString() + " konnte nicht eingefügt werden!");
            }
            clearZustaende();
        } catch (InitializationException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "insertExemplarzustand", e);
            throw new CannotInsertEntityException("Initialisierung zum Einfügen des Exemplarzustands " + zustand.toString() + " fehlgeschlagen!", e);
        } catch (SQLException e) {
            freeConnection();
            logger.throwing(getClass().getName(), "insertExemplarzustand", e);
            throw new CannotInsertEntityException("Einfügen des Exemplarzustands " + zustand.toString() + " fehlgeschlagen!", e);
        }
    }

    @Override
    public void saveExemplarzustand(Exemplarzustand zustand) throws CannotModifyTableException {
        if (zustand.getId() == -1) {
            insertExemplarzustand(zustand);
        } else {
            updateExemplarzustand(zustand);
        }
    }

    @Override
    public void deleteExemplarzustand(Exemplarzustand zustand) throws CannotDeleteEntityException {
        logger.info("Exemplarzustand " + zustand.toString() + " löschen.");
        delete(zustand, "exemplarzustaende", "Exemplarzustand " + zustand.toString());
        clearZustaende();
    }

    @Override
    public Exemplarzustand getStandardExemplarzustand() throws CannotSearchEntityException {
        logger.info("Standard-Exemplarzustand ermitteln.");
        for (Exemplarzustand zustand : getExemplarzustaende()) {
            if (zustand.isStandard()) return zustand;
        }
        return null;
    }
}
