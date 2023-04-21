package edu.cmu.cs.nbeckman.wptranslator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import edu.cmu.cs.nbeckman.wptranslator.Document.OutputFormat;
import edu.cmu.cs.nbeckman.wptransltor.util.Box;
import edu.cmu.cs.nbeckman.wptransltor.util.Option;

/**
 * The main application and GUI. This is not meant to be a WebStart GUI,
 * nor is it meant to be an Applet, both of which I intend to create.
 * 
 * @author Nels E. Beckman
 */
public class MainGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String newline = System.getProperty("line.separator");

    private Option<File> inputFile = Option.none();

    private Option<File> outputFile = Option.none();

    private Box<Document.OutputFormat> outputFormat = Box.box(Document.OutputFormat.defaultFormat());

    private final JButton goButton = new JButton("Translate!");

    public MainGUI() {
        super(new BorderLayout());
        final JTextArea log = new JTextArea(5, 20);
        log.setEditable(false);
        JScrollPane logPane = new JScrollPane(log);
        add(logPane, BorderLayout.SOUTH);
        JButton openButton = new JButton("Select WordPerfect File...");
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(MainGUI.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    inputFile = Option.some(chooser.getSelectedFile());
                    log.append("Input file: " + inputFile.unwrap().getName() + newline);
                } else {
                    log.append("Open canceled by user." + newline);
                }
                log.setCaretPosition(log.getDocument().getLength());
                enableGoButtonIfNeccessary();
            }
        });
        JButton saveButton = new JButton("Select Output File...");
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(MainGUI.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    outputFile = Option.some(chooser.getSelectedFile());
                    log.append("Output file: " + outputFile.unwrap().getName() + newline);
                } else {
                    log.append("Open canceled by user." + newline);
                }
                log.setCaretPosition(log.getDocument().getLength());
                enableGoButtonIfNeccessary();
            }
        });
        JPanel filePane = new JPanel();
        filePane.add(openButton);
        filePane.add(saveButton);
        add(filePane, BorderLayout.NORTH);
        goButton.setEnabled(false);
        goButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Document doc = Document.createDocumentFromFile(inputFile.unwrap());
                    doc.writeDocument(outputFile.unwrap(), outputFormat.getItem());
                    log.append("Translated " + inputFile.unwrap().getName() + " to " + outputFormat.getItem() + newline);
                    log.setCaretPosition(log.getDocument().getLength());
                } catch (Exception exn) {
                    log.append("Error with translation: " + exn.toString() + newline);
                    log.setCaretPosition(log.getDocument().getLength());
                }
            }
        });
        add(goButton, BorderLayout.EAST);
        JPanel fileTypeButtons = createFileTypeButtons(this.outputFormat, log);
        add(fileTypeButtons, BorderLayout.CENTER);
    }

    /**
	 * Create radio buttons for the file type.
	 */
    private static JPanel createFileTypeButtons(final Box<OutputFormat> defaultFormat, final JTextArea log) {
        ButtonGroup group = new ButtonGroup();
        JPanel result = new JPanel(new GridLayout(0, 1));
        result.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Output Format"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        for (final OutputFormat format : OutputFormat.values()) {
            final JRadioButton radioButton = new JRadioButton(format.name() + " (" + format.getDescription() + ")");
            radioButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    defaultFormat.setItem(format);
                    log.append("Output Format: " + defaultFormat.getItem() + newline);
                    log.setCaretPosition(log.getDocument().getLength());
                }
            });
            radioButton.setSelected(format.equals(OutputFormat.defaultFormat()));
            group.add(radioButton);
            result.add(radioButton);
        }
        return result;
    }

    private final void enableGoButtonIfNeccessary() {
        if (this.inputFile.isSome() && this.outputFile.isSome()) {
            goButton.setEnabled(true);
            this.validate();
        }
    }

    public static void runGUI() {
        JFrame frame = new JFrame("WP4Transltor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainGUI());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                runGUI();
            }
        });
    }
}
