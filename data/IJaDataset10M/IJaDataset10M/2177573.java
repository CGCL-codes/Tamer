package it.unibo.deis.interaction;

/**
 * Questa interfaccia deve essere implementata degli oggetti che vogliono registrarsi come interessati 
 * alla ricezione attiva o passiva di segnali.<br/>
 * Si veda la documentazione dei singoli metodi per il contratto della interfaccia
 * 
 * @see IServiceConnection
 * @author Antonio Natali, Gianfy (redazione javadoc)
 */
public interface IControl {

    /**
 	 * Ogni handler deve ritornare, tramite questo metodo, un nome che lo identifichi univocamente 
 	 * all'interno del suo ambiente di esecuzione.<br/>
 	 * Il nome deve necessariamente iniziare con una lettera minuscola.
 	 * 
 	 * @return Il nome identificativo per l'handler
 	 */
    public String getName();

    /**
 	 * Questo metodo viene invocato 
 	 * 
 	 * @param actionName
 	 * @param message
 	 * @throws Exception
 	 */
    public void handle(String actionName, Object message) throws Exception;
}
