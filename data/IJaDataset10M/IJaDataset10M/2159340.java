package orangenpresse.xsigns;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * XTuringSigns block listener
 * @author Orangenpresse
 */
public class XSignsBlockListener extends BlockListener {

    private final XSigns plugin;

    private Player player;

    public XSignsBlockListener(final XSigns plugin) {
        this.plugin = plugin;
    }

    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        for (BlockFace face : BlockFace.values()) {
            checkCurrent(event.getBlock().getFace(face), event.getNewCurrent());
        }
    }

    private void checkCurrent(Block block, int current) {
        if (block.getType() == Material.SIGN_POST || block.getType() == Material.SIGN || block.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) block.getState();
            String[][] lines = plugin.getString(block);
            if (lines == null) return;
            if (current != 0) {
                for (int i = 0; i < lines[1].length; i++) {
                    if (lines[1][i] != null) sign.setLine(i, lines[1][i]);
                }
            } else {
                for (int i = 0; i < lines[0].length; i++) {
                    if (lines[0][i] != null) sign.setLine(i, lines[0][i]);
                }
            }
        }
        ((CraftWorld) block.getWorld()).getHandle().g(block.getX(), block.getY(), block.getZ());
    }

    public void onBlockBreak(BlockBreakEvent event) {
        this.player = event.getPlayer();
        if (plugin.isXSign(event.getBlock())) {
            plugin.removeSign(event.getBlock());
            this.player.sendMessage("XSign destroyed");
        }
    }

    public void onSignChange(SignChangeEvent event) {
        boolean isRedstoneSign = false;
        String[] lines = event.getLines();
        String[][] newLines = new String[2][4];
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].matches("^<.*\\|.*>$")) {
                isRedstoneSign = true;
                String[] splitString = lines[i].replaceAll("<|>", "").split("\\|", 2);
                newLines[0][i] = splitString[0];
                newLines[1][i] = splitString[1];
            } else {
                newLines[0][i] = newLines[1][i] = lines[i];
            }
        }
        this.player = event.getPlayer();
        if (isRedstoneSign) {
            this.player.sendMessage("XSign created");
            plugin.addSign(event.getBlock(), newLines);
        }
    }
}
