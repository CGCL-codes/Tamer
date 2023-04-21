package populationSimulator;

import java.util.Map.Entry;

public class RandomUsersParticle extends Particle {

    public RandomUsersParticle(Particle particle) {
        super(particle);
    }

    public RandomUsersParticle() {
        super();
    }

    public void step(boolean init) {
        if (!init) {
            setBurstOnWithProb();
        }
        int[] newPopulation = new int[UserStatesEnum.values().length];
        for (int i = 0; i < newPopulation.length; i++) {
            newPopulation[i] = 0;
        }
        for (int i = 0; i < UserStatesEnum.values().length; i++) {
            UserStatesEnum state = UserStatesEnum.values()[i];
            for (int k = 0; k < getPopulation().get(state); k++) {
                double r = Math.random();
                double sum = 0.0;
                for (int j = 0; j < UserStatesEnum.values().length; j++) {
                    UserStatesEnum dest = UserStatesEnum.values()[j];
                    sum += state.transitionProb(dest, isBurstOn());
                    if (r <= sum) {
                        newPopulation[dest.getIndex()]++;
                        break;
                    }
                }
            }
        }
        for (Entry<UserStatesEnum, Integer> entry : getPopulation().entrySet()) {
            entry.setValue(newPopulation[entry.getKey().getIndex()]);
        }
    }
}
