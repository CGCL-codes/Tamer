package com.evver.evvercards.security;

import com.evver.evvercards.Player;

public class AuthorizedPlayer extends Player {

    private static final long serialVersionUID = 1L;

    private char[] answer = null;

    /**
	 * Creates an authorized player
	 * @param username the players user name
	 * @param answer the challenge answer
	 */
    public AuthorizedPlayer(String username, char[] answer) {
        super(username);
        this.answer = answer;
    }

    /**
	 * Gets the challenge answer offered by this player
	 */
    protected char[] getChallengeAnswer() {
        return this.answer;
    }
}
