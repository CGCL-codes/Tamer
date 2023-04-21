package csa.jportal.ai.enhancedAI.enhancedHints;

/**
 *
 * @author malban
 */
public interface HintOccurrence {

    public static String HINT_OCCURRENCE_WHEN_PLAYED = "WHEN PLAYED";

    public static String HINT_OCCURRENCE_WHEN_ATTACKED = "WHEN ATTACKED";

    public static String HINT_OCCURRENCE_WHEN_BLOCKED = "WHEN BLOCKED";

    public static String HINT_OCCURRENCE_WHEN_ATTACKING = "WHEN ATTACKING";

    public static String HINT_OCCURRENCE_WHEN_BLOCKING = "WHEN BLOCKING";

    public static String HINT_OCCURRENCE_WHEN_IN_PLAY = "WHEN IN PLAY";

    public static String HINT_OCCURRENCE_WHEN_IN_TIMING = "WHEN IN TIMING";

    public static String HINT_OCCURRENCE_WHEN_ACTIVATED = "WHEN ACTIVATED";

    public static String HINT_OCCURRENCE_WHEN_DRAW_CARD = "WHEN DRAW CARD";

    public static String HINT_OCCURRENCE_WHEN_SINGLE_ATTACKER = "WHEN SINGLE ATTACKER";

    public static String HINT_OCCURRENCE_WHEN_IN_COMBAT = "WHEN IN COMBAT";

    public static String HINT_OCCURRENCE_WHEN_COMBAT_RESOLVING = "WHEN COMBAT RESOLVING";

    public static String HINT_OCCURRENCE_WHEN_PUT_TO_GRAVEYARD = "WHEN PUT TO GRAVEYARD";

    public static String HINT_OCCURRENCE_WHEN_NOT_BLOCKED = "WHEN NOT BLOCKED";

    public static String HINT_OCCURRENCE_WHEN_PLAYER_DAMAGE_DEALT = "WHEN PlLAYER DAMAGE DEALT";

    public static String HINT_OCCURRENCE_DEPENDENCY_OPPONENT = "DEPENDENCY ON OPPONENT";

    public static String HINT_OCCURRENCE_DEPENDENCY_PLAYER = "DEPENDENCY ON PLAYER";

    public static String HINT_OCCURRENCE_DEPENDENCY_TYPE = "DEPENDENCY ON TYPE";

    public static String HINT_OCCURRENCE_DEPENDENCY_SUBTYPE = "DEPENDENCY ON SUBTYPE";

    public static String HINT_OCCURRENCE_DEPENDENCY_BATTLEFIELD = "DEPENDENCY ON BATTLEFIELD";

    public static String HINT_OCCURRENCE_DEPENDENCY_LAND = "DEPENDENCY ON LAND";

    public static String[] HINT_OCCURRENCE_ALL = { HINT_OCCURRENCE_WHEN_PLAYED, HINT_OCCURRENCE_WHEN_ATTACKED, HINT_OCCURRENCE_WHEN_BLOCKED, HINT_OCCURRENCE_WHEN_ATTACKING, HINT_OCCURRENCE_WHEN_BLOCKING, HINT_OCCURRENCE_WHEN_IN_PLAY, HINT_OCCURRENCE_WHEN_IN_TIMING, HINT_OCCURRENCE_WHEN_ACTIVATED, HINT_OCCURRENCE_WHEN_DRAW_CARD, HINT_OCCURRENCE_WHEN_PUT_TO_GRAVEYARD, HINT_OCCURRENCE_WHEN_NOT_BLOCKED, HINT_OCCURRENCE_WHEN_PLAYER_DAMAGE_DEALT, HINT_OCCURRENCE_DEPENDENCY_OPPONENT, HINT_OCCURRENCE_DEPENDENCY_PLAYER, HINT_OCCURRENCE_DEPENDENCY_TYPE, HINT_OCCURRENCE_DEPENDENCY_SUBTYPE, HINT_OCCURRENCE_DEPENDENCY_BATTLEFIELD, HINT_OCCURRENCE_DEPENDENCY_LAND, HINT_OCCURRENCE_WHEN_SINGLE_ATTACKER, HINT_OCCURRENCE_WHEN_IN_COMBAT, HINT_OCCURRENCE_WHEN_COMBAT_RESOLVING };

    public static String[] HINT_OCCURRENCE_COMMENTS = { "Effect takes place when card is played.", "Effect takes place when attacked.", "Effect takes place when blocked.", "Effect takes place when attacking.", "Effect takes place when blocking.", "Effect takes place when card is in play.", "Effect takes place as long as timed effect of card lasts (turns / phases).", "Effect takes place when card is activated.", "Effect takes place when card is drawn.", "Effect takes place when card is put to graveyard.", "Effect takes place when card is not blocked.", "Effect takes place after a creature dealt damage to a player.", "Dependency, whether opponent has \"something\"", "Dependency, whether player has \"something\"", "Dependency, whether type is available", "Dependency, whether subtype is available", "Dependency, whether on battlefield \"something\" is available", "Dependency, whether on land \"something\" is available", "Effect takes place when attacking alone.", "Effect takes place, in combat resolve phase.", "Effect takes place during resolve of combat." };

    public static final AIEnhancedHint O_WHEN_PLAYED = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_PLAYED, false);

    public static final AIEnhancedHint O_WHEN_ATTACKED = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_ATTACKED, false);

    public static final AIEnhancedHint O_WHEN_BLOCKED = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_BLOCKED, false);

    public static final AIEnhancedHint O_WHEN_ATTACKING = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_ATTACKING, false);

    public static final AIEnhancedHint O_WHEN_BLOCKING = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_BLOCKING, false);

    public static final AIEnhancedHint O_WHEN_IN_PLAY = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_IN_PLAY, false);

    public static final AIEnhancedHint O_WHEN_IN_TIMING = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_IN_TIMING, false);

    public static final AIEnhancedHint O_WHEN_ACTIVATED = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_ACTIVATED, false);

    public static final AIEnhancedHint O_WHEN_CARD_DRAWN = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_DRAW_CARD, false);

    public static final AIEnhancedHint O_WHEN_PUT_TO_GRAVEYARD = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_PUT_TO_GRAVEYARD, false);

    public static final AIEnhancedHint O_WHEN_NOT_BLOCKED = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_NOT_BLOCKED, false);

    public static final AIEnhancedHint O_WHEN_PLAYER_DAMAGE_DEALT = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_PLAYER_DAMAGE_DEALT, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHO_OPPONENT = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_OPPONENT, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHO_PLAYER = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_PLAYER, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHAT_TYPE = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_TYPE, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHAT_SUBTYPE = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_SUBTYPE, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHERE_FIELD = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_BATTLEFIELD, false);

    public static final AIEnhancedHint O_WHEN_DEPENDENCY_WHERE_LAND = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_DEPENDENCY_LAND, false);

    public static final AIEnhancedHint O_WHEN_SINGLE_ATTACKER = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_SINGLE_ATTACKER, false);

    public static final AIEnhancedHint O_WHEN_IN_COMBAT = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_IN_COMBAT, false);

    public static final AIEnhancedHint O_WHEN_COMBAT_RESOLVING = new AIEnhancedHint(HintDefinition.HINT_TYPE_OCCURRENCE, HINT_OCCURRENCE_WHEN_COMBAT_RESOLVING, false);
}
