package week6c;

/**
 *
 * @author Shreeti Tuladhar
 */
class DigitalCamera extends Camera implements CanFlash {

    int megaPixel;

    public DigitalCamera(String brand, int fLength, boolean shutterPressed, int megaPixel) {
        super(brand, fLength, shutterPressed);
        this.megaPixel = megaPixel;
    }

    public int getMegaPixel() {
        return megaPixel;
    }

    public void setMegaPixel(int megaPixel) {
        this.megaPixel = megaPixel;
    }

    public int shot(int f, int fs) {
        System.out.println("Digital Camera special shooting\n");
        return 0;
    }

    public int focus(int d) {
        System.out.println("Digital Camera focusing object at " + d + " km");
        System.out.println("Focal length returned by focusing is " + (int) (d / (0.2d + 1)));
        return (0);
    }

    public void flash() {
        System.out.println("Digital Camera flash");
    }
}
