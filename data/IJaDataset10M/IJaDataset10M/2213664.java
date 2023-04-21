package storage;

import GUI.EntityGUI;
import Interface.Entity;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import session.EntitySession;

/**
 * 
 * @author Alessandro Pollace
 * 
 * @version 1.0
 */
public class StorageInterface {

    private QueryGenerator qg = new QueryGenerator();

    private DBConnect db;

    /**
     * Questo metodo controlla se la tabella esiste, nel caso in cuo la tabella
     * non esiste allora viene creata.
     *
     * @param e
     * @return True se la tabella non esisteva ed è stata creata, false se la
     *         tabella già esiste.
     * @throws Exception
     */
    private boolean TestAndCreateTable(Entity e) throws Exception {
        String query = "SELECT * FROM " + e.getNome() + ";";
        try {
            this.exeQuery(query);
            return false;
        } catch (Exception e2) {
            query = qg.getQueryCreateTable(e);
            String q[] = query.split(";");
            for (int i = 0; i < q.length; i++) {
                this.exeQuery(q[i] + ";");
            }
        }
        return true;
    }

    private void exeQuery(String query) throws Exception {
        db.openConnection();
        try {
            db.exeQuery(query);
        } finally {
            db.closeConnection();
        }
    }

    private Entity[] exeQuery(String query, Entity e) throws Exception {
        ResultSet rs;
        String user, group;
        String attributeName[];
        Collection c = new ArrayList();
        db.openConnection();
        try {
            rs = db.exeQuery(query);
        } finally {
            db.closeConnection();
        }
        attributeName = e.getAllNameAttribute();
        EntityGUI s;
        s = (EntityGUI) e;
        user = s.user;
        group = s.group;
        try {
            while (rs.next()) {
                for (int i = 0; i < attributeName.length; i++) {
                    s.setAttributeValueSuperMode(attributeName[i], rs.getObject(attributeName[i]));
                }
                c.add(s);
                s = new EntityGUI(e.getNome(), user, group);
            }
        } catch (Exception e2) {
            throw new Exception("Error-StorageInterface-load-ResultSetEmpy");
        }
        Entity EntityArray[] = (Entity[]) c.toArray(new Entity[c.size()]);
        return EntityArray;
    }

    /**
     * Costruttore della classe Storage Interface
     * @throws Exception Impossibile creare l'oggetto DBConnect
     */
    public StorageInterface() throws Exception {
        this.db = new DBConnect();
    }

    /**
     * Permette di aggiungere una nuova EntityStorage al database.
     *
     * @param e
     *            EntityStorage su cui lavorare
     * @throws Exception Impossibile portare a completamento l'inserimento
     */
    public void add(Entity e) throws Exception {
        String query;
        query = qg.getQuerySalvataggio(e);
        try {
            this.exeQuery(query);
        } catch (Exception e2) {
            if (this.TestAndCreateTable(e)) {
                this.exeQuery(query);
            } else {
                throw new Exception("Error-StorageInterface-add-AfterCreateTableErrorPersistPleseCheckQuery:PROBABLY-theEntitysAlradyExist");
            }
        }
    }

    /**
     * Permette di caricare un'EntityStorage dal database.
     *
     * @param e
     *            EntityStorage su cui è avvalorata la chiave primaria
     * @return EntityStorage caricata
     * @throws Exception Entity da caricare non trovata
     */
    public Entity load(Entity e) throws Exception {
        String query;
        query = qg.getQueryCaricamento(e);
        return this.exeQuery(query, e)[0];
    }

    /**
     * Permette di aggiornare una entity modificata.
     *
     * @param e
     *            EntityStorage su cui lavorare
     * @throws Exception impossibile aggiornare, magari si è tentato di modificare un attributo di cui è richista l'unicità ma la modifica viola questo principio
     */
    public void update(Entity e) throws Exception {
        String query;
        EntityStorage e2 = (EntityStorage) e;
        if (e.getAllNameAttributeModified().length > 0) {
            query = qg.getQueryModifica(e);
            this.exeQuery(query);
            e2.resetModifyFlag();
        }
    }

    /**
     * Questo metodo permette di cancellare una entity dal database.
     *
     * @param e
     *            Entty su cui lavorare.
     * @throws Exception Impossibile cancellare, l'entiy selezionata per il cancellamento o non esiste oppura ha delle entity di tipo diversa ed essa collegate
     */
    public void del(Entity e) throws Exception {
        String query;
        query = qg.getQueryCancellazione(e);
        this.exeQuery(query);
    }

    /**
     * questo metodo permette di effettuare la ricerca sulla base di dati
     * @param e Enntity su cui lavorare, l'Entity deve avere caricati gli attributi secondo la specifica degli argomenti
     * @param Arguments Argomenti per la ricerca
     * @return Array di entity corrispondenti ai criteri di ricerca
     * @throws Exception Impossibile effettuare la ricerca
     */
    public Entity[] find(Entity e, String Arguments[]) throws Exception {
        String query;
        query = qg.getQueryRicerca(e, Arguments);
        return this.exeQuery(query, e);
    }

    /**
     * Questa classe permette di recuperare la caption di un attributo di
     * una EntityStorage
     * @param NameEntity Nome dell'entity
     * @param NameAttribute Nome dell'attributo dell'entity
     * @return Caption in default language of system
     */
    public String getCaption(String NameEntity, String NameAttribute) throws Exception {
        ConfigLoader config = new ConfigLoader();
        return config.getSingleAttributeCaption(NameEntity, NameAttribute);
    }

    /**
     * Questa classe permette di recuperare la descrizione di un attributo di
     * una EntityStorage
     * @param NameEntity Nome dell'entity
     * @param NameAttribute Nome dell'attributo dell'entity
     * @return Descrizione in default language of system
     */
    public String getDescription(String NameEntity, String NameAttribute) throws Exception {
        ConfigLoader config = new ConfigLoader();
        return config.getSingleAttributeDescriptionm(NameEntity, NameAttribute);
    }

    /**
     * Questa classe permette di recuperare la descrizione di un attributo di
     * Sistema
     * @param NameAttribute Nome dell'attributo
     * @return Caption in default language of system
     */
    public String getSystemControlCaption(String NameAttribute) throws Exception {
        ConfigLoader config = new ConfigLoader();
        return config.getSystemControlCaption(NameAttribute);
    }
}
