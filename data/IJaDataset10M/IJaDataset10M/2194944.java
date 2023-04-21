package emil.poker.dataInsert.statistics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import emil.poker.entities.HandEntity;
import net.sourceforge.robotnik.poker.Deck;
import net.sourceforge.robotnik.poker.Hand;

public class CalcChanceToGetHand {

    private List<HandEntity> alreadyDealtHands;

    HashMap<HandEntity, List<Hand>> handsPerHandEntity;

    List<HandEntity> playableHandEntities;

    int numberOfHands = 0;

    public int getNumberOfHands() {
        return numberOfHands;
    }

    /**
	 * 
	 * @param alreadyDealtHands
	 */
    public CalcChanceToGetHand(List<HandEntity> alreadyDealtHands) {
        this();
        this.alreadyDealtHands = alreadyDealtHands;
    }

    public CalcChanceToGetHand() {
        handsPerHandEntity = new HashMap<HandEntity, List<Hand>>();
    }

    /**
	 * 
	 * @param alreadyDealtHand
	 */
    public CalcChanceToGetHand(HandEntity alreadyDealtHand) {
        this();
        this.alreadyDealtHands = new LinkedList<HandEntity>();
        this.alreadyDealtHands.add(alreadyDealtHand);
    }

    private void init() {
        handsPerHandEntity.clear();
        numberOfHands = 0;
        Deck deck = new Deck();
        deck.init();
        if (alreadyDealtHands != null) {
            for (HandEntity handEntity : alreadyDealtHands) {
                deck.getHand(handEntity.getName());
            }
        }
        for (HandEntity handEntity : playableHandEntities) {
            List<Hand> allHandsForDesc = deck.getAllHandsForDesc(handEntity.getName());
            numberOfHands += allHandsForDesc.size();
            handsPerHandEntity.put(handEntity, allHandsForDesc);
        }
    }

    /**
	 * 
	 * @param handEntity
	 * @return
	 */
    public double getChanceToGet(HandEntity handEntity) {
        double d = handsPerHandEntity.get(handEntity).size();
        d /= numberOfHands;
        return d;
    }

    /**
	 * 
	 * @param handEntity
	 * @return
	 */
    public double getChanceToGetAnyOfTheseOutOfDeck(HandEntity handEntity) {
        return 0;
    }

    public List<HandEntity> getPlayableHandEntities() {
        return playableHandEntities;
    }

    public void setPlayableHandEntities(List<HandEntity> playableHandEntities) {
        this.playableHandEntities = playableHandEntities;
        init();
    }
}
