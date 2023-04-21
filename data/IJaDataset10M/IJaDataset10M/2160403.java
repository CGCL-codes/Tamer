package edu.tufts.osidimpl.repository.flickr;

public class Record implements org.osid.repository.Record {

    private java.util.Vector _partVector = new java.util.Vector();

    private String _displayName = null;

    private org.osid.shared.Id _id = null;

    private org.osid.repository.RecordStructure _recordStructure = new RecordStructure();

    protected Record() {
        try {
            _id = Configuration.getInstance().getIdManager().getId(Configuration.RECORD_ID);
        } catch (Throwable t) {
        }
    }

    public String getDisplayName() throws org.osid.repository.RepositoryException {
        return _displayName;
    }

    public org.osid.shared.Id getId() throws org.osid.repository.RepositoryException {
        return _id;
    }

    public org.osid.repository.Part createPart(org.osid.shared.Id partStructureId, java.io.Serializable value) throws org.osid.repository.RepositoryException {
        try {
            org.osid.repository.Part part = new Part(partStructureId, value);
            _partVector.addElement(part);
            return part;
        } catch (Throwable t) {
            t.printStackTrace();
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public void deletePart(org.osid.shared.Id partId) throws org.osid.repository.RepositoryException {
        try {
            for (int i = 0, size = _partVector.size(); i < size; i++) {
                org.osid.repository.Part part = (org.osid.repository.Part) _partVector.elementAt(i);
                if (part.getId().isEqual(partId)) {
                    _partVector.removeElementAt(i);
                    return;
                }
            }
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.UNKNOWN_ID);
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public void updateDisplayName(String displayName) throws org.osid.repository.RepositoryException {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.repository.PartIterator getParts() throws org.osid.repository.RepositoryException {
        return new PartIterator(_partVector);
    }

    public org.osid.repository.RecordStructure getRecordStructure() throws org.osid.repository.RepositoryException {
        return _recordStructure;
    }

    public boolean isMultivalued() throws org.osid.repository.RepositoryException {
        return false;
    }
}
