package net.sf.traser.client.scenario;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.stream.XMLStreamException;
import net.sf.traser.client.ScenarioFunctions;
import net.sf.traser.client.scanner.IdentifierListener;
import net.sf.traser.common.ConfigInitializable;
import net.sf.traser.common.ConfigManager;
import net.sf.traser.common.Identifier;
import net.sf.traser.common.LabelManager;
import net.sf.traser.common.identification.Resolver;
import net.sf.traser.common.impl.ConfigManagerImpl;
import net.sf.traser.configtool.BackgroundProgress;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * Shows and sets the entering and leaving of lab items.
 * @author karnokd, 2007.12.17.
 * @version $Revision 1.0$
 */
public class LabItemsGUI extends TFrame implements ConfigInitializable, ActionListener, IdentifierListener, DocumentListener {

    /**
	 * The logger object.
	 */
    private static final Logger LOG = Logger.getLogger(LabItemsGUI.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = -6206565733275542168L;

    /**
	 * The scenario functions interface.
	 */
    private transient ScenarioFunctions functions;

    /** The labels. */
    private transient LabelManager labels;

    /**
	 * The query progressbar.
	 */
    private BackgroundProgress queryProgress;

    /** The query button. */
    private JButton query;

    /** The borrow item button. */
    private JButton borrowItem;

    /** The return item button. */
    private JButton returnItem;

    /**
	 * Constructor. Initializes the GUI.
	 */
    public LabItemsGUI() {
        super();
    }

    /** The item identifier field. */
    private JTextField item;

    /** The item identifier. */
    private Identifier itemId;

    /** The status label. */
    private JLabel status;

    /** The borrower name. */
    private JLabel borrower;

    /** The identifier resolver. */
    private Resolver resolver;

    /**
	 * Initialize the GUI.
	 */
    private void initialize() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container c = getContentPane();
        setTitle(labels.get("lab_items_title"));
        GroupLayout gl = new GroupLayout(c);
        c.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        queryProgress = new BackgroundProgress(this, getTitle(), labels.get("operation_in_progress"));
        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        JLabel itemLabel = new JLabel(labels.get("item_id"));
        item = new JTextField(30);
        item.setOpaque(true);
        item.setBackground(new Color(0xFF, 0xFF, 0xCC));
        item.getDocument().addDocumentListener(this);
        JLabel statusLabel = new JLabel(labels.get("status"));
        status = new JLabel();
        JLabel borrowerLabel = new JLabel(labels.get("borrower"));
        borrower = new JLabel();
        borrowItem = new JButton(labels.get("borrow"));
        borrowItem.addActionListener(this);
        returnItem = new JButton(labels.get("return"));
        returnItem.addActionListener(this);
        query = new JButton(labels.get("query"));
        query.addActionListener(this);
        gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER).addGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(Alignment.LEADING).addComponent(itemLabel).addComponent(statusLabel).addComponent(borrowerLabel)).addGroup(gl.createParallelGroup(Alignment.LEADING).addComponent(item).addComponent(status).addComponent(borrower)).addComponent(query)).addComponent(separator1).addGroup(gl.createSequentialGroup().addComponent(borrowItem).addComponent(returnItem)));
        gl.setVerticalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(itemLabel).addComponent(item).addComponent(query)).addComponent(separator1).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(statusLabel).addComponent(status)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(borrowerLabel).addComponent(borrower)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(borrowItem).addComponent(returnItem)));
        pack();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == query) {
            doQuery();
        } else if (e.getSource() == borrowItem) {
            doBorrow();
        } else if (e.getSource() == returnItem) {
            doReturn();
        }
    }

    /**
	 * Perform the name query operation.
	 */
    private void doQuery() {
        final Identifier fItemId = itemId;
        if (fItemId == null) {
            showErrorMessage(labels.get("enter_identifier"));
            return;
        }
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private RuntimeException fault;

            private boolean value;

            private String borrowerPerson;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    value = functions.isDeviceInLab(fItemId);
                    if (!value) {
                        borrowerPerson = functions.getDeviceByWhom(fItemId);
                    }
                } catch (RuntimeException ex) {
                    fault = ex;
                    LOG.log(Level.SEVERE, ex.toString(), ex);
                }
                return null;
            }

            @Override
            protected void done() {
                if (fault != null) {
                    showErrorMessage(fault.getMessage());
                } else {
                    if (value) {
                        status.setText(labels.get("status_in_lab"));
                        borrower.setText(null);
                        borrowItem.setEnabled(true);
                        returnItem.setEnabled(false);
                    } else {
                        status.setText(labels.get("status_borrowed"));
                        borrower.setText(borrowerPerson);
                        borrowItem.setEnabled(false);
                        returnItem.setEnabled(true);
                    }
                }
                queryProgress.closeDialog();
            }
        };
        worker.execute();
        queryProgress.openDialog();
    }

    /**
	 * Perform the name query operation.
	 */
    private void doBorrow() {
        final Identifier fItemId = itemId;
        if (fItemId == null) {
            showErrorMessage(labels.get("enter_identifier"));
            return;
        }
        final String borrowerStr = JOptionPane.showInputDialog(this, labels.get("enter_borrower"), getTitle(), JOptionPane.QUESTION_MESSAGE);
        if (borrowerStr == null) {
            return;
        }
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private RuntimeException fault;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    functions.setBorrowDevice(fItemId, borrowerStr);
                } catch (RuntimeException ex) {
                    fault = ex;
                    LOG.log(Level.SEVERE, ex.toString(), ex);
                }
                return null;
            }

            @Override
            protected void done() {
                if (fault != null) {
                    showErrorMessage(fault.getMessage());
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doQuery();
                        }
                    });
                }
                queryProgress.closeDialog();
            }
        };
        worker.execute();
        queryProgress.openDialog();
    }

    /**
	 * Perform the name query operation.
	 */
    private void doReturn() {
        final Identifier fItemId = itemId;
        if (fItemId == null) {
            showErrorMessage(labels.get("enter_identifier"));
            return;
        }
        final String returnerStr = JOptionPane.showInputDialog(this, labels.get("enter_returner"), getTitle(), JOptionPane.QUESTION_MESSAGE);
        if (returnerStr == null) {
            return;
        }
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private RuntimeException fault;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    functions.setReturnDevice(fItemId, returnerStr);
                } catch (RuntimeException ex) {
                    fault = ex;
                    LOG.log(Level.SEVERE, ex.toString(), ex);
                }
                return null;
            }

            @Override
            protected void done() {
                if (fault != null) {
                    showErrorMessage(fault.getMessage());
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doQuery();
                        }
                    });
                }
                queryProgress.closeDialog();
            }
        };
        worker.execute();
        queryProgress.openDialog();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void init(OMElement config, ConfigManager manager) {
        super.init(config, manager);
        functions = manager.get(ScenarioFunctions.class);
        resolver = manager.get(Resolver.class);
        labels = manager.get(LabelManager.LabelManagerProvider.class).getManager();
        initialize();
    }

    /**
	 * Example runnable.
	 * @param args the arguments.
	 */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                LabItemsGUI gui = new LabItemsGUI();
                ConfigManagerImpl mgr = new ConfigManagerImpl();
                try {
                    mgr.init(new StAXOMBuilder("conf/TraserClient.xml").getDocumentElement());
                    gui.init(null, mgr);
                    gui.setVisible(true);
                    gui.setLocationRelativeTo(null);
                } catch (XMLStreamException ex) {
                    LOG.log(Level.SEVERE, ex.toString(), ex);
                } catch (FileNotFoundException ex) {
                    LOG.log(Level.SEVERE, ex.toString(), ex);
                }
            }
        });
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void error(Throwable th) {
        if (SwingUtilities.isEventDispatchThread()) {
            showErrorMessage(th.getMessage());
        } else {
            final String thStr = th.toString();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    showErrorMessage(thStr);
                }
            });
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void notify(Identifier id) {
        if (SwingUtilities.isEventDispatchThread()) {
            item.setText(id.toString());
        } else {
            final String idStr = id.toString();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    item.setText(idStr);
                }
            });
        }
    }

    /**
	 * Show an error message dialog.
	 * @param message the message
	 */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void changedUpdate(DocumentEvent e) {
        if (item.getDocument() == e.getDocument()) {
            tryResolve(item);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void insertUpdate(DocumentEvent e) {
        if (item.getDocument() == e.getDocument()) {
            tryResolve(item);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void removeUpdate(DocumentEvent e) {
        if (item.getDocument() == e.getDocument()) {
            tryResolve(item);
        }
    }

    /**
	 * Try to resolve the given string as an identifier.
	 * If succeds, sets the itemId field and colors the given textfield to green.
	 * If the resolvation fails, colors the textfield light red.
	 * @param textField the textfield to indicate success, not null
	 */
    private void tryResolve(JTextField textField) {
        String s = textField.getText();
        itemId = resolver.tryResolve(s);
        if (itemId != null) {
            textField.setBackground(OK_COLOR);
        } else {
            textField.setBackground(BAD_COLOR);
        }
    }

    /** The OK coloring. */
    private static final Color OK_COLOR = new Color(0xCC, 0xFF, 0xCC);

    /** The BAD coloring. */
    private static final Color BAD_COLOR = new Color(0xFF, 0xCC, 0xCC);
}
