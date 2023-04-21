package org.lsmp.djepExamples;

import org.nfunk.jep.*;

/**
 * Examples using assignment
 */
public class AssignmentExample {

    public static void main(String args[]) {
        JEP j = new JEP();
        j.addStandardConstants();
        j.addStandardFunctions();
        j.addComplex();
        j.setAllowUndeclared(true);
        j.setImplicitMul(true);
        j.setAllowAssignment(true);
        j.parseExpression("x=3");
        j.getValueAsObject();
        j.parseExpression("y=2");
        j.getValueAsObject();
        j.parseExpression("x^y");
        Object val3 = j.getValueAsObject();
        System.out.println("Value is " + val3);
        try {
            Node node1 = j.parse("z=i*pi");
            j.evaluate(node1);
            Node node2 = j.parse("exp(z)");
            Object val2 = j.evaluate(node2);
            System.out.println("Value: " + val2);
            Node node3 = j.parse("z=x^y");
            j.setVarValue("x", new Double(2));
            j.setVarValue("y", new Double(3));
            j.evaluate(node3);
            System.out.println(j.getVarValue("z"));
        } catch (ParseException e) {
            System.out.println("Error with parsing");
        } catch (Exception e) {
            System.out.println("Error with evaluation");
        }
    }
}
