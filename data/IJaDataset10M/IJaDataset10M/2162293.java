package org.plazmaforge.bsolution.finance.common.beans;

import org.plazmaforge.bsolution.base.common.beans.IOwnerable;
import org.plazmaforge.framework.core.data.IDictionary;

/**
 * @author Oleh Hapon
 * 
 * $Id: IContractorHeader.java,v 1.3 2010/05/26 14:11:43 ohapon Exp $
 */
public interface IContractorHeader extends IDictionary, IOwnerable {

    boolean isPartner();

    boolean isEmployee();
}
