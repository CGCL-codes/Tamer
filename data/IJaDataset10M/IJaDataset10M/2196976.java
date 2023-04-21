package org.jcvi.glk.elvira.nextgen;

import org.jcvi.glk.Extent;

public class DefaultElviraTuple implements ElviraTuple {

    public static DefaultElviraTuple create(String project, Extent sample) {
        String sampleId = sample.getReference();
        String collectionCode = sample.getParent().getParent().getReference();
        return new DefaultElviraTuple(project, collectionCode, sampleId);
    }

    private final String project;

    private final String sampleId;

    private final String collectionCode;

    /**
     * @param project
     * @param collectionCode
     * @param sampleId
     */
    public DefaultElviraTuple(String project, String collectionCode, String sampleId) {
        this.project = project;
        this.collectionCode = collectionCode;
        this.sampleId = sampleId;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public String getSampleId() {
        return sampleId;
    }

    @Override
    public String getCollectionCode() {
        return collectionCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collectionCode == null) ? 0 : collectionCode.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        result = prime * result + ((sampleId == null) ? 0 : sampleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DefaultElviraTuple)) {
            return false;
        }
        DefaultElviraTuple other = (DefaultElviraTuple) obj;
        if (collectionCode == null) {
            if (other.collectionCode != null) {
                return false;
            }
        } else if (!collectionCode.equals(other.collectionCode)) {
            return false;
        }
        if (project == null) {
            if (other.project != null) {
                return false;
            }
        } else if (!project.equals(other.project)) {
            return false;
        }
        if (sampleId == null) {
            if (other.sampleId != null) {
                return false;
            }
        } else if (!sampleId.equals(other.sampleId)) {
            return false;
        }
        return true;
    }

    /**
     * Prints this tuple as a comma separated string proj,collection,sampleId.
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%s", project, collectionCode, sampleId);
    }
}
