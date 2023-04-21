package jNab.core.protocol;

/**
 * Class wrapping a reboot block.
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class RebootBlock extends Block {

    /**
     * Creating a new reboot block instance.
     */
    public RebootBlock() {
        super(Block.REBOOT_BLOCK_TYPE, 0, new byte[] {});
    }
}
