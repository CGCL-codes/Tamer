package net.sourceforge.nattable.ui.binding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.layer.LabelStack;
import net.sourceforge.nattable.ui.action.IDragMode;
import net.sourceforge.nattable.ui.action.IKeyAction;
import net.sourceforge.nattable.ui.action.IMouseAction;
import net.sourceforge.nattable.ui.matcher.IKeyEventMatcher;
import net.sourceforge.nattable.ui.matcher.IMouseEventMatcher;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

public class UiBindingRegistry implements IUiBindingRegistry {

    private NatTable natTable;

    private LinkedList<KeyBinding> keyBindings = new LinkedList<KeyBinding>();

    private Map<MouseEventTypeEnum, LinkedList<MouseBinding>> mouseBindingsMap = new HashMap<MouseEventTypeEnum, LinkedList<MouseBinding>>();

    private LinkedList<DragBinding> dragBindings = new LinkedList<DragBinding>();

    public UiBindingRegistry(NatTable natTable) {
        this.natTable = natTable;
    }

    public IKeyAction getKeyEventAction(KeyEvent event) {
        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.getKeyEventMatcher().matches(event)) {
                return keyBinding.getAction();
            }
        }
        return null;
    }

    public IDragMode getDragMode(MouseEvent event) {
        LabelStack regionLabels = natTable.getRegionLabelsByXY(event.x, event.y);
        for (DragBinding dragBinding : dragBindings) {
            if (dragBinding.getMouseEventMatcher().matches(natTable, event, regionLabels)) {
                return dragBinding.getDragMode();
            }
        }
        return null;
    }

    public IMouseAction getMouseMoveAction(MouseEvent event) {
        return getMouseEventAction(MouseEventTypeEnum.MOUSE_MOVE, event);
    }

    public IMouseAction getMouseDownAction(MouseEvent event) {
        return getMouseEventAction(MouseEventTypeEnum.MOUSE_DOWN, event);
    }

    public IMouseAction getSingleClickAction(MouseEvent event) {
        return getMouseEventAction(MouseEventTypeEnum.MOUSE_SINGLE_CLICK, event);
    }

    public IMouseAction getDoubleClickAction(MouseEvent event) {
        return getMouseEventAction(MouseEventTypeEnum.MOUSE_DOUBLE_CLICK, event);
    }

    private IMouseAction getMouseEventAction(MouseEventTypeEnum mouseEventType, MouseEvent event) {
        try {
            LinkedList<MouseBinding> mouseEventBindings = mouseBindingsMap.get(mouseEventType);
            if (mouseEventBindings != null) {
                LabelStack regionLabels = natTable.getRegionLabelsByXY(event.x, event.y);
                for (MouseBinding mouseBinding : mouseEventBindings) {
                    if (mouseBinding.getMouseEventMatcher().matches(natTable, event, regionLabels)) {
                        return mouseBinding.getAction();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerFirstKeyBinding(IKeyEventMatcher keyMatcher, IKeyAction action) {
        keyBindings.addFirst(new KeyBinding(keyMatcher, action));
    }

    public void registerKeyBinding(IKeyEventMatcher keyMatcher, IKeyAction action) {
        keyBindings.addLast(new KeyBinding(keyMatcher, action));
    }

    public void unregisterKeyBinding(IKeyEventMatcher keyMatcher) {
        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.getKeyEventMatcher().equals(keyMatcher)) {
                keyBindings.remove(keyBinding);
                return;
            }
        }
    }

    public void registerFirstMouseDragMode(IMouseEventMatcher mouseEventMatcher, IDragMode dragMode) {
        dragBindings.addFirst(new DragBinding(mouseEventMatcher, dragMode));
    }

    public void registerMouseDragMode(IMouseEventMatcher mouseEventMatcher, IDragMode dragMode) {
        dragBindings.addLast(new DragBinding(mouseEventMatcher, dragMode));
    }

    public void unregisterMouseDragMode(IMouseEventMatcher mouseEventMatcher) {
        for (DragBinding dragBinding : dragBindings) {
            if (dragBinding.getMouseEventMatcher().equals(mouseEventMatcher)) {
                dragBindings.remove(dragBinding);
                return;
            }
        }
    }

    public void registerFirstMouseMoveBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(true, MouseEventTypeEnum.MOUSE_MOVE, mouseEventMatcher, action);
    }

    public void registerMouseMoveBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(false, MouseEventTypeEnum.MOUSE_MOVE, mouseEventMatcher, action);
    }

    public void unregisterMouseMoveBinding(IMouseEventMatcher mouseEventMatcher) {
        unregisterMouseBinding(MouseEventTypeEnum.MOUSE_MOVE, mouseEventMatcher);
    }

    public void registerFirstMouseDownBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(true, MouseEventTypeEnum.MOUSE_DOWN, mouseEventMatcher, action);
    }

    public void registerMouseDownBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(false, MouseEventTypeEnum.MOUSE_DOWN, mouseEventMatcher, action);
    }

    public void unregisterMouseDownBinding(IMouseEventMatcher mouseEventMatcher) {
        unregisterMouseBinding(MouseEventTypeEnum.MOUSE_DOWN, mouseEventMatcher);
    }

    public void registerFirstSingleClickBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(true, MouseEventTypeEnum.MOUSE_SINGLE_CLICK, mouseEventMatcher, action);
    }

    public void registerSingleClickBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(false, MouseEventTypeEnum.MOUSE_SINGLE_CLICK, mouseEventMatcher, action);
    }

    public void unregisterSingleClickBinding(IMouseEventMatcher mouseEventMatcher) {
        unregisterMouseBinding(MouseEventTypeEnum.MOUSE_SINGLE_CLICK, mouseEventMatcher);
    }

    public void registerFirstDoubleClickBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(true, MouseEventTypeEnum.MOUSE_DOUBLE_CLICK, mouseEventMatcher, action);
    }

    public void registerDoubleClickBinding(IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        registerMouseBinding(false, MouseEventTypeEnum.MOUSE_DOUBLE_CLICK, mouseEventMatcher, action);
    }

    public void unregisterDoubleClickBinding(IMouseEventMatcher mouseEventMatcher) {
        unregisterMouseBinding(MouseEventTypeEnum.MOUSE_DOUBLE_CLICK, mouseEventMatcher);
    }

    private void registerMouseBinding(boolean first, MouseEventTypeEnum mouseEventType, IMouseEventMatcher mouseEventMatcher, IMouseAction action) {
        LinkedList<MouseBinding> mouseEventBindings = mouseBindingsMap.get(mouseEventType);
        if (mouseEventBindings == null) {
            mouseEventBindings = new LinkedList<MouseBinding>();
            mouseBindingsMap.put(mouseEventType, mouseEventBindings);
        }
        if (first) {
            mouseEventBindings.addFirst(new MouseBinding(mouseEventMatcher, action));
        } else {
            mouseEventBindings.addLast(new MouseBinding(mouseEventMatcher, action));
        }
    }

    private void unregisterMouseBinding(MouseEventTypeEnum mouseEventType, IMouseEventMatcher mouseEventMatcher) {
        LinkedList<MouseBinding> mouseBindings = mouseBindingsMap.get(mouseEventType);
        for (MouseBinding mouseBinding : mouseBindings) {
            if (mouseBinding.getMouseEventMatcher().equals(mouseEventMatcher)) {
                mouseBindings.remove(mouseBinding);
                return;
            }
        }
    }

    private enum MouseEventTypeEnum {

        MOUSE_DOWN, MOUSE_MOVE, MOUSE_SINGLE_CLICK, MOUSE_DOUBLE_CLICK
    }
}
