package com.liferay.portlet.journal.service.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="JournalStructureLocalServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface JournalStructureLocalServiceHome extends EJBLocalHome {

    public JournalStructureLocalServiceEJB create() throws CreateException;
}
