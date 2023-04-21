package camerac;

/**
 *
 * @author Shreeti Tuladhar
 */
public class Camera {

    private String brand;

    private int fLength;

    private boolean shutterPressed;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getFLength() {
        return fLength;
    }

    public void setFLength(int fLength) {
        this.fLength = fLength;
    }

    public boolean isShutterPressed() {
        return shutterPressed;
    }

    public void setShutterPressed(boolean shutterPressed) {
        this.shutterPressed = shutterPressed;
    }

    public static void main(String[] args) {
        Camera[] cameraArray = { new Camera(), new Camera(), new Camera() };
        int j = 0;
        for (int i = 0; i < cameraArray.length; i++) {
            cameraArray[i].setBrand(args[j++]);
            cameraArray[i].setFLength(Integer.parseInt(args[j++]));
            cameraArray[i].setShutterPressed(Boolean.valueOf(args[j++]).booleanValue());
        }
        for (int i = 0; i < cameraArray.length; ++i) {
            System.out.println("Camera " + i);
            System.out.println("Brand is: " + cameraArray[i].getBrand() + " Focal Length is: " + cameraArray[i].getFLength() + " Shutter Pressed is: " + cameraArray[i].isShutterPressed());
        }
    }
}
