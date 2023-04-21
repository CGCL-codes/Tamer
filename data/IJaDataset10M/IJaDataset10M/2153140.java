package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Represents an XML attribute which has possible built-in values, and can be modified by
 * an editable Combo box.
 * <p/>
 * See {@link UiTextAttributeNode} for more information.
 */
public class UiListAttributeNode extends UiAbstractTextAttributeNode {

    protected Combo mCombo;

    public UiListAttributeNode(ListAttributeDescriptor attributeDescriptor, UiElementNode uiParent) {
        super(attributeDescriptor, uiParent);
    }

    @Override
    public final void createUiControl(final Composite parent, IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        TextAttributeDescriptor desc = (TextAttributeDescriptor) getDescriptor();
        Label label = toolkit.createLabel(parent, desc.getUiName());
        label.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        SectionHelper.addControlTooltip(label, DescriptorsUtils.formatTooltip(desc.getTooltip()));
        int style = SWT.DROP_DOWN;
        mCombo = new Combo(parent, style);
        TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE);
        twd.maxWidth = 100;
        mCombo.setLayoutData(twd);
        fillCombo();
        setTextWidgetValue(getCurrentValue());
        mCombo.addModifyListener(new ModifyListener() {

            /**
             * Sent when the text is modified, whether by the user via manual
             * input or programmatic input via setText().
             */
            public void modifyText(ModifyEvent e) {
                onComboChange();
            }
        });
        mCombo.addSelectionListener(new SelectionAdapter() {

            /** Sent when the text is changed from a list selection. */
            @Override
            public void widgetSelected(SelectionEvent e) {
                onComboChange();
            }
        });
        mCombo.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                mCombo = null;
            }
        });
    }

    protected void fillCombo() {
        String[] values = getPossibleValues(null);
        if (values == null) {
            AdtPlugin.log(IStatus.ERROR, "FrameworkResourceManager did not provide values yet for %1$s", getDescriptor().getXmlLocalName());
        } else {
            for (String value : values) {
                mCombo.add(value);
            }
        }
    }

    /**
     * Get the list values, either from the initial values set in the attribute
     * or by querying the framework resource parser.
     *
     * {@inheritDoc}
     */
    @Override
    public String[] getPossibleValues(String prefix) {
        AttributeDescriptor descriptor = getDescriptor();
        UiElementNode uiParent = getUiParent();
        String attr_name = descriptor.getXmlLocalName();
        String element_name = uiParent.getDescriptor().getXmlName();
        String nsPrefix = "";
        if (SdkConstants.NS_RESOURCES.equals(descriptor.getNamespaceUri())) {
            nsPrefix = "android:";
        } else if (XmlnsAttributeDescriptor.XMLNS_URI.equals(descriptor.getNamespaceUri())) {
            nsPrefix = "xmlns:";
        }
        attr_name = nsPrefix + attr_name;
        String[] values = null;
        if (descriptor instanceof ListAttributeDescriptor && ((ListAttributeDescriptor) descriptor).getValues() != null) {
            values = ((ListAttributeDescriptor) descriptor).getValues();
        }
        if (values == null) {
            UiElementNode uiNode = getUiParent();
            AndroidEditor editor = uiNode.getEditor();
            AndroidTargetData data = editor.getTargetData();
            if (data != null) {
                UiElementNode grandParentNode = uiParent.getUiParent();
                String greatGrandParentNodeName = null;
                if (grandParentNode != null) {
                    UiElementNode greatGrandParentNode = grandParentNode.getUiParent();
                    if (greatGrandParentNode != null) {
                        greatGrandParentNodeName = greatGrandParentNode.getDescriptor().getXmlName();
                    }
                }
                values = data.getAttributeValues(element_name, attr_name, greatGrandParentNodeName);
            }
        }
        return values;
    }

    @Override
    public String getTextWidgetValue() {
        if (mCombo != null) {
            return mCombo.getText();
        }
        return null;
    }

    @Override
    public final boolean isValid() {
        return mCombo != null;
    }

    @Override
    public void setTextWidgetValue(String value) {
        if (mCombo != null) {
            mCombo.setText(value);
        }
    }

    /**
     * Handles Combo change, either from text edit or from selection change.
     * <p/>
     * Simply mark the attribute as dirty if it really changed.
     * The container SectionPart will collect these flag and manage them.
     */
    private void onComboChange() {
        if (!isInInternalTextModification() && !isDirty() && mCombo != null && getCurrentValue() != null && !mCombo.getText().equals(getCurrentValue())) {
            setDirty(true);
        }
    }
}
