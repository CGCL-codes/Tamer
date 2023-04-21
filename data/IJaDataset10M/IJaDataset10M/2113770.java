package com.bbn.vessel.author.logicEditor.editor.searcher;

import com.bbn.vessel.author.imspec.IMConstants;
import com.bbn.vessel.author.models.GroupNode;
import com.bbn.vessel.author.models.VesselNode;

/**
 * @author jostwald
 *matcher that finds nodes that are signed with the Instructional Mechanic group signature
 *and who have the specified authoring prop
 */
public class IMAuthoringPropMatcher implements SearchMatcher<VesselNode> {

    private final String propValToMatch;

    private final String propName;

    /**
     * @param propName name of authoring prop
     * @param propValue desired value of authoring prop
     */
    public IMAuthoringPropMatcher(String propName, String propValue) {
        this.propName = propName;
        this.propValToMatch = propValue;
    }

    @Override
    public boolean matches(VesselNode node) {
        if ((node.getSignature() == null) || (!node.getSignature().equals(IMConstants.GROUP_SIGNATURE))) return false;
        GroupNode groupNode = (GroupNode) node;
        final String nodePropVal = groupNode.getAuthoringProperty(propName);
        return (nodePropVal != null) && (nodePropVal.equals(propValToMatch));
    }
}
