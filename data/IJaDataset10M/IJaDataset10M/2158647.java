package mw.server.card;

import groovy.lang.Closure;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mw.server.ChoiceCommand;
import mw.server.GameManager;
import mw.server.MWPlayer;
import mw.server.card.ability.AbilityList;
import mw.server.card.ability.factory.SpellEffectFactory;
import mw.server.card.ability.factory.SpellKickerFactory;
import mw.server.card.ability.factory.SpellTrapFactory;
import mw.server.card.ability.factory.TargetFactory;
import mw.server.card.compiler.CardCompiler;
import mw.server.card.compiler.ScriptInfo;
import mw.server.constant.Constant;
import mw.server.event.EventParam;
import mw.server.list.CardList;
import mw.server.list.CardListFilter;
import mw.server.list.SpellAbilityFilter;
import mw.server.model.Card;
import mw.server.model.MagicWarsModel;
import mw.server.model.MagicWarsModel.Color;
import mw.server.model.MagicWarsModel.GameZone;
import mw.server.model.SpellAbility;
import mw.server.model.ability.Ability;
import mw.server.model.ability.AbilityCanPlay;
import mw.server.model.ability.AbilityTriggered;
import mw.server.model.cost.AdditionalCost;
import mw.server.model.cost.Cost;
import mw.server.model.cost.SacrificeCost;
import mw.server.model.cost.TapCost;
import mw.server.model.effect.GlobalEffect;
import mw.server.model.effect.ManaCostEffect;
import mw.server.model.groovy.GroovyAbility;
import mw.server.model.groovy.GroovyManaAbility;
import mw.server.model.groovy.GroovySpell;
import mw.server.model.groovy.GroovyTriggeredAbility;
import mw.server.model.spell.Flashback;
import mw.server.model.spell.Permanent;
import mw.server.model.zone.PlayerZone;
import mw.server.pattern.Command;
import mw.utils.CacheObjectUtil;
import org.apache.log4j.Logger;

public class GroovyCardFactory implements Serializable {

    private static final long serialVersionUID = 5634924936149541442L;

    private static final String SCRIPT_EXTENSION = ".g";

    private static final String COMMON_SCRIPT = "api.mw";

    private String basepath;

    private SpellAbility lastAdded = null;

    private CardListFilter lastAddedFilter = null;

    private SpellAbilityFilter lastAddedSAFilter = null;

    private String commonScript = "";

    private Map<String, ScriptInfo> cardsScripts = new HashMap<String, ScriptInfo>();

    /**
	 * Date of last api.mw modification. Used to recompile all cards once api is changed.
	 */
    private long lastModifiedAPI = 0;

    private GameManager game;

    private static final Logger log = Logger.getLogger(GroovyCardFactory.class);

    public GroovyCardFactory(String basepath, Map<String, ScriptInfo> scripts, GameManager game) throws IOException, ResourceException, ScriptException {
        this.basepath = basepath;
        this.game = game;
        cardsScripts = scripts;
        File apiFile = new File(basepath + File.separator + COMMON_SCRIPT);
        if (apiFile.isFile()) {
            lastModifiedAPI = apiFile.lastModified();
            commonScript = readFileAsString(apiFile);
        }
        File scriptedDir = new File(Constant.COMPILED_SCRIPTED_CARDS_DIR);
        if (!scriptedDir.exists()) scriptedDir.mkdirs();
        compileApi();
    }

    public boolean isHasScript(Card card) {
        File scriptFile = new File(basepath + File.separator + card.getName() + SCRIPT_EXTENSION);
        return cardsScripts.containsKey(card.getName()) || scriptFile.isFile();
    }

    public String getScriptForCard(String name) {
        if (cardsScripts.containsKey(name)) {
            return cardsScripts.get(name).script;
        }
        return "";
    }

    private static String readFileAsString(File file) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1024);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }

    private String updateBindings(String scriptText) {
        scriptText = scriptText.replace("KEYWORD_", "MagicWarsModelRef.KEYWORD_");
        scriptText = scriptText.replace("ASPECT_", "MagicWarsModelRef.ASPECT_");
        scriptText = scriptText.replace("PERMANENT_ASPECT", "MagicWarsModelRef.PERMANENT_ASPECT");
        scriptText = scriptText.replace("CounterType", "CounterTypeRef");
        scriptText = scriptText.replace("Duration", "DurationRef");
        scriptText = scriptText.replaceAll("@upkeepYourTurn", "upkeepYourTurn");
        scriptText = scriptText.replaceAll("@upkeepEachTurn", "upkeepEachTurn");
        scriptText = scriptText.replaceAll("@endStep", "endStep");
        scriptText = scriptText.replaceAll("@effect", "effect");
        scriptText = scriptText.replaceAll("@entersTheBattlefield", "entersTheBattlefield");
        scriptText = scriptText.replaceAll("@leavesTheBattlefield", "leavesTheBattlefield");
        scriptText = scriptText.replaceAll("@whileInPlay", "whileInPlay");
        scriptText = scriptText.replaceAll("\\$this", "bindings.getThis()");
        scriptText = scriptText.replaceAll("\\$", "bindings.");
        scriptText = scriptText.replaceAll("(?i)file[ ]*\\(", "error(");
        scriptText = scriptText.replaceAll("(?i)url[ ]*\\(", "error(");
        boolean logCard = scriptText.contains("#debug");
        scriptText = scriptText.replace("#debug", "");
        if (logCard) {
            log.debug("updated script: " + scriptText);
        }
        scriptText = scriptText.replace("###", "").replace("implemented-in-java", "");
        return scriptText;
    }

    public SpellAbility addSpell(Card card, Closure closure) {
        lastAdded = new GroovySpell(card, closure);
        return lastAdded;
    }

    public SpellAbility addAbility(Card card, Closure closure, Object cost, String question) {
        String _cost = "0";
        if (cost instanceof String) {
            _cost = (String) cost;
        }
        lastAdded = new GroovyAbility(card, _cost, closure);
        card.addSpellAbility(lastAdded);
        if (cost instanceof Cost) {
            lastAdded.addCost((Cost) cost);
        }
        if (question != null && !question.isEmpty()) {
            lastAdded.setYesNoQuestion(card + ": " + question);
        }
        return lastAdded;
    }

    public SpellAbility addAbilityTap(Card card, Closure closure, String cost) {
        lastAdded = new GroovyAbility(card, cost, closure);
        lastAdded.addCost(new TapCost());
        card.addSpellAbility(lastAdded);
        return lastAdded;
    }

    public SpellAbility addManaAbilityTap(Card card, Closure closure, String cost, String may) {
        lastAdded = new GroovyManaAbility(card, cost, closure);
        lastAdded.addCost(new TapCost());
        card.addSpellAbility(lastAdded);
        return lastAdded;
    }

    public SpellAbility addManaAbility(Card card, Closure closure, String cost, String may) {
        lastAdded = new GroovyManaAbility(card, cost, closure);
        card.addSpellAbility(lastAdded);
        return lastAdded;
    }

    public void setSpecificTarget(String description, String targetExpression) {
        setSpecificTarget(lastAdded, description, targetExpression);
    }

    public void setSpecificTarget(SpellAbility sa, String description, String targetExpression) {
        sa.setNeedsSpecificTarget(true);
        sa.setChooseTargetDescription(description);
        TargetFactory.build(sa, targetExpression, this.game);
    }

    public void setSpecificTarget(GroovyTriggeredAbility sa, String description, String targetExpression) {
        setSpecificTarget((SpellAbility) sa, description, targetExpression);
    }

    public void setSpecificTarget(String description, final Closure closure, GameZone zone) {
        setSpecificTarget(lastAdded, description, closure, zone);
    }

    public void setSpecificTarget(GroovyTriggeredAbility sa, String description, final Closure closure, GameZone zone) {
        setSpecificTarget((SpellAbility) sa, description, closure, zone);
    }

    public void setSpecificTarget(final SpellAbility sa, String description, final Closure closure, GameZone zone) {
        sa.setChooseTargetDescription(description);
        CardListFilter filter = new CardListFilter() {

            public boolean addCard(Card c) {
                Card card = sa.getSourceCard();
                setProperty(card, "$permanent", c);
                setProperty(card, "$card", c);
                try {
                    setVariables(game, sa.getSourceCard(), c, 0, null);
                    return (Boolean) closure.call();
                } catch (Exception e) {
                    log.error(e, e);
                }
                return false;
            }
        };
        sa.setNeedsSpecificTarget(description, filter, zone);
    }

    public void setTarget(String pattern) {
        setTarget(lastAdded, pattern);
    }

    public void setTarget(SpellAbility sa, String pattern) {
        if (pattern == null) {
            return;
        }
        pattern = pattern.trim().replace(" ", "").toLowerCase();
        if (pattern.equals("creature")) {
            sa.setNeedsTargetCreature(true);
        }
        if (pattern.equals("creature|player") || pattern.equals("player|creature")) {
            sa.setNeedsTargetCreatureOrPlayer(true);
        }
        if (pattern.equals("player")) {
            sa.setNeedsTargetPlayer(true);
        }
        if (pattern.equals("opponent")) {
            sa.setNeedsTargetOpponent(true);
        }
    }

    public void setTarget(GroovyTriggeredAbility sa, String pattern) {
        setTarget((SpellAbility) sa, pattern);
    }

    public void setCondition(final Card card, final Closure closure) {
        lastAddedFilter = new CardListFilter() {

            public boolean addCard(Card c) {
                setProperty(card, "$permanent", c);
                setProperty(card, "$card", c);
                setProperty(card, "$this", card);
                try {
                    return (Boolean) closure.call();
                } catch (Exception e) {
                    log.error(e, e);
                }
                return false;
            }
        };
    }

    public SpellAbilityFilter createFilter(final Card card, final Closure closure) {
        return new SpellAbilityFilter() {

            public boolean addSpellAbility(SpellAbility sa) {
                setProperty(card, "$sa", sa);
                setProperty(card, "$this", card);
                try {
                    return (Boolean) closure.call();
                } catch (Exception e) {
                    log.error(e, e);
                }
                return false;
            }
        };
    }

    public void setFilter(final Card card, final Closure closure) {
        lastAddedSAFilter = createFilter(card, closure);
    }

    public void setDescription(String description) {
        if (lastAdded != null) {
            lastAdded.setDescription(description);
        } else {
            log.error("no spell to set description to");
        }
    }

    public void setStackDescription(String description) {
        if (lastAdded != null) {
            lastAdded.setStackDescription(description);
        } else {
            log.error("no spell to set stack description to");
        }
    }

    public void addKicker(Card card, String cost, int index, String msg) {
        if (card.isPermanent() && !card.isAura()) {
            Permanent permanent = (Permanent) card.getSpellAbilities().get(0);
            if (index == 0) {
                SpellKickerFactory.addKicker(card, permanent, cost);
            } else {
                SpellKickerFactory.addKicker(card, permanent, cost, index, msg);
            }
        } else {
            SpellKickerFactory.addKicker(card, lastAdded, cost);
        }
    }

    public void addMultikicker(Card card, String cost) {
        if (card.isPermanent() && !card.isAura()) {
            Permanent permanent = (Permanent) card.getSpellAbilities().get(0);
            permanent.setMultikickerCost(cost);
        } else {
            lastAdded.setMultikickerCost(cost);
        }
    }

    public void addFlashback(String cost) {
        Flashback flashback = new Flashback(lastAdded, cost);
        lastAdded.getSourceCard().addSpellAbility(flashback);
        lastAdded = flashback;
    }

    public void setUpkeepYourTurn(final Card card, final Closure effect, final Closure condition, final String stackDescription, final Boolean noresponse, final String may) {
        final Command upkeep = new Command() {

            public void execute() {
                boolean res = true;
                if (condition != null) {
                    try {
                        setProperty(card, "$this", card);
                        setProperty(card, "$activePlayerId", game.getActivePlayerId());
                        res = (Boolean) condition.call();
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                }
                if (res) {
                    setProperty(card, "$this", card);
                    SpellAbility sa = new GroovyAbility(card, "0", effect);
                    if (stackDescription != null) {
                        sa.setStackDescription(stackDescription);
                    }
                    if (noresponse != null) {
                        sa.setInvisible(noresponse);
                    }
                    if (may != null) {
                        sa.setYesNoQuestion(may);
                    }
                    game.getStack().add(sa);
                }
            }

            private static final long serialVersionUID = 1L;
        };
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getUpkeep().addUpkeepPlayersTurn(card.getControllerID(), upkeep);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getUpkeep().removeUpkeepPlayersTurn(card.getControllerID(), upkeep);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
        final Command changeControllerCommand = new Command() {

            public void execute() {
                if (getParam() != null && getParam() instanceof Integer) {
                    if (game.getUpkeep().removeUpkeepOnce(card.getControllerID(), upkeep)) {
                        game.getUpkeep().addUpkeepOnce((Integer) getParam(), upkeep);
                    }
                }
            }

            private static final long serialVersionUID = 1L;
        };
        card.setChangeControllerCommand(changeControllerCommand);
    }

    public void setUpkeepEachTurn(final Card card, final Closure effect, final Closure condition, final String stackDescription, final Boolean noresponse, final String may) {
        final Command upkeep = new Command() {

            public void execute() {
                Object param = getParam();
                if (param != null) {
                    if (param instanceof Integer) {
                        boolean res = true;
                        if (condition != null) {
                            try {
                                setProperty(card, "$playerId", (Integer) param);
                                setProperty(card, "$this", card);
                                setProperty(card, "$activePlayerId", game.getActivePlayerId());
                                res = (Boolean) condition.call();
                            } catch (Exception e) {
                                log.error(e, e);
                            }
                        }
                        if (res) {
                            setProperty(card, "$this", card);
                            setProperty(card, "$playerId", ((Integer) param).intValue());
                            SpellAbility sa = new GroovyAbility(card, "0", effect);
                            if (stackDescription != null) {
                                sa.setStackDescription(stackDescription);
                            }
                            if (noresponse != null) {
                                sa.setInvisible(noresponse);
                            }
                            if (may != null) {
                                sa.setYesNoQuestion(may);
                            }
                            game.getStack().add(sa);
                        }
                    } else throw new RuntimeException("GroovyCardFactory.UpkeepEachTurn: param is not Integer:" + param.getClass().getSimpleName());
                } else throw new RuntimeException("GroovyCardFactory.UpkeepEachTurn: param is null.");
            }

            private static final long serialVersionUID = 1L;
        };
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getUpkeep().addUpkeepEachTurn(upkeep);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getUpkeep().removeUpkeepEachTurn(upkeep);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
    }

    public void setEndStep(final Card card, final Closure effect, final Closure condition, final String stackDescription, final Boolean noresponse, final String may) {
        final Command eot = new Command() {

            public void execute() {
                boolean res = true;
                if (condition != null) {
                    try {
                        setProperty(card, "$this", card);
                        setProperty(card, "$activePlayerId", game.getActivePlayerId());
                        res = (Boolean) condition.call();
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                }
                if (res) {
                    setProperty(card, "$this", card);
                    SpellAbility sa = new GroovyAbility(card, "0", effect);
                    if (stackDescription != null) {
                        sa.setStackDescription(stackDescription);
                    }
                    if (noresponse != null) {
                        sa.setInvisible(noresponse);
                    }
                    if (may != null) {
                        sa.setYesNoQuestion(may);
                    }
                    game.getStack().add(sa);
                }
            }

            private static final long serialVersionUID = 1L;
        };
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getEndOfTurn().addAtEachTurn(eot);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                game.getEndOfTurn().removeAtEachTurn(eot);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
    }

    public GlobalEffect createEffect(final Card card, final Closure closureApply, final Closure closureDiscard) {
        SpellAbility apply = new GroovyAbility(card, "0", closureApply);
        SpellAbility discard = new GroovyAbility(card, "0", closureDiscard);
        return SpellEffectFactory.getEffect(card, apply, discard);
    }

    public GlobalEffect addEffect(final Card card, final Closure closureApply, final Closure closureDiscard) {
        final GlobalEffect effect = createEffect(card, closureApply, closureDiscard);
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                game.addGlobalEffect(effect);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                game.removeGlobalEffect(effect);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
        return effect;
    }

    public void addManaCostEffect(final Card card, final ManaCostEffect manaCostEffect) {
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                game.addManaCostEffect(manaCostEffect);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                game.removeManaCostEffect(manaCostEffect);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
    }

    public GroovyTriggeredAbility createTriggeredAbility(final Card card, String cost, Closure closure) {
        lastAddedFilter = null;
        return new GroovyTriggeredAbility(card, cost, closure);
    }

    public AbilityTriggered createAction(final Card card, Closure closure) {
        AbilityTriggered action = new GroovyTriggeredAbility(card, "0", closure);
        action.setInvisible(true);
        return action;
    }

    public void setYesNoQuestion(String question) {
        if (lastAdded != null) {
            lastAdded.setYesNoQuestion(question);
        }
    }

    public void addCost(Cost cost) {
        if (lastAdded != null) {
            lastAdded.addCost(cost);
            if (cost instanceof SacrificeCost && !lastAdded.isAbilityOptional()) {
                lastAdded.setYesNoQuestion(lastAdded.getSourceCard() + ": sacrifice?");
            }
        }
    }

    public Cost getSacrificeCost() {
        return new SacrificeCost();
    }

    public Cost getAdditionalCost(Card card, Closure closureEffect, Closure closureChoice, String ask) {
        ChoiceCommand choice = createChoiceCommand(card, closureChoice);
        Command effect = createCommand(card, closureEffect);
        if (closureChoice == null || ask == null) {
            return new AdditionalCost(effect, null, null);
        }
        return new AdditionalCost(effect, choice, ask);
    }

    /**
	 * Register observer
	 * 
	 * @param sa
	 *            action to be taken
	 * @param pattern
	 *            event pattern
	 * @param zone
	 *            Zone where observer will be turned on. Can be GameZone.Any.
	 */
    public void setTrigger(GroovyTriggeredAbility sa, String pattern, final GameZone zone) {
        Pattern triggerPattern = Pattern.compile("@(.*),from=(.*),to=(.*)");
        Pattern shortTriggerPattern = Pattern.compile("@(.*)");
        Matcher m = triggerPattern.matcher(pattern);
        if (!m.matches()) {
            m = shortTriggerPattern.matcher(pattern);
            if (!m.matches()) {
                try {
                    throw new IllegalArgumentException("Wrong trigger pattern: " + pattern);
                } catch (IllegalArgumentException e) {
                    log.error(e, e);
                }
                return;
            }
        }
        String event = m.group(1);
        final CardListFilter condition = lastAddedFilter;
        final SpellAbilityFilter saFilter = lastAddedSAFilter;
        final GroovyTriggeredAbility abilityTriggered = (GroovyTriggeredAbility) sa;
        if (event.equals("moveToZone")) {
            final String fromZone = m.group(2);
            final String toZone = m.group(3);
            final Observer triggerHandler = new Observer() {

                public void update(Observable o, Object arg) {
                    EventParam eventParam = (EventParam) arg;
                    PlayerZone from = (PlayerZone) eventParam.getParams().get(0);
                    PlayerZone to = (PlayerZone) eventParam.getParams().get(1);
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        Card c = (Card) eventParam.getParams().get(2);
                        if (!c.equals(abilityTriggered.getSourceCard())) return;
                    }
                    if (fromZone.equals("*") || fromZone.equals("Any") || fromZone.equals(from.getGameZone() + "")) {
                        if (toZone.equals("*") || toZone.equals("Any") || toZone.equals(to.getGameZone() + "")) {
                            Card c = (Card) eventParam.getParams().get(2);
                            if (condition != null && abilityTriggered != null) {
                                if (condition.addCard(c)) {
                                    boolean notFiltered = true;
                                    SpellAbility sa = null;
                                    if (saFilter != null && eventParam.getParams().size() > 3) {
                                        sa = (SpellAbility) eventParam.getParams().get(3);
                                        notFiltered = saFilter.addSpellAbility(sa);
                                    }
                                    if (notFiltered) {
                                        GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                        if (triggered.getSourceCard().superType(MagicWarsModel.CardSuperType.Rule)) {
                                            triggered.setRuleRef(triggered.getSourceCard());
                                            triggered.setSourceCard(c);
                                        }
                                        triggered.attachBinding("$card", c);
                                        if (sa != null) triggered.attachBinding("$sa", sa);
                                        game.getStack().add(triggered);
                                    }
                                }
                            }
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@moveToZone", game.getEventManager().getMoveToZoneEvent(), triggerHandler, zone);
        } else if (event.equals("creatureAttacks")) {
            final Observer creatureAttacksObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    Card c = (Card) arg;
                    if (condition != null && abilityTriggered != null) {
                        if (condition.addCard(c)) {
                            GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                            triggered.attachBinding("$card", c);
                            game.getStack().add(triggered);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@creatureAttacks", game.getEventManager().getAttacksEvent(), creatureAttacksObserver, zone);
        } else if (event.equals("creatureAttacksAlone")) {
            final Observer creatureAttacksAloneObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    Card c = (Card) arg;
                    if (condition != null && abilityTriggered != null) {
                        if (condition.addCard(c)) {
                            GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                            triggered.attachBinding("$card", c);
                            game.getStack().add(triggered);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@creatureAttacksAlone", game.getEventManager().getAttacksAloneEvent(), creatureAttacksAloneObserver, zone);
        } else if (event.equals("creatureBlocks")) {
            final Observer creatureBlocksObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        EventParam event = (EventParam) arg;
                        Card card = (Card) event.getParam1();
                        CardList attackers = (CardList) event.getParam2();
                        if (condition != null && abilityTriggered != null) {
                            setProperty(card, "$attackers", attackers);
                            if (condition.addCard(card)) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.attachBinding("$card", card);
                                triggered.attachBinding("$attackers", attackers);
                                game.getStack().add(triggered);
                            }
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@creatureBlocks", game.getEventManager().getBlocksEvent(), creatureBlocksObserver, zone);
        } else if (event.equals("creatureWasBlocked")) {
            final Observer creatureWasBlockedObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        EventParam event = (EventParam) arg;
                        Card card = (Card) event.getParam1();
                        CardList blockers = (CardList) event.getParam2();
                        if (condition != null && abilityTriggered != null) {
                            setProperty(card, "$blockers", blockers);
                            if (condition.addCard(card)) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.attachBinding("$card", card);
                                triggered.attachBinding("$blockers", blockers);
                                game.getStack().add(triggered);
                            }
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@creatureWasBlocked", game.getEventManager().getWasBlockedEvent(), creatureWasBlockedObserver, zone);
        } else if (event.equals("discard")) {
            final Observer discardObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    Card c = (Card) arg;
                    if (condition != null && abilityTriggered != null) {
                        if (condition.addCard(c)) {
                            GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                            triggered.attachBinding("$card", c);
                            game.getStack().add(triggered);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@discard", game.getEventManager().getDiscardEvent(), discardObserver, zone);
        } else if (event.equals("tapped")) {
            final Observer tapObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    Card c = (Card) arg;
                    if (condition != null && abilityTriggered != null) {
                        if (condition.addCard(c)) {
                            GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                            triggered.attachBinding("$card", c);
                            game.getStack().add(triggered);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@tapped", game.getEventManager().getTapEvent(), tapObserver, zone);
        } else if (event.equals("cast")) {
            final Observer castObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    SpellAbility sa = (SpellAbility) arg;
                    boolean notFiltered = true;
                    if (saFilter != null) {
                        sa = (SpellAbility) arg;
                        notFiltered = saFilter.addSpellAbility(sa);
                    }
                    if (notFiltered) {
                        GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                        if (sa != null) triggered.attachBinding("$sa", sa);
                        game.getStack().add(triggered);
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@cast", game.getEventManager().getCastingSpellEvent(), castObserver, zone);
        } else if (event.equals("dealDamage")) {
            final Observer dealtDamageObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        try {
                            EventParam e = (EventParam) arg;
                            Card source = e.getSource();
                            Integer damage = (Integer) e.getParam1();
                            Integer playerId = (Integer) e.getParam2();
                            Card target = e.getTarget();
                            boolean filtered = false;
                            if (condition != null) {
                                Card card = abilityTriggered.getSourceCard();
                                setProperty(card, "$source", source);
                                setProperty(card, "$damagedPlayer", playerId);
                                setVariables(game, card, null, 0, null);
                                if (!condition.addCard(source)) {
                                    filtered = true;
                                }
                            }
                            if (!filtered && abilityTriggered != null) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.setTargetCard(target);
                                triggered.setTargetPlayerID(playerId);
                                triggered.attachBinding("$source", source);
                                triggered.attachBinding("$damage", damage);
                                triggered.attachBinding("$damagedPlayer", playerId);
                                if (triggered.isInvisible()) {
                                    triggered.resolve();
                                } else {
                                    game.getStack().add(triggered);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e, e);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@dealDamage", game.getEventManager().getDealDamageEvent(), dealtDamageObserver, zone);
        } else if (event.equals("dealCombatDamage")) {
            final Observer dealtDamageObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        try {
                            EventParam e = (EventParam) arg;
                            Card source = e.getSource();
                            Integer damage = (Integer) e.getParam1();
                            Integer playerId = (Integer) e.getParam2();
                            Card target = e.getTarget();
                            boolean filtered = false;
                            if (condition != null) {
                                Card card = abilityTriggered.getSourceCard();
                                setProperty(card, "$source", source);
                                setProperty(card, "$damagedPlayer", playerId);
                                setVariables(game, card, null, 0, null);
                                if (!condition.addCard(source)) {
                                    filtered = true;
                                }
                            }
                            if (!filtered && abilityTriggered != null) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.setTargetCard(target);
                                triggered.setTargetPlayerID(playerId);
                                triggered.attachBinding("$source", source);
                                triggered.attachBinding("$damage", damage);
                                triggered.attachBinding("$damagedPlayer", playerId);
                                if (triggered.isInvisible()) {
                                    triggered.resolve();
                                } else {
                                    game.getStack().add(triggered);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e, e);
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@dealCombatDamage", game.getEventManager().getDealCombatDamageEvent(), dealtDamageObserver, zone);
        } else if (event.equals("addMana")) {
            final Observer addManaObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        EventParam event = (EventParam) arg;
                        Card source = event.getSource();
                        Color color = (Color) event.getParam2();
                        if (condition != null && abilityTriggered != null) {
                            if (condition.addCard(source)) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.attachBinding("$mana", color);
                                triggered.attachBinding("$card", source);
                                game.getStack().add(triggered);
                            }
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@addMana", game.getEventManager().getAddManaEvent(), addManaObserver, zone);
        } else if (event.equals("targeted")) {
            final Observer targetedObserver = new Observer() {

                public void update(Observable o, Object arg) {
                    if (!abilityTriggered.getSourceCard().zone(zone)) {
                        return;
                    }
                    if (arg instanceof EventParam) {
                        EventParam event = (EventParam) arg;
                        Card target = (Card) event.getParam1();
                        SpellAbility sa = (SpellAbility) event.getParam2();
                        if (condition != null && abilityTriggered != null) {
                            setProperty(abilityTriggered.getSourceCard(), "$sa", sa);
                            setProperty(abilityTriggered.getSourceCard(), "$source", sa.getSourceCard());
                            if (condition.addCard(target)) {
                                GroovyTriggeredAbility triggered = abilityTriggered.getCopy();
                                triggered.attachBinding("$card", target);
                                triggered.attachBinding("$sa", sa);
                                triggered.attachBinding("$source", sa.getSourceCard());
                                game.getStack().add(triggered);
                            }
                        }
                    }
                }
            };
            addObserver(sa.getSourceCard(), "@targeted", game.getEventManager().getTargetedEvent(), targetedObserver, zone);
        } else {
            log.error("Not supported event: " + event);
        }
    }

    private void addObserver(final Card card, final String name, final Observable event, final Observer observer, final GameZone zone) {
        Command entersTheGameCommand = new Command() {

            public void execute() {
                log.info(name + ": registered " + card + "#" + card.getCardKey() + ", zone: " + zone);
                event.addObserver(observer);
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheGameCommand(entersTheGameCommand);
    }

    public void setLimit(Integer limit) {
        if (limit > 0 && lastAdded != null) {
            if (lastAdded instanceof Ability) {
                ((Ability) lastAdded).setLimit(limit);
            }
        }
    }

    public void setEntersTheBattlefield(final Card card, final Closure closure, final Boolean invisible) {
        Command entersTheBattlefieldCommand = new Command() {

            public void execute() {
                SpellAbility sa = new GroovyAbility(card, "0", closure);
                setVariables(game, sa.getSourceCard(), null, 0, null);
                sa.resolve();
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheBattlefieldCommand(entersTheBattlefieldCommand);
    }

    public void setEntersTheGame(final Card card, final Closure closure) {
        Command entersTheGame = new Command() {

            public void execute() {
                SpellAbility sa = new GroovyAbility(card, "0", closure);
                setVariables(game, sa.getSourceCard(), null, 0, null);
                sa.resolve();
            }

            private static final long serialVersionUID = 1L;
        };
        card.setEntersTheGameCommand(entersTheGame);
    }

    public void setLeavesTheBattlefield(final Card card, final Closure closure, final Boolean invisible) {
        Command leavesTheBattlefieldCommand = new Command() {

            public void execute() {
                SpellAbility sa = new GroovyAbility(card, "0", closure);
                if (invisible != null && invisible) {
                    sa.resolve();
                } else {
                    sa.setInvisible(true);
                    game.getStack().add(sa);
                }
            }

            private static final long serialVersionUID = 1L;
        };
        card.setLeavesTheBattlefieldCommand(leavesTheBattlefieldCommand);
    }

    public void addTrap(final Card card, SpellAbility sa, final Closure closure, String trapCost) {
        if (sa instanceof GroovySpell) {
            AbilityCanPlay canPlay = new AbilityCanPlay() {

                @Override
                public boolean canPlay() {
                    try {
                        setVariables(card.getGame(), card, null, 0, null);
                        return (Boolean) closure.call();
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                    return false;
                }
            };
            SpellTrapFactory.addTrap(card, sa, trapCost, canPlay);
        }
    }

    public void setCanPlay(final Card card, final Closure closure) {
        if (lastAdded != null) {
            AbilityCanPlay abilityCanPlay = new AbilityCanPlay() {

                public boolean canPlay() {
                    try {
                        setVariables(card.getGame(), card, null, 0, null);
                        return (Boolean) closure.call();
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                    return false;
                }
            };
            if (lastAdded instanceof GroovySpell) {
                ((GroovySpell) lastAdded).setAbilityCanPlay(abilityCanPlay);
            } else if (lastAdded instanceof GroovyAbility) {
                ((GroovyAbility) lastAdded).setAbilityCanPlay(abilityCanPlay);
            }
        } else {
            log.error("no spell to set canPlay to");
        }
    }

    public AbilityCanPlay buildCanPlay(final Card card, final Closure closure) {
        AbilityCanPlay abilityCanPlay = new AbilityCanPlay() {

            public boolean canPlay() {
                try {
                    setVariables(card.getGame(), card, null, 0, null);
                    return (Boolean) closure.call();
                } catch (Exception e) {
                    log.error(e, e);
                }
                return false;
            }
        };
        return abilityCanPlay;
    }

    public void setAbilityToChoose(final Card card, final Closure closure) {
        if (lastAdded != null) {
            ChoiceCommand command = createChoiceCommand(card, closure);
            lastAdded.setNeedsToChooseAbility(true);
            lastAdded.setChoiceCommand(command);
        } else {
            log.error("no spell to set AbilityToChoose to");
        }
    }

    @SuppressWarnings("serial")
    public SpellAbility getCurrentSpell(Card card) {
        if (lastAdded == null) {
            log.error("No spell to return. Will create dummy instance.");
            return new GroovySpell(card, new Closure(this) {
            });
        }
        return lastAdded;
    }

    @SuppressWarnings("serial")
    public Command createCommand(final Card card, final Closure closure) {
        Command command = new Command() {

            public void execute() {
                try {
                    setVariables(card.getGame(), card, null, 0, null);
                    setProperty(card, "$param", getParam());
                    setProperty(card, "$target", getTarget());
                    setProperty(card, "$chosen", getTarget());
                    closure.call();
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        };
        return command;
    }

    @SuppressWarnings("serial")
    public ChoiceCommand createChoiceCommand(final Card card, final Closure closure) {
        ChoiceCommand choiceCommand = new ChoiceCommand() {

            public void execute() {
                try {
                    setVariables(card.getGame(), card, null, 0, null);
                    Object result = closure.call();
                    AbilityList abilityList = new AbilityList();
                    CardList cardList = new CardList();
                    if (result != null && result instanceof List<?>) {
                        for (Object o : (List<?>) result) {
                            if (o instanceof Card) {
                                cardList.add((Card) o);
                            } else if (o instanceof SpellAbility) {
                                abilityList.add((SpellAbility) o);
                            } else {
                                throw new Exception("createChoice: object is neither Card nor SpellAbility");
                            }
                        }
                    }
                    setInputChoice(abilityList);
                    setInputChoice(cardList);
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        };
        return choiceCommand;
    }

    public Object createInstance(String name, Object[] arglist) {
        return null;
    }

    @SuppressWarnings({ "rawtypes" })
    public Card compileAndLoadScript(Card card) throws Exception {
        long milliSec = new Date().getTime();
        long t1 = 0, t2 = 0, t3 = 0;
        try {
            prepareGroovyFile(card);
            CardCompiler cardCompiler = new CardCompiler();
            URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            String name = card.getName().replace(" ", "").replace(",", "").replace("'", "").replace("-", "");
            File f = new File(mw.server.constant.Constant.COMPILED_SCRIPTED_CARDS_DIR + File.separator + name + ".groovy");
            String className = cardCompiler.compileScript(f);
            Class c = systemClassLoader.loadClass(className);
            t1 = (new Date().getTime() - milliSec);
            milliSec = new Date().getTime();
            Object _card = c.newInstance();
            t2 = (new Date().getTime() - milliSec);
            if (_card instanceof GroovyCard && _card instanceof GroovyScriptFacade) {
                GroovyCard groovyCard = (GroovyCard) _card;
                GroovyScriptFacade facade = (GroovyScriptFacade) _card;
                facade.builder = this;
                facade.game = game;
                facade.getBindings().setProperty("$this", card);
                groovyCard.setCard(card);
                groovyCard.invoke();
                Object resultCard = groovyCard.getCard();
                t3 = (new Date().getTime() - milliSec);
                return (Card) resultCard;
            } else {
                log.error("GroovyCard has wrong class type:" + _card.getClass().getCanonicalName());
            }
        } finally {
            log.info(card.getName() + ", class loading: " + String.valueOf(t1) + "ms, instance creating: " + String.valueOf(t2) + "ms, " + "card building: " + String.valueOf(t3) + "ms.");
        }
        log.error(card.getName() + " has been loaded with errors: " + (new Date().getTime() - milliSec) + "ms.");
        return card;
    }

    public void compileApi() {
        try {
            long milliSec = new Date().getTime();
            prepareGroovyApi();
            CardCompiler cardCompiler = new CardCompiler();
            File f = new File(mw.server.constant.Constant.COMPILED_SCRIPTED_CARDS_DIR + File.separator + "GroovyApi.groovy");
            cardCompiler.compileScript(f);
            log.info("Groovy card api has been compiled in " + (new Date().getTime() - milliSec) + "ms.");
        } catch (Exception e) {
            log.fatal(e, e);
            throw new RuntimeException("Critical error: couldn't compile groovy card api!");
        }
    }

    /**
	 * Create .groovy card for the card using groovy template. Created file will be used later by compiler to build java classes.
	 * 
	 * Create file ONLY if script was updated. Whenever new .groovy file is created, groovy classes would be recompiled.
	 */
    private void prepareGroovyFile(Card card) {
        long milliSec = new Date().getTime();
        InputStream is = this.getClass().getResourceAsStream(Constant.GROOVY_SCRIPTED_CARD_TEMPLATE);
        if (is == null) {
            log.error("Couldn't find template for groovy scripted cards: " + Constant.GROOVY_SCRIPTED_CARD_TEMPLATE);
            return;
        }
        try {
            String groovyName = card.getName().replace(" ", "").replace(",", "").replace("'", "").replace("-", "");
            File groovyCard = new File(Constant.COMPILED_SCRIPTED_CARDS_DIR + File.separator + groovyName + ".groovy");
            if (groovyCard.exists()) {
                if (cardsScripts.containsKey(card.getName())) {
                    long lastModified = cardsScripts.get(card.getName()).lastModified;
                    if (groovyCard.lastModified() >= lastModified) {
                        if (groovyCard.lastModified() > lastModifiedAPI) {
                            return;
                        }
                    }
                }
                groovyCard.delete();
            }
            groovyCard.createNewFile();
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                int index = sb.indexOf(Constant.GROOVY_TEMPLATE_PARAM_CLASSNAME);
                if (index == -1) {
                    log.error("Couldn't find specific param in groovy template: classname.");
                    return;
                }
                sb.replace(index, index + Constant.GROOVY_TEMPLATE_PARAM_CLASSNAME.length(), groovyName);
                index = sb.indexOf(Constant.GROOVY_TEMPLATE_PARAM_SCRIPT);
                if (index == -1) {
                    log.error("Couldn't find specific param in groovy template: card script.");
                    return;
                }
                String scriptCode = "";
                if (cardsScripts.containsKey(card.getName())) {
                    scriptCode = cardsScripts.get(card.getName()).script;
                }
                scriptCode = updateBindings(scriptCode);
                sb.replace(index, index + Constant.GROOVY_TEMPLATE_PARAM_SCRIPT.length(), scriptCode);
                CacheObjectUtil.saveText(sb.toString(), groovyCard);
            } finally {
                is.close();
            }
            log.info(groovyName + ".groovy, script was created in " + (new Date().getTime() - milliSec) + "ms.");
        } catch (IOException io) {
            log.error(io, io);
        }
    }

    private void prepareGroovyApi() {
        InputStream is = this.getClass().getResourceAsStream(Constant.GROOVY_SCRIPTED_API);
        if (is == null) {
            log.error("Couldn't find template for groovy scripted cards: " + Constant.GROOVY_SCRIPTED_API);
            return;
        }
        try {
            File groovyCard = new File(Constant.COMPILED_SCRIPTED_CARDS_DIR + File.separator + "GroovyApi.groovy");
            if (groovyCard.exists()) {
                groovyCard.delete();
            }
            groovyCard.createNewFile();
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                int index = sb.indexOf(Constant.GROOVY_TEMPLATE_PARAM_API);
                if (index == -1) {
                    log.error("Couldn't find specific param in groovy.api: api label.");
                    return;
                }
                commonScript = commonScript.replaceAll("\\$this", "bindings.getThis()");
                commonScript = commonScript.replaceAll("\\$", "bindings.");
                sb.replace(index, index + Constant.GROOVY_TEMPLATE_PARAM_API.length(), commonScript);
                CacheObjectUtil.saveText(sb.toString(), groovyCard);
            } finally {
                is.close();
            }
        } catch (IOException io) {
            log.error(io, io);
        }
    }

    public void logError(Card source, String error) {
        log.error(source + ": " + error);
    }

    private void setProperty(Card card, String property, Object value) {
        if (card.getGroovyRef() == null) {
            log.warn("GroovyRef is null for the card: " + card.getName() + "[" + card.getUniqueNumber() + "]");
            return;
        }
        GroovyScriptBindings bindings = card.getGroovyRef().getBindings();
        bindings.setProperty(property, value);
    }

    public static void setVariables(GameManager game, Card card, Card targetCard, int playerID, SpellAbility sa) {
        if (card.getGroovyRef() == null) {
            log.warn("GroovyRef is null for the card: " + card.getName() + "[" + card.getUniqueNumber() + "]");
            return;
        }
        GroovyScriptBindings bindings = card.getGroovyRef().getBindings();
        if (targetCard != null) {
            bindings.setProperty("$target", targetCard);
        } else if (playerID > 0) {
            bindings.setProperty("$target", playerID);
        }
        bindings.setProperty("$chosen", targetCard);
        bindings.setProperty("$targetPlayer", playerID);
        if (card.isAura() && sa != null && sa.wasKickerPayed()) {
            if (!card.hasAspect(MagicWarsModel.ASPECT_CARD_WAS_KICKED)) {
                card.addAspect(MagicWarsModel.ASPECT_CARD_WAS_KICKED);
            }
        }
        if (card.isPermanent()) {
            bindings.setProperty("$wasKicked", card.hasAspect(MagicWarsModel.ASPECT_CARD_WAS_KICKED));
            bindings.setProperty("$firstKicker", card.hasAspect(MagicWarsModel.ASPECT_CARD_FIRST_KICKER));
            bindings.setProperty("$secondKicker", card.hasAspect(MagicWarsModel.ASPECT_CARD_SECOND_KICKER));
        } else if (sa != null) {
            bindings.setProperty("$wasKicked", sa.wasKickerPayed());
        }
        if (card.hasAspect(MagicWarsModel.ASPECT_MULTIKICKER)) {
            bindings.setProperty("$wasKicked", true);
            bindings.setProperty("$multikicker", card.getAspectValue(MagicWarsModel.ASPECT_MULTIKICKER));
        } else {
            bindings.setProperty("$multikicker", 0);
        }
        if (card.isPermanent()) {
            SpellAbility permanent = card.getSpellAbilities().get(0);
            if (permanent.isNeedsToChooseX()) {
                bindings.setProperty("$xValue", permanent.getXValue());
            }
        }
        if (sa != null) {
            bindings.setProperty("$targets", sa.getTargetCards());
            bindings.setProperty("$chosenCards", sa.getTargetCards());
            bindings.setProperty("$targetAbility", sa.getTargetAbility());
            bindings.setProperty("$targetSpell", sa.getTargetAbility());
            bindings.setProperty("$targetAbilities", sa.getTargetAbilities());
            bindings.setProperty("$targetSpells", sa.getTargetAbilities());
            if (sa.isNeedsToChooseX()) {
                bindings.setProperty("$xValue", sa.getXValue());
            }
            if (sa.isNeedsToChooseX2()) {
                bindings.setProperty("$xValue", sa.getXValue2());
            }
        }
        if (card.isEquipment() && card.isEquipping()) {
            bindings.setProperty("$equipped", card.getEquipping().get(0));
        }
        bindings.setProperty("$permanent", targetCard);
        bindings.setProperty("$allCreatures", game.getBattlefield().getAllCreatures().getLink());
        bindings.setProperty("$myCreatures", game.getBattlefield().getCreatures(card.getControllerID()).getLink());
        bindings.setProperty("$allPermanents", game.getBattlefield().getAllPermanents().getLink());
        bindings.setProperty("$myPermanents", game.getBattlefield().getPermanentList(card.getControllerID()).getLink());
        bindings.setProperty("$this", card);
        MWPlayer opponent = game.getOpponentById(card.getControllerID());
        MWPlayer player = game.getPlayerById(card.getControllerID());
        bindings.setProperty("$oppLife", opponent.getLifeCount());
        bindings.setProperty("$myLife", player.getLifeCount());
        bindings.setProperty("$player", player);
        bindings.setProperty("$controller", player);
        bindings.setProperty("$opponent", opponent);
        bindings.setProperty("$playerId", player.getPlayerId());
        bindings.setProperty("$opponentId", opponent.getPlayerId());
        bindings.setProperty("$activePlayerId", game.getActivePlayerId());
        List<MWPlayer> players = new ArrayList<MWPlayer>();
        players.add(player);
        players.add(opponent);
        bindings.setProperty("$allPlayers", players);
        bindings.setProperty("$oppPermanents", game.getBattlefield().getPermanentList(opponent.getPlayerId()));
        bindings.setProperty("$oppCreatures", game.getBattlefield().getCreatures(opponent.getPlayerId()));
    }
}
