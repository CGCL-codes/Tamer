package uk.ac.essex.common.gui.util;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.essex.common.gui.frame.MDIDesktopPane;
import uk.ac.essex.common.io.ResourceLoader;
import uk.ac.essex.common.util.Log4jInit;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * <br>
 * Created Date: 17-Jan-2004<br>
 * <p/>
 * You should have received a copy of Lesser GNU public license with this code.
 * If not please visit <a href="http://www.gnu.org/copyleft/lesser.html">this site </a>
 *
 * @author Laurence Smith
 * @version $Revision: 1.2 $
 */
public class ButtonPanelContainerFixed extends JPanel implements PropertyChangeListener {

    /** The logger */
    private static final transient Log logger = LogFactory.getLog(ButtonPanelContainerFixed.class);

    public static final int HIDDEN = 0;

    public static final int DISPLAYED = 1;

    JSplitPane rightPane, leftPane, topPane, bottomPane;

    int mode = HIDDEN;

    JComponent content;

    JSplitPane splitPane;

    ButtonPanel currentRightPanel, currentLeftPanel, currentTopPanel, currentBottomPanel;

    private SplitPaneUI rightPaneUI;

    private SplitPaneUI leftPaneUI;

    private SplitPaneUI topPaneUI;

    private SplitPaneUI bottomPaneUI;

    private PanelBuilder builder;

    private CellConstraints cellConstraints = new CellConstraints();

    private static final FormLayout LAYOUT = new FormLayout("left:min,  fill:default:grow, left:min", "pref, fill:default:grow, pref");

    ButtonPanelList leftButtonPanelList = new ButtonPanelList(ButtonPanelList.VERTICAL);

    ButtonPanelList rightButtonPanelList = new ButtonPanelList(ButtonPanelList.VERTICAL);

    ButtonPanelList topButtonPanelList = new ButtonPanelList(ButtonPanelList.HORIZONTAL);

    ButtonPanelList bottomButtonPanelList = new ButtonPanelList(ButtonPanelList.HORIZONTAL);

    public ButtonPanelContainerFixed() {
        init();
    }

    public void addButtonPanel(ButtonPanel buttonPanel) throws Exception {
        int orientation = buttonPanel.getOrientation();
        switch(orientation) {
            case ButtonPanel.LEFT:
                leftButtonPanelList.add(buttonPanel);
                break;
            case ButtonPanel.RIGHT:
                rightButtonPanelList.add(buttonPanel);
                break;
            case ButtonPanel.TOP:
                topButtonPanelList.add(buttonPanel);
                break;
            case ButtonPanel.BOTTOM:
                bottomButtonPanelList.add(buttonPanel);
                break;
            default:
                throw new Exception("ERROR: Invalid orientation");
        }
        updatePanels();
    }

    public void init() {
        rightPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        bottomPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightPaneUI = new SplitPaneUI();
        rightPane.setUI(rightPaneUI);
        leftPaneUI = new SplitPaneUI();
        leftPane.setUI(leftPaneUI);
        topPaneUI = new SplitPaneUI();
        topPane.setUI(topPaneUI);
        bottomPaneUI = new SplitPaneUI();
        bottomPane.setUI(bottomPaneUI);
        setLayout(new BorderLayout());
        rightButtonPanelList.addPropertyChangeListener(this);
        leftButtonPanelList.addPropertyChangeListener(this);
        topButtonPanelList.addPropertyChangeListener(this);
        bottomButtonPanelList.addPropertyChangeListener(this);
        buildMainPanel();
    }

    /**
     * Builds the panel. Initializes and configures components first,
     * then creates a FormLayout, configures the layout, creates a builder,
     * sets a border, and finally adds the components.
     *
     */
    private void buildMainPanel() {
        builder = new PanelBuilder(LAYOUT);
        builder.setDefaultDialogBorder();
        updatePanels();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Log4jInit.initLog4j();
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Quick Start");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        final JMenuBar menubar = new JMenuBar();
        menubar.add(new JMenu("File"));
        frame.setJMenuBar(menubar);
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        File f = ResourceLoader.getResourceAsFile("open24.gif");
        Icon icon = new ImageIcon(f.getAbsolutePath());
        JButton toolBarBut = new JButton(icon);
        toolBar.add(toolBarBut);
        contentPane.add(toolBar, BorderLayout.NORTH);
        MDIDesktopPane desktopPane = new MDIDesktopPane();
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        desktopPane.setBackground(new Color(170, 224, 255));
        JScrollPane desktopScrollPane = new JScrollPane();
        desktopScrollPane.setViewportView(desktopPane);
        desktopScrollPane.setPreferredSize(new Dimension(500, 500));
        JInternalFrame iFrame = new JInternalFrame("Test");
        desktopPane.add(iFrame);
        ButtonPanelContainerFixed buttonPanelContainer = new ButtonPanelContainerFixed();
        JPanel view1 = new JPanel();
        view1.add(new JLabel("Left Panel"));
        view1.setPreferredSize(new Dimension(100, 100));
        ButtonPanel hideablePanel = new ButtonPanelImpl("testL", "TestL", view1, ButtonPanel.LEFT);
        JPanel view2 = new JPanel();
        view2.add(new JLabel("Left Panel2"));
        ButtonPanel hideablePanel2 = new ButtonPanelImpl("testL2", "TestL2", view2, ButtonPanel.LEFT);
        JPanel view3 = new JPanel();
        view3.add(new JLabel("Top Panel1"));
        ButtonPanel hideablePanel3 = new ButtonPanelImpl("testT1", "TestT1", view3, ButtonPanel.TOP);
        try {
            buttonPanelContainer.addButtonPanel(hideablePanel);
            buttonPanelContainer.addButtonPanel(hideablePanel2);
            buttonPanelContainer.addButtonPanel(hideablePanel3);
        } catch (Exception e) {
            logger.error("ERROR: ", e);
        }
        buttonPanelContainer.setContent(desktopScrollPane);
        contentPane.add(buttonPanelContainer, BorderLayout.CENTER);
        frame.pack();
        frame.show();
    }

    private void setContent(JComponent jComponent) {
        content = jComponent;
        builder.add(content, cellConstraints.xy(2, 2));
        updatePanels();
    }

    private boolean hasTopButtons() {
        return topButtonPanelList.size() > 0;
    }

    private boolean hasBottomButtons() {
        return bottomButtonPanelList.size() > 0;
    }

    private boolean hasLeftButtons() {
        return leftButtonPanelList.size() > 0;
    }

    private boolean hasRightButtons() {
        return rightButtonPanelList.size() > 0;
    }

    /**
     * Updates the panels as a result of the user clicking one of the button panels buttons.
     * The effect is that one of the hideable panels displays itself.
     */
    private void updatePanels() {
        int noOfRows = getNoOfRows();
        boolean hasTopButtons = hasTopButtons();
        boolean hasLeftButtons = hasLeftButtons();
        boolean hasRightButtons = hasRightButtons();
        boolean hasBottomButtons = hasBottomButtons();
        boolean isLeftPoppedUp = leftButtonPanelList.isPopupPanelShowing();
        boolean isRightPoppedUp = rightButtonPanelList.isPopupPanelShowing();
        boolean isTopPoppedUp = topButtonPanelList.isPopupPanelShowing();
        boolean isBotPoppedUp = bottomButtonPanelList.isPopupPanelShowing();
        int horizontalPanelCols = 1;
        if (isLeftPoppedUp) horizontalPanelCols++;
        if (isRightPoppedUp) horizontalPanelCols++;
        int verticalPanelCols = 1;
        if (isTopPoppedUp) verticalPanelCols++;
        if (isBotPoppedUp) verticalPanelCols++;
        int startVerticalInset = 1;
        if (hasTopButtons) startVerticalInset++;
        if (isTopPoppedUp) startVerticalInset++;
        int startHorizontalInset = 1;
        if (hasLeftButtons) startHorizontalInset++;
        if (isLeftPoppedUp) startHorizontalInset++;
        String colLayout = getLayoutStringCol(isLeftPoppedUp, getNoOfColumns());
        String rowLayout = getLayoutStringRow(isTopPoppedUp, getNoOfRows());
        if (colLayout != null && rowLayout != null) {
            FormLayout layout = new FormLayout(colLayout, rowLayout);
            builder = new DefaultFormBuilder(layout, new FormDebugPanel());
        }
        if (hasLeftButtons) {
            builder.add(leftButtonPanelList.getButtonPanel(), cellConstraints.xywh(1, 1, 1, noOfRows));
            if (isLeftPoppedUp) {
                CellConstraints cc = cellConstraints.xyw(2, startVerticalInset, verticalPanelCols);
                builder.add(leftButtonPanelList.getPopupPanel(), cc);
            }
        }
        if (hasRightButtons) {
            int startRightPanelX = isRightPoppedUp ? startHorizontalInset + 2 : startHorizontalInset + 1;
            builder.add(rightButtonPanelList.getButtonPanel(), cellConstraints.xywh(startRightPanelX, 1, horizontalPanelCols, noOfRows));
            if (isRightPoppedUp) {
                CellConstraints cc = cellConstraints.xy(startHorizontalInset + 1, 1);
                builder.add(rightButtonPanelList.getPopupPanel(), cc);
            }
        }
        if (hasTopButtons) {
            builder.add(topButtonPanelList.getButtonPanel(), cellConstraints.xyw(2, 1, horizontalPanelCols));
            if (isTopPoppedUp) {
                CellConstraints cc = cellConstraints.xyw(startHorizontalInset, startVerticalInset, horizontalPanelCols);
                builder.add(topButtonPanelList.getPopupPanel(), cc);
            }
        }
        if (hasBottomButtons) {
            int startBottomPanelY = isBotPoppedUp ? startVerticalInset + 2 : startVerticalInset + 1;
            builder.add(bottomButtonPanelList.getButtonPanel(), cellConstraints.xyw(startBottomPanelY, 1, horizontalPanelCols));
            if (isRightPoppedUp) {
                CellConstraints cc = cellConstraints.xy(startHorizontalInset, startBottomPanelY);
                builder.add(bottomButtonPanelList.getPopupPanel(), cc);
            }
        }
        if (content != null) {
            builder.add(content, cellConstraints.xy(startHorizontalInset, startVerticalInset));
        }
        FormDebugUtils.dumpAll(builder.getPanel());
        final JPanel panel = builder.getPanel();
        FormDebugPanel debugPanel = new FormDebugPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        removeAll();
        add(panel, BorderLayout.CENTER);
        validate();
    }

    private int getNoOfColumns() {
        int cols = 1;
        if (hasLeftButtons()) {
            cols++;
            if (leftButtonPanelList.isPopupPanelShowing()) {
                cols++;
            }
        }
        if (hasRightButtons()) {
            cols++;
            if (rightButtonPanelList.isPopupPanelShowing()) {
                cols++;
            }
        }
        return cols;
    }

    private int getNoOfRows() {
        int rows = 1;
        if (hasTopButtons()) {
            rows++;
            if (topButtonPanelList.isPopupPanelShowing()) {
                rows++;
            }
        }
        if (hasBottomButtons()) {
            rows++;
            if (bottomButtonPanelList.isPopupPanelShowing()) {
                rows++;
            }
        }
        return rows;
    }

    private String getLayoutStringRow(boolean isFirstPoppedUp, int noOf) {
        switch(noOf) {
            case 1:
                return "fill:max(50dlu;pref):grow";
            case 2:
                return "fill:min,  fill:max(50dlu;pref):grow";
            case 3:
                return "fill:min,  fill:max(50dlu;pref):grow, fill:min";
            case 4:
                if (isFirstPoppedUp) {
                    return "fill:min,  fill:min,  fill:max(50dlu;pref):grow, fill:min";
                } else {
                    return "fill:min,  fill:max(50dlu;pref):grow, fill:min, fill:min";
                }
            case 5:
                return "fill:min, fill:min, fill:max(50dlu;pref):grow, fill:min, fill:min";
        }
        return null;
    }

    private String getLayoutStringCol(boolean isFirstPoppedUp, int noOf) {
        switch(noOf) {
            case 1:
                return "fill:max(100dlu;pref):grow";
            case 2:
                return "left:min,  fill:max(100dlu;pref):grow";
            case 3:
                if (isFirstPoppedUp) {
                    return "left:min, left:min, fill:max(100dlu;pref):grow";
                } else {
                    return "left:min,  fill:max(100dlu;pref):grow, left:min";
                }
            case 4:
                if (isFirstPoppedUp) {
                    return "left:min,  left:pref,  fill:max(100dlu;pref):grow, left:min";
                } else {
                    return "left:min,  fill:max(100dlu;pref):grow, left:pref, left:min";
                }
            case 5:
                return "left:min, left:pref, fill:max(100dlu;pref):grow, left:pref, left:min";
        }
        return null;
    }

    /**
     * This method gets called when a bound property is changed. This is called as the result of one of
     * the popup panels being hidden for instance.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *   	and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        Object o = evt.getSource();
        boolean selectedPanelChanged = ButtonPanelList.CURRENT_SELECTED_PANEL.equals(evt.getPropertyName());
        if (o instanceof ButtonPanelList && selectedPanelChanged) {
            updatePanels();
        }
    }
}
