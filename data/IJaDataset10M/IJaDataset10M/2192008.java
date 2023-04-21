package br.com.gentech.commons.model.entity;

/**
 * Representa um erro ocorrido em uma classe de neg�cio. 
 */
public class BusinessException extends Exception {

    private String localizedMsgKey;

    private String[] localizedMsgArgs;

    /**
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
    @Override
    public String getLocalizedMessage() {
        if (localizedMsgKey != null && "".equalsIgnoreCase(localizedMsgKey.trim())) {
            return localizedMsgKey;
        } else {
            return super.getLocalizedMessage();
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Quando tiver uma mensagem a ser acrescentada no Stacktrace
	 * 
	 * @param msg
	 *            Mensagem que deve ser acrescentada no Stacktrace
	 * @param e
	 *            Exce��o causadora
	 */
    public BusinessException(String msg, Exception e) {
        super(msg, e);
    }

    /**
	 * Quando ocorrer um erro de neg�cio, sem ter ocorrido uma exce��o
	 * 
	 * @param msg
	 *            Mensagem que deve ser acrescentada no Stacktrace
	 */
    public BusinessException(String msg) {
        super(msg, null);
    }

    /**
	 * Quando a exce��o n�o for esperada ou n�o tiver mensagem espec�fica para ela
	 * 
	 * @param e
	 *            Exce��o causadora
	 */
    public BusinessException(Exception e) {
        super(e);
    }

    /**
	 * @return the localizedMsgKey
	 */
    public String getLocalizedMsgKey() {
        return localizedMsgKey;
    }

    /**
	 * @param localizedMsgKey the localizedMsgKey to set
	 */
    public void setLocalizedMsgKey(String localizedMsgKey) {
        this.localizedMsgKey = localizedMsgKey;
    }

    /**
	 * @return the localizedMsgArgs
	 */
    public String[] getLocalizedMsgArgs() {
        return localizedMsgArgs;
    }

    /**
	 * @param localizedMsgArgs the localizedMsgArgs to set
	 */
    public void setLocalizedMsgArgs(String[] localizedMsgArgs) {
        this.localizedMsgArgs = localizedMsgArgs;
    }
}
