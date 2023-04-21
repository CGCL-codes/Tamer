package org.deri.iris.evaluation.common;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;

/**
 * @author richi
 *
 */
public class SimpleTest {

    public static void main(final String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        Set<IRule> rules = new HashSet<IRule>();
        ILiteral lh = BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        IHead head = BASIC.createHead(lh);
        ILiteral lb = BASIC.createLiteral(true, BASIC.createPredicate("flat", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        IBody body = BASIC.createBody(lb);
        rules.add(BASIC.createRule(head, body));
        lh = BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        head = BASIC.createHead(lh);
        List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("up", 2), TERM.createVariable("X"), TERM.createVariable("Z1")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), TERM.createVariable("Z1"), TERM.createVariable("Z2")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("flat", 2), TERM.createVariable("Z2"), TERM.createVariable("Z3")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), TERM.createVariable("Z3"), TERM.createVariable("Z4")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("down", 2), TERM.createVariable("Z4"), TERM.createVariable("Y")));
        body = BASIC.createBody(bodyLiterals);
        rules.add(BASIC.createRule(head, body));
        IQuery query = BASIC.createQuery(BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), TERM.createConstant(TERM.createString("john")), TERM.createVariable("X")));
        System.out.println("*** input: ******");
        for (IRule r : rules) {
            System.out.println(r);
        }
        System.out.println(query);
        System.out.println();
        System.out.println("*** output: ******");
        AdornedProgram program = new AdornedProgram(rules, query);
        System.out.println(program);
    }

    private static void test2() {
        System.out.println("*** TEST 2 ******");
        Set<IRule> rules = new HashSet<IRule>();
        ILiteral lh = BASIC.createLiteral(true, BASIC.createPredicate("rsg", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        IHead head = BASIC.createHead(lh);
        ILiteral lb = BASIC.createLiteral(true, BASIC.createPredicate("flat", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        IBody body = BASIC.createBody(lb);
        rules.add(BASIC.createRule(head, body));
        lh = BASIC.createLiteral(true, BASIC.createPredicate("rsg", 2), TERM.createVariable("X"), TERM.createVariable("Y"));
        head = BASIC.createHead(lh);
        List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("up", 2), TERM.createVariable("X"), TERM.createVariable("X1")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("rsg", 2), TERM.createVariable("Y1"), TERM.createVariable("X1")));
        bodyLiterals.add(BASIC.createLiteral(true, BASIC.createPredicate("down", 2), TERM.createVariable("Y1"), TERM.createVariable("Y")));
        body = BASIC.createBody(bodyLiterals);
        rules.add(BASIC.createRule(head, body));
        IQuery query = BASIC.createQuery(BASIC.createLiteral(true, BASIC.createPredicate("rsg", 2), TERM.createConstant(TERM.createString("a")), TERM.createVariable("Y")));
        System.out.println("*** input: ******");
        for (IRule r : rules) {
            System.out.println(r);
        }
        System.out.println(query);
        System.out.println();
        System.out.println("*** output: ******");
        AdornedProgram program = new AdornedProgram(rules, query);
        System.out.println(program);
    }
}
