package windowUnits;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import windows.MainAppW;
import com.swtdesigner.SwingResourceManager;

public class HistoryToolBar extends JToolBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final JLabel changesLabel = new JLabel();

    public HistoryToolBar() {
    }

    public void setupToolBar() {
        this.setOrientation(SwingConstants.VERTICAL);
        changesLabel.setIcon(SwingResourceManager.getIcon(MainAppW.class, "/icons/folder_48.png"));
        changesLabel.setAutoscrolls(true);
        changesLabel.setText("Changes queue");
        this.add(changesLabel);
    }

    public void setTitle(String title) {
        changesLabel.setText(title);
    }
}
