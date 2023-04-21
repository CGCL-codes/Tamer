package com.redtwitch.craps;

import com.redtwitch.craps.controller.*;
import com.redtwitch.craps.view.*;
import com.redtwitch.craps.utils.*;
import java.util.*;
import java.io.*;

/**
 * This is a main command-line testing class. 
 * A user would call this class's main()
 * and follow the prompts to play.
 *
 * @author  <a href="mailto:glenn@redtwitch.com">Glenn Wilson</a>
 * $Id: Craps.java,v 1.3 2005/06/29 04:44:47 wilsong123 Exp $
 */
public class Craps implements IBetOwner {

    public static final int QUIT = 9;

    public static final int ROLL = 0;

    public static final int MAKE_BET = 1;

    public static final int BACKUP_BET = 2;

    public static final int REMOVE_BET = 3;

    public static final String PASSLINE_BET = "1";

    public static final String DONT_PASSLINE_BET = "2";

    public static final String COME_BET = "5";

    public static final String DONT_COME_BET = "11";

    public static final String HARD_FOUR_BET = "4";

    public static final String HARD_SIX_BET = "6";

    public static final String HARD_EIGHT_BET = "8";

    public static final String HARD_TEN_BET = "10";

    public static final String FIELD_BET = "3";

    public static final String PLACE_FOUR_BET = "14";

    public static final String PLACE_FIVE_BET = "15";

    public static final String PLACE_SIX_BET = "16";

    public static final String PLACE_EIGHT_BET = "18";

    public static final String PLACE_NINE_BET = "19";

    public static final String PLACE_TEN_BET = "20";

    public static final String DONT_PLACE_FOUR_BET = "24";

    public static final String DONT_PLACE_FIVE_BET = "25";

    public static final String DONT_PLACE_SIX_BET = "26";

    public static final String DONT_PLACE_EIGHT_BET = "28";

    public static final String DONT_PLACE_NINE_BET = "29";

    public static final String DONT_PLACE_TEN_BET = "30";

    public static final String RETURN = "0";

    private List bets_;

    private int balance_;

    /** Creates a new instance of Craps.  
        Doesn't actually run the craps simulator.  
        Call run() to start it up.
     */
    public Craps(int balance) {
        bets_ = new ArrayList();
        balance_ = balance;
    }

    /**
     *  Runs the craps simulation.  Accepts input from
     *  the user on the command line.
     */
    public void run() {
        Table table = new Table();
        GameController controller = new GameController();
        BetResolver.DEBUG = true;
        while (true) {
            int action = mainMenu(table, controller);
            if (action == QUIT) break; else if (action == MAKE_BET) makeBet(table, controller); else if (action == BACKUP_BET) backupBet(table, controller); else if (action == ROLL) {
                List resolved_bets = controller.doRoll(table);
                if (resolved_bets != null) {
                    while (resolved_bets.size() > 0) {
                        Bet b = (Bet) resolved_bets.remove(0);
                        this.returnedBet(b);
                        b = null;
                    }
                }
            } else System.out.println("Invalid command: '" + action + "'  Try again.");
        }
        System.out.println("Ending balance:" + balance_);
    }

    /**
     *  Print out the bets and the unique identifiers for each bet
     */
    public void sayHardWaysAndPlaceBets() {
        System.out.println(FIELD_BET + " = Field Bet");
        System.out.println(HARD_FOUR_BET + " = Hard Four");
        System.out.println(HARD_SIX_BET + " = Hard Six");
        System.out.println(HARD_EIGHT_BET + " = Hard Eight");
        System.out.println(HARD_TEN_BET + " = Hard Ten");
        System.out.println(PLACE_FOUR_BET + " = Place Four");
        System.out.println(PLACE_FIVE_BET + " = Place Five");
        System.out.println(PLACE_SIX_BET + " = Place Six");
        System.out.println(PLACE_EIGHT_BET + " = Place Eight");
        System.out.println(PLACE_NINE_BET + " = Place Nine");
        System.out.println(PLACE_TEN_BET + " = Place Ten");
        System.out.println(DONT_PLACE_FOUR_BET + " = Don't Place Four");
        System.out.println(DONT_PLACE_FIVE_BET + " = Don't Place Five");
        System.out.println(DONT_PLACE_SIX_BET + " = Don't Place Six");
        System.out.println(DONT_PLACE_EIGHT_BET + " = Don't Place Eight");
        System.out.println(DONT_PLACE_NINE_BET + " = Don't Place Nine");
        System.out.println(DONT_PLACE_TEN_BET + " = Don't Place Ten");
        System.out.println(RETURN + " = Return");
    }

    /**
     *  finds whether the given response from the user is 
     *  valid -- given if the roll is currently a comeout
     *
     */
    private boolean isValidResponse(String response, boolean comeOut) {
        if (response.equals(RETURN)) return true;
        if (response.equals(HARD_FOUR_BET) || response.equals(HARD_SIX_BET) || response.equals(HARD_EIGHT_BET) || response.equals(HARD_TEN_BET) || response.equals(PLACE_FOUR_BET) || response.equals(PLACE_FIVE_BET) || response.equals(PLACE_SIX_BET) || response.equals(PLACE_EIGHT_BET) || response.equals(PLACE_NINE_BET) || response.equals(PLACE_TEN_BET) || response.equals(DONT_PLACE_FOUR_BET) || response.equals(DONT_PLACE_FIVE_BET) || response.equals(DONT_PLACE_SIX_BET) || response.equals(DONT_PLACE_EIGHT_BET) || response.equals(DONT_PLACE_NINE_BET) || response.equals(DONT_PLACE_TEN_BET) || response.equals(FIELD_BET)) return true;
        if (comeOut) {
            if (response.equals(PASSLINE_BET) || response.equals(DONT_PASSLINE_BET)) return true;
        } else {
            if (response.equals(COME_BET) || response.equals(DONT_COME_BET)) return true;
        }
        return false;
    }

    /**
     *  Make a new bet, query the user for the type of bet and the amount.
     *  
     */
    public void makeBet(Table table, GameController controller) {
        char[] buffer = new char[5];
        InputStreamReader reader = new InputStreamReader(System.in);
        while (true) {
            System.out.println("Making new bet:");
            if (controller.isComeOut()) {
                System.out.println(PASSLINE_BET + " = Pass Line Bet");
                System.out.println(DONT_PASSLINE_BET + " = Don't Pass Line Bet");
            } else {
                System.out.println(COME_BET + " = Come Bet");
                System.out.println(DONT_COME_BET + " = Don't Come Bet");
            }
            sayHardWaysAndPlaceBets();
            try {
                reader.read(buffer);
            } catch (Exception e) {
                continue;
            }
            String response = (new String(buffer)).trim();
            if (isValidResponse(response, controller.isComeOut())) {
                int amount = -1;
                while (amount == -1) {
                    System.out.print("Enter amount of bet:");
                    buffer = new char[20];
                    try {
                        reader.read(buffer);
                        String amt_string = (new String(buffer)).trim();
                        amount = Integer.parseInt(amt_string);
                        if (amount > balance_) {
                            System.out.println("You don't have a high enough balance.");
                            amount = -1;
                        }
                        if (amount <= 0) {
                            System.out.println("Amount must be more than 0.");
                            amount = -1;
                        }
                    } catch (Exception e) {
                        amount = -1;
                        System.out.println("You did not enter a valid number.");
                    }
                }
                balance_ = balance_ - amount;
                Bet newbet = new Bet(this, amount);
                bets_.add(newbet);
                if (response.equals(PASSLINE_BET)) table.addPassLineBet(newbet); else if (response.equals(COME_BET)) table.addComeBarBet(newbet); else if (response.equals(DONT_PASSLINE_BET)) table.addDontPassLineBet(newbet); else if (response.equals(DONT_COME_BET)) table.addDontComeBet(newbet); else if (response.equals(HARD_FOUR_BET)) table.addHardFourBet(newbet); else if (response.equals(HARD_SIX_BET)) table.addHardSixBet(newbet); else if (response.equals(HARD_EIGHT_BET)) table.addHardEightBet(newbet); else if (response.equals(HARD_TEN_BET)) table.addHardTenBet(newbet); else if (response.equals(PLACE_FOUR_BET)) table.addPlaceFourBet(newbet); else if (response.equals(PLACE_FIVE_BET)) table.addPlaceFiveBet(newbet); else if (response.equals(PLACE_SIX_BET)) table.addPlaceSixBet(newbet); else if (response.equals(PLACE_EIGHT_BET)) table.addPlaceEightBet(newbet); else if (response.equals(PLACE_NINE_BET)) table.addPlaceNineBet(newbet); else if (response.equals(PLACE_TEN_BET)) table.addPlaceTenBet(newbet); else if (response.equals(FIELD_BET)) table.addFieldBet(newbet); else if (response.equals(DONT_PLACE_FOUR_BET)) table.addDontPlaceFourBet(newbet); else if (response.equals(DONT_PLACE_FIVE_BET)) table.addDontPlaceFiveBet(newbet); else if (response.equals(DONT_PLACE_SIX_BET)) table.addDontPlaceSixBet(newbet); else if (response.equals(DONT_PLACE_EIGHT_BET)) table.addDontPlaceEightBet(newbet); else if (response.equals(DONT_PLACE_NINE_BET)) table.addDontPlaceNineBet(newbet); else if (response.equals(DONT_PLACE_TEN_BET)) table.addDontPlaceTenBet(newbet);
                break;
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    /**
     *  Backup an existing bet.  Query the user for which bet to backup
     *  and the amount of the odds.
     */
    public void backupBet(Table table, GameController controller) {
        boolean finished = false;
        char[] buffer = new char[20];
        InputStreamReader reader = new InputStreamReader(System.in);
        while (!finished) {
            System.out.println("Select bet to back up with odds:");
            for (Iterator i = bets_.iterator(); i.hasNext(); ) {
                Bet b = (Bet) i.next();
                if (table.isBackupable(b, controller.isComeOut())) {
                    System.out.print(b.getIndex() + " =");
                    System.out.println(table.getBetString(b));
                }
            }
            System.out.println(RETURN + " = Return");
            try {
                buffer = new char[20];
                reader.read(buffer);
                String temp = (new String(buffer)).trim();
                int index = Integer.parseInt(temp);
                if (RETURN.equals(temp)) finished = true;
                for (Iterator j = bets_.iterator(); !finished && j.hasNext(); ) {
                    Bet b = (Bet) j.next();
                    if (index == b.getIndex() && table.isBackupable(b, controller.isComeOut())) {
                        System.out.print("Enter amount of odds:");
                        buffer = new char[20];
                        reader.read(buffer);
                        int amount = Integer.parseInt((new String(buffer)).trim());
                        if ((amount + b.getOdds()) % b.getAmount() != 0) {
                            System.out.println("Total odds must be a multiple" + "of " + b.getAmount());
                        } else if (amount + b.getOdds() > (b.getAmount() * GameUtils.MAX_ODDS)) {
                            System.out.println("Maximum amount of odds is " + (b.getAmount() * GameUtils.MAX_ODDS));
                        } else if (amount <= 0) {
                            System.out.println("Invalid amount entered.");
                        } else {
                            balance_ -= amount;
                            b.setOdds(b.getOdds() + amount);
                            finished = true;
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     *  Handle the returning of a Bet.  Alert the user
     *  to the win and the amount of the win.
     *  TODO:  refactor this to win() and lose()
     */
    public void returnedBet(Bet b) {
        if ((b.getAmount() + b.getOdds() + b.getWinnings()) > 0) System.out.println("Win: " + b.getIndex() + ":" + (b.getAmount() + b.getOdds() + b.getWinnings()));
        if (b.getOdds() > 0) System.out.println("[Odds returned]");
        if (b.getAmountLost() > 0) System.out.println("Loss: " + b.getIndex() + ":" + b.getAmountLost());
        balance_ += b.getAmount();
        balance_ += b.getOdds();
        balance_ += b.getWinnings();
        bets_.remove(b);
    }

    public void win(Table table, Bet b) {
    }

    public void loss(Table table, Bet b) {
    }

    /**
     *  Print the main menu to the user, and accept the response.
     *
     */
    public int mainMenu(Table table, GameController controller) {
        System.out.println("---------------------------------------");
        System.out.println("Balance: " + balance_);
        if (controller.isComeOut()) System.out.println("Come Out Roll"); else System.out.println("Point:" + controller.getPoint());
        System.out.println("Your bets:");
        if (bets_ != null && bets_.size() > 0) for (Iterator i = bets_.iterator(); i.hasNext(); ) {
            Bet b = (Bet) i.next();
            System.out.println(table.getBetString(b));
        } else System.out.println("You have no bets.");
        System.out.println("Options:");
        System.out.println(ROLL + " = Roll");
        System.out.println(MAKE_BET + " = Make New Bet");
        System.out.println(BACKUP_BET + " = Back Up Bet");
        System.out.println(QUIT + " = Quit");
        System.out.println("---------------------------------------");
        int action = -1;
        try {
            char[] buffer = new char[10];
            InputStreamReader reader = new InputStreamReader(System.in);
            reader.read(buffer, 0, 10);
            String temp = (new String(buffer)).trim();
            action = Integer.parseInt(temp);
        } catch (Exception e) {
            e.printStackTrace();
            action = -1;
        }
        return action;
    }

    /**
     * main() - run the craps game simulation
     * @param args #1 - the amount of money to start with
     */
    public static void main(String[] args) {
        int money = 1000;
        try {
            if (args.length > 0) {
                money = Integer.parseInt(args[0]);
                if (money <= 0) throw new Exception("Must be greater than zero.");
            }
            Craps c = new Craps(money);
            c.run();
        } catch (Exception e) {
            System.out.println("You must enter a valid number as an argument.");
        }
    }
}
