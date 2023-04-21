package org.ogre4j;

import org.xbig.base.*;

public interface IMultiRenderTarget extends INativeObject, org.ogre4j.IRenderTarget {

    public interface IBoundSufaceList extends INativeObject, org.std.Ivector<org.ogre4j.IRenderTexture> {

        /** **/
        public void assign(int num, org.ogre4j.IRenderTexture val);

        /** **/
        public org.ogre4j.IRenderTexture at(int loc);

        /** **/
        public org.ogre4j.IRenderTexture back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IRenderTexture front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.IRenderTexture val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    /** 
    Bind a surface to a certain attachment point. 
It does not bind the surface and fails with an exception (ERR_INVALIDPARAMS) if:**/
    public void bindSurface(int attachment, org.ogre4j.IRenderTexture target);

    /** 
    Unbind attachment. **/
    public void unbindSurface(int attachment);

    /** 
    Error throwing implementation, it's not possible to write a  to disk. **/
    public void copyContentsToMemory(org.ogre4j.IPixelBox dst, org.ogre4j.RenderTarget.FrameBuffer buffer);

    /** **/
    public org.ogre4j.PixelFormat suggestPixelFormat();

    /** **/
    public org.ogre4j.IMultiRenderTarget.IBoundSufaceList getBoundSurfaceList();

    /** 
    Get a pointer to a bound surface **/
    public org.ogre4j.IRenderTexture getBoundSurface(int index);
}
