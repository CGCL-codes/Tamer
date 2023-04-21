package br.ufrj.cad.fwk.exception;

/**
 * Tipo de notificação
 * 
 * @author Maycon
 *
 */
public class EventType {

    public static final EventType FORMATO_INVALIDO = new EventType(BaseEventCodes.FORMATO_INVALIDO);

    public static final EventType VALOR_INVALIDO = new EventType(BaseEventCodes.VALOR_INVALIDO);

    public static final EventType PERCENTUAL_INVALIDO = new EventType(BaseEventCodes.PERCENTUAL_INVALIDO);

    public static final EventType DATA_INVALIDA = new EventType(BaseEventCodes.DATA_INVALIDA);

    /**
	 * prefixo da notificação, a ser precedido pelo prefixo geral
	 */
    private String codigo;

    public EventType(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }
}
