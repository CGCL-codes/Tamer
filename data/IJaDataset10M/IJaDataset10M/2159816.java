package quizcards;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.UndoableEditListener;

public class TextExampleSide extends Side {

    private static SoundField left_example_field = new SoundField();

    private static JPanel left_example_panel = new JPanel();

    private static SoundField left_field = new SoundField();

    private static JPanel left_panel = new JPanel();

    private static SoundField right_example_field = new SoundField();

    private static JPanel right_example_panel = new JPanel();

    private static SoundField right_field = new SoundField();

    private static JPanel right_panel = new JPanel();

    private static UndoableEditListener undoableEditListener = null;

    private String example = "";

    private SoundField example_field;

    private String example_sound_file = "";

    private SoundField field;

    private JPanel panel;

    private String sound_file = "";

    private String text = "";

    public TextExampleSide(boolean left_side) {
        if (left_side) {
            field = left_field;
            example_field = left_example_field;
            panel = left_panel;
        } else {
            field = right_field;
            example_field = right_example_field;
            panel = right_panel;
        }
    }

    public boolean find(String text) {
        return this.text.toLowerCase().indexOf(text) != -1 || example.toLowerCase().indexOf(text) != -1;
    }

    public JComponent get() {
        field.setText(text);
        field.setSound(sound_file);
        example_field.setText(example);
        example_field.setSound(example_sound_file);
        return panel;
    }

    public char getType() {
        return TEXT_EXAMPLE;
    }

    public static void init() {
        left_example_panel.setLayout(new BorderLayout());
        left_example_panel.add(new JLabel("Example", JLabel.CENTER), "North");
        left_example_panel.add(left_example_field, "Center");
        right_example_panel.setLayout(new BorderLayout());
        right_example_panel.add(new JLabel("Example", JLabel.CENTER), "North");
        right_example_panel.add(right_example_field, "Center");
        left_panel.setLayout(new GridLayout(2, 1));
        left_panel.add(left_field);
        left_panel.add(left_example_panel);
        right_panel.setLayout(new GridLayout(2, 1));
        right_panel.add(right_field);
        right_panel.add(right_example_panel);
    }

    public boolean playSound() {
        return field.playSound();
    }

    public void read(int version) {
        String s;
        while (XML.readElement(element, contents)) {
            s = element.toString();
            if (s.equals("text")) text = contents.toString(); else if (s.equals("sound_file")) sound_file = contents.toString(); else if (s.equals("example")) example = contents.toString(); else example_sound_file = contents.toString();
        }
    }

    public void requestFocus() {
        field.requestFocus();
    }

    public boolean set() {
        boolean changed = false;
        if (0 != text.compareTo(field.getText())) {
            text = field.getText();
            changed = true;
        }
        if (0 != sound_file.compareTo(field.soundFile)) {
            sound_file = field.soundFile;
            changed = true;
        }
        if (0 != example.compareTo(example_field.getText())) {
            example = example_field.getText();
            changed = true;
        }
        if (0 != example_sound_file.compareTo(example_field.soundFile)) {
            example_sound_file = example_field.soundFile;
            changed = true;
        }
        return changed;
    }

    public void setEditMode(boolean edit_mode) {
        field.setEditMode(edit_mode);
        example_field.setEditMode(edit_mode);
    }

    public void setFocusable(boolean focusable) {
        field.setFocusable(focusable);
        example_field.setFocusable(focusable);
    }

    public static void setFont(Font left_font, Font right_font) {
        left_field.setFont(left_font);
        left_example_field.setFont(left_font);
        right_field.setFont(right_font);
        right_example_field.setFont(right_font);
    }

    public void setSoundFlag(boolean sound_flag) {
        field.setSoundFlag(sound_flag);
        example_field.setSoundFlag(sound_flag);
    }

    public static void setUndoableEditListener(UndoableEditListener undoableEditListener) {
        if (TextExampleSide.undoableEditListener != null) {
            left_field.getDocument().removeUndoableEditListener(TextExampleSide.undoableEditListener);
            left_example_field.getDocument().removeUndoableEditListener(TextExampleSide.undoableEditListener);
            right_field.getDocument().removeUndoableEditListener(TextExampleSide.undoableEditListener);
            right_example_field.getDocument().removeUndoableEditListener(TextExampleSide.undoableEditListener);
        }
        TextExampleSide.undoableEditListener = undoableEditListener;
        left_field.getDocument().addUndoableEditListener(undoableEditListener);
        left_example_field.getDocument().addUndoableEditListener(undoableEditListener);
        right_field.getDocument().addUndoableEditListener(undoableEditListener);
        right_example_field.getDocument().addUndoableEditListener(undoableEditListener);
    }

    public void setVisible(boolean visible) {
        panel.setVisible(visible);
    }

    public void write() {
        XML.startElement("side", "type", "text_example");
        XML.writeElement("text", text);
        XML.writeElement("sound_file", sound_file);
        XML.writeElement("example", example);
        XML.writeElement("example_sound_file", example_sound_file);
    }
}
