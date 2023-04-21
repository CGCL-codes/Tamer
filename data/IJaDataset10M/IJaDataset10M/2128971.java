package net.sf.eqemutils.wikiquest;

import java.sql.*;
import java.util.*;
import net.sf.eqemutils.utils.*;
import net.sf.eqemutils.wiki.*;

/** Engine reading the entries from a profile page in a Wiki and loading them into a WikiQuest database.
 */
public class ProfileReader {

    /** Name of the category for Profile pages.
   */
    public static final String ProfileCategory = "Profile";

    /** Reads all profiles of 'ThisDatabase' from 'ThisWiki'.
   */
    public static void ReadAllFromWiki(WikiServer ThisWiki, WikiQuestDatabase ThisDatabase) throws Exception {
        assert ThisDatabase.IsOpened();
        Vector<String> Errors;
        Set<Long> TheseProfiles;
        TheseProfiles = SqlUtils.RequestReturning_Set_Long(ThisDatabase.SqlConnection(), "SELECT " + WikiQuestDatabase.Column_Chapter_Id + " FROM " + WikiQuestDatabase.Table_Chapter + " WHERE " + WikiQuestDatabase.Column_Chapter_Kind + " ='" + WikiQuestDatabase.Kind_Chapter_Profile + "'");
        Errors = new Vector<String>();
        for (long ThisProfile : TheseProfiles) try {
            ReadFromWiki(ThisWiki, ThisProfile, ThisDatabase);
        } catch (Exception e) {
            Errors.addElement("" + e.getMessage());
        }
        if (!Errors.isEmpty()) throw new Exception(ParseUtils.RecomposeFromLineS(Errors));
    }

    /** Reads all entries of 'ThisProfile' page from 'ThisWiki' into 'ThisDatabase'.
   */
    public static void ReadFromWiki(WikiServer ThisWiki, long CurrentProfile, WikiQuestDatabase ThisDatabase) throws Exception {
        assert ThisDatabase.IsOpened();
        Vector<String> Errors;
        String CurrentProfilePageName;
        Vector<ProfileLine> ProfileLines;
        ProfileLine ThisProfileLine;
        BarterClaimLine ThisBarterClaimLine;
        GiveItemLine ThisGiveItemLine;
        HailNpcLine ThisHailNpcLine;
        ItemRewardLine ThisItemRewardLine;
        ProfileLine PreviousLine;
        long CurrentNpc, CurrentEvent, CurrentBarter, CurrentAlternative, CurrentChange = -1;
        int j, jEnd;
        CurrentProfilePageName = SqlUtils.RequestReturning_String(ThisDatabase.SqlConnection(), "SELECT " + WikiQuestDatabase.Column_Chapter_PageName + " FROM " + WikiQuestDatabase.Table_Chapter + " WHERE " + WikiQuestDatabase.Column_Chapter_Id + "='" + SqlUtils.ToSqlString(CurrentProfile) + "'");
        ProfileLines = ReadProfileLines(ThisWiki, CurrentProfilePageName);
        ThisProfileLine = ProfileLines.elementAt(1 - 1);
        if (!(ThisProfileLine instanceof HailNpcLine)) ParseUtils.RaiseException(ThisProfileLine, "the first line in the profile is not the NPC");
        ThisHailNpcLine = (HailNpcLine) ThisProfileLine;
        CurrentNpc = ThisHailNpcLine.AddNpc(ThisDatabase);
        if (ThisHailNpcLine.ResponseIsDefined()) CurrentEvent = ThisHailNpcLine.AddHailToNpcInChapter(ThisDatabase, CurrentNpc, CurrentProfile); else CurrentEvent = -1;
        Errors = new Vector<String>();
        PreviousLine = ThisHailNpcLine;
        CurrentBarter = -1;
        CurrentAlternative = -1;
        for (j = 2, jEnd = ProfileLines.size(); j <= jEnd; j++) {
            try {
                ThisProfileLine = ProfileLines.elementAt(j - 1);
                if (ThisProfileLine.Kind() == Line.Kind_Trigger && ThisProfileLine.IsAddition() && PreviousLine.Kind() != Line.Kind_Trigger) ParseUtils.RaiseException(ThisProfileLine, "additional trigger lines must follow a previous trigger line");
                if (ThisProfileLine.Kind() == Line.Kind_Change && ThisProfileLine.IsAlternative() && PreviousLine.Kind() != Line.Kind_Change) ParseUtils.RaiseException(ThisProfileLine, "alternative change lines must follow a previous change line");
                if (ThisProfileLine.Kind() == Line.Kind_Change && CurrentEvent == -1) ParseUtils.RaiseException(ThisProfileLine, "a change must occur inside an event");
                if (ThisProfileLine instanceof KeywordLine) CurrentBarter = -1;
                if (ThisProfileLine.Kind() == Line.Kind_Trigger && !ThisProfileLine.IsAddition()) {
                    if (CurrentBarter == -1) CurrentEvent = ThisDatabase.AddEventToNpcInChapter(ThisProfileLine.Location(), ThisProfileLine.LineNo(), CurrentNpc, CurrentProfile); else CurrentEvent = ThisDatabase.AddEventToNpcInChapter(ThisProfileLine.Location(), ThisProfileLine.LineNo(), CurrentNpc, CurrentBarter);
                }
                if (ThisProfileLine instanceof BarterClaimLine) {
                    ThisBarterClaimLine = (BarterClaimLine) ThisProfileLine;
                    if (CurrentBarter == -1) ParseUtils.RaiseException(ThisProfileLine, "a barter claim must occur inside a barter definition");
                    ThisBarterClaimLine.AddToBarterStatusForNpc(ThisDatabase, CurrentNpc, CurrentBarter);
                    CurrentEvent = ThisBarterClaimLine.AddClaimToNpc(ThisDatabase, CurrentNpc, CurrentBarter);
                } else if (ThisProfileLine instanceof BarterLine) {
                    CurrentBarter = ((BarterLine) ThisProfileLine).AddToChapter(ThisDatabase, CurrentProfile, CurrentProfile, 1);
                    CurrentEvent = -1;
                } else if (ThisProfileLine instanceof BarterMessagesLine) {
                    if (CurrentBarter == -1 || CurrentEvent != -1) ParseUtils.RaiseException(ThisProfileLine, "the barter messages must follow a barter definition");
                    ((BarterMessagesLine) ThisProfileLine).AddToBarterStatusForNpc(ThisDatabase, CurrentNpc, CurrentBarter);
                } else if (ThisProfileLine instanceof BarterValueLine) {
                    if (CurrentBarter == -1 || CurrentEvent == -1) ParseUtils.RaiseException(ThisProfileLine, "a barter value must occur inside an event and inside a barter definition");
                    ((BarterValueLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent, CurrentBarter);
                } else if (ThisProfileLine instanceof CoinRewardLine) ((CoinRewardLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof CompareVariableLine) (((CompareVariableLine) ThisProfileLine)).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof EmoteLine) ((EmoteLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof FactionChangeLine) ((FactionChangeLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof GiveItemLine) (((GiveItemLine) ThisProfileLine)).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof HailNpcLine) ParseUtils.RaiseException(ThisProfileLine, "only the first line of a profile can be an NPC"); else if (ThisProfileLine instanceof ItemRewardLine) CurrentChange = ((ItemRewardLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof KeywordLine) ((KeywordLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentNpc, CurrentEvent); else if (ThisProfileLine instanceof SayLine) ((SayLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof SetVariableLine) ((SetVariableLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else if (ThisProfileLine instanceof XPChangeLine) ((XPChangeLine) ThisProfileLine).AddToEvent(ThisDatabase, CurrentEvent); else throw new Exception("unhandled profile line :\n" + ThisProfileLine);
                if (ThisProfileLine.IsAlternative()) {
                    ThisDatabase.MakeAlternative(ThisProfileLine.Location(), ThisProfileLine.LineNo(), CurrentAlternative, CurrentAlternative, 1);
                    ThisDatabase.MakeAlternative(ThisProfileLine.Location(), ThisProfileLine.LineNo(), CurrentChange, CurrentAlternative, 1);
                } else CurrentAlternative = CurrentChange;
                PreviousLine = ThisProfileLine;
            } catch (Exception e) {
                Errors.addElement("" + e.getMessage());
            }
        }
        if (!Errors.isEmpty()) throw new Exception(ParseUtils.RecomposeFromLineS(Errors));
    }

    /** Reads the contents of 'ThisProfile' page from 'ThisWiki' and returns the non-filler and non-category recognized lines of the page.
   *  Ensures that 'ThisProfile' is in the 'Profile' category. 
   */
    protected static Vector<ProfileLine> ReadProfileLines(WikiServer ThisWiki, String ThisProfile) throws Exception {
        Vector<ProfileLine> Result;
        Vector<String> Errors;
        String ThisSource;
        Vector<String> TheseLines;
        String ThisLine;
        ProfileLine ThisProfileLine;
        boolean IsInProfileCategory;
        int j, jEnd;
        ThisSource = ThisWiki.PageSource(ThisProfile);
        TheseLines = ParseUtils.SplitIntoLines(ThisSource);
        Result = new Vector<ProfileLine>(TheseLines.size());
        Errors = new Vector<String>();
        IsInProfileCategory = false;
        jEnd = TheseLines.size();
        for (j = 1; j <= jEnd; j++) {
            try {
                ThisLine = TheseLines.elementAt(j - 1);
                ThisLine = ThisLine.trim();
                if (FillerLine.Recognize(ThisLine) != null) continue;
                ThisProfileLine = CategoryLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    IsInProfileCategory = IsInProfileCategory || ((CategoryLine) ThisProfileLine).CategoryName().equals(ProfileCategory);
                    continue;
                }
                ThisProfileLine = FillerLine.Recognize(ThisLine);
                if (ThisProfileLine != null) continue;
                ThisProfileLine = BarterClaimLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = BarterLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = BarterMessagesLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = BarterValueLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = CoinRewardLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = CompareVariableLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = EmoteLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = FactionChangeLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = GiveItemLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = HailNpcLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = ItemRewardLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = KeywordLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = SayLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = SetVariableLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                ThisProfileLine = XPChangeLine.Recognize(ThisLine, ThisProfile, j);
                if (ThisProfileLine != null) {
                    Result.addElement(ThisProfileLine);
                    continue;
                }
                if (ThisProfileLine == null) ParseUtils.RaiseException(ThisProfile, j, "unrecognized profile line\n  " + ThisLine);
            } catch (Exception e) {
                Errors.addElement("" + e.getMessage());
            }
        }
        if (!Errors.isEmpty()) throw new Exception(ParseUtils.RecomposeFromLineS(Errors));
        return Result;
    }

    protected ProfileReader() {
    }
}
