package gleam.annotationdiffgui.gui;

import gleam.annotationdiffgui.AnnotationDiffGUI;
import gleam.annotationdiffgui.AnnotationDiffGUIException;
import gleam.annotationdiffgui.DocserviceConnection;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ConnectToAnnDiffDialog extends ConnectDialog {

    private JTextField docserviceUrl;

    private JTextField documentId;

    private static final String CONNECTION_TITLE = "Annotation Diff Service";

    /**
   * When autoconnection mode is true the dialog is folded.
   */
    private boolean autoconnect;

    private JButton buttonDetails;

    private JLabel docserviceLabel;

    private JLabel documentIdLabel;

    private ConnectToAnnDiffDialog dialogInstance = this;

    public ConnectToAnnDiffDialog(Frame owner, boolean autoconnect) {
        super(owner, CONNECTION_TITLE, true);
        initGUI();
        hookupEvents();
        if (autoconnect) {
            docserviceUrl.setText(AnnotationDiffGUI.getProperties().getProperty(DOCSERVICE_URL_PARAMETER_NAME));
            documentId.setText(AnnotationDiffGUI.getProperties().getProperty(DOC_ID_PARAMETER_NAME));
            connect();
        }
    }

    /**
	 * Constructs all needed GUI elements of this dialog.
	 */
    protected void initGUI() {
        JDialog.setDefaultLookAndFeelDecorated(true);
        setResizable(true);
        docserviceLabel = new JLabel("Document Service URL");
        docserviceUrl = new JTextField();
        documentIdLabel = new JLabel("Document ID");
        documentId = new JTextField();
        if (AnnotationDiffGUI.getConnection() != null && AnnotationDiffGUI.getConnection() instanceof DocserviceConnection) {
            docserviceUrl.setText(((DocserviceConnection) AnnotationDiffGUI.getConnection()).getDocserviceUrlString());
            documentId.setText(((DocserviceConnection) AnnotationDiffGUI.getConnection()).getDocId());
        } else {
            docserviceUrl.setText(AnnotationDiffGUI.getProperties().getProperty(DOCSERVICE_URL_PARAMETER_NAME));
            documentId.setText(AnnotationDiffGUI.getProperties().getProperty(DOC_ID_PARAMETER_NAME, ""));
        }
        docserviceUrl.setToolTipText("Document Service URL to connect to");
        documentId.setText(AnnotationDiffGUI.getProperties().getProperty(DOC_ID_PARAMETER_NAME, ""));
        buttonConnect = new JButton("Connect");
        buttonConnect.setToolTipText("Connect to Service");
        buttonCancel = new JButton("Cancel");
        buttonCancel.setToolTipText("Cancel");
        buttonDetails = new JButton("Details");
        buttonDetails.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonDetails.setVisible(false);
                docserviceLabel.setVisible(true);
                docserviceUrl.setVisible(true);
                documentIdLabel.setVisible(true);
                documentId.setVisible(true);
                buttonConnect.setVisible(true);
                dialogInstance.pack();
            }
        });
        statusMessage = new JLabel(AnnotationDiffGUI.getConnectionStatus());
        statusMessage.setFont(statusMessage.getFont().deriveFont(Font.PLAIN, 9.0F));
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        this.getContentPane().add(docserviceLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        this.getContentPane().add(docserviceUrl, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        this.getContentPane().add(documentIdLabel, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        this.getContentPane().add(documentId, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        this.getContentPane().add(statusMessage, c);
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        this.getContentPane().add(buttonConnect, c);
        c.gridx = 1;
        c.gridy = 5;
        this.getContentPane().add(buttonCancel, c);
        if (autoconnect) {
            docserviceLabel.setVisible(false);
            docserviceUrl.setVisible(false);
            documentIdLabel.setVisible(false);
            documentId.setVisible(false);
            buttonConnect.setVisible(false);
        } else {
            buttonDetails.setVisible(false);
        }
        this.pack();
        this.setLocation(Math.min(getToolkit().getScreenSize().width - getWidth(), getOwner().getX() + getOwner().getWidth() / 2 - getWidth() / 2), Math.min(getToolkit().getScreenSize().height - getHeight(), getOwner().getY() + getOwner().getHeight() / 2 - getHeight() / 2));
        this.setResizable(false);
    }

    /**
	 * Adds event handling for this dialog and its elements.
	 */
    protected void hookupEvents() {
        buttonConnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escPressed");
        getRootPane().getActionMap().put("escPressed", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                }
            }
        });
        documentId.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterPressed");
        documentId.getActionMap().put("enterPressed", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        documentId.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connect();
                }
            }
        });
    }

    /**
	 * Performs connection to the server.
	 */
    protected void connect() {
        buttonConnect.setEnabled(false);
        connectAsync(this.docserviceUrl.getText().trim(), this.documentId.getText().trim());
    }

    /**
	 * Starts connection process asynchronously.
	 */
    protected void connectAsync(final String docserviceUrlString, final String docId) {
        Runnable connectRun = new Runnable() {

            public void run() {
                DocserviceConnection connection = null;
                try {
                    try {
                        statusMessage.setText("Closing current connection...");
                        AnnotationDiffGUI.closeConnection();
                    } catch (AnnotationDiffGUIException e) {
                        handleError(new AnnotationDiffGUIException("An error occured while closing current connection.\n\n" + e.getClass().getName() + " occured. Message:\n" + e.getMessage(), e));
                    }
                    if (Thread.currentThread().isInterrupted() || Thread.currentThread() != connectionThread) {
                        buttonConnect.setEnabled(true);
                        return;
                    }
                    statusMessage.setText("Connecting to " + docserviceUrlString + " ...");
                    connection = new DocserviceConnection(docserviceUrlString, docId);
                    if (Thread.currentThread().isInterrupted() || Thread.currentThread() != connectionThread) {
                        buttonConnect.setEnabled(true);
                        return;
                    }
                    if (Thread.currentThread().isInterrupted() || Thread.currentThread() != connectionThread) {
                        buttonConnect.setEnabled(true);
                        return;
                    }
                    statusMessage.setText("Loading document...");
                    connection.loadDocument();
                    if (Thread.currentThread().isInterrupted() || Thread.currentThread() != connectionThread) {
                        if (connection != null) connection.cleanup();
                        buttonConnect.setEnabled(true);
                        return;
                    }
                    statusMessage.setText("Applying connection...");
                    setConnectionSafely(connection);
                    dispose();
                    return;
                } catch (AnnotationDiffGUIException e) {
                    handleError(e);
                    buttonConnect.setEnabled(true);
                } catch (Throwable e) {
                    if (connection != null) {
                        try {
                            connection.cleanup();
                        } catch (AnnotationDiffGUIException e1) {
                        }
                    }
                    handleError(new AnnotationDiffGUIException("Unexpected Internal error:\n\n" + e.getClass().getName() + " occured. Message:\n" + e.getMessage(), e));
                    buttonConnect.setEnabled(true);
                }
            }
        };
        connectionThread = new Thread(connectRun, "connectionThread");
        connectionThread.start();
    }
}
