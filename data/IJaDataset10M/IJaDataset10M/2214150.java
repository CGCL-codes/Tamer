package dmi.unict.it;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.Vector;

public class N95_Mouse {

    private static String CODE = "\n\nimport appuifw, axyz, e32\nfrom graphics import *\n\nappuifw.app.screen = \"full\"\nappuifw.app.body = canvas = appuifw.Canvas()\n\ncanvas.clear(0)\n\ndef click():\n    print click\n\nappuifw.app.exit_key_handler=click\ndef printout(x, y, z):\n    try:\n        print \"x%iy%i\" % (x, y)\n    except:\n        print \"x0y0\"\n\naxyz.connect(printout)\ne32.ao_sleep(600)\naxyz.disconnect()\n\n";

    public N95_Mouse() {
        SerialConnection sc = new SerialConnection();
        boolean ciclo;
        try {
            sc.connect();
            sc.writeString(CODE);
            ciclo = true;
        } catch (Exception error) {
            error.printStackTrace();
            ciclo = false;
        }
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int mouseX = 0;
        int mouseY = 0;
        Vector<Integer> fX = new Vector<Integer>();
        Vector<Integer> fY = new Vector<Integer>();
        int indfX = 18;
        int indfY = 18;
        int indX = 0;
        int indY = 0;
        String cellularOutput = "";
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException error) {
        }
        while (ciclo) {
            try {
                cellularOutput = sc.readString();
            } catch (IOException error) {
                cellularOutput = "";
            }
            int posX = cellularOutput.indexOf("x");
            int posY = cellularOutput.indexOf("y");
            int click = cellularOutput.indexOf("click");
            try {
                posX = Integer.valueOf(cellularOutput.substring(posX + 1, posY));
                posY = Integer.valueOf(cellularOutput.substring(posY + 1, cellularOutput.length()));
            } catch (Exception error) {
                posX = 0;
                posY = 0;
            }
            if (posX >= 0 && posY >= 0) {
                posX += (int) ((float) (posY / 4) * Math.abs((float) posX / 30F));
                posY += (int) ((float) (posX / 4) * Math.abs((float) posY / 30F));
            }
            if (posX >= 0 && posY < 0) {
                posX -= (int) ((float) (posY / 3) * Math.abs((float) posX / 30F));
                posY -= (int) ((float) (posX / 3) * Math.abs((float) posY / 30F));
            }
            if (posX < 0 && posY < 0) {
                posX += (int) ((float) (posY / 3) * Math.abs((float) posX / 30F));
                posY += (int) ((float) (posX / 3) * Math.abs((float) posY / 30F));
            }
            if (posX < 0 && posY >= 0) {
                posX -= (int) ((float) (posY / 8) * Math.abs((float) posX / 30F));
                posY -= (int) ((float) (posX / 8) * Math.abs((float) posY / 30F));
            }
            int X = screenWidth / 2 + posX * 16;
            int Y = screenHeight / 2 - posY * 16;
            for (Object i : fX) mouseX += Integer.valueOf(i.toString());
            mouseX /= indfX;
            try {
                fX.setElementAt(X, indX);
            } catch (Exception error) {
                fX.addElement(X);
            }
            if (indX < indfX - 1) indX += 1; else indX = 0;
            for (Object i : fY) mouseY += Integer.valueOf(i.toString());
            mouseY /= indfY;
            try {
                fY.setElementAt(Y, indY);
            } catch (Exception error) {
                fY.addElement(Y);
            }
            if (indY < indfY - 1) indY += 1; else indY = 0;
            if (mouseX > screenWidth) mouseX = screenWidth - 1;
            if (mouseX <= 0) mouseX = 0;
            if (mouseY > screenHeight) mouseY = screenHeight - 1;
            if (mouseY <= 0) mouseY = 0;
            if (r != null) r.mouseMove(mouseX, mouseY);
            if (click > 0) {
                click = 0;
                r.mousePress(InputEvent.BUTTON1_MASK);
                r.mouseRelease(InputEvent.BUTTON1_MASK);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException error) {
                }
                r.mousePress(InputEvent.BUTTON1_MASK);
                r.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }
    }

    public static void main(String[] args) {
        new N95_Mouse();
    }
}
