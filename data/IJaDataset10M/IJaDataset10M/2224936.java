package org.apache.harmony.awt.gl;

import org.apache.harmony.awt.gl.Surface;
import com.google.code.appengine.awt.image.*;

public abstract class GLVolatileImage extends VolatileImage {

    public abstract Surface getImageSurface();
}
