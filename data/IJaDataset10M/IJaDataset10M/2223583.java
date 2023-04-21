package megamek.client.ui.swing;

import megamek.client.Client;
import megamek.client.ui.swing.util.PlayerColors;
import megamek.common.Player;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Helper class to handle camo selection.  This class will update an
 * <code>JButton</code> to show the selection, update a <code>Player</code>
 * to use the selection, and (optionally) communicate the selection to
 * the server through a <code>Client</code>.
 * <p/>
 * Created on January 24, 2004
 *
 * @author James Damour
 * @version 1
 */
public class CamoChoiceListener implements ItemListener {

    /**
     * The camo button.
     */
    private final JButton butCamo;

    /**
     * The selection dialog.
     */
    private final CamoChoiceDialog dialog;

    /**
     * The default background color.
     */
    private final Color defaultBG;

    /**
     * The <code>Player</code> whose camo selection is being updated.
     */
    private final Player localPlayer;

    /**
     * The player whose camo selection is being updated.
     */
    private final int playerId;

    /**
     * The sender of messages.  This value may be <code>null</code>.
     */
    private final Client client;

    /**
     * Create a new camo selection listener that alerts a server.
     *
     * @param camoDialog - the <code>CamoChoiceDialog</code> that is
     *                   being listened to.
     * @param button     - the <code>JButton</code> that gets updated.
     * @param background - the default background <code>Color</code>
     *                   for the button when a camo image is selected.
     * @param player     - the <code>int</code> ID of the player whose
     *                   camo is updated.
     * @param sender     - the <code>Client</code> that sends the update.
     */
    public CamoChoiceListener(CamoChoiceDialog camoDialog, JButton button, Color background, int player, Client sender) {
        dialog = camoDialog;
        butCamo = button;
        defaultBG = background;
        localPlayer = null;
        playerId = player;
        client = sender;
    }

    /**
     * Create a new camo selection listener that does not alert a server.
     *
     * @param camoDialog - the <code>CamoChoiceDialog</code> that is
     *                   being listened to.
     * @param button     - the <code>JButton</code> that gets updated.
     * @param background - the default background <code>Color</code>
     *                   for the button when a camo image is selected.
     * @param player     - the <code>Player</code> whose camo is updated.
     */
    public CamoChoiceListener(CamoChoiceDialog camoDialog, JButton button, Color background, Player player) {
        dialog = camoDialog;
        butCamo = button;
        defaultBG = background;
        localPlayer = player;
        playerId = player.getId();
        client = null;
    }

    /**
     * Update the camo button when the selection dialog tells us to.
     * <p/>
     * Implements <code>ItemListener</code>.
     *
     * @param event - the <code>ItemEvent</code> of the camo selection.
     */
    public void itemStateChanged(ItemEvent event) {
        Player player = localPlayer;
        if (null == player) player = client.getPlayer(playerId);
        Image image = (Image) event.getItem();
        String category = dialog.getCategory();
        String itemName = dialog.getItemName();
        if (null == image) {
            for (int color = 0; color < Player.colorNames.length; color++) {
                if (Player.colorNames[color].equals(itemName)) {
                    butCamo.setText(Messages.getString("CamoChoiceListener.NoCammo"));
                    butCamo.setBackground(PlayerColors.getColor(color));
                    player.setColorIndex(color);
                    break;
                }
            }
            itemName = null;
        } else {
            butCamo.setText("");
            butCamo.setBackground(defaultBG);
        }
        butCamo.setIcon(new ImageIcon(image));
        player.setCamoCategory(category);
        player.setCamoFileName(itemName);
        if (null != client) client.sendPlayerInfo();
    }
}
