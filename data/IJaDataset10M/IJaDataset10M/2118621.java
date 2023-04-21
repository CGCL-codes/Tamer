package com.agentfactory.afapl.compiler;

import java.util.LinkedList;
import java.util.List;
import com.agentfactory.afapl.interpreter.CommitmentRule;
import com.agentfactory.afapl.interpreter.Plan;

public class TestMain {

    public static void main(String args[]) {
        List<SimpleNode> programNodes = new LinkedList<SimpleNode>();
        try {
            AFAPLParser parser = new AFAPLParser(AFAPLParser.class.getResourceAsStream("/com/agentfactory/afapl/compiler/test.afapl"));
            SimpleNode node = parser.Start();
            node.dump("");
            programNodes.add(node);
            CodeGeneratorVisitor iVisitor = new CodeGeneratorVisitor();
            iVisitor.visit(node, null);
            for (CommitmentRule rule : iVisitor.rules) {
                System.out.println(rule.toString());
            }
            for (Plan plan : iVisitor.plans) {
                System.out.println(plan);
            }
            for (AFAPLError error : parser.errors) {
                System.out.println(error.issue);
            }
            System.out.println("Thank you.");
        } catch (Exception e) {
            System.out.println("Oops.");
            e.printStackTrace();
        }
    }
}
