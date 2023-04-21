package ch.intertec.storybook.main;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.main.sidebar.Sidebar;
import ch.intertec.storybook.model.InternalPeer;
import ch.intertec.storybook.view.IRefreshable;
import ch.intertec.storybook.view.content.AbstractContentPanel;
import ch.intertec.storybook.view.content.BlankPanel;
import ch.intertec.storybook.view.content.ViewControlPanel;
import ch.intertec.storybook.view.content.book.BookContentPanel;
import ch.intertec.storybook.view.content.chrono.ChronoContentPanel;
import ch.intertec.storybook.view.content.manage.ManageContentPanel;

@SuppressWarnings("serial")
public class MainSplitPane extends JSplitPane implements IRefreshable {

    public enum ContentPanelType {

        BLANK, CHRONO, BOOK, MANAGE;

        private int scale = 1;

        public void setScale(int scale) {
            this.scale = scale;
        }

        public int getScale() {
            return scale;
        }

        public int getCalculatedScale() {
            switch(this) {
                case BOOK:
                    return scale * InternalPeer.getScaleFactorBook() + 150;
                case CHRONO:
                    return scale * InternalPeer.getScaleFactorChrono() + 100;
                case MANAGE:
                    return scale * InternalPeer.getScaleFactorManage();
            }
            return scale;
        }

        public void setDefaultScales() {
            CHRONO.scale = 8;
            BOOK.scale = 7;
            MANAGE.scale = 9;
        }
    }

    private AbstractContentPanel contentPanel;

    private JScrollPane scroller;

    private ViewControlPanel viewControlPanel;

    private ContentPanelType contentPanelType = ContentPanelType.BLANK;

    private boolean dividerShown = true;

    private int saveDividerLocation = 0;

    public MainSplitPane() {
        super(JSplitPane.HORIZONTAL_SPLIT);
        contentPanel = null;
        scroller = new JScrollPane();
        scroller.setAutoscrolls(true);
        scroller.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        viewControlPanel = new ViewControlPanel();
        setSmartDividerLocation();
        setResizeWeight(0.75);
        initGUI();
    }

    public void setSmartDividerLocation() {
        int width = MainFrame.getInstance().getWidth() - Sidebar.PREFERRED_WIDTH;
        setDividerLocation(width);
    }

    private void initGUI() {
        setContentPanel(contentPanelType);
        JPanel panel = new JPanel(new MigLayout("flowy,ins 0"));
        panel.add(viewControlPanel, "growx");
        panel.add(scroller, "grow");
        setLeftComponent(panel);
        Sidebar sidebar = Sidebar.getInstance();
        setRightComponent(sidebar);
    }

    public JScrollPane getScroller() {
        return scroller;
    }

    private void setContentPanel(ContentPanelType type) {
        contentPanelType = type;
        switch(contentPanelType) {
            case CHRONO:
                contentPanel = new ChronoContentPanel();
                break;
            case BOOK:
                contentPanel = new BookContentPanel();
                break;
            case MANAGE:
                contentPanel = new ManageContentPanel();
                break;
            default:
                contentPanel = new BlankPanel();
        }
        scroller.setViewportView(contentPanel);
    }

    public void resetViewPosition() {
        setViewPosition(new Point());
    }

    public void setViewPosition(Point point) {
        if (scroller == null) {
            return;
        }
        scroller.getViewport().setViewPosition(point);
    }

    public Point getViewPosition() {
        if (scroller == null) {
            return new Point();
        }
        return scroller.getViewport().getViewPosition();
    }

    @Override
    public void refresh() {
        initGUI();
    }

    public AbstractContentPanel getContentPanel() {
        return contentPanel;
    }

    public void setBlankPanel() {
        setContentPanel(ContentPanelType.BLANK);
    }

    public void setChronoPanel() {
        setContentPanel(ContentPanelType.CHRONO);
    }

    public void setBookPanel() {
        setContentPanel(ContentPanelType.BOOK);
    }

    public void setManagePanel() {
        setContentPanel(ContentPanelType.MANAGE);
    }

    public boolean isChronoPanelActive() {
        return contentPanelType == ContentPanelType.CHRONO ? true : false;
    }

    public boolean isBookPanelActive() {
        return contentPanelType == ContentPanelType.BOOK ? true : false;
    }

    public boolean isManagePanelActive() {
        return contentPanelType == ContentPanelType.MANAGE ? true : false;
    }

    public ContentPanelType getContentPanelType() {
        return contentPanelType;
    }

    public boolean isDividerShown() {
        return dividerShown;
    }

    public void toggleDivider() {
        dividerShown = !dividerShown;
        if (dividerShown) {
            showDivider();
        } else {
            hideDivider();
        }
    }

    public void hideDivider() {
        dividerShown = false;
        saveDividerLocation = getDividerLocation();
        setDividerLocation(1.0);
    }

    public void showDivider() {
        dividerShown = true;
        setDividerLocation(saveDividerLocation);
    }

    public ViewControlPanel getViewControlPanel() {
        return viewControlPanel;
    }
}
