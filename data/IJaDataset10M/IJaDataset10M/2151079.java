package org.plazmaforge.framework.core.data;

import java.util.Date;

/**
 * @author Oleh Hapon
 * $Id: IHistoryEntity.java,v 1.1 2010/12/05 07:51:26 ohapon Exp $
 */
public interface IHistoryEntity {

    Date getCreateTimestamp();

    Integer getCreateUserId();

    Date getUpdateTimestamp();

    Integer getUpdateUserId();
}
