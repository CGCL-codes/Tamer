package com.uglygreencar.games.crazy8.web;

import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Crazy8Cookie {

    private static final String COOKIE_NAME = "crazy8s";

    private static final String DELIMINATOR = "|";

    private String gameId;

    private int playerNumber;

    private Crazy8Cookie(String gameId, int playerNumber) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
    }

    /*****************************************************************
	 * Creates the HTML Cookie and places into HTTP Response
	 * 
	 * @param gameId
	 * @param playerId
	 *****************************************************************/
    public static void create(HttpServletResponse response, String gameId, int playerNumber) {
        Crazy8Cookie c = new Crazy8Cookie(gameId, playerNumber);
        Cookie cookie = new Cookie(COOKIE_NAME, c.toString());
        response.addCookie(cookie);
    }

    /******************************************************************
	 * 
	 * @return
	 ******************************************************************/
    public static Crazy8Cookie get(HttpServletRequest request) {
        for (Cookie c : request.getCookies()) {
            if (c.getName().equalsIgnoreCase(COOKIE_NAME)) {
                StringTokenizer t = new StringTokenizer(c.getValue(), DELIMINATOR);
                return new Crazy8Cookie(t.nextToken(), Integer.parseInt(t.nextToken()));
            }
        }
        return null;
    }

    public String getGameId() {
        return this.gameId;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.gameId);
        s.append(DELIMINATOR);
        s.append(this.playerNumber);
        return s.toString();
    }
}
