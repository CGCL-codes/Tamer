package org.mitre.scap.xccdf.util.resolvers;

import org.mitre.scap.xccdf.util.*;
import gov.nist.checklists.xccdf.x11.*;
import java.util.ArrayList;

public class GroupPropertyExtensionResolver extends ItemPropertyExtensionResolver {

    private final GroupType extendingGroup, extendedGroup;

    public GroupPropertyExtensionResolver(GroupType extending, GroupType extended) {
        super(extending, extended);
        this.extendingGroup = extending;
        this.extendedGroup = extended;
    }

    @Override
    public void resolve() {
        super.resolve();
        PropertyExtensionResolver.getIdrefListTypeResolver().resolve(this.extendingGroup.getRequiresList(), this.extendedGroup.getRequiresList(), PropertyExtensionResolver.Action.APPEND);
        PropertyExtensionResolver.getIdrefTypeResolver().resolve(this.extendingGroup.getConflictsList(), this.extendedGroup.getConflictsList(), PropertyExtensionResolver.Action.APPEND);
        if (this.extendedGroup.isSetWeight() && !this.extendingGroup.isSetWeight()) {
            this.extendingGroup.setWeight(this.extendedGroup.getWeight());
        }
        if (this.extendedGroup.isSetSelected() && !this.extendingGroup.isSetSelected()) {
            this.extendingGroup.setSelected(this.extendedGroup.getSelected());
        }
        PropertyExtensionResolver.getHtmlTextWithSubTypeResolver().resolve(this.extendingGroup.getWarningList(), this.extendedGroup.getWarningList(), PropertyExtensionResolver.Action.OVERRIDE);
        PropertyExtensionResolver.getTextTypeResolver().resolve(this.extendingGroup.getQuestionList(), this.extendedGroup.getQuestionList(), PropertyExtensionResolver.Action.OVERRIDE);
        PropertyExtensionResolver.getHtmlTextWithSubTypeResolver().resolve(this.extendingGroup.getRationaleList(), this.extendedGroup.getRationaleList(), PropertyExtensionResolver.Action.OVERRIDE);
        PropertyExtensionResolver.getURIIdRefTypeResolver().resolve(this.extendingGroup.getPlatformList(), this.extendedGroup.getPlatformList(), PropertyExtensionResolver.Action.OVERRIDE);
    }
}
