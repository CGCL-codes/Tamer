package sk.bielyvlk.vlkgps;

import javax.microedition.lcdui.*;
import sk.bielyvlk.vlkui.*;
import sk.bielyvlk.gps.*;

public class BottomSatelites extends MidletUser {

    private static Font fontPlain = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    private static int height = 32;

    public BottomSatelites(VlkGps midlet) {
        super(midlet);
    }

    public void paint(Graphics g) {
        if (midlet.getView().getDayMode()) g.setColor(223, 223, 223); else g.setColor(15, 15, 15);
        g.fillRect(0, VlkUiCanvas.HEIGHT - height, VlkUiCanvas.WIDTH, height);
        g.setFont(fontPlain);
        for (int i = 0; i < midlet.gpsDevice.getSatView(); i++) {
            if (midlet.gpsDevice.getSat(i).getState() == GpsSat.USED) {
                g.setColor(0, 191, 0);
            } else {
                g.setColor(191, 0, 0);
            }
            int h = midlet.gpsDevice.getSat(i).getSnr() * (height - 1) / 100;
            g.fillRect(i * VlkUiCanvas.WIDTH / 12 + 1, VlkUiCanvas.HEIGHT - 1 - h, VlkUiCanvas.WIDTH / 12 - 2, h);
            if (midlet.getView().getDayMode()) g.setColor(0, 0, 0); else g.setColor(255, 255, 255);
            g.drawString("" + midlet.gpsDevice.getSat(i).getPrn(), VlkUiCanvas.WIDTH / 24 + i * VlkUiCanvas.WIDTH / 12, VlkUiCanvas.HEIGHT - 1, Graphics.HCENTER | Graphics.BOTTOM);
        }
    }

    public int getHeight() {
        return height;
    }
}
