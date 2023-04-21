package org.fudaa.ebli.calque;

import javax.swing.Action;
import org.fudaa.ebli.palette.BPaletteCouleurChooser;
import org.fudaa.ebli.palette.BPaletteInterface;
import org.fudaa.ebli.ressource.EbliResource;

/**
 * @author Fred Deniger
 * @version $Id: CalqueActionColorForeground.java,v 1.2 2004-09-24 15:15:36 deniger Exp $
 */
public class CalqueActionColorForeground extends CalqueActionTreePalette {

    public CalqueActionColorForeground(BArbreCalqueModel _m) {
        super(EbliResource.EBLI.getString("Couleur de trac�"), EbliResource.EBLI.getIcon("avantplan"), "FOREGROUND_COLOR", _m);
        putValue(Action.SHORT_DESCRIPTION, EbliResource.getS("Modifier la couleur de trac�"));
    }

    protected boolean isTargetValid(Object _o) {
        return BPaletteCouleurChooser.isTargetValid(_o);
    }

    public BPaletteInterface buildPaletteContent() {
        return new BPaletteCouleurChooser();
    }

    protected void setTarget(Object _o) {
        super.setTarget(_o);
    }
}
