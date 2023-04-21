package net.liveseeds.base.scenario;

import net.liveseeds.base.LiveSeedsException;
import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.listeners.ListenerSet;
import net.liveseeds.listeners.StrongListenerSet;
import java.util.Iterator;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
class DefaultLiveSeedScenarioInstruction implements LiveSeedScenarioInstruction {

    private transient ListenerSet listeners;

    private LiveSeedScenario liveSeedScenario;

    private int lineNumber;

    protected DefaultLiveSeedScenarioInstruction(final LiveSeedScenario liveSeedScenario) {
        this.liveSeedScenario = liveSeedScenario;
    }

    public final LiveSeedScenario getLiveSeedScenario() {
        return liveSeedScenario;
    }

    public void setLiveSeedScenario(final LiveSeedScenario liveSeedScenario) {
        this.liveSeedScenario = liveSeedScenario;
    }

    public final int getLineNumber() throws LiveSeedsException {
        final LiveSeedScenarioInstruction[] instructions = getLiveSeedScenario().getInstructions();
        if (lineNumber < instructions.length && instructions[lineNumber] == this) {
            return lineNumber;
        }
        return lineNumber = LiveSeedInstructionUtil.getLineNumber(getLiveSeedScenario(), this);
    }

    public void interpret(final LiveSeed liveSeed) throws LiveSeedsException {
    }

    public void mutate() throws LiveSeedsException {
    }

    public boolean requiresTurn() {
        return false;
    }

    public void notifyListeners() {
        if (listeners == null) {
            return;
        }
        for (Iterator iterator = listeners.notifyListeners(); iterator.hasNext(); ) {
            final LiveSeedScenarioInstructionListener listener = (LiveSeedScenarioInstructionListener) iterator.next();
            if (listener != null) {
                listener.instructionChanged();
            } else {
                iterator.remove();
            }
        }
        listeners.finishNotification();
    }

    public void addLiveSeedScenarioInstructionListener(final LiveSeedScenarioInstructionListener listener) {
        initListeners();
        listeners.addListener(listener);
    }

    public void removeLiveSeedScenarioInstructionListener(final LiveSeedScenarioInstructionListener listener) {
        initListeners();
        listeners.removeListener(listener);
    }

    private void initListeners() {
        if (listeners == null) {
            listeners = new StrongListenerSet();
        }
    }

    public LiveSeedScenarioInstruction createCopy(final LiveSeedScenario scenario) throws LiveSeedsException {
        return new DefaultLiveSeedScenarioInstruction(scenario);
    }
}
