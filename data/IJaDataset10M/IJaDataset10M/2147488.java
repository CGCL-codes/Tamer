package com.googlecode.theifstatements.oriontrail;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * The Orion Trail IntroViewController - Initializes the game's execution, and gives Wagon the user's input data from
 * introFrame CS 2340 MWF 12PM
 * 
 * @author The If Statements
 * @version 1.0 10/05/2011
 **/
public class IntroViewController implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1624948852744303744L;

    static Wagon wagon = new Wagon();

    static introFrame frame = new introFrame();

    static StoreViewController storeViewController;

    /**
     * The constructor creates instances of the introFrame and the Wagon. It begins execution, and sets the frame to be
     * visible.
     * 
     * @author David Sharpe
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * This method pulls the names and profession data from introFrame and uses it to create PartyMembers and the
     * PartyLeader, by calling on a method in Wagon. It's called when the start button is hit in introFrame. It then
     * closes the window, and creates an instance of StoreViewController so that the game can continue. Before moving
     * on, however, it checks with the frame to make sure the player has selected a pace, a ration, and a profession.
     */
    public void Go() {
        wagon.makeLeader(frame.getLeaderName(), frame.getProfession());
        wagon.makePartyMembers(frame.getMember1Name(), frame.getMember2Name(), frame.getMember3Name(), frame.getMember4Name());
        setPace();
        setRations();
        frame.setVisible(false);
        storeViewController = new StoreViewController(wagon);
    }

    /**
     * setter for the pace
     */
    public void setPace() {
        if (frame.getPace() == "Leisurely") {
            wagon.setTPace("Leisurely");
            wagon.settPaceFormula(1);
        } else if (frame.getPace() == "Steady") {
            wagon.setTPace("Steady");
            wagon.settPaceFormula(2);
        } else {
            wagon.setTPace("Grueling");
            wagon.settPaceFormula(40);
        }
    }

    /**
     * setter for the rations
     */
    public void setRations() {
        if (frame.getRations() == "Meager") {
            wagon.setRPace("Meager");
            wagon.setrPaceFormula(2);
        } else if (frame.getRations() == "Normal") {
            wagon.setRPace("Normal");
            wagon.setrPaceFormula(3);
        } else if (frame.getRations() == "Well-Fed") {
            wagon.setRPace("Well-Fed");
            wagon.setrPaceFormula(5);
        } else {
            wagon.setRPace("Bare-bones");
            wagon.setrPaceFormula(1);
        }
    }

    /**
     * The load function loads savedWagon.data into a FileInputStream, then converts it into an object It casts that
     * object into a Wagon, and makes it our new Wagon It then moves on to the storeViewController All exceptions are
     * caught
     */
    public void load() {
        try {
            FileInputStream wagonIn = new FileInputStream("savedWagon.data");
            ObjectInputStream thingIn = new ObjectInputStream(wagonIn);
            System.out.println("Let's see how far I get");
            wagon = (Wagon) thingIn.readObject();
            thingIn.close();
            frame.setVisible(false);
            storeViewController = new StoreViewController(wagon);
        } catch (FileNotFoundException e) {
            System.out.println("Error 1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error 2");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error 3");
            e.printStackTrace();
        }
    }

    public static Wagon getWagon() {
        return wagon;
    }
}
