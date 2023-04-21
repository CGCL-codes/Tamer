package de.tum.team26.eistpoll.shared;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class to be used as a Model (MVC) and transfered from client to server and server to client
 */
public class PollData implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    public int[] optionResults;

    public LinkedList<String> like;

    public LinkedList<String> dislike;

    public boolean alreadyVoted;

    public String title;

    boolean isActive;

    /**
	 * Constructor
	 */
    public PollData() {
        id = -1;
        optionResults = new int[5];
        like = new LinkedList<String>();
        dislike = new LinkedList<String>();
        alreadyVoted = false;
        title = "";
        isActive = false;
    }

    public int[] getOption() {
        return optionResults;
    }

    public String[] getLike() {
        String[] result = new String[like.size()];
        return like.toArray(result);
    }

    public String[] getDisLike() {
        String[] result = new String[dislike.size()];
        return dislike.toArray(result);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
