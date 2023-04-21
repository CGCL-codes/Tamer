package edu.tufts.osidimpl.repository.fedora_2_2;

public class URLPartStructure implements org.osid.repository.PartStructure {

    private java.util.Vector partsVector = new java.util.Vector();

    private org.osid.repository.RecordStructure imageRecordStructure = null;

    private String displayName = "URL";

    private String description = "Image URL to be display alongside others";

    private org.osid.shared.Id id = null;

    private boolean populatedByRepository = true;

    private boolean mandatory = true;

    private boolean repeatable = false;

    private org.osid.shared.Type type = new Type("mit.edu", "partStructure", "URL");

    private org.osid.repository.RecordStructure recordStructure = (org.osid.repository.RecordStructure) imageRecordStructure;

    protected URLPartStructure(org.osid.repository.RecordStructure recordStructure, Repository repository) throws org.osid.repository.RepositoryException {
        this.recordStructure = recordStructure;
        try {
            this.id = new PID("URLPartStructureId");
        } catch (org.osid.shared.SharedException sex) {
            sex.printStackTrace();
        }
    }

    public String getDisplayName() throws org.osid.repository.RepositoryException {
        return this.displayName;
    }

    public void updateDisplayName(String displayName) throws org.osid.repository.RepositoryException {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public String getDescription() throws org.osid.repository.RepositoryException {
        return this.description;
    }

    public org.osid.shared.Id getId() throws org.osid.repository.RepositoryException {
        return this.id;
    }

    public org.osid.shared.Type getType() throws org.osid.repository.RepositoryException {
        return this.type;
    }

    public org.osid.repository.PartStructureIterator getPartStructures() throws org.osid.repository.RepositoryException {
        return new PartStructureIterator(this.partsVector);
    }

    public org.osid.repository.RecordStructure getRecordStructure() throws org.osid.repository.RepositoryException {
        return this.recordStructure;
    }

    public boolean isMandatory() throws org.osid.repository.RepositoryException {
        return this.mandatory;
    }

    public boolean isPopulatedByRepository() throws org.osid.repository.RepositoryException {
        return this.populatedByRepository;
    }

    public boolean isRepeatable() throws org.osid.repository.RepositoryException {
        return this.repeatable;
    }

    public boolean validatePart(org.osid.repository.Part part) throws org.osid.repository.RepositoryException {
        return true;
    }
}
