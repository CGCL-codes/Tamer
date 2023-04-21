package org.fudaa.fudaa.tr.reflux;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.ButtonGroup;
import com.memoire.bu.BuBorders;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuRadioButton;
import com.memoire.bu.BuTextField;
import com.memoire.bu.BuVerticalLayout;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluFileChooserPanel;
import org.fudaa.ctulu.gui.CtuluLibSwing;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.fudaa.tr.common.TrResource;

/**
 * @author fred deniger
 * @version $Id: TrRefluxRadiationVentPanel.java,v 1.2 2007-05-04 14:01:53 deniger Exp $
 */
public class TrRefluxRadiationVentPanel extends CtuluDialogPanel {

    final H2dVariableType[] values_;

    BuTextField[] tfValues_;

    CtuluFileChooserPanel pn_;

    BuRadioButton rdFile_;

    BuPanel pnValues_;

    public TrRefluxRadiationVentPanel(final H2dVariableType[] _values, final File _f) {
        super(true);
        values_ = _values;
        tfValues_ = new BuTextField[values_.length];
        for (int i = 0; i < tfValues_.length; i++) {
            tfValues_[i] = BuTextField.createDoubleField();
            tfValues_[i].setValue(CtuluLib.ZERO);
        }
        setLayout(new BuVerticalLayout(5));
        rdFile_ = new BuRadioButton();
        rdFile_.setText(TrResource.getS("Initialiser depuis un fichier compatible"));
        final BuRadioButton rdValue = new BuRadioButton();
        final ButtonGroup bg = new ButtonGroup();
        bg.add(rdValue);
        bg.add(rdFile_);
        rdValue.setText(TrResource.getS("Initialiser depuis des valeurs constantes"));
        rdValue.setSelected(true);
        add(rdValue);
        pnValues_ = new BuPanel(new BuGridLayout(2, 3, 3));
        for (int i = 0; i < values_.length; i++) {
            addLabel(pnValues_, values_[i].getName());
            pnValues_.add(tfValues_[i]);
        }
        pnValues_.setBorder(BuBorders.EMPTY0505);
        add(pnValues_);
        add(rdFile_);
        pn_ = new CtuluFileChooserPanel();
        pn_.setWriteMode(false);
        pn_.setBorder(BuBorders.EMPTY0505);
        add(pn_);
        pn_.setEnabled(false);
        final ItemListener object = new ItemListener() {

            public void itemStateChanged(final ItemEvent _e) {
                pn_.setEnabled(rdFile_.isSelected());
                CtuluLibSwing.setEnable(pnValues_, !rdFile_.isSelected());
            }
        };
        rdFile_.addItemListener(object);
        if (_f != null) {
            pn_.setFile(_f);
            rdFile_.setSelected(true);
        }
    }

    public double[] getValues() {
        final double[] res = new double[tfValues_.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = ((Double) tfValues_[i].getValue()).doubleValue();
        }
        return res;
    }

    @Override
    public boolean valide() {
        if (rdFile_.isSelected() && !CtuluLibFile.exists(getSelectedFile())) {
            setErrorText(CtuluLib.getS("Le fichier n'existe pas"));
            return false;
        }
        return true;
    }

    public File getSelectedFile() {
        if (rdFile_.isSelected()) {
            return pn_.getFile();
        }
        return null;
    }
}
