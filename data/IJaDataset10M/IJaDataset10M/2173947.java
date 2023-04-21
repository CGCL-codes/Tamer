package messages;

import java.lang.*;
import java.util.*;
import org.logi.crypto.*;
import utils.misc.*;
import utils.crypto.*;
import messages.tools.*;
import messages.*;

/**
 * @author 
 * @version 
 * Request from the Citizen to Withdraw the Money
 * Citizen -> ZoneServer_Citizen
 * 
 * IDENTIFIER, AMOUNT, RTT
 */
public class REQ_WIT implements Message {

    private String IDENTIFIER;

    private Float AMOUNT;

    private Long RTT;

    public parseMessage theParseMessage;

    /**
    * @return 
    * @exception 
    * @author 
    * @version
    * @roseuid 3B9A02FC0354
    */
    public REQ_WIT() {
        IDENTIFIER = null;
        AMOUNT = null;
        RTT = null;
        theParseMessage = new parseMessage();
    }

    public REQ_WIT(String I, Float A, Long RT) throws Exception {
        if (I == null || A == null || RT == null) throw (new Exception("Null value in Input I=" + I + " A=" + A + " RT=" + RT));
        IDENTIFIER = I;
        AMOUNT = A;
        RTT = RT;
        theParseMessage = new parseMessage();
    }

    public REQ_WIT(String Ip) throws Exception {
        theParseMessage = new parseMessage();
        if (Ip == null) throw (new Exception(" Null Value in Input"));
        String[] s = theParseMessage.parse(Ip);
        if (s.length != 3) throw (new Exception(Ip + " Contains " + s.length + " params while I need 3"));
        IDENTIFIER = s[0];
        AMOUNT = new Float(s[1]);
        RTT = new Long(s[2]);
    }

    /**
    * @return String
    * @exception 
    * @author 
    * @version 
    * @roseuid 3B9A142700F0
    */
    public String getIDENTIFIER() {
        return IDENTIFIER;
    }

    /**
    * @return Float
    * @exception 
    * @author 
    * @version 
    * @roseuid 3B9A1430008F
    */
    public Float getAMOUNT() {
        return AMOUNT;
    }

    /**
    * @return Long
    * @exception 
    * @author 
    * @version 
    * @roseuid 3B9A144E01BF
    */
    public Long getRTT() {
        return RTT;
    }

    /**
    * @return String
    * @exception 
    * @author 
    * @version 
    * @roseuid 3B9A17B4010C
    */
    public String getType() {
        return ("REQ_WIT");
    }

    public String toString() {
        if (IDENTIFIER == null || AMOUNT == null || RTT == null) return null;
        Vector D = new Vector();
        D.add(IDENTIFIER);
        D.add(AMOUNT);
        D.add(RTT);
        String f;
        try {
            f = theParseMessage.generate(D);
        } catch (Exception e) {
            return null;
        }
        return f;
    }

    /**
    * @param inputString
    * @return boolean
    * @exception Exception
    * @author 
    * @version 
    * @roseuid 3B9A21EA0100
    */
    public boolean SetValues(String Ip) throws Exception {
        if (Ip == null) return false;
        String[] s = theParseMessage.parse(Ip);
        if (s.length != 3) return false;
        IDENTIFIER = s[0];
        AMOUNT = new Float(s[1]);
        RTT = new Long(s[2]);
        return true;
    }

    public static void main(String[] args) throws Exception {
        REQ_WIT Jada = new REQ_WIT();
        System.out.println("The result of set values is " + Jada.SetValues(args[0]));
        if (args.length < 1) {
            System.out.println(" Arguments not enough");
            System.exit(1);
        }
        Long a = new Long(System.currentTimeMillis());
        System.out.println("The Values are \n \nIDENTIFIER=" + Jada.getIDENTIFIER() + "\n AMOUNT=" + Jada.getAMOUNT() + "\n RTT=" + Jada.getRTT());
        Long b = new Long(System.currentTimeMillis());
        String Ki = Jada.toString();
        System.out.println("Got " + Ki);
        REQ_WIT Jada1 = new REQ_WIT(Ki);
        System.out.println("The Values are \n \nIDENTIFIER=" + Jada1.getIDENTIFIER() + "\n AMOUNT=" + Jada1.getAMOUNT() + "\n RTT=" + Jada1.getRTT());
        Long c = new Long(System.currentTimeMillis());
        Float f = new Float(1231231098868997698769823.1231231);
        REQ_WIT Jada21 = new REQ_WIT("Helo dear", f, a);
        System.out.println("The Values are \n \nIDENTIFIER=" + Jada21.getIDENTIFIER() + "\n AMOUNT=" + Jada21.getAMOUNT() + "\n RTT=" + Jada21.getRTT());
        System.out.println(Jada21 + "\n" + Jada1 + "\n" + Jada);
    }
}
