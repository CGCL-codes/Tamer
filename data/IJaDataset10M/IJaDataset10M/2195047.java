package org.openthinclient.console.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openthinclient.common.model.DirectoryObject;
import org.openthinclient.console.DetailView;
import org.openthinclient.console.Messages;
import org.openthinclient.console.util.ChildValidator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * @author bohnerne
 */
public class DirObjectEditPanel extends JPanel {

    private final ChildValidator validator = new ChildValidator();

    private final ValidationResultModel vrm = new DefaultValidationResultModel();

    public DirObjectEditPanel(DetailView detailView) {
        setLayout(new FormLayout("p:g", "p, 3dlu, f:p:g, 3dlu, p, 3dlu"));
        final CellConstraints cc = new CellConstraints();
        final JComponent headerComponent = detailView.getHeaderComponent();
        if (null != headerComponent) {
            headerComponent.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getBackground().darker()), headerComponent.getBorder()));
            validator.addValidatorFrom(headerComponent);
            add(headerComponent, cc.xy(1, 1));
        }
        add(ValidationResultViewFactory.createReportIconAndTextPane(vrm), cc.xy(1, 5));
        final JComponent mainComponent = detailView.getMainComponent();
        validator.addValidatorFrom(mainComponent);
        add(mainComponent, cc.xy(1, 3));
        setPreferredSize(new Dimension(800, 600));
    }

    /**
	 * @param node
	 * @param dirObject
	 * @return
	 * 
	 */
    public boolean doEdit(DirectoryObject dirObject, Node node) {
        final JButton okButton = new JButton(Messages.getString("OK"));
        final JButton cancelButton = new JButton(Messages.getString("Cancel"));
        final String name = node.getDisplayName().replace("()", "").trim();
        final String simpleClassName = dirObject.getClass().getSimpleName();
        final String title = MessageFormat.format(Messages.getString("DirObjectEditPanel.title"), new Object[] { Messages.getString("types.singular." + simpleClassName), name });
        final DialogDescriptor descriptor = new DialogDescriptor(this, title, true, new Object[] { okButton, cancelButton }, okButton, DialogDescriptor.BOTTOM_ALIGN, null, null);
        doValidate(okButton);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setIconImage(Utilities.loadImage("org/openthinclient/console/icon.png", true));
        final PropertyChangeListener pcl = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                doValidate(okButton);
            }
        };
        dirObject.addPropertyChangeListener(pcl);
        dialog.setSize(830, 600);
        dialog.setPreferredSize(new Dimension(830, 600));
        dialog.pack();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((screenSize.width - dialog.getWidth()) / 2, (screenSize.height - dialog.getHeight()) / 2);
        dialog.setVisible(true);
        dialog.toFront();
        dirObject.removePropertyChangeListener(pcl);
        return descriptor.getValue() == okButton;
    }

    /**
	 * @param okButton
	 */
    private void doValidate(final JButton okButton) {
        final ValidationResult validate = validator.validate();
        okButton.setEnabled(!validate.hasErrors());
        DirObjectEditPanel.this.revalidate();
        vrm.setResult(validate);
    }
}
