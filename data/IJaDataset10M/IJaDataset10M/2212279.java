package com.ideals.weavec.codeanalysis.cfg;

import com.ideals.weavec.cpr.cprassembling.cgramast.TNode;
import com.ideals.weavec.cpr.cprbuilding.cprtree.CPRNode;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 16-mrt-2005
 * Time: 10:54:34
 * To change this template use File | Settings | File Templates.
 */
public class IntraCFNode extends CFNode {

    private Vector predecessors = new Vector();

    private Vector successors = new Vector();

    private Vector interPredecessors = new Vector();

    public Vector getInterSuccessors() {
        return interSuccessors;
    }

    private Vector interSuccessors = new Vector();

    private Vector intraSucCalls = new Vector();

    private Vector intraPreCalls = new Vector();

    private boolean visited = false;

    private boolean tempVisisted = false;

    private int visitedNumber = 0;

    private int level = -1;

    private String text = null;

    private String label = null;

    private CPRNode cprNode = null;

    public CPRNode getCPRNode() {
        return cprNode;
    }

    public void setCPRNode(CPRNode cprNode) {
        this.cprNode = cprNode;
    }

    public boolean isTempVisisted() {
        return tempVisisted;
    }

    public void setTempVisisted(boolean tempVisisted) {
        this.tempVisisted = tempVisisted;
    }

    public Vector getIntraSucCalls() {
        return intraSucCalls;
    }

    public void setIntraSucCalls(Vector intraSucCalls) {
        this.intraSucCalls = intraSucCalls;
    }

    public Vector getIntraPreCalls() {
        return intraPreCalls;
    }

    public void setIntraPreCalls(Vector intraPreCalls) {
        this.intraPreCalls = intraPreCalls;
    }

    public IntraCFNode(String id) {
        super(id);
    }

    public IntraCFNode(String id, String kind, String text) {
        super(id, kind);
        this.text = text;
    }

    public IntraCFNode(String id, String kind, String text, String label) {
        super(id, kind);
        this.text = text;
        this.label = label;
    }

    public void addPredecessor(IntraCFNode node) {
        predecessors.addElement(node);
    }

    public void addSuccessor(IntraCFNode node) {
        successors.addElement(node);
    }

    public void addInterPredecessor(CFNode node) {
        interPredecessors.addElement(node);
    }

    public void addInterSuccessor(CFNode node) {
        interSuccessors.addElement(node);
    }

    public int getNumberOfPredecessors() {
        return this.predecessors.size();
    }

    public int getNumberOfSuccessors() {
        return this.successors.size();
    }

    public int getNumberOfInterPredecessors() {
        return this.interPredecessors.size();
    }

    public int getNumberOfInterSuccessors() {
        return this.interSuccessors.size();
    }

    public IntraCFNode getPredecessor(int i) {
        return (IntraCFNode) this.predecessors.elementAt(i);
    }

    public IntraCFNode getSuccessor(int i) {
        return (IntraCFNode) this.successors.elementAt(i);
    }

    public CFNode getInterPredecessor(int i) {
        return (CFNode) this.interPredecessors.elementAt(i);
    }

    public CFNode getInterSuccessor(int i) {
        return (CFNode) this.interSuccessors.elementAt(i);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
        this.visitedNumber++;
    }

    public void setInterPredecessors(Vector interPredecessors) {
        this.interPredecessors = interPredecessors;
    }

    public void setInterSuccessors(Vector interSuccessors) {
        this.interSuccessors = interSuccessors;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void testNode() {
        System.out.println("\n>>>  " + id + " : " + kind + " : " + text + " ins: " + predecessors.size() + " outs: " + successors.size());
        System.out.println(">>>>>>> pred >>>");
        for (int i = 0; i < predecessors.size(); i++) {
            IntraCFNode intraCFNode = (IntraCFNode) predecessors.elementAt(i);
            System.out.println(i + "  " + intraCFNode.getId());
        }
        System.out.println(">>>>>>> suc >>>");
        for (int i = 0; i < successors.size(); i++) {
            IntraCFNode intraCFNode = (IntraCFNode) successors.elementAt(i);
            System.out.println(i + "  " + intraCFNode.getId());
        }
    }

    public void testNodeSimple() {
        System.out.println("\n*>>>  " + id + " : " + kind + " : " + text);
        for (int i = 0; i < interPredecessors.size(); i++) {
            InterCFNodeCall interCall = (InterCFNodeCall) interPredecessors.elementAt(i);
            System.out.println(" inter in: " + interCall.getFunctionName() + " : " + interCall.getFunctionType());
        }
        for (int i = 0; i < interSuccessors.size(); i++) {
            InterCFNodeCall interCall = (InterCFNodeCall) interSuccessors.elementAt(i);
            System.out.println(" inter out: " + interCall.getFunctionName() + " : " + interCall.getFunctionType());
        }
        for (int i = 0; i < intraSucCalls.size(); i++) {
            IntraCFNodeCall intra = (IntraCFNodeCall) intraSucCalls.elementAt(i);
            System.out.println("! succesLabel" + intra.getId() + " : " + intra.getLabel());
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void increaseLevel() {
        this.level++;
    }

    public void decreaseLevel() {
        this.level--;
    }

    public int getVisitedNumber() {
        return visitedNumber;
    }

    public void increaseVisitedNumber() {
        this.visitedNumber++;
    }

    public boolean isGoto() {
        if (this.getKind().equalsIgnoreCase(CFNode.CS_TYPE_JUMP) && this.getText().indexOf("goto") >= 0) return true;
        return false;
    }

    public boolean isIndirect() {
        if (this.getKind().equalsIgnoreCase(CFNode.CS_TYPE_INDIRECT_CALL)) return true;
        return false;
    }

    public IntraCFNode getSuccessorFalse() {
        if (this.getKind().equalsIgnoreCase(CFNode.CS_TYPE_CONTROL_POINT)) {
            String falseSucID = null;
            for (int i = 0; i < intraSucCalls.size(); i++) {
                IntraCFNodeCall intra = (IntraCFNodeCall) intraSucCalls.elementAt(i);
                if (intra.getLabel().trim().equalsIgnoreCase("#f")) {
                    falseSucID = intra.getId().trim();
                    break;
                }
            }
            for (int i = 0; i < successors.size(); i++) {
                IntraCFNode intraCFNode = (IntraCFNode) successors.elementAt(i);
                if (intraCFNode.getId().equalsIgnoreCase(falseSucID)) return intraCFNode;
            }
        }
        return null;
    }

    public IntraCFNode getSuccessorTrue() {
        if (this.getKind().equalsIgnoreCase(CFNode.CS_TYPE_CONTROL_POINT)) {
            String trueSucID = null;
            for (int i = 0; i < intraSucCalls.size(); i++) {
                IntraCFNodeCall intra = (IntraCFNodeCall) intraSucCalls.elementAt(i);
                if (intra.getLabel().trim().equalsIgnoreCase("#t")) {
                    trueSucID = intra.getId().trim();
                    break;
                }
            }
            for (int i = 0; i < successors.size(); i++) {
                IntraCFNode intraCFNode = (IntraCFNode) successors.elementAt(i);
                if (intraCFNode.getId().equalsIgnoreCase(trueSucID)) return intraCFNode;
            }
        }
        return null;
    }

    public IntraCFNode getSuccessorAnyCase(String str) {
        String toFind = "#t";
        if (str.equalsIgnoreCase("true")) toFind = "#t"; else if (str.equalsIgnoreCase("false")) toFind = "#f"; else toFind = str;
        String trueSucID = null;
        for (int i = 0; i < intraSucCalls.size(); i++) {
            IntraCFNodeCall intra = (IntraCFNodeCall) intraSucCalls.elementAt(i);
            if (intra.getLabel().trim().equalsIgnoreCase(toFind)) {
                trueSucID = intra.getId().trim();
                break;
            }
        }
        if (trueSucID == null) {
            if (successors.size() > 0) return (IntraCFNode) successors.elementAt(0);
        }
        for (int i = 0; i < successors.size(); i++) {
            IntraCFNode intraCFNode = (IntraCFNode) successors.elementAt(i);
            if (intraCFNode.getId().equalsIgnoreCase(trueSucID)) return intraCFNode;
        }
        return null;
    }

    public void testForJaki(String str) {
        System.out.println(str + "\t>>  " + id + " : " + kind + " : " + text);
    }

    public IntraCFNode findDoWhilePredeccesor() {
        for (int i = 0; i < predecessors.size(); i++) {
            IntraCFNode predeccesor = (IntraCFNode) predecessors.elementAt(i);
            if (predeccesor.getKind().equalsIgnoreCase(CFNode.CS_TYPE_CONTROL_POINT) && predeccesor.getText().indexOf("dowhile") >= 0) {
                IntraCFNode candidate = predeccesor;
                String candidateID = candidate.getId();
                for (int j = 0; j < intraPreCalls.size(); j++) {
                    IntraCFNodeCall call = (IntraCFNodeCall) intraPreCalls.elementAt(j);
                    if (call.getId().equalsIgnoreCase(candidateID)) {
                        if (call.getLabel().trim().equalsIgnoreCase("#t")) return candidate;
                    }
                }
            }
        }
        return null;
    }
}
