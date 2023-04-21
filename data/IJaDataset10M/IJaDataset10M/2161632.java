package org.nmc.pachyderm.authoring;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import ca.ucalgary.apollo.core.*;
import ca.ucalgary.apollo.app.*;
import org.nmc.pachyderm.foundation.*;

public class InlineArrayBindingEditor extends InlineBindingEditor {

    public InlineArrayBindingEditor(WOContext context) {
        super(context);
    }

    public String editComponentName() {
        return (String) targetBindingContext().inferSecondValueForKey("editComponentName");
    }

    public MCContext targetBindingContext() {
        MCContext _bindingContext = new MCContext(super.bindingContext());
        _bindingContext.setTask("edit");
        return _bindingContext;
    }

    public void setTargetBindingContext(MCContext context) {
    }

    public int actualNumberOfItemsInContainer() {
        NSArray array = (NSArray) component().bindingValues().storedValueForKey(bindingKey());
        return (array != null) ? array.count() : 0;
    }

    public boolean willResolveToComponentEditor() {
        return "InlineComponentEditor".equals(editComponentName());
    }

    public void setDestinationComponent(Object object) {
    }

    public Object destinationComponent() {
        return willResolveToComponentEditor() ? currentArrayItem() : component();
    }

    public int preferredNumberOfItemsInContainer() {
        return -1;
    }

    public int maximumAllowedNumberofItemsInContainer() {
        NSDictionary limits = limitsForCurrentContentType();
        Integer maxAllow = (Integer) limits.objectForKey("max-length");
        return (maxAllow != null) ? maxAllow.intValue() : -1;
    }

    private int minimumAllowedNumberOfItemsInContainer() {
        NSDictionary limits = limitsForCurrentContentType();
        Integer minAllow = (Integer) limits.objectForKey("min-length");
        return (minAllow != null) ? minAllow.intValue() : -1;
    }

    public void setCurrentArrayItem(Object item) {
    }

    public Object currentArrayItem() {
        int index = containerIndex();
        PXBindingValues values = component().bindingValues();
        PXAssociation _assoc = (PXAssociation) values.objectInKeyAtIndex(bindingKey(), index);
        return (_assoc != null) ? (_assoc.valueInContext(null)) : null;
    }

    public WOComponent removeCurrentItem() {
        int index = containerIndex();
        PXBindingValues values = component().bindingValues();
        PXAssociation assc = (PXAssociation) values.objectInKeyAtIndex(bindingKey(), index);
        PXComponent sub = (PXComponent) assc.constantValue();
        component().removeChildComponent(sub);
        values.removeObjectFromKeyAtIndex(bindingKey(), index);
        session().setObjectForKey(new Integer(-1), "PXComponentIndex");
        return null;
    }

    public WOComponent addNewItemToContainer() {
        String contenttype = contentType();
        MCPage page = (MCPage) context().page();
        PXDocument currentDocument = (PXDocument) page.document();
        PXScreenModel screenModel = currentDocument.screenModel();
        PXComponent newComponent = screenModel.createNewComponent();
        PXComponentDescription newDescription = PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(contenttype);
        newComponent.setComponentDescription(newDescription);
        PXBindingValues currentValues = component().bindingValues();
        PXConstantValueAssociation newCVA = new PXConstantValueAssociation();
        newCVA.setConstantValue(newComponent);
        currentValues.insertObjectInKeyAtIndex(newCVA, bindingKey(), actualNumberOfItemsInContainer());
        component().addChildComponent(newComponent);
        Integer levels = new Integer(-1);
        session().setObjectForKey(levels, "PXComponentIndex");
        session().setObjectForKey("yes", "justAddedItemToContainer");
        return null;
    }

    public WOComponent orderCurrentItemUp() {
        PXBindingValues values = component().bindingValues();
        PXBindingDescription description = bindingDescription();
        int index = containerIndex();
        String key = description.key();
        if (index == 0) {
            return context().page();
        }
        Object object = values.removeObjectFromKeyAtIndex(key, index);
        values.insertObjectInKeyAtIndex(object, key, index - 1);
        return context().page();
    }

    public WOComponent orderCurrentItemDown() {
        PXBindingValues values = component().bindingValues();
        PXBindingDescription description = bindingDescription();
        int index = containerIndex();
        String key = description.key();
        if (!(index + 1 < actualNumberOfItemsInContainer())) {
            return context().page();
        }
        Object object = values.removeObjectFromKeyAtIndex(key, index);
        values.insertObjectInKeyAtIndex(object, key, index + 1);
        return context().page();
    }

    public int displayedIndexValue() {
        return containerIndex() + 1;
    }

    public boolean allowModifyArray() {
        boolean allowModifyArray = true;
        return allowModifyArray;
    }

    public boolean canAddNewItem() {
        boolean canAddNewItem = false;
        int maxNum = maximumAllowedNumberofItemsInContainer();
        if (maxNum == -1) {
            return true;
        }
        int currentNum = actualNumberOfItemsInContainer();
        if (maxNum > currentNum) {
            canAddNewItem = true;
        }
        return canAddNewItem;
    }

    public boolean canRemoveItem() {
        boolean canRemoveItem = false;
        int minNum = minimumAllowedNumberOfItemsInContainer();
        if (minNum == -1) {
            return true;
        }
        int currentNum = actualNumberOfItemsInContainer();
        if (minNum < currentNum) {
            canRemoveItem = true;
        }
        return canRemoveItem;
    }

    public void setContainerIndex(int index) {
        super.setContainerIndex(index);
        String check = (String) session().objectForKey("justAddedItemToContainer");
        if ((check == null) || (!(((String) session().objectForKey("justAddedItemToContainer")).equals("yes")))) {
            if (index > 0) {
                _incrementFirstLevelInteger();
            }
        }
        session().setObjectForKey("no", "justAddedItemToContainer");
    }

    public boolean canGoUp() {
        boolean canGoUp = true;
        if (containerIndex() == 0) {
            canGoUp = false;
        }
        return canGoUp;
    }

    public boolean canGoDown() {
        boolean canGoDown = true;
        if (!(containerIndex() + 1 < actualNumberOfItemsInContainer())) {
            canGoDown = false;
        }
        return canGoDown;
    }

    public String addNewLabel() {
        return "Add new " + bindingDescription().name();
    }
}
