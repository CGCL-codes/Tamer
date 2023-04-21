package com.barbarianprince.bus.event2;

import com.barbarianprince.bus.condition.Condition;
import com.barbarianprince.bus.enums.AttackRule;
import com.barbarianprince.main.Globals;
import com.dalonedrau.d20.Diceroller;

/**
 * A collection of <code>Event</code>s that fires one randomly.
 * 
 * @author <i>Uard'lanod</i>
 */
public final class Rule330Event extends Event {

    /** the modifier to the encounter roll. */
    private int modifier;

    /** the surprise type. */
    private AttackRule type;

    /**
	 * Creates a new instance of Rule330Event.
	 */
    public Rule330Event() {
        this(null, 0, null);
    }

    /**
	 * Creates a new instance of Rule330Event.
	 * @param newType the rule to follow; can be null
	 */
    public Rule330Event(final AttackRule newType) {
        this(null, 0, newType);
    }

    /**
	 * Creates a new instance of AttackEvent.
	 * @param c the Condition associated with this event
	 * @param newModifier the modifier to the encounter roll
	 */
    public Rule330Event(final Condition c, final int newModifier) {
        this(c, newModifier, null);
    }

    /**
	 * 
	 * Creates a new instance of Rule330Event.java.
	 * @param c the Condition associated with this event
	 * @param newModifier the modifier to the encounter roll
	 * @param newType the rule to follow; can be null
	 */
    public Rule330Event(final Condition c, final int newModifier, final AttackRule newType) {
        super(c);
        modifier = newModifier;
        type = newType;
    }

    /**
	 * Creates a new instance of Rule330Event.
	 * @param newModifier the modifier to the encounter roll
	 */
    public Rule330Event(final int newModifier) {
        this(null, newModifier, null);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Event clone() {
        return new Rule330Event(super.getCondition(), modifier, type);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void fire() {
        if (super.happens()) {
            if (type == null) {
                int roll = Diceroller.rollXdY(2, Globals.DIE_VALUE);
                roll += modifier;
                final int two = 2, three = 3, four = 4, five = 5, six = 6, seven = 7, eight = 8, nine = 9, ten = 10, eleven = 11, twelve = 12;
                if (roll <= two) {
                    type = AttackRule.R310;
                } else if (roll == three) {
                    type = AttackRule.R309;
                } else if (roll == four) {
                    type = AttackRule.R308;
                } else if (roll == five) {
                    type = AttackRule.R307;
                } else if (roll == six) {
                    type = AttackRule.R306;
                } else if (roll == seven) {
                    type = AttackRule.R305;
                } else if (roll == eight) {
                    type = AttackRule.R304;
                } else if (roll == nine) {
                    type = AttackRule.R303;
                } else if (roll == ten) {
                    type = AttackRule.R302;
                } else if (roll == eleven) {
                    type = AttackRule.R301;
                } else if (roll >= twelve) {
                    type = AttackRule.R300;
                }
            }
        }
    }

    /**
	 * Gets the altMessage.
	 * @return String
	 */
    public String getAltMessage() {
        String altMessage = "";
        switch(type) {
            case R300:
                altMessage = "Your attack catches the enemy off-guard.";
                break;
            case R301:
            case R302:
            case R303:
            case R304:
                altMessage = "You manage to strike at the enemy first.";
                break;
            case R305:
            case R306:
            case R307:
            case R308:
            case R309:
                altMessage = "The enemy attacks you first.";
                break;
            case R310:
                altMessage = "The enemy attacks before you can draw your weapons!";
                break;
            default:
                break;
        }
        return altMessage;
    }

    /**
	 * Gets the message.
	 * @return String
	 */
    public String getMessage() {
        String message = "";
        switch(type) {
            case R300:
            case R301:
            case R302:
            case R303:
                message = "Your attack catches the enemy off-guard.";
                break;
            case R304:
            case R305:
            case R306:
                message = "You manage to strike at the enemy first.";
                break;
            case R307:
                message = "The enemy attacks you first.";
                break;
            case R308:
            case R309:
            case R310:
                message = "The enemy attacks before you can draw your weapons!";
                break;
            default:
                break;
        }
        return message;
    }

    /**
	 * Gets the title.
	 * @return String
	 */
    public String getTitle() {
        String title = "";
        switch(type) {
            case R300:
            case R301:
            case R302:
            case R303:
                title = "Surprise!";
                break;
            case R304:
            case R305:
            case R306:
            case R307:
                title = "Fight!";
                break;
            case R308:
            case R309:
            case R310:
                title = "Surprised!";
                break;
            default:
                break;
        }
        return title;
    }

    /**
	 * Gets the type.
	 * @return AttackRule
	 */
    public AttackRule getType() {
        return type;
    }
}
