package org.nakedobjects.metamodel.specloader.internal.instances;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorSet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetDefaultToObject;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.IntrospectableSpecificationAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public class InstanceCollectionSpecification extends IntrospectableSpecificationAbstract {

    private final SpecificationLoader specificationLoader;

    public InstanceCollectionSpecification(final SpecificationLoader specificationLoader, final RuntimeContext runtimeContext) {
        super(runtimeContext);
        this.specificationLoader = specificationLoader;
    }

    public void markAsService() {
    }

    public void introspect(final FacetDecoratorSet decorator) {
        fullName = NakedObjectList.class.getName();
        identifier = Identifier.classIdentifier(fullName);
        superClassSpecification = specificationLoader.loadSpecification(Object.class);
        superClassSpecification.addSubclass(this);
        fields = new NakedObjectAssociation[0];
        addFacet(new InstancesCollectionFacet(this));
        addFacet(new TypeOfFacetDefaultToObject(this, specificationLoader) {
        });
        setIntrospected(true);
    }

    public NakedObjectAssociation getAssociation(final String name) {
        return null;
    }

    @Override
    public NakedObjectAction[] getServiceActionsFor(final NakedObjectActionType... type) {
        return new NakedObjectAction[0];
    }

    @Override
    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... type) {
        return new NakedObjectAction[0];
    }

    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String id, final NakedObjectSpecification[] parameters) {
        return null;
    }

    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String id) {
        return null;
    }

    public String getSingularName() {
        return "Instances";
    }

    public String getPluralName() {
        return "Instances";
    }

    public String getShortName() {
        return "Instances";
    }

    public String getTitle(final NakedObject object) {
        return ((NakedObjectList) object.getObject()).titleString();
    }

    @Override
    public String getIconName(final NakedObject object) {
        return "instances";
    }

    public String getDescription() {
        return "Typed instances";
    }

    @Override
    public boolean isCollectionOrIsAggregated() {
        return true;
    }
}
