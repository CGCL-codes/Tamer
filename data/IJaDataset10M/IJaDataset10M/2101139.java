package com.cirnoworks.cis.impl.monkey2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import me.fantasy2.Cell;
import me.fantasy2.Function;
import odk.lang.FastMath;
import com.cirnoworks.cis.data.ImageResource.Blend;
import com.jme.image.Image.Format;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture2D;
import com.jme.intersection.CollisionResults;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.BlendEquation;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureKey;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;
import com.jme.util.resource.ResourceLocatorTool;

public class MassiveImage extends TriMesh {

    private final int maxQuads;

    private final ShortBuffer idxs;

    FloatBuffer vex;

    FloatBuffer tex;

    FloatBuffer color;

    IntBuffer idx;

    short quads;

    FloatBuffer clearFloat;

    IntBuffer clearInt;

    private float width, height;

    {
        setCullHint(CullHint.Never);
    }

    public MassiveImage(String name, int maxQuads) {
        super();
        if (maxQuads > 16384) {
            throw new IllegalArgumentException("max quads must be less than 16384");
        }
        setName(name);
        this.maxQuads = maxQuads;
        idxs = BufferUtils.createShortBuffer(6);
        idxs.put(new short[] { 0, 1, 2, 0, 2, 3 });
        idxs.clear();
        vex = BufferUtils.createFloatBuffer(maxQuads * 12);
        tex = BufferUtils.createFloatBuffer(maxQuads * 8);
        color = BufferUtils.createFloatBuffer(maxQuads * 16);
        idx = BufferUtils.createIntBuffer(maxQuads * 6);
        clearFloat = BufferUtils.createFloatBuffer(maxQuads * 16);
        clearInt = BufferUtils.createIntBuffer(maxQuads * 6);
        setVertexBuffer(vex);
        setTextureCoords(new TexCoords(tex));
        setColorBuffer(color);
        setIndexBuffer(idx);
        setCullHint(CullHint.Never);
        for (short qu = 0; qu < maxQuads; qu++) {
            short q = (short) (qu * 4);
            idx.put(q);
            idx.put((short) (q + 1));
            idx.put((short) (q + 2));
            idx.put(q);
            idx.put((short) (q + 2));
            idx.put((short) (q + 3));
        }
        idx.clear();
    }

    public void setImage(Renderer r, String imgName, Blend blendMode) {
        TextureKey key = new TextureKey(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, imgName), true, Format.Guess);
        Texture2D tex = (Texture2D) TextureManager.loadTexture(key);
        tex.setMagnificationFilter(MagnificationFilter.Bilinear);
        tex.setMinificationFilter(MinificationFilter.BilinearNoMipMaps);
        setTexture(r, tex, blendMode);
        setWidth(tex.getImage().getWidth());
        setHeight(tex.getImage().getHeight());
    }

    public void setTexture(Renderer r, Texture2D tex, Blend blendMode) {
        TextureState ts = r.createTextureState();
        ts.setTexture(tex);
        ts.setEnabled(true);
        BlendState as = r.createBlendState();
        as.setEnabled(true);
        switch(blendMode) {
            case NONE:
                as.setBlendEnabled(false);
                break;
            case ALPHA:
                as.setBlendEnabled(true);
                break;
            case ALPHAADD:
                as.setBlendEnabled(true);
                as.setSourceFunction(SourceFunction.SourceAlpha);
                as.setDestinationFunction(DestinationFunction.One);
                as.setBlendEquation(BlendEquation.Add);
                break;
            case MODULATE:
                as.setBlendEnabled(true);
                as.setSourceFunction(SourceFunction.Zero);
                as.setDestinationFunctionAlpha(DestinationFunction.One);
                as.setDestinationFunctionRGB(DestinationFunction.SourceColor);
                as.setBlendEquation(BlendEquation.Add);
        }
        setRenderState(ts);
        setRenderState(as);
        updateRenderState();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void addElement(Cell c) {
    }

    public void addLaser(MassiveFrame f, float rot, float scaleX, float scaleY, int color, int alpha, float x, float y, boolean flipH, boolean flipV, Function fun) {
        final int steps = 16;
        final float cr = ((color & 0xff0000) >> 16) / 255f;
        final float cg = ((color & 0xff00) >> 8) / 255f;
        final float cb = (color & 0xff) / 255f;
        final float ca = alpha / 255f;
        float[] xl = new float[steps];
        float[] yl = new float[steps];
        float[] xr = new float[steps];
        float[] yr = new float[steps];
        float[] ty = new float[steps];
        float tl = f.tx1;
        float tr = f.tx4;
        float tup = f.ty1;
        float tdown = f.ty2;
        float th = tup - tdown;
        float left = f.p1x;
        float right = f.p4x;
        float up = f.p1y;
        float down = f.p2y;
        float width = right - left;
        float height = up - down;
        float txl, txr, tyl, tyr;
        float zz, ww;
        final float zw;
        zz = FastMath.sinf(rot * .5f);
        ww = FastMath.cosf(rot * .5f);
        zw = zz * ww;
        zz *= zz;
        ww *= ww;
        for (int i = 0; i < steps; i++) {
            float percent = ((float) (i - 1)) / steps;
            float sc = (1 - fun.f(percent)) * 0.5f * width;
            ty[i] = th * percent + tdown;
            float left0 = (left + sc) * scaleX;
            float right0 = (right - sc) * scaleX;
            float y0 = (height * percent + down) * scaleY;
            txl = ww * left0 - 2 * zw * y0 - zz * left0;
            txr = ww * right0 - 2 * zw * y0 - zz * right0;
            tyl = 2 * zw * left0 - zz * y0 + ww * y0;
            tyr = 2 * zw * right0 - zz * y0 + ww * y0;
            xl[i] = txl;
            xr[i] = txr;
            yl[i] = tyl;
            yr[i] = tyr;
            if (i > 0) {
                float x1 = xl[i] + x;
                float y1 = yl[i] + y;
                float x2 = xl[i - 1] + x;
                float y2 = yl[i - 1] + y;
                float x3 = xr[i - 1] + x;
                float y3 = yr[i - 1] + y;
                float x4 = xr[i] + x;
                float y4 = yr[i] + y;
                if (flipH) {
                    if (flipV) {
                        addQuad(x3, y3, 0, x4, y4, 0, x1, y1, 0, x2, y2, 0, cr, cg, cb, ca, tl, ty[i], tl, ty[i - 1], tr, ty[i - 1], tr, ty[i]);
                    } else {
                        addQuad(x4, y4, 0, x3, y3, 0, x2, y2, 0, x1, y1, 0, cr, cg, cb, ca, tl, ty[i], tl, ty[i - 1], tr, ty[i - 1], tr, ty[i]);
                    }
                } else if (flipV) {
                    addQuad(x2, y2, 0, x1, y1, 0, x4, y4, 0, x3, y3, 0, cr, cg, cb, ca, tl, ty[i], tl, ty[i - 1], tr, ty[i - 1], tr, ty[i]);
                } else {
                    addQuad(x1, y1, 0, x2, y2, 0, x3, y3, 0, x4, y4, 0, cr, cg, cb, ca, tl, ty[i], tl, ty[i - 1], tr, ty[i - 1], tr, ty[i]);
                }
            }
        }
    }

    public void add(MassiveFrame f, float rot, float rotX, float rotY, float scaleX, float scaleY, int color, int alpha, float x, float y, boolean flipH, boolean flipV) {
        float x1, y1, z1 = 0, x2, y2, z2 = 0, x3, y3, z3 = 0, x4, y4, z4 = 0;
        final float cr = ((color & 0xff0000) >> 16) / 255f;
        final float cg = ((color & 0xff00) >> 8) / 255f;
        final float cb = (color & 0xff) / 255f;
        final float ca = alpha / 255f;
        x1 = f.p1x;
        y1 = f.p1y;
        x2 = f.p2x;
        y2 = f.p2y;
        x3 = f.p3x;
        y3 = f.p3y;
        x4 = f.p4x;
        y4 = f.p4y;
        x1 *= scaleX;
        y1 *= scaleY;
        x2 *= scaleX;
        y2 *= scaleY;
        x3 *= scaleX;
        y3 *= scaleY;
        x4 *= scaleX;
        y4 *= scaleY;
        if (rotX != 0 || rotY != 0) {
            float angle;
            float sinRoll, sinPitch, sinYaw, cosRoll, cosPitch, cosYaw;
            angle = rot * 0.5f;
            sinPitch = FastMath.sinf(angle);
            cosPitch = FastMath.cosf(angle);
            angle = rotY * 0.5f;
            sinRoll = FastMath.sinf(angle);
            cosRoll = FastMath.cosf(angle);
            angle = rotX * 0.5f;
            sinYaw = FastMath.sinf(angle);
            cosYaw = FastMath.cosf(angle);
            float cosRollXcosPitch = cosRoll * cosPitch;
            float sinRollXsinPitch = sinRoll * sinPitch;
            float cosRollXsinPitch = cosRoll * sinPitch;
            float sinRollXcosPitch = sinRoll * cosPitch;
            float qw = (cosRollXcosPitch * cosYaw - sinRollXsinPitch * sinYaw);
            float qww = qw * qw;
            float qx = (cosRollXcosPitch * sinYaw + sinRollXsinPitch * cosYaw);
            float qxx = qx * qx;
            float qy = (sinRollXcosPitch * cosYaw + cosRollXsinPitch * sinYaw);
            float qyy = qy * qy;
            float qz = (cosRollXsinPitch * cosYaw - sinRollXcosPitch * sinYaw);
            float qzz = qz * qz;
            float qxy = qx * qy;
            float qxz = qx * qz;
            float qxw = qx * qw;
            float qyz = qy * qz;
            float qyw = qy * qw;
            float qzw = qz * qw;
            float n = com.jme.math.FastMath.invSqrt(qww + qxx + qyy + qzz);
            qx *= n;
            qy *= n;
            qz *= n;
            qw *= n;
            float tx, ty;
            tx = qww * x1 + 2 * qyw * z1 - 2 * qzw * y1 + qxx * x1 + 2 * qxy * y1 + 2 * qxz * z1 - qzz * x1 - qyy * x1;
            ty = 2 * qxy * x1 + qyy * y1 + 2 * qyz * z1 + 2 * qzw * x1 - qzz * y1 + qww * y1 - 2 * qxw * z1 - qxx * y1;
            z1 = 2 * qxz * x1 + 2 * qyz * y1 + qzz * z1 - 2 * qyw * x1 - qyy * z1 + 2 * qxw * y1 - qxx * z1 + qww * z1;
            x1 = tx;
            y1 = ty;
            tx = qww * x2 + 2 * qyw * z2 - 2 * qzw * y2 + qxx * x2 + 2 * qxy * y2 + 2 * qxz * z2 - qzz * x2 - qyy * x2;
            ty = 2 * qxy * x2 + qyy * y2 + 2 * qyz * z2 + 2 * qzw * x2 - qzz * y2 + qww * y2 - 2 * qxw * z2 - qxx * y2;
            z2 = 2 * qxz * x2 + 2 * qyz * y2 + qzz * z2 - 2 * qyw * x2 - qyy * z2 + 2 * qxw * y2 - qxx * z2 + qww * z2;
            x2 = tx;
            y2 = ty;
            tx = qww * x3 + 2 * qyw * z3 - 2 * qzw * y3 + qxx * x3 + 2 * qxy * y3 + 2 * qxz * z3 - qzz * x3 - qyy * x3;
            ty = 2 * qxy * x3 + qyy * y3 + 2 * qyz * z3 + 2 * qzw * x3 - qzz * y3 + qww * y3 - 2 * qxw * z3 - qxx * y3;
            z3 = 2 * qxz * x3 + 2 * qyz * y3 + qzz * z3 - 2 * qyw * x3 - qyy * z3 + 2 * qxw * y3 - qxx * z3 + qww * z3;
            x3 = tx;
            y3 = ty;
            tx = qww * x4 + 2 * qyw * z4 - 2 * qzw * y4 + qxx * x4 + 2 * qxy * y4 + 2 * qxz * z4 - qzz * x4 - qyy * x4;
            ty = 2 * qxy * x4 + qyy * y4 + 2 * qyz * z4 + 2 * qzw * x4 - qzz * y4 + qww * y4 - 2 * qxw * z4 - qxx * y4;
            z4 = 2 * qxz * x4 + 2 * qyz * y4 + qzz * z4 - 2 * qyw * x4 - qyy * z4 + 2 * qxw * y4 - qxx * z4 + qww * z4;
            x4 = tx;
            y4 = ty;
        } else if (rot != 0) {
            float tx, ty;
            float zz, ww;
            final float zw;
            zz = FastMath.sinf(rot * .5f);
            ww = FastMath.cosf(rot * .5f);
            zw = zz * ww;
            zz *= zz;
            ww *= ww;
            tx = ww * x1 - 2 * zw * y1 - zz * x1;
            ty = 2 * zw * x1 - zz * y1 + ww * y1;
            x1 = tx;
            y1 = ty;
            tx = ww * x2 - 2 * zw * y2 - zz * x2;
            ty = 2 * zw * x2 - zz * y2 + ww * y2;
            x2 = tx;
            y2 = ty;
            tx = ww * x3 - 2 * zw * y3 - zz * x3;
            ty = 2 * zw * x3 - zz * y3 + ww * y3;
            x3 = tx;
            y3 = ty;
            tx = ww * x4 - 2 * zw * y4 - zz * x4;
            ty = 2 * zw * x4 - zz * y4 + ww * y4;
            x4 = tx;
            y4 = ty;
        }
        x1 += x;
        y1 += y;
        x2 += x;
        y2 += y;
        x3 += x;
        y3 += y;
        x4 += x;
        y4 += y;
        if (flipH) {
            if (flipV) {
                addQuad(x3, y3, z3, x4, y4, z4, x1, y1, z1, x2, y2, z2, cr, cg, cb, ca, f.tx1, f.ty1, f.tx2, f.ty2, f.tx3, f.ty3, f.tx4, f.ty4);
            } else {
                addQuad(x4, y4, z4, x3, y3, z3, x2, y2, z2, x1, y1, z1, cr, cg, cb, ca, f.tx1, f.ty1, f.tx2, f.ty2, f.tx3, f.ty3, f.tx4, f.ty4);
            }
        } else if (flipV) {
            addQuad(x2, y2, z2, x1, y1, z1, x4, y4, z4, x3, y3, z3, cr, cg, cb, ca, f.tx1, f.ty1, f.tx2, f.ty2, f.tx3, f.ty3, f.tx4, f.ty4);
        } else {
            addQuad(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, cr, cg, cb, ca, f.tx1, f.ty1, f.tx2, f.ty2, f.tx3, f.ty3, f.tx4, f.ty4);
        }
    }

    @Override
    public void findCollisions(Spatial arg0, CollisionResults arg1, int arg2) {
    }

    @Override
    public boolean hasCollision(Spatial arg0, boolean arg1, int arg2) {
        return false;
    }

    /**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @param cr
	 * @param cg
	 * @param cb
	 * @param ca
	 * @param textureBuffer
	 */
    public void addQuad(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float cr, float cg, float cb, float ca, float tx1, float ty1, float tx2, float ty2, float tx3, float ty3, float tx4, float ty4) {
        quads++;
        if (quads > maxQuads) {
            throw new RuntimeException("Quad overflow!");
        }
        vex.put(x1);
        vex.put(y1);
        vex.put(z1);
        vex.put(x2);
        vex.put(y2);
        vex.put(z2);
        vex.put(x3);
        vex.put(y3);
        vex.put(z3);
        vex.put(x4);
        vex.put(y4);
        vex.put(z4);
        tex.put(tx1);
        tex.put(ty1);
        tex.put(tx2);
        tex.put(ty2);
        tex.put(tx3);
        tex.put(ty3);
        tex.put(tx4);
        tex.put(ty4);
        color.put(cr);
        color.put(cg);
        color.put(cb);
        color.put(ca);
        color.put(cr);
        color.put(cg);
        color.put(cb);
        color.put(ca);
        color.put(cr);
        color.put(cg);
        color.put(cb);
        color.put(ca);
        color.put(cr);
        color.put(cg);
        color.put(cb);
        color.put(ca);
    }

    public void beginUpdate() {
        quads = 0;
        vex.clear();
        tex.clear();
        color.clear();
        idx.clear();
        clearFloat.position(0);
        clearFloat.limit(vex.capacity());
        vex.put(clearFloat);
        vex.clear();
        clearFloat.position(0);
        clearFloat.limit(tex.capacity());
        tex.put(clearFloat);
        tex.clear();
        clearFloat.position(0);
        clearFloat.limit(color.capacity());
        color.put(clearFloat);
        color.clear();
    }

    public void endUpdate() {
        setVertexCount(quads * 4);
        setTriangleQuantity(quads * 2);
    }
}
