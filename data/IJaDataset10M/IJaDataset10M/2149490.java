package com.simpledata.bc.uicomponents.worksheet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import org.apache.log4j.Logger;
import com.simpledata.bc.datamodel.WorkSheet;
import com.simpledata.bc.datamodel.event.*;
import com.simpledata.bc.tools.Lang;

/**
 * Defines a border for WorkSheetPanelContainers
 */
public class WorkSheetPanelBorder extends JPanel implements NamedEventListener {

    private static final Logger m_log = Logger.getLogger(WorkSheetPanelBorder.class);

    private WorkSheetPanel owner;

    private static final String NULL_WS_TITLE = "WorkSheetPanelBorder:NullWorkSheetTitle";

    private JPanel titlePanel;

    private JLabel titleLabel;

    private JPanel actionPanelContainer;

    public WorkSheetPanelBorder(WorkSheetPanel wsp) {
        super();
        this.owner = wsp;
        initComponents();
        if (wsp != null) {
            WorkSheet ws = wsp.getWorkSheet();
            if (ws != null) {
                ws.addNamedEventListener(this, -1, ws.getClass());
            }
        }
        this.titleLabel.addMouseListener(new BorderMouseListener(owner));
    }

    private static Color getBackgroundColor() {
        return UIManager.getColor("TextField.selectionBackground");
    }

    private static Color getForegroundColor() {
        return UIManager.getColor("TextField.selectionForeground");
    }

    private void initComponents() {
        this.setOpaque(true);
        this.setBackground(getBackgroundColor());
        this.setBorder(new LineBorder(getForegroundColor(), 1));
        GridBagConstraints gridBagConstraints;
        titlePanel = new JPanel();
        titleLabel = new JLabel();
        actionPanelContainer = new JPanel(new BorderLayout());
        this.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        this.add(new JLabel(), gridBagConstraints);
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);
        titleLabel.setOpaque(false);
        titleLabel.setForeground(getForegroundColor());
        if (this.owner == null) {
            titleLabel.setText(Lang.translate("WorkSheetPanelBorder:NullWorkSheetPanel"));
        } else {
            titleLabel.setIcon(this.owner.getIcon());
            WorkSheet ws = this.owner.getWorkSheet();
            if (ws != null) {
                setTitle(ws.getTitle());
            } else {
                setTitle(Lang.translate(NULL_WS_TITLE));
            }
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 5, 0, 5);
        titlePanel.add(titleLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        this.add(titlePanel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        this.add(new JLabel(), gridBagConstraints);
        actionPanelContainer.setOpaque(false);
        actionPanelContainer.setForeground(getForegroundColor());
        if (this.owner != null) {
            JPanel jp = this.owner.getActionPanel();
            if (jp != null) {
                jp.setOpaque(false);
                jp.setForeground(getForegroundColor());
                this.actionPanelContainer.add(jp, BorderLayout.CENTER);
            }
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        this.add(actionPanelContainer, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        this.add(new JLabel(), gridBagConstraints);
    }

    private void setTitle(String s) {
        int len1 = -1;
        int len2 = -1;
        Font f = titleLabel.getFont().deriveFont(BorderMouseListener.NOT_OVER_STYLE);
        FontMetrics fm = titleLabel.getFontMetrics(f);
        len1 = fm.stringWidth(s);
        f = f.deriveFont(BorderMouseListener.OVER_STYLE);
        fm = titleLabel.getFontMetrics(f);
        len2 = fm.stringWidth(s);
        this.titleLabel.setPreferredSize(new Dimension(Math.max(len1, len2) + 16 + titleLabel.getIconTextGap(), 18));
        this.titleLabel.setText(s);
    }

    /**
     * @see com.simpledata.bc.datamodel.event.NamedEventListener#eventOccured(com.simpledata.bc.datamodel.event.NamedEvent)
     */
    public void eventOccured(NamedEvent e) {
        if (e.getSource() == this.owner.getWorkSheet()) {
            if (e.getEventCode() == NamedEvent.TITLE_MODIFIED) {
                setTitle(this.owner.getWorkSheet().getTitle());
            }
            if (e.getEventCode() == NamedEvent.WORKSHEET_DATA_MODIFIED || e.getEventCode() == NamedEvent.WORKSHEET_OPTION_ADDED || e.getEventCode() == NamedEvent.WORKSHEET_OPTION_REMOVED || e.getEventCode() == NamedEvent.WORKSHEET_REDUC_OR_FIXED_ADD_REMOVE) this.titleLabel.setIcon(this.owner.getIcon());
        }
    }
}

/**
 * A mouse listener for the border
 */
class BorderMouseListener implements MouseListener {

    private static Logger m_log = Logger.getLogger(BorderMouseListener.class);

    private static JPopupMenu jPopupMenu;

    private WorkSheetPanel attWsp;

    public static final int OVER_STYLE = Font.BOLD;

    public static final int NOT_OVER_STYLE = Font.PLAIN;

    public BorderMouseListener(WorkSheetPanel wsp) {
        this.attWsp = wsp;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        manageEvent(e, true);
    }

    public void mouseReleased(MouseEvent e) {
        if (!e.isConsumed()) {
            manageEvent(e, false);
        }
    }

    public void mouseEntered(MouseEvent e) {
        JLabel jb = (JLabel) e.getSource();
        Font f = jb.getFont().deriveFont(OVER_STYLE);
        jb.setFont(f);
    }

    public void mouseExited(MouseEvent e) {
        JLabel jb = (JLabel) e.getSource();
        Font f = jb.getFont().deriveFont(NOT_OVER_STYLE);
        jb.setFont(f);
    }

    private void manageEvent(MouseEvent e, boolean pressed) {
        if (e.isPopupTrigger() || pressed) {
            jPopupMenu = this.attWsp.getJPopupMenu();
            if (jPopupMenu != null) {
                jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
