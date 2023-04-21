package org.jcompany.commons;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * @since jCompany 3.0
 * Classe de Exce��o do jCompany que mant�m exce��o "raiz" para
 * tratamento gen�rico recomendado pela Struts conforme O'Reilly<p>
 */
public class PlcException extends Exception {

    private static final long serialVersionUID = -913609256934864491L;

    private Throwable rootCause = null;

    private transient Logger loggerCause = null;

    private String messageKey = null;

    private Object[] messageArgs = null;

    private String deviation = "";

    private boolean automaticLog = true;

    private String errorLevel = "error";

    private String stackTraceString = "";

    private boolean alreadyLoggingShowed = false;

    private Integer errorNumber;

    /**
	 * Exibe exce��o: Se tiver exce��o raiz, exibe seu toString. Se n�o tiver e tiver mensagem, exibe
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        if (getRootCause() != null) return getRootCause().toString(); else if (getMessageKey() != null) {
            return getMessageKey();
        } else return super.toString();
    }

    public PlcException() {
        super();
        setMessageKey(null);
    }

    /**
     * Retorna a mensagem da exce��o raiz, caso exista. Sen�o retorna string vazia<p>
     * @since jCompany 3.0
     */
    public String getRootMessage() {
        if (rootCause != null) {
            return rootCause.getMessage() + "";
        } else {
            return "";
        }
    }

    /**
     * @since jcompany 3.0
     * Construtor que pode ser utilizado para se criar o objeto de exce��o com passagem de argumentos.<br>
     * Exemplo de uso: "throw new PlcException("jcompany.minha.mensagem",new Object[]{arg1,arg2},e,log);"
     * @param messageKeyLoc String contendo o identificador internacionalizado para a mensagem
     * @param messageArgsLoc Array de Object que cont�m uma lista de at� 4 argumentos din�micos para a mensagem
     */
    public PlcException(String messageKeyLoc, Object[] messageArgsLoc) {
        setMessageKey(messageKeyLoc);
        setMessageArgs(messageArgsLoc);
    }

    /**
	 * @since jcompany 3.0
	 */
    public PlcException(String messageKeyLoc, Object[] messageArgsLoc, Throwable cause, Logger logCause) {
        if (PlcException.class.isAssignableFrom(cause.getClass())) {
            PlcException causePlcException = (PlcException) cause;
            setMessageKey(causePlcException.getMessageKey());
            setMessageArgs(causePlcException.getMessageArgs());
            if (causePlcException.getLoggerCause() != null) setLoggerCause(causePlcException.getLoggerCause()); else setLoggerCause(logCause);
            setRootCause(causePlcException);
        } else {
            if (cause.getCause() != null) setRootCause(cause.getCause()); else setRootCause(cause);
            setLoggerCause(logCause);
            setMessageKey(messageKeyLoc);
            setMessageArgs(messageArgsLoc);
        }
    }

    /**
	 * @since jcompany 3.0
	 * Este construtor disponibiliza a causa tamb�m no vetor de argumentos para exibi��o com {0} nas mensagens
	 * @param messageKeyLoc Chave da mensagem
	 * @param cause Exce��o causadora
	 */
    public PlcException(String messageKeyLoc, Throwable cause, Logger logCause) {
        if (cause != null && PlcException.class.isAssignableFrom(cause.getClass())) {
            PlcException rootExceptionPlcException = (PlcException) cause;
            setRootCause(rootExceptionPlcException.getRootCause());
            if (rootExceptionPlcException.getRootCause() == null) {
                setMessageKey(rootExceptionPlcException.getMessageKey());
                setMessageArgs(rootExceptionPlcException.getMessageArgs());
            } else {
                setMessageKey(messageKeyLoc);
            }
            setLoggerCause(logCause);
        } else {
            if (cause != null && !PlcException.class.isAssignableFrom(cause.getClass())) {
                if (cause.getCause() != null) setRootCause(cause.getCause()); else setRootCause(cause);
                setLoggerCause(logCause);
            }
            setMessageKey(messageKeyLoc);
        }
    }

    /**
	 * @since jcompany 3.0
	 * Se construir uma exce��o passando outra, ent�o usa mensagem default
	 * @param cause
	 */
    public PlcException(Throwable cause) {
        setMessageKey("jcompany.errors.event.not.treated");
        setMessageArgs(new Object[] { cause });
        setRootCause(cause);
    }

    /**
	 * @since jcompany 2
	 */
    public PlcException(String novaMessageKey) {
        setMessageKey(novaMessageKey);
    }

    /**
	 * @since jcompany 5.0 Exibe mensagem de erro em p�gina correspondente ao n�mero do erro, conforme declarada no web.xml.
	 * Exemplo: new PlcException("#Meu erro especial",599); // exibe erro na p�gina erro599.jsp.
	 */
    public PlcException(String novaMessageKey, Integer errorNumber) {
        setMessageKey(novaMessageKey);
        setErrorNumber(errorNumber);
    }

    public PlcException(String messageKeyLoc, Throwable cause, Class classErrorAux, String errorLevel) {
        setMessageKey(messageKeyLoc);
        setRootCause(cause);
        setMessageArgs(new Object[] { cause });
        setAutomaticLog(true);
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setMessageKey(String key) {
        this.messageKey = key == null || key.trim().length() == 0 ? "jcompany.errors.event.not.treated" : key;
    }

    /**
	 * @since jcompany 3.0
	 */
    public String getMessageKey() {
        return messageKey;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setDesviation(String deviation) {
        this.deviation = deviation;
    }

    /**
	 * @since jcompany 3.0
	 */
    public String getDesviation() {
        return deviation;
    }

    /**
	 * @since jcompany 3.0
	 * Converte para String[] os argumentos em Object[] para facilitar mensagens de logging
	 * @param args
	 */
    public void setMessageArgs(Object[] args) {
        messageArgs = args;
    }

    /**
	 * @since jcompany 3.0
	 */
    public Object[] getMessageArgs() {
        return messageArgs;
    }

    /**
	 * @since jcompany 3.0
	 */
    public String[] getMessageArgsString() {
        if (messageArgs != null) {
            String[] msgS = new String[messageArgs.length];
            for (int i = 0; i < messageArgs.length; i++) {
                msgS[i] = messageArgs[i] + "";
            }
            return msgS;
        } else return null;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setRootCause(Throwable rootCause) {
        this.rootCause = rootCause;
    }

    /**
	 * @since jcompany 3.0
	 */
    public Throwable getRootCause() {
        return rootCause;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void printStrackTrace() {
        printStackTrace(System.err);
    }

    /**
	 * @since jcompany 3.0
	 */
    public void printStackTrace(PrintStream outStream) {
        printStackTrace(new PrintWriter(outStream));
    }

    /**
	 * @since jcompany 3.0
	 */
    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (getRootCause() != null) {
            getRootCause().printStackTrace(writer);
        }
        writer.flush();
    }

    /**
	 * @since jcompany 3.0
	 * @return boolean
	 */
    public boolean isAutomaticLog() {
        return automaticLog;
    }

    /**
	 * @since jcompany 3.0
	 * @param b
	 */
    public void setAutomaticLog(boolean automaticLog) {
        this.automaticLog = automaticLog;
    }

    /**
	 * @since jcompany 3.0
	 */
    public Logger getLoggerCause() {
        return loggerCause;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setLoggerCause(Logger loggerCause) {
        this.loggerCause = loggerCause;
    }

    /**
	 * @since jcompany 3.0
	 */
    public String getErrorLevel() {
        return errorLevel;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

    /**
	 * @since jcompany 3.0
	 */
    public String getStackTraceString() {
        return stackTraceString;
    }

    /**
	 * @since jcompany 3.0
	 */
    public void setStackTraceString(String stackTraceString) {
        this.stackTraceString = stackTraceString;
    }

    /**
	 * @return Retorna alreadyLoggingShowed.
	 */
    public boolean isAlreadyLoggingShowed() {
        return alreadyLoggingShowed;
    }

    /**
	 * @param alreadyLoggingShowed Registra alreadyLoggingShowed
	 */
    public void setAlreadyLoggingShowed(boolean alreadyLoggingShowed) {
        this.alreadyLoggingShowed = alreadyLoggingShowed;
    }

    @Override
    public String getLocalizedMessage() {
        String baseName = "jCompanyResources";
        String baseName2 = "ApplicationResources";
        String msg = mountLocalizedMessage(baseName, Locale.getDefault(), getMessageKey(), getMessageArgs());
        if (msg == null) msg = mountLocalizedMessage(baseName2, Locale.getDefault(), getMessageKey(), getMessageArgs());
        return (msg == null) ? getMessageKey() + ", " + Arrays.asList(getMessageArgs()) : msg;
    }

    private String mountLocalizedMessage(String nomeBundle, Locale locale, String key, Object[] parameters) {
        try {
            ResourceBundle messages = ResourceBundle.getBundle(nomeBundle, locale);
            return MessageFormat.format(messages.getString(key), parameters);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
	 * @since jcompany 2
	 * @deprecated	
	 * Este construtor disponibiliza a causa tamb�m no vetor de argumentos para exibi��o com {0} nas mensagens
	 * @param messageKeyLoc Chave da mensagem
	 * @param causa Exce��o causadora
	 */
    public PlcException(String messageKeyLoc, Exception e) {
        this(messageKeyLoc, new Object[] { e.getCause() }, e.getCause(), null);
    }

    /**
	 * @since jcompany 2
	 * @deprecated	
	 */
    public PlcException(String messageKeyLoc, Object[] messageArgsLoc, Exception e) {
        this(messageKeyLoc, messageArgsLoc, e, null);
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }
}
