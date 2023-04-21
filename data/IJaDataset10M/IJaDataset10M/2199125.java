package org.nakedobjects.plugins.dnd.view.field;

import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.dnd.drawing.Image;
import org.nakedobjects.plugins.dnd.drawing.ImageFactory;
import org.nakedobjects.plugins.dnd.view.Content;
import org.nakedobjects.plugins.dnd.view.UserActionSet;
import org.nakedobjects.plugins.dnd.view.collection.AbstractCollectionContent;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class OneToManyFieldImpl extends AbstractCollectionContent implements OneToManyField {

    private final NakedObject collection;

    private final ObjectField field;

    public OneToManyFieldImpl(final NakedObject parent, final NakedObject object, final OneToManyAssociation association) {
        field = new ObjectField(parent, association);
        this.collection = object;
    }

    public Consent canDrop(final Content sourceContent) {
        if (sourceContent.getNaked() instanceof NakedObject) {
            final NakedObject sourceAdapter = sourceContent.getNaked();
            final NakedObject parentAdapter = field.getParent();
            final NakedObject collection = getNaked();
            if (collection == null) {
                return new Veto("Collection not set up; can't add elements to a non-existant collection");
            }
            final Consent usableInState = getOneToManyAssociation().isUsable(NakedObjectsContext.getAuthenticationSession(), parentAdapter);
            if (usableInState.isVetoed()) {
                return usableInState;
            }
            final NakedObjectSpecification specification = sourceAdapter.getSpecification();
            final NakedObjectSpecification elementSpecification = getElementSpecification();
            if (!specification.isOfType(elementSpecification)) {
                return new Veto(String.format("Only objects of type %s are allowed in this collection", elementSpecification.getSingularName()));
            }
            if (parentAdapter.isPersistent() && sourceAdapter.isTransient()) {
                return new Veto("Can't set field in persistent object with reference to non-persistent object");
            }
            return getOneToManyAssociation().isValidToAdd(parentAdapter, sourceAdapter);
        } else {
            return Veto.DEFAULT;
        }
    }

    public Consent canSet(final NakedObject dragSource) {
        return Veto.DEFAULT;
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);
    }

    @Override
    public void debugDetails(final DebugString debug) {
        field.debugDetails(debug);
        debug.appendln("collection", collection);
        super.debugDetails(debug);
    }

    public NakedObject drop(final Content sourceContent) {
        final NakedObject object = sourceContent.getNaked();
        final NakedObject parent = field.getParent();
        final Consent perm = canDrop(sourceContent);
        if (perm.isAllowed()) {
            getOneToManyAssociation().addElement(parent, object);
        }
        return null;
    }

    @Override
    public NakedObject getCollection() {
        return collection;
    }

    @Override
    public String getDescription() {
        final String name = getFieldName();
        String type = getField().getSpecification().getSingularName();
        type = name.indexOf(type) == -1 ? " (" + type + ")" : "";
        final String description = getOneToManyAssociation().getDescription();
        return name + type + " " + description;
    }

    public NakedObjectAssociation getField() {
        return field.getNakedObjectAssociation();
    }

    public String getFieldName() {
        return field.getName();
    }

    public String getHelp() {
        return getOneToManyAssociation().getHelp();
    }

    public String getIconName() {
        return null;
    }

    @Override
    public Image getIconPicture(final int iconHeight) {
        final NakedObjectSpecification specification = getOneToManyAssociation().getSpecification();
        Image icon = ImageFactory.getInstance().loadIcon(specification, iconHeight, null);
        if (icon == null) {
            icon = ImageFactory.getInstance().loadDefaultIcon(iconHeight, null);
        }
        return icon;
    }

    public String getId() {
        return getOneToManyAssociation().getId();
    }

    public NakedObject getNaked() {
        return collection;
    }

    public OneToManyAssociation getOneToManyAssociation() {
        return (OneToManyAssociation) field.getNakedObjectAssociation();
    }

    public NakedObject getParent() {
        return field.getParent();
    }

    public NakedObjectSpecification getSpecification() {
        return field.getSpecification();
    }

    public Consent isEditable() {
        return getField().isUsable(NakedObjectsContext.getAuthenticationSession(), getParent());
    }

    public boolean isMandatory() {
        return getOneToManyAssociation().isMandatory();
    }

    public boolean isTransient() {
        return false;
    }

    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public final String title() {
        return field.getName();
    }

    @Override
    public String toString() {
        return collection + "/" + field.getNakedObjectAssociation();
    }

    @Override
    public String windowTitle() {
        return title() + " for " + field.getParent().titleString();
    }
}
