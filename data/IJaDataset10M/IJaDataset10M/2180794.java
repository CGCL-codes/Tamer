package org.fudaa.fudaa.curvi;

import java.awt.Color;
import javax.swing.JComponent;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuInternalFrame;
import com.memoire.bu.BuResource;

/**
 * @version      $Revision: 1.6 $ $Date: 2006-09-19 15:02:25 $ by $Author: deniger $
 * @author       Guillaume Desnoix 
 */
public class CurviFilleSurface extends BuInternalFrame {

    JComponent canvas_;

    JComponent content_;

    public CurviFilleSurface(final String _titre, final double[][] _p, final int _c) {
        super("", true, true, true, true);
        setName("ifSURFACE");
        canvas_ = new CurviSurfaceResultats(_p, _c);
        content_ = (JComponent) getContentPane();
        content_.setLayout(new BuBorderLayout());
        content_.add(canvas_, BuBorderLayout.CENTER);
        setTitle(_titre);
        setBackground(Color.white);
        setFrameIcon(BuResource.BU.getIcon("image"));
        setLocation(60 + 30 * _c, 60 + 30 * _c);
        pack();
    }
}
