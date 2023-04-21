package net.sf.eqemutils.wikiquest;

import java.util.*;
import java.util.regex.*;
import net.sf.eqemutils.utils.*;

/** Recognizes and collects the information encoded into a 'Quest Coin Reward' line.
 */
public class CoinRewardLine extends LineImpl implements ProfileLine, QuestLine {

    /** Initializes 'this' with the XP delta 'nAmount'.
   */
    public CoinRewardLine(boolean Ignored) {
        _InitWithNoCoins();
    }

    protected void _InitWithNoCoins() {
        _InitDefault();
    }

    /** Returns the PP reward of 'this'.
   */
    public int AmountPP() {
        int Result = _AmountPP;
        return Result;
    }

    /** Returns the GP reward of 'this'.
   */
    public int AmountGP() {
        int Result = _AmountGP;
        return Result;
    }

    /** Returns the SP reward of 'this'.
   */
    public int AmountSP() {
        int Result = _AmountSP;
        return Result;
    }

    /** Returns the CP reward of 'this'.
   */
    public int AmountCP() {
        int Result = _AmountCP;
        return Result;
    }

    /** Tries to read an instance of 'this' from 'ThisLine'.
   *  Returns the contents of 'ThisLine' if 'ThisLine' encodes an instance of this class.
   *  Otherwise returns 'null'. 
   */
    public static CoinRewardLine Recognize(String ThisLine) {
        CoinRewardLine Result;
        Matcher ThisLineMatcher;
        Result = null;
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_gp_sp_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_gp_SP_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_gp_SP_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_GP_sp_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_GP_sp_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_GP_SP_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_pp_GP_SP_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(4));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_gp_sp_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_gp_sp_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_gp_SP_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_gp_SP_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(4));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_GP_sp_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_GP_sp_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(4));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_GP_SP_cp.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(4));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        if (Result == null) {
            ThisLineMatcher = _LinePattern_PP_GP_SP_CP.matcher(ThisLine);
            if (ThisLineMatcher.find()) {
                Result = new CoinRewardLine(false);
                Result._AmountPP = ParseUtils.ToNatural(ThisLineMatcher.group(2));
                Result._AmountGP = ParseUtils.ToNatural(ThisLineMatcher.group(3));
                Result._AmountSP = ParseUtils.ToNatural(ThisLineMatcher.group(4));
                Result._AmountCP = ParseUtils.ToNatural(ThisLineMatcher.group(5));
                if (ThisLineMatcher.group(1) != null) Result._IsAlternative = true;
            }
        }
        return Result;
    }

    /** Tries to read an instance of 'this' from 'ThisLine'.
   *  Returns the contents of 'ThisLine' if 'ThisLine' encodes an instance of this class. Sets the result location to 'nLocation'
   *    and the result line number to 'nLineNo'.
   *  Otherwise returns 'null'. 
   */
    public static CoinRewardLine Recognize(String ThisLine, String nLocation, int nLineNo) {
        CoinRewardLine Result;
        Result = Recognize(ThisLine);
        if (Result != null) {
            Result.SetLocation(nLocation);
            Result.SetLineNo(nLineNo);
        }
        return Result;
    }

    /** Adds the contents defined by 'this' to ThisDatabase', issuing errors only if it is impossible.
   *  Some Integrity and best practice checks will be done later.
   */
    public void AddToEvent(WikiQuestDatabase ThisDatabase, long EventId) throws Exception {
        assert ThisDatabase.IsOpened();
        ThisDatabase.AddCoinRewardToEvent(_Location, _LineNo, EventId, _AmountPP, _AmountGP, _AmountSP, _AmountCP);
    }

    protected int _AmountPP;

    protected int _AmountGP;

    protected int _AmountSP;

    protected int _AmountCP;

    /** Patterns recognizing an instance of this line class.
   */
    protected static Pattern _LinePattern_pp_gp_sp_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_pp_gp_SP_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)sp\\}\\}$");

    protected static Pattern _LinePattern_pp_gp_SP_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)sp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_pp_GP_sp_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)gp\\}\\}$");

    protected static Pattern _LinePattern_pp_GP_sp_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)gp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_pp_GP_SP_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)gp +([0-9]+)sp\\}\\}$");

    protected static Pattern _LinePattern_pp_GP_SP_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)gp +([0-9]+)sp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_PP_gp_sp_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp\\}\\}$");

    protected static Pattern _LinePattern_PP_gp_sp_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_PP_gp_SP_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)sp\\}\\}$");

    protected static Pattern _LinePattern_PP_gp_SP_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)sp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_PP_GP_sp_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)gp\\}\\}$");

    protected static Pattern _LinePattern_PP_GP_sp_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)gp +([0-9]+)cp\\}\\}$");

    protected static Pattern _LinePattern_PP_GP_SP_cp = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)gp +([0-9]+)sp\\}\\}$");

    protected static Pattern _LinePattern_PP_GP_SP_CP = Pattern.compile("^\\*\\* *(or)? *\\{\\{Quest Coin Reward\\|([0-9]+)pp +([0-9]+)gp +([0-9]+)sp +([0-9]+)cp\\}\\}$");

    public String toString() {
        String Result = "Coin reward";
        if (_AmountPP != 0) {
            Result = Result + "\n  pp : '" + _AmountPP + "'";
        }
        if (_AmountGP != 0) {
            if (!Result.equals("\n  ")) Result = Result + "  ";
            Result = Result + "gp : '" + _AmountGP + "'";
        }
        if (_AmountSP != 0) {
            if (!Result.equals("\n  ")) Result = Result + "  ";
            Result = Result + "sp : '" + _AmountSP + "'";
        }
        if (_AmountCP != 0) {
            if (!Result.equals("\n  ")) Result = Result + "  ";
            Result = Result + "cp : '" + _AmountCP + "'";
        }
        if (Result.equals("")) Result = "\n  0";
        return Result;
    }

    protected void _InitDefault() {
        _AmountPP = 0;
        _AmountGP = 0;
        _AmountSP = 0;
        _AmountCP = 0;
    }

    public int Kind() {
        int Result = Kind_Change;
        return Result;
    }

    protected CoinRewardLine() {
    }

    public static void main(String Arguments[]) {
        try {
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|1cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|2sp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|3sp 2cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|4gp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|5gp 4cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|6gp 5cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|7gp 6sp 5cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|8pp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|9pp 8cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|10pp 9sp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|11pp 10sp 9cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|12pp 11gp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|13pp 12gp 11cp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|14pp 13gp 12sp}}"));
            System.out.println("" + CoinRewardLine.Recognize("** {{Quest Coin Reward|15pp 14gp 13sp 12cp}}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
