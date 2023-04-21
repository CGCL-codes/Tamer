package org.bs.sm;

import org.bs.sm.outputers.XMLContext;
import org.bs.sm.outputers.dot.SMDotContext;
import org.bs.sm.outputers.dot.SMDotBuildError;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

/**
 * Because the code involved in writing transition dot is so complicated,
 * I put in in separate class
 * @author Boaz Nahum
 * @version WI VX.6, V12.0X, ADC V0.95
 */
class TransitionDotHelper {

    private TransitionDotHelper() {
    }

    static void writeDotData(XMLContext xmlContext, Element nodeOfSource, SMStateVertex source, SMBaseTrigger trigger, SMTransition transition) {
        final SMDotContext dotContext = xmlContext.getDotContext();
        if (dotContext == null) {
            return;
        }
        boolean sourceIsComplex = source.isComplex();
        int nBranches = transition.getN();
        SMStateDotInfo sourceDotInfo = source.getDotInfo();
        boolean isNary = nBranches > 1;
        @Nullable final SMGuard guard = transition.getGuard();
        if (isNary) {
            String conditionNodeName = "__dummy_condition_" + dotContext.getRandomUniqueID();
            SMStateDotInfo conditionDotInfo = new SMStateDotInfo(source, conditionNodeName, conditionNodeName, true);
            SMComplexState stateContainsCondition = SMStateVertex.firstStateContainsBothNoConcurrent(source, transition);
            String conditionDot = conditionNodeName + SMDotContext.concatenateAttributesWrap("label=\"C\"", "shape=circle", "fontsize=10", "margin=0", "width=0.2", "height=0.2") + ";";
            Element elementContainsCondition = SMDotContext.findElementOfParent(nodeOfSource, source, stateContainsCondition);
            if (elementContainsCondition == null) {
                throw new SMDefinitionError();
            }
            String comment = "Dummy node to support transition " + buildTransitionDescription(source, transition, trigger);
            dotContext.addDotComment(elementContainsCondition, comment);
            dotContext.addDotElement(elementContainsCondition, conditionDot);
            String connectorLabel = getArrowLabel(trigger, guard);
            writeDotUnaryTransition(dotContext, nodeOfSource, sourceDotInfo, sourceIsComplex, conditionDotInfo, false, false, false, connectorLabel);
            sourceIsComplex = false;
            trigger = null;
            sourceDotInfo = conditionDotInfo;
        }
        for (int i = 0; i < nBranches; ++i) {
            String connectorLabel = null;
            if (!isNary) {
                connectorLabel = getArrowLabel(trigger, guard);
            } else {
                if (guard != null) {
                    String bn = guard.getBranchName(nBranches, i);
                    if (bn != null) {
                        connectorLabel = "[" + bn + "]";
                    }
                }
            }
            SMStateVertex target = transition.getBranchTarget(i);
            boolean targetIsComplex = target.isComplex();
            SMStateDotInfo targetDotInfo = target.getDotInfo();
            boolean targetIsInOrEqualSource = source.isStateContainsOrEq(target);
            boolean sourceIsInOrEqualTarget = target.isStateContainsOrEq(source);
            writeDotUnaryTransition(dotContext, nodeOfSource, sourceDotInfo, sourceIsComplex, targetDotInfo, targetIsComplex, targetIsInOrEqualSource, sourceIsInOrEqualTarget, connectorLabel);
        }
    }

    /**
     * Return ull if empty
     */
    @Nullable
    static String getArrowLabel(@Nullable SMBaseTrigger trigger, @Nullable SMGuard guard) {
        String name = "";
        if (trigger != null && !(trigger instanceof SystemTrigger)) {
            name = trigger.getName();
        }
        if (guard != null) {
            name += "[";
            String guardName = guard.getName();
            if (guardName != null) {
                name += guardName;
            }
            name += "]";
        }
        if (name.isEmpty()) {
            return null;
        } else {
            return name;
        }
    }

    static String buildTransitionDescription(SMStateVertex source, SMTransition transition, SMBaseTrigger trigger) {
        String s = source.getAbsName();
        s += " --";
        SMGuard guard = transition.getGuard();
        String arrow = getArrowLabel(trigger, guard);
        if (arrow != null) {
            s += " " + arrow + " ";
        }
        s += "--> {";
        int n = transition.getN();
        boolean first = true;
        for (int i = 0; i < n; ++i) {
            if (first) {
                first = false;
            } else {
                s += ", ";
            }
            if (guard != null) {
                String bn = guard.getBranchName(n, i);
                if (bn != null) {
                    s += "[" + bn + "]";
                }
            }
            s += transition.getBranchTarget(i).getAbsName();
        }
        s += "}";
        return s;
    }

    private static void writeDotUnaryTransition(SMDotContext dotContext, Element nodeOfSource, SMStateDotInfo sourceDotInfo, boolean sourceIsComplex, SMStateDotInfo targetDotInfo, boolean targetIsComplex, boolean targetIsInOrEqualSource, boolean sourceIsInOrEqualTarget, String connectorLabel) {
        if (sourceIsComplex) {
            if (targetIsInOrEqualSource && !dotContext.isSupportingClusterToClusterArrow()) {
                dotDummyCluster2SameCluster(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, connectorLabel);
            } else if (!targetIsInOrEqualSource && !dotContext.isSupportingNodeToClusterArrow()) {
                dotDummyCluster2X(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, connectorLabel);
            } else {
                dotRegularTransition(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, connectorLabel);
            }
        } else {
            if (targetIsComplex) {
                if (!dotContext.isSupportingNodeToClusterArrow()) {
                    dotDummyNode2Cluster(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, sourceIsInOrEqualTarget, connectorLabel);
                } else {
                    dotRegularTransition(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, connectorLabel);
                }
            } else {
                dotRegularTransition(dotContext, nodeOfSource, sourceDotInfo, targetDotInfo, connectorLabel);
            }
        }
    }

    /**
     * Do transition from node to cluster using dummy nodes in cluster or outside of it
     */
    private static void dotDummyNode2Cluster(SMDotContext dotContext, Element inNode, SMStateDotInfo source, SMStateDotInfo target, boolean sourceIsInOrEqualTarget, @Nullable String connectorLabel) {
        String sourceName = source.getDotNameAsSourceOfTransition(target);
        if (sourceIsInOrEqualTarget) {
            String targetName = dotCreateDummyNodeOutsideCluster(dotContext, target);
            String dotTran = dotContext.createDotTransition(sourceName, targetName, getDotDefaultTransitionAttributes(connectorLabel, false));
            addDotTransition(dotContext, inNode, dotTran, source, target);
            connectorLabel = null;
            sourceName = targetName;
        }
        String targetName = dotCreateDummyNodeInCluster(dotContext, target);
        String edgeAttributes = SMDotContext.concatenateAttributes("lhead=" + target.getDotName(), getDotDefaultTransitionAttributes(connectorLabel));
        String dotTran = dotContext.createDotTransition(sourceName, targetName, edgeAttributes);
        addDotTransition(dotContext, inNode, dotTran, source, target);
    }

    /**
     * Handle case when source is cluster and target is cluster or node.
     * Use dummy nodes in clusters
     */
    private static void dotDummyCluster2X(SMDotContext dotContext, Element inNode, SMStateDotInfo source, SMStateDotInfo target, @Nullable String connectorLabel) {
        String dummySourceName = dotCreateDummyNodeInCluster(dotContext, source);
        String edgeAttributes = "ltail=" + source.getDotName();
        edgeAttributes = SMDotContext.concatenateAttributes(edgeAttributes, getDotDefaultTransitionAttributes(connectorLabel));
        String targetName;
        if (target.isComplex() && !dotContext.isSupportingNodeToClusterArrow()) {
            targetName = dotCreateDummyNodeInCluster(dotContext, target);
            edgeAttributes = SMDotContext.concatenateAttributes(edgeAttributes, "lhead=" + target.getDotName());
        } else {
            targetName = target.getDotName();
        }
        String dotTran = dotContext.createDotTransition(dummySourceName, targetName, edgeAttributes);
        addDotTransition(dotContext, inNode, dotTran, source, target);
    }

    /**
     * This is the case were source and target in the same cluster, so wee need to put dummy target outside
     *
     * @param dotContext
     * @param inNode
     * @param source
     * @param target
     * @param connectorLabel
     */
    private static void dotDummyCluster2SameCluster(SMDotContext dotContext, Element inNode, SMStateDotInfo source, SMStateDotInfo target, @Nullable String connectorLabel) {
        String dummySourceName = dotCreateDummyNodeInCluster(dotContext, source);
        String intermediateDummyTargetName = dotCreateDummyNodeOutsideCluster(dotContext, source);
        String lTail = "ltail=" + source.getDotName();
        String edgeAttributes = SMDotContext.concatenateAttributes(lTail);
        edgeAttributes = SMDotContext.concatenateAttributes(edgeAttributes, getDotDefaultTransitionAttributes(connectorLabel, false));
        String intermediateTransition = dotContext.createDotTransition(dummySourceName, intermediateDummyTargetName, edgeAttributes);
        addDotTransition(dotContext, inNode, intermediateTransition, source, target);
        String targetName;
        edgeAttributes = "";
        if (target.isComplex()) {
            targetName = dotCreateDummyNodeInCluster(dotContext, target);
            edgeAttributes = SMDotContext.concatenateAttributes(edgeAttributes, "lhead=" + target.getDotName());
        } else {
            targetName = target.getDotName();
        }
        String outsideInTran = dotContext.createDotTransition(intermediateDummyTargetName, targetName, edgeAttributes);
        addDotTransition(dotContext, inNode, outsideInTran, source, target);
    }

    private static void dotRegularTransition(SMDotContext dotContext, Element inNode, SMStateDotInfo source, SMStateDotInfo target, @Nullable String connectorLabel) {
        String sourceName;
        sourceName = source.getDotNameAsSourceOfTransition(target);
        String edgeAttributes = getDotDefaultTransitionAttributes(connectorLabel);
        String targetName;
        targetName = target.getDotNameAsTargetOfTransition(source);
        String dotTran = dotContext.createDotTransition(sourceName, targetName, edgeAttributes);
        addDotTransition(dotContext, inNode, dotTran, source, target);
    }

    private static String getDotDefaultTransitionAttributes(@Nullable String connectorLabel) {
        return getDotDefaultTransitionAttributes(connectorLabel, true);
    }

    private static String getDotDefaultTransitionAttributes(@Nullable String label, boolean hasArrow) {
        String directionAttr;
        if (hasArrow) {
            directionAttr = SMDotContext.concatenateAttributes("dir=\"" + "forward" + "\"", "arrowhead=empty");
        } else {
            directionAttr = SMDotContext.concatenateAttributes("dir=none");
        }
        return SMDotContext.concatenateAttributes(directionAttr, getDotTransitionLabelAttributes(label));
    }

    /**
     * @return null if label is null
     */
    private static String getDotTransitionLabelAttributes(@Nullable String label) {
        if (label == null) {
            return null;
        } else {
            return SMDotContext.concatenateAttributes("label=\"" + label + "\"", "labelfontcolor=\"blue\"");
        }
    }

    /**
     * All transitions are added in root
     */
    private static void addDotTransition(SMDotContext dotContext, Element inNode, String dotTran, SMStateDotInfo actualSource, SMStateDotInfo actualTarget) {
        int transitionID = dotContext.getRandomUniqueID();
        final String s = "Transition #" + transitionID + ":" + actualSource.getTypeAndDotName() + "-->" + actualTarget.getTypeAndDotName();
        dotContext.addDotElement(inNode, "// See " + s);
        Element root = inNode.getOwnerDocument().getDocumentElement();
        dotContext.addDotElement(root, "//" + s);
        dotContext.addDotElement(root, dotTran);
    }

    /**
     * Add dummy node in cluster. The node is not actually added now, it will be added
     * at second pass
     *
     * @param dotContext
     * @param inCluster - Assume to complex state @return Name of dummy node add to cluster
     * @return name of dummy node
     */
    private static String dotCreateDummyNodeInCluster(SMDotContext dotContext, SMStateDotInfo inCluster) {
        String dummyNodeAttributes = getDummyNodeAttributes();
        String dummyName = createDummyNameInCluster(dotContext, inCluster);
        final String dummyNodeStatement = dummyName + SMDotContext.wrapAttributes(dummyNodeAttributes) + ";";
        dotContext.addDummyNodeToCluster(inCluster, dummyNodeStatement);
        return dummyName;
    }

    /**
     * Create dummy node outside inNode
     */
    private static String dotCreateDummyNodeOutsideCluster(SMDotContext dotContext, SMStateDotInfo clusterInfo) {
        if (clusterInfo.isFake()) {
            throw new SMDotBuildError("Can't handle fake cluster");
        }
        String actualName = clusterInfo.getDotName();
        String dummyName = "__dummy_" + dotContext.getRandomUniqueIDStr() + "_outdise_" + actualName;
        Element outsideSource = dotContext.findElementAssert(clusterInfo.getState().getParent());
        String dummyNodeAttributes = getDummyNodeAttributes();
        dotContext.addDotElement(outsideSource, dummyName + SMDotContext.wrapAttributes(dummyNodeAttributes) + ";");
        return dummyName;
    }

    private static String createDummyNameInCluster(SMDotContext dotContext, SMStateDotInfo cluster) {
        String actualSourceName = cluster.getDotName();
        return "__dummy_" + dotContext.getRandomUniqueIDStr() + "_in_" + actualSourceName;
    }

    private static String getDummyNodeAttributes() {
        String dummyNodeAttributes = SMDotContext.concatenateAttributes("label=\"\"", "shape=point", "style=invis");
        return dummyNodeAttributes;
    }
}
