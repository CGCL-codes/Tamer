package com.tensegrity.palorules;

import org.palo.api.ConnectionEvent;

/**
 * The target part of the rule editor.
 * @author Andreas Ebbert
 * @version $Id: IRuleTargetPartEditor.java,v 1.5 2007/12/12 18:59:25 AndreasEbbert Exp $
 */
public interface IRuleTargetPartEditor {

    /**
   * Returns <code>true</code> of the editor contains unsaved changes.
   * @return <code>true</code> of the editor contains unsaved changes.
   */
    boolean isDirty();

    /**
   * Resets the dirty flag of the editor.
   */
    void resetDirty();

    /**
   * Sets the content of the editor to match the given {@link CellAreaDefinition}.
   */
    void setTargetCoords(CellAreaDefinition targetDef);

    void setListeningForTargetChanges(boolean b);

    void handleConnectionEvent(ConnectionEvent e);

    /**
   * Returns a {@link CellAreaDefinition} instance matching the editor state.
   * @return a {@link CellAreaDefinition} instance matching the editor state.
   */
    CellAreaDefinition createTargetDefinition();

    /**
   * Returns the target portion of the rule definition string.
   * @return the target portion of the rule definition string.
   */
    String toRule();
}
