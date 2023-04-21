package org.ogre4j;

import org.xbig.base.*;

public interface IPose extends INativeObject, org.ogre4j.IAnimationAllocatedObject {

    public interface IVertexOffsetMap extends INativeObject, org.std.Imap<Integer, org.ogre4j.IVector3> {

        /** **/
        public void clear();

        /** **/
        public int count(int key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(int key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.IVector3 get(int key);

        /** **/
        public void insert(int key, org.ogre4j.IVector3 value);
    }

    public interface IVertexOffsetIterator extends INativeObject, org.ogre4j.IMapIterator<org.ogre4j.IPose.IVertexOffsetMap> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public void getNext(org.ogre4j.IVector3 returnValue);

        /** **/
        public void peekNextValue(org.ogre4j.IVector3 returnValue);

        /** **/
        public int peekNextKey();

        /** **/
        public org.ogre4j.IPose.IVertexOffsetIterator operatorAssignment(org.ogre4j.IPose.IVertexOffsetIterator rhs);

        /** **/
        public org.ogre4j.IVector3 peekNextValuePtr();

        /** **/
        public void moveNext();
    }

    public interface IConstVertexOffsetIterator extends INativeObject, org.ogre4j.IConstMapIterator<org.ogre4j.IPose.IVertexOffsetMap> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public void getNext(org.ogre4j.IVector3 returnValue);

        /** **/
        public void peekNextValue(org.ogre4j.IVector3 returnValue);

        /** **/
        public int peekNextKey();

        /** **/
        public org.ogre4j.IPose.IConstVertexOffsetIterator operatorAssignment(org.ogre4j.IPose.IConstVertexOffsetIterator rhs);

        /** **/
        public org.ogre4j.IVector3 peekNextValuePtr();

        /** **/
        public void moveNext();
    }

    /** **/
    public String getName();

    /** **/
    public int getTarget();

    /** 
    Adds an offset to a vertex for this pose. **/
    public void addVertex(int index, org.ogre4j.IVector3 offset);

    /** 
    Remove a vertex offset. **/
    public void removeVertex(int index);

    /** 
    Clear all vertex offsets. **/
    public void clearVertexOffsets();

    /** **/
    public void getVertexOffsetIterator_const(org.ogre4j.IPose.IConstVertexOffsetIterator returnValue);

    /** 
    Gets an iterator over all the vertex offsets. **/
    public void getVertexOffsetIterator(org.ogre4j.IPose.IVertexOffsetIterator returnValue);

    /** 
    Gets a const reference to the vertex offsets. **/
    public org.ogre4j.IPose.IVertexOffsetMap getVertexOffsets();

    /** 
    Get a hardware vertex buffer version of the vertex offsets. **/
    public org.ogre4j.IHardwareVertexBufferSharedPtr _getHardwareVertexBuffer(int numVertices);

    /** 
    Clone this pose and create another one configured exactly the same way (only really useful for cloning holders of this class). **/
    public org.ogre4j.IPose clone();
}
