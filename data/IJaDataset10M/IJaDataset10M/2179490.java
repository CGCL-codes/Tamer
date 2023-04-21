package org.tzi.ugt.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.tzi.ugt.main.Exception;
import org.tzi.ugt.model.expression.OCLExpression;
import org.tzi.ugt.model.uml.Transition;
import org.tzi.ugt.ss2.LocalVarNode;
import org.tzi.ugt.ss2.OpCallProcessNode;
import org.tzi.ugt.ss2.OperationNode;
import org.tzi.ugt.ss2.ReturnProcessNode;
import org.tzi.ugt.ss2.Rule;
import org.tzi.ugt.ss2.SystemState;
import org.tzi.ugt.ss2.TU;
import systemStates.Grammar;
import systemStates.types.Variable;

/**
 * Visitor for Collaboration defining usecases.
 * 
 * @author lschaps
 */
class UseCaseVisitor extends ActivatorVisitor {

    /**
	 * Constructs the visitor.
	 * 
	 * @param in_ColD
	 *            The name of the collaboration diagram.
	 * @param in_Grammar
	 *            The grammar for the rules.
	 */
    UseCaseVisitor(CollaborationDiagram in_ColD, Grammar in_Grammar) {
        super(in_ColD, in_Grammar);
    }

    void visit(Activator in_Activator) throws Exception {
        if (in_Activator instanceof Message) {
            Message m = (Message) in_Activator;
            m_Rules.putAll(genRules(m));
        } else if (in_Activator instanceof UseCase) {
            UseCase u = (UseCase) in_Activator;
            m_Rules.putAll(genRules(u));
        } else {
            throw new Exception("Activator whether Message or UseCase.");
        }
    }

    /**
	 * Generates the rule for the usecase.
	 * 
	 * @param u
	 *            The usecase.
	 * 
	 * @return The rules
	 * @throws Exception
	 *             Thrown when rule a could not be generated.
	 */
    protected Map genRules(UseCase u) throws Exception {
        Map rules = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        Integer key = null;
        Integer key_old = null;
        Iterator activatorForIt = u.getActivatorFor().keySet().iterator();
        while (activatorForIt.hasNext()) {
            key_old = key;
            key = (Integer) activatorForIt.next();
            Rule r = genRule(u, key, key_old);
            u.acceptLocalVars();
            rules.put(r.getName(), r);
        }
        if (!u.isExplicitReturn()) {
            Rule rule = genReturnOpcall(u, key);
            if (null != rule) {
                rules.put(rule.getName(), rule);
            }
        }
        return rules;
    }

    /**
	 * Generates a rule for the usecase.
	 * 
	 * @param u
	 *            The usecase.
	 * @param key
	 *            The key of the subprocesses.
	 * @param key_old
	 *            The key of the predecessorsubprocesses.
	 * 
	 * @return The rule.
	 * 
	 * @throws Exception
	 *             Thrown when the rule could not be generated.
	 */
    protected Rule genRule(UseCase u, Integer key, Integer key_old) throws Exception {
        String rulename;
        rulename = this.getColDName() + ":" + key.toString();
        Rule rule = new Rule(this.getGrammar(), rulename, key.toString(), Rule.Enum.USECASE);
        SystemState lss = rule.getLeft();
        SystemState rss = rule.getRight();
        OperationNode lopn = lss.createOperationNode(u.getOperation());
        OperationNode ropn = rss.createOperationNode(u.getOperation());
        lopn.setName(u.getOperation().signature());
        ropn.setName(u.getOperation().signature());
        rule.addMapping(lopn, ropn);
        OpCallProcessNode lopcn = lss.createOpCallProcessNode(u);
        OpCallProcessNode ropcn = rss.createOpCallProcessNode(u);
        lopcn.setOperation(lopn);
        ropcn.setOperation(ropn);
        if (null == key_old) {
            lopcn.setStatus("#waiting");
        } else {
            lopcn.setStatus("#active");
        }
        ropcn.setStatus("#active");
        rule.addMapping(lopcn, ropcn);
        Iterator it = u.getLocalVars().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            String type = u.getLocalVarType(name);
            LocalVarNode llvn1 = lss.createLocalVarNode();
            LocalVarNode rlvn1 = rss.createLocalVarNode();
            llvn1.setName(name);
            rlvn1.setName(name);
            llvn1.setType(type);
            rlvn1.setType(type);
            llvn1.setValue(new Variable(name));
            rlvn1.setValue(new Variable(name));
            llvn1.setProcess(lopcn);
            rlvn1.setProcess(ropcn);
            rule.addMapping(llvn1, rlvn1);
        }
        if (null != key_old) {
            genPredecessors(rule, lopcn, (Set) u.getActivatorFor().get(key_old));
        }
        genActivates(u, key, rule);
        return rule;
    }

    /**
	 * Generates a rule of the message. Called by genRules. Is for generating
	 * the rules with different transitions of the activator.
	 * 
	 * @param in_msg
	 *            The activator message.
	 * @param key
	 *            The key of the actual activated messages. Used for the rule
	 *            name.
	 * @param key_old
	 *            The old key (of the predecessors). Not the value itself ist
	 *            important but rather if it has one.
	 * @param in_Transition
	 *            The transition for the rule.
	 * 
	 * @return The rule.
	 * 
	 * @throws Exception
	 */
    protected Rule genRule(Message in_msg, Integer key, Integer key_old, Transition in_Transition) throws Exception {
        TU tu = new TU(this.getGrammar(), genRuleName(in_msg, key.toString(), in_Transition), genShortRuleName(in_msg, key.toString(), in_Transition), Rule.Enum.USECASE);
        Rule rule = new Rule(this.getGrammar(), genRuleName(in_msg, key.toString(), in_Transition), genShortRuleName(in_msg, key.toString(), in_Transition), Rule.Enum.USECASE);
        SystemState lss = rule.getLeft();
        SystemState rss = rule.getRight();
        genActivator(in_msg, rule, in_Transition, key_old);
        OpCallProcessNode lopcn = lss.createOpCallProcessNode(in_msg);
        OpCallProcessNode ropcn = rss.createOpCallProcessNode(in_msg);
        if (null != key_old) {
            genPredecessors(rule, lopcn, (Set) in_msg.getActivatorFor().get(key_old));
        }
        genActivates(in_msg, key, rule);
        return rule;
    }

    protected Rule genReturnOpcall(Message in_msg, Integer in_KeyOld) throws Exception {
        String rulename = genRuleName(in_msg, "x", null);
        Rule rule = new Rule(this.getGrammar(), rulename, genShortRuleName(in_msg, "x", null), Rule.Enum.USECASE);
        SystemState lss = rule.getLeft();
        SystemState rss = rule.getRight();
        genActivator(in_msg, rule, null, in_KeyOld);
        OpCallProcessNode lopcn = lss.createOpCallProcessNode(in_msg);
        OpCallProcessNode ropcn = rss.createOpCallProcessNode(in_msg);
        if (null != in_KeyOld) {
            genPredecessors(rule, lopcn, (Set) in_msg.getActivatorFor().get(in_KeyOld));
        }
        if (null != in_msg.getAssign()) {
            System.out.println(rulename + ": Dont know the value for " + in_msg.getAssign());
        }
        Iterator it = in_msg.getLocalVars().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            String type = in_msg.getLocalVarType(name);
            LocalVarNode llvn1 = lss.createLocalVarNode();
            LocalVarNode rlvn1 = rss.createLocalVarNode();
            llvn1.setName(name);
            rlvn1.setName(name);
            llvn1.setType(type);
            rlvn1.setType(type);
            llvn1.setValue(new Variable(name));
            rlvn1.setValue(new Variable(name));
            llvn1.setProcess(lopcn);
            rlvn1.setProcess(ropcn);
            rule.addMapping(llvn1, rlvn1);
        }
        ReturnProcessNode rpn = rss.createReturnProcessNode();
        rpn.setActivator(rss.createOpCallProcessNode(in_msg));
        rpn.setSeqNo(in_msg.getSeqNo() + ".x");
        rpn.setStatus("#waiting");
        rpn.setType("undef");
        rpn.setValue(new OCLExpression("oclUndefined(OclAny)"));
        return rule;
    }

    protected Rule genReturnOpcall(UseCase in_u, Integer in_KeyOld) throws Exception {
        String rulename = in_u.getOperation().signature() + ":x";
        Rule rule = new Rule(this.getGrammar(), rulename, "x", Rule.Enum.USECASE);
        SystemState lss = rule.getLeft();
        SystemState rss = rule.getRight();
        OperationNode lopn = lss.createOperationNode(in_u.getOperation());
        OperationNode ropn = rss.createOperationNode(in_u.getOperation());
        lopn.setName(in_u.getOperation().signature());
        ropn.setName(in_u.getOperation().signature());
        rule.addMapping(lopn, ropn);
        OpCallProcessNode lopcn = lss.createOpCallProcessNode(in_u);
        OpCallProcessNode ropcn = rss.createOpCallProcessNode(in_u);
        lopcn.setOperation(lopn);
        ropcn.setOperation(ropn);
        if (null == in_KeyOld) {
            lopcn.setStatus("#waiting");
        } else {
            lopcn.setStatus("#active");
        }
        ropcn.setStatus("#active");
        rule.addMapping(lopcn, ropcn);
        Iterator it = in_u.getLocalVars().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            String type = in_u.getLocalVarType(name);
            LocalVarNode llvn1 = lss.createLocalVarNode();
            LocalVarNode rlvn1 = rss.createLocalVarNode();
            llvn1.setName(name);
            rlvn1.setName(name);
            llvn1.setType(type);
            rlvn1.setType(type);
            llvn1.setValue(new Variable(name));
            rlvn1.setValue(new Variable(name));
            llvn1.setProcess(lopcn);
            rlvn1.setProcess(ropcn);
            rule.addMapping(llvn1, rlvn1);
        }
        if (null != in_KeyOld) {
            genPredecessors(rule, lopcn, (Set) in_u.getActivatorFor().get(in_KeyOld));
        }
        ReturnProcessNode rpn = rss.createReturnProcessNode();
        rpn.setActivator(rss.createOpCallProcessNode(in_u));
        rpn.setSeqNo("x");
        rpn.setStatus("#waiting");
        rpn.setType("undef");
        rpn.setValue(new OCLExpression("oclUndefined(OclAny)"));
        return rule;
    }
}
