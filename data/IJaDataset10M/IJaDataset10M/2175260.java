package com.barbarianprince.bus.event2;

import com.barbarianprince.bus.PartyMember;
import com.barbarianprince.bus.condition.Condition;
import com.barbarianprince.bus.turn.Flow;
import com.barbarianprince.main.controllers.BPController;

/**
 * A collection of <code>Event</code>s that fires one randomly.
 * @author <i>Uard'lanod</i>
 */
public class RestEvent extends Event {

    /** flag indicating the rest message was displayed. */
    private boolean displayedRestMessage;

    /** flag indicating the rest comes with a possible location-based event. */
    private boolean hasHexEvent;

    /** the location-based event. */
    private HexEvent hexEvent;

    /**
	 * Creates a new instance of RestEvent with 
	 * a possible location-based event.
	 */
    public RestEvent() {
        this(null, true);
    }

    /**
	 * Creates a new instance of RestEvent.
	 * @param hasEvent flag indicating a possible location-based event
	 */
    public RestEvent(final boolean hasEvent) {
        this(null, hasEvent);
    }

    /**
	 * Creates a new instance of RestEvent.
	 * @param c the Condition associated with this event
	 */
    public RestEvent(final Condition c) {
        this(c, true);
    }

    /**
	 * Creates a new instance of RestEvent.
	 * @param c the Condition associated with this event
	 * @param hasEvent flag indicating a possible location-based event
	 */
    public RestEvent(final Condition c, final boolean hasEvent) {
        super(c);
        hasHexEvent = hasEvent;
        displayedRestMessage = false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Event clone() {
        return new RestEvent(super.getCondition(), hasHexEvent);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void fire() {
        if (super.happens()) {
            System.out.println("rest happens");
            if (hasHexEvent && hexEvent == null) {
                switch(BPController.getPartyLocation().getType()) {
                    case TEMPLE:
                    case RUINS:
                    case CASTLE:
                    case TOWN:
                        hasHexEvent = false;
                        break;
                    default:
                        hexEvent = new HexEvent();
                        break;
                }
            }
            if (!displayedRestMessage) {
                System.out.println("displaying message");
                displayedRestMessage = true;
                BPController.showSmallMessage(null, "Rested", "The party rests for the day.");
            } else if (hasHexEvent && !hexEvent.isResolved()) {
                System.out.println("resolving hex event");
                Flow.addEventToBeginning(hexEvent);
                Flow.fireActions();
            } else {
                System.out.println("healing and resolving");
                PartyMember[] members = BPController.getPartyMembers();
                for (int i = 0; i < members.length; i++) {
                    if (!members[i].isDead()) {
                        members[i].heal(1);
                    }
                }
                BPController.setPartyRested(true);
                super.setResolved();
            }
        } else {
            super.setResolved();
        }
    }
}
