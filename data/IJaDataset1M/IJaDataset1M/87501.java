package ch.skyguide.tools.requirement.hmi;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import ch.skyguide.fdp.common.hmi.ChainedFrame;
import ch.skyguide.fdp.common.hmi.ClosingException;
import ch.skyguide.fdp.common.hmi.IChainedFrameClosingListener;
import ch.skyguide.fdp.common.hmi.framework.form.IFormProxy;
import ch.skyguide.fdp.common.hmi.framework.form.TextComponentWrapper;
import ch.skyguide.fdp.common.hmi.input.UpperCaseDocumentDecorator;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.Preferences;
import ch.skyguide.tools.requirement.data.Requirement;
import ch.skyguide.tools.requirement.data.RequirementDescription;
import ch.skyguide.tools.requirement.data.RequirementDomain;
import ch.skyguide.tools.requirement.data.Roles;
import ch.skyguide.tools.requirement.hmi.CursorManager.CursorManagerId;
import ch.skyguide.tools.requirement.hmi.enabler.EditingStateManager;
import ch.skyguide.tools.requirement.hmi.memento.FormatBoxMementoFactory;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.requirement.hmi.openoffice.IOpenOfficeCanvas;
import ch.skyguide.tools.requirement.hmi.openoffice.IOpenOfficeDocumentListener;
import ch.skyguide.tools.requirement.hmi.openoffice.OpenOfficeException;
import ch.skyguide.tools.requirement.hmi.openoffice.OpenOfficeManager;
import ch.skyguide.tools.usermgt.UserDescription;
import ch.skyguide.tools.usermgt.UserRoster;

@SuppressWarnings("serial")
public class RequirementDomainPanel extends JPanel implements IRequirementPanel {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private static final String HINT_DOMAIN_CODE = "hint.DomainCode";

    private static final String BUTTON_PREVIOUS = "button.Previous";

    private static final String BUTTON_NEXT = "button.Next";

    private static final String HINT_PREVIOUS_DESCRIPTION_BUTTON = "hintPreviousDescriptionButton";

    private static final String HINT_NEXT_DESCRIPTION_BUTTON = "hintNextDescriptionButton";

    private static final String HINT_SUPERSEDE_DESCRIPTION_BUTTON = "hintSupersedeDescriptionButton";

    private static final String HINT_RESET_TESTS_BUTTON = "hintResetTests";

    private static final String SUPERSEDE_DESCRIPTION = "button.SupersedeDescription";

    private static final String RESET_TESTS = "button.ResetTests";

    private static final String LABEL_CREATED = "label.Created";

    private static final String LABEL_MODIFIED = "label.Modified";

    private static final String LABEL_HEADING = "label.Heading";

    private static final String LABEL_REQUIREMENT = "label.Requirement";

    private static GridBagConstraints CONSTRAINTS_LABEL = new GridBagConstraints();

    static {
        CONSTRAINTS_LABEL.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_LABEL.insets.right = 5;
    }

    private static GridBagConstraints CONSTRAINTS_HEADING_FIELD = new GridBagConstraints();

    static {
        CONSTRAINTS_HEADING_FIELD.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_HEADING_FIELD.insets.bottom = 7;
        CONSTRAINTS_HEADING_FIELD.insets.top = 7;
        CONSTRAINTS_HEADING_FIELD.weightx = 1;
        CONSTRAINTS_HEADING_FIELD.fill = GridBagConstraints.HORIZONTAL;
        CONSTRAINTS_HEADING_FIELD.gridwidth = GridBagConstraints.REMAINDER;
    }

    private static GridBagConstraints CONSTRAINTS_CODE_PREFIX = new GridBagConstraints();

    static {
        CONSTRAINTS_CODE_PREFIX.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_CODE_PREFIX.gridwidth = 3;
    }

    private static GridBagConstraints CONSTRAINTS_CODE_FIELD = new GridBagConstraints();

    static {
        CONSTRAINTS_CODE_FIELD.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_CODE_FIELD.gridwidth = GridBagConstraints.REMAINDER;
    }

    private static GridBagConstraints CONSTRAINTS_DATE_FIELD = new GridBagConstraints();

    static {
        CONSTRAINTS_DATE_FIELD.anchor = GridBagConstraints.WEST;
        CONSTRAINTS_DATE_FIELD.insets.right = 5;
        CONSTRAINTS_DATE_FIELD.weightx = 1;
    }

    private static GridBagConstraints CONSTRAINTS_DESCRIPTION_FIELD = new GridBagConstraints();

    static {
        CONSTRAINTS_DESCRIPTION_FIELD.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_DESCRIPTION_FIELD.weightx = 1;
        CONSTRAINTS_DESCRIPTION_FIELD.weighty = 1;
        CONSTRAINTS_DESCRIPTION_FIELD.fill = GridBagConstraints.BOTH;
        CONSTRAINTS_DESCRIPTION_FIELD.gridwidth = GridBagConstraints.REMAINDER;
    }

    private static GridBagConstraints CONSTRAINTS_BUTTON_BAR = new GridBagConstraints();

    static {
        CONSTRAINTS_BUTTON_BAR.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_BUTTON_BAR.fill = GridBagConstraints.HORIZONTAL;
        CONSTRAINTS_BUTTON_BAR.weightx = 1;
        CONSTRAINTS_BUTTON_BAR.gridwidth = GridBagConstraints.REMAINDER;
    }

    private static GridBagConstraints CONSTRAINTS_RESET_TESTS_BUTTON = new GridBagConstraints();

    static {
        CONSTRAINTS_RESET_TESTS_BUTTON.anchor = GridBagConstraints.EAST;
        CONSTRAINTS_RESET_TESTS_BUTTON.weightx = 1;
        CONSTRAINTS_RESET_TESTS_BUTTON.gridwidth = GridBagConstraints.REMAINDER;
        CONSTRAINTS_RESET_TESTS_BUTTON.weighty = 1;
        CONSTRAINTS_RESET_TESTS_BUTTON.fill = GridBagConstraints.VERTICAL;
    }

    private static GridBagConstraints CONSTRAINTS_OTHER_BUTTON = new GridBagConstraints();

    static {
        CONSTRAINTS_OTHER_BUTTON.anchor = GridBagConstraints.CENTER;
        CONSTRAINTS_OTHER_BUTTON.weighty = 1;
        CONSTRAINTS_OTHER_BUTTON.fill = GridBagConstraints.VERTICAL;
    }

    private static GridBagConstraints CONSTRAINTS_VERSION_LABEL = new GridBagConstraints();

    static {
        CONSTRAINTS_VERSION_LABEL.anchor = GridBagConstraints.CENTER;
        CONSTRAINTS_VERSION_LABEL.weighty = 1;
        CONSTRAINTS_VERSION_LABEL.fill = GridBagConstraints.VERTICAL;
        CONSTRAINTS_VERSION_LABEL.insets.left = 5;
    }

    private static GridBagConstraints CONSTRAINTS_FORMAT_BOX = new GridBagConstraints();

    static {
        CONSTRAINTS_FORMAT_BOX.anchor = GridBagConstraints.WEST;
        CONSTRAINTS_FORMAT_BOX.gridwidth = GridBagConstraints.REMAINDER;
    }

    private final RequirementTool requirementTool;

    private final IFormProxy<AbstractRequirement> formProxy = BeanManagerAndTableModelFactory.getInstance().createRequirementDomainFormProxy();

    private final RequirementTreeModel requirementTreeModel;

    private DefaultMutableTreeNode node;

    private final EditingStateManager editingStateManager;

    private AbstractRequirement domain;

    private final IOpenOfficeCanvas openOfficeCanvas;

    private final JButton resetTestsButton = createResetTestsButton();

    private final JButton supersedeDescriptionButton = createSupersedeDescriptionButton();

    private final JButton nextDescriptionButton = createNextDescriptionButton();

    private final JButton previousDescriptionButton = createPreviousDescriptionButton();

    private final Stack<AbstractRequirement> historyStack = new Stack<AbstractRequirement>();

    private final JLabel versionLabel = new JLabel();

    private final JLabel creationDateLabel = new JLabel(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_CREATED));

    private final JLabel headingLabel = new JLabel(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_HEADING));

    private final JCheckBox formatBox = new JCheckBox(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("label.FormattedAsRequirement"));

    private boolean formatBoxIdle = true;

    public RequirementDomainPanel(RequirementTool tool, EditingStateManager _editingStateManager) {
        super(new GridBagLayout());
        requirementTool = tool;
        editingStateManager = _editingStateManager;
        try {
            openOfficeCanvas = OpenOfficeManager.getContext().createCanvas();
        } catch (OpenOfficeException e) {
            throw new RuntimeException(e);
        }
        editingStateManager.addComponent(resetTestsButton, Roles.AUTHOR, Roles.TEST_WRITER, Roles.TESTER);
        requirementTreeModel = requirementTool.getRequirementTreeModel();
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(creationDateLabel, CONSTRAINTS_LABEL);
        add(formProxy.getComponentAt("CreationDate"), CONSTRAINTS_DATE_FIELD);
        add(formProxy.getComponentAt("CodePrefix"), CONSTRAINTS_CODE_PREFIX);
        final JTextField codeField = (JTextField) formProxy.getComponentAt("Code");
        TextComponentWrapper.setTextWidth(codeField, "MMMMMMMMMM");
        codeField.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_DOMAIN_CODE));
        new UpperCaseDocumentDecorator(codeField);
        add(codeField, CONSTRAINTS_CODE_FIELD);
        add(headingLabel, CONSTRAINTS_LABEL);
        add(formProxy.getComponentAt("Heading"), CONSTRAINTS_HEADING_FIELD);
        initializeFormatBox();
        add(formatBox, CONSTRAINTS_FORMAT_BOX);
        add(openOfficeCanvas.getComponent(), CONSTRAINTS_DESCRIPTION_FIELD);
        add(createButtonBar(), CONSTRAINTS_BUTTON_BAR);
        openOfficeCanvas.getComponent().setMinimumSize(new Dimension(100, 100));
        formProxy.addPropertyChangeListener(new RequirementCodeVerifier<AbstractRequirement>(formProxy, tool));
        Preferences.addUserPreferenceChangeListener(new RequirementToolPreferenceChangeListener(this));
        openOfficeCanvas.addDocumentListener(new IOpenOfficeDocumentListener() {

            public void documentSaved() {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        changeDescription();
                    }
                });
            }
        });
        UserRoster.instance.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (domain != null) {
                    setDomain(domain);
                }
            }
        });
    }

    public Object getComponentAt(String componentName) {
        if ("Description".equals(componentName)) {
            return openOfficeCanvas;
        }
        return formProxy.getComponentAt(componentName);
    }

    private void initializeFormatBox() {
        final FormatBoxMementoFactory factory = new FormatBoxMementoFactory(requirementTool);
        formatBox.setVisible(false);
        formatBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                if (formatBoxIdle && domain instanceof RequirementDomain) {
                    factory.processChange((RequirementDomain) domain, event.getStateChange() == ItemEvent.SELECTED);
                }
            }
        });
    }

    protected void linkToFrame(final ChainedFrame frame) {
        frame.addChainedFrameClosingListener(new IChainedFrameClosingListener() {

            public void askPermissionToClose(WindowEvent _event) throws ClosingException {
                try {
                    requirementTreeModel.checkSaving(frame, isOpenOfficeDocumentModified());
                } catch (ClosingException e) {
                    if (domain != null) {
                        changeDescription();
                    }
                    throw e;
                }
                dispose();
            }

            private void dispose() {
                openOfficeCanvas.dispose();
            }
        });
    }

    /**
	 * @see ch.skyguide.tools.requirement.hmi.IRequirementPanel#setDomain(javax.swing.tree.DefaultMutableTreeNode,
	 *      ch.skyguide.tools.requirement.data.AbstractRequirement)
	 */
    public void setDomain(DefaultMutableTreeNode _node, AbstractRequirement _domain) {
        node = _node;
        historyStack.clear();
        setDomain(_domain);
    }

    protected void setDomain(AbstractRequirement _domain) {
        final CursorManagerId oldCursor = requirementTool.getCursorManager().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (domain != null && isOpenOfficeDocumentModified()) {
                changeDescription();
            }
            domain = _domain;
            if (domain == null) {
                return;
            }
            try {
                formProxy.setObject(domain);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to load object values for class RequirementProject", e);
            }
            formatBox.setVisible(domain instanceof RequirementDomain);
            final AbstractRequirement currentDomainVersion = (AbstractRequirement) node.getUserObject();
            final boolean editable = currentDomainVersion == domain && userIsAnAuthor() && !(currentDomainVersion).isInTrash() && editingStateManager.isEditable();
            if (domain instanceof Requirement) {
                headingLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_REQUIREMENT));
            } else {
                headingLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_HEADING));
                if (domain instanceof RequirementDomain) {
                    final RequirementDomain trueDomain = (RequirementDomain) domain;
                    formatBoxIdle = false;
                    try {
                        formatBox.setSelected(trueDomain.getFormattedAsRequirement());
                        formatBox.setEnabled(editable);
                    } finally {
                        formatBoxIdle = true;
                    }
                }
            }
            enableButtonBar();
            setVersionLabel(currentDomainVersion);
            formProxy.setEnabled(editable);
            formProxy.getComponentAt("CreationDate").setEnabled(false);
            resetTestsButton.setEnabled(editable);
            supersedeDescriptionButton.setEnabled(editable);
            if (domain.getCode() == null) {
                EventQueue.invokeLater(new FocusRequester(formProxy.getComponentAt("Code")));
            } else if (domain.getHeading() == null) {
                EventQueue.invokeLater(new FocusRequester(formProxy.getComponentAt("Heading")));
            }
            if (domain.getPreviousRequirement() == null) {
                creationDateLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_CREATED));
            } else {
                creationDateLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_MODIFIED));
            }
            openOfficeCanvas.loadText(domain.getDescription().getData(), editable);
        } catch (OpenOfficeException e) {
            throw new RuntimeException(e);
        } finally {
            requirementTool.getCursorManager().restore(oldCursor);
        }
    }

    private void setVersionLabel(AbstractRequirement currentDomainVersion) {
        int n = 0;
        int index = 0;
        AbstractRequirement previousDomain = currentDomainVersion;
        while (previousDomain != null) {
            n += 1;
            if (previousDomain == domain) {
                index = n;
            }
            previousDomain = previousDomain.getPreviousRequirement();
        }
        if (n > 1) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(n + 1 - index);
            buffer.append('/');
            buffer.append(n);
            versionLabel.setText(buffer.toString());
        } else {
            versionLabel.setText("");
        }
    }

    private boolean userIsAnAuthor() {
        final UserDescription user = UserRoster.instance.getCurrentUser();
        return user != null && user.hasRole(Roles.AUTHOR);
    }

    private void enableButtonBar() {
        final boolean b = domain != null;
        resetTestsButton.setEnabled(b);
        supersedeDescriptionButton.setEnabled(b);
        previousDescriptionButton.setEnabled(b && domain.getPreviousRequirement() != null);
        nextDescriptionButton.setEnabled(b && !historyStack.isEmpty());
    }

    protected void giveFocusBack(FocusEvent event) {
        if (event.getSource() instanceof JTextComponent) {
            final JTextComponent field = (JTextComponent) event.getSource();
            if (field.getText().trim().length() > 0) {
                if (event.getOppositeComponent() instanceof JTree) {
                    event.getOppositeComponent().requestFocus();
                }
            }
        }
    }

    private Container createButtonBar() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.add(supersedeDescriptionButton, CONSTRAINTS_OTHER_BUTTON);
        panel.add(previousDescriptionButton, CONSTRAINTS_OTHER_BUTTON);
        panel.add(nextDescriptionButton, CONSTRAINTS_OTHER_BUTTON);
        panel.add(versionLabel, CONSTRAINTS_VERSION_LABEL);
        panel.add(resetTestsButton, CONSTRAINTS_RESET_TESTS_BUTTON);
        return panel;
    }

    private JButton createSupersedeDescriptionButton() {
        final JButton button = new JButton(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(SUPERSEDE_DESCRIPTION));
        button.setName("SUPERSEDE");
        button.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_SUPERSEDE_DESCRIPTION_BUTTON));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent _e) {
                byte[] data;
                String text;
                try {
                    data = openOfficeCanvas.getStoredDocument();
                    text = openOfficeCanvas.getText();
                } catch (OpenOfficeException e) {
                    throw new RuntimeException(e);
                }
                requirementTreeModel.supersedeDescription(domain, new RequirementDescription(data, text));
                previousDescriptionButton.setEnabled(true);
                setDomain(domain);
            }
        });
        button.setEnabled(domain != null);
        return button;
    }

    private JButton createResetTestsButton() {
        final JButton button = new JButton(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(RESET_TESTS));
        button.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_RESET_TESTS_BUTTON));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent _e) {
                requirementTreeModel.notifyTestChanged(domain.resetTests());
            }
        });
        button.setEnabled(domain instanceof Requirement);
        return button;
    }

    private JButton createNextDescriptionButton() {
        final JButton button = new JButton(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(BUTTON_NEXT));
        button.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_NEXT_DESCRIPTION_BUTTON));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent _e) {
                if (!historyStack.isEmpty()) {
                    setDomain(historyStack.pop());
                }
            }
        });
        button.setEnabled(historyStack != null && !historyStack.isEmpty());
        return button;
    }

    private JButton createPreviousDescriptionButton() {
        final JButton button = new JButton(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(BUTTON_PREVIOUS));
        button.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_PREVIOUS_DESCRIPTION_BUTTON));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent _e) {
                if (domain.getPreviousRequirement() != null) {
                    historyStack.push(domain);
                    setDomain(domain.getPreviousRequirement());
                }
            }
        });
        button.setEnabled(domain != null && domain.getPreviousRequirement() != null);
        return button;
    }

    protected void changeDescription() {
        if (domain != null) {
            try {
                requirementTreeModel.setDescription(domain, new RequirementDescription(openOfficeCanvas.getStoredDocument(), openOfficeCanvas.getText()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void resetDomain() {
        if (domain != null) {
            if (node == null) {
                node = requirementTreeModel.getNode(domain);
            }
            setDomain(domain);
        }
    }

    public boolean hasPendingUpdates() {
        return isOpenOfficeDocumentModified();
    }

    private boolean isOpenOfficeDocumentModified() {
        boolean documentModified;
        try {
            documentModified = openOfficeCanvas.isDocumentModified();
        } catch (OpenOfficeException e) {
            logger.log(Level.WARNING, "isDocumentModified failed, considering document as modified", e);
            documentModified = true;
        }
        return documentModified;
    }

    public void savePendingUpdates() {
        if (domain != null && isOpenOfficeDocumentModified()) {
            final CursorManagerId oldCursor = requirementTool.getCursorManager().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                changeDescription();
                openOfficeCanvas.loadText(domain.getDescription().getData(), true);
            } catch (OpenOfficeException e) {
                throw new RuntimeException(e);
            } finally {
                requirementTool.getCursorManager().restore(oldCursor);
            }
        }
    }

    public boolean hasTextArea() {
        return true;
    }

    public void setFullScreen() {
        invalidate();
        final Component[] components = getComponents();
        for (Component component : components) {
            component.setVisible(component == openOfficeCanvas.getComponent());
        }
        revalidate();
        repaint();
    }

    public void setNonFullScreen() {
        invalidate();
        final Component[] components = getComponents();
        for (Component component : components) {
            component.setVisible(true);
        }
        revalidate();
        repaint();
    }
}
