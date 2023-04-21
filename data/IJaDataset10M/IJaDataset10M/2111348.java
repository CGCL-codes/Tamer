package net.directx4j.graphics.d3d9;

import java.nio.ByteBuffer;
import net.directx4j.graphics.d3d9.constants.D3DERR;
import net.directx4j.graphics.d3d9.structs.D3DRect;
import net.directx4j.graphics.d3d9.structs.D3DSurfaceDesc;

public class Direct3DTexture9 extends Direct3DBaseTexture9 {

    public Direct3DTexture9(long handle) {
        super(handle);
    }

    public boolean AddDirtyRect(D3DRect pDirtyRect) {
        if (nAddDirtyRect(handle, pDirtyRect.getByteBuffer()) == D3DERR.D3D_OK) {
            return true;
        }
        return false;
    }

    public native int nAddDirtyRect(long handel, ByteBuffer pDirtyRect);

    public D3DSurfaceDesc GetLevelDesc(int Level) {
        ByteBuffer buff = nGetLevelDesc(handle, Level);
        if (buff != null) {
            D3DSurfaceDesc desc = new D3DSurfaceDesc();
            desc.setByteBuffer(buff);
            return desc;
        }
        return null;
    }

    private native ByteBuffer nGetLevelDesc(long handle, int Level);

    public Direct3DSurface9 GetSurfaceLevel(int Level) {
        return new Direct3DSurface9(nGetSurfaceLevel(handle, Level));
    }

    private native long nGetSurfaceLevel(long handle, int Level);

    public void update(ByteBuffer imagebuffer) {
        if (imagebuffer != null) {
            nUpdate(handle, imagebuffer);
        }
    }

    private native void nUpdate(long handle, ByteBuffer imagedata);
}
