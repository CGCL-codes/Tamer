package net.sf.wwusmart.gui;

import net.sf.wwusmart.algorithms.framework.*;
import net.sf.wwusmart.browsing.Filter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Gui element representing a Filter Operation.
 * 
 * @author Armin
 * @version $Rev: 777 $
 */
public class FilterPanel extends JPanel implements BrowsingOperationPanel {

    private BrowsingInstance bi;

    private FilterAlgorithm filterAlgorithm;

    private ParametersSetting parameters;

    private boolean isApplied;

    private static final Border APPLIED_BORDER = BorderFactory.createTitledBorder(null, "Filter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 11), new Color(25, 130, 50));

    private static final Border PREPARED_BORDER = BorderFactory.createTitledBorder(null, "Filter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 0, 11), new Color(210, 150, 10));

    /** 
     * Creates new form FilterPanel represemting the givn filter algorithm with
     * the given parameter settings and inteded for application in the given
     * BrowsingInstance.
     *
     * @param filterAlgorithm The represented filter algorithm.
     * @param parameters The parameter settings for filterAlgorithm.
     * @param bi The BrowsingInstance to which to apply to.
     */
    public FilterPanel(FilterAlgorithm filterAlgorithm, ParametersSetting parameters, BrowsingInstance bi) {
        this.bi = bi;
        initComponents();
        update(filterAlgorithm, parameters);
        setApplied(false);
    }

    /**
     * Creates a new form FilterPanel representing the given Filter and applied
     * to the given BrowsingInstance.
     *
     * @param filter The Filter to represent.
     * @param bi Where filter is currently applied.
     */
    public FilterPanel(Filter filter, BrowsingInstance bi) {
        this(filter.getFilterAlgorithm(), filter.getParameters(), bi);
        setApplied(true);
    }

    /**
     * Updates the represented Filter with the information from the given
     * AddBrowsingDialog.
     *
     * @param edit An AddBrowsingDialog setting up a filter operation.
     */
    public void update(AddBrowsingDialog edit) {
        update(edit.getFilterAlgorithm(), edit.getParameters());
    }

    /**
     * Updates the represented Filter with the given information and sets the
     * label according to the updated data.
     *
     * @param filterAlgorithm The filterAlgorithm henceforth represented.
     * @param parameters the parameter settings for the represented Filter.
     */
    private void update(FilterAlgorithm filterAlgorithm, ParametersSetting parameters) {
        this.filterAlgorithm = filterAlgorithm;
        this.parameters = parameters;
        infoLabel.setText(filterAlgorithm.getName());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        infoLabel = new JTextArea();
        keepOpsTop = new JPanel();
        opsPanel = new JPanel();
        applyBtn = new JButton();
        editBtn = new JButton();
        removeBtn = new JButton();
        setBorder(PREPARED_BORDER);
        setLayout(new BorderLayout());
        infoLabel.setEditable(false);
        infoLabel.setLineWrap(true);
        infoLabel.setWrapStyleWord(true);
        infoLabel.setOpaque(false);
        add(infoLabel, BorderLayout.CENTER);
        keepOpsTop.setLayout(new BorderLayout());
        opsPanel.setLayout(new BoxLayout(opsPanel, BoxLayout.PAGE_AXIS));
        applyBtn.setIcon(new ImageIcon(getClass().getResource("/net/sf/wwusmart/gui/res/px16/success.png")));
        applyBtn.setToolTipText("Apply");
        applyBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                applyBtnAction(evt);
            }
        });
        opsPanel.add(applyBtn);
        editBtn.setIcon(new ImageIcon(getClass().getResource("/net/sf/wwusmart/gui/res/px16/edit.png")));
        editBtn.setToolTipText("Edit");
        editBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                editBtnAction(evt);
            }
        });
        opsPanel.add(editBtn);
        removeBtn.setIcon(new ImageIcon(getClass().getResource("/net/sf/wwusmart/gui/res/px16/remove.png")));
        removeBtn.setToolTipText("Remove");
        removeBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });
        opsPanel.add(removeBtn);
        keepOpsTop.add(opsPanel, BorderLayout.CENTER);
        add(keepOpsTop, BorderLayout.EAST);
    }

    private void applyBtnAction(ActionEvent evt) {
        apply(bi);
    }

    private void editBtnAction(ActionEvent evt) {
        edit();
    }

    private void removeBtnActionPerformed(ActionEvent evt) {
        remove();
    }

    /**
     * Getter for the algorithm of the represented Filter.
     *
     * @return A FilterAlgorithm.
     */
    public FilterAlgorithm getAlgorithm() {
        return filterAlgorithm;
    }

    /**
     * Getter for the parameter settings of the represented Filter.
     *
     * @return The selected parameter settings.
     */
    public ParametersSetting getParameters() {
        return parameters;
    }

    /**
     * {@inheritDoc }
     */
    public void remove() {
        bi.removeBrowsingOperation(this);
    }

    /**
     * {@inheritDoc }
     */
    public void edit() {
        if (isApplied()) {
            bi.editAppliedBrowsingOperationPanel(this);
        } else {
            Filter f = new Filter(filterAlgorithm, parameters);
            AddBrowsingDialog edit = new AddBrowsingDialog(true, f);
            edit.setVisible(true);
            if (edit.okOption != true) return;
            this.update(edit);
        }
    }

    /**
     * {@inheritDoc }
     */
    public void apply(BrowsingInstance bi) {
        bi.applyFilter(this);
    }

    /**
     * Getter for the isApplied flag.
     * @return The isApplied flag.
     */
    public boolean isApplied() {
        return isApplied;
    }

    /**
     * Determines if the Filter descriibed by this FilterPanel's could be
     * applied.
     * @return true - If the described Filter can be applied.<br>false - else;
     */
    public boolean isApplicable() {
        return !isApplied;
    }

    /**
     * {@inheritDoc}
     * @param b {@inheritDoc}
     */
    public void setApplied(boolean b) {
        this.isApplied = b;
        this.applyBtn.setVisible(!b);
        this.editBtn.setVisible(true);
        this.setBorder((b) ? APPLIED_BORDER : PREPARED_BORDER);
        validate();
        repaint();
    }

    /**
     * {@inheritDoc }
     * @return {@inheritDoc }
     */
    public java.awt.Component getComponent() {
        return this;
    }

    private JButton applyBtn;

    private JButton editBtn;

    private JTextArea infoLabel;

    private JPanel keepOpsTop;

    private JPanel opsPanel;

    private JButton removeBtn;
}
