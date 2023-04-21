package jhomenet.ui.panel;

import java.awt.Dimension;
import javax.swing.JPanel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import jhomenet.ui.window.DefaultWindowEvent;

/**
 * An abstract panel model class.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public abstract class AbstractPanel implements CustomPanel {

    /**
	 * Reference to the panel group manager.
	 */
    protected PanelGroupManager panelGroupManager;

    /**
	 * The actual panel implementation. 
	 */
    private BackgroundPanel backgroundPanel;

    /**
	 * Constructor.
	 */
    public AbstractPanel() {
        super();
    }

    /**
	 * @see jhomenet.ui.panel.CustomPanel#getIdentifier()
	 */
    public final String getIdentifier() {
        return getClass().getName();
    }

    /**
	 * @see jhomenet.ui.panel.CustomPanel#getPreferredDimension()
	 */
    @Override
    public Dimension getPreferredDimension() {
        return new Dimension(300, 400);
    }

    /**
	 * Build the panel.
	 */
    public final BackgroundPanel buildPanel() {
        JPanel header = getHeaderPanel();
        if (header == null) {
            this.backgroundPanel = buildPanelImpl();
        } else {
            BackgroundPanel panel = new BackgroundPanel();
            FormLayout panelLayout = new FormLayout("fill:default:grow", "fill:pref, fill:default:grow");
            CellConstraints cc = new CellConstraints();
            PanelBuilder builder = new PanelBuilder(panelLayout, panel);
            builder.add(header, cc.xy(1, 1));
            builder.add(buildPanelImpl(), cc.xy(1, 2));
            this.backgroundPanel = (BackgroundPanel) builder.getPanel();
        }
        this.backgroundPanel.revalidate();
        return this.backgroundPanel;
    }

    /**
	 * Build the actual main panel.
	 * <p>
	 * Note: It is up to the implementing method to add any necessary padding
	 * around the panel components as the AbstractPanel will NOT provide any
	 * additional padding. 
	 * 
	 * @return
	 */
    protected abstract BackgroundPanel buildPanelImpl();

    /**
	 * Get the header panel. This method is intended to be overridden by
	 * subclasses to provide the necessary functionality.
	 * 
	 * @return
	 */
    protected JPanel getHeaderPanel() {
        return null;
    }

    /**
	 * @see jhomenet.ui.panel.CustomPanel#repaint()
	 */
    @Override
    public void repaint() {
    }

    /**
	 * 
	 * @param visible
	 */
    public void setVisible(boolean visible) {
        backgroundPanel.setVisible(visible);
    }

    /**
	 * @see jhomenet.ui.panel.CustomPanel#setPanelGroupManager(jhomenet.ui.panel.PanelGroupManager)
	 */
    public void setPanelGroupManager(PanelGroupManager panelGroupManager) {
        this.panelGroupManager = panelGroupManager;
    }

    /**
	 * 
	 * @return
	 */
    public PanelGroupManager getPanelGroupManager() {
        return this.panelGroupManager;
    }

    /**
	 * Classes that extend this class should override this method to receive notification
	 * of a parent window closing.
	 * 
	 * @see jhomenet.ui.panel.CustomPanel#parentWindowIsClosing(jhomenet.ui.window.DefaultWindowEvent)
	 */
    public void parentWindowIsClosing(DefaultWindowEvent event) {
    }
}
