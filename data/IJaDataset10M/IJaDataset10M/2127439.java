package math.render;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class TexturedSpace<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final HyperPlane<T> hyperPlane;

    private final List<Texture<T>> textures;

    public TexturedSpace(HyperPlane<T> hyperPlane, List<Texture<T>> textures) {
        super();
        this.hyperPlane = hyperPlane;
        this.textures = new ArrayList<Texture<T>>(textures);
    }

    public int getTextureCount() {
        return textures.size();
    }

    public Texture<T> getTexture(int i) {
        return textures.get(i);
    }

    public HyperPlane<T> getHyperPlane() {
        return hyperPlane;
    }
}
