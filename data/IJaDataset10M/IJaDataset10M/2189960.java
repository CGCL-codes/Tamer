package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.List;

/**Represents traded volume at each price on the given runner in a particular market.
 * 
 * @author korzekwad
 *
 */
public class BFRunnerTradedVolume implements Serializable {

    private final int selectionId;

    private final List<BFPriceTradedVolume> priceTradedVolume;

    public BFRunnerTradedVolume(int selectionId, List<BFPriceTradedVolume> priceTradedVolume) {
        this.selectionId = selectionId;
        this.priceTradedVolume = priceTradedVolume;
    }

    public int getSelectionId() {
        return selectionId;
    }

    public List<BFPriceTradedVolume> getPriceTradedVolume() {
        return priceTradedVolume;
    }

    @Override
    public String toString() {
        return "BFRunnerTradedVolume [selectionId=" + selectionId + ", priceTradedVolume=" + priceTradedVolume + "]";
    }
}
