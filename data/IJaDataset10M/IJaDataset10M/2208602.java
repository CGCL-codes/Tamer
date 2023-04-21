package org.oors;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name = "DOCUMENTS")
public class Document extends Base {

    @Id
    @Column(name = "PROJECT_BRANCH_ID", insertable = false, updatable = false)
    protected long projectBranchId;

    @Id
    @Column(name = "DOCUMENT_ID", insertable = false, updatable = false)
    protected long id;

    @Column(name = "DOCUMENT_NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_BRANCH_ID", insertable = false, updatable = false)
    ProjectBranch projectBranch;

    @Column(name = "FOLDER_ID")
    protected long folderId;

    @Transient
    Folder folder;

    @Transient
    protected List<Obj> objs;

    public Folder getFolder() {
        if (folder == null) {
            Collection<Folder> fs = DataSource.getInstance().entityManager.createQuery("FROM Folder f WHERE f.id = " + this.folderId + " AND f.projectBranchId = " + this.projectBranchId, Folder.class).getResultList();
            if (!fs.isEmpty()) folder = fs.iterator().next();
        }
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
        if (folder != null) folderId = folder.id; else folderId = -1;
    }

    protected Document() {
    }

    Document(ProjectBranch projectBranch, Folder folder, String name) {
        this.projectBranchId = projectBranch.id;
        this.projectBranch = projectBranch;
        Query q = DataSource.getInstance().entityManager.createQuery("SELECT MAX(d.id) FROM Document d");
        Number result = (Number) q.getSingleResult();
        if (result == null) this.id = 1; else this.id = result.longValue() + 1;
        this.folder = folder;
        if (folder == null) this.folderId = -1; else this.folderId = folder.id;
        this.name = name;
        objs = new LinkedList<Obj>();
    }

    protected long getFolderId() {
        return folderId;
    }

    protected void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return "Document[projectBranchId=" + projectBranchId + ",id=" + id + ",folderId=" + getFolder() + ",name=" + name + "]";
    }

    protected long getProjectBranchId() {
        return projectBranchId;
    }

    protected void setProjectBranchId(long projectBranchId) {
        this.projectBranchId = projectBranchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectBranch getProjectBranch() {
        return projectBranch;
    }

    protected void setProjectBranch(ProjectBranch projectBranch) {
        this.projectBranch = projectBranch;
    }

    public List<Obj> getObjs() {
        if (objs == null) {
            objs = DataSource.getInstance().entityManager.createQuery("FROM Obj o WHERE DOCUMENT_ID = " + this.id + " AND PROJECT_BRANCH_ID = " + this.projectBranchId + " AND PARENT_OBJ_ID = -1 ORDER BY OBJ_ORDER", Obj.class).getResultList();
        }
        return objs;
    }

    public Obj appendObj() {
        getObjs();
        Obj o = new Obj(this.getProjectBranch(), this);
        int index = objs.size();
        o.order = index;
        objs.add(index, o);
        DataSource.getInstance().persist(o);
        for (int i = index + 1; i < objs.size(); i++) {
            Obj oi = objs.get(i);
            oi.setOrder(oi.getOrder() + 1);
        }
        fireCreatedEvent(o);
        return o;
    }
}
