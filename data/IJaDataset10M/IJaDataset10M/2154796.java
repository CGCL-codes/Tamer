package org.plazmaforge.bsolution.bank.client.swt.forms;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.plazmaforge.bsolution.bank.common.beans.BankAccount;
import org.plazmaforge.bsolution.bank.common.services.BankAccountService;
import org.plazmaforge.bsolution.partner.common.beans.IPartnerTitle;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;
import org.plazmaforge.framework.core.exception.ApplicationException;

/** 
 * @author Oleh Hapon
 * $Id: BankAccountHelper.java,v 1.3 2010/12/05 07:57:18 ohapon Exp $
 */
public class BankAccountHelper {

    private static final Log logger = LogFactory.getLog(BankAccountHelper.class);

    private BankAccountHelperProvider provider;

    public BankAccountHelper(BankAccountHelperProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider is null");
        }
        this.provider = provider;
    }

    public BankAccountHelperProvider getProvider() {
        return provider;
    }

    protected Integer getPartnerId(IPartnerTitle partner) {
        return partner == null ? null : partner.getId();
    }

    public List getBankAccountsByPartner(IPartnerTitle partner) {
        return getBankAccountsByPartnerId(getPartnerId(partner));
    }

    public List getBankAccountsByPartnerId(Integer partnerId) {
        if (partnerId == null) {
            return null;
        }
        BankAccountService service = getProvider().getBankAccountService();
        if (service == null) {
            return null;
        }
        try {
            return service.findByPartnerId(partnerId);
        } catch (ApplicationException ex) {
            logger.error(ex);
            return null;
        }
    }

    public BankAccount getBankAccount(IPartnerTitle partner) {
        if (partner == null) {
            return null;
        }
        return getBankAccount(partner.getId());
    }

    public BankAccount getBankAccount(Integer partnerId) {
        if (partnerId == null) {
            return null;
        }
        try {
            return getProvider().getPartnerService().findDefaultBankAccount(partnerId);
        } catch (ApplicationException ex) {
            logger.error(ex);
        }
        return null;
    }

    public void loadBankAccountsByPartnerField(XComboEdit bankAccountField, IPartnerTitle partner) {
        List dataList = getBankAccountsByPartner(partner);
        if (dataList != null) {
            bankAccountField.setDataList(dataList);
        }
    }
}
