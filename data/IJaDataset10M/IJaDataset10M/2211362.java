package at.ac.tuwien.infosys.www.pixy.analysis.type;

import java.util.*;
import at.ac.tuwien.infosys.www.pixy.analysis.GenericRepos;
import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunctionId;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.AnalysisType;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.InterAnalysis;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.InterAnalysisNode;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.InterWorkList;
import at.ac.tuwien.infosys.www.pixy.analysis.type.tf.*;
import at.ac.tuwien.infosys.www.pixy.conversion.TacConverter;
import at.ac.tuwien.infosys.www.pixy.conversion.TacFunction;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;
import at.ac.tuwien.infosys.www.pixy.conversion.nodes.*;
import org.apache.log4j.Logger;

public class TypeAnalysis extends InterAnalysis {

    protected static Logger log = Logger.getLogger(TypeAnalysis.class.getName());

    private GenericRepos<LatticeElement> repos;

    private Collection<String> classNames;

    public TypeAnalysis(TacConverter tac, AnalysisType analysisType, InterWorkList workList) {
        this.repos = new GenericRepos<LatticeElement>();
        this.classNames = tac.getUserClasses().keySet();
        this.initGeneral(tac.getAllFunctions(), tac.getMainFunction(), analysisType, workList);
    }

    public Set<Type> getType(Variable var, CfgNode cfgNode) {
        InterAnalysisNode ian = this.interAnalysisInfo.getAnalysisNode(cfgNode);
        if (ian == null) {
            log.error("InterAnalysisNode is null var='" + var + "' cfgNode='" + cfgNode + "' loc='" + cfgNode.getLoc() + "'");
            throw new RuntimeException("SNH: InterAnalysisNode is null");
        }
        TypeLatticeElement elem = (TypeLatticeElement) ian.computeFoldedValue();
        if (elem == null) {
            return null;
        }
        return elem.getType(var);
    }

    protected Boolean evalIf(CfgNodeIf ifNode, LatticeElement inValue) {
        return null;
    }

    protected void initLattice() {
        this.lattice = new TypeLattice(this.classNames);
        this.startValue = new TypeLatticeElement();
        this.initialValue = this.lattice.getBottom();
    }

    public LatticeElement recycle(LatticeElement recycleMe) {
        return this.repos.recycle(recycleMe);
    }

    protected TransferFunction assignSimple(CfgNode cfgNodeX, CfgNode aliasInNode) {
        CfgNodeAssignSimple cfgNode = (CfgNodeAssignSimple) cfgNodeX;
        Variable left = (Variable) cfgNode.getLeft();
        return new TypeTfAssignSimple(left, cfgNode.getRight());
    }

    protected TransferFunction assignUnary(CfgNode cfgNodeX, CfgNode aliasInNode) {
        CfgNodeAssignUnary cfgNode = (CfgNodeAssignUnary) cfgNodeX;
        Variable left = (Variable) cfgNode.getLeft();
        return new TypeTfAssignUnary(left);
    }

    protected TransferFunction assignBinary(CfgNode cfgNodeX, CfgNode aliasInNode) {
        CfgNodeAssignBinary cfgNode = (CfgNodeAssignBinary) cfgNodeX;
        Variable left = (Variable) cfgNode.getLeft();
        return new TypeTfAssignBinary(left);
    }

    protected TransferFunction assignRef(CfgNode cfgNodeX) {
        CfgNodeAssignRef cfgNode = (CfgNodeAssignRef) cfgNodeX;
        Variable left = (Variable) cfgNode.getLeft();
        return new TypeTfAssignRef(left, cfgNode.getRight());
    }

    protected TransferFunction unset(CfgNode cfgNodeX) {
        CfgNodeUnset cfgNode = (CfgNodeUnset) cfgNodeX;
        return new TypeTfUnset(cfgNode.getOperand());
    }

    protected TransferFunction assignArray(CfgNode cfgNodeX) {
        CfgNodeAssignArray cfgNode = (CfgNodeAssignArray) cfgNodeX;
        return new TypeTfAssignArray(cfgNode.getLeft());
    }

    protected TransferFunction callPrep(CfgNode cfgNodeX, TacFunction traversedFunction) {
        CfgNodeCallPrep cfgNode = (CfgNodeCallPrep) cfgNodeX;
        TacFunction calledFunction = cfgNode.getCallee();
        TacFunction callingFunction = traversedFunction;
        if (calledFunction == null) {
            return TransferFunctionId.INSTANCE;
        }
        List actualParams = cfgNode.getParamList();
        List formalParams = calledFunction.getParams();
        TransferFunction tf = null;
        if (actualParams.size() > formalParams.size()) {
            throw new RuntimeException("More actual than formal params for function " + cfgNode.getFunctionNamePlace().toString() + " on line " + cfgNode.getOrigLineno());
        } else {
            tf = new TypeTfCallPrep(actualParams, formalParams, callingFunction, calledFunction, this);
        }
        return tf;
    }

    protected TransferFunction callRet(CfgNode cfgNodeX, TacFunction traversedFunction) {
        CfgNodeCallRet cfgNodeRet = (CfgNodeCallRet) cfgNodeX;
        CfgNodeCall cfgNodeCall = cfgNodeRet.getCallNode();
        CfgNodeCallPrep cfgNodePrep = cfgNodeRet.getCallPrepNode();
        TacFunction callingFunction = traversedFunction;
        TacFunction calledFunction = cfgNodeCall.getCallee();
        TransferFunction tf;
        if (calledFunction == null) {
            tf = new TypeTfCallRetUnknown(cfgNodeRet);
        } else {
            tf = new TypeTfCallRet(this.interAnalysisInfo.getAnalysisNode(cfgNodePrep), callingFunction, calledFunction, cfgNodeCall);
        }
        return tf;
    }

    protected TransferFunction callBuiltin(CfgNode cfgNodeX, TacFunction traversedFunction) {
        CfgNodeCallBuiltin cfgNode = (CfgNodeCallBuiltin) cfgNodeX;
        return new TypeTfCallBuiltin(cfgNode);
    }

    protected TransferFunction isset(CfgNode cfgNodeX) {
        CfgNodeIsset cfgNode = (CfgNodeIsset) cfgNodeX;
        return new TypeTfIsset((Variable) cfgNode.getLeft());
    }
}
