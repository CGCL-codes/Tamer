package mwt.xml.xdbforms.xformlayer.transactions;

import java.io.InputStream;
import mwt.xml.xdbforms.schemalayer.SchemaDocument;
import mwt.xml.xdbforms.xformlayer.transactions.exception.CommitterServiceCommitEx;
import mwt.xml.xdbforms.xformlayer.transactions.exception.CommitterServiceValidateEx;

/**
 * Valida il documento che è stato passato in input da
 * @author Gianfranco Murador, Matteo Ferri, Cristian Castiglia
 * @copyright (C) 2009, MCG08
 */
public interface CommitterService {

    /**
     * Carica il documento passato in input
     * e lo schema e valida il contenuto passato
     * @param sd schema xml corrispondente al modello
     * @param inputSource stream con il modello in ingresso
     * @throws CommitterServiceValidateEx
     */
    public void validate(SchemaDocument sd, InputStream inputSource) throws CommitterServiceValidateEx;

    /**
    * Inserisce il contenuto nel db
    * @param table nome della tabella
    * @throws mwt.xml.xdbforms.xformlayer.transactions.exception.CommitterServiceCommitEx
    */
    public void insert(String table) throws CommitterServiceCommitEx;

    /**
     * Aggiorna un record della tabella
     * @param table nome della tabella
     * @param keys array di chiavi per identificare la tabella
     * @throws mwt.xml.xdbforms.xformlayer.transactions.exception.CommitterServiceCommitEx
     */
    public void update(String table, Object[] keys) throws CommitterServiceCommitEx;

    /**
     * Ritorna lo statement sql dell'ultima
     * @return
     */
    public String getInsertStmt(String table) throws CommitterServiceCommitEx;

    public String getUpdateStmt(String table) throws CommitterServiceCommitEx;
}
