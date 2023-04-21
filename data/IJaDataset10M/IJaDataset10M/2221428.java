package org.jactr.eclipse.ui.editor.assist;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.text.IRegion;
import org.jactr.eclipse.core.ast.Support;
import org.jactr.eclipse.core.comp.ICompilationUnit;
import org.jactr.eclipse.ui.editor.markers.ASTPosition;
import org.jactr.eclipse.ui.editor.markers.PositionMarker;
import org.jactr.io.antlr3.builder.JACTRBuilder;
import org.jactr.io.antlr3.misc.ASTSupport;

public class ProposalGenerator {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(ProposalGenerator.class);

    /**
   * generate a series of proposals that are relevant to the position
   * 
   * @param position
   * @param compilationUnit
   * @return
   */
    public static Map<String, CommonTree> generateProposals(ASTPosition position, int offset, ICompilationUnit compilationUnit) {
        Map<String, CommonTree> rtn = new TreeMap<String, CommonTree>();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Checking " + position.getNode());
        int type = position.getNode().getType();
        CommonTree detail = Support.getNodeOfOffset(position.getNode(), offset, position.getBase());
        if (detail == null) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Could not get any more detail");
            return rtn;
        }
        int dType = detail.getType();
        if (type == JACTRBuilder.SLOT) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Trying to find slot proposals");
            rtn.putAll(generateSlotProposals(position, offset, compilationUnit));
        } else if (type == JACTRBuilder.BUFFER) {
            if (dType == JACTRBuilder.BUFFER) rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.CHUNK));
            if (dType == JACTRBuilder.NAME) rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.BUFFER));
        } else if (type == JACTRBuilder.CHUNK || type == JACTRBuilder.CHUNK_TYPE) {
            if (dType == JACTRBuilder.PARENT) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("returning all the chunk types as a proposal");
                rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.CHUNK_TYPE));
            }
        } else if (type == JACTRBuilder.ADD_ACTION || type == JACTRBuilder.MATCH_CONDITION || type == JACTRBuilder.QUERY_CONDITION || type == JACTRBuilder.REMOVE_ACTION || type == JACTRBuilder.MODIFY_ACTION) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Got a condition/action " + detail);
            if (dType == JACTRBuilder.CHUNK_IDENTIFIER || dType == JACTRBuilder.CHUNK_TYPE_IDENTIFIER || dType == JACTRBuilder.IDENTIFIER || dType == JACTRBuilder.VARIABLE) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("adding all chunktypes and possibly variables");
                rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.CHUNK_TYPE));
                if (hasParentType(position, JACTRBuilder.PRODUCTION)) rtn.putAll(getVariablesBefore(position));
            } else if (dType == JACTRBuilder.NAME) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("returning all the buffer names");
                rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.BUFFER));
            }
        } else if (type == JACTRBuilder.OUTPUT_ACTION) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("returning variables for output");
            rtn.putAll(getVariablesBefore(position));
        }
        return rtn;
    }

    protected static boolean hasParentType(ASTPosition position, int type) {
        ASTPosition parent = getParentOfType(position, type);
        return parent != null;
    }

    public static ASTPosition getParentOfType(ASTPosition position, int type) {
        ASTPosition parent = position;
        while (parent != null && parent.getNode().getType() != type) parent = parent.getParent();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Returning " + parent + " as parent of " + position + " of " + type);
        return parent;
    }

    public static Map<String, CommonTree> getVariablesBefore(ASTPosition position) {
        ASTPosition parent = getParentOfType(position, JACTRBuilder.PRODUCTION);
        if (parent == null) return Collections.EMPTY_MAP;
        Collection<CommonTree> variables = ASTSupport.getTrees(parent.getNode(), JACTRBuilder.VARIABLE);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Found " + variables.size() + " variables under " + parent.getNode());
        Map<String, CommonTree> rtn = new TreeMap<String, CommonTree>();
        Iterator<CommonTree> itr = variables.iterator();
        while (itr.hasNext()) {
            CommonTree node = itr.next();
            int[] offsets = Support.getTreeOffsets(node, position.getBase());
            if (offsets[0] < position.getOffset()) rtn.put(node.getText(), node);
        }
        return rtn;
    }

    public static Map<String, CommonTree> generateSlotProposals(ASTPosition slotPosition, int offset, ICompilationUnit compilationUnit) {
        Map<String, CommonTree> rtn = new HashMap<String, CommonTree>();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Generating slot proposals");
        CommonTree detail = Support.getNodeOfOffset(slotPosition.getNode(), offset, slotPosition.getBase());
        if (detail.getType() == JACTRBuilder.NAME || detail.getType() == JACTRBuilder.SLOT) {
            String chunkTypeName = guessChunkType(slotPosition, compilationUnit);
            if (chunkTypeName != null) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Guessed chunkType : " + chunkTypeName);
                return getSlots(chunkTypeName, compilationUnit);
            }
        } else {
            rtn.putAll(getVariablesBefore(slotPosition));
            rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.CHUNK));
            rtn.putAll(compilationUnit.getNamedContents(JACTRBuilder.CHUNK_TYPE));
        }
        return rtn;
    }

    public static Map<String, CommonTree> getSlots(String chunkType, ICompilationUnit compUnit) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Getting slots of " + chunkType);
        if (chunkType == null) return Collections.EMPTY_MAP;
        CommonTree chunkTypeNode = compUnit.getNamedContents(JACTRBuilder.CHUNK_TYPE).get(chunkType.toLowerCase());
        if (chunkTypeNode == null) return Collections.EMPTY_MAP;
        Map<String, CommonTree> rtn = ASTSupport.getMapOfTrees(chunkTypeNode, JACTRBuilder.SLOT);
        CommonTree parentNode = null;
        for (CommonTree child : Support.getAllChildren(chunkTypeNode)) if (child.getType() == JACTRBuilder.PARENT) parentNode = child;
        if (parentNode != null) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("getting parent's slots " + parentNode);
            rtn.putAll(getSlots(parentNode.getText(), compUnit));
        }
        return rtn;
    }

    public static String guessChunkType(ASTPosition position, ICompilationUnit compUnit) {
        int type = position.getNode().getType();
        if (type == JACTRBuilder.SLOT) {
            ASTPosition parent = position.getParent();
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Got slot, delegating to parent");
            return guessChunkType(parent, compUnit);
        } else if (type == JACTRBuilder.IDENTIFIER || type == JACTRBuilder.CHUNK_IDENTIFIER || type == JACTRBuilder.CHUNK_TYPE_IDENTIFIER) {
            String text = position.getNode().getText().toLowerCase();
            CommonTree child = compUnit.getNamedContents(JACTRBuilder.CHUNK).get(text);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Trying chunks : " + child);
            if (child != null) return ASTSupport.getFirstDescendantWithType(child, JACTRBuilder.PARENT).getText();
            child = compUnit.getNamedContents(JACTRBuilder.CHUNK_TYPE).get(text);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Trying chunk types " + child);
            if (child != null) return ASTSupport.getName(child);
        } else if (type == JACTRBuilder.CHUNK) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("got chunk, returning our parent");
            return ASTSupport.getFirstDescendantWithType(position.getNode(), JACTRBuilder.PARENT).getText();
        } else if (type == JACTRBuilder.CHUNK_TYPE) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("got a chunktype, returning our name");
            return ASTSupport.getName(position.getNode());
        } else if (type != JACTRBuilder.QUERY_CONDITION) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Checking production type thingy? " + position.getNode());
            String variableName = null;
            for (CommonTree child : Support.getAllChildren(position.getNode())) {
                int childType = child.getType();
                if (childType == JACTRBuilder.IDENTIFIER || childType == JACTRBuilder.CHUNK_IDENTIFIER || childType == JACTRBuilder.CHUNK_TYPE_IDENTIFIER) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("found identifier, recursing");
                    IRegion region = PositionMarker.getTreeSpan(child, position.getBase());
                    ASTPosition cPos = new ASTPosition(region.getOffset(), region.getLength(), child);
                    position.addChild(cPos);
                    return guessChunkType(cPos, compUnit);
                } else if (childType == JACTRBuilder.VARIABLE) {
                    variableName = child.getText().toLowerCase();
                    variableName = variableName.substring(1, variableName.length());
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("got a variable, will try " + variableName);
                } else if (childType == JACTRBuilder.NAME) {
                    variableName = child.getText().toLowerCase();
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("Got a buffer name, will try " + variableName);
                }
            }
            if (variableName != null) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("variable : " + variableName);
                ASTPosition production = getParentOfType(position, JACTRBuilder.PRODUCTION);
                for (ASTPosition side : production.getChildren()) for (ASTPosition condition : side.getChildren()) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("Checking " + condition);
                    if (condition.getNode().getType() == JACTRBuilder.MATCH_CONDITION) {
                        String name = ASTSupport.getName(condition.getNode()).toLowerCase();
                        if (name.equals(variableName)) return guessChunkType(condition, compUnit);
                    }
                }
            }
        }
        return null;
    }
}
