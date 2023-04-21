package hailmary.dicebags;

import hailmary.network.Connection;
import hailmary.network.DataListener;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * A dice bag which uses a encryption to securely roll
 * dice with no trusted third party. The encryption
 * used is fairly weak, but should be sufficient.</p>
 *
 * The two parties communicate using <code>String</code> objects in
 * private messages, using <code>Connection.sendData()</code>.
 * The messages sent and handled by this class are:
 * <dl>
 * <dt><code>roll &lt;index&gt;
       &lt;roll&gt;[<b>^R</b>&lt;rerollstate&gt;]</code>
 * <dd>Announce a new roll with specified index, and if necessary (for
 *     the pro and rr commands) supplies the local re-roll state, seperated
 *     from the dice roll by a <b>^R</b> character (the <code>REROLL</code>
 *     character).
 * <dt><code>pro &lt;index&gt; &lt;roll&gt;</code>
 * <dd>Announce a new pro roll with specified index. Pro rolls are
 *     not displayed on the user's screen, because there already has
 *     been an appropriate message ("nick rolls pro").
 * <dt><code>die &lt;index&gt; &lt;securedie&gt;</code>
 * <dd>Messages intended for <code>SecureDie</code>. The prefix and index
 *     are stripped and the rest of the message is dispatched to the
 *     appropriate <code>SecureDie</code> object
 * </dl>
 * The parties involved in dice rolling both know the result, and
 * the result is also sent to the rest of the channel via
 * <code>DiceMessage</code> objects.
 * @see DiceMessage
 * @see RerollState
 * @see SecureDie
 * @see hailmary.network.Connection
 * @author Corvass
 */
public class SecureDiceBag implements DiceBag, DataListener {

    /** Reroll state identifier. */
    protected static final char REROLL = ((char) 0x12);

    private Connection connection;

    private String nick;

    private Random random = new Random();

    private RerollState reroll = new RerollState();

    private Hashtable localIndexToDie = new Hashtable();

    private Hashtable remoteIndexToDie = new Hashtable();

    private Hashtable localIndexToRoll = new Hashtable();

    private Hashtable remoteIndexToRoll = new Hashtable();

    private int nextLocalIndex = 1;

    /**
   * Constructs a dice bag for the given connection.
   * @param connection the connection to use
   */
    public SecureDiceBag(Connection connection, String nick) {
        this.connection = connection;
        this.nick = nick;
        connection.registerClass(DiceMessage.class, "dice");
        connection.addDataListener(this, String.class, true);
    }

    /**
   * Returns this dice bag's random generator.
   * @return a random generator
   */
    public Random getRandom() {
        return random;
    }

    /**
   * Handles a succesful die roll. Removes the die from
   * its hashtable, and either instantiates a new die, or
   * if all dice for a roll have been rolled, formats
   * the result of the dice roll and cleans up the dice roll.
   * @param die the die which is done
   * @param result the die's result
   */
    public void rollDone(SecureDie die, int result) {
        DiceRoll roll;
        Integer index = new Integer(die.getIndex());
        if (die.isAlice()) {
            localIndexToDie.remove(index);
            roll = (DiceRoll) localIndexToRoll.get(index);
        } else {
            remoteIndexToDie.remove(index);
            roll = (DiceRoll) remoteIndexToRoll.get(index);
        }
        int diceIndex = die.getDiceIndex();
        roll.dice[diceIndex] = result;
        diceIndex++;
        if (diceIndex == roll.getDiceCount()) {
            String s = roll.roll();
            if (die.isAlice()) {
                localIndexToRoll.remove(index);
                if (s != null) {
                    connection.doLocalAction(false, connection.getNick(), "rolled " + s);
                    connection.sendData(null, new DiceMessage(false, s));
                    if (s.indexOf("Pro roll successful") > -1) {
                        DiceRoll proRoll = reroll.doProReroll();
                        send("pro " + nextLocalIndex + " " + reroll.getLastRoll());
                        Integer proIndex = new Integer(nextLocalIndex);
                        localIndexToRoll.put(proIndex, proRoll);
                        SecureDie proDie = new SecureDie(this, nextLocalIndex, 0, proRoll.getDiceType(0));
                        localIndexToDie.put(proIndex, proDie);
                        proDie.startRoll();
                        nextLocalIndex++;
                    }
                }
            } else {
                remoteIndexToRoll.remove(index);
                if (s != null) connection.doLocalAction(false, nick, "rolled " + s);
            }
        } else {
            if (die.isAlice()) {
                SecureDie newDie = new SecureDie(this, index.intValue(), diceIndex, roll.getDiceType(diceIndex));
                localIndexToDie.put(index, newDie);
                newDie.startRoll();
            }
        }
    }

    /**
   * Handles a dice roll which failed due to protcol failure,
   * for instance tampering with the encryption. Terminates
   * the entire dice roll.
   * @param die the failed die
   * @param msg the failure message
   */
    public void failedRoll(SecureDie die, String msg) {
        failedRoll(die);
        if (die.isAlice()) connection.sendAction(null, "-- protocol failure: " + msg);
    }

    /**
   * Handles a dice roll which failed due to a non-protocol error.
   * Terminates the entire dice roll.
   * @param die the failed die
   */
    public void failedRoll(SecureDie die) {
        DiceRoll roll;
        Integer index = new Integer(die.getIndex());
        if (die.isAlice()) {
            localIndexToDie.remove(index);
            localIndexToRoll.remove(index);
        } else {
            remoteIndexToDie.remove(index);
            remoteIndexToRoll.remove(index);
        }
        if (die.isAlice()) connection.sendAction(null, "-- dice roll failed");
    }

    /**
   * Sends a dice roll message to the other party.
   * @param msg the message to send
   */
    public void send(String msg) {
        connection.sendData(nick, msg);
    }

    private static boolean startsWith(String s1, String s2) {
        return (s1.length() >= s2.length()) ? s1.substring(0, s2.length()).equalsIgnoreCase(s2) : false;
    }

    public void roll(String dice) {
        connection.doLocalAction(false, connection.getNick(), "rolls " + dice);
        connection.sendData(null, new DiceMessage(true, dice));
        DiceRoll diceRoll;
        if (startsWith(dice, "rr") || startsWith(dice, "reroll")) {
            dice += REROLL + reroll.toString();
            diceRoll = reroll.doReroll();
        } else if (startsWith(dice, "pro")) {
            dice += REROLL + reroll.toString();
            diceRoll = reroll.doPro();
        } else {
            diceRoll = new DiceRoll(dice);
            reroll.setLastRoll(dice);
        }
        send("roll " + nextLocalIndex + " " + dice);
        Integer index = new Integer(nextLocalIndex);
        localIndexToRoll.put(index, diceRoll);
        SecureDie die = new SecureDie(this, nextLocalIndex, 0, diceRoll.getDiceType(0));
        localIndexToDie.put(index, die);
        die.startRoll();
        nextLocalIndex++;
    }

    public void onData(boolean priv, String nick, Object data) {
        if (nick.equalsIgnoreCase(this.nick)) {
            try {
                String msg = (String) data;
                StringTokenizer st = new StringTokenizer(msg);
                String s = st.nextToken();
                if (s.equalsIgnoreCase("roll")) {
                    Integer n = new Integer(st.nextToken());
                    String dice = msg.substring(msg.indexOf(' ', msg.indexOf(' ') + 1) + 1);
                    RerollState reroll = null;
                    int i = dice.indexOf(REROLL);
                    if (i > -1) {
                        reroll = new RerollState(dice.substring(i + 1));
                        dice = dice.substring(0, i);
                    }
                    connection.doLocalAction(false, nick, "rolls " + dice);
                    DiceRoll diceRoll;
                    if (startsWith(dice, "rr") || startsWith(dice, "reroll")) diceRoll = reroll.doReroll(); else if (startsWith(dice, "pro")) diceRoll = reroll.doPro(); else diceRoll = new DiceRoll(dice);
                    remoteIndexToRoll.put(n, diceRoll);
                } else if (s.equalsIgnoreCase("pro")) {
                    Integer n = new Integer(st.nextToken());
                    String dice = msg.substring(msg.indexOf(' ', msg.indexOf(' ') + 1) + 1);
                    remoteIndexToRoll.put(n, new DiceRoll(dice));
                } else if (s.equalsIgnoreCase("die")) {
                    int n = Integer.parseInt(st.nextToken());
                    SecureDie die;
                    if (n < 0) die = (SecureDie) localIndexToDie.get(new Integer(-n)); else die = (SecureDie) remoteIndexToDie.get(new Integer(n));
                    if (die != null) die.receive(msg.substring(msg.indexOf(' ', msg.indexOf(' ') + 1) + 1)); else if (n > 0) {
                        die = new SecureDie(this, n, msg.substring(msg.indexOf(' ', msg.indexOf(' ') + 1) + 1));
                        remoteIndexToDie.put(new Integer(n), die);
                        die.startRoll();
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
