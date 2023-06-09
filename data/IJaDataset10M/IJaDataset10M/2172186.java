package org.ogre4j;

import org.xbig.base.*;

public interface IRenderTarget extends INativeObject, org.ogre4j.IRenderSysAllocatedObject {

    public static interface IImpl extends INativeObject {
    }

    public static interface IFrameStats extends INativeObject {

        /** **/
        public float getlastFPS();

        /** **/
        public void setlastFPS(float _jni_value_);

        /** **/
        public float getavgFPS();

        /** **/
        public void setavgFPS(float _jni_value_);

        /** **/
        public float getbestFPS();

        /** **/
        public void setbestFPS(float _jni_value_);

        /** **/
        public float getworstFPS();

        /** **/
        public void setworstFPS(float _jni_value_);

        /** **/
        public long getbestFrameTime();

        /** **/
        public void setbestFrameTime(long _jni_value_);

        /** **/
        public long getworstFrameTime();

        /** **/
        public void setworstFrameTime(long _jni_value_);

        /** **/
        public int gettriangleCount();

        /** **/
        public void settriangleCount(int _jni_value_);

        /** **/
        public int getbatchCount();

        /** **/
        public void setbatchCount(int _jni_value_);
    }

    public interface IViewportList extends INativeObject, org.std.Imap<Integer, org.ogre4j.IViewport> {

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
        public org.ogre4j.IViewport get(int key);

        /** **/
        public void insert(int key, org.ogre4j.IViewport value);
    }

    public interface IRenderTargetListenerList extends INativeObject, org.std.Ivector<org.ogre4j.IRenderTargetListener> {

        /** **/
        public void assign(int num, org.ogre4j.IRenderTargetListener val);

        /** **/
        public org.ogre4j.IRenderTargetListener at(int loc);

        /** **/
        public org.ogre4j.IRenderTargetListener back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IRenderTargetListener front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.IRenderTargetListener val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    /** **/
    public String getName();

    /** **/
    public void getMetrics(LongPointer width, LongPointer height, LongPointer colourDepth);

    /** **/
    public long getWidth();

    /** **/
    public long getHeight();

    /** **/
    public long getColourDepth();

    /** 
    Tells the target to update it's contents. **/
    public void update(boolean swapBuffers);

    /** 
    Swaps the frame buffers to display the next frame. **/
    public void swapBuffers(boolean waitForVSync);

    /** 
    Adds a viewport to the rendering target. **/
    public org.ogre4j.IViewport addViewport(org.ogre4j.ICamera cam, int ZOrder, float left, float top, float width, float height);

    /** 
    Returns the number of viewports attached to this target. **/
    public int getNumViewports();

    /** 
    Retrieves a pointer to the viewport with the given index. **/
    public org.ogre4j.IViewport getViewport(int index);

    /** 
    Removes a viewport at a given ZOrder. **/
    public void removeViewport(int ZOrder);

    /** 
    Removes all viewports on this target. **/
    public void removeAllViewports();

    /** 
    Retieves details of current rendering performance. **/
    public void getStatistics(FloatPointer lastFPS, FloatPointer avgFPS, FloatPointer bestFPS, FloatPointer worstFPS);

    /** **/
    public org.ogre4j.IRenderTarget.IFrameStats getStatistics();

    /** 
    Individual stats access - gets the number of frames per second (FPS) based on the last frame rendered. **/
    public float getLastFPS();

    /** 
    Individual stats access - gets the average frames per second (FPS) since call to . **/
    public float getAverageFPS();

    /** 
    Individual stats access - gets the best frames per second (FPS) since call to . **/
    public float getBestFPS();

    /** 
    Individual stats access - gets the worst frames per second (FPS) since call to . **/
    public float getWorstFPS();

    /** 
    Individual stats access - gets the best frame time **/
    public float getBestFrameTime();

    /** 
    Individual stats access - gets the worst frame time **/
    public float getWorstFrameTime();

    /** 
    Resets saved frame-rate statistices. **/
    public void resetStatistics();

    /** 
    Gets a custom (maybe platform-specific) attribute. **/
    public void getCustomAttribute(String name, VoidPointer pData);

    /** 
    Add a listener to this  which will be called back before & after rendering. **/
    public void addListener(org.ogre4j.IRenderTargetListener listener);

    /** 
    Removes a  previously registered using addListener. **/
    public void removeListener(org.ogre4j.IRenderTargetListener listener);

    /** 
    Removes all listeners from this instance. **/
    public void removeAllListeners();

    /** 
    Sets the priority of this render target in relation to the others. **/
    public void setPriority(short priority);

    /** 
    Gets the priority of a render target. **/
    public short getPriority();

    /** 
    Used to retrieve or set the active state of the render target. **/
    public boolean isActive();

    /** 
    Used to set the active state of the render target. **/
    public void setActive(boolean state);

    /** 
    Sets whether this target should be automatically updated if Ogre's rendering loop or  is being used. **/
    public void setAutoUpdated(boolean autoupdate);

    /** 
    Gets whether this target is automatically updated if Ogre's rendering loop or  is being used. **/
    public boolean isAutoUpdated();

    /** 
    Copies the current contents of the render target to a pixelbox. **/
    public void copyContentsToMemory(org.ogre4j.IPixelBox dst, org.ogre4j.RenderTarget.FrameBuffer buffer);

    /** 
    Suggests a pixel format to use for extracting the data in this target, when calling copyContentsToMemory. **/
    public org.ogre4j.PixelFormat suggestPixelFormat();

    /** 
    Writes the current contents of the render target to the named file. **/
    public void writeContentsToFile(String filename);

    /** 
    Writes the current contents of the render target to the (PREFIX)(time-stamp)(SUFFIX) file. **/
    public String writeContentsToTimestampedFile(String filenamePrefix, String filenameSuffix);

    /** **/
    public boolean requiresTextureFlipping();

    /** 
    Gets the number of triangles rendered in the last  call. **/
    public int getTriangleCount();

    /** 
    Gets the number of batches rendered in the last  call. **/
    public int getBatchCount();

    /** 
    Utility method to notify a render target that a camera has been removed, incase it was referring to it as a viewer. **/
    public void _notifyCameraRemoved(org.ogre4j.ICamera cam);

    /** 
    Indicates whether this target is the primary window. The primary window is special in that it is destroyed when ogre is shut down, and cannot be destroyed directly. This is the case because it holds the context for vertex, index buffers and textures. **/
    public boolean isPrimary();

    /** 
    Indicates whether on rendering, linear colour space is converted to sRGB gamma colour space. This is the exact opposite conversion of what is indicated by , and can only be enabled on creation of the render target. For render windows, it's enabled through the 'gamma' creation misc parameter. For textures, it is enabled through the hwGamma parameter to the create call. **/
    public boolean isHardwareGammaEnabled();

    /** 
    Indicates whether multisampling is performed on rendering and at what level. **/
    public long getFSAA();

    /** 
    Get rendersystem specific interface for this . This is used by the  to (un)bind this target, and to get specific information like surfaces and framebuffer objects. **/
    public org.ogre4j.IRenderTarget.IImpl _getImpl();
}
