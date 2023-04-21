package net.ar.guia.managers.names;

import net.ar.guia.own.implementation.*;
import net.ar.guia.own.interfaces.*;

public class HashComponentNameManager extends ComponentVisitorAdapter implements ComponentNameManager {

    protected int currentChildNumber;

    public VisualComponent findComponentWithNameIn(final String aComponentName, VisualComponent aRootComponent) {
        ComponentFinder componentFinder = new ComponentFinder(aComponentName);
        aRootComponent.accept(componentFinder);
        return componentFinder.getFoundComponent();
    }

    public void assignNamesFrom(VisualComponent aComponent) {
        currentChildNumber = 0;
        aComponent.accept(new ComponentVisitorAdapter() {

            public void visitComponentBegin(VisualComponent aComponent) {
                currentChildNumber++;
                setUniqueId(aComponent, getTail(aComponent));
            }
        });
    }

    public String getTail(VisualComponent aComponent) {
        String theTail = (String) aComponent.getClientProperty(CharacterComponentNameManager.USER_UNIQUE_NAME_PROPERTY);
        if (theTail == null) theTail = aComponent.getTypeId().substring(aComponent.getTypeId().lastIndexOf('.') + 1) + aComponent.hashCode();
        int indexOfMoney = theTail.indexOf('$');
        if (indexOfMoney != -1) theTail = theTail.substring(0, indexOfMoney);
        return theTail;
    }

    protected final class ComponentFinder extends ComponentVisitorAdapter {

        protected VisualComponent foundComponent = null;

        private final String aComponentName;

        protected ComponentFinder(String aComponentName) {
            this.aComponentName = aComponentName;
        }

        public void visitComponentBegin(VisualComponent aComponent) {
            if (getUniqueId(aComponent).equals(aComponentName)) foundComponent = aComponent;
        }

        public VisualComponent getFoundComponent() {
            return foundComponent;
        }
    }

    public String getUniqueId(VisualComponent aVisualComponent) {
        return null;
    }

    public void setUniqueId(VisualComponent aVisualComponent, String anId) {
    }
}
