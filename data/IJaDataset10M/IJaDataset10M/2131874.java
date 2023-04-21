package test;

import java.util.ArrayList;
import java.util.List;
import otservices.translator.language.sparql.Filter;
import otservices.translator.language.sparql.LogExpr;
import otservices.translator.language.sparql.RegExpr;
import otservices.translator.language.sparql.RelExpr;
import otservices.translator.language.sparql.Result;
import otservices.translator.language.sparql.SPARQLObject;
import otservices.translator.language.sparql.Term;
import otservices.translator.language.sparql.Where;
import otservices.translator.reputationreasoner.impl.repage.RepageReputationReasoner;
import reputationreasoners.repage.RepageModule;

public class RepageRRTest {

    public static void main(String[] args) {
        SPARQLObject sparql;
        RepageReputationReasoner rri = new RepageReputationReasoner();
        RepageModule r = rri.getReputationReasoner();
        System.out.println(r.getReputation("C", "era1"));
        r.setRepageMemory("A", "era1");
        System.out.println("1 - " + r.getImageText("A", "era1") + " " + r.getImage("A", "era1") + " " + r.getReputationText("A", "era1") + " " + r.getReputation("A", "era1"));
        System.out.println("2 - " + r.getDeviatedImageText("A", "era1", 0.1) + " " + r.getDeviatedImage("A", "era1", 0.1) + "  " + r.getDeviatedReputationText("A", "era1", 0.2) + " " + r.getDeviatedReputation("A", "era1", 0.2));
        r.calculateImage("A", "era1", new Float(0.2));
        r.calculateImage("A", "era1", new Float(0.1));
        r.calculateImage("A", "era1", new Float(0.1));
        r.calculateImage("A", "era1", new Float(0.1));
        r.calculateImage("A", "era1", new Float(0.1));
        r.calculateImage("A", "era1", new Float(0.1));
        System.out.println("A = " + r.getImage("A", "era1"));
        r.calculateReputation("A", "era1", new Float(0.9));
        r.setRepageMemory("B", "era1");
        r.calculateImage("B", "era1", new Float(0.8));
        r.calculateReputation("B", "era1", new Float(0.1));
        r.setRepageMemory("C", "era1");
        r.calculateImage("C", "era1", new Float(0.2));
        r.setRepageMemory("D", "era1");
        r.calculateImage("D", "era1", new Float(0.4));
        List<String> list = new ArrayList<String>();
        for (String target : r.getTargets("era1")) {
            list.add(target);
        }
        list = r.sortTarget(list, "era1", false);
        for (String target : list) {
            System.out.println(target);
        }
        System.out.println(r.getImageText("A", "era1"));
        System.out.println(r.getReputationText("A", "era1"));
        sparql = setSelectSPARQLObject();
        System.out.println(sparql.getMessage());
        rri.processInMessage(sparql);
        sparql.print();
    }

    private static SPARQLObject setSelectSPARQLObject() {
        SPARQLObject sparql = new SPARQLObject();
        sparql.setCommand(SPARQLObject.Command.SELECT);
        Result result = new Result();
        Where where = new Where();
        Filter filter = new Filter();
        LogExpr logical1 = new LogExpr();
        LogExpr logical2 = new LogExpr();
        RegExpr regcontext = new RegExpr();
        RegExpr regname = new RegExpr();
        RelExpr relational = new RelExpr();
        Term term1 = new Term();
        Term term2 = new Term();
        result.addResult("?value");
        result.addResult("?drep");
        result.addResult("?context");
        result.addResult("?name");
        sparql.addResult(result);
        where.addWhere("?drep", "reputationbyrepage");
        where.addWhere("?context", "agentreputationbyrepage");
        where.addWhere("?name", "agentname");
        where.addWhere("?value", "valuebyrepage");
        sparql.addWhere(where);
        regcontext.setVariable("?context");
        regcontext.setPattern("era1");
        term1.setPosition(Term.Position.FIRST);
        term1.setType(Term.TermType.VARIABLE);
        term1.setTerm(new String("?drep"));
        term2.setPosition(Term.Position.SECOND);
        term2.setType(Term.TermType.BOOLEAN);
        term2.setTerm(new Boolean(true));
        relational.setTerm1(term1);
        relational.setTerm2(term2);
        relational.setRelationalOp(RelExpr.RelationalOp.EQ);
        logical2.setLogicalOp(LogExpr.LogicalOp.AND);
        logical2.addExpression(regname);
        logical2.addExpression(relational);
        filter.addItem(regcontext);
        filter.addItem(term1);
        filter.addItem(term2);
        filter.addItem(relational);
        filter.addItem(logical2);
        sparql.addFilter(filter);
        return sparql;
    }

    private static SPARQLObject setUpdateSPARQLObject(String value) {
        SPARQLObject sparql = new SPARQLObject();
        sparql.setCommand(SPARQLObject.Command.UPDATE);
        Result result = new Result();
        Where where = new Where();
        Filter filter = new Filter();
        LogExpr logical1 = new LogExpr();
        LogExpr logical2 = new LogExpr();
        RegExpr regcontext = new RegExpr();
        RegExpr regname = new RegExpr();
        RelExpr relational = new RelExpr();
        Term term = new Term();
        Term term1 = new Term();
        Term term2 = new Term();
        term.setType(Term.TermType.STRING);
        term.setTerm(value);
        result.addResult("?value", term);
        sparql.addResult(result);
        where.addWhere("?drep", "imagebyrepage");
        where.addWhere("?context", "agentimagebyrepage");
        where.addWhere("?name", "agentname");
        where.addWhere("?value", "valuebyrepage");
        sparql.addWhere(where);
        regname.setVariable("?name");
        regname.setPattern("A");
        regcontext.setVariable("?context");
        regcontext.setPattern("era1");
        term1.setPosition(Term.Position.FIRST);
        term1.setType(Term.TermType.VARIABLE);
        term1.setTerm(new String("?drep"));
        term2.setPosition(Term.Position.SECOND);
        term2.setType(Term.TermType.BOOLEAN);
        term2.setTerm(new Boolean(true));
        relational.setTerm1(term1);
        relational.setTerm2(term2);
        relational.setRelationalOp(RelExpr.RelationalOp.EQ);
        logical1.setLogicalOp(LogExpr.LogicalOp.AND);
        logical1.addExpression(regname);
        logical1.addExpression(regcontext);
        logical2.setLogicalOp(LogExpr.LogicalOp.AND);
        logical2.addExpression(logical1);
        logical2.addExpression(relational);
        filter.addItem(regname);
        filter.addItem(regcontext);
        filter.addItem(logical1);
        filter.addItem(term1);
        filter.addItem(term2);
        filter.addItem(relational);
        filter.addItem(logical2);
        sparql.addFilter(filter);
        return sparql;
    }
}
