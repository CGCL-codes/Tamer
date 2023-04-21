package org.auramp.ui.event;

import java.util.HashMap;
import com.trolltech.qt.QSignalEmitter.Signal0;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;

public class KeyEventDriver {

    private static HashMap<KeyInfo, Signal0> bindings = new HashMap<KeyInfo, Signal0>();

    public static void bind(KeyInfo key, Signal0 signal) {
        bindings.put(key, signal);
    }

    public static void handle(QKeyEvent e) {
        Signal0 signal = bindings.get(createKeyInfo(e));
        if (signal != null) {
            signal.emit();
        }
        e.accept();
    }

    public static KeyInfo getBoundKey(Signal0 signal) {
        for (KeyInfo key : bindings.keySet()) {
            if (bindings.get(key).equals(signal)) {
                return key;
            }
        }
        return null;
    }

    public static KeyInfo createKeyInfo(Qt.Key key, boolean alt, boolean shift, boolean ctrl) {
        return new KeyEventDriver().new KeyInfo(key, alt, shift, ctrl);
    }

    public static KeyInfo createKeyInfo(QKeyEvent e) {
        return createKeyInfo(Qt.Key.resolve(e.key()), e.modifiers().isSet(Qt.KeyboardModifier.AltModifier), e.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier), e.modifiers().isSet(Qt.KeyboardModifier.ControlModifier));
    }

    public static KeyInfo keyFromString(String s) {
        if (s.equals("null") || s.equals("none")) {
            return null;
        }
        boolean ctrl = false, shift = false, alt = false;
        String str[] = s.split("\\+");
        Qt.Key k = null;
        for (String st : str) {
            st = st.trim();
            if (st.equalsIgnoreCase("Ctrl")) {
                ctrl = true;
            } else if (st.equalsIgnoreCase("shift")) {
                shift = true;
            } else if (st.equalsIgnoreCase("alt")) {
                alt = true;
            } else {
                k = Qt.Key.valueOf(st);
            }
        }
        return createKeyInfo(k, alt, shift, ctrl);
    }

    public class KeyInfo {

        public Qt.Key key;

        public boolean alt;

        public boolean shift;

        public boolean ctrl;

        public KeyInfo(Qt.Key key, boolean alt, boolean shift, boolean ctrl) {
            this.key = key;
            this.alt = alt;
            this.shift = shift;
            this.ctrl = ctrl;
        }

        public int hashCode() {
            return key.value() + (alt ? 21 : 0) + (shift ? 31 : 0) + (ctrl ? 39 : 0);
        }

        public QKeySequence toSequence() {
            return new QKeySequence((ctrl ? Qt.KeyboardModifier.ControlModifier.value() : 0) + (shift ? Qt.KeyboardModifier.ShiftModifier.value() : 0) + (alt ? Qt.KeyboardModifier.AltModifier.value() : 0) + key.value());
        }

        public String toString() {
            String s = ctrl ? "Ctrl + " : "";
            s += shift ? "Shift + " : "";
            s += alt ? "Alt + " : "";
            s += "" + key;
            return s;
        }

        public boolean equals(Object o) {
            if (o instanceof KeyInfo) {
                KeyInfo t = KeyInfo.class.cast(o);
                if (t.key == key && t.alt == alt && t.shift == shift && t.ctrl == ctrl) {
                    return true;
                }
            }
            return false;
        }
    }
}
