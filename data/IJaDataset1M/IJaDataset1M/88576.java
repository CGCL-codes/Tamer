package heisig.random;

public class LeitnerStrategyEqualSizedGroups extends LeitnerStrategy {

    @Override
    protected int maxCardsForIntermediateGroup(int group) {
        int nrCards = cachedMax - cachedMin + 1;
        return nrCards;
    }
}
