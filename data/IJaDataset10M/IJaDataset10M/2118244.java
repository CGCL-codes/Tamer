package net.taylor.mda.properties.views;

import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesService;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.UMLPackage;

public class OpaqueExpressionLanguageSection extends AbstractPropertySection {

    private CCombo languageCombo;

    private Element umlElement;

    private ModifyListener listener = new ModifyListener() {

        public void modifyText(ModifyEvent arg0) {
            IPropertySource properties = getPropertySource(umlElement);
            int index = languageCombo.getSelectionIndex();
            Object v = languageCombo.getItem(index);
            properties.setPropertyValue(UMLPackage.Literals.OPAQUE_EXPRESSION__LANGUAGE, v);
        }
    };

    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        Assert.isTrue(selection instanceof IStructuredSelection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        Assert.isTrue(input instanceof Element);
        this.umlElement = (Element) input;
    }

    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;
        languageCombo = getWidgetFactory().createCCombo(composite);
        languageCombo.add("EL");
        languageCombo.add("Java");
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        languageCombo.setLayoutData(data);
        languageCombo.addModifyListener(listener);
        CLabel labelLabel = getWidgetFactory().createCLabel(composite, "Language:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(languageCombo, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(languageCombo, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);
    }

    public void refresh() {
        languageCombo.removeModifyListener(listener);
        IPropertySource properties = getPropertySource(umlElement);
        Object value = properties.getPropertyValue(UMLPackage.Literals.OPAQUE_EXPRESSION__LANGUAGE);
        if (value != null) {
            if (value.toString().contains("Java")) {
                languageCombo.select(1);
            } else {
                languageCombo.select(0);
            }
        }
        languageCombo.addModifyListener(listener);
    }

    protected IPropertySource getPropertySource(Object o) {
        return PropertiesService.getInstance().getPropertySource(o);
    }
}
