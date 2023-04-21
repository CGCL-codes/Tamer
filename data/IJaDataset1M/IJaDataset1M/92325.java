package raptor.chess.pgn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import raptor.Raptor;
import raptor.chess.Game;
import raptor.chess.GameCursor;
import raptor.chess.Move;
import raptor.chess.Variant;
import raptor.pref.PreferenceKeys;
import raptor.util.RaptorLogger;
import raptor.util.RaptorStringUtils;

/**
 * A class containing PGN utils.
 */
public class PgnUtils {

    private static Date DEFAULT_PGN_DATE_HEADER = null;

    public static final DateFormat PGN_HEADER_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

    public static String DEFAULT_PGN_HEADER = "?";

    public static String DEFAULT_PGN_RESULT_HEADER = "*";

    private static final RaptorLogger LOG = RaptorLogger.getLog(PgnUtils.class);

    public static final String PGN_MIME_TYPE = "application/x-chess-pgn";

    private static final Object PGN_APPEND_SYNCH = new Object();

    /**
	 * Prepends the game to the users game pgn file.
	 */
    public static void appendGameToFile(Game game) {
        if (Variant.isBughouse(game.getVariant())) {
            return;
        }
        if (game.getMoveList().getSize() == 0) {
            return;
        }
        String pgnFilePath = Raptor.getInstance().getPreferences().getString(PreferenceKeys.APP_PGN_FILE);
        if (StringUtils.isNotEmpty(pgnFilePath)) {
            synchronized (PGN_APPEND_SYNCH) {
                if (game instanceof GameCursor) {
                    game = ((GameCursor) game).getMasterGame();
                }
                String whiteRating = game.getHeader(PgnHeader.WhiteElo);
                String blackRating = game.getHeader(PgnHeader.BlackElo);
                whiteRating = StringUtils.remove(whiteRating, 'E');
                whiteRating = StringUtils.remove(whiteRating, 'P');
                blackRating = StringUtils.remove(blackRating, 'E');
                blackRating = StringUtils.remove(blackRating, 'P');
                if (!NumberUtils.isDigits(whiteRating)) {
                    game.removeHeader(PgnHeader.WhiteElo);
                }
                if (!NumberUtils.isDigits(blackRating)) {
                    game.removeHeader(PgnHeader.BlackElo);
                }
                String pgn = game.toPgn();
                File file = new File(pgnFilePath);
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(file, true);
                    fileWriter.append(pgn).append("\n\n");
                    fileWriter.flush();
                } catch (IOException ioe) {
                    LOG.error("Error saving game", ioe);
                } finally {
                    try {
                        if (fileWriter != null) {
                            fileWriter.close();
                        }
                    } catch (IOException ioe) {
                    }
                }
            }
        }
    }

    /**
	 * Returns the approximate number of games in the specified file.
	 */
    public static int getApproximateGameCount(String file) {
        int result = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                if (StringUtils.startsWithIgnoreCase(currentLine, "[Event")) {
                    result++;
                }
            }
        } catch (IOException ioe) {
            LOG.error("Error reading game count" + file, ioe);
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
            }
        }
        return result;
    }

    /**
	 * Returns the PgnHeader line for the specified header name and value.
	 */
    public static void getHeaderLine(StringBuilder builder, String pgnHeaderName, String pgnHeaderValue) {
        builder.append("[").append(pgnHeaderName).append(" \"").append(pgnHeaderValue == null || pgnHeaderValue.length() == 0 ? PgnHeader.UNKNOWN_VALUE : pgnHeaderValue).append("\"]");
    }

    /**
	 * Returns the string to use for the move in a Pgn file. This includes the
	 * move number and all annotations.
	 */
    public static boolean getMove(StringBuilder builder, Move move, boolean forceMoveNumber) {
        boolean result = false;
        if (forceMoveNumber || move.isWhitesMove()) {
            int moveNumber = move.getFullMoveCount();
            builder.append(moveNumber).append(move.isWhitesMove() ? ". " : "... ");
        }
        builder.append(move.toString());
        for (SublineNode subline : move.getSublines()) {
            result = true;
            builder.append(" (");
            getSubline(builder, subline);
            builder.append(")");
        }
        for (Comment comment : move.getComments()) {
            builder.append(" {").append(comment.getText()).append("}");
        }
        for (Nag nag : move.getNags()) {
            builder.append(" ").append(nag.getNagString());
        }
        for (TimeTakenForMove timeTaken : move.getTimeTakenForMove()) {
            builder.append(" {").append(timeTaken.getText()).append("}");
            break;
        }
        return result;
    }

    /**
	 * Cuts off all information except for the position.
	 */
    public static String getPositionFromFen(String fen) {
        int spaceIndex = fen.indexOf(' ');
        return fen.substring(0, spaceIndex);
    }

    /**
	 * Returns the pgn representation of the specified subline including all
	 * annotations.
	 */
    public static void getSubline(StringBuilder builder, SublineNode subline) {
        boolean forceMoveNumber = getMove(builder, subline.getMove(), true);
        SublineNode current = subline.getReply();
        while (current != null) {
            builder.append(" ");
            forceMoveNumber = getMove(builder, current.getMove(), forceMoveNumber);
            current = current.getReply();
        }
    }

    /**
	 * [Date "2009.10.07"]
	 */
    public static String longToPgnDate(long time) {
        return PGN_HEADER_DATE_FORMAT.format(new Date(time));
    }

    /**
	 * Converts a PgnHeader.Date header into a date object.
	 */
    public static Date pgnDateHeaderToDate(String pgnDateValue) {
        Date result = null;
        initPgnDateHeader();
        if (pgnDateValue.length() != 10) {
            LOG.error("Invalid pgn header date format: " + pgnDateValue + " setting to default.");
            result = DEFAULT_PGN_DATE_HEADER;
        } else if (pgnDateValue.startsWith("????")) {
            result = DEFAULT_PGN_DATE_HEADER;
        } else {
            String year = pgnDateValue.substring(0, 4);
            String month = pgnDateValue.substring(5, 7);
            String day = pgnDateValue.substring(8, 10);
            if (month.equals("??")) {
                month = "01";
            }
            if (day.equals("??")) {
                day = "01";
            }
            try {
                result = PGN_HEADER_DATE_FORMAT.parse(year + "." + month + "." + day);
            } catch (ParseException pe) {
                LOG.error("Invalid pgn header date format: " + pgnDateValue + " " + year + "." + month + "." + day + " setting to default.");
                result = DEFAULT_PGN_DATE_HEADER;
            }
        }
        return result;
    }

    /**
	 * [TimeControl "60+0"]
	 */
    public static String timeIncMillisToTimeControl(long startTimeMillis, long startIncMillis) {
        String minutes = String.valueOf(startTimeMillis / 1000);
        String inc = String.valueOf(startIncMillis / 1000);
        return minutes + "+" + inc;
    }

    /**
	 * [WhiteClock "0:01:00.000"] [BlackClock "0:01:00.000"]
	 */
    public static String timeToClock(long timeMillis) {
        long timeLeft = timeMillis;
        if (timeLeft < 0) {
            timeLeft = 0;
        }
        int hour = (int) (timeLeft / (60000L * 60));
        timeLeft -= hour * 60 * 1000 * 60;
        int minute = (int) (timeLeft / 60000L);
        timeLeft -= minute * 60 * 1000;
        int seconds = (int) (timeLeft / 1000L);
        timeLeft -= seconds * 1000;
        int millis = (int) timeLeft;
        return RaptorStringUtils.defaultTimeString(hour, 2) + ":" + RaptorStringUtils.defaultTimeString(minute, 2) + ":" + RaptorStringUtils.defaultTimeString(seconds, 2) + "." + RaptorStringUtils.defaultTimeString(millis, 1);
    }

    /**
	 * Chess base EMT format. 1. e4 {[%emt 0.0]} e6 {[%emt 0.0]} 2. Nc3 {[%emt
	 * 1.398]} Nf6 {[%emt 0.1]}
	 */
    public static String timeToEMTFormat(long elapsedTimeMillis) {
        double elapsedTimeInSeconds = elapsedTimeMillis / 1000.0;
        BigDecimal bigDecimal = new BigDecimal(elapsedTimeInSeconds);
        bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
        return "[%emt " + bigDecimal.toString() + "]";
    }

    private static void initPgnDateHeader() {
        if (DEFAULT_PGN_DATE_HEADER == null) {
            try {
                DEFAULT_PGN_DATE_HEADER = PGN_HEADER_DATE_FORMAT.parse("1500.01.01");
            } catch (ParseException pe) {
                throw new RuntimeException(pe);
            }
        }
    }
}
