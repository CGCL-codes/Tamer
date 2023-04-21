package edu.tufts.osidimpl.repository.localfiles;

public class LastModifiedTimePartStructure implements org.osid.repository.PartStructure {

    private org.osid.shared.Id lastModifiedTimePartStructureId = null;

    private org.osid.shared.Type type = new Type("mit.edu", "partStructure", "lastModifiedTime", "LastModifiedTime");

    private String displayName = "LastModifiedTime";

    private String description = "LastModifiedTime";

    private boolean mandatory = false;

    private boolean populatedByRepository = true;

    private boolean repeatable = false;

    private static LastModifiedTimePartStructure lastModifiedTimePartStructure = new LastModifiedTimePartStructure();

    protected static LastModifiedTimePartStructure getInstance() {
        return lastModifiedTimePartStructure;
    }

    public String getDisplayName() throws org.osid.repository.RepositoryException {
        return this.displayName;
    }

    public String getDescription() throws org.osid.repository.RepositoryException {
        return this.description;
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

    protected LastModifiedTimePartStructure() {
        try {
            this.lastModifiedTimePartStructureId = Utilities.getIdManager().getId("LastModifiedTimePartStructureId");
        } catch (Throwable t) {
        }
    }

    public void updateDisplayName(String displayName) throws org.osid.repository.RepositoryException {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.shared.Id getId() throws org.osid.repository.RepositoryException {
        return this.lastModifiedTimePartStructureId;
    }

    public org.osid.shared.Type getType() throws org.osid.repository.RepositoryException {
        return this.type;
    }

    public org.osid.repository.RecordStructure getRecordStructure() throws org.osid.repository.RepositoryException {
        return RecordStructure.getInstance();
    }

    public boolean validatePart(org.osid.repository.Part part) throws org.osid.repository.RepositoryException {
        return true;
    }

    public org.osid.repository.PartStructureIterator getPartStructures() throws org.osid.repository.RepositoryException {
        return new PartStructureIterator(new java.util.Vector());
    }
}
