package jabba.ui.roster.cell;

import java.awt.Color;
import javax.swing.ImageIcon;
import jabba.platform.roster.RosterEntry;

public abstract class RosterCellBase {

    protected RosterEntry re;

    public RosterCellBase(RosterEntry re) {
        this.re = re;
    }

    public String getName() {
        return re.getName();
    }

    public abstract ImageIcon getIcon();

    public abstract Color getForeground();

    public abstract Color getBackground();
}
