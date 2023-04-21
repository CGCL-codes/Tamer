package org.apache.myfaces.trinidadinternal.skin.parse;

import java.util.List;

/**
 * Object which represents the skin and skin-addition nodes in trinidad-skins.xml.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/ui/laf/xml/parse/SkinPropertyNode.java#0 $) $Date: 10-nov-2005.18:50:45 $
 */
public class SkinsNode {

    /**
   * 
   */
    public SkinsNode(List<SkinNode> skinNodes, List<SkinAdditionNode> skinAdditionNodes) {
        _skinAdditionNodes = skinAdditionNodes;
        _skinNodes = skinNodes;
    }

    /**
   */
    public List<SkinAdditionNode> getSkinAdditionNodes() {
        return _skinAdditionNodes;
    }

    /**
   * 
   */
    public List<SkinNode> getSkinNodes() {
        return _skinNodes;
    }

    private List<SkinAdditionNode> _skinAdditionNodes;

    private List<SkinNode> _skinNodes;
}
