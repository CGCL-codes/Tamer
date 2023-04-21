package net.liveseeds.persistence;

import net.liveseeds.base.action.ActionSlot;
import net.liveseeds.base.action.MoveAction;
import net.liveseeds.base.action.ActionSlotSet;
import net.liveseeds.base.action.ActionsEnum;
import net.liveseeds.base.direction.DefaultDirection;
import net.liveseeds.base.direction.Direction;
import net.liveseeds.base.liveseed.DefaultLiveSeed;
import net.liveseeds.base.liveseed.Genotype;
import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.base.scenario.*;
import net.liveseeds.base.world.World;
import net.liveseeds.base.LiveSeedsException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class XMLPersistenceManager implements PersistenceManager {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(XMLPersistenceManager.class.getName());

    private static final String GENOTYPE_TAG = "genotype";

    private static final String SCENARIO_TAG = "scenario";

    private static final String CONDITION_TAG = "condition";

    private static final String LINE_NUMBER_ATT = "lineNumber";

    private static final String PARAMETER_TAG = "parameter";

    private static final String TYPE_ATT = "type";

    private static final String DIRECTION_ATT = "direction";

    private static final String NUMERIC_PARAM_ATT = "numericParam";

    private static final String GOTO_TAG = "goto";

    private static final String PLAIN_TAG = "plain";

    private static final String ACTIONREF_ATT = "actionref";

    private static final String ACTIONS_TAG = "actions";

    private static final String ACTION_TAG = "action";

    private static final String NAME_ATT = "name";

    private static final String PRICE_ATT = "price";

    private Document document;

    public void init(final Document document) {
        this.document = document;
    }

    private void checkInited() {
        if (document == null) {
            throw new IllegalStateException(RESOURCE_BUNDLE.getString("exception.illegal"));
        }
    }

    public void store(final LiveSeed liveSeed) throws PersistenceManagerException {
        checkInited();
        final Element genotypeElement = document.createElement(GENOTYPE_TAG);
        document.appendChild(genotypeElement);
        storeActions(genotypeElement, liveSeed.getActionSlotSet());
        storeScenario(genotypeElement, liveSeed.getScenario());
    }

    private void storeScenario(final Element genotypeElement, final LiveSeedScenario scenario) {
        final Element scenarioElement = document.createElement(SCENARIO_TAG);
        genotypeElement.appendChild(scenarioElement);
        final LiveSeedScenarioInstruction[] instructions = scenario.getInstructions();
        for (int i = 0; i < instructions.length; i++) {
            scenarioElement.appendChild(document.createComment(i + ": " + instructions[i].toString()));
            storeInstruction(scenarioElement, instructions[i]);
        }
    }

    private void storeInstruction(final Element scenarioElement, final LiveSeedScenarioInstruction instruction) {
        if (instruction instanceof PlainLiveSeedScenarioInstruction) {
            storePlainInstruction((PlainLiveSeedScenarioInstruction) instruction, scenarioElement);
        } else if (instruction instanceof GotoLiveSeedScenarioInstruction) {
            storeGotoInstruction((GotoLiveSeedScenarioInstruction) instruction, scenarioElement);
        } else if (instruction instanceof ConditionalLiveSeedScenarioInstruction) {
            storeConditionalInstruction((ConditionalLiveSeedScenarioInstruction) instruction, scenarioElement);
        }
    }

    private void storeConditionalInstruction(final ConditionalLiveSeedScenarioInstruction conditionalLiveSeedScenarioInstruction, final Element scenarioElement) {
        final Element conditionElement = document.createElement(CONDITION_TAG);
        conditionElement.setAttribute(LINE_NUMBER_ATT, Integer.toString(conditionalLiveSeedScenarioInstruction.getGotoLineNumber()));
        storeParameter(conditionElement, conditionalLiveSeedScenarioInstruction.getLowerBound());
        storeParameter(conditionElement, conditionalLiveSeedScenarioInstruction.getMedian());
        storeParameter(conditionElement, conditionalLiveSeedScenarioInstruction.getUpperBound());
        scenarioElement.appendChild(conditionElement);
    }

    private void storeParameter(final Element conditionElement, final Parameter parameter) {
        final Element parameterElement = document.createElement(PARAMETER_TAG);
        parameterElement.setAttribute(TYPE_ATT, parameter.getClass().getName());
        if (parameter instanceof AbstractResourceParameter) {
            final AbstractResourceParameter resourceParameter = (AbstractResourceParameter) parameter;
            if (resourceParameter.getDirection() != null) {
                parameterElement.setAttribute(DIRECTION_ATT, getDirecitonString(resourceParameter.getDirection()));
            }
        } else if (parameter instanceof ConstantParameter) {
            final ConstantParameter constantParameter = (ConstantParameter) parameter;
            parameterElement.setAttribute(NUMERIC_PARAM_ATT, Integer.toString(constantParameter.getValue()));
        }
        conditionElement.appendChild(parameterElement);
    }

    private void storeGotoInstruction(final GotoLiveSeedScenarioInstruction gotoLiveSeedScenarioInstruction, final Element scenarioElement) {
        final Element gotoElement = document.createElement(GOTO_TAG);
        gotoElement.setAttribute(LINE_NUMBER_ATT, Integer.toString(gotoLiveSeedScenarioInstruction.getGotoLineNumber()));
        scenarioElement.appendChild(gotoElement);
    }

    private void storePlainInstruction(final PlainLiveSeedScenarioInstruction plainInstruction, final Element scenarioElement) {
        final Element plainElement = document.createElement(PLAIN_TAG);
        plainElement.setAttribute(ACTIONREF_ATT, plainInstruction.getAction().getClass().getName());
        if (plainInstruction.getAction() instanceof MoveAction) {
            plainElement.setAttribute(DIRECTION_ATT, getDirecitonString(DefaultDirection.DIRECTIONS[plainInstruction.getParam()]));
        } else {
            plainElement.setAttribute(NUMERIC_PARAM_ATT, Integer.toString(plainInstruction.getParam()));
        }
        scenarioElement.appendChild(plainElement);
    }

    private void storeActions(final Element genotypeElement, final ActionSlotSet actionSlotSet) {
        final ActionSlot[] actionSlots = actionSlotSet.getActionSlots();
        final Element actionsElement = document.createElement(ACTIONS_TAG);
        genotypeElement.appendChild(actionsElement);
        for (int i = 0; i < actionSlots.length; i++) {
            storeAction(actionsElement, actionSlots[i]);
        }
    }

    private void storeAction(final Element actionsElement, final ActionSlot actionSlot) {
        final Element actionElement = document.createElement(ACTION_TAG);
        actionElement.setAttribute(NAME_ATT, actionSlot.getAction().getClass().getName());
        actionElement.setAttribute(PRICE_ATT, Integer.toString(actionSlot.getPrice()));
        actionsElement.appendChild(actionElement);
    }

    public LiveSeed restore(final World world) throws PersistenceManagerException {
        checkInited();
        final DefaultLiveSeed liveSeed = new DefaultLiveSeed(world);
        initActions(liveSeed);
        initScenario(liveSeed);
        liveSeed.setGenotype(new Genotype(world.getTime()));
        return liveSeed;
    }

    private void initScenario(final DefaultLiveSeed liveSeed) throws PersistenceManagerException {
        cleanUpScenario(liveSeed);
        final NodeList scenarioChildren = document.getElementsByTagName(SCENARIO_TAG).item(0).getChildNodes();
        try {
            for (int i = 0; i < scenarioChildren.getLength(); i++) {
                final Node node = scenarioChildren.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                final Element instructionElement = (Element) node;
                if (CONDITION_TAG.equals(instructionElement.getNodeName())) {
                    initConditionInstruction(instructionElement, liveSeed);
                } else if (PLAIN_TAG.equals(instructionElement.getNodeName())) {
                    initPlainInstruction(instructionElement, liveSeed);
                } else if (GOTO_TAG.equals(instructionElement.getNodeName())) {
                    initGotoInstruction(liveSeed, instructionElement);
                }
            }
        } catch (LiveSeedsException e) {
            throw new PersistenceManagerException(e);
        }
    }

    private void initConditionInstruction(final Element instructionElement, final DefaultLiveSeed liveSeed) throws LiveSeedsException, PersistenceManagerException {
        final NodeList elementsByTagName = instructionElement.getElementsByTagName(PARAMETER_TAG);
        liveSeed.getScenario().addInstruction(new ConditionalLiveSeedScenarioInstruction(liveSeed.getScenario(), initParameter((Element) elementsByTagName.item(0), liveSeed), initParameter((Element) elementsByTagName.item(1), liveSeed), initParameter((Element) elementsByTagName.item(2), liveSeed), Integer.parseInt(instructionElement.getAttribute(LINE_NUMBER_ATT))));
    }

    private static void initGotoInstruction(final DefaultLiveSeed liveSeed, final Element instructionElement) throws LiveSeedsException {
        liveSeed.getScenario().addInstruction(new GotoLiveSeedScenarioInstruction(liveSeed.getScenario(), Integer.parseInt(instructionElement.getAttribute(LINE_NUMBER_ATT))));
    }

    private void initPlainInstruction(final Element instructionElement, final DefaultLiveSeed liveSeed) throws PersistenceManagerException, LiveSeedsException {
        final int actionIndex = getActionIndex(instructionElement.getAttribute(ACTIONREF_ATT), liveSeed);
        final String directionAttribute = instructionElement.getAttribute(DIRECTION_ATT);
        final String numericParamAttribute = instructionElement.getAttribute(NUMERIC_PARAM_ATT);
        final int param;
        if (directionAttribute != null && directionAttribute.length() > 0) {
            param = getDirection(directionAttribute).getCode();
        } else if (numericParamAttribute != null && numericParamAttribute.length() > 0) {
            param = Integer.parseInt(numericParamAttribute);
        } else {
            param = 0;
        }
        liveSeed.getScenario().addInstruction(new PlainLiveSeedScenarioInstruction(liveSeed.getScenario(), actionIndex, param));
    }

    private static Direction getDirection(final String directionAttribute) throws PersistenceManagerException {
        for (int i = 0; i < DefaultDirection.DIRECTIONS.length; i++) {
            final Direction direction = DefaultDirection.DIRECTIONS[i];
            if (directionAttribute.equals(direction.getDescription())) {
                return direction;
            }
        }
        throw new PersistenceManagerException(MessageFormat.format(RESOURCE_BUNDLE.getString("exception.unknownDirection"), new Object[] { directionAttribute }));
    }

    private int getActionIndex(final String actionref, final DefaultLiveSeed liveSeed) throws PersistenceManagerException {
        for (int i = 0; i < ActionsEnum.AMOUNT; i++) {
            final ActionSlot actionSlot = liveSeed.getActionSlotSet().get(i);
            if (actionSlot.getAction().getClass().getName().equals(actionref)) {
                return i;
            }
        }
        throw new PersistenceManagerException(MessageFormat.format(RESOURCE_BUNDLE.getString("exception.unknownAction"), new Object[] { actionref }));
    }

    private static Parameter initParameter(final Element paramElement, final LiveSeed liveSeed) throws PersistenceManagerException {
        try {
            final String name = paramElement.getAttribute(TYPE_ATT);
            final Class parameterClass = Class.forName(name);
            final String directionParamString = paramElement.getAttribute(DIRECTION_ATT);
            final String numericParamString = paramElement.getAttribute(NUMERIC_PARAM_ATT);
            if (AbstractResourceParameter.class.isAssignableFrom(parameterClass)) {
                final Direction direction;
                if (directionParamString != null && directionParamString.length() > 0) {
                    direction = getDirection(directionParamString);
                } else {
                    direction = null;
                }
                return (Parameter) parameterClass.getConstructor(new Class[] { Direction.class }).newInstance(new Object[] { direction });
            } else if (ResourceParameter.class.isAssignableFrom(parameterClass)) {
                return (Parameter) parameterClass.getConstructor(new Class[] {}).newInstance(new Object[] {});
            } else if (ConstantParameter.class.isAssignableFrom(parameterClass)) {
                final Integer numericValue = new Integer(numericParamString);
                return (Parameter) parameterClass.getConstructor(new Class[] { int.class }).newInstance(new Object[] { numericValue });
            } else {
                throw new PersistenceManagerException(MessageFormat.format(RESOURCE_BUNDLE.getString("exception.unknownParam"), new Object[] { name }));
            }
        } catch (ClassNotFoundException e) {
            throw new PersistenceManagerException(e);
        } catch (NoSuchMethodException e) {
            throw new PersistenceManagerException(e);
        } catch (IllegalAccessException e) {
            throw new PersistenceManagerException(e);
        } catch (InvocationTargetException e) {
            throw new PersistenceManagerException(e);
        } catch (InstantiationException e) {
            throw new PersistenceManagerException(e);
        }
    }

    private static void cleanUpScenario(final DefaultLiveSeed liveSeed) throws PersistenceManagerException {
        try {
            while (liveSeed.getScenario().getLength() > 0) {
                liveSeed.getScenario().removeInstruction(0);
            }
        } catch (LiveSeedsException e) {
            throw new PersistenceManagerException(e);
        }
    }

    private void initActions(final DefaultLiveSeed liveSeed) {
        final NodeList actionElements = document.getElementsByTagName(ACTION_TAG);
        for (int i = 0; i < actionElements.getLength(); i++) {
            final Element actionElement = (Element) actionElements.item(i);
            for (int j = 0; j < ActionsEnum.AMOUNT; j++) {
                final ActionSlot actionSlot = liveSeed.getActionSlotSet().get(j);
                if (actionSlot.getClass().getName().equals(actionElement.getAttribute(NAME_ATT))) {
                    actionSlot.setPrice(Integer.parseInt(actionElement.getAttribute(PRICE_ATT)));
                }
            }
        }
        liveSeed.getActionSlotSet().verifyActions();
    }

    private static String getDirecitonString(final Direction direction) {
        return direction.getDescription();
    }

    public static void main(final String[] args) throws Exception {
        ConstantParameter.class.getConstructor(new Class[] { int.class });
    }
}
