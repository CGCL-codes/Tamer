package trb.jsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import trb.jsg.enums.*;
import trb.jsg.util.Hash;

/**
 * The state for a texture unit state.
 * @author tombr
 *
 */
public class Unit implements Serializable {

    private static final long serialVersionUID = 0L;

    public static UnitComparator unitComparator = new UnitComparator();

    public static Unit disabledUnit = new Unit();

    /** States that references this object */
    ArrayList<State> owners = new ArrayList<State>();

    public Hash hash = new Hash();

    /** true if unit is enabled. */
    private boolean isEnabled = false;

    private Texture texture;

    private TextureEnvMode textureEnvMode = TextureEnvMode.MODULATE;

    private CombineFuncRGB combineFuncRGB = CombineFuncRGB.REPLACE;

    private CombineFuncAlpha combineFuncAlpha = CombineFuncAlpha.MODULATE;

    private CombineSrc combineSrc0RGB = CombineSrc.TEXTURE;

    private CombineSrc combineSrc1RGB = CombineSrc.PREVIOUS;

    private CombineSrc combineSrc2RGB = CombineSrc.PREVIOUS;

    private CombineSrc combineSrc0Alpha = CombineSrc.TEXTURE;

    private CombineSrc combineSrc1Alpha = CombineSrc.PREVIOUS;

    private CombineSrc combineSrc2Alpha = CombineSrc.PREVIOUS;

    private CombineOperandRGB combineOperand0RGB = CombineOperandRGB.SRC_COLOR;

    private CombineOperandRGB combineOperand1RGB = CombineOperandRGB.SRC_COLOR;

    private CombineOperandRGB combineOperand2RGB = CombineOperandRGB.SRC_ALPHA;

    private CombineOperandAlpha combineOperand0Alpha = CombineOperandAlpha.SRC_ALPHA;

    private CombineOperandAlpha combineOperand1Alpha = CombineOperandAlpha.SRC_ALPHA;

    private CombineOperandAlpha combineOperand2Alpha = CombineOperandAlpha.SRC_ALPHA;

    private CombineScale combineScaleRGB = CombineScale.INT_1;

    private CombineScale combineScaleAlpha = CombineScale.INT_1;

    /** The texGenMode for s, t, r and q respectivly. */
    private TexGenMode[] texGenModes = new TexGenMode[] { TexGenMode.EYE_LINEAR, TexGenMode.EYE_LINEAR, TexGenMode.EYE_LINEAR, TexGenMode.EYE_LINEAR };

    private boolean[] texGenEnabled = new boolean[4];

    /**
	 * Creates a unit with the default state.
	 */
    public Unit() {
    }

    /**
	 * Creates an enabled unit with the specified texture.
	 * @param texture
	 */
    public Unit(Texture texture) {
        this.texture = texture;
        isEnabled = true;
    }

    /**
	 * Updates the hash member
	 */
    public void updateHash() {
        hash.setSeed(isEnabled ? 1 : 0);
        if (isEnabled) {
            hash.addInt(texture.stateId);
            hash.addInt(textureEnvMode.get());
            if (textureEnvMode == TextureEnvMode.COMBINE) {
                hash.addInt(combineFuncRGB.get());
                hash.addInt(combineFuncAlpha.get());
                hash.addInt(combineSrc0RGB.get());
                hash.addInt(combineSrc1RGB.get());
                hash.addInt(combineSrc2RGB.get());
                hash.addInt(combineSrc0Alpha.get());
                hash.addInt(combineSrc1Alpha.get());
                hash.addInt(combineSrc2Alpha.get());
                hash.addInt(combineOperand0RGB.get());
                hash.addInt(combineOperand1RGB.get());
                hash.addInt(combineOperand2RGB.get());
                hash.addInt(combineOperand0Alpha.get());
                hash.addInt(combineOperand1Alpha.get());
                hash.addInt(combineOperand2Alpha.get());
                hash.addInt(combineScaleRGB.get());
                hash.addInt(combineScaleAlpha.get());
            }
            for (int i = 0; i < 4; i++) {
                hash.addBoolean(texGenEnabled[i]);
                if (texGenEnabled[i]) {
                    hash.addInt(texGenModes[i].get());
                }
            }
        }
    }

    /**
	 * @param texture the texture to set
	 */
    public void setTexture(Texture texture) {
        if (this.texture != texture) {
            Texture oldTexture = texture;
            this.texture = texture;
            for (State stateOwner : owners) {
                for (Shape shapeOwner : stateOwner.owners) {
                    if (shapeOwner.nativePeer != null) {
                        shapeOwner.nativePeer.textureChanged(oldTexture, texture);
                    }
                }
            }
            stateChanged();
        }
    }

    /**
	 * @return the texture
	 */
    public Texture getTexture() {
        return texture;
    }

    /**
	 * Sets the texture env mode.
	 * @param textureEnvMode the texture env mode
	 */
    public void setTextureEnvMode(TextureEnvMode textureEnvMode) {
        if (this.textureEnvMode != textureEnvMode) {
            this.textureEnvMode = textureEnvMode;
            stateChanged();
        }
    }

    /**
	 * Gets the texture env mode.
	 * @return the texture env mode
	 */
    public TextureEnvMode getTextureEnvMode() {
        return textureEnvMode;
    }

    /**
	 * Enables or disables this unit.
	 * @param isEnabled true to enable, false to disable
	 */
    public void setEnabled(boolean isEnabled) {
        if (this.isEnabled = isEnabled) {
            this.isEnabled = isEnabled;
            stateChanged();
        }
    }

    /**
	 * Checks if unit is enabled
	 * @return true if enabled, otherwise false
	 */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
	 * @param combineFuncRGB the combineFuncRGB to set
	 */
    public void setCombineFuncRGB(CombineFuncRGB combineFuncRGB) {
        this.combineFuncRGB = combineFuncRGB;
        stateChanged();
    }

    /**
	 * @return the combineFuncRGB
	 */
    public CombineFuncRGB getCombineFuncRGB() {
        return combineFuncRGB;
    }

    /**
	 * @param combineFuncAlpha the combineFuncAlpha to set
	 */
    public void setCombineFuncAlpha(CombineFuncAlpha combineFuncAlpha) {
        this.combineFuncAlpha = combineFuncAlpha;
        stateChanged();
    }

    /**
	 * @return the combineFuncAlpha
	 */
    public CombineFuncAlpha getCombineFuncAlpha() {
        return combineFuncAlpha;
    }

    /**
	 * @param combineSrc0RGB the combineSrc0RGB to set
	 */
    public void setCombineSrc0RGB(CombineSrc combineSrc0RGB) {
        this.combineSrc0RGB = combineSrc0RGB;
        stateChanged();
    }

    /**
	 * @return the combineSrc0RGB
	 */
    public CombineSrc getCombineSrc0RGB() {
        return combineSrc0RGB;
    }

    /**
	 * @param combineSrc1RGB the combineSrc1RGB to set
	 */
    public void setCombineSrc1RGB(CombineSrc combineSrc1RGB) {
        this.combineSrc1RGB = combineSrc1RGB;
        stateChanged();
    }

    /**
	 * @return the combineSrc1RGB
	 */
    public CombineSrc getCombineSrc1RGB() {
        return combineSrc1RGB;
    }

    /**
	 * @param combineSrc2RGB the combineSrc2RGB to set
	 */
    public void setCombineSrc2RGB(CombineSrc combineSrc2RGB) {
        this.combineSrc2RGB = combineSrc2RGB;
        stateChanged();
    }

    /**
	 * @return the combineSrc2RGB
	 */
    public CombineSrc getCombineSrc2RGB() {
        return combineSrc2RGB;
    }

    /**
	 * @param combineSrc0Alpha the combineSrc0Alpha to set
	 */
    public void setCombineSrc0Alpha(CombineSrc combineSrc0Alpha) {
        this.combineSrc0Alpha = combineSrc0Alpha;
        stateChanged();
    }

    /**
	 * @return the combineSrc0Alpha
	 */
    public CombineSrc getCombineSrc0Alpha() {
        return combineSrc0Alpha;
    }

    /**
	 * @param combineSrc1Alpha the combineSrc1Alpha to set
	 */
    public void setCombineSrc1Alpha(CombineSrc combineSrc1Alpha) {
        this.combineSrc1Alpha = combineSrc1Alpha;
        stateChanged();
    }

    /**
	 * @return the combineSrc1Alpha
	 */
    public CombineSrc getCombineSrc1Alpha() {
        return combineSrc1Alpha;
    }

    /**
	 * @param combineSrc2Alpha the combineSrc2Alpha to set
	 */
    public void setCombineSrc2Alpha(CombineSrc combineSrc2Alpha) {
        this.combineSrc2Alpha = combineSrc2Alpha;
        stateChanged();
    }

    /**
	 * @return the combineSrc2Alpha
	 */
    public CombineSrc getCombineSrc2Alpha() {
        return combineSrc2Alpha;
    }

    /**
	 * @param combineOperand0RGB the combineOperand0RGB to set
	 */
    public void setCombineOperand0RGB(CombineOperandRGB combineOperand0RGB) {
        this.combineOperand0RGB = combineOperand0RGB;
        stateChanged();
    }

    /**
	 * @return the combineOperand0RGB
	 */
    public CombineOperandRGB getCombineOperand0RGB() {
        return combineOperand0RGB;
    }

    /**
	 * @param combineOperand1RGB the combineOperand1RGB to set
	 */
    public void setCombineOperand1RGB(CombineOperandRGB combineOperand1RGB) {
        this.combineOperand1RGB = combineOperand1RGB;
        stateChanged();
    }

    /**
	 * @return the combineOperand1RGB
	 */
    public CombineOperandRGB getCombineOperand1RGB() {
        return combineOperand1RGB;
    }

    /**
	 * @param combineOperand2RGB the combineOperand2RGB to set
	 */
    public void setCombineOperand2RGB(CombineOperandRGB combineOperand2RGB) {
        this.combineOperand2RGB = combineOperand2RGB;
        stateChanged();
    }

    /**
	 * @return the combineOperand2RGB
	 */
    public CombineOperandRGB getCombineOperand2RGB() {
        return combineOperand2RGB;
    }

    /**
	 * @param combineOperand0Alpha the combineOperand0Alpha to set
	 */
    public void setCombineOperand0Alpha(CombineOperandAlpha combineOperand0Alpha) {
        this.combineOperand0Alpha = combineOperand0Alpha;
        stateChanged();
    }

    /**
	 * @return the combineOperand0Alpha
	 */
    public CombineOperandAlpha getCombineOperand0Alpha() {
        return combineOperand0Alpha;
    }

    /**
	 * @param combineOperand1Alpha the combineOperand1Alpha to set
	 */
    public void setCombineOperand1Alpha(CombineOperandAlpha combineOperand1Alpha) {
        this.combineOperand1Alpha = combineOperand1Alpha;
        stateChanged();
    }

    /**
	 * @return the combineOperand1Alpha
	 */
    public CombineOperandAlpha getCombineOperand1Alpha() {
        return combineOperand1Alpha;
    }

    /**
	 * @param combineOperand2Alpha the combineOperand2Alpha to set
	 */
    public void setCombineOperand2Alpha(CombineOperandAlpha combineOperand2Alpha) {
        this.combineOperand2Alpha = combineOperand2Alpha;
        stateChanged();
    }

    /**
	 * @return the combineOperand2Alpha
	 */
    public CombineOperandAlpha getCombineOperand2Alpha() {
        return combineOperand2Alpha;
    }

    /**
	 * @param combineScaleRGB the combineScaleRGB to set
	 */
    public void setCombineScaleRGB(CombineScale combineScaleRGB) {
        this.combineScaleRGB = combineScaleRGB;
        stateChanged();
    }

    /**
	 * @return the combineScaleRGB
	 */
    public CombineScale getCombineScaleRGB() {
        return combineScaleRGB;
    }

    /**
	 * @param combineScaleAlpha the combineScaleAlpha to set
	 */
    public void setCombineScaleAlpha(CombineScale combineScaleAlpha) {
        this.combineScaleAlpha = combineScaleAlpha;
        stateChanged();
    }

    /**
	 * @return the combineScaleAlpha
	 */
    public CombineScale getCombineScaleAlpha() {
        return combineScaleAlpha;
    }

    /**
	 * Sets the texture generate mode to use on the specified coordinate.
	 * @param coord the coordinate
	 * @param mode the texture generate mode
	 */
    public void setTexGenMode(TextureCoordinate coord, TexGenMode mode) {
        texGenModes[coord.get() - TextureCoordinate.S.get()] = mode;
        stateChanged();
    }

    /**
	 * Gets the texture generate mode of the specified coordinate.
	 * @param coord the coordinate
	 * @return the texture generate mode
	 */
    public TexGenMode getTexGenMode(TextureCoordinate coord) {
        return texGenModes[coord.get() - TextureCoordinate.S.get()];
    }

    /**
	 * Enables or disables texture generation on the specified coordinate.
	 * @param coord the coordinate
	 * @param enable true to enable, false to disable
	 */
    public void setTexGenEnabled(TextureCoordinate coord, boolean enable) {
        texGenEnabled[coord.get() - TextureCoordinate.S.get()] = enable;
        stateChanged();
    }

    /**
	 * Checks if texture generation is enabled on the specified coordinate.
	 * @param coord the coordinate
	 * @return true if texture generation is enabled, otherwise false
	 */
    public boolean isTexGenEnabled(TextureCoordinate coord) {
        return texGenEnabled[coord.get() - TextureCoordinate.S.get()];
    }

    /**
	 * Notifies owner that state has changed.
	 */
    private void stateChanged() {
        for (State stateOwner : owners) {
            stateOwner.stateChanged();
        }
    }

    public String toString() {
        if (this == disabledUnit) {
            return "disabledUnit";
        }
        return "tex" + texture.getWidth() + "x" + texture.getHeight();
    }

    /**
	 * Compares texture unit states
	 */
    public static class UnitComparator implements Comparator<Unit> {

        public int compare(Unit a, Unit b) {
            if (a.isEnabled() ^ b.isEnabled() || a.isEnabled() == false) {
                return a.isEnabled() ^ b.isEnabled() ? 1 : -1;
            }
            if (a.getTexture().stateId != b.getTexture().stateId) {
                return a.getTexture().stateId - b.getTexture().stateId;
            }
            for (int i = 0; i < 4; i++) {
                if (a.texGenEnabled[i] ^ b.texGenEnabled[i]) {
                    return (a.texGenEnabled[i] ^ b.texGenEnabled[i]) ? 1 : -1;
                }
                if (a.texGenModes[i] != b.texGenModes[i]) {
                    return a.texGenModes[i].get() - b.texGenModes[i].get();
                }
            }
            if (a.getTextureEnvMode() != b.getTextureEnvMode() || a.getTextureEnvMode() == TextureEnvMode.REPLACE) {
                return a.getTextureEnvMode().get() - b.getTextureEnvMode().get();
            }
            if (a.getCombineFuncRGB() != b.getCombineFuncRGB()) {
                return a.getCombineFuncRGB().get() - b.getCombineFuncRGB().get();
            }
            if (a.getCombineFuncAlpha() != b.getCombineFuncAlpha()) {
                return a.getCombineFuncAlpha().get() - b.getCombineFuncAlpha().get();
            }
            if (a.getCombineScaleRGB() != b.getCombineScaleRGB()) {
                return a.getCombineScaleRGB().get() - b.getCombineScaleRGB().get();
            }
            if (a.getCombineScaleAlpha() != b.getCombineScaleAlpha()) {
                return a.getCombineScaleAlpha().get() - b.getCombineScaleAlpha().get();
            }
            if (a.getCombineSrc0RGB() != b.getCombineSrc0RGB()) {
                return a.getCombineSrc0RGB().get() - b.getCombineSrc0RGB().get();
            }
            if (a.getCombineSrc1RGB() != b.getCombineSrc1RGB()) {
                return a.getCombineSrc1RGB().get() - b.getCombineSrc1RGB().get();
            }
            if (a.getCombineSrc2RGB() != b.getCombineSrc2RGB()) {
                return a.getCombineSrc2RGB().get() - b.getCombineSrc2RGB().get();
            }
            if (a.getCombineSrc0Alpha() != b.getCombineSrc0Alpha()) {
                return a.getCombineSrc0Alpha().get() - b.getCombineSrc0Alpha().get();
            }
            if (a.getCombineSrc1Alpha() != b.getCombineSrc1Alpha()) {
                return a.getCombineSrc1Alpha().get() - b.getCombineSrc1Alpha().get();
            }
            if (a.getCombineSrc2Alpha() != b.getCombineSrc2Alpha()) {
                return a.getCombineSrc2Alpha().get() - b.getCombineSrc2Alpha().get();
            }
            if (a.getCombineOperand0RGB() != b.getCombineOperand0RGB()) {
                return a.getCombineOperand0RGB().get() - b.getCombineOperand0RGB().get();
            }
            if (a.getCombineOperand1RGB() != b.getCombineOperand1RGB()) {
                return a.getCombineSrc1RGB().get() - b.getCombineSrc1RGB().get();
            }
            if (a.getCombineOperand2RGB() != b.getCombineOperand2RGB()) {
                return a.getCombineSrc2RGB().get() - b.getCombineSrc2RGB().get();
            }
            if (a.getCombineOperand0Alpha() != b.getCombineOperand0Alpha()) {
                return a.getCombineOperand0Alpha().get() - b.getCombineOperand0Alpha().get();
            }
            if (a.getCombineOperand1Alpha() != b.getCombineOperand1Alpha()) {
                return a.getCombineSrc1Alpha().get() - b.getCombineSrc1Alpha().get();
            }
            if (a.getCombineOperand2Alpha() != b.getCombineOperand2Alpha()) {
                return a.getCombineSrc2Alpha().get() - b.getCombineSrc2Alpha().get();
            }
            return 0;
        }
    }
}
