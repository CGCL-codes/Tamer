package mw.server.model;

import groovy.lang.Closure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.SimpleBindings;
import mw.server.ChoiceCommand;
import mw.server.GameManager;
import mw.server.MWPlayer;
import mw.server.card.GroovyScriptBindings;
import mw.server.card.ability.AbilityList;
import mw.server.card.ability.factory.TargetFactory;
import mw.server.list.CardList;
import mw.server.list.CardListFilter;
import mw.server.model.MagicWarsModel.GameZone;
import mw.server.model.cost.Cost;
import mw.server.model.cost.CostList;
import mw.server.model.cost.LoyaltyCost;
import mw.server.model.cost.ManaCost;
import mw.server.pattern.Command;
import mw.server.pattern.SpellChainFactory;
import org.apache.log4j.Logger;

public abstract class SpellAbility implements Serializable, Cloneable {

    private static final long serialVersionUID = -8595085918351107533L;

    protected Logger log = Logger.getLogger(SpellAbility.class);

    private static int nextUniqueNumber;

    private int uniqueNumber = nextUniqueNumber++;

    public static final int Spell = 0;

    public static final int Ability = 1;

    public static final int TapAbility = 2;

    public static final int ManaAbility = 3;

    private String description = "";

    private String targetPlayer = "";

    private String stackDescription = "";

    private String manaCost = "";

    private String manaToPlay = "";

    private CostList cost = new CostList();

    private String multikickerCost;

    private int targetPlayerID = -1;

    private CardList targetCards;

    private AbilityList targetAbilities;

    private Card sourceCard;

    private Card rememberedCard;

    private boolean spell;

    private boolean tapAbility;

    private ChoiceCommand choiceCommand;

    private ChoiceCommand payChoiceCommand;

    private Command cancelCommand = null;

    private AbilityList abililtyChoice;

    private String choiceDetailedDescription = "";

    private String choiceDescription = "";

    private String yesNoQuestion = "";

    private Command spellChain;

    private Map<String, List<Serializable>> aspects = null;

    /**
     * Targets
     */
    private boolean isNeedsTargetCreatureOrPlayer = false;

    private boolean isNeedsTargetPlayer = false;

    private boolean isNeedsTargetOpponent = false;

    private boolean isNeedsTargetCreature = false;

    private boolean isNeedsTargetPermanent = false;

    private boolean isNeedsTargetSpecific = false;

    private boolean areTargetsOptional = true;

    private boolean areTargetsUnique = false;

    private boolean isExactCount = true;

    private String chooseTargetDescription = "";

    private CardList choice;

    private int targetCount = 1;

    /**
	 * Choosing
	 */
    private boolean isNeedsToChooseCreature = false;

    private boolean isNeedsToChooseSpecificPermanent = false;

    private boolean areChoicesOptional = true;

    private boolean isNeedsToChooseAbility = false;

    private boolean isNeedsToChooseAbilityToPlay = false;

    private boolean isNeedsToChooseCard = false;

    private boolean isNeedsToChooseX = false;

    private boolean isNeedsToChooseX2 = false;

    private boolean isNeedsToChooseManaSymbol = false;

    private int choiceCount = 1;

    /**
     * Other
     */
    private boolean isNeedsDiscardCard = false;

    private boolean isNeedsDiscardCardToPay = false;

    private boolean isDiscardEqualToRemove = false;

    private boolean isNeedsAdditionalCostToPlay = false;

    private boolean isCancelStopsPlaying = true;

    private boolean isAbilityOptional = false;

    private int playerIdToAsk = 0;

    private int targetsChosen = 0;

    private int xValue;

    private int xValue2;

    private boolean kickerWasPayed = false;

    private Command command;

    /**
     * For choose two (Lorwyn Commands)
     */
    private int firstChosenAbility = 0;

    private int secondChosenAbility = 0;

    /**
     * That means that opponent won't receive the opportunity to respond to this ability.
     * Example: devour a creature. 
     */
    private boolean isInvisible;

    public boolean isNeedsTargetCreaturePlayer() {
        return isNeedsTargetCreatureOrPlayer;
    }

    public void setNeedsTargetCreatureOrPlayer(boolean isNeedsTargetCreaturePlayer) {
        this.isNeedsTargetCreatureOrPlayer = isNeedsTargetCreaturePlayer;
    }

    public void setNeedsTargetPlayer(boolean isNeedsTargetPlayer) {
        this.isNeedsTargetPlayer = isNeedsTargetPlayer;
    }

    public boolean isNeedsTargetPlayer() {
        return isNeedsTargetPlayer;
    }

    public boolean isNeedsTargetOpponent() {
        return isNeedsTargetOpponent;
    }

    /**
     * Is not used, reserved for future versions.
     * At the moment get opponent id on spell resolve.
     * @param isNeedsTargetOpponent
     */
    public void setNeedsTargetOpponent(boolean isNeedsTargetOpponent) {
        this.isNeedsTargetOpponent = isNeedsTargetOpponent;
    }

    public boolean isNeedsTargetCreature() {
        return isNeedsTargetCreature;
    }

    public void setNeedsTargetCreature(boolean isNeedsTargetCreature) {
        this.isNeedsTargetCreature = isNeedsTargetCreature;
    }

    public boolean isNeedsTargetPermanent() {
        return isNeedsTargetPermanent;
    }

    public void setNeedsTargetPermanent(boolean isNeedsTargetPermanent) {
        this.isNeedsTargetPermanent = isNeedsTargetPermanent;
    }

    public boolean areTargetsUnique() {
        return areTargetsUnique;
    }

    public void setTargetsAreUnique(boolean areTargetsUnique) {
        this.areTargetsUnique = areTargetsUnique;
    }

    public boolean isNeedsDiscardCard() {
        return isNeedsDiscardCard;
    }

    public void setNeedsDiscardCard(boolean isNeedsDiscardCard) {
        this.isNeedsDiscardCard = isNeedsDiscardCard;
    }

    public boolean isNeedsDiscardCardToPay() {
        return isNeedsDiscardCardToPay;
    }

    public void setNeedsDiscardCardToPay(boolean isNeedsDiscardCardToPay) {
        this.isNeedsDiscardCardToPay = isNeedsDiscardCardToPay;
    }

    public boolean isNeedsAdditionalCostToPlay() {
        return isNeedsAdditionalCostToPlay;
    }

    public void setNeedsAdditionalCostToPlay(boolean isNeedsAdditionalCostToPlay) {
        this.isNeedsAdditionalCostToPlay = isNeedsAdditionalCostToPlay;
    }

    public boolean isNeedsChooseSpecific() {
        return isNeedsTargetSpecific;
    }

    public boolean isNeedsSpecificTarget() {
        return isNeedsTargetSpecific;
    }

    public void setNeedsTargetSpecific(boolean isNeedsTargetSpecific) {
        this.isNeedsTargetSpecific = isNeedsTargetSpecific;
    }

    public void setNeedsSpecificTarget(boolean isNeedsSpecificTarget) {
        this.isNeedsTargetSpecific = isNeedsSpecificTarget;
    }

    public void setNeedsSpecificTarget(String description, CardListFilter filter, GameZone zone) {
        if (zone.equals(GameZone.Battlefield)) {
            this.isNeedsTargetSpecific = true;
        } else {
            this.isNeedsToChooseCard = true;
            addAspect(MagicWarsModel.ASPECT_NEW_CORE_TARGET);
        }
        this.chooseTargetDescription = description;
        TargetFactory.build(this, filter, zone);
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public SpellAbility(int spellOrAbility, Card sourceCard) {
        if (spellOrAbility == Spell) spell = true; else if (spellOrAbility == Ability) spell = false; else if (spellOrAbility == TapAbility) {
            spell = false;
            tapAbility = true;
        } else if (spellOrAbility == ManaAbility) {
            spell = false;
        } else {
            throw new RuntimeException("SpellAbility : constructor error, invalid spellOrAbility argument = " + spellOrAbility);
        }
        this.sourceCard = sourceCard;
    }

    public abstract boolean canPlay();

    public abstract void resolve();

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String cost) {
        manaCost = cost;
        manaToPlay = cost;
        this.cost.updateCost(new ManaCost(cost));
    }

    public void setManaCost(ManaCost cost) {
        manaCost = cost.toString();
        manaToPlay = manaCost;
        this.cost.updateCost(new ManaCost(manaCost));
    }

    public CostList getCost() {
        return this.cost;
    }

    public void addCost(Cost cost) {
        this.cost.addCost(cost);
    }

    public boolean isSpell() {
        return spell && !hasAspect(MagicWarsModel.ASPECT_ATTACHED) && !sourceCard.isLand();
    }

    /**
     * Is this spell ability attached to other effect.
     */
    public boolean isAttached() {
        return hasAspect(MagicWarsModel.ASPECT_ATTACHED);
    }

    public boolean isAbility() {
        return !isSpell();
    }

    public boolean isTapAbility() {
        return tapAbility;
    }

    public Card getSourceCard() {
        return sourceCard;
    }

    public boolean isInstant() {
        return getSourceCard().isInstant();
    }

    public boolean isSorcery() {
        return getSourceCard().isSorcery();
    }

    public void setSourceCard(Card card) {
        this.sourceCard = card;
    }

    public void setChoiceCommand(ChoiceCommand choiceCommand) {
        this.choiceCommand = choiceCommand;
    }

    public ChoiceCommand getChoiceCommand() {
        return choiceCommand;
    }

    public void formChoice() {
        if (choiceCommand != null) {
            choiceCommand.execute();
            choice = choiceCommand.getResult();
        }
    }

    public void formAbilityChoice() {
        if (choiceCommand != null) {
            choiceCommand.execute();
            abililtyChoice = choiceCommand.getAbilityResult();
        }
    }

    public void setPayChoiceCommand(ChoiceCommand payChoiceCommand) {
        this.payChoiceCommand = payChoiceCommand;
    }

    public void formPayChoice() {
        if (payChoiceCommand != null) {
            payChoiceCommand.execute();
            choice = payChoiceCommand.getResult();
        }
    }

    public String getChoiceDetailedDescription() {
        return choiceDetailedDescription;
    }

    public String getChoiceDescription() {
        return choiceDescription;
    }

    public CardList getChoice() {
        return choice;
    }

    public void setChoice(CardList cardList) {
        this.choice = cardList;
    }

    public AbilityList getAbililtyChoice() {
        return abililtyChoice;
    }

    public void setChoiceDetailedDescription(String choiceDetailedDescription) {
        this.choiceDetailedDescription = choiceDetailedDescription;
    }

    public void setChoiceDescription(String shortDescription) {
        this.choiceDescription = shortDescription;
    }

    public void setStackDescription(String s) {
        stackDescription = s;
    }

    public String getStackDescription() {
        if (stackDescription.isEmpty() && getSourceCard() != null) {
            if (!description.isEmpty()) stackDescription = getSourceCard().toString() + ": " + description; else stackDescription = getSourceCard().toString() + ": " + getSourceCard().getText();
        }
        if (stackDescription.contains("{this}")) {
            stackDescription = stackDescription.replace("{this}", getSourceCard().getName());
        }
        return stackDescription;
    }

    public void setDescription(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        if (description == null || description.isEmpty()) {
            if (getSourceCard() != null) {
                return getSourceCard().getName();
            }
        }
        return description;
    }

    public Card getTargetCard() {
        if (targetCards == null || targetCards.size() == 0) {
            return null;
        } else {
            return targetCards.get(0);
        }
    }

    public CardList getTargetCards() {
        if (targetCards == null) {
            targetCards = new CardList();
        }
        return targetCards;
    }

    public void setTargetCard(Card card) {
        targetPlayer = null;
        if (card != null) {
            targetCards = new CardList();
            targetCards.add(card);
            String s = getStackDescription();
            if (s.equals("")) {
                setStackDescription(getSourceCard().getName() + " - targeting " + card);
            } else {
                if (!getStackDescription().contains(" - targeting " + card)) {
                    setStackDescription(getStackDescription() + " - targeting " + card);
                }
            }
        } else {
            targetCards = null;
        }
    }

    public void setTargetCards(CardList cards) {
        targetPlayer = null;
        targetCards = cards;
        if (!getStackDescription().contains("targeting") && cards != null) {
            setStackDescription(getStackDescription() + " - targeting " + cards);
        }
    }

    public void addTargetCard(Card card) {
        if (targetCards == null) {
            targetCards = new CardList();
        }
        targetCards.add(card);
        if (targetCards.size() == targetCount) {
            setStackDescription(getSourceCard().getName() + ": " + getStackDescription() + " - targeting " + targetCards);
        }
    }

    public void setTargetPlayer(String p) {
        String s = getStackDescription();
        if (s.equals("")) {
            setStackDescription(getSourceCard().getName() + " [targeting " + p + "]");
        } else {
            if (s.contains(" [")) {
                int pos = s.indexOf(" [");
                s = s.substring(0, pos);
            }
            setStackDescription(s + " [targeting " + p + "]");
        }
        if ((getStackDescription().contains(" X ") || getStackDescription().contains("+X")) && isNeedsToChooseX) {
            setStackDescription(getStackDescription() + "[X:" + getXValue() + "]");
        }
        targetPlayer = p;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public int getTargetPlayerID() {
        return targetPlayerID;
    }

    public boolean isTargetPlayer(int playerId) {
        return getTargetPlayerID() == playerId;
    }

    public void setTargetPlayerID(int targetPlayerID) {
        this.targetPlayerID = targetPlayerID;
    }

    public SpellAbility getTargetAbility() {
        if (targetAbilities == null || targetAbilities.size() == 0) {
            return null;
        } else {
            return targetAbilities.get(0);
        }
    }

    public void setTargetAbility(SpellAbility sa) {
        if (sa != null) {
            targetAbilities = new AbilityList();
            targetAbilities.add(sa);
        } else {
            targetAbilities = null;
        }
    }

    public void setTargetAbilities(AbilityList list) {
        targetAbilities = list;
    }

    public void addTargetAbility(SpellAbility sa) {
        if (targetAbilities == null) {
            targetAbilities = new AbilityList();
        }
        targetAbilities.add(sa);
    }

    public AbilityList getTargetAbilities() {
        return targetAbilities;
    }

    public boolean isNeedsToChooseCard() {
        return isNeedsToChooseCard;
    }

    public void setNeedsToChooseCard(boolean isNeedsToChooseCard) {
        this.isNeedsToChooseCard = isNeedsToChooseCard;
    }

    public void setNeedsToChooseCard(final int playerId, final GameZone zone, final Closure closure) {
        setNeedsToChooseCard(playerId, zone, closure, -1);
    }

    @SuppressWarnings("serial")
    public void setNeedsToChooseCard(final int playerId, final GameZone zone, final Closure closure, final int count) {
        setNeedsToChooseCard(true);
        this.choiceCommand = new ChoiceCommand() {

            public void execute() {
                CardListFilter filter = null;
                if (closure != null) {
                    filter = new CardListFilter() {

                        public boolean addCard(Card c) {
                            Card card = getSourceCard();
                            setProperty(card, "$permanent", c);
                            setProperty(card, "$card", c);
                            setProperty(card, "$this", getSourceCard());
                            try {
                                return (Boolean) closure.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
                }
                CardList specificCards = null;
                GameManager game = getSourceCard().getGame();
                if (zone.equals(GameZone.Library)) {
                    specificCards = game.getPlayerById(playerId).getLibrary().getCardList();
                } else if (zone.equals(GameZone.Hand)) {
                    specificCards = new CardList(game.getPlayerById(playerId).getHandCards());
                } else if (zone.equals(GameZone.Battlefield)) {
                    if (playerId == 0) {
                        specificCards = game.getBattlefield().getAllPermanents();
                    } else {
                        specificCards = game.getBattlefield().getPermanentList(playerId);
                    }
                } else {
                    log.error("setNeedsToChooseCard(), not implemented for: " + zone);
                    return;
                }
                if (specificCards != null) {
                    if (count != -1) {
                        specificCards = specificCards.subList(0, count);
                    }
                    if (filter != null) {
                        specificCards = specificCards.filter(filter);
                    }
                    setInputChoice(specificCards);
                }
            }
        };
    }

    protected void setProperty(Card card, String property, Object value) {
        if (card.getGroovyRef() == null) {
            throw new RuntimeException("GroovyRef is null for the card: " + card.getName() + "[" + card.getUniqueNumber() + "]");
        }
        GroovyScriptBindings bindings = card.getGroovyRef().getBindings();
        bindings.setProperty(property, value);
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getCount() {
        return targetCount;
    }

    public void setCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getTargetsChosen() {
        return targetsChosen;
    }

    public void setTargetsChosen(int targetsChosen) {
        this.targetsChosen = targetsChosen;
    }

    public boolean areTargetsOptional() {
        return areTargetsOptional;
    }

    public void setTargetsAreOptional(boolean areTargetsOptional) {
        this.areTargetsOptional = areTargetsOptional;
    }

    public boolean isExactCount() {
        return isExactCount;
    }

    public void setExactCount(boolean isExactCount) {
        this.isExactCount = isExactCount;
    }

    public boolean wasKickerPayed() {
        return kickerWasPayed;
    }

    public void setKickerWasPayed(boolean kickerWasPayed) {
        this.kickerWasPayed = kickerWasPayed;
    }

    public boolean isDiscardEqualToRemove() {
        return isDiscardEqualToRemove;
    }

    public void setDiscardEqualToRemove(boolean isDiscardEqualToRemove) {
        this.isDiscardEqualToRemove = isDiscardEqualToRemove;
    }

    public boolean isNeedsToChooseX() {
        return isNeedsToChooseX;
    }

    public int getXValue() {
        return xValue;
    }

    public void setNeedsToChooseX(boolean isNeedsToChooseX) {
        this.isNeedsToChooseX = isNeedsToChooseX;
    }

    public void setXValue(int value) {
        if ((getStackDescription().contains(" X ") || getStackDescription().contains("+X")) && isNeedsToChooseX) {
            setStackDescription(getStackDescription() + "[X:" + value + "]");
        }
        xValue = value;
    }

    public boolean isNeedsToChooseX2() {
        return isNeedsToChooseX2;
    }

    public int getXValue2() {
        return xValue2;
    }

    public void setNeedsToChooseX2(boolean isNeedsToChooseX2) {
        this.isNeedsToChooseX2 = isNeedsToChooseX2;
    }

    public void setXValue2(int value) {
        xValue2 = value;
    }

    public String getManaToPlay() {
        return manaToPlay;
    }

    public void setManaToPlay(String manaToPlay) {
        this.manaToPlay = manaToPlay;
    }

    public void reset() {
        this.setTargetCards(null);
        this.setTargetPlayerID(-1);
        this.setManaToPlay(getManaCost());
        for (Cost cost : getCost()) {
            if (cost instanceof LoyaltyCost) {
                LoyaltyCost lcost = (LoyaltyCost) cost;
                if (lcost.isNeedX()) {
                    lcost.setLoyaltyToPay(0);
                }
            }
        }
    }

    public boolean isCancelStopsPlaying() {
        return isCancelStopsPlaying;
    }

    public void setCancelStopsPlaying(boolean isCancelStopsPlaying) {
        this.isCancelStopsPlaying = isCancelStopsPlaying;
    }

    public int getFirstChosenAbility() {
        return firstChosenAbility;
    }

    public void setFirstChosenAbility(int firstChosenAbility) {
        this.firstChosenAbility = firstChosenAbility;
    }

    public int getSecondChosenAbility() {
        return secondChosenAbility;
    }

    public void setSecondChosenAbility(int secondChosenAbility) {
        this.secondChosenAbility = secondChosenAbility;
    }

    public boolean isAbilityOptional() {
        return isAbilityOptional;
    }

    public String getYesNoQuestion() {
        return yesNoQuestion;
    }

    public void setYesNoQuestion(String yesNoQuestion) {
        this.isAbilityOptional = true;
        this.yesNoQuestion = yesNoQuestion;
    }

    public boolean isNeedsToChooseAbility() {
        return isNeedsToChooseAbility;
    }

    public void setNeedsToChooseAbility(boolean isNeedsToChooseAbility) {
        this.isNeedsToChooseAbility = isNeedsToChooseAbility;
    }

    public int getPlayerIdToAsk() {
        return playerIdToAsk;
    }

    public void setPlayerIdToAsk(int playerIdToAsk) {
        this.playerIdToAsk = playerIdToAsk;
    }

    public void cancelCommand() {
        if (cancelCommand != null) {
            cancelCommand.execute();
        }
    }

    public Command getCancelCommand() {
        return cancelCommand;
    }

    public void setCancelCommand(Command cancelCommand) {
        this.cancelCommand = cancelCommand;
    }

    public Card getRememberedCard() {
        return rememberedCard;
    }

    public void setRememberedCard(Card rememberedCard) {
        this.rememberedCard = rememberedCard;
    }

    public boolean isNeedsToChooseAbilityToPlay() {
        return isNeedsToChooseAbilityToPlay;
    }

    public void setNeedsToChooseAbilityToPlay(boolean isNeedsToChooseAbilityToPlay) {
        this.isNeedsToChooseAbilityToPlay = isNeedsToChooseAbilityToPlay;
    }

    public String getChooseTargetDescription() {
        return chooseTargetDescription;
    }

    public void setChooseTargetDescription(String chooseTargetDescription) {
        this.chooseTargetDescription = chooseTargetDescription;
    }

    public CardList getPossibleTargets() {
        return choice;
    }

    public void setPossibleTargets(CardList choice) {
        this.choice = choice;
    }

    public boolean isNeedsToChooseCreature() {
        return isNeedsToChooseCreature;
    }

    public void setNeedsToChooseCreature(boolean isNeedsToChooseCreature) {
        this.isNeedsToChooseCreature = isNeedsToChooseCreature;
    }

    public boolean areChoicesOptional() {
        return areChoicesOptional;
    }

    public void setChoicesAreOptional(boolean areChoicesOptional) {
        this.areChoicesOptional = areChoicesOptional;
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public void setChoiceCount(int choiceCount) {
        this.choiceCount = choiceCount;
    }

    public boolean isNeedsToChooseSpecificPermanent() {
        return isNeedsToChooseSpecificPermanent;
    }

    public void setNeedsToChooseSpecificPermanent(boolean isNeedsToChooseSpecificPermanent) {
        this.isNeedsToChooseSpecificPermanent = isNeedsToChooseSpecificPermanent;
    }

    public boolean isNeedsToChooseManaSymbol() {
        return isNeedsToChooseManaSymbol;
    }

    public void setNeedsToChooseManaSymbol(boolean isNeedsToChooseManaSymbol) {
        this.isNeedsToChooseManaSymbol = isNeedsToChooseManaSymbol;
    }

    public Command getSpellChain() {
        return spellChain;
    }

    public void setSpellChain(Command spellChain) {
        this.spellChain = spellChain;
    }

    public SpellAbility getCopy() {
        SpellAbility clone = null;
        try {
            clone = (SpellAbility) this.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }
        return clone;
    }

    public SpellAbility addChain(SpellAbility chain) {
        return SpellChainFactory.addChain(this, chain);
    }

    public boolean hasAspect(String aspect) {
        if (aspects == null) {
            return false;
        }
        return aspects.containsKey(aspect);
    }

    public void addAspect(String aspect) {
        if (aspects == null) {
            aspects = new HashMap<String, List<Serializable>>();
        }
        aspects.put(aspect, null);
    }

    public void addAspect(String aspect, Serializable value) {
        if (aspects == null) {
            aspects = new HashMap<String, List<Serializable>>();
        }
        if (aspects.containsKey(aspect)) {
            List<Serializable> values = aspects.get(aspect);
            if (values == null) {
                values = new ArrayList<Serializable>();
            }
            values.add(value);
            aspects.put(aspect, values);
        } else {
            ArrayList<Serializable> values = new ArrayList<Serializable>();
            values.add(value);
            aspects.put(aspect, values);
        }
    }

    public void removeAspect(String aspect) {
        if (aspects == null) {
            return;
        }
        aspects.remove(aspect);
    }

    public Serializable getAspectValue(String aspect) {
        if (aspects == null) {
            return "";
        }
        if (aspects.containsKey(aspect)) {
            List<Serializable> values = aspects.get(aspect);
            if (values != null) {
                return values.get(values.size() - 1);
            }
        }
        return "";
    }

    public List<Serializable> getAspectValues(String aspect) {
        if (aspects == null) {
            return new ArrayList<Serializable>();
        }
        if (aspects.containsKey(aspect)) {
            List<Serializable> values = aspects.get(aspect);
            if (values != null) {
                return values;
            }
        }
        return new ArrayList<Serializable>();
    }

    public String getMultikickerCost() {
        return multikickerCost;
    }

    public void setMultikickerCost(String multikickerCost) {
        this.multikickerCost = multikickerCost;
    }

    protected void savePreviousValues(Closure closure, HashMap<String, Object> previousValues) {
        if (closure.getThisObject() instanceof groovy.lang.Script) {
            try {
                SimpleBindings sb = (SimpleBindings) ((groovy.lang.Script) closure.getThisObject()).getBinding().getVariables();
                for (String key : sb.keySet()) {
                    if (key.startsWith("$")) {
                        previousValues.put(key, sb.get(key));
                    }
                }
            } catch (Exception e) {
            }
        } else {
            if (getSourceCard().getGroovyRef() == null) {
                throw new RuntimeException("GroovyRef is null for the card: " + getSourceCard().getName() + "[" + getSourceCard().getUniqueNumber() + "]");
            }
            GroovyScriptBindings bindings = getSourceCard().getGroovyRef().getBindings();
            SimpleBindings sb = bindings.getBindingsRef();
            for (String key : sb.keySet()) {
                if (key.startsWith("$")) {
                    previousValues.put(key, sb.get(key));
                }
            }
        }
    }

    protected void restorePreviousValues(Closure closure, HashMap<String, Object> previousValues) {
        if (closure.getThisObject() instanceof groovy.lang.Script) {
            for (String key : previousValues.keySet()) {
                closure.setProperty(key, previousValues.get(key));
            }
        } else {
            if (getSourceCard().getGroovyRef() == null) {
                throw new RuntimeException("GroovyRef is null for the card: " + getSourceCard().getName() + "[" + getSourceCard().getUniqueNumber() + "]");
            }
            GroovyScriptBindings bindings = getSourceCard().getGroovyRef().getBindings();
            for (String key : previousValues.keySet()) {
                bindings.setProperty(key, previousValues.get(key));
            }
        }
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public boolean isManaAbility() {
        return false;
    }

    public boolean isTriggeredAbility() {
        return false;
    }

    public boolean isActivatedAbility() {
        return false;
    }
}
