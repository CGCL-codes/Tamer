package ru.cos.sim.road.init.xml;

import java.util.HashSet;
import java.util.Set;
import org.jdom.Element;
import org.jdom.Namespace;
import ru.cos.sim.mdf.MDFReader;
import ru.cos.sim.road.init.data.TurnTRGroupData;

/**
 * @author zroslaw
 *
 */
public class TurnTRGroupDataReader {

    private static Namespace NS = MDFReader.MDF_NAMESPACE;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String TRANSITION_RULES = "TransitionRules";

    public static final String TRANSITION_RULE = "TransitionRule";

    public static final String TRANSITION_RULE_ID = "transitionRuleId";

    public static TurnTRGroupData read(Element turnTRGroupElement) {
        TurnTRGroupData turnGroupData = new TurnTRGroupData();
        Element idElement = turnTRGroupElement.getChild(ID, NS);
        turnGroupData.setId(Integer.parseInt(idElement.getText()));
        Element nameElement = turnTRGroupElement.getChild(NAME, NS);
        turnGroupData.setName(nameElement.getText());
        Set<Integer> transitionRuleIds = new HashSet<Integer>();
        for (Object transitionRuleObj : turnTRGroupElement.getChild(TRANSITION_RULES, NS).getChildren(TRANSITION_RULE, NS)) {
            Element transitionRuleElement = (Element) transitionRuleObj;
            Element transitionRuleIdElement = transitionRuleElement.getChild(TRANSITION_RULE_ID, NS);
            transitionRuleIds.add(Integer.parseInt(transitionRuleIdElement.getText()));
        }
        turnGroupData.setTransitionRuleIds(transitionRuleIds);
        return turnGroupData;
    }
}
