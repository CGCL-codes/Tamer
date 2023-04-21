package scheme4j.state;

import scheme4j.execute.ExecutionSemaphore;
import scheme4j.gui.actions.interprete.CheckAction;
import scheme4j.gui.actions.interprete.DebugOnAction;
import scheme4j.gui.actions.interprete.ExecuteCommandLineAction;
import scheme4j.gui.actions.interprete.PlayStepAction;
import scheme4j.gui.actions.interprete.PauseAction;
import scheme4j.gui.builders.FrameToolbarBuilder;
import scheme4j.gui.panels.EnvironmentWatcherPanel;
import scheme4j.gui.panels.StackElementsWatcherPanel;

/**
 * Stato di debug senza programma.
 */
class EmptyDebugState implements IInterpreterState {

    public IInterpreterState play() {
        ExecutionSemaphore.getInstance().play(1);
        return InterpreterStateConstants.STOP_DEBUG;
    }

    public IInterpreterState stopOrKill() {
        throw new IllegalStateException();
    }

    public IInterpreterState debugSwitch() {
        return InterpreterStateConstants.EMPTY_NORMAL;
    }

    public IInterpreterState programTerminated() {
        return this;
    }

    public void updateProgram() {
        StackElementsWatcherPanel.updateStackDrawing();
        EnvironmentWatcherPanel.getInstance().setVisible(true);
        StackElementsWatcherPanel.getInstance().setVisible(true);
        CheckAction.getInstance().setEnabled(true);
        FrameToolbarBuilder.checkButton().setAction(CheckAction.getInstance());
        PlayStepAction.getInstance().setEnabled(true);
        FrameToolbarBuilder.playButton().setAction(PlayStepAction.getInstance());
        PauseAction.getInstance().setEnabled(false);
        FrameToolbarBuilder.stopButton().setAction(PauseAction.getInstance());
        DebugOnAction.getInstance().setEnabled(true);
        FrameToolbarBuilder.debugButton().setAction(DebugOnAction.getInstance());
        ExecuteCommandLineAction.getInstance().setEnabled(true);
    }
}
