package br.ufes.xpflow.module.validation;

import br.ufes.xpflow.core.flow.execution.ExecutionContext;
import br.ufes.xpflow.core.main.XPFlowException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 26/07/2007
 * Time: 02:15:07
 * Validador
 */
public abstract class AbstractValidator implements Validator {

    /**
     * Mensagem para o validador
     */
    protected String message;

    /**
     * Expressao xpath que retorna
     * o valor a ser validado
     */
    protected XPathExpression xpath;

    /**
     * provedor de xpaths
     */
    protected static final XPath xpathProvider = XPathFactory.newInstance().newXPath();

    public void setXpath(String xpath) throws XPFlowException {
        try {
            synchronized (xpathProvider) {
                this.xpath = xpathProvider.compile(xpath);
            }
        } catch (XPathExpressionException e) {
            throw new XPFlowException("Nao foi possivel compilar xpath '" + xpath + "' para validador", e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Prepara os dados para serem
     * validados pela subclasse
     *
     * @param ctxt contexto de execucao
     * @return true se valido
     * @throws br.ufes.xpflow.core.main.XPFlowException
     *          se ocorrer erro
     */
    public boolean validate(ExecutionContext ctxt) throws XPFlowException {
        String value;
        try {
            synchronized (xpath) {
                value = xpath.evaluate(ctxt.getContent());
            }
            boolean valid = validate(value);
            if (!valid && message != null) {
                ctxt.addMessage(message);
            }
            return valid;
        } catch (Exception e) {
            throw new XPFlowException("Nao foi possivel fazer validacao de um dado", e);
        }
    }

    /**
     * faz a validacao propriamente
     *
     * @param value valor a ser validado
     * @return true se valido
     */
    public abstract boolean validate(String value);
}
