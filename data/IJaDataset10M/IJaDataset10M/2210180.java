package com.halware.nakedide.eclipse.ext.annot.descriptors;

import com.halware.nakedide.eclipse.ext.annot.ast.MultipleValueAnnotationSingleMemberEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptor;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;

/**
 * Maintains <tt>@Searchable#repository</tt> annotation member.
 */
public class SearchableMemberRepositoryMetadataDescriptor extends MetadataDescriptor {

    public SearchableMemberRepositoryMetadataDescriptor() {
        super("S#repository", new MultipleValueAnnotationSingleMemberEvaluatorAndModifier("org.nakedobjects.applib.annotation.Searchable", "repository", MetadataDescriptorKind.CLASS_NAME));
    }
}
