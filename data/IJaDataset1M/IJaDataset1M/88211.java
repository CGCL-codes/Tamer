package ponkOut.graphics;

import ponkOut.graphics.resources.Texture;
import ponkOut.graphics.resources.TextureManager;

public class TexturedMaterial extends Material {

    private Texture texture;

    public TexturedMaterial(String texture, float amR, float amG, float amB, float diR, float diG, float diB, float spR, float spG, float spB, float alpha, float shininess) {
        super(amR, amG, amB, diR, diG, diB, spR, spG, spB, alpha, shininess);
        this.texture = TextureManager.getInstance().getTexture(texture);
    }

    public TexturedMaterial(String texture, String normalMap, float amR, float amG, float amB, float diR, float diG, float diB, float spR, float spG, float spB, float alpha, float shininess) {
        super(amR, amG, amB, diR, diG, diB, spR, spG, spB, alpha, shininess);
        this.texture = TextureManager.getInstance().getNormalMappedTexture(texture, normalMap);
    }

    @Override
    public void use() {
        super.use();
        texture.bind();
    }
}
