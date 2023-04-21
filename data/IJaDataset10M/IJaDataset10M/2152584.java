package net.sourceforge.pokerapp.ai;

import net.sourceforge.pokerapp.*;
import java.util.ArrayList;
import javax.swing.*;

/****************************************************
 * AILogic is an abstract class that must be extended in order to implement an AI player. 
 * This class really doesn't do much - it just has a couple common functions and sets up the AILogic class.
 * Any AI to be written must be an extention of this class.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public abstract class AILogic {

    /**
 * List of values that define which game this AI likes to deal
 **/
    protected ArrayList gamePreference;

    /**
 * The AIApp instance that this logic class belongs to
 **/
    protected AIApp theAIApp;

    /**
 * The server that started this AI
 **/
    protected StartPoker server;

    /**
 * The string on the game currently being played
 **/
    protected String gameClass;

    /**
 * This AI's hand
 **/
    protected Hand myHand;

    /**
 * The position of this player.  Basically seat number from dealer
 **/
    protected int myPosition;

    /**
 * Number of players still in on this round
 **/
    protected int numPlayersIn;

    /**
 * Number of players that started playing this game
 **/
    protected int numPlayersDealt;

    /**
 * What action the game is currently on
 **/
    protected int actionNum;

    /**
 * What was the previous value of actionNum.  If change, then the game action changed.
 **/
    protected int prevActionNum;

    /**
 * The rank of this AI's hand
 **/
    protected float handRank;

    /**
 * Best possible hand that can be made with current cards showing.
 **/
    protected float bestPossible;

    /**
 * List of previous actions performed in this hand.
 **/
    protected ArrayList prevActions;

    /**
 * List of player models.
 **/
    protected ArrayList playerModels;

    /**********************
 * Constructor creates the AILogic class
 *
 * @param a The AIApp to which this class belongs
 *
 **/
    public AILogic(AIApp a) {
        theAIApp = a;
        theAIApp.log("Constructing AILogic", 3);
        server = theAIApp.startUpApp;
        gamePreference = new ArrayList();
        gameClass = new String();
        myHand = new Hand();
        myPosition = 0;
        numPlayersIn = 0;
        numPlayersDealt = 0;
        actionNum = 0;
        prevActionNum = 0;
        handRank = 0.0f;
        bestPossible = 0.0f;
        prevActions = new ArrayList();
        playerModels = new ArrayList();
    }

    /**********************
 * timeToBet() is an abstract function that must be defined in any class extending this class.
 * It is used to define what to do when it's this AI's turn to bet 
 **/
    protected abstract void timeToBet();

    /**********************
 * newGame() is an abstract function that must be defined in any class extending this class.
 * It is used to define what happens when a new game starts
 **/
    public abstract void newGame();

    /**********************
 * otherPlayerAction() is an abstract function that must be defined in any class extending this class.
 * It is called every time another player does something
 *
 * @param m The string telling the AI what the other player did.
 *
 **/
    protected abstract void otherPlayerAction(String m);

    /**********************
 * stopLogic() is an abstract function that must be defined in any class extending this class.
 * It is called to shut down the AI logic from running
 **/
    public abstract void stopLogic();

    /**********************
 * getGamePreferences() is used to access the private class variable.
 *
 * @return The game preferences ArrayList which defines which game the AI likes to play
 **/
    public ArrayList getGamePreference() {
        return gamePreference;
    }

    /**********************
 * getGameClass() is used to access the private class variable.
 *
 * @return The String of the game currently being played by this AI
 **/
    public String getGameClass() {
        return gameClass;
    }

    /**********************
 * playerAction() function is called when another player (not this AI) does something.  It then calls the otherPlayerAction() function in
 * the derived classes in cases anything more specific needs to be done.
 *
 * @param m The string description of what the other player did
 *
 **/
    public void playerAction(String m) {
        theAIApp.log("AILogin.playerAction( " + m + " )", 3);
        int i = m.indexOf('&');
        String name = m.substring(0, i);
        String action = m.substring(i + 1, m.length());
        if (server.getGame() != null) {
            actionNum = server.getGame().getActionNum();
        }
        if (prevActionNum != actionNum) {
            prevActionNum = actionNum;
        }
        otherPlayerAction(m);
    }

    /**********************
 * myBet() function calculates some common logic and then calls the specific AILogic routine
 **/
    public void myBet() {
        theAIApp.log("AILogic.myBet()", 3);
        myHand = server.getAIHand(theAIApp);
        if (myHand != null) {
            if (server.getGame() != null) {
                handRank = server.getGame().bestHand(myHand);
                bestPossible = server.getGame().getBestPossible();
                timeToBet();
            } else {
                theAIApp.log("Warning - its the AI's turn to bet, but there isn't a game going on.");
            }
        } else {
            theAIApp.log("Warning - its the AI's turn to bet, but it doesn't have a hand.");
        }
    }

    /**********************
 * position() is used to figure out where you are sitting at the table.
 * A return value of 1 is the first person past the dealer, a return value equal to the number
 * of players at the table means this AI is the dealer.  A return value of zero means it didn't
 * find the position.
 *
 * @return Which seat relative to the dealer this AI is sitting.
 *
 **/
    protected int position() {
        int ret = 0;
        int seat = server.nextSeat(server.dealer.seat);
        for (int i = 0; i < theAIApp.getPlayerList().size(); i++) {
            ret++;
            if (theAIApp.getThisPlayer().seat == seat) {
                theAIApp.log("AILogic.position() returned " + ret, 3);
                return ret;
            }
            seat = server.nextSeat(seat);
        }
        theAIApp.log("Warning : couldn't find position in AILogic.  Returning 0");
        return 0;
    }
}
