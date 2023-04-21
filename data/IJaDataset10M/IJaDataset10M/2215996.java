package com.volantis.mcs.runtime.policies.layout;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.runtime.layouts.ActivatedLayoutContent;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.asset.OldObjectCreator;

public class RuntimeLayoutCreator implements OldObjectCreator {

    public RepositoryObject createOldObject(ActivatedVariablePolicy policy, Variant variant, InternalDevice device) {
        ActivatedLayoutContent content = (ActivatedLayoutContent) variant.getContent();
        return new RuntimeLayoutAdapter(policy.getName(), content.getLayout(), content.getCompiledStyleSheet(), content.getContainerNameToFragments());
    }
}
