package org.ogre4j;

import org.xbig.base.*;

public interface ICompositorChain extends INativeObject, org.ogre4j.IRenderTargetListener, org.ogre4j.IResourceAllocatedObject {

    public static interface IRQListener extends INativeObject, org.ogre4j.IRenderQueueListener {

        /** 
    **/
        public void renderQueueStarted(short id, String invocation, BooleanPointer skipThisQueue);

        /** 
    **/
        public void renderQueueEnded(short id, String invocation, BooleanPointer repeatThisQueue);

        /** 
    Set current operation and target **/
        public void setOperation(org.ogre4j.ICompositorInstance.ITargetOperation op, org.ogre4j.ISceneManager sm, org.ogre4j.IRenderSystem rs);

        /** 
    Notify current destination viewport **/
        public void notifyViewport(org.ogre4j.IViewport vp);

        /** 
    Flush remaining render system operations **/
        public void flushUpTo(short id);
    }

    public interface IInstances extends INativeObject, org.std.Ivector<org.ogre4j.ICompositorInstance> {

        /** **/
        public void assign(int num, org.ogre4j.ICompositorInstance val);

        /** **/
        public org.ogre4j.ICompositorInstance at(int loc);

        /** **/
        public org.ogre4j.ICompositorInstance back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.ICompositorInstance front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.ICompositorInstance val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface IInstanceIterator extends INativeObject, org.ogre4j.IVectorIterator<org.ogre4j.ICompositorChain.IInstances> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.ICompositorInstance getNext();

        /** **/
        public org.ogre4j.ICompositorInstance peekNext();

        /** **/
        public NativeObjectPointer<org.ogre4j.ICompositorInstance> peekNextPtr();

        /** **/
        public void moveNext();
    }

    public interface IRenderSystemOperations extends INativeObject, org.std.Ivector<org.ogre4j.ICompositorInstance.IRenderSystemOperation> {

        /** **/
        public void assign(int num, org.ogre4j.ICompositorInstance.IRenderSystemOperation val);

        /** **/
        public org.ogre4j.ICompositorInstance.IRenderSystemOperation at(int loc);

        /** **/
        public org.ogre4j.ICompositorInstance.IRenderSystemOperation back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.ICompositorInstance.IRenderSystemOperation front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.ICompositorInstance.IRenderSystemOperation val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    /** 
    Apply a compositor. Initially, the filter is enabled. **/
    public org.ogre4j.ICompositorInstance addCompositor(org.ogre4j.ICompositorPtr filter, int addPosition, int technique);

    /** 
    Remove a compositor. **/
    public void removeCompositor(int position);

    /** 
    Get the number of compositors. **/
    public int getNumCompositors();

    /** 
    Remove all compositors. **/
    public void removeAllCompositors();

    /** 
    Get compositor instance by position. **/
    public org.ogre4j.ICompositorInstance getCompositor(int index);

    /** 
    Get the original scene compositor instance for this chain (internal use). **/
    public org.ogre4j.ICompositorInstance _getOriginalSceneCompositor();

    /** 
    Get an iterator over the compositor instances. The first compositor in this list is applied first, the last one is applied last. **/
    public void getCompositors(org.ogre4j.ICompositorChain.IInstanceIterator returnValue);

    /** 
    Enable or disable a compositor, by position. Disabling a compositor stops it from rendering but does not free any resources. This can be more efficient than using removeCompositor and addCompositor in cases the filter is switched on and off a lot. **/
    public void setCompositorEnabled(int position, boolean state);

    /** 
    **/
    public void preRenderTargetUpdate(org.ogre4j.IRenderTargetEvent evt);

    /** 
    **/
    public void preViewportUpdate(org.ogre4j.IRenderTargetViewportEvent evt);

    /** 
    **/
    public void postViewportUpdate(org.ogre4j.IRenderTargetViewportEvent evt);

    /** 
    **/
    public void viewportRemoved(org.ogre4j.IRenderTargetViewportEvent evt);

    /** 
    Mark state as dirty, and to be recompiled next frame. **/
    public void _markDirty();

    /** 
    Get viewport that is the target of this chain **/
    public org.ogre4j.IViewport getViewport();

    /** 
    Internal method for reconnecting with viewport **/
    public void _notifyViewport(org.ogre4j.IViewport vp);

    /** 
    Remove a compositor by pointer. This is internally used by  to "weak" remove any instanced of a deleted technique. **/
    public void _removeInstance(org.ogre4j.ICompositorInstance i);

    /** 
    Internal method for registering a queued operation for deletion later **/
    public void _queuedOperation(org.ogre4j.ICompositorInstance.IRenderSystemOperation op);

    /** 
    Compile this Composition chain into a series of  operations. **/
    public void _compile();
}
