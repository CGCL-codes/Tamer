package ictk.boardgame.chess.net.ics.event;

import ictk.util.Log;
import ictk.boardgame.chess.net.ics.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** <p>A message from the server indicating something happened. All events
 *  are timestamped (with the client's receiving time).  If Log.debug is 
 *  true then the original server message is also stored.  If Log.debug
 *  isn't true you'll save some memory overhead and the native message
 *  can be rebuilt from the appropriate parser.
 *  <p>Fake messages are also supported (those issued by bots but 
 *  intending to emulate the server).
 */
public abstract class ICSEvent {

    public static final long DEBUG = Log.ICSEvent;

    public static final int UNKNOWN_EVENT = 0, BOARD_UPDATE_EVENT = 1, GAME_CREATED_EVENT = 2, GAME_RESULT_EVENT = 3, GAME_NOTIFICATION_EVENT = 4, TAKEBACK_REQUEST_EVENT = 5, CHANNEL_EVENT = 6, SHOUT_EVENT = 7, TOURNAMENT_CHANNEL_EVENT = 8, TELL_EVENT = 9, SAY_EVENT = 10, CHALLENGE_EVENT = 11, SEEK_AD_EVENT = 12, SEEK_REMOVE_EVENT = 13, SEEK_AD_READABLE_EVENT = 14, SEEK_REMOVE_READABLE_EVENT = 15, SEEK_CLEAR_EVENT = 16, KIBITZ_EVENT = 17, WHISPER_EVENT = 18, BOARD_SAY_EVENT = 19, QTELL_EVENT = 20, AUTO_SALUTE_EVENT = 21, MOVE_LIST_EVENT = 22, MATCH_REQUEST_EVENT = 23, PLAYER_NOTIFICATION_EVENT = 24, AVAIL_INFO_EVENT = 25, USER_DEFINED_EVENT = 26, PLAYER_CONNECTION_EVENT = 27, HISTORY_EVENT = 28, EXAMINE_NAVIGATION_EVENT = 29, EXAMINE_NAVIGATION_END_EVENT = 30, EXAMINE_NAVIGATION_BEGIN_EVENT = 31, EXAMINE_NAVIGATION_END_VAR_EVENT = 32, EXAMINE_REVERT_EVENT = 33, EXAMINE_COMMIT_EVENT = 34, EXAMINER_SELF_EVENT = 35, EXAMINER_OTHER_EVENT = 36, NUM_EVENTS = 37;

    /** each event has a type for easy casting */
    protected int eventType = UNKNOWN_EVENT;

    /** This is the parser for particular ICS messages.  This way
       ** FICS parsers and ICC parsers can be used by the same ICSEvent
       ** objects. */
    protected ICSEventParser eventParser;

    /** this is the server the ICSEvent originally came from */
    protected ICSProtocolHandler server;

    /** some servers have a way of sending events from non-server
       ** origins, such as QTells, where the event might look like
       ** a tell, but is proceeded by a ":" because it's actually
       ** generated from a bot. */
    protected boolean isFake;

    /** this is the timestamp of when the message was received by
       ** the ICTK program.  This date might slightly differ from when
       ** the server thought it sent the event, as transmission time
       ** is involved. */
    protected Date timestamp;

    /** this is used by many of the events for a human readable 
       ** message of some kind.  It also doubles as a repository for
       ** the parsing errors that produce UNKNONW_EVENTs. */
    protected String message;

    /** this is the original data that was parsed to create this 
       ** message.  This is only valid if Log.debug() is true. */
    protected String original;

    public ICSEvent(ICSProtocolHandler server, int eventType) {
        this.server = server;
        this.eventType = eventType;
        this.timestamp = new Date();
    }

    public ICSEvent(int eventType) {
        this.eventType = eventType;
        this.timestamp = new Date();
    }

    /** returns the server that originated this message 
    */
    public ICSProtocolHandler getServer() {
        return server;
    }

    /** sets the server this event originally came from
    */
    public void setServer(ICSProtocolHandler server) {
        this.server = server;
    }

    /** returns the type of the object (for easy casting)
    */
    public int getEventType() {
        return eventType;
    }

    /** sets the type of event this is.
    */
    public void setEventType(int type) {
        eventType = type;
    }

    /** this is the moment the event was received from the server.
    */
    public Date getTimestamp() {
        return timestamp;
    }

    /** sets the timestamp to the value specified.
    */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /** returns a non-parseable string associated with this message, or if
    *  an error in the parsing as occured setting the message type to
    *  UNKNOWN_EVENT then this will contain the entire original event string.
    */
    public String getMessage() {
        return message;
    }

    /** sets a non-parseable string associated with this message, or if
    *  an error in the parsing as occured setting the message type to
    *  UNKNOWN_EVENT then this will contain the entire original event string.
    */
    public void setMessage(String mesg) {
        message = mesg;
    }

    public ICSEventParser getEventParser() {
        return eventParser;
    }

    public void setEventParser(ICSEventParser parser) {
        this.eventParser = parser;
    }

    /**is this a QTell in disquise?
    */
    public void setFake(boolean t) {
        isFake = t;
    }

    /**is this a QTell in disquise?
    */
    public boolean isFake() {
        return isFake;
    }

    /** returns a readable form of this event.  Typically this is similar
    *  to the original text sent by the server, but it does not have
    *  to be.
    */
    public abstract String getReadable();

    /** this method stores the original message that was parsed to 
    *  create this event.  This only happens if debug mode is on
    *  (Log.debug).  If not, then no value is stored.
    */
    public void setOriginal(String s) {
        if (Log.debug) original = s;
    }

    /** this method returns the original text that was parsed to get the 
    *  the event.  This method is not valid if debug mode is not active.
    *  If this method is called and Log.debug is not true, then an
    *  IllegalStateException will be thrown.
    */
    public String DEBUG_getOriginal() {
        if (Log.debug) return original; else {
            throw new IllegalStateException("Can't ask for original if not in debug mode.");
        }
    }

    public String toString() {
        return getReadable();
    }
}
