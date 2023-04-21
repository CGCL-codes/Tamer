package com.barbarianprince.bus.event2;

import com.barbarianprince.bus.EventCatalog;
import com.barbarianprince.bus.condition.Condition;
import com.barbarianprince.bus.enums.HexType;
import com.barbarianprince.bus.hex.MapHex;
import com.barbarianprince.bus.hex.TempleHex;
import com.barbarianprince.bus.hex.TownCastleTemple;
import com.barbarianprince.bus.turn.Flow;
import com.barbarianprince.main.Globals;
import com.barbarianprince.main.controllers.BPController;
import com.dalonedrau.d20.Diceroller;

/**
 * @author <i>DaLoneDrau</i>
 *
 */
public final class NewsEvent extends Event {

    /** the title for all messages. */
    private static final String TITLE = "News & Events";

    /** flag indicating the event has been fired at least once. */
    private boolean fired;

    /** 
	 * flag indicating the party has been asked 
	 * to spend gold to hear better news.
	 */
    private boolean askedToSpendGold;

    /**
	 * Creates a new instance of <code>HexEvent</code>.
	 * with no <code>Condition</code>
	 */
    public NewsEvent() {
        super(null);
    }

    /**
	 * Creates a new instance of <code>HexEvent</code>.
	 * with no <code>Condition</code>
	 * @param condition the <code>Condition</code> 
	 * under which this event fires. can be null
	 */
    public NewsEvent(final Condition condition) {
        super(condition);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Event clone() {
        return new NewsEvent(super.getCondition());
    }

    /**
	 * Checks for duplicate news events, to make sure they don't happen
	 * twice in the same location.
	 * @param roll the current die roll
	 * @param feelsAtHome flag indicating the player 
	 * "feels at home" in this location
	 * @return boolean
	 */
    private boolean checkDuplicates(final int roll, final boolean feelsAtHome) {
        MapHex hex = BPController.getPartyLocation();
        boolean dup = false;
        final int templeRites = 4, feelsLikeHome = 5;
        if (roll == templeRites) {
            TempleHex nearest = null;
            if (hex instanceof TempleHex) {
                nearest = (TempleHex) hex;
            } else {
                nearest = (TempleHex) hex.findNearest(HexType.TEMPLE);
            }
            if (nearest.knowsRites()) {
                dup = true;
            }
        } else if (roll == feelsLikeHome && feelsAtHome) {
            dup = true;
        }
        return dup;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void fire() {
        if (super.happens()) {
            final int five = 5;
            if (!fired) {
                int modifiers = 0;
                int roll = Diceroller.rollXdY(2, Globals.DIE_VALUE);
                if (askedToSpendGold) {
                    boolean spendGold = BPController.getPlayerAnswerToSmallYesNo();
                    if (spendGold) {
                        modifiers++;
                        BPController.spendPartyGold(five);
                    }
                } else if (BPController.getPartyGold() >= five) {
                    askedToSpendGold = true;
                    BPController.showSmallYesNoQuestion(NewsEvent.TITLE, getDescriptor(), "Yes", "No");
                    return;
                }
                if (BPController.getPrince().getWit() >= five) {
                    modifiers++;
                }
                boolean feelsAtHome = false;
                MapHex loc = BPController.getPartyLocation();
                if (loc instanceof TownCastleTemple) {
                    TownCastleTemple hex = (TownCastleTemple) BPController.getPartyLocation();
                    if (hex.feelsAtHome()) {
                        modifiers++;
                        feelsAtHome = true;
                    }
                }
                roll += modifiers;
                while (checkDuplicates(roll, feelsAtHome)) {
                    roll = Diceroller.rollXdY(2, Globals.DIE_VALUE);
                    roll += modifiers;
                }
                Flow.addEventToBeginning(getEvent(3));
                super.setResolved();
            }
        } else {
            super.setResolved();
        }
    }

    /**
	 * Gets a descriptor of the event.
	 * @return <code>String</code>
	 */
    private String getDescriptor() {
        StringBuffer descriptor = new StringBuffer();
        descriptor.append("<p>You spend the day in taverns, streets, ");
        descriptor.append("and the market talking and learning.  You ");
        descriptor.append("can learn more if you spread around some cash.<br>");
        descriptor.append("<center><strong>Will you spend 5 gold ");
        descriptor.append("to try to hear more news?");
        descriptor.append("</strong></center></p>");
        return descriptor.toString();
    }

    /**
	 * Rolls the dice and determines which random news event happens.
	 * @param number the die roll
	 * @return <code>Event</code>
	 */
    private Event getEvent(final int number) {
        Event event = null;
        MapHex hex = BPController.getPartyLocation();
        StringBuffer buffer = new StringBuffer();
        final int two = 2, three = 3, four = 4, five = 5, six = 6, seven = 7, eight = 8, nine = 9;
        switch(number) {
            case two:
                event = new SmallMessageEvent(TITLE, "<p>No news of note; nothing seems to be happening.</p>");
                break;
            case three:
                final int gold50 = 50;
                buffer.append("<p>You discover an empty thieves' hideout, and ");
                buffer.append("loot the place while they're gone, gaining 50 ");
                buffer.append("gold.</p>");
                event = new GoldEvent(gold50, TITLE, buffer.toString());
                break;
            case four:
                TempleHex nearest = null;
                if (hex instanceof TempleHex) {
                    nearest = (TempleHex) hex;
                } else {
                    nearest = (TempleHex) hex.findNearest(HexType.TEMPLE);
                }
                buffer.append("<p>You learn more about the sacred rites of ");
                if (nearest.getName().toLowerCase().startsWith("temple of")) {
                    buffer.append("the ");
                }
                buffer.append(nearest.getName());
                buffer.append(". With this knowledge, the Gods are sure to ");
                buffer.append("look upon you with favor when you make ");
                buffer.append("offerings there.</p>");
                nearest.setKnowsRites(true);
                event = new SmallMessageEvent(TITLE, buffer.toString());
                break;
            case five:
                TownCastleTemple tct = (TownCastleTemple) hex;
                tct.setFeelsAtHome(true);
                buffer.append("<p>You don't learn anything important, ");
                buffer.append("instead managing to get a feel for this ");
                buffer.append("municipality and its politics. It will ");
                buffer.append("be easier to learn rumors or hire ");
                buffer.append("followers here from now on.</p>");
                event = new SmallMessageEvent(TITLE, buffer.toString());
                break;
            case six:
                buffer.append("<p>You learn that a large caravan has stopped ");
                buffer.append("here for the day.  Do you wish to visit them?</p>");
                event = new CompoundSmallYesNoMessageEvent(TITLE, buffer.toString(), "Yes", "No", EventCatalog.getEvent("e129"));
                break;
            case seven:
                buffer.append("<p>You discover a small inn far away from the ");
                buffer.append("market square.  Lodging and meals will be ");
                buffer.append("half price in this location.</p>");
                event = new SmallMessageEvent(TITLE, buffer.toString());
                tct = (TownCastleTemple) hex;
                tct.setFoundCheaperLodgings(true);
                break;
            case eight:
                int goldHalf = BPController.getPartyGold();
                goldHalf = (int) Math.ceil((double) goldHalf / (double) 2);
                buffer.append("<p>A small child looking over her shoulder runs ");
                buffer.append("straight into you.  You help the crying girl ");
                buffer.append("to her feet and help her on her way.  An hour ");
                buffer.append("later you discover your purse is lighter.</p>");
                BPController.spendPartyGold(goldHalf);
                event = new SmallMessageEvent(TITLE, buffer.toString());
                break;
            case nine:
                CompoundEvent ce = new CompoundEvent();
                buffer.append("<p>Your questions attract the attention ");
                buffer.append("of the local authorities.</p>");
                SmallMessageEvent sme = new SmallMessageEvent(TITLE, buffer.toString());
                ce.add(sme);
                ce.add(EventCatalog.getEvent("e050"));
                event = ce;
                break;
        }
        return event;
    }
}
