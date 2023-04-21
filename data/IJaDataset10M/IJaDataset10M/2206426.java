package org.oors;

import java.io.Serializable;

public final class ProjectBranchElementId implements Serializable {

    private static final long serialVersionUID = -987158191570100284L;

    public long projectBranchId;

    public long id;

    public ProjectBranchElementId() {
    }

    public ProjectBranchElementId(long projectBranchId, long id) {
        this.projectBranchId = projectBranchId;
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectBranchElementId)) {
            return false;
        }
        ProjectBranchElementId pbeid = (ProjectBranchElementId) o;
        return this.projectBranchId == pbeid.projectBranchId && this.id == pbeid.id;
    }

    public int hashCode() {
        return (int) projectBranchId ^ (int) id;
    }

    public String toString() {
        return "ProjectBranchElementId[projectBranchId=" + projectBranchId + ",id=" + id + "]";
    }
}
