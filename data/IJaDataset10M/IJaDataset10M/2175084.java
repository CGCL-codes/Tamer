package org.xnap.commons.gui.shortcut;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import junit.framework.TestCase;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.BackwardKillWordAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.CapitalizeWordAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.DowncaseWordAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.KillLineAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.KillRegionAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.KillRing;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.KillWordAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.SetMarkCommandAction;
import org.xnap.commons.gui.shortcut.EmacsKeyBindings.UpcaseWordAction;

public class EmacsKeyBindingsTest extends TestCase {

    public void testCapitalizeWordAction() {
        CapitalizeWordAction action = new EmacsKeyBindings.CapitalizeWordAction("");
        JTextField textField = new JTextField();
        textField.setText("Foo Bar");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("Foo Bar", textField.getText());
        assertEquals(3, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("Foo Bar", textField.getText());
        assertEquals(7, textField.getCaretPosition());
        textField.setText("mIXed caSe");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("Mixed caSe", textField.getText());
        assertEquals(5, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("Mixed CaSe", textField.getText());
        assertEquals(10, textField.getCaretPosition());
        textField.setText("Upper word");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("Upper word", textField.getText());
        assertEquals(5, textField.getCaretPosition());
        textField.setText("Single");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("Single", textField.getText());
        assertEquals(6, textField.getCaretPosition());
    }

    public void testDowncaseWordAction() {
        DowncaseWordAction action = new EmacsKeyBindings.DowncaseWordAction("");
        JTextField textField = new JTextField();
        textField.setText("lower case");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("lower case", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("lower case", textField.getText());
        assertEquals(10, textField.getCaretPosition());
        textField.setText("mIXed caSe");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("mixed caSe", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("mixed case", textField.getText());
        assertEquals(10, textField.getCaretPosition());
        textField.setText("Upper word");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("upper word", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        textField.setText("Single");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("single", textField.getText());
        assertEquals(6, textField.getCaretPosition());
    }

    public void testUpcaseWordAction() {
        UpcaseWordAction action = new EmacsKeyBindings.UpcaseWordAction("");
        JTextField textField = new JTextField();
        textField.setText("UPPER CASE");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("UPPER CASE", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("UPPER CASE", textField.getText());
        assertEquals(10, textField.getCaretPosition());
        textField.setText("mIXed caSe");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("MIXED caSe", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("MIXED CASE", textField.getText());
        assertEquals(10, textField.getCaretPosition());
        textField.setText("Upper word");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("UPPER word", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        textField.setText("Single");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("SINGLE", textField.getText());
        assertEquals(6, textField.getCaretPosition());
    }

    public void testKillWordAction() {
        KillWordAction action = new EmacsKeyBindings.KillWordAction("");
        JTextField textField = new JTextField();
        ((DefaultCaret) textField.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textField.setText("");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        textField.setText("mIXed caSe");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("caSe", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        perform(action, textField);
        assertEquals("", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        textField.setText("Upper word");
        textField.setCaretPosition(6);
        perform(action, textField);
        assertEquals("Upper ", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        textField.setText("Upper word foo");
        textField.setCaretPosition(8);
        perform(action, textField);
        assertEquals("Upper wofoo", textField.getText());
        assertEquals(8, textField.getCaretPosition());
    }

    public void testKillBackwardKillWordAction() {
        BackwardKillWordAction action = new BackwardKillWordAction("");
        JTextField textField = new JTextField();
        ((DefaultCaret) textField.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textField.setText("");
        textField.setCaretPosition(0);
        perform(action, textField);
        assertEquals("", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        textField.setText("mIXed caSe");
        textField.setCaretPosition(textField.getText().length());
        perform(action, textField);
        assertEquals("mIXed ", textField.getText());
        assertEquals(textField.getText().length(), textField.getCaretPosition());
        assertTrue(KillRing.getInstance().getRing().contains("mIXed "));
        perform(action, textField);
        assertEquals("", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        textField.setText("Upper word");
        textField.setCaretPosition(5);
        perform(action, textField);
        assertEquals(" word", textField.getText());
        assertEquals(0, textField.getCaretPosition());
        assertTrue(KillRing.getInstance().getRing().contains("Upper"));
        textField.setText("Upper word foo");
        textField.setCaretPosition(8);
        perform(action, textField);
        assertEquals("Upper rd foo", textField.getText());
        assertEquals(6, textField.getCaretPosition());
        assertTrue(KillRing.getInstance().getRing().contains("wo"));
    }

    public void testSetMarkCommandAction() {
        SetMarkCommandAction mark = new SetMarkCommandAction("");
        KillRegionAction kill = new KillRegionAction("");
        JTextField jtf = new JTextField();
        ((DefaultCaret) jtf.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jtf.setText("this is a line");
        jtf.setCaretPosition(0);
        perform(mark, jtf);
        assertTrue(SetMarkCommandAction.isMarked(jtf));
        assertEquals(0, SetMarkCommandAction.getCaretPosition());
        jtf.setCaretPosition(jtf.getDocument().getLength());
        perform(kill, jtf);
        assertEquals("", jtf.getText());
        assertTrue(KillRing.getInstance().getRing().contains("this is a line"));
        assertFalse(SetMarkCommandAction.isMarked(jtf));
        jtf.setText("this is no line");
        jtf.setCaretPosition("this is no".length());
        perform(mark, jtf);
        assertTrue(SetMarkCommandAction.isMarked(jtf));
        assertEquals(jtf.getCaretPosition(), SetMarkCommandAction.getCaretPosition());
        jtf.setCaretPosition(jtf.getCaretPosition() - 6);
        perform(kill, jtf);
        assertEquals("this line", jtf.getText());
        assertTrue(KillRing.getInstance().getRing().contains(" is no"));
        assertFalse(SetMarkCommandAction.isMarked(jtf));
    }

    public void testKillLineAction() {
        KillLineAction action = new EmacsKeyBindings.KillLineAction("");
        JTextField jtf = new JTextField();
        jtf.setText("this is a line");
        jtf.setCaretPosition(0);
    }

    private void perform(Action action, JTextField textField) {
        ActionEvent event = new ActionEvent(textField, 0, null);
        action.actionPerformed(event);
    }
}
