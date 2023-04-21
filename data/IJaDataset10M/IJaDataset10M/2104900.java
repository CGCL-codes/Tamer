package com.customwars.client.action;

import com.customwars.client.ui.state.InGameContext;

/**
 * Interface for each Action in the game
 * an Action can be done and undone.
 * <p/>
 * When the action has been performed {@link #isCompleted()} will return true
 */
public interface CWAction {

    void invoke(InGameContext context);

    void update(int elapsedTime);

    boolean canUndo();

    void undo();

    String getName();

    boolean isCompleted();

    /**
   * The action command that can be used to recreate this action. Each parameter is separated with a space.
   * Following pattern is used:
   * capture 0 0
   * build_unit 0 0
   * move 0 0 1 1
   * <action_name> <param1> <param2>
   *
   * @return The action command, null if this action has no action command.
   */
    String getActionCommand();
}
