package marten.age.graphics.texture.textures;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.texture.TextureProvider;

public class TestTexture extends TextureProvider {

    @Override
    public void generateTexture() {
        double height = this.getHeight();
        double width = this.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = 1.0;
                if (x % 10 == 0 || y % 10 == 0) {
                    value = 0x00;
                }
                Color p = new Color(value, value, value);
                this.setPixel(p, x, y);
            }
        }
    }
}
