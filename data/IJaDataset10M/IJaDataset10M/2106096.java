package com.threerings.gwt.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.threerings.gwt.util.Preconditions;
import com.threerings.gwt.util.Value;

/**
 * Handles the binding of various widget states to dynamic {@link Value}s.
 */
public class Bindings {

    /** Used to defer the creation of a widget. */
    public interface Thunk {

        /** Creates a deferred widget, adds it to the appropriate parent, and returns it. */
        Widget createWidget();
    }

    /**
     * Binds the enabledness state of the target widget to the supplied boolean value.
     */
    public static void bindEnabled(Value<Boolean> value, final FocusWidget... targets) {
        value.addListenerAndTrigger(new Value.Listener<Boolean>() {

            public void valueChanged(Boolean enabled) {
                for (FocusWidget target : targets) {
                    target.setEnabled(enabled);
                }
            }
        });
    }

    /**
     * Binds the visible state of the target widget to the supplied boolean value.
     */
    public static void bindVisible(Value<Boolean> value, final Widget... targets) {
        value.addListenerAndTrigger(new Value.Listener<Boolean>() {

            public void valueChanged(Boolean visible) {
                for (Widget target : targets) {
                    target.setVisible(visible);
                }
            }
        });
    }

    /**
     * Binds the visible state of a to-be-created widget to the supplied boolean value. The
     * supplied thunk will be called to create the widget (and add it to the appropriate parent)
     * the first time the value transitions to true, at which point the visiblity of the created
     * widget will be bound to subsequent changes of the value.
     */
    public static void bindVisible(final Value<Boolean> value, final Thunk thunk) {
        Preconditions.checkNotNull(thunk, "thunk");
        value.addListenerAndTrigger(new Value.Listener<Boolean>() {

            public void valueChanged(Boolean visible) {
                if (visible) {
                    value.removeListener(this);
                    bindVisible(value, thunk.createWidget());
                }
            }
        });
    }

    /**
     * Binds the hovered state of the supplied target widgets to the supplied boolean value. The
     * supplied value will be toggled to true when the mouse is hovering over any of the supplied
     * targets and false otherwise.
     */
    public static <T extends HasMouseOverHandlers & HasMouseOutHandlers> void bindHovered(final Value<Boolean> value, T... targets) {
        HoverHandler handler = new HoverHandler(value);
        for (T target : targets) {
            target.addMouseOverHandler(handler);
            target.addMouseOutHandler(handler);
        }
    }

    /**
     * Binds the specified toggle button to the supplied boolean value. The binding will work both
     * ways: interactive changes to the toggle button will update the value and changes to the
     * value will update the state of the toggle button.
     */
    public static void bindDown(final Value<Boolean> value, final ToggleButton toggle) {
        toggle.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                value.updateIf(toggle.isDown());
            }
        });
        value.addListenerAndTrigger(new Value.Listener<Boolean>() {

            public void valueChanged(Boolean value) {
                toggle.setDown(value);
            }
        });
    }

    /**
     * Binds the specified string value to the supplied text-having widget. The binding is one-way,
     * in that only changes to the value will be reflected in the text-having widget. It is
     * expected that no other changes will be made to the widget.
     */
    public static void bindLabel(final Value<String> value, final HasText target) {
        value.addListenerAndTrigger(new Value.Listener<String>() {

            public void valueChanged(String value) {
                if (!target.getText().equals(value)) {
                    target.setText(value);
                }
            }
        });
    }

    /**
     * Binds the contents of the supplied text box to the supplied string value. The binding is
     * multidirectional, i.e. changes to the value will update the text box and changes to the text
     * box will update the value. The value is updated on key up as well as on change so that both
     * keyboard initiated changes and non-keyboard initiated changes (paste) are handled.
     */
    public static void bindText(final Value<String> value, final TextBoxBase text) {
        text.addKeyUpHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                value.updateIf(((TextBoxBase) event.getSource()).getText());
            }
        });
        text.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                value.updateIf(((TextBoxBase) event.getSource()).getText());
            }
        });
        bindLabel(value, text);
    }

    /**
     * Returns a click handler that toggles the supplied boolean value when clicked.
     */
    public static ClickHandler makeToggler(final Value<Boolean> value) {
        Preconditions.checkNotNull(value, "value");
        return new ClickHandler() {

            public void onClick(ClickEvent event) {
                value.update(!value.get());
            }
        };
    }

    /**
     * Configures either `onStyle` or `offStyle` on the supplied target widgets depending on the
     * state of the supplied boolean `value`.
     *
     * @param onStyle the style name to be applied when the value is true, or null.
     * @param offStyle the style name to be applied when the value is false, or null.
     */
    public static void bindStateStyle(Value<Boolean> value, final String onStyle, final String offStyle, final Widget... targets) {
        value.addListenerAndTrigger(new Value.Listener<Boolean>() {

            public void valueChanged(Boolean value) {
                String add, remove;
                if (value) {
                    remove = offStyle;
                    add = onStyle;
                } else {
                    remove = onStyle;
                    add = offStyle;
                }
                for (Widget target : targets) {
                    if (remove != null) {
                        target.removeStyleName(remove);
                    }
                    if (add != null) {
                        target.addStyleName(add);
                    }
                }
            }
        });
    }

    protected static class HoverHandler implements MouseOverHandler, MouseOutHandler {

        public HoverHandler(Value<Boolean> value) {
            _value = Preconditions.checkNotNull(value, "value");
        }

        public void onMouseOver(MouseOverEvent event) {
            ++_hovered;
            _value.update(true);
        }

        public void onMouseOut(MouseOutEvent event) {
            --_hovered;
            _value.update(_hovered != 0);
        }

        protected Value<Boolean> _value;

        protected int _hovered;
    }
}
