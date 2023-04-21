package meraner81.wattn.gameplay;

import java.util.List;
import meraner81.wattn.core.WattRound;
import meraner81.wattn.gameplay.action.WattAction;

public interface WattGameManagerListener {

    public String getPlayerName();

    public void gameActionsAvailable(List<WattAction> availableActions);

    public void gameActionPerformed(WattAction action);

    public void gameRoundFinished(WattRound round);
}
