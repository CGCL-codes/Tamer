package com.ideals.weavec.cprweaving;

import com.ideals.weavec.cpr.CPR;
import com.ideals.weavec.cprweaving.functionanalysing.FunctionAnalyser;
import com.ideals.weavec.specificationanalysis.specificationelements.Instruction;
import com.ideals.weavec.specificationanalysis.specificationelements.Scope;
import com.ideals.weavec.specificationanalysis.specificationelements.WeavingPoint;
import com.ideals.weavec.specificationanalysis.specificationelements.WeavingPointSequence;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 13-jul-2005
 * Time: 10:41:19
 * To change this template use File | Settings | File Templates.
 */
public class Weaver {

    private CPR cpr = null;

    private CPRWeaver cprWeaver = null;

    private FunctionAnalyser functionAnalyser = new FunctionAnalyser();

    private Instruction instruction = null;

    private int weavingNumber;

    public Weaver() {
    }

    public Weaver(CPR cpr, int weavingNumber) {
        this.cpr = cpr;
        this.cprWeaver = new CPRWeaver(cpr);
        this.weavingNumber = weavingNumber;
    }

    public void proceedInstruction(Instruction instruction) {
        System.out.print("\n PROCEED " + instruction.getId() + "\n");
        this.instruction = instruction;
        String code = instruction.getCodeToWeave();
        String type = instruction.getAdviseType();
        Vector functionNames = identifyFunctions();
        for (int i = 0; i < functionNames.size(); i++) {
            String functionName = (String) functionNames.elementAt(i);
            System.out.println("fun: " + functionName);
            for (int j = 0; j < instruction.getWeavingPointsNumber(); j++) {
                WeavingPoint point = instruction.getWeavingPoint(j);
                cprWeaver.weaveAtPoint(weavingNumber, functionName, point, type, code);
                weavingNumber = cprWeaver.getLastID();
            }
            for (int j = 0; j < instruction.getWeavingPointsSequenceNumber(); j++) {
                WeavingPointSequence sequence = instruction.getWeavingPointSequence(j);
                cprWeaver.weaveAtSequence(weavingNumber, functionName, sequence, type, code);
                weavingNumber = cprWeaver.getLastID();
            }
        }
    }

    private Vector identifyFunctions() {
        Vector functionList = new Vector();
        if (!instruction.isToCompleteFile()) {
            functionList = functionAnalyser.analyseNames(instruction.getFunctionNames(), cpr.getFunctionNames());
        } else if (instruction.isOfSpecialType()) {
            functionList = functionAnalyser.analyseProperties(instruction.getfProperties(), cpr.getFunctions());
        } else {
            functionList = cpr.getFunctionNames();
        }
        if (instruction.getBlackScopesNumber() > 0) {
            System.out.println("BLACK SCOPE");
            for (int i = 0; i < instruction.getBlackScopesNumber(); i++) {
                Scope blackScope = instruction.getBlackScope(i);
                if (!blackScope.isToCompleteFile()) {
                    Vector blackNames = blackScope.getFunctionNames();
                    functionList = functionAnalyser.analyseBlackNames(functionList, blackNames);
                }
            }
        }
        return functionList;
    }

    public int getWeavingNumber() {
        return weavingNumber;
    }

    public void finalizeCPRWeaving() {
        cprWeaver.finalizeWeaving();
    }
}
