package lablog.lib.db.entity;

import static lablog.lib.util.TextType.STRING_NAME;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import javax.swing.tree.MutableTreeNode;
import lablog.lib.db.entity.base.DirectoryContainer;
import lablog.lib.db.entity.base.EntityMetadata;
import lablog.lib.db.entity.base.RestrictedTreeNode;
import lablog.lib.util.Errors;
import lablog.lib.util.LablogError;

/**
 * A Directory entity. Directories constitute a tree structure that can contain {@link File} as
 * leafs.
 * 
 * @assoc 1 - n File
 * @assoc 1 - n Directory
 */
@Entity
@Table(name = "directory", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "parent_directory_id" }))
public class Directory extends RestrictedTreeNode<Directory, File> {

    public static final short CLASSID = 17;

    public static final long serialVersionUID = createSerialUID(DB_VERSION, MODULE, CLASSID);

    private String name;

    private Set<Directory> nodeChildren;

    private Set<File> leafChildren;

    private Directory parent;

    private DirectoryContainer containerHint;

    public Directory() {
        this(null, null, null, null, new TreeSet<Directory>(), new TreeSet<File>());
    }

    public Directory(String name) {
        this(name, null, null, null, new TreeSet<Directory>(), new TreeSet<File>());
    }

    public Directory(String name, Directory parent, Person owner, Group ownerGroup) {
        this(name, parent, owner, ownerGroup, new TreeSet<Directory>(), new TreeSet<File>());
    }

    public Directory(String name, Directory parent, Person owner, Group ownerGroup, Set<Directory> nodeChildren, Set<File> leafChildren) {
        this.setName(name);
        this.setParent(parent);
        this.setLeafChildren(leafChildren);
        this.setNodeChildren(nodeChildren);
    }

    @Override
    @Column(name = "name", nullable = false, length = 64)
    @EntityMetadata(name = "Name", order = 1, title = true, texttype = STRING_NAME)
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Transient
    public void insert(MutableTreeNode node, int index) {
        if (node instanceof Directory) getNodeChildren().add((Directory) node); else if (node instanceof File) getLeafChildren().add((File) node); else throw new LablogError(Errors.ERR_INTERNAL, "The entity to insert doesn't match the required Type (File or Directory).");
    }

    @Override
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_directory_id", nullable = true)
    @EntityMetadata(name = "Directory", order = 10)
    public Directory getParent() {
        return parent;
    }

    @Override
    public void setParent(MutableTreeNode parent) {
        if (parent instanceof Directory) this.parent = (Directory) parent;
    }

    @Override
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Override
    @OneToMany(targetEntity = Directory.class, mappedBy = "parent", fetch = FetchType.EAGER)
    @OrderBy("name")
    public Set<Directory> getNodeChildren() {
        return nodeChildren;
    }

    @Override
    public void setNodeChildren(Set<Directory> nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    @Override
    @OneToMany(targetEntity = File.class, mappedBy = "parent", fetch = FetchType.EAGER)
    @OrderBy("name")
    public Set<File> getLeafChildren() {
        return leafChildren;
    }

    @Override
    public void setLeafChildren(Set<File> leafChildren) {
        this.leafChildren = leafChildren;
    }

    @Override
    @Transient
    public String toString() {
        return this.name;
    }

    /**
    * Get a hint to the {@link DirectoryContainer} referencing this section. This information
    * will not be persisted in the database.
    * 
    * @return The property container.
    */
    @Transient
    @EntityMetadata(name = "Directory Container", editable = false, display = false)
    public final DirectoryContainer getContainerHint() {
        return containerHint;
    }

    /**
    * Get a hint to the {@link DirectoryContainer} referencing this section. This information
    * will not be persisted in the database.
    * 
    * @param container The container hint to set.
    */
    public final void setContainerHint(DirectoryContainer container) {
        this.containerHint = container;
    }
}
