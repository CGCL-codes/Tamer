package org.pixory.pxmodel;

import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 */
public class PXShare extends PXPersistentObject {

    private static final Log LOG = LogFactory.getLog(PXShare.class);

    private Date _shareDate;

    private PXAlbumFace _album;

    private PXPerson _sharer;

    private PXIdentity _sharee;

    public PXShare() {
        super(null);
    }

    public PXAlbumFace getAlbum() {
        return _album;
    }

    public void setAlbum(PXAlbumFace album) {
        _album = album;
    }

    public PXPerson getSharer() {
        return _sharer;
    }

    public void setSharer(PXPerson sharer) {
        _sharer = sharer;
    }

    public PXIdentity getSharee() {
        return _sharee;
    }

    public void setSharee(PXIdentity sharee) {
        _sharee = sharee;
    }

    public Date getShareDate() {
        return _shareDate;
    }

    public void setShareDate(Date shareDate) {
        _shareDate = shareDate;
    }

    /**
	 * @return List of PXShare from the threadSession
	 */
    public static List getEffectiveSharesWithContacts() {
        List getEffective = null;
        try {
            Session aSession = PXObjectStore.getInstance().getThreadSession();
            if (aSession != null) {
                String findQuery = "from org.pixory.pxmodel.PXShare as share where share.album.shareMethod = :shareMethod";
                getEffective = aSession.createQuery(findQuery).setEntity("shareMethod", PXShareMethod.LIST).list();
            }
        } catch (Exception e) {
            LOG.warn(null, e);
        }
        return getEffective;
    }

    /**
	 * Equals and hashcode. We cannot use the ID, since this
	 * is filled when persisted, which is not working when building more shares at the
	 * same time before persisting. 
	 * We try to define a business key equality here. 
	 * 
	 */
    private String buskeyeq() {
        return this.getAlbum().getName() + this.getSharee().getName() + this.getSharer().getName();
    }

    public boolean equals(Object other) {
        if (!(other instanceof PXShare)) return false;
        PXShare castOther = (PXShare) other;
        return new EqualsBuilder().append(this.buskeyeq(), castOther.buskeyeq()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.buskeyeq()).toHashCode();
    }
}
