package com.golemgame.structural;

import java.lang.ref.WeakReference;
import com.golemgame.model.Model;
import com.golemgame.model.Model.ModelTypeException;
import com.golemgame.model.effect.ModelEffect;
import com.golemgame.model.effect.ModelEffectImpl;
import com.golemgame.model.effect.TextureLayerEffect;
import com.golemgame.model.effect.TintableColorEffect;
import com.golemgame.model.texture.TextureServer;
import com.golemgame.model.texture.TextureTypeKey;
import com.golemgame.model.texture.TextureWrapper;
import com.golemgame.model.texture.TextureTypeKey.TextureShape;
import com.golemgame.model.texture.TextureWrapper.ApplyMode;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.SustainedView;
import com.golemgame.mvc.golems.AppearanceInterpreter;
import com.golemgame.mvc.golems.AppearanceInterpreter.ImageType;
import com.jme.renderer.ColorRGBA;

/**
 * This object is intended to hold the serializable, static appearance of a structure.
 * @author Sam
 *
 */
public class StructuralAppearanceEffect extends ModelEffectImpl implements SustainedView {

    private static final long serialVersionUID = 1L;

    private static final ModelEffectType[] types = new ModelEffectType[] { ModelEffectType.TEXTURELAYER };

    private TintableColorEffect colorEffect;

    private TextureLayerEffect textureLayerEffect;

    private TextureLayerEffect textEffect;

    private TextureTypeKey.TextureShape preferedShape = TextureTypeKey.TextureShape.Plane;

    public TextureTypeKey.TextureShape getPreferedShape() {
        return preferedShape;
    }

    public void setPreferedShape(TextureTypeKey.TextureShape preferedShape) {
        this.preferedShape = preferedShape;
    }

    private AppearanceInterpreter interpreter;

    public AppearanceInterpreter getInterpreter() {
        return interpreter;
    }

    public StructuralAppearanceEffect(PropertyStore store) {
        super();
        this.interpreter = new AppearanceInterpreter(store);
        store.setSustainedView(this);
    }

    public ModelEffectType[] getEffectTypes() {
        return types;
    }

    @Override
    public void attachModel(Model model) throws ModelTypeException {
        super.attachModel(model);
        refresh();
    }

    @Override
    public void removeModel(Model model) {
        super.removeModel(model);
        colorEffect.removeModel(model);
        textureLayerEffect.removeModel(model);
        model.refreshLockedData();
        refresh();
    }

    public ColorRGBA getBaseColor() {
        return colorEffect.getColor();
    }

    public void setBaseColor(ColorRGBA baseColor) {
        this.interpreter.setBaseColor(baseColor);
        refresh();
    }

    public ColorRGBA getHighlight() {
        return this.interpreter.getHighlightColor();
    }

    public void setHighlight(ColorRGBA highlight) {
        this.interpreter.getHighlightColor().set(highlight);
        refresh();
    }

    public TextureWrapper getHighlightTexture() {
        return this.textureLayerEffect.getTextureWrapper();
    }

    public void setHighlightTexture(TextureWrapper highlightTexture) {
        this.interpreter.setTextureImage(highlightTexture.getTextureTypeKey(0).getImage());
        refresh();
    }

    public void set(ModelEffect setFrom) throws ModelEffectTypeException {
        super.set(setFrom);
        if (!(setFrom instanceof StructuralAppearanceEffect)) throw new ModelEffectTypeException("Wrong effect type");
        StructuralAppearanceEffect effect = (StructuralAppearanceEffect) setFrom;
        this.colorEffect.set(effect.colorEffect);
        this.getHighlight().set(effect.getHighlight());
        this.textureLayerEffect.set(effect.textureLayerEffect);
        this.setBaseColor(effect.getBaseColor());
        this.setHighlight(effect.getHighlight());
    }

    @Override
    protected void refreshEffect(Model model) {
    }

    public void refreshEffect() {
        if (colorEffect != null) colorEffect.refreshEffect();
        if (textEffect != null) textEffect.refreshEffect();
        if (textureLayerEffect != null) textureLayerEffect.refreshEffect();
    }

    public ModelEffect copy() {
        StructuralAppearanceEffect copy = new StructuralAppearanceEffect(interpreter.getStore().deepCopy());
        copy.refresh();
        return copy;
    }

    public void refresh() {
        boolean changed = false;
        if (this.colorEffect == null) {
            this.colorEffect = new TintableColorEffect();
            changed = true;
        }
        if (!interpreter.getBaseColor().equals(this.colorEffect.getColor())) {
            this.colorEffect.setColor(interpreter.getBaseColor());
            changed = true;
        }
        ImageType image = interpreter.getImage();
        if (this.textureLayerEffect == null) {
            TextureTypeKey requestedTexture = new TextureTypeKey(image, 512, 512, TextureWrapper.TextureFormat.RGBA, true, getPreferedShape());
            TextureWrapper texture = TextureServer.getInstance().getTexture(requestedTexture);
            texture.setApplyMode(ApplyMode.BLEND);
            this.textureLayerEffect = new TextureLayerEffect(texture, 4);
            changed = true;
        } else if (this.textureLayerEffect.getTextureWrapper() == null || this.textureLayerEffect.getTextureWrapper().getTextureTypeKey(0).getImage() != image) {
            TextureTypeKey requestedTexture = new TextureTypeKey(image, 512, 512, TextureWrapper.TextureFormat.RGBA, true, getPreferedShape());
            TextureWrapper texture = TextureServer.getInstance().getTexture(requestedTexture);
            texture.setApplyMode(ApplyMode.BLEND);
            textureLayerEffect.setTextureWrapper(texture);
            changed = true;
        }
        String text = interpreter.getText();
        if (this.textEffect == null) {
            TextureTypeKey requestedTexture = new TextureTypeKey(ImageType.TEXT, 256, 256, TextureWrapper.TextureFormat.RGBA, false, TextureShape.Plane);
            requestedTexture.setText(text);
            TextureWrapper texture;
            texture = TextureServer.getInstance().getTexture(requestedTexture);
            texture.setApplyMode(ApplyMode.BLEND);
            this.textEffect = new TextureLayerEffect(texture, 4);
            textEffect.setPrecedence(4);
            changed = true;
        } else if (this.textEffect.getTextureWrapper() == null || this.textEffect.getTextureWrapper().getTextureTypeKey(0).getText() == null || !this.textEffect.getTextureWrapper().getTextureTypeKey(0).getText().equals(text)) {
            TextureTypeKey requestedTexture = new TextureTypeKey(ImageType.TEXT, 256, 256, TextureWrapper.TextureFormat.RGBA, false, TextureShape.Plane);
            requestedTexture.setText(text);
            TextureWrapper texture = TextureServer.getInstance().getTexture(requestedTexture);
            texture.setApplyMode(ApplyMode.BLEND);
            textEffect.setTextureWrapper(texture);
            changed = true;
        }
        float r = getHighlight().r;
        float g = getHighlight().g;
        float b = getHighlight().b;
        float a = getHighlight().a;
        ColorRGBA baseColor = this.getBaseColor();
        r = baseColor.r * (1 - a) + r * a;
        g = baseColor.g * (1 - a) + g * a;
        b = baseColor.b * (1 - a) + b * a;
        ColorRGBA tint = new ColorRGBA(r, g, b, 1);
        if (!this.textureLayerEffect.getTextureWrapper().getTint().equals(tint)) {
            this.textureLayerEffect.getTextureWrapper().setTint(new ColorRGBA(r, g, b, 1));
            changed = true;
        }
        if (this.textEffect != null) {
            ColorRGBA textColor = interpreter.getTextColor();
            if (!this.textEffect.getTextureWrapper().getTint().equals(textColor)) {
                this.textEffect.getTextureWrapper().setTint(textColor);
                changed = true;
            }
        }
        for (WeakReference<Model> m : super.getModels()) {
            Model model = m.get();
            if (model != null) {
                if (textEffect != null) textEffect.attachModel(model);
                if (textureLayerEffect != null) textureLayerEffect.attachModel(model);
                if (colorEffect != null) colorEffect.attachModel(model);
            }
        }
    }

    public PropertyStore getStore() {
        return interpreter.getStore();
    }

    public void remove() {
    }

    public String getText() {
        return interpreter.getText();
    }
}
