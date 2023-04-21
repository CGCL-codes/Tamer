package org.grandtestauto.distributed.test;

import org.grandtestauto.distributed.*;
import org.grandtestauto.test.tools.*;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class AgentDetailsTest {

    public boolean constructorTest() {
        AgentDetails ag = new AgentDetails("99", 99);
        assert ag.name().equals("99");
        return true;
    }

    public boolean nameTest() {
        AgentDetails ag = new AgentDetails("99", 99);
        assert ag.name().equals("99");
        return true;
    }

    public boolean toStringTest() {
        AgentDetails ag = new AgentDetails("99", 99);
        assert ag.toString().equals("99");
        return true;
    }

    public boolean maximumGradeTest() {
        AgentDetails ag = new AgentDetails("99", 999);
        assert ag.maximumGrade() == 999;
        return true;
    }

    public boolean serializationTest() {
        assert SerializationTester.check(new AgentDetails("99", 999));
        return true;
    }

    public boolean systemPropertiesTest() {
        Properties fromAgent = new AgentDetails("99", 999).systemProperties();
        Properties direct = System.getProperties();
        assert fromAgent.equals(direct);
        return true;
    }
}
