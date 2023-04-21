package net.liveseeds.base.scenario;

import net.liveseeds.base.LiveSeedsException;
import net.liveseeds.base.liveseed.LiveSeed;
import java.io.Serializable;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public interface LiveSeedScenarioInstruction extends Serializable {

    long serialVersionUID = -2521728439776734409L;

    LiveSeedScenario getLiveSeedScenario();

    void setLiveSeedScenario(final LiveSeedScenario liveSeedScenario);

    int getLineNumber() throws LiveSeedsException;

    void interpret(final LiveSeed liveSeed) throws LiveSeedsException;

    void mutate() throws LiveSeedsException;

    boolean requiresTurn();

    void addLiveSeedScenarioInstructionListener(LiveSeedScenarioInstructionListener listener);

    void removeLiveSeedScenarioInstructionListener(LiveSeedScenarioInstructionListener listener);

    LiveSeedScenarioInstruction createCopy(LiveSeedScenario scenario) throws LiveSeedsException;
}
