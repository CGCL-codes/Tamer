package org.ogre4j;

import org.xbig.base.*;

public interface IRibbonTrail extends INativeObject, org.ogre4j.IBillboardChain, org.ogre4j.IMovableObject.IListener {

    public interface INodeList extends INativeObject, org.std.Ivector<org.ogre4j.INode> {

        /** **/
        public void assign(int num, org.ogre4j.INode val);

        /** **/
        public org.ogre4j.INode at(int loc);

        /** **/
        public org.ogre4j.INode back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.INode front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.INode val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface INodeIterator extends INativeObject, org.ogre4j.IConstVectorIterator<org.ogre4j.IRibbonTrail.INodeList> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.INode getNext();

        /** **/
        public org.ogre4j.INode peekNext();

        /** **/
        public NativeObjectPointer<org.ogre4j.INode> peekNextPtr();

        /** **/
        public void moveNext();
    }

    public interface IIndexVector extends INativeObject, org.std.Ivector<Integer> {

        /** **/
        public void assign(int num, int val);

        /** **/
        public IntegerPointer at(int loc);

        /** **/
        public IntegerPointer back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public IntegerPointer front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(int val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface INodeToChainSegmentMap extends INativeObject, org.std.Imap<org.ogre4j.INode, Integer> {

        /** **/
        public void clear();

        /** **/
        public int count(org.ogre4j.INode key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(org.ogre4j.INode key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public IntegerPointer get(org.ogre4j.INode key);

        /** **/
        public void insert(org.ogre4j.INode key, IntegerPointer value);
    }

    public interface IColourValueList extends INativeObject, org.std.Ivector<org.ogre4j.IColourValue> {

        /** **/
        public void assign(int num, org.ogre4j.IColourValue val);

        /** **/
        public org.ogre4j.IColourValue at(int loc);

        /** **/
        public org.ogre4j.IColourValue back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IColourValue front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.IColourValue val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface IRealList extends INativeObject, org.std.Ivector<Float> {

        /** **/
        public void assign(int num, float val);

        /** **/
        public FloatPointer at(int loc);

        /** **/
        public FloatPointer back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public FloatPointer front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(float val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    /** 
    Add a node to be tracked. **/
    public void addNode(org.ogre4j.INode n);

    /** 
    Remove tracking on a given node. **/
    public void removeNode(org.ogre4j.INode n);

    /** 
    Get an iterator over the nodes which are being tracked. **/
    public void getNodeIterator(org.ogre4j.IRibbonTrail.INodeIterator returnValue);

    /** 
    Get the chain index for a given  being tracked. **/
    public int getChainIndexForNode(org.ogre4j.INode n);

    /** 
    Set the length of the trail. **/
    public void setTrailLength(float len);

    /** 
    Get the length of the trail. **/
    public float getTrailLength();

    /** 
    **/
    public void setMaxChainElements(int maxElements);

    /** 
    **/
    public void setNumberOfChains(int numChains);

    /** 
    **/
    public void clearChain(int chainIndex);

    /** 
    Set the starting ribbon colour for a given segment. **/
    public void setInitialColour(int chainIndex, org.ogre4j.IColourValue col);

    /** 
    Set the starting ribbon colour. **/
    public void setInitialColour(int chainIndex, float r, float g, float b, float a);

    /** 
    Get the starting ribbon colour. **/
    public org.ogre4j.IColourValue getInitialColour(int chainIndex);

    /** 
    Enables / disables fading the trail using colour. **/
    public void setColourChange(int chainIndex, org.ogre4j.IColourValue valuePerSecond);

    /** 
    Set the starting ribbon width in world units. **/
    public void setInitialWidth(int chainIndex, float width);

    /** 
    Get the starting ribbon width in world units. **/
    public float getInitialWidth(int chainIndex);

    /** 
    Set the change in ribbon width per second. **/
    public void setWidthChange(int chainIndex, float widthDeltaPerSecond);

    /** 
    Get the change in ribbon width per second. **/
    public float getWidthChange(int chainIndex);

    /** 
    Enables / disables fading the trail using colour. **/
    public void setColourChange(int chainIndex, float r, float g, float b, float a);

    /** 
    Get the per-second fading amount **/
    public org.ogre4j.IColourValue getColourChange(int chainIndex);

    /** **/
    public void nodeUpdated(org.ogre4j.INode node);

    /** **/
    public void nodeDestroyed(org.ogre4j.INode node);

    /** **/
    public void _timeUpdate(float time);

    /** 
    Overridden from **/
    public String getMovableType();
}
