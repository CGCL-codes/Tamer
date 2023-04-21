package com.cajhughes.jdev.copy.view;

import com.cajhughes.jdev.copy.view.resource.CopyResourceUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.ide.controller.IdeAction;
import oracle.ide.keyboard.KeyStrokeContext;
import oracle.ide.keyboard.KeyStrokeMap;
import oracle.ide.keyboard.KeyStrokes;

/**
 * This class implements the KeyStrokeContext interface, and exists to define
 * the scope in which the shortcut key defined for the CopyAsHtml action
 * should act.
 *
 * @author Chris Hughes
 */
public final class CopyKeyStrokeContext implements KeyStrokeContext {

    private final Set<IdeAction> actions = new HashSet<IdeAction>();

    private final KeyStrokeMap keyStrokeMap = new KeyStrokeMap();

    public void add(final IdeAction action, final KeyStrokes keyStrokes) {
        actions.add(action);
        keyStrokeMap.put(keyStrokes, action.getCommandId());
    }

    @Override
    public String getAcceleratorFile() {
        return null;
    }

    @Override
    public Set getAllActions(final boolean global) {
        if (global) {
            return actions;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public List getAllPresets() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return CopyResourceUtil.getString("EXTENSION_NAME");
    }

    @Override
    public KeyStrokeMap getPresetKeyStrokeMap(final Object object, final boolean global) {
        if (global) {
            return keyStrokeMap;
        } else {
            return null;
        }
    }
}
