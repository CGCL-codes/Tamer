package org.arastreju.sge.model.nodes.views;

import java.util.Set;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *	Abstract base for all classes that provide a view on a {@link SNResource}. 
 * </p>
 * 
 * <p>
 * 	Created Nov 29, 2009
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ResourceView implements ResourceNode {

    private final ResourceNode resource;

    /**
	 * Creates a new view to the given resource.
	 * @param resource The resource to be wrapped.
	 */
    public ResourceView(final ResourceNode resource) {
        this.resource = resource;
    }

    /**
	 * Creates a view to a resource to be created implicitly.
	 */
    protected ResourceView() {
        this.resource = new SNResource();
    }

    public String getName() {
        return resource.getName();
    }

    public Namespace getNamespace() {
        return resource.getNamespace();
    }

    public QualifiedName getQualifiedName() {
        return resource.getQualifiedName();
    }

    public void setName(final String name) {
        resource.setName(name);
    }

    public void setNamespace(final Namespace namespace) {
        resource.setNamespace(namespace);
    }

    public boolean references(final ResourceID ref) {
        return resource.references(ref);
    }

    public Set<Association> getAssociations() {
        return resource.getAssociations();
    }

    public Set<Association> getAssociations(final ResourceID predicate) {
        return resource.getAssociations(predicate);
    }

    public Set<SemanticNode> getAssociationClients(final ResourceID predicate) {
        return resource.getAssociationClients(predicate);
    }

    public Association getSingleAssociation(final ResourceID predicate) {
        return resource.getSingleAssociation(predicate);
    }

    public SemanticNode getSingleAssociationClient(final ResourceID predicate) {
        return resource.getSingleAssociationClient(predicate);
    }

    public boolean hasAssociation(final Association assoc) {
        return resource.hasAssociation(assoc);
    }

    public boolean revoke(final Association assoc) {
        return resource.revoke(assoc);
    }

    public void removeAssociations(final ResourceID predicate) {
        for (Association assoc : getAssociations(predicate)) {
            remove(assoc);
        }
    }

    public void addToAssociations(final Association assoc) {
        resource.addToAssociations(assoc);
    }

    public void remove(final Association assoc) {
        resource.remove(assoc);
    }

    public void reset() {
        resource.reset();
    }

    public boolean isValueNode() {
        return false;
    }

    public boolean isResourceNode() {
        return true;
    }

    public boolean isAttached() {
        return resource.isAttached();
    }

    public SNClass asClass() {
        return resource.asClass();
    }

    public SNEntity asEntity() {
        return resource.asEntity();
    }

    public SNProperty asProperty() {
        return resource.asProperty();
    }

    public SNPropertyDeclaration asPropertyDeclaration() {
        return resource.asPropertyDeclaration();
    }

    public ResourceNode asResource() {
        return resource;
    }

    public ValueNode asValue() {
        throw new IllegalStateException("Cannot convert a resource to a value node");
    }

    /**
	 * Returns the wrapped resource.
	 * @return The resource wrapped by this view.
	 */
    protected ResourceNode getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return resource.equals(obj);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}
