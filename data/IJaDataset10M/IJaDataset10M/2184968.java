package net.sf.eqemutils.wikiquest;

import java.util.*;
import java.util.regex.*;

/** Recognizes and collects the information encoded into a 'Quest Barter' line.
 */
public class BarterLine extends LineImpl implements ProfileLine {

    /** Initializes 'this' with the barter named 'nBarterName'.
   */
    public BarterLine(String nBarterName) {
        _InitWithBarterName(nBarterName);
    }

    protected void _InitWithBarterName(String nBarterName) {
        _InitDefault();
        _BarterName = nBarterName;
    }

    /** Returns the barter name of 'this'.
   */
    public String BarterName() {
        String Result = _BarterName;
        return Result;
    }

    /** Tries to read an instance of 'this' from 'ThisLine'.
   *  Returns the contents of 'ThisLine' if 'ThisLine' encodes an instance of this class.
   *  Otherwise returns 'null'. 
   */
    public static BarterLine Recognize(String ThisLine) {
        BarterLine Result;
        Matcher ThisLineMatcher;
        Result = null;
        if (Result == null) {
            ThisLineMatcher = _LinePattern_1.matcher(ThisLine);
            if (ThisLineMatcher.find()) Result = new BarterLine(ThisLineMatcher.group(1));
        }
        return Result;
    }

    /** Tries to read an instance of 'this' from 'ThisLine'.
   *  Returns the contents of 'ThisLine' if 'ThisLine' encodes an instance of this class. Sets the result location to 'nLocation'
   *    and the result line number to 'nLineNo'.
   *  Otherwise returns 'null'. 
   */
    public static BarterLine Recognize(String ThisLine, String nLocation, int nLineNo) {
        BarterLine Result;
        Result = Recognize(ThisLine);
        if (Result != null) {
            Result.SetLocation(nLocation);
            Result.SetLineNo(nLineNo);
        }
        return Result;
    }

    /** Adds the contents defined by 'this' to ThisDatabase', issuing errors only if it is impossible.
   *  Some Integrity and best practice checks will be done later.
   *  Returns the created chapter.
   */
    public long AddToChapter(WikiQuestDatabase ThisDatabase, long TopChapterId, long ParentChapterId, int Depth) throws Exception {
        assert ThisDatabase.IsOpened();
        long Result = ThisDatabase.AddBarter(_Location, _LineNo, ParentChapterId, _BarterName, Depth);
        ThisDatabase.MakeTopChapterIncludeSubChapter(_Location, _LineNo, TopChapterId, Result);
        return Result;
    }

    protected String _BarterName;

    /** Patterns recognizing an instance of this line class.
   */
    protected static Pattern _LinePattern_1 = Pattern.compile("^\\{\\{Quest Barter\\|([^|}]+)\\}\\}$");

    public String toString() {
        String Result = "Barter";
        Result = Result + "\n  barter name : '" + _BarterName + "'";
        return Result;
    }

    protected void _InitDefault() {
        _BarterName = null;
    }

    protected BarterLine() {
    }

    public static void main(String Arguments[]) {
        try {
            System.out.println("" + BarterLine.Recognize("{{Quest Barter|Glob Insect Eye}}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
