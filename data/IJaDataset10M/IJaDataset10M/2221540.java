package ch.skyguide.tools.requirement.hmi;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ch.skyguide.fdp.common.hmi.ChainedFrame;
import ch.skyguide.fdp.common.hmi.IChainedFrameClosingListener;
import ch.skyguide.fdp.common.hmi.framework.form.IFormProxy;
import ch.skyguide.fdp.common.hmi.framework.form.TextComponentWrapper;
import ch.skyguide.fdp.common.hmi.input.UpperCaseDocumentDecorator;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.Requirement;
import ch.skyguide.tools.requirement.data.RequirementDomain;
import ch.skyguide.tools.requirement.hmi.CursorManager.CursorManagerId;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.requirement.hmi.openoffice.IOpenOfficeCanvas;
import ch.skyguide.tools.requirement.hmi.openoffice.MyOOoBean;
import ch.skyguide.tools.requirement.hmi.openoffice.OpenOfficeException;
import ch.skyguide.tools.requirement.hmi.openoffice.OpenOfficeManager;

@SuppressWarnings("serial")
public class SingleReadOnlyRequirementDomainPanel extends JPanel {

    private static final String HINT_DOMAIN_CODE = "hint.DomainCode";

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

    private AbstractRequirement domain;

    private final RequirementTool requirementTool;

    private final IOpenOfficeCanvas openOfficeCanvas;

    private final JLabel creationDateLabel = new JLabel(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_CREATED));

    private final JLabel headingLabel = new JLabel(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_HEADING));

    private final JCheckBox formatBox = new JCheckBox(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("label.FormattedAsRequirement"));

    private final IFormProxy<AbstractRequirement> formProxy = BeanManagerAndTableModelFactory.getInstance().createRequirementDomainFormProxy();

    public SingleReadOnlyRequirementDomainPanel(final RequirementTool tool, AbstractRequirement _requirement) {
        super(new GridBagLayout());
        requirementTool = tool;
        try {
            openOfficeCanvas = OpenOfficeManager.getContext().createCanvas();
        } catch (OpenOfficeException e) {
            throw new RuntimeException(e);
        }
        MyOOoBean oooBean = (MyOOoBean) openOfficeCanvas.getComponent();
        oooBean.setAllBarsVisible(false);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(creationDateLabel, CONSTRAINTS_LABEL);
        add(formProxy.getComponentAt("CreationDate"), CONSTRAINTS_DATE_FIELD);
        add(formProxy.getComponentAt("CodePrefix"), CONSTRAINTS_CODE_PREFIX);
        final JTextField codeField = (JTextField) formProxy.getComponentAt("Code");
        TextComponentWrapper.setTextWidth(codeField, "MMMMM");
        codeField.setToolTipText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(HINT_DOMAIN_CODE));
        new UpperCaseDocumentDecorator(codeField);
        add(codeField, CONSTRAINTS_CODE_FIELD);
        add(headingLabel, CONSTRAINTS_LABEL);
        add(formProxy.getComponentAt("Heading"), CONSTRAINTS_HEADING_FIELD);
        add(formatBox, CONSTRAINTS_FORMAT_BOX);
        add(openOfficeCanvas.getComponent(), CONSTRAINTS_DESCRIPTION_FIELD);
        openOfficeCanvas.getComponent().setMinimumSize(new Dimension(100, 100));
        setDomain(_requirement);
    }

    protected void linkToFrame(final ChainedFrame frame) {
        frame.addChainedFrameClosingListener(new IChainedFrameClosingListener() {

            public void askPermissionToClose(WindowEvent _event) {
                dispose();
            }

            private void dispose() {
                openOfficeCanvas.dispose();
            }
        });
    }

    protected void setDomain(AbstractRequirement _domain) {
        final CursorManagerId oldCursor = requirementTool.getCursorManager().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            domain = _domain;
            if (domain == null) {
                return;
            }
            try {
                formProxy.setObject(domain);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
            formatBox.setVisible(domain instanceof RequirementDomain);
            if (domain instanceof Requirement) {
                headingLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_REQUIREMENT));
            } else {
                headingLabel.setText(BeanManagerAndTableModelFactory.getInstance().getTranslatedText(LABEL_HEADING));
                if (domain instanceof RequirementDomain) {
                    final RequirementDomain trueDomain = (RequirementDomain) domain;
                    formatBox.setSelected(trueDomain.getFormattedAsRequirement());
                    formatBox.setEnabled(false);
                }
            }
            formProxy.setEnabled(false);
            formProxy.getComponentAt("CreationDate").setEnabled(false);
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
            openOfficeCanvas.loadText(domain.getDescription().getData(), false);
        } catch (OpenOfficeException e) {
            throw new RuntimeException(e);
        } finally {
            requirementTool.getCursorManager().restore(oldCursor);
        }
    }

    public boolean hasTextArea() {
        return true;
    }
}
