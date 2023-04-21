package org.fudaa.fudaa.tr.post;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import com.memoire.bu.BuBorders;
import com.memoire.bu.BuCheckBox;
import com.memoire.bu.BuComboBox;
import com.memoire.bu.BuVerticalLayout;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.gui.CtuluComboBoxModelAdapter;
import org.fudaa.fudaa.tr.common.TrResource;

/**
 * @author fred deniger
 * @version $Id: TrPostProjetCompTimeStepPanel.java,v 1.4 2007-01-17 10:44:30 deniger Exp $
 */
public class TrPostProjetCompTimeStepPanel {

    BuComboBox cbImportTimeStep_;

    BuComboBox cbSrcTimeStep_;

    BuCheckBox chImportUseOne_;

    BuCheckBox chSrcUseOne_;

    private final JPanel pn_;

    public TrPostProjetCompTimeStepPanel(final TrPostSource _ref, final TrPostSource _proj) {
        pn_ = TrPostProjectCompPanel.createVarPn(CtuluLib.getS("Pas de temps"));
        pn_.setLayout(new BuVerticalLayout(2));
        if (_ref != null) {
            buildSrc(_ref);
        }
        if (_proj != null) {
            buildProj(_proj);
        }
    }

    private void buildProj(final TrPostSource _proj) {
        chImportUseOne_ = new BuCheckBox(TrResource.getS("Projet import�: utiliser un seul pas de temps"));
        cbImportTimeStep_ = new BuComboBox();
        cbImportTimeStep_.setModel(new CtuluComboBoxModelAdapter(_proj.getTime().getTimeListModel()));
        pn_.add(chImportUseOne_);
        cbImportTimeStep_.setAlignmentX(1);
        cbImportTimeStep_.setBorder(BuBorders.EMPTY0505);
        pn_.add(cbImportTimeStep_);
        cbImportTimeStep_.setEnabled(false);
        chImportUseOne_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                cbImportTimeStep_.setEnabled(chImportUseOne_.isSelected());
            }
        });
        cbImportTimeStep_.setSelectedIndex(cbImportTimeStep_.getModel().getSize() - 1);
    }

    private void buildSrc(final TrPostSource _ref) {
        chSrcUseOne_ = new BuCheckBox(TrResource.getS("Projet courant: utiliser un seul pas de temps"));
        cbSrcTimeStep_ = new BuComboBox();
        cbSrcTimeStep_.setModel(new CtuluComboBoxModelAdapter(_ref.getTime().getTimeListModel()));
        pn_.add(chSrcUseOne_);
        cbSrcTimeStep_.setAlignmentX(1);
        cbSrcTimeStep_.setBorder(BuBorders.EMPTY0505);
        pn_.add(cbSrcTimeStep_);
        chSrcUseOne_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                cbSrcTimeStep_.setEnabled(chSrcUseOne_.isSelected());
            }
        });
        cbSrcTimeStep_.setSelectedIndex(cbSrcTimeStep_.getModel().getSize() - 1);
        cbSrcTimeStep_.setEnabled(false);
    }

    public int getImportedTimeStepToUse() {
        return cbImportTimeStep_ == null ? -1 : cbImportTimeStep_.getSelectedIndex();
    }

    int getSrcTimeStepToUse() {
        return cbSrcTimeStep_ == null ? -1 : cbSrcTimeStep_.getSelectedIndex();
    }

    public boolean useOneTimeStepImported() {
        return chImportUseOne_ == null ? false : chImportUseOne_.isSelected();
    }

    boolean useOneTimeStepSrc() {
        return chSrcUseOne_ == null ? false : chSrcUseOne_.isSelected();
    }

    public JPanel getPn() {
        return pn_;
    }
}
