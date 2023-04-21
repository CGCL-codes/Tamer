package hailmary.dicebags;

import hailmary.util.Base64;
import hailmary.util.CommutativeRSAKey;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * A secure die rolling protocol, which implements an adaption of
 * the mental coin toss algorithm by Rivest, Shamir and Adleman.
 * There is known to be a small information leak in this algorithm,
 * and the encryption keys used are only 64-bit, but for the
 * purpose it is being used for it is secure enough.</p>
 *
 * The two parties communicate using <code>String</code> objects in
 * private messages, using <code>Connection.sendData()</code>.
 * The messages sent and handled by this class are (note that Alice
 * denotes the party initiating the die roll and Bob denotes the
 * responding party):
 * <dl>
 * <dt><code>&lt;dieindex&gt; &lt;sides&gt; &lt;chits&gt;</code>
 * <dd>Alice announces a die roll with specified index (in the roll's
 *     dice array) and number of sides, and sends an appropriate number
 *     of chits encrypted with her key.
 * <dt><code>pick &lt;chit&gt;</code>
 * <dd>Bob picks a chit and returns it encrypted with his key.
 * <dt><code>decr &lt;chit&gt;</code>
 * <dd>Alice returns the chit after decrypting it with her key.
 * <dt><code>peek &lt;chit&gt;</code>
 * <dd>Bob returns the chit after decrypting it with his key.
 *     The chit is now in plaintext.
 * <dt><code>akey &lt;key&gt;</code>
 * <dd>Alice sends her key, as returned by
 *     <code>CommutativeRSAKey.toString()</code>.
 * <dt><code>bchk &lt;result&gt;</code>
 * <dd>Bob has verified the validity of the roll and sends the
 *     result he found to Alice.
 * <dt><code>achk</code>
 * <dd>Alice has verified the validity of the roll.
 * <dt><code>error</code>
 * <dd>Generic error message, which terminates the die roll.
 * </dl>
 * All chits, and only chits, are in Base64 representation.
 * All other numbers are in decimal representation.
 * Note that all messages are prefixed with:
 * <blockquote>
 * <code>die &lt;index&gt;</code>
 * </blockquote>
 * where <code>index</code> is the index of the roll. However, this
 * is stripped by <code>SecureDiceBag</code> before dispatching it.
 * @see SecureDiceBag
 * @see hailmary.network.Connection
 * @see hailmary.util.CommutativeRSAKey
 * @author Corvass
 */
public class SecureDie {

    private SecureDiceBag diceBag;

    private int index;

    private int diceIndex;

    private boolean isAlice;

    private int sides;

    private BigInteger[] chits;

    private BigInteger pickChit;

    private int pickResult;

    private CommutativeRSAKey rsaKey;

    private boolean isStarted = false;

    private String startMsg;

    /**
   * Constructs a secure die which will initiate a dice roll.
   * @param diceBag the dice bag containing this die
   * @param index the index of the roll this die belongs to
   * @param diceIndex the index in the dice array of the roll
   * @param sides the number of sides of this die
   */
    public SecureDie(SecureDiceBag diceBag, int index, int diceIndex, int sides) {
        this.diceBag = diceBag;
        this.index = index;
        this.diceIndex = diceIndex;
        this.sides = sides;
        this.rsaKey = new CommutativeRSAKey(diceBag.getRandom());
        isAlice = true;
    }

    /**
   * Constructs a secure die as a response to a remote message.
   * @param diceBag the dice bag containing this die
   * @param index the index of the roll this die belongs to
   * @param msg the remote message
   */
    public SecureDie(SecureDiceBag diceBag, int index, String msg) {
        this.diceBag = diceBag;
        this.index = index;
        this.rsaKey = new CommutativeRSAKey(diceBag.getRandom());
        isAlice = false;
        startMsg = msg;
    }

    /**
   * Starts the die roll.
   */
    public void startRoll() {
        if (!isStarted) {
            isStarted = true;
            if (isAlice) aliceStarts(); else bobPicks(startMsg);
        }
    }

    /**
   * Returns the index of the roll this die belongs to.
   * @return the index of the roll
   */
    public int getIndex() {
        return index;
    }

    /**
   * Returns the index in the dice array of the roll.
   * @return the index in the dice array
   */
    public int getDiceIndex() {
        return diceIndex;
    }

    /**
   * Returns <code>true</code> if we are Alice.
   * Alice always initiates the dice roll.
   * @return <code>true</code> if we are Alice
   */
    public boolean isAlice() {
        return isAlice;
    }

    /**
   * Receives an incoming message and dispatches it to the
   * correct method.
   * @param msg the incoming message
   */
    public void receive(String msg) {
        try {
            String s = msg.substring(0, 4);
            if (s.equalsIgnoreCase("pick")) aliceDecrypts(msg); else if (s.equalsIgnoreCase("decr")) bobPeeks(msg); else if (s.equalsIgnoreCase("peek")) aliceSendsKey(msg); else if (s.equalsIgnoreCase("akey")) bobChecks(msg); else if (s.equalsIgnoreCase("bchk")) aliceChecks(msg); else if (s.equalsIgnoreCase("achk")) bobDone(); else throw new IllegalArgumentException(s);
        } catch (SecurityException e2) {
            diceBag.failedRoll(this, e2.getMessage());
            diceBag.send("die " + (isAlice ? index : -index) + " error");
        } catch (Exception e1) {
            diceBag.failedRoll(this);
            diceBag.send("die " + (isAlice ? index : -index) + " error");
        }
    }

    /**
   * Alice starts the roll. Alice generates one random number for
   * each side of the dice, where the number modulo the number of
   * sides designates the side. Then, she shuffles them and encrypts
   * them with her key, and sends them to Bob.
   */
    protected void aliceStarts() {
        chits = new BigInteger[sides];
        BigInteger bigSides = BigInteger.valueOf(sides);
        BigInteger offset = BigInteger.ZERO;
        for (int i = 0; i < sides; i++) {
            chits[i] = new BigInteger(32, diceBag.getRandom());
            chits[i] = chits[i].subtract(chits[i].mod(bigSides)).add(offset);
            offset = offset.add(BigInteger.ONE);
        }
        String s = diceIndex + " " + sides;
        Collections.shuffle(Arrays.asList(chits), diceBag.getRandom());
        for (int i = 0; i < sides; i++) s += " " + Base64.toBase64(rsaKey.encrypt(chits[i]));
        diceBag.send("die " + index + " " + s);
    }

    /**
   * Bob picks one of the chits, and sends it back after encrypting it.
   * @param msg the message sent by Alice in <code>aliceStarts</code>
   */
    protected void bobPicks(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        diceIndex = Integer.parseInt(st.nextToken());
        sides = Integer.parseInt(st.nextToken());
        int pick = diceBag.getRandom().nextInt(sides);
        chits = new BigInteger[sides];
        for (int i = 0; i < sides; i++) chits[i] = Base64.fromBase64(st.nextToken());
        diceBag.send("die " + (-index) + " pick " + Base64.toBase64(rsaKey.encrypt(chits[pick])));
    }

    /**
   * Alice decrypts the chit Bob sent with her key, and returns
   * it to Bob. She can't read the chit, because Bob encrypted it
   * with his key as well.
   * @param msg the message sent by Bob in <code>bobPicks</code>
   */
    protected void aliceDecrypts(String msg) {
        int i = msg.indexOf(' ');
        BigInteger chit = Base64.fromBase64(msg.substring(i + 1));
        diceBag.send("die " + index + " decr " + Base64.toBase64(rsaKey.decrypt(chit)));
    }

    /**
   * Bob decrypts the returned chit. He now knows the result of the
   * roll. He returns the unencrypted chit so Alice can verify the
   * genuinity of the chit.
   * @param msg the message sent by Alice in <code>aliceDecrypts</code>
   */
    protected void bobPeeks(String msg) {
        int i = msg.indexOf(' ');
        pickChit = rsaKey.decrypt(Base64.fromBase64(msg.substring(i + 1)));
        pickResult = pickChit.mod(BigInteger.valueOf(sides)).intValue();
        diceBag.send("die " + (-index) + " peek " + Base64.toBase64(pickChit));
    }

    /**
   * Alice sends Bob her key for validation. She also verifies the chit
   * Bob picked was one she sent out.
   * @param msg the message sent by Bob in <code>bobPeeks</code>
   */
    protected void aliceSendsKey(String msg) {
        int i = msg.indexOf(' ');
        pickChit = Base64.fromBase64(msg.substring(i + 1));
        pickResult = pickChit.mod(BigInteger.valueOf(sides)).intValue();
        boolean found = false;
        for (int j = 0; j < sides; j++) if (pickChit.equals(chits[j])) {
            found = true;
            break;
        }
        if (!found) throw new SecurityException("Bob sent non-existing chit");
        diceBag.send("die " + index + " akey " + rsaKey);
    }

    /**
   * Bob checks that Alice didn't send biased chits, and that the
   * chit she sent was legit.
   * @param msg the message sent by Alice in <code>aliceSendsKey</code>
   */
    protected void bobChecks(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        st.nextToken();
        CommutativeRSAKey aliceKey;
        try {
            aliceKey = new CommutativeRSAKey(Base64.fromBase64(st.nextToken()), Base64.fromBase64(st.nextToken()));
        } catch (IllegalArgumentException e) {
            throw new SecurityException("Alice used an incorrect key");
        }
        boolean found = false;
        for (int i = 0; i < sides; i++) {
            chits[i] = aliceKey.decrypt(chits[i]);
            if (pickChit.equals(chits[i])) found = true;
        }
        if (!found) throw new SecurityException("Alice sent non-existing chit");
        BigInteger bigSides = BigInteger.valueOf(sides);
        for (int i = 0; i < sides; i++) chits[i] = chits[i].mod(bigSides);
        Arrays.sort(chits);
        BigInteger bigI = BigInteger.ZERO;
        for (int i = 0; i < sides; i++) {
            if (!chits[i].equals(bigI)) throw new SecurityException("Alice weighted the die");
            bigI = bigI.add(BigInteger.ONE);
        }
        diceBag.send("die " + (-index) + " bchk " + pickResult);
    }

    /**
   * Alice checks the number Bob found corresponds to the number
   * she found.
   * @param msg the message sent by Bob in <code>bobChecks</code>
   */
    protected void aliceChecks(String msg) {
        int i = msg.indexOf(' ');
        if (Integer.parseInt(msg.substring(i + 1)) != pickResult) throw new SecurityException("Bob sent incorrect die result");
        diceBag.send("die " + index + " achk");
        diceBag.rollDone(this, pickResult + 1);
    }

    /**
   * Bob notifies the dice bag of the result of the roll.
   */
    protected void bobDone() {
        diceBag.rollDone(this, pickResult + 1);
    }
}
