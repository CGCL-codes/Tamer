package com.simpledata.bc.uicomponents.worksheet.dispatcher;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import com.simpledata.bc.Resources;
import com.simpledata.bc.components.worksheet.dispatcher.DispatcherSequencer;
import com.simpledata.bc.datamodel.WorkSheet;
import com.simpledata.bc.tools.Lang;
import com.simpledata.bc.uicomponents.TarifViewer;
import com.simpledata.bc.uicomponents.worksheet.WorkSheetPanel;
import com.simpledata.bc.uicomponents.worksheet.dispatcher.tools.TabbedDispatcherAbstract;
import com.simpledata.bc.uitools.SButtonIcon;

/**
 * UI for DispatcherSequencer
 * @see com.simpledata.bc.components.worksheet.dispatcher.DispatcherSequencer
 */
public class DispatcherSequencerPanel extends TabbedDispatcherAbstract {

    private static final Logger m_log = Logger.getLogger(DispatcherSequencerPanel.class);

    /** my Icon **/
    public static ImageIcon defaultClassTreeIcon = Resources.wsDispatcherSequence;

    /** my WorkSheet **/
    private DispatcherSequencer ds;

    /**
	 *
	 */
    public DispatcherSequencerPanel(DispatcherSequencer ds, TarifViewer tv) {
        super(ds, tv);
        this.ds = ds;
    }

    public void save() {
    }

    public ImageIcon getTreeIcon() {
        return defaultClassTreeIcon;
    }

    /**
	 * get the worksheet panel at this index
	 * @param index position of the workSheetPanel desired
	 */
    public WorkSheetPanel getWorkSheetPanelAt(int index) {
        if (ds == null) return null;
        return getDisplayController().getWorkSheetPanel(ds.getWorkSheetAtIndex(index));
    }

    /**
	 * get the index of this workSheet
	 * @return -1 if not found
	 */
    public int getWorkSheetIndex(WorkSheet ws) {
        if (ds == null) return -1;
        return ds.getWorkSheetIndex(ws);
    }

    /**
	 * get the number of tabs
	 */
    public int getTabCount() {
        if (ds == null || ds.getChildWorkSheets() == null) return 0;
        return ds.getChildWorkSheets().size();
    }

    /**
	 * get the WorkSHeetPanel Title .. can be overwriten
	 */
    public String getTitleOf(int index) {
        return (index + 1) + " " + super.getTitleOf(index);
    }

    /**
	 * 
	 */
    public JPanel getOptionPanel() {
        return null;
    }

    /**
	 * return a JPanel to be included in the border (tool bar)<BR>
	 * Overide this method if needed;
	 */
    public JPanel getActionPanel() {
        if (getDisplayController().getEditWorkPlaceState() == WorkSheetPanel.WSIf.EDIT_STATE_NONE) {
            return null;
        }
        if (jPanel5 != null) return jPanel5;
        upButton = new SButtonIcon(HORIZONTAL_LAY ? Resources.moveLeft : Resources.moveUP);
        downButton = new SButtonIcon(HORIZONTAL_LAY ? Resources.moveRight : Resources.moveDown);
        plusButton = new SButtonIcon(Resources.iconPlus);
        deleteButton = new SButtonIcon(Resources.iconDelete);
        jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        upButton.setToolTipText(Lang.translate("Move step Up"));
        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                upButtonActionPerformed();
            }
        });
        jPanel5.add(upButton);
        downButton.setToolTipText(Lang.translate("Move step Down"));
        downButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                downButtonActionPerformed();
            }
        });
        jPanel5.add(downButton);
        plusButton.setToolTipText(Lang.translate("Insert a new step after this one"));
        plusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                plusButtonActionPerformed();
            }
        });
        jPanel5.add(plusButton);
        deleteButton.setToolTipText(Lang.translate("Delete this step"));
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                deleteButtonActionPerformed();
            }
        });
        jPanel5.add(deleteButton);
        return jPanel5;
    }

    void downButtonActionPerformed() {
        if (getSelectedIndex() < 0) return;
        ds.moveWorkSheet(getSelectedIndex(), 1);
        setSelectedIndex(getSelectedIndex() + 1);
        refresh();
    }

    void deleteButtonActionPerformed() {
        if (getSelectedIndex() < 0) return;
        ds.removeWorkSheet(getSelectedIndex());
        refresh();
    }

    void plusButtonActionPerformed() {
        ds.createWorkSheetAfter(getSelectedIndex());
        setSelectedIndex(getSelectedIndex() + 1);
        refresh();
    }

    void upButtonActionPerformed() {
        if (getSelectedIndex() < 0) return;
        ds.moveWorkSheet(getSelectedIndex(), -1);
        setSelectedIndex(getSelectedIndex() - 1);
        refresh();
    }

    private SButtonIcon deleteButton;

    private SButtonIcon downButton;

    private JPanel jPanel5;

    private SButtonIcon plusButton;

    private SButtonIcon upButton;

    /** nothing to do */
    public void indexHasBeenSelected(int index) {
    }
}
