package org.cumt.view.utils;

import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class TextEditorTester extends JFrame {

    public TextEditorTester() {
        setContentPane(new TextEditor());
        setSize(300, 300);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new TextEditorTester().setVisible(true);
    }
}
