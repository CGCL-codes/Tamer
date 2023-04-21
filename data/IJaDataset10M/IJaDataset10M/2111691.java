package com.simpledata.bc.uicomponents.compact;

/**
 * Compact tree visitor interface. This is the interface 
 * that all classes that want to visit the CompactTree 
 * must implement. 
 * 
 */
public interface CompactTreeVisitor {

    public void caseCompactBCNodeSingle(CompactBCNodeSingle node);

    public void caseCompactRoot(CompactRoot node);

    public void caseCompactShadowNode(CompactShadowNode node);

    public void caseCompactTarifNode(CompactTarifNode node);

    public void caseCompactTarifLinkNode(CompactTarifLinkNode node);

    public void caseCompactWorkSheetNode(CompactWorkSheetNode node);

    public void caseCompactBCGroupNode(CompactBCGroupNode node);
}
