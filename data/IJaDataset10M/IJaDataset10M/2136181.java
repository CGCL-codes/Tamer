package computergraphicsproject.Engines.InputHandling;

/**
 *
 * @author hussein
 */
public class MouseEvent {

    private int objectID;

    private int buttonID;

    private int wheel;

    private int Dx;

    private int Dy;

    private boolean stillPressed;

    private boolean button0;

    private boolean button1;

    private boolean button2;

    public MouseEvent(int objectID, int buttonID, int wheel, int Dx, int Dy, boolean stillPressed, boolean button0, boolean button1, boolean button2) {
        this.objectID = objectID;
        this.buttonID = buttonID;
        this.wheel = wheel;
        this.Dx = Dx;
        this.Dy = Dy;
        this.stillPressed = stillPressed;
        this.button0 = button0;
        this.button1 = button1;
        this.button2 = button2;
    }

    /**
     * @return the objectID
     */
    public int getObjectID() {
        return objectID;
    }

    /**
     * @param objectID the objectID to set
     */
    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    /**
     * @return the buttonID
     */
    public int getButtonID() {
        return buttonID;
    }

    /**
     * @param buttonID the buttonID to set
     */
    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }

    /**
     * @return the wheel
     */
    public int getWheel() {
        return wheel;
    }

    /**
     * @param wheel the wheel to set
     */
    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    /**
     * @return the Dx
     */
    public int getDx() {
        return Dx;
    }

    /**
     * @param Dx the Dx to set
     */
    public void setDx(int Dx) {
        this.Dx = Dx;
    }

    /**
     * @return the Dy
     */
    public int getDy() {
        return Dy;
    }

    /**
     * @param Dy the Dy to set
     */
    public void setDy(int Dy) {
        this.Dy = Dy;
    }

    /**
     * @return the stillPressed
     */
    public boolean isStillPressed() {
        return stillPressed;
    }

    /**
     * @param stillPressed the stillPressed to set
     */
    public void setStillPressed(boolean stillPressed) {
        this.stillPressed = stillPressed;
    }

    /**
     * @return the button0
     */
    public boolean isButton0() {
        return button0;
    }

    /**
     * @param button0 the button0 to set
     */
    public void setButton0(boolean button0) {
        this.button0 = button0;
    }

    /**
     * @return the button1
     */
    public boolean isButton1() {
        return button1;
    }

    /**
     * @param button1 the button1 to set
     */
    public void setButton1(boolean button1) {
        this.button1 = button1;
    }

    /**
     * @return the button2
     */
    public boolean isButton2() {
        return button2;
    }

    /**
     * @param button2 the button2 to set
     */
    public void setButton2(boolean button2) {
        this.button2 = button2;
    }
}
