package de.objectcode.soa.common.mfm.api;

import java.util.Iterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.objectcode.soa.common.mfm.api.accessor.IDataAccessor;
import de.objectcode.soa.common.mfm.api.collector.IDataCollector;
import de.objectcode.soa.common.mfm.api.normalize.NormalizedData;

@XmlRootElement(name = "type")
@XmlType(name = "complex-type")
@Entity
@Table(name = "MFM_COMPONENTTYPES")
public class ComponentType extends Type implements IComponentOwner {

    private Type superType;

    private Set<Component> components;

    @XmlElement(name = "component", namespace = "http://objectcode.de/soa/mfm")
    @OneToMany(mappedBy = "ownerType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("name")
    public Set<Component> getComponents() {
        return this.components;
    }

    public void setComponents(final Set<Component> components) {
        this.components = components;
    }

    @XmlElement(name = "super", namespace = "http://objectcode.de/soa/mfm", type = TypeRef.class)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "SUPER_TYPE_NAME", referencedColumnName = "NAME"), @JoinColumn(name = "SUPER_TYPE_VERSION", referencedColumnName = "VERSION") })
    public Type getSuperType() {
        return superType;
    }

    public void setSuperType(Type superType) {
        this.superType = superType;
    }

    @Override
    @Transient
    public boolean isComponentType() {
        return true;
    }

    public void normalize(IDataAccessor dataAccessor, final IDataCollector collector, final IMessageFormatRepository repository) throws ValidationException, UpgradeException, DowngradeException {
        if (superType != null && superType.isComponentType()) {
            ((ComponentType) superType).normalize(dataAccessor, collector, repository);
        }
        final String currentTypeName = dataAccessor.getType();
        final int currentVersion = dataAccessor.getVersion();
        if (currentTypeName != null) {
            if (!currentTypeName.equals(this.name)) {
                throw new ValidationException("Type information do not match: " + this.name + " != " + currentTypeName);
            }
            if (currentVersion < this.version) {
                ComponentType currentType = repository.getComplexType(this.name, currentVersion);
                if (currentType == null) {
                    throw new ValidationException("Type '" + this.name + "' version " + currentVersion + " not found");
                }
                final NormalizedData normalizedData = new NormalizedData();
                currentType.normalize(dataAccessor, normalizedData, repository);
                dataAccessor = normalizedData;
                for (int i = currentVersion + 1; i <= this.version; i++) {
                    currentType = repository.getComplexType(this.name, i);
                    if (currentType != null) {
                        final NormalizedData nextData = new NormalizedData();
                        currentType.upgradeType(dataAccessor, nextData);
                        dataAccessor = nextData;
                    }
                }
            } else if (currentVersion > this.version) {
                ComponentType currentType = repository.getComplexType(this.name, currentVersion);
                if (currentType == null) {
                    throw new ValidationException("Type '" + this.name + "' version " + currentVersion + " not found");
                }
                final NormalizedData normalizedData = new NormalizedData();
                currentType.normalize(dataAccessor, normalizedData, repository);
                dataAccessor = normalizedData;
                for (int i = currentVersion; i > this.version; i--) {
                    currentType = repository.getComplexType(this.name, i);
                    if (currentType != null) {
                        final NormalizedData nextData = new NormalizedData();
                        currentType.downgradeType(nextData, dataAccessor);
                        dataAccessor = nextData;
                    }
                }
            }
        }
        collector.setTypeInformation(this.name, this.version);
        for (final Component component : this.components) {
            component.normalize(dataAccessor, collector, repository);
        }
    }

    @Override
    public void resolve(final IResolveContext context) {
        if (superType != null) {
            superType = context.merge(superType);
        }
        for (final Component component : this.components) {
            component.setOwnerType(this);
            component.resolve(context);
        }
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("ComponentType(");
        buffer.append("name=").append(this.name);
        buffer.append(", version=").append(this.version);
        buffer.append(", bodyComponents=").append(this.components);
        buffer.append(")");
        return buffer.toString();
    }

    @Override
    public void update(final IResolveContext context, final Type type) {
        if (type.isComponentType()) {
            final ComponentType componentType = (ComponentType) type;
            final Set<Component> newComponents = componentType.getComponents();
            final Iterator<Component> thisCompIter = this.components.iterator();
            final Iterator<Component> newCompIter = newComponents.iterator();
            while (thisCompIter.hasNext() && newCompIter.hasNext()) {
                final Component thisComp = thisCompIter.next();
                final Component newComp = newCompIter.next();
                thisComp.update(context, newComp);
            }
            while (newCompIter.hasNext()) {
                final Component newComp = newCompIter.next();
                final Component createdComponent = new Component();
                createdComponent.setOwnerType(this);
                createdComponent.update(context, newComp);
                this.components.add(newComp);
            }
            for (int i = newComponents.size(); i < this.components.size(); i++) {
                this.components.remove(newComponents.size());
            }
        }
    }

    public void downgradeType(final IDataCollector oldVersion, final IDataAccessor newVersion) throws DowngradeException {
        if (superType != null && superType.isComponentType()) {
            ((ComponentType) superType).downgradeType(oldVersion, newVersion);
        }
        for (final Component component : this.components) {
            component.downgradeComponent(oldVersion, newVersion);
        }
    }

    public void upgradeType(final IDataAccessor oldVersion, final IDataCollector newVersion) throws UpgradeException {
        if (superType != null && superType.isComponentType()) {
            ((ComponentType) superType).upgradeType(oldVersion, newVersion);
        }
        for (final Component component : this.components) {
            component.upgradeComponent(oldVersion, newVersion);
        }
    }

    public void validate(final IDataAccessor dataAccessor) throws ValidationException {
        if (superType != null && superType.isComponentType()) {
            ((ComponentType) superType).validate(dataAccessor);
        }
        for (final Component component : this.components) {
            component.validate(dataAccessor);
        }
    }
}
