package org.epoline.phoenix.dossiernotepad.shared;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.epoline.phoenix.common.shared.EnumPackageStatus;
import org.epoline.phoenix.common.shared.Item;
import org.epoline.phoenix.usersettings.shared.EnumTOCSource;

public class ItemPackage extends Item implements Cloneable {

    private ItemDossier ownerDossier;

    private final String keyOfOwnerMessage;

    private List documents;

    private final int logicalNumber;

    private final EnumTOCSource source;

    private final String sourceOriginal;

    private final String originalApplicationNumber;

    private final Date dateFormal;

    private EnumPackageStatus status;

    private EnumPackageStatus latestStatusInDMS;

    private final String id;

    private int pages;

    private boolean relatedToCurrentMessage;

    /**
	 * Constructor for existing package.
	 */
    public ItemPackage(String id, Date dateFormal, List documents, int logicalNumber, EnumPackageStatus status, EnumPackageStatus latestStatusInDMS, String keyOfOwnerMessage, EnumTOCSource source, String sourceOriginal, String originalApplicationNumber, int newPages) {
        if (id == null || dateFormal == null || documents == null || status == null) {
            throw new IllegalArgumentException("Constructor for ItemPackage was called with null parameter(s)");
        }
        if (id.length() > 17) {
            throw new IllegalArgumentException("Constructor for ItemPackage was called with invalid id: " + id + ".");
        }
        if (id.length() < 17) {
            StringBuffer sb = new StringBuffer(id);
            while (sb.length() < 17) {
                sb.append(' ');
            }
            id = sb.toString();
        }
        this.dateFormal = dateFormal;
        this.documents = documents;
        for (int i = 0; i < documents.size(); i++) {
            ((ItemDocument) documents.get(i)).setOwnerPackage(this);
        }
        this.logicalNumber = logicalNumber;
        this.status = status;
        this.latestStatusInDMS = latestStatusInDMS;
        this.id = id;
        this.keyOfOwnerMessage = keyOfOwnerMessage == null ? null : keyOfOwnerMessage.trim();
        this.source = source;
        this.sourceOriginal = sourceOriginal == null ? null : sourceOriginal.trim();
        this.originalApplicationNumber = originalApplicationNumber;
        pages = newPages;
    }

    public void addDocument(ItemDocument document) {
        documents.add(document);
        document.setOwnerPackage(this);
        sortDocuments();
    }

    /**
	 * Return a copy of this. <BR>
	 * Parts that are not editable and that are not needed to identify the
	 * instance (name, number or id), are excluded from the copy: null is
	 * applied for them. <BR>
	 * This method is like clone(), but every field that is editable is deep
	 * copied, so that editing the result of this method does not affect this,
	 * vv. <BR>
	 * Recursively also for children <BR>
	 * The status history is not copied; instead the current status is put into
	 * the newStatus field, so that getStatus() effectively yields the same
	 * result.
	 * 
	 * @param participantCopiesByOriginals map filled with ItemParticipant >>
	 *        ItemParticipant.copy
	 */
    public ItemPackage deepCopyEditableParts(Map participantCopiesByOriginals) {
        List documentsCopy = new ArrayList();
        ItemPackage result = new ItemPackage(getId(), getDateFormal(), documentsCopy, getLogicalNumber(), getStatus(), null, null, null, null, "originalApplicationNumber", 0);
        result.setPages(pages);
        for (int i = 0; i < getDocuments().size(); i++) {
            ItemDocument d = (ItemDocument) getDocuments().get(i);
            ItemDocument dc = d.deepCopyEditableParts(participantCopiesByOriginals);
            dc.setOwnerPackage(result);
            documentsCopy.add(dc);
        }
        return result;
    }

    /**
	 * One document was just closed. If currently all documents are closed,
	 * change the package to closed
	 */
    void documentHasJustBeenClosed() {
        for (Iterator iter = getDocuments().iterator(); iter.hasNext(); ) {
            ItemDocument doc = (ItemDocument) iter.next();
            if (!doc.isDeleted()) {
                return;
            }
        }
        setStatus(EnumPackageStatus.CLOSED);
    }

    /**
	 * One document is about to be reopened. If currently all documents are
	 * closed, change the package to the last non closed status
	 */
    void documentIsAboutToBeReopened() {
        for (Iterator iter = getDocuments().iterator(); iter.hasNext(); ) {
            ItemDocument doc = (ItemDocument) iter.next();
            if (!doc.isDeleted()) {
                return;
            }
        }
        if (status.equals(EnumPackageStatus.CLOSED)) {
            status = latestStatusInDMS;
            if (status.equals(EnumPackageStatus.CLOSED)) {
                status = EnumPackageStatus.PREVIOUS_STATUS;
            }
        }
    }

    public Date getDateFormal() {
        return dateFormal;
    }

    public List getDocuments() {
        return documents;
    }

    public java.lang.String getId() {
        return id;
    }

    public String getKeyOfOwnerMessage() {
        return keyOfOwnerMessage;
    }

    public int getLogicalNumber() {
        return logicalNumber;
    }

    /**
	 * Answer the original application number
	 */
    public String getOriginalApplicationNumber() {
        return originalApplicationNumber;
    }

    public ItemDossier getOwnerDossier() {
        return ownerDossier;
    }

    /**
	 * Insert the method's description here. Creation date: (2/25/02 9:32:14 AM)
	 * 
	 * @return int
	 */
    public int getPages() {
        return pages;
    }

    public EnumTOCSource getSource() {
        return source;
    }

    public String getSourceOriginal() {
        return sourceOriginal;
    }

    public EnumPackageStatus getStatus() {
        return status;
    }

    /**
	 * Answer whether the editable parts of this are equal to the editable parts
	 * of the other object. Recursively including children
	 */
    public boolean hasEqualEditableParts(ItemPackage other) {
        if (other == null) {
            throw new NullPointerException();
        }
        if (!getDateFormal().equals(other.getDateFormal()) || !getStatus().equals(other.getStatus()) || getLogicalNumber() != other.getLogicalNumber() || getDocuments().size() != other.getDocuments().size()) {
            return false;
        }
        Iterator iter1 = getDocuments().iterator();
        Iterator iter2 = other.getDocuments().iterator();
        while (iter1.hasNext()) {
            ItemDocument d1 = (ItemDocument) iter1.next();
            ItemDocument d2 = (ItemDocument) iter2.next();
            if (!d1.hasEqualEditableParts(d2)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Insert the method's description here. Creation date: (3/19/02 5:00:31 PM)
	 * 
	 * @return boolean
	 */
    public boolean isRelatedToCurrentMessage() {
        return relatedToCurrentMessage;
    }

    public boolean isScanned() {
        if (!getStatus().isScanned()) {
            return false;
        }
        String id = getId().toUpperCase();
        return id.endsWith("P1 ") || id.endsWith("P2 ");
    }

    public boolean isStatusDirty() {
        boolean statusIsClosed = EnumPackageStatus.CLOSED.equals(getStatus());
        for (Iterator iter = getDocuments().iterator(); iter.hasNext(); ) {
            ItemDocument doc = (ItemDocument) iter.next();
            if (doc.isDeleted() != statusIsClosed) {
                return true;
            }
        }
        return false;
    }

    public void setOwnerDossier(ItemDossier newOwnerDossier) {
        if (newOwnerDossier == null) {
            throw new NullPointerException("newOwnerDossier == null");
        }
        ownerDossier = newOwnerDossier;
    }

    /**
	 * Insert the method's description here. Creation date: (2/25/02 9:32:14 AM)
	 * 
	 * @param newPages int
	 */
    public void setPages(int newPages) {
        pages = newPages;
    }

    public void setRelatedToCurrentMessage(boolean newRelatedToCurrentMessage) {
        relatedToCurrentMessage = newRelatedToCurrentMessage;
    }

    public void setStatus(EnumPackageStatus newStatus) {
        this.status = newStatus;
    }

    private void sortDocuments() {
        Collections.sort(documents, new Comparator() {

            public int compare(Object obj1, Object obj2) {
                ItemDocument doc1 = (ItemDocument) obj1;
                ItemDocument doc2 = (ItemDocument) obj2;
                int n1 = doc1.getPackageSequenceNumber();
                int n2 = doc2.getPackageSequenceNumber();
                return n1 < n2 ? -1 : (n1 > n2 ? 1 : 0);
            }
        });
    }

    /**
	 * Assign sequence numbers to documents strictly sequential. Ex: was - 0, 1,
	 * 3, 7, 10; will - 1, 2, 3, 4, 5
	 */
    public void updateSequenceNumbers() {
        sortDocuments();
        for (int i = 0; i < documents.size(); i++) {
            ((ItemDocument) documents.get(i)).setPackageSequenceNumber(i + 1);
        }
    }

    /**
	 * Update the status using the current package status in DMS.
	 */
    void updateStatus(EnumPackageStatus statusFromDms) {
        latestStatusInDMS = statusFromDms;
        status = statusFromDms;
    }
}
