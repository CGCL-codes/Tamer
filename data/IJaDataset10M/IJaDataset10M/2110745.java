package br.com.felix.easyclearcase.ui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import br.com.felix.easyclearcase.Comment;
import br.com.felix.easyclearcase.EasyClearCaseFile;

/**
 * Base class for a dialog that asks the user to input his comments about a file.
 * This class is used in the checkin and add to source control operations. 
 * */
public abstract class AbstractCommentDialog extends JDialog {

    private static final long serialVersionUID = 6900132158235419732L;

    private JPanel mainPanel;

    /**
	 * Label for the file being processed.
	 * */
    private JLabel lblProcessingFile;

    /**
	 * Component that will store the user's comment.
	 * */
    protected JTextArea txtaComment;

    /**
	 * TextArea to display the content of the file.
	 * */
    private JTextArea txtaFile;

    /**
	 * Ok button.
	 * */
    private JButton btnCommit;

    /**
	 * Skip file button.
	 * */
    private JButton btnSkip;

    /**
	 * Cancel all button. Used when a series of operations is in progress and the user decides to cancel all.
	 * */
    private JButton btnCancelAll;

    /**
	 * Apply to All checkbox. The user may signal the application to use the same comment thoughout the entire series of operations.
	 * */
    protected JCheckBox chkApplyToAll;

    /**
	 * Constructor.
	 * Initializes the UI. Sets the default behavior of the buttons to dispose the dialog.
	 * Default dialog layout is GridBagLayout.
	 * 
	 * @param parent parent component
	 * @param file target file of the operation
	 * @param commonComment the comment that is common to all operations. Will be used as default value.
	 * */
    public AbstractCommentDialog(JFrame parent, EasyClearCaseFile file, Comment commonComment) {
        super(parent);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        Dimension dimension = new Dimension(650, 580);
        this.setPreferredSize(dimension);
        this.setSize(dimension);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 10, 10, 10);
        JLabel lblFile = new JLabel("File:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblFile, gbc);
        lblProcessingFile = new JLabel("no file");
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(lblProcessingFile, gbc);
        JLabel lblComment = new JLabel("Comment:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblComment, gbc);
        chkApplyToAll = new JCheckBox("apply to all");
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(chkApplyToAll, gbc);
        txtaComment = new JTextArea();
        JScrollPane jsComentario = new JScrollPane(txtaComment);
        jsComentario.setPreferredSize(new Dimension(500, 60));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(jsComentario, gbc);
        gbc.gridwidth = 1;
        JLabel lblConteudo = new JLabel("File contents:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(lblConteudo, gbc);
        txtaFile = new JTextArea();
        Dimension dimensionArquivo = new Dimension(600, 300);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JScrollPane scrollPane = new JScrollPane(txtaFile);
        scrollPane.setSize(dimensionArquivo);
        scrollPane.setPreferredSize(dimensionArquivo);
        scrollPane.setAutoscrolls(true);
        mainPanel.add(scrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel(getOperationConfirmationTitle()), gbc);
        btnCommit = new JButton("Ok");
        btnSkip = new JButton("Skip");
        btnCancelAll = new JButton("Cancel");
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnCommit, gbc);
        buttonsPanel.add(btnSkip, gbc);
        buttonsPanel.add(btnCancelAll, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        mainPanel.add(buttonsPanel, gbc);
        if (commonComment != null && commonComment.getComment() != null) {
            String comment = commonComment.getComment();
            comment = comment.replaceAll("\\$\\{file\\}", file.getName());
            txtaComment.setText(comment);
        }
        Set<String> extensions = new HashSet<String>();
        extensions.add("txt");
        extensions.add("java");
        extensions.add("jsp");
        extensions.add("asp");
        extensions.add("cs");
        extensions.add("properties");
        extensions.add("xml");
        extensions.add("html");
        extensions.add("htm");
        extensions.add("aspx");
        extensions.add("log");
        lblProcessingFile.setText(file.getAbsolutePath());
        try {
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf('.') + 1);
            if (extensions.contains(extension.toLowerCase())) {
                FileReader fr = new FileReader(file.getFile());
                final BufferedReader br = new BufferedReader(fr);
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        String line;
                        StringBuilder strb = new StringBuilder();
                        try {
                            while ((line = br.readLine()) != null) {
                                strb.append(line).append("\n");
                            }
                            txtaFile.setText(strb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                };
                t.start();
            } else {
                txtaFile.setText("Binary file.");
            }
        } catch (FileNotFoundException e) {
            txtaFile.setText("File not found.");
        }
        MouseAdapter disposeMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                dispose();
            }
        };
        btnCommit.addMouseListener(disposeMouseAdapter);
        btnSkip.addMouseListener(disposeMouseAdapter);
        btnCancelAll.addMouseListener(disposeMouseAdapter);
    }

    /**
	 * Returns the question to be displayed to the user above the buttons.
	 * */
    protected abstract String getOperationConfirmationTitle();

    public static void main(String args[]) {
        JFrame jfr = new JFrame();
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CheckinDialog cd = new CheckinDialog(jfr, EasyClearCaseFile.fromFile(new File("d:\\log.log")), null);
        cd.setModal(true);
        jfr.setSize(new Dimension(1024, 768));
        jfr.setVisible(true);
        cd.setVisible(true);
    }

    /**
	 * Adds a mouse listener to the Ok Button.
	 * */
    public void addCommitMouseListener(MouseListener ml) {
        btnCommit.addMouseListener(ml);
    }

    /**
	 * Adds a mouse listener to the Cancel Button.
	 * */
    public void addCancelMouseListener(MouseListener ml) {
        btnCancelAll.addMouseListener(ml);
    }

    /**
	 * Adds a mouse listener to the Skip Button.
	 * */
    public void addSkipMouseListener(MouseListener ml) {
        btnSkip.addMouseListener(ml);
    }

    /**
	 * Returns the comment entered by the user.
	 * */
    public String getComment() {
        return txtaComment.getText();
    }

    /**
	 * Returns the willingness to use the entered comment for the tasks that follow (in a TaskGroup, for example).
	 * @returns true if the user has checked this option on the interface. False otherwise.
	 * */
    public boolean getApplyToAll() {
        return chkApplyToAll.isSelected();
    }
}
