package adventure.placeable.trigger;

import adventure.placeable.PlaceableEdit;

/**
 *
 * @author Michael Hanns
 *
 */
public interface TriggerEdit extends PlaceableEdit {

    public String getStandardName();

    public String getTriggeredName();

    public boolean limitedUses();

    public int getTotalUses();

    public TriggerResult[] getResults();

    public void setTriggeredName(String name);

    public void setLimitedUses(boolean val);

    public void setTotalUses(int val);

    public void addResult(TriggerResult t);

    public boolean editResult(TriggerResult t, int position);

    public void moveResultUp(int position);

    public void moveResultDown(int position);

    public void moveConditionUp(int resultPos, int condPos);

    public void moveConditionDown(int resultPos, int condPos);

    public void removeResultAtIndex(int x);
}
