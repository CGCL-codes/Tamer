package org.eclipse.epsilon.fptc.test.acceptance;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.epsilon.fptc.system.Block;
import org.eclipse.epsilon.fptc.system.System;
import org.eclipse.epsilon.fptc.system.diagram.parser.ExpressionsParserFacade;
import org.eclipse.epsilon.fptc.test.FptcTest;
import org.eclipse.epsilon.fptc.test.fixtures.util.SystemConstructor;

public abstract class FptcAcceptanceTest extends FptcTest {

    protected static System system;

    public static void acceptanceTest(String hutn, String blockName, String behaviour) {
        acceptanceTest(hutn, Collections.singletonMap(blockName, behaviour));
    }

    public static void acceptanceTest(String hutn, Map<String, String> behaviours) {
        system = new SystemConstructor().construct(hutn);
        setBehaviours(behaviours);
    }

    private static void setBehaviours(Map<String, String> behaviours) {
        for (Entry<String, String> entry : behaviours.entrySet()) {
            setBehaviour(entry.getKey(), entry.getValue());
        }
    }

    private static void setBehaviour(String blockName, String faultBehaviour) {
        findBlock(blockName).setFaultBehaviour(new ExpressionsParserFacade().parse(faultBehaviour));
    }

    private static Block findBlock(String name) {
        for (Block block : system.getBlocks()) {
            if (name.equals(block.getName())) {
                return block;
            }
        }
        return null;
    }
}
