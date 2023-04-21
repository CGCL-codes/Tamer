package net.sf.ipm.sluchacze;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class PaintGrafikaListener implements Listener {

    private Display display;

    private String plik;

    public PaintGrafikaListener(Display display, String plik) {
        this.display = display;
        this.plik = plik;
    }

    @Override
    public void handleEvent(Event event) {
        if (plik != null) {
            Image image = new Image(display, plik);
            event.gc.drawImage(image, 0, 0);
            image.dispose();
        }
    }

    /**
	 * @param plik the plik to set
	 */
    public void setPlik(String plik) {
        this.plik = plik;
    }
}
