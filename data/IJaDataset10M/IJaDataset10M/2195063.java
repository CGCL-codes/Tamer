package org.jcompany.control.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jcompany.commons.PlcConstantsCommons;
import org.jcompany.commons.PlcException;
import org.jcompany.commons.aop.PlcAopProfilingHelper;
import org.jcompany.commons.facade.IPlcFacade;
import org.jcompany.control.PlcControlLocator;
import org.jcompany.control.struts.service.PlcI18nService;

/**
 * jCompany 3.0. Classe para tratamento de mensagens gen�ricas de exce��o
 * @since jCompany 3.0
 * @version $Id: PlcExceptionHandlerService.java,v 1.9 2006/08/03 19:44:15 alvim Exp $
 */
public class PlcExceptionHandlerService {

    private static final Logger logControl = Logger.getLogger(PlcConstantsCommons.LOGGERs.JCOMPANY_CONTROL);

    protected static Logger log = Logger.getLogger(PlcExceptionHandlerService.class);

    /**
	 * jCompany 3.0 DP Composite. Devolve o singleton que encapsula l�gica de manipula��es de recursos de internacionaliza��o<p>
	 * Ao se pegar o servi�o de registro visual a partir deste m�todo e n�o instanci�-lo diretamente, cria-se um desacoplamento
	 * que permite que se altere este servi�o por outros espec�ficos, com m�nimo de esfor�o.
	 * @return Servi�o de registro de manipula��es visuais.
	 */
    protected PlcI18nService getI18nService() throws PlcException {
        return (PlcI18nService) PlcControlLocator.getInstance().get(PlcI18nService.class);
    }

    /**
 	 * jCompany 2.5.3. Retorna a Interface do Servi�o de Persist�ncia armazenado no escopo da aplica��o
 	 */
    protected IPlcFacade getFacadeService() throws PlcException {
        return PlcControlLocator.getInstance().getFacadeDefault();
    }

    /**
	 * jCompany 3.0. Trata erros de persistencia
	 * @return Exce��o contendo messagekey e um argumento alterados para mensagem especifica, com
	 * exce��o original em causaRaiz. Ou null se n�o foi tratada
	 */
    public PlcException handleErrorsWrapperModel(PlcException plcException) {
        log.debug("############# Entrou em trataErrosPersistencia");
        Throwable causaRaiz = (Throwable) plcException.getRootCause();
        String[] msgs = null;
        if (causaRaiz != null) {
            try {
                IPlcFacade plc = getFacadeService();
                msgs = plc.retrieveMessageException(causaRaiz);
                plcException.setMessageKey(msgs[0]);
                String[] msgsArgs = new String[3];
                int cont = 0;
                for (int i = 1; i < msgs.length; i++) {
                    if (msgs[i] != null) {
                        msgsArgs[cont] = msgs[i];
                        cont++;
                    }
                }
                plcException.setMessageArgs(msgsArgs);
            } catch (Exception e) {
                return null;
            }
            if (msgs != null) return plcException;
            if (log.isDebugEnabled()) log.debug("Mensagem de Erro recebida=" + plcException.getMessageKey() + " Classe erro raiz=" + causaRaiz.getClass());
        }
        return null;
    }

    /**
	 * jCompany 3.0 Trata erros controle e geral
	 */
    public PlcException verifyErrorsControl(Throwable erroG) {
        if (logControl.isDebugEnabled()) logControl.debug(PlcAopProfilingHelper.getInstance().showLogInitial(this.getClass().getSimpleName() + ":trataErrosControle"));
        if (erroG == null || PlcException.class.isAssignableFrom(erroG.getClass())) return null;
        log.debug("Entrou em trata erros geral");
        if (erroG instanceof javax.servlet.ServletException && erroG.getMessage() != null && erroG.getMessage().indexOf("does not contain handler parameter named") > 0) {
            return new PlcException("jcompany.erros.evento", new String[] { erroG.getLocalizedMessage() }, erroG, log);
        } else if (erroG instanceof java.lang.reflect.InvocationTargetException) {
            InvocationTargetException ite = (InvocationTargetException) erroG;
            if (logControl.isDebugEnabled()) logControl.debug(PlcAopProfilingHelper.getInstance().showLogFinal(this.getClass().getSimpleName() + ":trataErrosControle"));
            if (!ite.getCause().getLocalizedMessage().equals("")) return new PlcException("jcompany.erros.evento", new String[] { ite.getCause().getLocalizedMessage() }, ite.getCause(), log); else return new PlcException("jcompany.erros.evento", new String[] { ite.getCause() + "" }, ite.getCause(), log);
        } else {
            if (logControl.isDebugEnabled()) logControl.debug(PlcAopProfilingHelper.getInstance().showLogFinal(this.getClass().getSimpleName() + ":trataErrosControle"));
            return new PlcException("jcompany.erros.evento", new String[] { erroG.getLocalizedMessage() }, erroG, log);
        }
    }

    /**
	 * jCompany 3.0 Trata erros controle e geral
	 */
    public PlcException handleErrorsWrapperControl(PlcException plcException) {
        if (plcException.getRootCause() == null) return null; else {
            PlcException excecaoControle = verifyErrorsControl(plcException.getRootCause());
            if (excecaoControle != null) {
                excecaoControle.setAutomaticLog(plcException.isAutomaticLog());
                excecaoControle.setLoggerCause(plcException.getLoggerCause());
            }
            return excecaoControle;
        }
    }

    /**
	 * jCompany 2.7.1. Recebe uma exce��o e devolve String com sua Stack Trace
	 * @param e Exce��o
	 * @param formatHTML true para formatar com HTML \<br\> e \<p\> cada linha
	 * @return String contendo stack trace
	 */
    public String stackTraceToString(Throwable e, boolean formatHTML) {
        log.debug("######## Entrou em enclosing_method");
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(ops);
        e.printStackTrace(ps);
        StringBuffer stackTrace = new StringBuffer();
        if (formatHTML) stackTrace.append("<p class=\"msgExceptionPlc\"><b>");
        stackTrace.append("Stack trace:");
        if (formatHTML) stackTrace.append("</b><br/>");
        stackTrace.append(ops.toString());
        if (formatHTML) stackTrace.append("</p>");
        return stackTrace.toString();
    }

    /**
     * jCompany 3.0. Realiza a 'pauta m�nima' de tratamento de exce��es de qualquer type:<p>
     * 1. Emite o stack trace para console<br>
     * 2. Armazena o erro no request para exibi��o para usu�rios<br>
     * 3. Envia um log do log4j (para qualquer appender definido)<br>
     * 4. Envia um log especial com appender para disparo de email/JMS.
     */
    public void performActions(HttpServletRequest request, PlcException treatedException) {
        log.debug("############# Entrou em realizaAcoesTratamento");
        if (treatedException == null) log.fatal("Erro ao tentar fazer tratamento de excecao generico. Excecao tratada estah nula ");
        if (treatedException.getRootCause() != null) {
            if (treatedException.isAutomaticLog()) {
                String login = "An�nimo";
                if (request.getUserPrincipal() != null) login = request.getUserPrincipal().getName();
                if (treatedException.getRootCause().getCause() != null && Exception.class.isAssignableFrom(treatedException.getRootCause().getCause().getClass())) sendLogAutomatic(login, treatedException, (Exception) treatedException.getRootCause().getCause()); else if (Exception.class.isAssignableFrom(treatedException.getRootCause().getClass())) sendLogAutomatic(login, treatedException, (Exception) treatedException.getRootCause()); else log.fatal("Erro de nivel diferenciado disparado. Erro: " + treatedException.getRootCause(), treatedException.getRootCause());
            }
        }
    }

    /**
     * jCompany 3.0. Realiza a 'pauta m�nima' de tratamento de exce��es de qualquer type:<p>
     * 1. Emite o stack trace para console<br>
     * 2. Armazena o erro no request para exibi��o para usu�rios<br>
     * 3. Envia um log do log4j (para qualquer appender definido)<br>
     * 4. Envia um log especial com appender para disparo de email/JMS.
     */
    public void doActionsTreatment(PlcException treatedException) {
        log.debug("############### Entrou em realizaAcoesTratamentoBatch");
        if (treatedException == null) log.fatal("Erro ao tentar fazer tratamento de excecao generico. Excecao tratada estah nula ");
        String login = "Batch";
        if (treatedException.getRootCause() != null) {
            if (treatedException.isAutomaticLog()) {
                if (treatedException.getRootCause().getCause() != null && Exception.class.isAssignableFrom(treatedException.getRootCause().getCause().getClass())) sendLogAutomatic(login, treatedException, (Exception) treatedException.getRootCause().getCause()); else if (Exception.class.isAssignableFrom(treatedException.getRootCause().getClass())) sendLogAutomatic(login, treatedException, (Exception) treatedException.getRootCause()); else log.fatal("Erro de nivel diferenciado disparado. Erro: " + treatedException.getRootCause(), treatedException.getRootCause());
            }
        } else {
            sendLogAutomatic(login, treatedException, treatedException);
        }
    }

    /**
     * jCompany 3.0 Envia log via JMS ou Email para mensagens inesperadas, se enviadas com exce��o raiz
     * como argumento, no padr�o  new PlcException("message.key",e);
     */
    public void sendLogAutomatic(String login, PlcException treatedException, Exception rootException) {
        if (rootException == null) rootException = treatedException;
        if (treatedException.getLoggerCause() != null && treatedException.isAutomaticLog()) {
            Logger logErro = treatedException.getLoggerCause();
            String mensagem = translateMessage(treatedException);
            if (despiseLog(mensagem, treatedException, rootException)) return;
            if (treatedException.getErrorLevel().equals("error")) logErro.error("Usuario corrente: " + login + " Erro: " + mensagem, rootException); else if (treatedException.getErrorLevel().equals("warn")) logErro.warn("Usuario corrente: " + login + " Erro: " + mensagem, rootException); else if (treatedException.getErrorLevel().equals("fatal")) logErro.fatal("Usuario corrente: " + login + " Erro: " + mensagem, rootException);
        }
    }

    /**
     * @since jCompany 3.2.3 Indica para n�o enviar log (stack trace) em alguns casos especiais, que j� foram enviados ou cujo
     * tratamento � mais sofisticado.  Exemplo: Integridade Referencial.
     */
    protected boolean despiseLog(String message, PlcException treatedException, Exception rootException) {
        return errorForeignKey(message);
    }

    /**
     * @return true se mensagem de erro for erro de foreign key. Pode precisar de especializa��o para outros SGBDs que n�o sejam Oracle e Derby.
     */
    public boolean errorForeignKey(String message) {
        return message.toLowerCase().indexOf("foreign key") > -1 || message.toLowerCase().indexOf("integrity constraint") > -1;
    }

    private String translateMessage(PlcException treatedException) {
        String mensagem = treatedException.getMessageKey();
        if (treatedException.getMessageKey() == null) return "[Mensagem de erro para o log estah nula em " + treatedException + " e com args= " + treatedException.getMessageArgs() + "]";
        String bundle = "ApplicationResources";
        if (mensagem.startsWith("jcompany.")) bundle = "jCompanyResources";
        try {
            mensagem = getI18nService().mountLocalizedMessage(bundle, new Locale("pt,BR"), mensagem, treatedException.getMessageArgsString());
        } catch (Exception e) {
            log.error("Excecao encadeada ao tentar tratar excecao", e);
        }
        return mensagem;
    }

    /**
	 * Recebe uma exce��o e interpreta se � uma exce��o controlada internamente ou wrapper que cont�m exce��o interna.
	 * Verifica ainda se exce��o externa nao foi convertida para um wrapper, conforme o padrao, e se n�o foi ajusta para o tratamento.
	 * @param ex Exce�ao a ser interpretada
	 * @return "wrapper" PlcException contendo a exce��o, ajustada
	 */
    public PlcException verifyException(Throwable ex) {
        if (PlcException.class.isAssignableFrom(ex.getClass())) {
            if (((PlcException) ex).getRootCause() == null) return (PlcException) ex;
        }
        PlcException excecaoInterpretada = null;
        if (PlcException.class.isAssignableFrom(ex.getClass())) excecaoInterpretada = handleErrorsWrapperModel((PlcException) ex);
        if (excecaoInterpretada != null) return excecaoInterpretada;
        if (PlcException.class.isAssignableFrom(ex.getClass())) {
            return handleErrorsWrapperControl((PlcException) ex);
        } else {
            return verifyErrorsControl(ex);
        }
    }
}
