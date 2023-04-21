package net.sf.jyntax.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import net.sf.jyntax.HighlightRule;
import net.sf.jyntax.SimpleSyntaxHighlighter;

public class ExampleFrame extends JFrame {

    public ExampleFrame() {
        super("Jyntax Demo Example");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTextComponent textComp = new JTextArea();
        textComp.setPreferredSize(new Dimension(200, 150));
        add(textComp, BorderLayout.CENTER);
        SimpleSyntaxHighlighter hl = new SimpleSyntaxHighlighter(textComp);
        HighlightRule rule = new HighlightRule("[a-z][0-9][A-Z]", "[0-9]");
        hl.addRule(rule);
        textComp.getDocument().addDocumentListener(hl);
        textComp.setText("s0A d3F g0L");
        pack();
    }

    public static void main(String[] args) {
        new ExampleFrame().setVisible(true);
    }
}
