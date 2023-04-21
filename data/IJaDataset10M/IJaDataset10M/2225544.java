package net.sf.xpontus.modules.gui.components.preferences;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;
import java.awt.BorderLayout;
import javax.swing.Icon;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class EditorPanelDialog extends AbstractDialogPage {

    /**
	 * 
	 */
    private static final long serialVersionUID = 320490676255742109L;

    public EditorPanelDialog(String name, Icon icon) {
        super(name, icon);
    }

    public void lazyInitialize() {
        setLayout(new BorderLayout());
        BannerPanel bannerPanel = new BannerPanel(getName());
        bannerPanel.setBackground(getBackground().brighter());
        add(bannerPanel, BorderLayout.NORTH);
        add(new EditorPanel(), BorderLayout.CENTER);
    }
}
