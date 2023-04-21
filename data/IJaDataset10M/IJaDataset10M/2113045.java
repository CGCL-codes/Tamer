package org.modyna.modyna.gui;

import junit.framework.TestCase;

/**
 * Pupose:
 * 
 * @author Dr. Rupert Rebentisch
 */
public class StateManagerTest extends TestCase {

    private StateManager stateMgr;

    public void setUp() {
        stateMgr = new StateManager(new GuiManager());
    }

    public void testValidStateTransition() {
        assertTrue(stateMgr.checkStateTransition(StateManager.INITIAL_STATE, StateManager.DEFINITION_STATE));
    }

    public void testInvalidStateTransition() {
        assertTrue(!stateMgr.checkStateTransition(StateManager.DEFINITION_STATE, StateManager.INITIAL_STATE));
    }
}
