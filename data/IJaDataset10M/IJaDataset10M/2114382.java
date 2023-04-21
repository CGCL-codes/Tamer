package eu.actorsproject.xlim.decision2;

import java.util.Collections;
import java.util.Map;
import eu.actorsproject.util.XmlAttributeFormatter;
import eu.actorsproject.util.XmlElement;
import eu.actorsproject.xlim.XlimOperation;
import eu.actorsproject.xlim.XlimSource;
import eu.actorsproject.xlim.XlimTestModule;
import eu.actorsproject.xlim.XlimTopLevelPort;
import eu.actorsproject.xlim.dependence.ValueNode;
import eu.actorsproject.xlim.util.LiteralPattern;

/**
 * A Condition, which is associated with a DecisionNode.
 * The subclass AvailabilityTest represents the special case
 * of token/space availability on in-/out-port
 */
public class AtomicCondition extends Condition {

    private static LiteralPattern sTruePattern = new LiteralPattern(1);

    public AtomicCondition(XlimSource xlimSource, ValueNode value) {
        super(xlimSource, value);
    }

    @Override
    public <Result, Arg> Result accept(Visitor<Result, Arg> visitor, Arg arg) {
        return visitor.visitAtomicCondition(this, arg);
    }

    @Override
    public Iterable<? extends Condition> getTerms() {
        return Collections.singleton(this);
    }

    @Override
    public boolean alwaysTrue() {
        return sTruePattern.matches(getXlimSource());
    }

    @Override
    public boolean testsAvailability() {
        return false;
    }

    @Override
    public String getTagName() {
        return "condition";
    }

    @Override
    public String getAttributeDefinitions(XmlAttributeFormatter formatter) {
        XlimSource source = getXlimSource();
        return formatter.getAttributeDefinition("decision", source, source.getUniqueId());
    }

    @Override
    public Iterable<? extends XmlElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    protected void findPinAvail(Map<XlimTopLevelPort, XlimOperation> pinAvailMap) {
    }

    @Override
    protected void updateDominatingTests(Map<XlimTopLevelPort, Integer> testsInAncestors) {
    }

    @Override
    protected Condition removeRedundantTests(PortSignature portSignature, XlimTestModule testModule) {
        if (isImpliedBy(portSignature)) return null; else return this;
    }
}
