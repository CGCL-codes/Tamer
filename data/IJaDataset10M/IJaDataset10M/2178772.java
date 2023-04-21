package eu.actorsproject.xlim.decision;

import java.util.Collections;
import eu.actorsproject.util.Pair;
import eu.actorsproject.util.XmlElement;
import eu.actorsproject.xlim.XlimTopLevelPort;

/**
 * Represents a blocking condition: pinAvail(port) >= N
 */
public class BlockingCondition extends Pair<XlimTopLevelPort, Integer> implements XmlElement, Comparable<BlockingCondition> {

    public BlockingCondition(XlimTopLevelPort port, int tokenCount) {
        super(port, tokenCount);
    }

    public BlockingCondition(AvailabilityTest failedTest) {
        super(failedTest.getPort(), failedTest.getTokenCount());
    }

    public XlimTopLevelPort getPort() {
        return mFirst;
    }

    public int getTokenCount() {
        return mSecond;
    }

    @Override
    public String getTagName() {
        return "pinWait";
    }

    @Override
    public int compareTo(BlockingCondition otherCond) {
        XlimTopLevelPort.Direction thisDir = getPort().getDirection();
        XlimTopLevelPort.Direction otherDir = otherCond.getPort().getDirection();
        int result = thisDir.compareTo(otherDir);
        if (result == 0) {
            String thisPortName = getPort().getSourceName();
            String otherPortName = otherCond.getPort().getSourceName();
            result = thisPortName.compareTo(otherPortName);
            if (result == 0) result = getTokenCount() - otherCond.getTokenCount();
        }
        return result;
    }

    @Override
    public Iterable<? extends XmlElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getAttributeDefinitions() {
        return "portName=\"" + getPort().getSourceName() + "\" size=\"" + getTokenCount() + "\"";
    }
}
