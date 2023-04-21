package objects.votes;

import objects.Galaxy;

public final class LightVotesCalculator extends StandartVotesCalculator {

    @Override
    public final double calc(Galaxy galaxy) {
        return 0.0005001 * galaxy.totalPop();
    }
}
