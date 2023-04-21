package com.halware.nakedide.eclipse.ext.annot.action;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CellEditor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DebugMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DescribedAsMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.DisabledMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ExecutedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.ExplorationMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.HiddenMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberNameMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.MemberOrderMemberSequenceMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.descriptors.NamedMetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluator;
import com.halware.nakedide.eclipse.ext.annot.mdd.IMetadataDescriptorsProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

public final class NakedObjectActionsViewStandardDescriptorsProvider implements IMetadataDescriptorsProvider {

    public List<MetadataDescriptor> getDescriptors() {
        List<MetadataDescriptor> descriptors = new ArrayList<MetadataDescriptor>();
        descriptors.add(new MetadataDescriptor("Action", new IEvaluator() {

            public Object evaluate(Object object) {
                NakedObjectAction nakedObjectAction = (NakedObjectAction) object;
                return nakedObjectAction.getActionMethodName();
            }

            public MetadataDescriptorKind<? extends CellEditor> getKind() {
                return MetadataDescriptorKind.STRING;
            }
        }, null));
        descriptors.add(new MemberOrderMemberSequenceMetadataDescriptor());
        descriptors.add(new MemberOrderMemberNameMetadataDescriptor());
        descriptors.add(new DisabledMetadataDescriptor());
        descriptors.add(new ExecutedMetadataDescriptor());
        descriptors.add(new HiddenMetadataDescriptor());
        descriptors.add(new DebugMetadataDescriptor());
        descriptors.add(new ExplorationMetadataDescriptor());
        descriptors.add(new NamedMetadataDescriptor());
        descriptors.add(new DescribedAsMetadataDescriptor());
        return descriptors;
    }
}
