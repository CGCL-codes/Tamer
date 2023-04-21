package org.yafra.interfaces;

import java.util.Date;
import java.util.List;
import org.yafra.model.MPerson;
import org.yafra.model.MPersonLog;

/**
 * interface model handler implementation person log</br>
 * @version $Id: IYafraMHIPersonLog.java,v 1.2 2009-12-12 18:49:20 mwn Exp $
 * @author <a href="mailto:martin.weber@yafra.org">Martin Weber</a>
 * @since 1.0
 */
public interface IYafraMHIPersonLog {

    public MPersonLog selectPersonLog(String id);

    public List<MPersonLog> getPersonLogs();

    public List<MPersonLog> getPersonLogs(MPerson mpl);

    public void insertPersonLog(String eAudit, String eReviewer, String eCreator, Date eDate, String eDesc);

    public void updatePersonLog(MPersonLog mpl);

    public void deletePersonLog(String id);
}
