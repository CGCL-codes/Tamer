package org.freelords.questmanager;

import java.util.ArrayList;

/**
  * Interface for managing the quest within an interface.
  *
  * @author Andrea Paternesi
  */
public interface QuestInterface {

    /**
     * Get progress information 
     */
    public String getProgress();

    /**
     * Provide the lines of the message describing
     * the quest completion.
     */
    public void getSuccessMsg(ArrayList<String> msgs);

    /**
     * Provide the lines of the message describing
     * the quest expiration.
     */
    public void getExpiredMsg(ArrayList<String> msgs);

    /** Checks and returns if quest is still valid (hero living etc.) */
    public boolean isActive();
}
