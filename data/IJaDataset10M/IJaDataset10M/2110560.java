package meraner81.wattn.gameplay.action;

import meraner81.wattn.player.WattPlayer;
import meraner81.wattn.utils.WattColor;

public class TellCardColorAction extends WattAction {

    private WattColor color;

    public TellCardColorAction(WattPlayer player, int validitySequence) {
        super(player, validitySequence);
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    public WattColor getColor() {
        return color;
    }

    public void setColor(WattColor color) {
        if (!isExecuted()) {
            this.color = color;
        }
    }
}
