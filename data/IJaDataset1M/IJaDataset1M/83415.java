package de.sudokuloeser.gui.observers;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import de.sudokuloeser.gui.Messages;
import de.sudokuloeser.observer.SelfSubscribing;
import de.sudokuloeser.observer.SudokuManager;
import de.sudokuloeser.observer.SudokuObserverWorkingState;
import de.sudokuloeser.observer.WorkSatus;

/**
 * @author daju
 * @since 1.0.0
 *
 */
public class WorkingStateLable extends JLabel implements SudokuObserverWorkingState, SelfSubscribing {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4540749804448277276L;

    SudokuManager sudokuManager;

    public WorkingStateLable(SudokuManager sudokuManager) {
        super();
        this.sudokuManager = sudokuManager;
        updateWorkingState();
        this.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        this.setPreferredSize(new Dimension(125, (int) getPreferredSize().getHeight()));
    }

    public void updateWorkingState() {
        WorkSatus state = sudokuManager.getCurrentWorkingState();
        if (state.equals(WorkSatus.WORKING)) {
            this.setText(Messages.getString("WorkingStateLable.Computing"));
        } else {
            assert state.equals(WorkSatus.WAITING);
            this.setText(Messages.getString("WorkingStateLable.Done"));
        }
    }

    public void subscribe() {
        sudokuManager.subscribe(this);
    }
}
