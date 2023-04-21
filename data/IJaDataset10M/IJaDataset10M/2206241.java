package uk.ac.reload.straker.editors.learningdesign.shared;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import uk.ac.reload.straker.StrakerPlugin;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModelListenerAdapter;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.editors.learningdesign.LearningDesignEditor;
import uk.ac.reload.straker.ui.widgets.BannerCLabel;

/**
 * Blank Editor Panel with a Title and message
 * 
 * @author Phillip Beauvoir
 * @version $Id: BlankPanel.java,v 1.2 2006/07/10 11:50:32 phillipus Exp $
 */
public class BlankPanel extends Composite {

    /**
     * Banner
     */
    private BannerCLabel _banner;

    /**
     * Label
     */
    private Label _label;

    /**
     * Optional component we are displaying info for
     */
    private LD_DataComponent _component;

    /**
     * Constructor
     */
    public BlankPanel(LearningDesignEditor ldEditor, Composite parent) {
        super(parent, SWT.NULL);
        GridLayout layout = new GridLayout(1, true);
        setLayout(layout);
        _banner = new BannerCLabel(this, "");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        _banner.setLayoutData(gd);
        _label = new Label(this, SWT.WRAP);
        gd = new GridData(GridData.FILL_BOTH);
        _label.setLayoutData(gd);
        ldEditor.getLearningDesignDataModel().addIDataModelListener(new DataModelListenerAdapter() {

            public void componentChanged(DataComponent component) {
                if (_component == component) {
                    _banner.setText(((LD_DataComponent) component).getTitleMessage());
                }
            }
        });
    }

    /**
     * Set the Data Component
     * @param component
     */
    public void setDataComponent(LD_DataComponent component) {
        if (component != null) {
            Image image = null;
            String imageName = component.getImageName();
            if (imageName != null) {
                image = StrakerPlugin.getDefault().getImage(imageName);
            }
            setTitleMessage(component.getTitleMessage(), component.getDescriptionMessage(), image);
        }
        _component = component;
    }

    /**
     * Just set the title, message and icon without reference to a Data Component
     * The Data Component will be set to null.
     * @param title
     * @param description
     * @param image
     */
    public void setTitleMessage(String title, String description, Image image) {
        _banner.setText(title);
        _banner.setImage(image);
        _label.setText(description);
        _component = null;
    }

    public void dispose() {
        super.dispose();
        _banner.dispose();
        _banner = null;
        _label = null;
        _component = null;
    }
}
