package ces.platform.infoplat.utils.jar.view;

import java.util.zip.ZipEntry;
import javax.swing.JPanel;

/**
 * Basic jar/zip file display interface.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public interface JarUI {

    /**
     * Update panel look & feel.
     */
    public void updateLnF();

    /**
     * Get the panel to display.
     */
    public JPanel getPanel();

    /**
     * Get all the selected entries.
     */
    public ZipEntry[] getSelectedEntries();

    /**
     * Is this panel active?
     */
    public boolean isActive();

    /**
     * Set the panel active status.
     */
    public void setActive(boolean b);
}
