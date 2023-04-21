package org.anddev.andengine.opengl.buffer;

import javax.microedition.khronos.opengles.GL11;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.util.GLHelper;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public abstract class BufferObject {

    private static final int[] HARDWAREBUFFERID_FETCHER = new int[1];

    protected final int[] mBufferData;

    private final int mDrawType;

    protected final FastFloatBuffer mFloatBuffer;

    private int mHardwareBufferID = -1;

    private boolean mLoadedToHardware;

    private boolean mHardwareBufferNeedsUpdate = true;

    private boolean mManaged;

    /**
	 * @param pCapacity
	 * @param pDrawType
	 * @param pManaged when passing <code>true</code> this {@link BufferObject} loads itself to the active {@link BufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 */
    public BufferObject(final int pCapacity, final int pDrawType, final boolean pManaged) {
        this.mDrawType = pDrawType;
        this.mManaged = pManaged;
        this.mBufferData = new int[pCapacity];
        this.mFloatBuffer = new FastFloatBuffer(pCapacity);
        if (pManaged) {
            this.loadToActiveBufferObjectManager();
        }
    }

    public boolean isManaged() {
        return this.mManaged;
    }

    public void setManaged(final boolean pManaged) {
        this.mManaged = pManaged;
    }

    public FastFloatBuffer getFloatBuffer() {
        return this.mFloatBuffer;
    }

    public int getHardwareBufferID() {
        return this.mHardwareBufferID;
    }

    public boolean isLoadedToHardware() {
        return this.mLoadedToHardware;
    }

    void setLoadedToHardware(final boolean pLoadedToHardware) {
        this.mLoadedToHardware = pLoadedToHardware;
    }

    public void setHardwareBufferNeedsUpdate() {
        this.mHardwareBufferNeedsUpdate = true;
    }

    public void selectOnHardware(final GL11 pGL11) {
        final int hardwareBufferID = this.mHardwareBufferID;
        if (hardwareBufferID == -1) {
            return;
        }
        GLHelper.bindBuffer(pGL11, hardwareBufferID);
        if (this.mHardwareBufferNeedsUpdate) {
            this.mHardwareBufferNeedsUpdate = false;
            synchronized (this) {
                GLHelper.bufferData(pGL11, this.mFloatBuffer.mByteBuffer, this.mDrawType);
            }
        }
    }

    public void loadToActiveBufferObjectManager() {
        BufferObjectManager.getActiveInstance().loadBufferObject(this);
    }

    public void unloadFromActiveBufferObjectManager() {
        BufferObjectManager.getActiveInstance().unloadBufferObject(this);
    }

    public void loadToHardware(final GL11 pGL11) {
        this.mHardwareBufferID = this.generateHardwareBufferID(pGL11);
        this.mLoadedToHardware = true;
    }

    public void unloadFromHardware(final GL11 pGL11) {
        this.deleteBufferOnHardware(pGL11);
        this.mHardwareBufferID = -1;
        this.mLoadedToHardware = false;
    }

    private void deleteBufferOnHardware(final GL11 pGL11) {
        GLHelper.deleteBuffer(pGL11, this.mHardwareBufferID);
    }

    private int generateHardwareBufferID(final GL11 pGL11) {
        pGL11.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0);
        return HARDWAREBUFFERID_FETCHER[0];
    }
}
