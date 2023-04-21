package it.unibo.lmc.pjdbc.parser.dml;

public abstract class ParsedCommand {

    protected String defaultSchema;

    public ParsedCommand() {
    }

    /**
	 * Rappresentazione interna delle informazioni del comando richiesto
	 * @return la forma testuale del comando
	 */
    public abstract String toString();
}
