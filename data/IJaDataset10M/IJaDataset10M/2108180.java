package org.xith3d.ui.hud.base;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.scenegraph.Texture;
import org.xith3d.ui.hud.utils.HUDUnitsMeasurement;

/**
 * This is an interface for the most basic methods of a Border.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface Border {

    /**
     * This class is used to describe a Border Widget.
     * You can pass it to the Border constructor.
     * Modifications on the used instance after creating the Border Widget
     * won't have any effect.
     * 
     * @author Marvin Froehlich (aka Qudus)
     */
    public static class Description {

        private HUDUnitsMeasurement measurement;

        private float leftWidth;

        private float rightWidth;

        private float topHeight;

        private float bottomHeight;

        private float llUpperHeight;

        private float llRightWidth;

        private float lrLeftWidth;

        private float lrUpperHeight;

        private float urLowerHeight;

        private float urLeftWidth;

        private float ulRightWidth;

        private float ulLowerHeight;

        private Color4f color;

        private Texture bottomTexture;

        private Texture rightTexture;

        private Texture topTexture;

        private Texture leftTexture;

        private Texture llTexture;

        private Texture llUpperTexture;

        private Texture llRightTexture;

        private Texture lrTexture;

        private Texture lrLeftTexture;

        private Texture lrUpperTexture;

        private Texture urTexture;

        private Texture urLowerTexture;

        private Texture urLeftTexture;

        private Texture ulTexture;

        private Texture ulRightTexture;

        private Texture ulLowerTexture;

        public void setMeasurement(HUDUnitsMeasurement measurement) {
            this.measurement = measurement;
        }

        public HUDUnitsMeasurement getMeasurement() {
            return (measurement);
        }

        public boolean hasNonZeroSize() {
            return ((bottomHeight + rightWidth + topHeight + leftWidth) > 0f);
        }

        public void setBottomHeight(float bh) {
            this.bottomHeight = bh;
        }

        public float getBottomHeight() {
            return (bottomHeight);
        }

        public void setRightWidth(float rw) {
            this.rightWidth = rw;
        }

        public float getRightWidth() {
            return (rightWidth);
        }

        public void setTopHeight(float th) {
            this.topHeight = th;
        }

        public float getTopHeight() {
            return (topHeight);
        }

        public void setLeftWidth(float lw) {
            this.leftWidth = lw;
        }

        public float getLeftWidth() {
            return (leftWidth);
        }

        public void setLLupperHeight(float value) {
            this.llUpperHeight = value;
        }

        public float getLLupperHeight() {
            if (llUpperTexture == null) return (0f); else return (llUpperHeight);
        }

        public void setLLrightWidth(float value) {
            this.llRightWidth = value;
        }

        public float getLLrightWidth() {
            if (llRightTexture == null) return (0f); else return (llRightWidth);
        }

        public void setLRleftWidth(float value) {
            this.lrLeftWidth = value;
        }

        public float getLRleftWidth() {
            if (lrLeftTexture == null) return (0f); else return (lrLeftWidth);
        }

        public void setLRupperHeight(float value) {
            this.lrUpperHeight = value;
        }

        public float getLRupperHeight() {
            if (lrUpperTexture == null) return (0f); else return (lrUpperHeight);
        }

        public void setURlowerHeight(float value) {
            this.urLowerHeight = value;
        }

        public float getURlowerHeight() {
            if (urLeftTexture == null) return (0f); else return (urLowerHeight);
        }

        public void setURleftWidth(float value) {
            this.urLeftWidth = value;
        }

        public float getURleftWidth() {
            if (urLeftTexture == null) return (0f); else return (urLeftWidth);
        }

        public void setULrightWidth(float value) {
            this.ulRightWidth = value;
        }

        public float getULrightWidth() {
            if (ulRightTexture == null) return (0f); else return (ulRightWidth);
        }

        public void setULlowerHeight(float value) {
            this.ulLowerHeight = value;
        }

        public float getULlowerHeight() {
            if (ulLowerTexture == null) return (0f); else return (ulLowerHeight);
        }

        public void setColor(Color4f color) {
            this.color = color;
        }

        public void setColor(Color3f color) {
            this.color = new Color4f(color.x, color.y, color.z, 0.0f);
        }

        public Color4f getColor() {
            return (color);
        }

        public void setBottomTexture(Texture texture) {
            this.bottomTexture = texture;
        }

        public Texture setBottomTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setBottomTexture(tex);
            return (tex);
        }

        public Texture getBottomTexture() {
            return (bottomTexture);
        }

        public void setRightTexture(Texture texture) {
            this.rightTexture = texture;
        }

        public Texture setRightTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setRightTexture(tex);
            return (tex);
        }

        public Texture getRightTexture() {
            return (rightTexture);
        }

        public void setTopTexture(Texture texture) {
            this.topTexture = texture;
        }

        public Texture setTopTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setTopTexture(tex);
            return (tex);
        }

        public Texture getTopTexture() {
            return (topTexture);
        }

        public void setLeftTexture(Texture texture) {
            this.leftTexture = texture;
        }

        public Texture setLeftTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLeftTexture(tex);
            return (tex);
        }

        public Texture getLeftTexture() {
            return (leftTexture);
        }

        public void setLLTexture(Texture texture) {
            this.llTexture = texture;
        }

        public Texture setLLTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLLTexture(tex);
            return (tex);
        }

        public Texture getLLTexture() {
            return (llTexture);
        }

        public void setLLupperTexture(Texture texture, float height) {
            this.llUpperTexture = texture;
            this.llUpperHeight = height;
        }

        public Texture setLLupperTexture(String texture, boolean alpha, float height) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLLupperTexture(tex, height);
            return (tex);
        }

        public Texture getLLupperTexture() {
            return (llUpperTexture);
        }

        public void setLLrightTexture(Texture texture, float width) {
            this.llRightTexture = texture;
            this.llRightWidth = width;
        }

        public Texture setLLrightTexture(String texture, boolean alpha, float width) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLLrightTexture(tex, width);
            return (tex);
        }

        public Texture getLLrightTexture() {
            return (llRightTexture);
        }

        public void setLRTexture(Texture texture) {
            this.lrTexture = texture;
        }

        public Texture setLRTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLRTexture(tex);
            return (tex);
        }

        public Texture getLRTexture() {
            return (lrTexture);
        }

        public void setLRleftTexture(Texture texture, float width) {
            this.lrLeftTexture = texture;
            this.lrLeftWidth = width;
        }

        public Texture setLRleftTexture(String texture, boolean alpha, float width) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLRleftTexture(tex, width);
            return (tex);
        }

        public Texture getLRleftTexture() {
            return (lrLeftTexture);
        }

        public void setLRupperTexture(Texture texture, float height) {
            this.lrUpperTexture = texture;
            this.lrUpperHeight = height;
        }

        public Texture setLRupperTexture(String texture, boolean alpha, float height) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setLRupperTexture(tex, height);
            return (tex);
        }

        public Texture getLRupperTexture() {
            return (lrUpperTexture);
        }

        public void setURTexture(Texture texture) {
            this.urTexture = texture;
        }

        public Texture setURTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setURTexture(tex);
            return (tex);
        }

        public Texture getURTexture() {
            return (urTexture);
        }

        public void setURlowerTexture(Texture texture, float height) {
            this.urLowerTexture = texture;
            this.urLowerHeight = height;
        }

        public Texture setURlowerTexture(String texture, boolean alpha, float height) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setURlowerTexture(tex, height);
            return (tex);
        }

        public Texture getURlowerTexture() {
            return (urLowerTexture);
        }

        public void setURleftTexture(Texture texture, float width) {
            this.urLeftTexture = texture;
            this.urLeftWidth = width;
        }

        public Texture setURleftTexture(String texture, boolean alpha, float width) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setURleftTexture(tex, width);
            return (tex);
        }

        public Texture getURleftTexture() {
            return (urLeftTexture);
        }

        public void setULTexture(Texture texture) {
            this.ulTexture = texture;
        }

        public Texture setULTexture(String texture, boolean alpha) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setULTexture(tex);
            return (tex);
        }

        public Texture getULTexture() {
            return (ulTexture);
        }

        public void setULrightTexture(Texture texture, float width) {
            this.ulRightTexture = texture;
            this.ulRightWidth = width;
        }

        public Texture setULrightTexture(String texture, boolean alpha, float width) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setULrightTexture(tex, width);
            return (tex);
        }

        public Texture getULrightTexture() {
            return (ulRightTexture);
        }

        public void setULlowerTexture(Texture texture, float height) {
            this.ulLowerTexture = texture;
            this.ulLowerHeight = height;
        }

        public Texture setULlowerTexture(String texture, boolean alpha, float height) {
            Texture tex = TextureLoader.getInstance().getTexture(texture, (alpha ? Texture.RGBA : Texture.RGB));
            setULlowerTexture(tex, height);
            return (tex);
        }

        public Texture getULlowerTexture() {
            return (ulLowerTexture);
        }

        public void collectTexturesByBaseName(String baseName, String extension, boolean alpha, float bs_llu, float bs_llr, float bs_lrl, float bs_lrup, float bs_urlo, float bs_urle, float bs_ulr, float bs_ull) {
            baseName = baseName.replace('\\', '/');
            if (!baseName.endsWith("/")) baseName += "/";
            if (extension.startsWith(".")) extension = extension.substring(1);
            this.setBottomTexture(baseName + "bottom." + extension, alpha);
            if (this.getBottomTexture() == TextureLoader.getFallbackTexture()) this.setBottomTexture(null);
            this.setRightTexture(baseName + "right." + extension, alpha);
            if (this.getRightTexture() == TextureLoader.getFallbackTexture()) this.setRightTexture(null);
            this.setTopTexture(baseName + "top." + extension, alpha);
            if (this.getTopTexture() == TextureLoader.getFallbackTexture()) this.setTopTexture(null);
            this.setLeftTexture(baseName + "left." + extension, alpha);
            if (this.getLeftTexture() == TextureLoader.getFallbackTexture()) this.setLeftTexture(null);
            this.setLLTexture(baseName + "ll." + extension, alpha);
            if (this.getLLTexture() == TextureLoader.getFallbackTexture()) this.setLLTexture(null);
            this.setLRTexture(baseName + "lr." + extension, alpha);
            if (this.getLRTexture() == TextureLoader.getFallbackTexture()) this.setLRTexture(null);
            this.setURTexture(baseName + "ur." + extension, alpha);
            if (this.getURTexture() == TextureLoader.getFallbackTexture()) this.setURTexture(null);
            this.setULTexture(baseName + "ul." + extension, alpha);
            if (this.getULTexture() == TextureLoader.getFallbackTexture()) this.setULTexture(null);
            this.setLLupperTexture(baseName + "ll_upper." + extension, alpha, bs_llu);
            if (this.getLLupperTexture() == TextureLoader.getFallbackTexture()) this.setLLupperTexture(null, 0f);
            this.setLLrightTexture(baseName + "ll_right." + extension, alpha, bs_llr);
            if (this.getLLrightTexture() == TextureLoader.getFallbackTexture()) this.setLLrightTexture(null, 0f);
            this.setLRleftTexture(baseName + "lr_left." + extension, alpha, bs_lrl);
            if (this.getLRleftTexture() == TextureLoader.getFallbackTexture()) this.setLRleftTexture(null, 0f);
            this.setLRupperTexture(baseName + "lr_upper." + extension, alpha, bs_lrup);
            if (this.getLRupperTexture() == TextureLoader.getFallbackTexture()) this.setLRupperTexture(null, 0f);
            this.setURlowerTexture(baseName + "ur_lower." + extension, alpha, bs_urlo);
            if (this.getURlowerTexture() == TextureLoader.getFallbackTexture()) this.setURlowerTexture(null, 0f);
            this.setURleftTexture(baseName + "ur_left." + extension, alpha, bs_urle);
            if (this.getURleftTexture() == TextureLoader.getFallbackTexture()) this.setURleftTexture(null, 0f);
            this.setULrightTexture(baseName + "ul_right." + extension, alpha, bs_ulr);
            if (this.getULrightTexture() == TextureLoader.getFallbackTexture()) this.setULrightTexture(null, 0f);
            this.setULlowerTexture(baseName + "ul_lower." + extension, alpha, bs_ull);
            if (this.getULlowerTexture() == TextureLoader.getFallbackTexture()) this.setULlowerTexture(null, 0f);
        }

        public void collectTexturesByBaseName(String baseName, String extension, boolean alpha) {
            collectTexturesByBaseName(baseName, extension, alpha, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }

        /**
         * @return a clone of this instance.
         */
        @Override
        public Description clone() {
            return (new Description(this));
        }

        /**
         * Clone-Contructor
         * 
         * @param bd the original to be duplicated
         */
        public Description(Description bd) {
            this.measurement = bd.measurement;
            this.bottomHeight = bd.bottomHeight;
            this.rightWidth = bd.rightWidth;
            this.topHeight = bd.topHeight;
            this.leftWidth = bd.leftWidth;
            this.llUpperHeight = bd.llUpperHeight;
            this.llRightWidth = bd.llRightWidth;
            this.lrLeftWidth = bd.lrLeftWidth;
            this.lrUpperHeight = bd.lrUpperHeight;
            this.urLowerHeight = bd.urLowerHeight;
            this.urLeftWidth = bd.urLeftWidth;
            this.ulRightWidth = bd.ulRightWidth;
            this.ulLowerHeight = bd.ulLowerHeight;
            this.color = bd.color;
            this.llTexture = bd.llTexture;
            this.llUpperTexture = bd.llUpperTexture;
            this.llRightTexture = bd.llRightTexture;
            this.lrTexture = bd.lrTexture;
            this.lrLeftTexture = bd.lrLeftTexture;
            this.lrUpperTexture = bd.lrUpperTexture;
            this.urTexture = bd.urTexture;
            this.urLowerTexture = bd.urLowerTexture;
            this.urLeftTexture = bd.urLeftTexture;
            this.ulTexture = bd.ulTexture;
            this.ulRightTexture = bd.ulRightTexture;
            this.ulLowerTexture = bd.ulLowerTexture;
            this.bottomTexture = bd.bottomTexture;
            this.rightTexture = bd.rightTexture;
            this.topTexture = bd.topTexture;
            this.leftTexture = bd.leftTexture;
        }

        public Description(float bottom, float right, float top, float left, Texture texBottom, Texture texRight, Texture texTop, Texture texLeft, Texture texLL, Texture texLLupper, float heightLLupper, Texture texLLright, float widthLLright, Texture texLR, Texture texLRleft, float widthLRleft, Texture texLRupper, float heightLRupper, Texture texUR, Texture texURlower, float heightURlower, Texture texURleft, float widthURleft, Texture texUL, Texture texULright, float widthULright, Texture texULlower, float heightULlower) {
            this.measurement = HUDUnitsMeasurement.PIXELS;
            this.bottomHeight = bottom;
            this.rightWidth = right;
            this.topHeight = top;
            this.leftWidth = left;
            this.bottomTexture = texBottom;
            this.rightTexture = texRight;
            this.topTexture = texTop;
            this.leftTexture = texLeft;
            this.llTexture = texLL;
            this.llUpperTexture = texLLupper;
            this.llUpperHeight = heightLLupper;
            this.llRightTexture = texLLright;
            this.llRightWidth = widthLLright;
            this.lrTexture = texLR;
            this.lrLeftTexture = texLRleft;
            this.lrLeftWidth = widthLRleft;
            this.lrUpperTexture = texLRupper;
            this.lrUpperHeight = heightLRupper;
            this.urTexture = texUR;
            this.urLowerTexture = texURlower;
            this.urLowerHeight = heightURlower;
            this.urLeftTexture = texURleft;
            this.urLeftWidth = widthURleft;
            this.ulTexture = texUL;
            this.ulRightTexture = texULright;
            this.ulRightWidth = widthULright;
            this.ulLowerTexture = texULlower;
            this.ulLowerHeight = heightULlower;
        }

        public Description(float bottom, float right, float top, float left, Texture texBottom, Texture texRight, Texture texTop, Texture texLeft, Texture texLL, Texture texLR, Texture texUR, Texture texUL) {
            this(bottom, right, top, left, texBottom, texRight, texTop, texLeft, texLL, null, 0f, null, 0f, texLR, null, 0f, null, 0f, texUR, null, 0f, null, 0f, texUL, null, 0f, null, 0f);
        }

        public Description(float bottom, float right, float top, float left, boolean alpha, String texBottom, String texRight, String texTop, String texLeft, String texLL, String texLLupper, float heightLLupper, String texLLright, float widthLLright, String texLR, String texLRleft, float widthLRleft, String texLRupper, float heightLRupper, String texUR, String texURlower, float heightURlower, String texURleft, float widthURleft, String texUL, String texULright, float widthULright, String texULlower, float heightULlower) {
            this(bottom, right, top, left, TextureLoader.getInstance().getTexture(texBottom, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texRight, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texTop, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLeft, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLL, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLLupper, (alpha ? Texture.RGBA : Texture.RGB)), heightLLupper, TextureLoader.getInstance().getTexture(texLLright, (alpha ? Texture.RGBA : Texture.RGB)), widthLLright, TextureLoader.getInstance().getTexture(texLR, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLRleft, (alpha ? Texture.RGBA : Texture.RGB)), widthLRleft, TextureLoader.getInstance().getTexture(texLRupper, (alpha ? Texture.RGBA : Texture.RGB)), heightLRupper, TextureLoader.getInstance().getTexture(texUR, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texURlower, (alpha ? Texture.RGBA : Texture.RGB)), heightURlower, TextureLoader.getInstance().getTexture(texURleft, (alpha ? Texture.RGBA : Texture.RGB)), widthURleft, TextureLoader.getInstance().getTexture(texUL, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texULright, (alpha ? Texture.RGBA : Texture.RGB)), widthULright, TextureLoader.getInstance().getTexture(texULlower, (alpha ? Texture.RGBA : Texture.RGB)), heightULlower);
        }

        public Description(float bottom, float right, float top, float left, boolean alpha, String texBottom, String texRight, String texTop, String texLeft, String texLL, String texLR, String texUR, String texUL) {
            this(bottom, right, top, left, TextureLoader.getInstance().getTexture(texBottom, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texRight, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texTop, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLeft, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLL, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texLR, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texUR, (alpha ? Texture.RGBA : Texture.RGB)), TextureLoader.getInstance().getTexture(texUL, (alpha ? Texture.RGBA : Texture.RGB)));
        }

        public Description(float bottom, float right, float top, float left) {
            this(bottom, right, top, left, (Texture) null, null, null, null, null, null, 0f, null, 0f, null, null, 0f, null, 0f, null, null, 0f, null, 0f, null, null, 0f, null, 0f);
        }

        public Description(float bottom, float right, float top, float left, Color4f color) {
            this(bottom, right, top, left);
            this.setColor(color);
        }

        public Description(float width, Color4f color) {
            this(width, width, width, width, color);
        }

        public Description(float bottom, float right, float top, float left, Color3f color) {
            this(bottom, right, top, left);
            this.setColor(color);
        }

        public Description(float width, Color3f color) {
            this(width, width, width, width, color);
        }
    }

    /**
     * @return the height of the bottom side of this Border
     */
    public float getBottomHeight();

    /**
     * @return the width of the right side of this Border
     */
    public float getRightWidth();

    /**
     * @return the height of the top side of this Border
     */
    public float getTopHeight();

    /**
     * @return the width of the left side of this Border
     */
    public float getLeftWidth();
}
