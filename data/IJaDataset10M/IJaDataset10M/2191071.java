package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.eclipse.builder.editors.common.EncodingLabelProvider;
import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptor;
import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptorUtil;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Editor for base URL policies (asset groups).
 */
public class BaseURLPolicyEditor extends BasicPolicyEditor {

    protected PolicyType getPolicyType() {
        return PolicyType.BASE_URL;
    }

    protected Map getComboDescriptors() {
        return ComboDescriptorUtil.getBaseLocationDescriptors();
    }

    protected PropertyDescriptor[] getAttributeDescriptors() {
        BeanClassDescriptor bcd = (BeanClassDescriptor) PolicyModel.MODEL_DESCRIPTOR.getTypeDescriptorStrict(BaseURLPolicyBuilder.class);
        return new PropertyDescriptor[] { bcd.getPropertyDescriptor(PolicyModel.BASE_URL), bcd.getPropertyDescriptor(PolicyModel.BASE_LOCATION) };
    }
}
