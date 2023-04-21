package uk.ac.dl.dp.coreutil.interfaces;

import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;
import uk.ac.dl.dp.coreutil.entity.Bookmark;
import uk.ac.dl.dp.coreutil.entity.DataRefAuthorisation;
import uk.ac.dl.dp.coreutil.entity.DataReference;
import uk.ac.dl.dp.coreutil.exceptions.DataCenterException;
import uk.ac.dl.dp.coreutil.exceptions.SessionException;
import uk.ac.dl.dp.coreutil.util.DPAuthType;

/**
 * This is the business interface for DataAuthorisationSession enterprise bean.
 */
@Remote
public interface DataAuthorisationRemote {

    public void addAuthorisedUser(String sid, String DN, Date startDate, Date endDate, DPAuthType type) throws SessionException;

    public Collection<DataRefAuthorisation> getGivenAuthorisedList(String sid, DPAuthType type) throws SessionException;

    public Collection<DataRefAuthorisation> getRecievedAuthorisedList(String sid, DPAuthType type) throws SessionException;

    public Collection<DataReference> getOtherUsersDataReferences(String sid, String DN) throws SessionException, DataCenterException;

    public Collection<Bookmark> getOtherUsersBookmarks(String sid, String DN) throws SessionException, DataCenterException;

    public Collection<String> searchUserDns(String sid, String search) throws SessionException;

    public void removeAuthorisedUser(String sid, String DN) throws SessionException;

    public void removeAuthorisedUser(String sid, DataRefAuthorisation dataRef) throws SessionException;
}
