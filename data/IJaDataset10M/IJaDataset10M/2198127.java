package net.sf.daro.timetracker.viewer;

import javax.swing.JComponent;
import javax.swing.JTextField;
import net.sf.daro.core.viewer.AbstractValidatingPMViewer;
import net.sf.daro.core.viewer.AbstractViewer;
import net.sf.daro.timetracker.domain.Project;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author rohe
 */
public class ProjectViewer extends AbstractValidatingPMViewer<Project> {

    /**
	 * identifier field
	 */
    private JTextField identifierField;

    /**
	 * name field
	 */
    private JTextField nameField;

    /**
	 * Creates a new ProjectViewer.
	 * 
	 * @param bean
	 *            the backing bean
	 */
    public ProjectViewer(Project bean) {
        super(bean);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractViewer#createComponent()
	 */
    @Override
    protected JComponent createComponent() {
        initComponents();
        initComponentBindings();
        initComponentAnnotations();
        initEventHandling();
        FormLayout layout = new FormLayout("pref, 3dlu, MIN(150dlu;pref):grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, getResourceBundle());
        builder.setDefaultDialogBorder();
        return builder.getPanel();
    }

    private void initComponents() {
        identifierField = new JTextField();
        identifierField.setName("IdentifierField");
        identifierField.setColumns(17);
        nameField = new JTextField();
        nameField.setName("NameField");
        nameField.setColumns(17);
    }

    private void initComponentBindings() {
        Bindings.bind(identifierField, getPresentationModel().getModel("identifier"), true);
        Bindings.bind(nameField, getPresentationModel().getModel("name"), true);
    }

    private void initComponentAnnotations() {
    }

    private void initEventHandling() {
    }
}
