package de.rowbuddy.client.events;

public class StartTripEvent extends AbstractEvent<StartTripPresenterChanger> {

    public static Type<StartTripPresenterChanger> TYPE = new Type<StartTripPresenterChanger>();

    public static final String HISTORY_IDENTIFIER = "StartTrip";

    @Override
    public Type<StartTripPresenterChanger> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toHistoryItem() {
        return HISTORY_IDENTIFIER;
    }
}
