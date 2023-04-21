package org.xmdl.ida.lib.web.taglib.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.TextFieldTag;
import org.xmdl.ida.lib.web.taglib.component.MoneyTagBean;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Tag for money type
 * 
 * @author hakan
 */
public class MoneyTag extends TextFieldTag {

    /**
     * Transient log to prevent session synchronization issues - children can use instance for logging.
     */
    protected final transient Log log = LogFactory.getLog(getClass());

    /** serial id */
    private static final long serialVersionUID = -537967841316615869L;

    protected String currencyList;

    protected String currencyListKey;

    protected String currencyListValue;

    protected String emptyOption;

    @Override
    public Component getBean(ValueStack valueStack, HttpServletRequest req, HttpServletResponse res) {
        return new MoneyTagBean(valueStack, req, res);
    }

    protected void populateParams() {
        if (log.isDebugEnabled()) log.debug("populateParams() <-");
        super.populateParams();
        MoneyTagBean moneyBean = ((MoneyTagBean) component);
        moneyBean.setEmptyOption(emptyOption);
        moneyBean.setSize(size);
        moneyBean.setCurrencyList(currencyList);
        moneyBean.setCurrencyListKey(currencyListKey);
        moneyBean.setCurrencyListValue(currencyListValue);
        if (log.isDebugEnabled()) log.debug("moneyBean = " + moneyBean);
        if (log.isDebugEnabled()) log.debug("moneyBean.emptyOption = " + emptyOption);
        if (log.isDebugEnabled()) log.debug("moneyBean.size = " + size);
        if (log.isDebugEnabled()) log.debug("moneyBean.currencyList = " + currencyList);
        if (log.isDebugEnabled()) log.debug("moneyBean.currencyListKey = " + currencyListKey);
        if (log.isDebugEnabled()) log.debug("moneyBean.currencyListValue = " + currencyListValue);
        if (log.isDebugEnabled()) log.debug("populateParams() ->");
    }

    /**
	 * @param currencyList the currencyList to set
	 */
    public void setCurrencyList(String currencyList) {
        this.currencyList = currencyList;
    }

    /**
	 * @param currencyListKey the currencyListKey to set
	 */
    public void setCurrencyListKey(String currencyListKey) {
        this.currencyListKey = currencyListKey;
    }

    /**
	 * @param currencyListValue the currencyListValue to set
	 */
    public void setCurrencyListValue(String currencyListValue) {
        this.currencyListValue = currencyListValue;
    }

    /**
	 * @param emptyOption the emptyOption to set
	 */
    public void setEmptyOption(String emptyOption) {
        this.emptyOption = emptyOption;
    }
}
