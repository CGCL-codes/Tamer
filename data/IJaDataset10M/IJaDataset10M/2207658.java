package csa.jportal.ai.enhancedAI;

import csa.jportal.ai.AIPlayer;
import csa.jportal.ai.enhancedAI.enhancedHints.AIEnhancedCardHints;
import csa.jportal.ai.enhancedAI.enhancedHints.HintAll;
import csa.jportal.ai.enhancedAI.enhancedHints.HintOccurrence;
import csa.jportal.ai.enhancedAI.enhancedHints.HintTargetTypes;
import csa.jportal.ai.enhancedAI.enhancedSim.CardSim;
import csa.jportal.ai.enhancedAI.enhancedSim.CardSimList;
import csa.jportal.ai.enhancedAI.enhancedSim.CombatSimConfig;
import csa.jportal.ai.enhancedAI.enhancedSim.VirtualMatch;
import csa.jportal.ai.namedai.NamedAIData;
import csa.jportal.ai.namedai.NamedAIDataPool;
import csa.jportal.card.Card;
import csa.jportal.carddeck.CardDeck;
import csa.jportal.match.Match;
import csa.jportal.match.MatchComputerPlayer;
import csa.jportal.match.MatchConstants;
import csa.jportal.match.communication.Communication;
import csa.jportal.match.communication.ManaPayment;

/**
 *
 * @author malban
 *
 * Difference:
 * - Mana will be put into a pool befor card is played activated
 * - and payed after card played activated
 * - in opposite to mana collected after the card is played
 *
 * Erstelle Fahrpläne für eine Runde
 * Merke vorasussetzungen pro Runde, so dass nicht mehrmels evaluiert werden muss
 * die Aktionen dienen als Grundlage einer Simulation
 *
 * Im Grund wichtig sind die Phasen:
 * 1) Main
 * 2) Attack
 * 3) Block
 * 4) Main2
 *
 * Jede Phase erhält als INPUT eine "Voraussetzung" aus der anderen Phase - oder der WIrklichkeit
 * - auf Grund dessen wird eine Bewertung durchgeführt
 * Wenn alle Kombintaionen getestet sind gewinnt eine durch "Wichtung"
 * Das kann eine Kombi sein, die in den ersten 2 Runden schlechtere Wichtungen hat als andere
 * Aber die Gesamtwichtung besser ist.
 *
 * Benötigt:
 *  - Ergebnis einer Wichtung über die 4 Runden
 *  - Simulation auf Basis eines Einstiegs an beliebiger Stelle der 4 Runden
 *  - Codierte EInschränkungen, die "allgemein" gemacht werdne können, um die Kombintaionsvielfallt einzuschränken
 *  - dies kann Tests binhalten:
 *    * fast tot
 *    * kann mit gewalt gegner töten
 *    * Auswertung Kreaturen Anzahl
 *    * Prioriserung / Damage Gegner / Damage Creaturen / Survive / Receive less damage
 *  ....
 *
 *
 */
public class EnhancedAI extends AbstractEnhancedAI implements MatchConstants, EAIConstants {

    public static boolean PauseAfterPlaning = true;

    EAIActionStack mAStack = new EAIActionStack();

    EAIActionStack mAStackStack = new EAIActionStack();

    EAIActionStack mAStackSave = null;

    String mCurrentKey = "";

    String lastRoundKey = "";

    String lastActionExecuteKey = "";

    EAIPlan plan = null;

    EAIPlan adHocPlan = null;

    EAISchedule stackPlan = null;

    int lastHandSize = 100;

    int lastRound = -1;

    int lastStep = -1;

    boolean roundInitDone = false;

    boolean stepInit = false;

    boolean inPayment = false;

    boolean inBlockTargetting = false;

    private static EnhancedAI mHintAI = null;

    private static MatchComputerPlayer mHintPlayer = null;

    public static EnhancedAI getHintAI() {
        if (mHintAI == null) {
            mHintAI = new EnhancedAI();
            String aiName = "HintNAI";
            NamedAIDataPool mNamedAIDataPool = new NamedAIDataPool();
            NamedAIData namedAIData = mNamedAIDataPool.get(aiName);
            AIPlayer cplayer = new AIPlayer(namedAIData.getAIUsed());
            cplayer.mNamedAI = namedAIData;
            cplayer.setSelectedDeckName("NoDeck");
            CardDeck.mDummyLoadingActive = true;
            mHintPlayer = new MatchComputerPlayer(cplayer);
            CardDeck.mDummyLoadingActive = false;
        }
        return mHintAI;
    }

    public void setMatch(Match m, int no) {
        E = new Environment(mHintPlayer, m, no);
        helper = new EAIHelper(E);
    }

    public static MatchComputerPlayer getHintPlayer() {
        return mHintPlayer;
    }

    public EAIActionStack getEAIStack() {
        return mAStack;
    }

    String generateLastRoundKey() {
        String key = "";
        key += "" + E.match.getRound();
        key += "" + E.match.getPhase();
        if (lastHandSize < E.match.getHand(E.player).size()) {
            key += "" + E.match.getHand(E.player).size();
        }
        lastHandSize = E.match.getHand(E.player).size();
        return key;
    }

    static String generateCurrentMatchKey(Match match, int playNo) {
        String key = "";
        key += match.getRound();
        key += match.getLibrary(playNo).size();
        key += match.getHand(playNo).size();
        key += match.getLand(playNo).size();
        key += match.getLand(playNo).tapStates();
        key += match.getGraveyard(playNo).size();
        key += match.getBattlefield(playNo).size();
        key += match.getBattlefield(playNo).tapStates();
        key += match.getAttacker().size();
        return key;
    }

    static String generateCurrentVMatchKey(VirtualMatch match, int playNo) {
        String key = "";
        key += match.getRound();
        key += match.getLibrary(playNo).size();
        key += match.getHand(playNo).size();
        key += match.getLand(playNo).size();
        key += match.getLand(playNo).tapStates();
        key += match.getGraveyard(playNo).size();
        key += match.getBattlefield(playNo).size();
        key += match.getBattlefield(playNo).tapStates();
        key += match.getAttacker().size();
        return key;
    }

    static String generateCurrentVMatchKeyID(VirtualMatch match, int playNo) {
        String key = "";
        key += match.getRound();
        if (match.getLibrary(playNo).size() > 0) {
            key += match.getLibrary(playNo).getCard(0).toString();
            key += match.getLibrary(playNo).getCard(match.getLibrary(playNo).size() - 1).toString();
            key += match.getLibrary(playNo).size();
        }
        key += match.getHand(playNo).toIDString();
        key += match.getLand(playNo).size();
        key += match.getLand(playNo).tapStates();
        key += match.getGraveyard(playNo).size();
        key += match.getBattlefield(playNo).toIDString();
        key += match.getBattlefield((playNo + 1) % 2).toIDString();
        key += match.getAttacker().size();
        key += "0:" + match.getLife(0);
        key += "1:" + match.getLife(1);
        return key;
    }

    public void resetPlanKey() {
        lastActionExecuteKey = "-1";
    }

    String getCurrentPlanKey() {
        return lastActionExecuteKey;
    }

    void generatePlanStack() {
        if (mAStackStack.size() > 0) {
            if (mAStackStack.read().started) mAStackStack.reset();
        }
        stackPlan = EAIPlan.createStackSchedule(E.match.getPhase(), E, true);
    }

    void generatePlan() {
        if (mAStackStack.size() > 0) {
            if (mAStackStack.read().started) mAStackStack.reset();
        }
        if ((!E.match.isMyTurn(E.pNumber)) && (E.match.getPhase() != MatchConstants.PHASE_COMBAT_DECLARE_BLOCKERS)) {
            return;
        }
        String planKey = getCurrentPlanKey();
        String matchKey = generateCurrentMatchKey(E.match, E.pNumber);
        if (lastStep <= E.match.getPhase()) {
            lastStep = E.match.getPhase();
            if ((planKey.equals(matchKey)) && (plan != null)) return;
        }
        lastStep = E.match.getPhase();
        plan = new EAIPlan(E);
        mAStackSave = null;
    }

    @Override
    public void initRound(boolean isPlayer) {
        initEnvironment();
        resetPlanKey();
        roundInitDone = lastRound == E.match.getRound();
        lastRound = E.match.getRound();
        stepInit = false;
        inPayment = false;
        lastHandSize = 100;
        inBlockTargetting = false;
    }

    @Override
    public boolean handleStack() {
        initEnvironment();
        if ((E.match.getStackSize() == 0) && (E.match.isMyTurn(E.pNumber))) {
            if (E.match.getPhase() == MatchConstants.PHASE_COMBAT_DECLARE_BLOCKERS) {
                if (E.match.isOpponentStackPass(E.pNumber)) {
                    boolean nothingDoneInStack = true;
                    generatePlanStack();
                    nothingDoneInStack = executeStackPlan();
                    return nothingDoneInStack;
                }
            }
            return true;
        }
        if (!E.match.isMyTurn(E.pNumber)) {
            int phase = E.match.getPhase();
            boolean goOn = false;
            if (phase == MatchConstants.PHASE_MAIN1) goOn = true;
            if (phase == MatchConstants.PHASE_MAIN2) goOn = true;
            if (phase == MatchConstants.PHASE_COMBAT_DECLARE_ATTACKERS) goOn = true;
            if (phase == MatchConstants.PHASE_COMBAT_DECLARE_BLOCKERS) goOn = true;
            if (!goOn) return true;
        } else {
            if (E.match.getStackSize() != 0) {
                Card lastStackCard = E.match.getStackCard(E.match.getStackSize() - 1);
                if (E.match.getIntOwner(lastStackCard) != E.pNumber) {
                    mAStackSave = mAStack;
                    mAStack = new EAIActionStack();
                    plan = null;
                }
            }
        }
        if (E.match.getStackSize() != 0) {
            Card lastStackCard = E.match.getStackCard(E.match.getStackSize() - 1);
            if (E.match.getIntOwner(lastStackCard) == E.pNumber) {
                if (mAStackSave != null) mAStack = mAStackSave;
                mAStackSave = null;
                return true;
            }
        }
        boolean nothingDoneInStack = true;
        generatePlanStack();
        nothingDoneInStack = executeStackPlan();
        return nothingDoneInStack;
    }

    public void generateHintData() {
        initEnvironment();
        generatePlan();
    }

    @Override
    public boolean doMainPhase() {
        redo = 0;
        initEnvironment();
        generatePlan();
        boolean result = executePlan();
        if (redo == 1) {
            redo = 0;
            generatePlan();
            result = executePlan();
            E.D.addLog("Redo - done!");
        }
        return result;
    }

    @Override
    public boolean declareAttackers() {
        initEnvironment();
        generatePlan();
        return executePlan();
    }

    @Override
    public boolean declareBlockers() {
        initEnvironment();
        generatePlan();
        return executePlan();
    }

    @Override
    public boolean untapCards() {
        initEnvironment();
        generatePlan();
        return executePlan();
    }

    @Override
    public void handleCommunicationRequest(Communication c) {
        int communicationType = E.match.getCurrentCommunication().getType();
        initEnvironment(c);
        switch(communicationType) {
            case COMMUNICATION_CACTIVATION_MANA_PAYMENT_I:
                {
                    doManaPayment(c);
                    break;
                }
            case COMMUNICATION_CMANA_PAYMENT_I:
                {
                    doManaPayment(c);
                    break;
                }
            case COMMUNICATION_CPOINT_BLOCKER_TO_ATTACKER_I:
                {
                    doPointBlockerToAttacker(c);
                    break;
                }
            case COMMUNICATION_CNOTHING_I:
                {
                    E.D.addLog("EAI: handleCommunicationRequest() (NOTHING) not done yet using super.", 3);
                    super.handleCommunicationRequest(c);
                    break;
                }
            case COMMUNICATION_CYES_NO_I:
                {
                    if (c.E != null) {
                        if (c.E.mInitiatorCard != null) {
                            CardSim initiator = new CardSim(c.E.mInitiatorCard, c.E.mInitiatorMatch.getIntOwner(c.E.mInitiatorCard));
                            EAIAction action = mAStack.read();
                            if (action != null) {
                                if (action.targets.size() > 0) {
                                    EAIAction subAction;
                                    subAction = action.targets.elementAt(0);
                                    action.targets.removeElement(subAction);
                                    if (subAction.type == EAIAction.ACTION_YES_NO) {
                                        subAction.source = c;
                                        subAction.successfull = true;
                                        subAction.cancled = false;
                                        subAction.maybe = false;
                                        subAction.started = true;
                                        mAStack.put(subAction);
                                        executePlan();
                                        return;
                                    }
                                }
                            }
                            AIEnhancedCardHints aHints = AIEnhancedCardHints.getHints(initiator);
                            String key = HintAll.HINT_SITUATION_BLOCKED;
                            if (aHints.hasHint(key, HintOccurrence.O_WHEN_BLOCKED)) {
                                if (aHints.hasHint(key, HintTargetTypes.TY_DAMAGE_AS_NON_BLOCKED)) {
                                    VirtualMatch vMatch = new VirtualMatch(c.E.mInitiatorMatch);
                                    int damageToDo = initiator.getNowPower();
                                    CombatSimConfig configTMP = new CombatSimConfig();
                                    configTMP.attackers = new CardSimList(initiator);
                                    configTMP.blockerLands = vMatch.getLand((initiator.getOwner() + 1) % 2);
                                    configTMP.attackerLands = vMatch.getLand(initiator.getOwner());
                                    csa.jportal.ai.enhancedAI.enhancedSim.SingleFight sFight = csa.jportal.ai.enhancedAI.enhancedSim.SingleFight.getFight(initiator, vMatch.getBlocker(initiator), configTMP, false);
                                    vMatch.getAttacker().remove(initiator);
                                    int oldOpponentHealth = vMatch.getLife((initiator.getOwner() + 1) % 2);
                                    vMatch.doCombat(initiator.getOwner());
                                    int newOpponentHealth = vMatch.getLife((initiator.getOwner() + 1) % 2);
                                    int otherDamage = oldOpponentHealth - newOpponentHealth;
                                    boolean damagePlayer = EAIPlanTree.doRatherPlayerDamage(new VirtualMatch(c.E.mInitiatorMatch), sFight, initiator.getOwner(), otherDamage);
                                    c.setBooleanResult(damagePlayer);
                                    c.setSuccessfull(true);
                                    return;
                                }
                            }
                        }
                    }
                    E.D.addLog("EAI: handleCommunicationRequest() (YES NO) only partially done, using super.", 3);
                    super.handleCommunicationRequest(c);
                    break;
                }
            default:
                {
                    boolean translated = translateTargetToGeneric(c);
                    if (!translated) {
                        super.handleCommunicationRequest(c);
                        return;
                    }
                    doGenericTargetting(c);
                    break;
                }
        }
        return;
    }

    @Override
    public void doPointBlockerToAttacker(Communication c) {
        EAIAction action = mAStack.read();
        if (action == null) {
            E.D.addLog("EAI: Targetting info = null, using super.", 3);
            super.doPointBlockerToAttacker(c);
            return;
        }
        if ((action.targets.isEmpty()) && (!inBlockTargetting)) {
            E.D.addLog("EAI: Targetting info empty, using super.", 3);
            super.doPointBlockerToAttacker(c);
            return;
        }
        inBlockTargetting = false;
        action.targets.elementAt(0).source = c;
        action.targets.elementAt(0).successfull = true;
        action.targets.elementAt(0).cancled = false;
        action.targets.elementAt(0).maybe = false;
        EAIAction tAction = action.targets.elementAt(0);
        mAStack.put(tAction);
        executePlan();
    }

    @Override
    public void doManaPayment(Communication c) {
        if (mAStackStack.read() != null) {
            E.D.addLog("EAI:Stack action found -> considering using that!.", 3);
            doStackManaPayment(c);
            return;
        }
        EAIAction action = mAStack.read();
        E.D.addLog("EAI:Mana Payment entered.", 3);
        if (action == null) {
            E.D.addLog("EAI: No action known (either instant from old AI code, or triggered Mana need).", 3);
            ManaPayment mana = c.manaExpected;
            Card card = c.E.mInitiatorCard;
            if ((mana != null) && (mana.paymentType == ManaPayment.MP_TRIGGER_ACTIVATE_CARD)) {
                E.D.addLog("EAI: triggered Mana encountered.", 3);
                if (adHocPlan == null) {
                    adHocPlan = new EAIPlan(E, c);
                    plan = null;
                }
                EAISchedule s = adHocPlan.getSchedule(0);
                if ((s != null) && (s.size() == 1)) {
                    if (mAStack.size() == 0) {
                        action = s.getNext();
                        if (action != null) {
                            action.started = true;
                            if (action.type == ACTION_PAY_MANA) {
                                action.source = c;
                                action.successfull = false;
                                action.cancled = false;
                                action.maybe = true;
                            }
                            mAStack.put(action);
                        }
                    }
                    executeAction();
                    return;
                }
                lastActionExecuteKey = "";
                if (s == null) {
                    super.doManaPayment(c);
                    return;
                }
                if (mAStack.size() == 0) {
                    action = s.getNext();
                    if (action != null) {
                        action.started = true;
                        if (action.type == ACTION_PAY_MANA) {
                            action.source = c;
                            action.successfull = true;
                            action.cancled = false;
                            action.maybe = false;
                        }
                        mAStack.put(action);
                    }
                }
                executeAction();
                return;
            }
            E.D.addLog("EAI: manapayment null() using super.", 3);
            super.doManaPayment(c);
            return;
        }
        if ((action.manaPayment.isEmpty()) && (!inPayment)) {
            E.D.addLog("EAI: manapayment null() using super (2).", 3);
            super.doManaPayment(c);
        }
        inPayment = true;
        if (!action.paymentInitiated) {
            if (action.manaPayment.size() > 0) {
                action.manaPayment.elementAt(action.manaPayment.size() - 1).source = c;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).successfull = true;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).cancled = false;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).maybe = false;
                for (int i = action.manaPayment.size() - 1; i >= 0; i--) {
                    EAIAction subAction = action.manaPayment.elementAt(i);
                    subAction.started = true;
                    mAStack.put(subAction);
                }
            }
            action.paymentInitiated = true;
        }
        executePlan();
    }

    public void doStackManaPayment(Communication c) {
        EAIAction action = mAStackStack.read();
        E.D.addLog("EAI:Stack Mana Payment entered.", 3);
        if ((action.manaPayment.isEmpty()) && (!inPayment)) {
            E.D.addLog("EAI: manapayment null() using super (3).", 3);
            super.doManaPayment(c);
        }
        inPayment = true;
        if (!action.paymentInitiated) {
            if (action.manaPayment.size() > 0) {
                action.manaPayment.elementAt(action.manaPayment.size() - 1).source = c;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).successfull = true;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).cancled = false;
                action.manaPayment.elementAt(action.manaPayment.size() - 1).maybe = false;
                for (int i = action.manaPayment.size() - 1; i >= 0; i--) {
                    EAIAction subAction = action.manaPayment.elementAt(i);
                    subAction.started = true;
                    mAStackStack.put(subAction);
                }
            }
            action.paymentInitiated = true;
        }
        executeStackPlan();
    }

    void ActionError(int code) {
        E.D.addLog(EAI_ERROR_STRINGS[code], 3);
    }

    protected void unexpectedTargetting(Communication c) {
        E.D.addLog("EAI: Targeting() not done yet using super.", 3);
        if (c.E != null) {
            if (c.E.mInitiatorCard != null) {
                CardSim initiator = new CardSim(c.E.mInitiatorCard, c.E.mInitiatorMatch.getIntOwner(c.E.mInitiatorCard));
                AIEnhancedCardHints aHints = AIEnhancedCardHints.getHints(initiator);
                String key = HintAll.HINT_SITUATION_ATTACKER;
                if (aHints.hasHint(key, HintOccurrence.O_WHEN_NOT_BLOCKED)) {
                    if (aHints.hasHint(key, HintTargetTypes.TY_DAMAGE_AS_BLOCKED)) {
                        VirtualMatch vMatch = new VirtualMatch(c.E.mInitiatorMatch);
                        int damageToDo = initiator.getNowPower();
                        vMatch.getAttacker().remove(initiator);
                        CardSimList attackersToRemove = new CardSimList(c.E.mInitiatorMatch.getAttacksDone());
                        vMatch.getAttacker().removeListDirect(attackersToRemove);
                        vMatch.doCombat(initiator.getOwner());
                        EAIPlanTree.getBestDamageTarget(vMatch, damageToDo, initiator.getOwner(), c);
                        return;
                    }
                }
            }
        }
        super.doSpecificTargeting(c);
    }

    public void doGenericTargetting(Communication c) {
        if (mAStackStack.read() != null) {
            E.D.addLog("EAI:doGenericTargetting Stack action found -> considering using that!.", 3);
            doStackGenericTargetting(c);
            return;
        }
        EAIAction action = mAStack.read();
        if (action == null) {
            ActionError(EAI_CANT_TARGET_ACTION_NULL);
            unexpectedTargetting(c);
            return;
        } else if (action.type == EAIAction.ACTION_TARGET_CARD) {
            if (!action.started) {
                ActionError(EAI_TARGET_NOT_INITIALIZED);
                unexpectedTargetting(c);
                return;
            }
        } else if (action.targets.isEmpty()) {
            if (action.iDidMyOwnTargetting) {
                c.setCancled(false);
                c.setMaybeTaken(true);
                c.setCardResult(null);
                c.setSuccessfull(true);
                return;
            }
            ActionError(EAI_CANT_TARGET_EMPTY);
            unexpectedTargetting(c);
            return;
        } else if ((action.targets.elementAt(0).getCard() != null) && (action.targets.elementAt(0).getCard().isDummy())) {
            ActionError(EAI_IS_DUMMY);
            EAIAction subAction = action.targets.elementAt(action.targets.size() - 1);
            action.targets.removeElement(subAction);
            unexpectedTargetting(c);
            return;
        } else if (!action.targetsInitiated) {
            EAIAction subAction;
            if (action.type == EAIAction.ACTION_PAY_MANA) {
                subAction = action.targets.elementAt(action.targets.size() - 1);
                action.targets.removeElement(subAction);
            } else {
                subAction = action.targets.elementAt(0);
                action.targets.removeElement(subAction);
                action.iDidMyOwnTargetting = true;
            }
            subAction.source = c;
            subAction.successfull = true;
            subAction.cancled = false;
            subAction.maybe = false;
            subAction.started = true;
            mAStack.put(subAction);
        }
        executePlan();
    }

    public void doStackGenericTargetting(Communication c) {
        EAIAction action = mAStackStack.read();
        if (action.type == EAIAction.ACTION_TARGET_CARD) {
            if (!action.started) {
                ActionError(EAI_TARGET_NOT_INITIALIZED);
                unexpectedTargetting(c);
                return;
            }
        } else if (action.targets.isEmpty()) {
            ActionError(EAI_CANT_TARGET);
            unexpectedTargetting(c);
            return;
        } else if ((action.targets.elementAt(0).getCard() != null) && (action.targets.elementAt(0).getCard().isDummy())) {
            ActionError(EAI_IS_DUMMY);
            EAIAction subAction = action.targets.elementAt(action.targets.size() - 1);
            action.targets.removeElement(subAction);
            unexpectedTargetting(c);
            return;
        } else if (!action.targetsInitiated) {
            EAIAction subAction = action.targets.elementAt(action.targets.size() - 1);
            action.targets.removeElement(subAction);
            subAction.source = c;
            subAction.successfull = true;
            subAction.cancled = false;
            subAction.maybe = false;
            subAction.started = true;
            mAStackStack.put(subAction);
        }
        executeStackPlan();
    }

    public boolean executePlan() {
        EAISchedule s = plan.getSchedule(lastStep);
        if (s == null) return true;
        EAIAction action = null;
        if (mAStack.size() == 0) {
            mAStackSave = null;
            action = s.getNext();
            if (action != null) {
                action.started = true;
                mAStack.put(action);
            }
        }
        executeAction();
        return ((s.isAllFinished()) && (mAStack.size() == 0));
    }

    public boolean executeStackPlan() {
        EAISchedule s = stackPlan;
        if (s == null) return true;
        if (s.size() == 0) return true;
        EAIAction action = null;
        if (mAStackStack.size() == 0) {
            action = s.getNext();
            if (action != null) {
                action.started = true;
                mAStackStack.put(action);
            }
        }
        executeStackAction();
        if ((s.isAllFinished()) && (mAStackStack.size() == 0)) {
            stackPlan = null;
        }
        return ((s.isAllFinished()) && (mAStackStack.size() == 0));
    }

    boolean executeStackAction() {
        EAIAction action = mAStackStack.read();
        if (action == null) return true;
        if (safety > 100) {
            E.D.addLog("\n!!!!!\nEAI ERROR!\n!!!!!!!");
            System.out.println("EAI ERROR!!!");
            E.match.setPause(true);
            action.forceFinished();
            stackPlan = null;
            adHocPlan = null;
            while ((mAStack.read() != null) && (mAStack.read().isFinished())) {
                mAStack.get();
            }
            while ((mAStackStack.read() != null) && (mAStackStack.read().isFinished())) {
                mAStackStack.get();
            }
            mAStackSave = null;
            return mAStackStack.size() == 0;
        }
        executeAction(action);
        while ((mAStackStack.read() != null) && (mAStackStack.read().isFinished())) {
            mAStackStack.get();
        }
        return mAStackStack.size() == 0;
    }

    /** Executes actions on the real match
     *
     * returns true if the current action from the stack was completely finished, false otherwise
     *
     * removes the just finished action from the Action stack
     *
     *
     * @return
     */
    boolean executeAction() {
        if (oldTurn != E.match.getRound() + E.match.getPhase()) {
            oldTurn = E.match.getRound() + E.match.getPhase();
            safety = 0;
        }
        safety++;
        EAIAction action = mAStack.read();
        if (action == null) return true;
        if (safety > 100) {
            E.D.addLog("\n!!!!!\nEAI ERROR!\n!!!!!!!");
            System.out.println("EAI ERROR!!!");
            E.match.setPause(true);
            action.forceFinished();
            adHocPlan = null;
            stackPlan = null;
            while ((mAStackStack.read() != null) && (mAStackStack.read().isFinished())) {
                mAStackStack.get();
            }
            while ((mAStack.read() != null) && (mAStack.read().isFinished())) {
                mAStack.get();
            }
            mAStackSave = null;
            return mAStack.size() == 0;
        }
        executeAction(action);
        lastActionExecuteKey = action.getVMatchKeyAfter();
        while ((mAStack.read() != null) && (mAStack.read().isFinished())) {
            mAStack.get();
        }
        if (mAStack.size() == 0) mAStackSave = null;
        return mAStack.size() == 0;
    }

    int safety = 0;

    int oldTurn = -1;

    int redo = 0;

    void executeAction(EAIAction action) {
        switch(action.type) {
            case ACTION_PLAY_CARD:
                {
                    if (!E.match.isInHandUID(E.player, action.getCard())) {
                        resetPlanKey();
                        E.D.addLog("\n!!!!!\nNon Fatal EAI Warning!\n!!!!!!!");
                        E.D.addLog("Trying to play - " + action.getCard() + " but card is not in hand...");
                        E.D.addLog("building new plan!");
                        action.forceFinished();
                        adHocPlan = null;
                        stackPlan = null;
                        mAStackStack.reset();
                        mAStack.reset();
                        mAStackSave = null;
                        redo = 1;
                        break;
                    }
                    E.match.playCard(E.player, action.getCard());
                    action.setFinished();
                    adHocPlan = null;
                    break;
                }
            case ACTION_CARD_AS_ATTACKER:
                {
                    E.match.declareAttacker(E.player, action.getCard());
                    action.setFinished();
                    break;
                }
            case ACTION_CARD_AS_BLOCKER:
                {
                    E.match.declareBlocker(E.player, action.getCard());
                    action.setFinished();
                    inBlockTargetting = true;
                    break;
                }
            case ACTION_TARGET_CARD:
                {
                    if (action.getCard() != null) if (action.getCard().isDummy()) {
                    }
                    action.source.setSuccessfull(action.successfull);
                    action.source.setCancled(action.cancled);
                    action.source.setMaybeTaken(action.maybe);
                    if (action.getCard() != null) {
                        if (action.successfull) {
                            action.source.E.mTargetCardTo = action.getCard();
                            action.source.setCardResult(action.getCard());
                        }
                    } else {
                        if (action.successfull) {
                            if (action.targetIsPlayer) {
                                action.source.E.mTargetPlayer = E.match.getPlayer(action.targetPlayerNumber);
                                action.source.setResultPlayer(E.match.getPlayer(action.targetPlayerNumber));
                            }
                        }
                    }
                    action.setFinished();
                    break;
                }
            case ACTION_ACTIVATE_CARD:
                {
                    E.match.playAbility(E.player, action.getCard());
                    action.setFinished();
                    break;
                }
            case ACTION_UNTAP_CARD:
                {
                    E.match.untapCard(E.player, action.getCard());
                    action.setFinished();
                    break;
                }
            case ACTION_PAY_MANA:
                {
                    action.source.setSuccessfull(action.successfull);
                    action.source.setCancled(action.cancled);
                    action.source.setMaybeTaken(action.maybe);
                    action.source.setResultMana(action.successfull);
                    action.setFinished();
                    adHocPlan = null;
                    inPayment = false;
                    break;
                }
            case ACTION_DRAW_CARD:
                {
                    E.match.drawLibraryCardToHand(E.player, 1);
                    action.setFinished();
                    break;
                }
            case ACTION_YES_NO:
                {
                    action.source.setBooleanResult(action.yesNo);
                    action.source.setSuccessfull(action.successfull);
                    action.source.setCancled(action.cancled);
                    action.source.setMaybeTaken(action.maybe);
                    action.setFinished();
                    break;
                }
        }
    }

    public EAISchedule getSchedule(int phase) {
        if (plan == null) return null;
        return plan.getSchedule(phase);
    }

    public EAIPlan getCurrentPlan() {
        return plan;
    }
}
