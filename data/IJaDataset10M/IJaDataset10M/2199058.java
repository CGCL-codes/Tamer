package net.sourceforge.pokerapp.ai;

import net.sourceforge.pokerapp.*;

/****************************************************
 * PlayerModel is a class that is used to remember actions that other players did in each
 * circumstance.  It is very basic class which will have to be extened to be useful.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public class PlayerModel {

    /**
 * The AIApp instance that this PokerModel class belongs to.
 **/
    protected AIApp theAIApp;

    /**
 * The server that started this PokerModel class.
 **/
    protected StartPoker server;

    /**
 * The name of the player.
 **/
    protected String name;

    /***********************
 * Constructor creates a new AIApp
 * 
 * @param a The AIApp instance to which this PlayerModel belongs
 * @param n The name of this player.
 *
 **/
    public PlayerModel(AIApp a, String n) {
        theAIApp = a;
        name = n;
        theAIApp.log("Constructing PokerModel.", 3);
        server = theAIApp.startUpApp;
    }

    /**********************
 * getName() is used to access the name variable
 *
 * @return The name of this player
 *
 **/
    public String getName() {
        return name;
    }
}
