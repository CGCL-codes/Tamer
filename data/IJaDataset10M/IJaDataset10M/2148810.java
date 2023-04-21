package it.unibo.lmc.pjdbc.database.transaction;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.core.PSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Transaction Schema
 *
 */
public abstract class TSchema {

    protected UUID currentTransactionID;

    /**
	 * Array dei comandi eseguiti durante la transizione corrente
	 */
    protected ArrayList<ParsedCommand> log = new ArrayList<ParsedCommand>();

    /**
	 * Schema 
	 */
    protected PSchema realSchema;

    /**
	 * Database
	 */
    protected PrologDatabase database;

    /**
	 * Costruttore
	 * @param db database core
	 * @param schema schema su cui applicare le richieste
	 */
    public TSchema(PrologDatabase db, PSchema schema) {
        this.realSchema = schema;
        this.database = db;
        this.currentTransactionID = UUID.randomUUID();
    }

    /**
	 * Devo ripristinare una vecchia situazione, quindi rieseguo al contrario le operazioni effettuate
	 * in maniera però "atomica"
	 */
    public abstract void rollback();

    /**
	 * Committo la situzione attuale sul database.
	 */
    public abstract void commit() throws PSQLException;

    public abstract PResultSet applyCommand(ParsedCommand request) throws PSQLException;

    public void close() throws PSQLException {
        this.realSchema.close();
    }
}
