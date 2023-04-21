package org.bpmn.bpel.exporter.phase4;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.bpmn.bpel.exporter.common.StartNode;
import org.bpmn.bpel.exporter.phase1.PreProcessor;
import org.bpmn.bpel.exporter.phase2.FlowAnalyzer;
import org.bpmn.bpel.exporter.phase3.AssignInstruction;
import org.bpmn.bpel.exporter.phase3.Branch;
import org.bpmn.bpel.exporter.phase3.ElementContainer;
import org.bpmn.bpel.exporter.phase3.EmptyInstruction;
import org.bpmn.bpel.exporter.phase3.Instruction;
import org.bpmn.bpel.exporter.phase3.InstructionSequence;
import org.bpmn.bpel.exporter.phase3.InstructionVisitor;
import org.bpmn.bpel.exporter.phase3.LoopInstruction;
import org.bpmn.bpel.exporter.phase3.SimpleInstruction;
import org.bpmn.bpel.exporter.phase3.SplitMergeInstruction;
import org.bpmn.bpel.exporter.phase3.Translator;
import org.bpmn.bpel.exporter.phase4.SplitBPMNSwitch.SwitchWrapper;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Case;
import org.eclipse.bpel.model.Condition;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Documentation;
import org.eclipse.bpel.model.Expression;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Otherwise;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Sequence;
import org.eclipse.bpel.model.Switch;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.While;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.internal.presentations.BasicPartList;
import bpmn.ANDGateway;
import bpmn.Assignment;
import bpmn.BPMNDiagram;
import bpmn.BpmnPackage;
import bpmn.ConditionType;
import bpmn.Event;
import bpmn.FlowObject;
import bpmn.Gateway;
import bpmn.ORGateway;
import bpmn.SequenceFlow;
import bpmn.XORGateway;
import bpmn.util.BpmnSwitch;

/**
 * @author jassuncao
 *
 */
public class BPELEmitter implements InstructionVisitor {

    BPELFactory BPEL = BPELFactory.eINSTANCE;

    public BPELEmitter() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("bpel", new BPELResourceFactoryImpl());
    }

    public static void fillCommonAttributes(Activity activity, FlowObject object) {
        activity.setName(object.getName());
        String s = object.getDocumentation();
        if (s != null && s.length() > 0) {
            Documentation doc = BPELFactory.eINSTANCE.createDocumentation();
            doc.setValue(s);
            activity.setDocumentation(doc);
        }
    }

    private class ProcessWrapper implements ElementContainer {

        private Process process;

        public ProcessWrapper(Process process) {
            this.process = process;
        }

        public void addElement(Object element) {
            process.setActivity((Activity) element);
        }
    }

    private class WhileWrapper implements ElementContainer {

        private While owner;

        public WhileWrapper(While owner) {
            this.owner = owner;
        }

        public void addElement(Object element) {
            owner.setActivity((Activity) element);
        }
    }

    private class OtherwiseWrapper implements ElementContainer {

        private Otherwise owner;

        public OtherwiseWrapper(Otherwise owner) {
            this.owner = owner;
        }

        public void addElement(Object element) {
            owner.setActivity((Activity) element);
        }
    }

    private class CaseWrapper implements ElementContainer {

        private Case owner;

        public CaseWrapper(Case owner) {
            this.owner = owner;
        }

        public void addElement(Object element) {
            owner.setActivity((Activity) element);
        }
    }

    private class SequenceWrapper implements ElementContainer {

        private Sequence owner;

        public SequenceWrapper(Sequence owner) {
            this.owner = owner;
        }

        public void addElement(Object element) {
            owner.getActivities().add(element);
        }
    }

    public ElementContainer beginRepeat(LoopInstruction instruction, ElementContainer parent) {
        if (parent instanceof SequenceWrapper) return parent;
        Sequence sequence = BPEL.createSequence();
        parent.addElement(sequence);
        return new SequenceWrapper(sequence);
    }

    public ElementContainer beginVisit(Branch branch, ElementContainer parent) {
        if (parent instanceof SwitchWrapper) {
            SequenceFlow seq = branch.getSequence();
            if (seq.getConditionType() == ConditionType.DEFAULT_LITERAL) {
                Otherwise otherwise = BPEL.createOtherwise();
                parent.addElement(otherwise);
                return new OtherwiseWrapper(otherwise);
            } else {
                Case case1 = BPEL.createCase();
                parent.addElement(case1);
                Condition c = BPEL.createCondition();
                c.setBody(seq.getConditionExpression());
                case1.setCondition(c);
                return new CaseWrapper(case1);
            }
        }
        return parent;
    }

    public ElementContainer beginVisit(InstructionSequence sequence, ElementContainer parent) {
        if (parent instanceof SequenceWrapper) return parent;
        Sequence seq = BPEL.createSequence();
        parent.addElement(seq);
        return new SequenceWrapper(seq);
    }

    public ElementContainer beginVisit(SplitMergeInstruction instruction, ElementContainer parent) {
        SplitBPMNSwitch switch1 = new SplitBPMNSwitch(parent);
        return (ElementContainer) switch1.doSwitch(instruction.getFlowObject());
    }

    public ElementContainer beginWhile(LoopInstruction instruction, ElementContainer container) {
        While while1 = BPEL.createWhile();
        container.addElement(while1);
        Condition condition = BPEL.createCondition();
        condition.setBody(instruction.getLoopPath().getConditionExpression());
        while1.setCondition(condition);
        fillCommonAttributes(while1, instruction.getFlowObject());
        return new WhileWrapper(while1);
    }

    public void endRepeat(LoopInstruction instruction, ElementContainer parent) {
    }

    public void endVisit(Branch branch, ElementContainer parent) {
    }

    public void endVisit(InstructionSequence sequence, ElementContainer parent) {
    }

    public void endVisit(SplitMergeInstruction instruction, ElementContainer parent) {
    }

    public void endWhile(LoopInstruction instruction, ElementContainer container) {
    }

    public void visit(AssignInstruction instruction, ElementContainer parent) {
        Assign assign = BPEL.createAssign();
        parent.addElement(assign);
        Iterator<Assignment> it = instruction.getAssigments();
        while (it.hasNext()) {
            assign.getCopy().add(createCopy(it.next()));
        }
    }

    private Copy createCopy(Assignment assignment) {
        Copy copy = BPEL.createCopy();
        From from = BPEL.createFrom();
        Expression expression = BPEL.createExpression();
        expression.setBody(assignment.getFrom());
        from.setExpression(expression);
        To to = BPEL.createTo();
        copy.setFrom(from);
        copy.setTo(to);
        return copy;
    }

    public void visit(EmptyInstruction instruction, ElementContainer parent) {
    }

    SimpleObjectsBPMNSwitch simpleObjectsBPMNSwitch = new SimpleObjectsBPMNSwitch();

    public void visit(SimpleInstruction instruction, ElementContainer parent) {
        Object res = simpleObjectsBPMNSwitch.doSwitch(instruction.getFlowObject());
        if (res != null) parent.addElement(res);
    }

    public Process execute(bpmn.Process bpmnProcess, Instruction instruction) {
        org.eclipse.bpel.model.Process bpelProcess = BPEL.createProcess();
        initializeBPELProcess(bpelProcess, bpmnProcess);
        instruction.accept(this, new ProcessWrapper(bpelProcess));
        return bpelProcess;
    }

    private void initializeBPELProcess(Process bpelProcess, bpmn.Process bpmnProcess) {
        bpelProcess.setName(bpmnProcess.getName());
        bpmn.BPMNDiagram diagram = (BPMNDiagram) bpmnProcess.eContainer();
        bpelProcess.setExpressionLanguage(diagram.getExpressionLanguage());
        bpelProcess.setQueryLanguage(diagram.getQueryLanguage());
        String doc = bpmnProcess.getDocumentation();
        if (doc != null) {
            Documentation doc1 = BPEL.createDocumentation();
            doc1.setValue(doc);
            bpelProcess.setDocumentation(doc1);
        }
    }

    public void write(Process bpelProcess, String baseLocation) throws IOException {
        ResourceSet resourceSet = new ResourceSetImpl();
        URI outputURI = URI.createFileURI(baseLocation + "/" + bpelProcess.getName() + ".bpel");
        Resource bpelResource = resourceSet.createResource(outputURI);
        bpelResource.getContents().add(bpelProcess);
        bpelResource.save(Collections.EMPTY_MAP);
    }
}
