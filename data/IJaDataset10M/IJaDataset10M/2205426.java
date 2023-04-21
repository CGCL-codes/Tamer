package net.sf.dozer.eclipse.plugin.editorpage.pages.composites;

import net.sf.dozer.eclipse.plugin.editorpage.utils.DozerUiUtils;
import net.sf.dozer.eclipse.plugin.editorpage.utils.DozerUtils;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

public class ConfigurationOptionComposite extends Composite {

    private IObservableValue relationshipType;

    private IObservableValue stopOnErrors;

    private IObservableValue wildcard;

    private IObservableValue trimStrings;

    private IObservableValue dateFormat;

    private IObservableValue beanFactory;

    private IObservableValue mappingType;

    private IObservableValue mapNull;

    private IObservableValue mapEmpty;

    private IObservableValue mapId;

    public ConfigurationOptionComposite(Composite parent, FormToolkit toolkit, boolean isMapping, IDOMModel model) {
        super(parent, SWT.NULL);
        this.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        this.setLayout(layout);
        dateFormat = DozerUiUtils.createLabelText(this, "ConfigSection.dateformat");
        beanFactory = DozerUiUtils.createLabelClassBrowse(this, "ConfigSection.beanfactory", DozerUtils.getBeanFactoryInterfaceName(model), false);
        DozerUiUtils.createLabel(this, "ConfigSection.relationshiptype");
        Composite multiComboComposite = toolkit.createComposite(this);
        TableWrapLayout multiComboLayout = new TableWrapLayout();
        multiComboLayout.numColumns = isMapping ? 3 : 1;
        multiComboLayout.bottomMargin = 0;
        multiComboLayout.horizontalSpacing = 5;
        multiComboLayout.leftMargin = 0;
        multiComboLayout.rightMargin = 0;
        multiComboLayout.topMargin = 0;
        multiComboComposite.setLayout(multiComboLayout);
        TableWrapData td = new TableWrapData();
        multiComboComposite.setLayoutData(td);
        relationshipType = DozerUiUtils.createCombo(multiComboComposite, "ConfigSection.relationshiptype", new String[] { "", "cumulative", "non-cumulative" });
        DozerUiUtils.createLabel(this, "ConfigSection.stoponerrors");
        Composite comboComposite = toolkit.createComposite(this);
        TableWrapLayout comboLayout = new TableWrapLayout();
        comboLayout.numColumns = isMapping ? 9 : 5;
        comboLayout.bottomMargin = 0;
        comboLayout.horizontalSpacing = 5;
        comboLayout.leftMargin = 0;
        comboLayout.rightMargin = 0;
        comboLayout.topMargin = 0;
        comboComposite.setLayout(comboLayout);
        td = new TableWrapData();
        comboComposite.setLayoutData(td);
        stopOnErrors = DozerUiUtils.createCombo(comboComposite, "ConfigSection.stoponerrors", new String[] { "", "true", "false" });
        wildcard = DozerUiUtils.createLabelCombo(comboComposite, "ConfigSection.wildcard", new String[] { "", "true", "false" });
        trimStrings = DozerUiUtils.createLabelCombo(comboComposite, "ConfigSection.trimstrings", new String[] { "", "true", "false" });
        if (isMapping) {
            mappingType = DozerUiUtils.createLabelCombo(multiComboComposite, "MappingSection.mappingtype", new String[] { "", "one-way", "bi-directional" });
            mapNull = DozerUiUtils.createLabelCombo(comboComposite, "MappingSection.mapnull", new String[] { "", "true", "false" });
            mapEmpty = DozerUiUtils.createLabelCombo(comboComposite, "MappingSection.mapempty", new String[] { "", "true", "false" });
            mapId = DozerUiUtils.createLabelText(this, "MappingSection.mapid");
        }
    }

    public IObservableValue getRelationshipType() {
        return relationshipType;
    }

    public IObservableValue getStopOnErrors() {
        return stopOnErrors;
    }

    public IObservableValue getWildcard() {
        return wildcard;
    }

    public IObservableValue getTrimStrings() {
        return trimStrings;
    }

    public IObservableValue getDateFormat() {
        return dateFormat;
    }

    public IObservableValue getBeanFactory() {
        return beanFactory;
    }

    public IObservableValue getMappingType() {
        return mappingType;
    }

    public IObservableValue getMapNull() {
        return mapNull;
    }

    public IObservableValue getMapEmpty() {
        return mapEmpty;
    }

    public IObservableValue getMapId() {
        return mapId;
    }
}
