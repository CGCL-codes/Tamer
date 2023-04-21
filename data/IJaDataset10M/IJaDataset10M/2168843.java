package org.ogre4j;

import org.xbig.base.*;

public interface IOverlayContainer extends INativeObject, org.ogre4j.IOverlayElement {

    public interface IChildMap extends INativeObject, org.std.Imap<String, org.ogre4j.IOverlayElement> {

        /** **/
        public void clear();

        /** **/
        public int count(String key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(String key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.IOverlayElement get(String key);

        /** **/
        public void insert(String key, org.ogre4j.IOverlayElement value);
    }

    public interface IChildIterator extends INativeObject, org.ogre4j.IMapIterator<org.ogre4j.IOverlayContainer.IChildMap> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.IOverlayElement getNext();

        /** **/
        public org.ogre4j.IOverlayElement peekNextValue();

        /** **/
        public String peekNextKey();

        /** **/
        public org.ogre4j.IOverlayContainer.IChildIterator operatorAssignment(org.ogre4j.IOverlayContainer.IChildIterator rhs);

        /** **/
        public NativeObjectPointer<org.ogre4j.IOverlayElement> peekNextValuePtr();

        /** **/
        public void moveNext();
    }

    public interface IChildContainerMap extends INativeObject, org.std.Imap<String, org.ogre4j.IOverlayContainer> {

        /** **/
        public void clear();

        /** **/
        public int count(String key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(String key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.IOverlayContainer get(String key);

        /** **/
        public void insert(String key, org.ogre4j.IOverlayContainer value);
    }

    public interface IChildContainerIterator extends INativeObject, org.ogre4j.IMapIterator<org.ogre4j.IOverlayContainer.IChildContainerMap> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.IOverlayContainer getNext();

        /** **/
        public org.ogre4j.IOverlayContainer peekNextValue();

        /** **/
        public String peekNextKey();

        /** **/
        public org.ogre4j.IOverlayContainer.IChildContainerIterator operatorAssignment(org.ogre4j.IOverlayContainer.IChildContainerIterator rhs);

        /** **/
        public NativeObjectPointer<org.ogre4j.IOverlayContainer> peekNextValuePtr();

        /** **/
        public void moveNext();
    }

    /** 
    Adds another  to this container. **/
    public void addChild(org.ogre4j.IOverlayElement elem);

    /** 
    Adds another  to this container. **/
    public void addChildImpl(org.ogre4j.IOverlayElement elem);

    /** 
    Add a nested container to this container. **/
    public void addChildImpl(org.ogre4j.IOverlayContainer cont);

    /** 
    Removes a named element from this container. **/
    public void removeChild(String name);

    /** 
    Gets the named child of this container. **/
    public org.ogre4j.IOverlayElement getChild(String name);

    /** 
    **/
    public void initialise();

    /** **/
    public void _addChild(org.ogre4j.IOverlayElement elem);

    /** **/
    public void _removeChild(org.ogre4j.IOverlayElement elem);

    /** **/
    public void _removeChild(String name);

    /** 
    Gets an object for iterating over all the children of this object. **/
    public void getChildIterator(org.ogre4j.IOverlayContainer.IChildIterator returnValue);

    /** 
    Gets an iterator for just the container children of this object. **/
    public void getChildContainerIterator(org.ogre4j.IOverlayContainer.IChildContainerIterator returnValue);

    /** 
    Tell the object and its children to recalculate **/
    public void _positionsOutOfDate();

    /** 
    Overridden from . **/
    public void _update();

    /** 
    Overridden from . **/
    public int _notifyZOrder(int newZOrder);

    /** 
    Overridden from . **/
    public void _notifyViewport();

    /** 
    Overridden from . **/
    public void _notifyWorldTransforms(org.ogre4j.IMatrix4 xform);

    /** 
    Overridden from . **/
    public void _notifyParent(org.ogre4j.IOverlayContainer parent, org.ogre4j.IOverlay overlay);

    /** 
    Overridden from . **/
    public void _updateRenderQueue(org.ogre4j.IRenderQueue queue);

    /** 
    Overridden from . **/
    public boolean isContainer();

    /** 
    Should this container pass events to their children **/
    public boolean isChildrenProcessEvents();

    /** 
    Should this container pass events to their children **/
    public void setChildrenProcessEvents(boolean val);

    /** 
    This returns a  at position x,y. **/
    public org.ogre4j.IOverlayElement findElementAt(float x, float y);

    /** **/
    public void copyFromTemplate(org.ogre4j.IOverlayElement templateOverlay);

    /** **/
    public org.ogre4j.IOverlayElement clone(String instanceName);
}
