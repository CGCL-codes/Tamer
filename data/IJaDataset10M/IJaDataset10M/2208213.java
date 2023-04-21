package il.co.gadiworks.games.framework.gl;

public class TextureRegion {

    public final float U1, V1;

    public final float U2, V2;

    public final Texture TEXTURE;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        this.U1 = x / texture.width;
        this.V1 = y / texture.height;
        this.U2 = this.U1 + width / texture.width;
        this.V2 = this.V1 + height / texture.height;
        this.TEXTURE = texture;
    }
}
