package noty.ui.input;

import java.util.Vector;
import noty.configuration.AppConfig;
import noty.ui.cleffs.notes.GenericNote;
import noty.ui.input.buttons.KeyButtons;
import noty.ui.input.keyboard.KeyBoard;

public class InputHandler {

    public enum INPUTS {

        GENERIC_NOTES, KEYBOARD
    }

    private static InputDevice m_input_device = null;

    private static Vector<NoteEventListener> m_vNoteListeners = new Vector<NoteEventListener>();

    public static InputDevice setInput(INPUTS input) {
        switch(input) {
            case GENERIC_NOTES:
                m_input_device = new KeyButtons(0, 15);
                break;
            case KEYBOARD:
            default:
                m_input_device = new KeyBoard(7, 0);
                break;
        }
        m_input_device.setVisible(true);
        m_input_device.addNoteListener(new NoteEventListener() {

            public void PlayNote(GenericNote note) {
                for (int i = 0; i < m_vNoteListeners.size(); i++) ((NoteEventListener) m_vNoteListeners.get(i)).PlayNote(note);
            }
        });
        return m_input_device;
    }

    public static boolean currentDeviceIs(INPUTS inputCheck) {
        if (m_input_device == null) setInput(INPUTS.valueOf(AppConfig.getStr("default.input")));
        return (m_input_device.getDeviceType() == inputCheck);
    }

    public static InputDevice getCurrentInputDevice() {
        if (m_input_device == null) setInput(INPUTS.valueOf(AppConfig.getStr("default.input")));
        return m_input_device;
    }

    public static void addNoteListener(NoteEventListener listener) {
        m_vNoteListeners.addElement(listener);
    }

    public static void removeDataListener(NoteEventListener listener) {
        m_vNoteListeners.removeElement(listener);
    }
}
