package it.webscience.kpeople.client.activiti.exception;

/**
 * Classe relativa ad un'eccezione generica del livello BLL.
 */
public class KPeopleActivitiExecuteTaskException extends KPeopleGenericActivitiException {

    /**
     * Auto generated version UID.
     */
    private static final long serialVersionUID = 5411645337875261011L;

    /**
     * Costruttore che definisce il messaggio dell'eccezione.
     * @param message messaggio dell'eccezione
     */
    public KPeopleActivitiExecuteTaskException(final String message) {
        super(message);
    }
}
