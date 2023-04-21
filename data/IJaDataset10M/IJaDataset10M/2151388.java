package launcher.contentpanel.runnerviews;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import launcher.application.Constants.IO;
import launcher.components.formattedtext.PropertySyntax;
import launcher.components.formattedtext.SyntaxColouringTextPane;
import launcher.runtime.ExecutingCommand;
import launcher.util.GraphicsUtils;

/**
 * An implementation of the {@link RunnerView} that uses a
 * {@link SyntaxColouringTextPane}
 * 
 * @author Ramon Servadei
 * @version $Revision: 1.1 $
 */
public class SyntaxColouringTextPaneRunnerView extends AbstractExecutingCommandView {

    private static final long serialVersionUID = 1L;

    private final SyntaxColouringTextPane textPane;

    public SyntaxColouringTextPaneRunnerView() {
        super();
        textPane = new SyntaxColouringTextPane(new PropertySyntax());
        textPane.setEditable(false);
        textPane.setWordWrap(false);
        textPane.setFont(GraphicsUtils.SYSTEM_FONT);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public void displayExecutingCommand(ExecutingCommand command) {
        executingCommand = command;
        textPane.setText(command.getOutput());
        textPane.setCaretPosition(textPane.getDocument().getLength());
        revalidate();
    }

    public void displayMessage(ExecutingCommand source, String message) {
        if (source.equals(executingCommand)) {
            Document doc = textPane.getDocument();
            if (doc != null) {
                try {
                    doc.insertString(doc.getLength(), message, null);
                    doc.insertString(doc.getLength(), IO.LS, null);
                } catch (BadLocationException e) {
                }
            }
            revalidate();
            repaint();
        }
    }

    public void setProcessTitle(ExecutingCommand command) {
    }
}
