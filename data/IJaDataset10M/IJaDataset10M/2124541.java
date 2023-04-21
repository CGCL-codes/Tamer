package org.bpmn.bpel.exporter.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import bpmn.*;
import bpmn.Process;
import bpmn.impl.BpmnFactoryImpl;

/**
 * @author jassuncao
 *
 */
public class Test1 {

    public class FlowMerge {

        FlowObject flowObject;

        List<Token> currentTokens = new ArrayList<Token>();

        private int expectedTokens;

        private int currentAmount;

        private String id;

        public FlowMerge(FlowObject flowObject, int expectedTokens, Token token) {
            this.flowObject = flowObject;
            this.expectedTokens = expectedTokens;
            this.id = token.id;
            currentTokens.add(token);
            currentAmount = 1;
        }

        public boolean addToken(Token token) {
            if (!token.id.equals(this.id)) {
                throw new RuntimeException("Invalid Merge at " + flowObject.getName());
            }
            currentTokens.add(token);
            currentAmount++;
            return currentAmount == expectedTokens;
        }
    }

    public class Split {

        int amount;

        FlowObject splitObject;

        String id;

        public Split(String id, FlowObject splitObject, int amount) {
            this.splitObject = splitObject;
            this.amount = amount;
            this.id = id;
        }
    }

    private Map<FlowObject, FlowMerge> flowMerges = new HashMap<FlowObject, FlowMerge>();

    private Map<FlowObject, Token> tokensMapping = new HashMap<FlowObject, Token>();

    public class Token {

        String id;

        int current;

        int count;

        Token parent;

        int amount;

        Split split = null;

        public Token(String id, int current, int count) {
            this(id, current, count, null);
        }

        public Token(String id, int current, int count, Token parent) {
            this.id = id;
            this.current = current;
            this.count = count;
            this.parent = parent;
            this.amount = 1;
        }
    }

    public static FlowObject findStartElement(Process process) {
        Iterator it = process.getGraphicalElements().iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Event) {
                Event event = (Event) obj;
                if (event.getEventType() == EventType.START_LITERAL) ;
                return event;
            }
        }
        return null;
    }

    private static char currentID = 'A';

    public static String nextId() {
        return Character.toString(currentID++);
    }

    public void visit(Gateway gateway, Token token) {
        FlowMerge merge = flowMerges.get(gateway);
        if (merge != null) {
            if (merge.addToken(token)) {
                System.out.println("Visiting:" + gateway.getName());
                flowMerges.remove(gateway);
                if (merge.currentAmount == token.split.amount) {
                    System.out.println("Completed merge " + token.id);
                    token = token.parent;
                } else {
                    System.out.println("Completed parcial merge " + token.id);
                    token.split.amount -= merge.currentAmount - 1;
                }
                if (gateway.getGates().size() != 1) {
                    System.out.println("Invalid merge gateway: number of gates =" + gateway.getGates().size());
                    System.exit(0);
                }
                Gate gate = (Gate) gateway.getGates().get(0);
                FlowObject nextObject = (FlowObject) gate.getOutgoingSequenceFlow().getTarget();
                visit(nextObject, token);
            }
        } else {
            int incoming = 0;
            Iterator it = gateway.getInConnections().iterator();
            while (it.hasNext()) {
                if (it.next() instanceof SequenceFlow) {
                    ++incoming;
                }
            }
            if (incoming > 1) {
                FlowMerge newMerge = new FlowMerge(gateway, incoming, token);
                flowMerges.put(gateway, newMerge);
            } else {
                int outgoing = gateway.getGates().size();
                if (outgoing < 2) {
                    System.out.println("Invalid split gateway: number of gates =" + gateway.getGates().size());
                    System.exit(0);
                }
                String id = nextId();
                Split split = new Split(id, gateway, outgoing);
                System.out.println("Initiating split " + id + " at " + gateway.getName());
                it = gateway.getGates().iterator();
                int current = 0;
                while (it.hasNext()) {
                    Gate gate = (Gate) it.next();
                    FlowObject nextObject = (FlowObject) gate.getOutgoingSequenceFlow().getTarget();
                    Token newToken = new Token(id, current, outgoing, token);
                    newToken.split = split;
                    visit(nextObject, newToken);
                    ++current;
                }
            }
        }
    }

    public void visit(Event event, Token token) {
        System.out.println("Visiting:" + event.getName());
        if (event.getEventType() == EventType.END_LITERAL) {
            if (token.parent != null) {
                System.out.println("Found incomplete token at end event");
            }
        } else if (event.getEventType() == EventType.START_LITERAL) {
            List<SequenceFlow> outgoing = new ArrayList<SequenceFlow>();
            Iterator it = event.getOutConnections().iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof SequenceFlow) {
                    outgoing.add((SequenceFlow) obj);
                }
            }
            if (outgoing.size() > 1) {
                System.out.println("Unexpected number of outgoing connections for:" + event.getName());
            }
            Iterator<SequenceFlow> sequence = outgoing.iterator();
            while (sequence.hasNext()) {
                visit((FlowObject) sequence.next().getTarget(), token);
            }
        }
    }

    public void visit(FlowObject flowObject, Token token) {
        Token existingToken = tokensMapping.get(flowObject);
        if (existingToken != null) {
        }
        tokensMapping.put(flowObject, token);
        if (flowObject instanceof Gateway) {
            visit((Gateway) flowObject, token);
        } else if (flowObject instanceof Event) {
            visit((Event) flowObject, token);
        } else {
            System.out.println("Visiting:" + flowObject.getName());
            List<SequenceFlow> outgoing = new ArrayList<SequenceFlow>();
            Iterator it = flowObject.getOutConnections().iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof SequenceFlow) {
                    outgoing.add((SequenceFlow) obj);
                }
            }
            if (outgoing.size() > 1) {
                System.out.println("Unexpected number of outgoing connections for:" + flowObject.getName());
            }
            Iterator<SequenceFlow> sequence = outgoing.iterator();
            while (sequence.hasNext()) {
                visit((FlowObject) sequence.next().getTarget(), token);
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        BpmnPackage bpmnPackage = BpmnPackage.eINSTANCE;
        ResourceSet resourceSet = new ResourceSetImpl();
        URI fileURI = URI.createURI("examples/testcase7.bpmn");
        Resource resource = resourceSet.getResource(fileURI, true);
        BPMNDiagram diagram = (BPMNDiagram) resource.getEObject("/");
        Process process = (Process) diagram.getProcesses().get(0);
        Test1 test1 = new Test1();
        Token token = test1.new Token(nextId(), 1, 1);
        FlowObject start = findStartElement(process);
        System.out.println(start);
        test1.visit(start, token);
    }
}
