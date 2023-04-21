package org.fudaa.fudaa.tr.data;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import com.memoire.bu.BuLabel;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.fudaa.meshviewer.MvFindComponentDefault;
import org.fudaa.fudaa.meshviewer.MvResource;

/**
 * @author deniger
 * @version $Id: TrFindComponentBcPoint.java,v 1.7 2004-09-07 15:07:25 deniger Exp $
 */
public class TrFindComponentBcPoint extends MvFindComponentDefault {

    JRadioButton generalData_;

    /**
   * 
   */
    public TrFindComponentBcPoint(String _l) {
        super(_l);
        JRadioButton bt1 = new JRadioButton(MvResource.getS("Index(s) sur les fronti�res"));
        generalData_ = new JRadioButton(MvResource.getS("Index(s) g�n�raux"));
        bt1.setMnemonic('f');
        if (bt1.getText().indexOf('f') < 0) bt1.setMnemonic('b');
        generalData_.setMnemonic('g');
        ButtonGroup bg = new ButtonGroup();
        bg.add(bt1);
        bg.add(generalData_);
        bt1.setSelected(true);
        add(new BuLabel(CtuluLibString.EMPTY_STRING));
        add(bt1);
        add(new BuLabel(CtuluLibString.EMPTY_STRING));
        add(generalData_);
    }

    /**
   *
   */
    public String getFindText() {
        if (generalData_.isSelected()) return "G:" + super.getFindText(); else return super.getFindText();
    }
}
