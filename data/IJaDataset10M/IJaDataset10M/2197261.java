package octlight.material.texture;

import octlight.image.Image;

public class Texture {

    private Image image;

    public Texture(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
