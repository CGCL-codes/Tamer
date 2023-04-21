package your.rpg.namespace;

public class LevelObject {

    private String name;

    private int image;

    private int xpos;

    private int ypos;

    public LevelObject(String naam, int plaatje, int xas, int yas) {
        name = naam;
        image = plaatje;
        xpos = xas;
        ypos = yas;
    }

    public int getImage() {
        return image;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public String getName() {
        return name;
    }

    public void setPos(int x, int y) {
        xpos = x;
        ypos = y;
    }

    public void setImage(int image1) {
        image = image1;
    }
}
