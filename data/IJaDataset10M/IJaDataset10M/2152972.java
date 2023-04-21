package ictk.boardgame.chess.io;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.*;
import java.io.StreamTokenizer;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import java.util.regex.*;
import java.util.Stack;
import ictk.util.Log;
import ictk.boardgame.Game;
import ictk.boardgame.History;
import ictk.boardgame.Board;
import ictk.boardgame.GameInfo;
import ictk.boardgame.IllegalMoveException;
import ictk.boardgame.AmbiguousMoveException;
import ictk.boardgame.OutOfTurnException;
import ictk.boardgame.io.InvalidGameFormatException;
import ictk.boardgame.chess.*;

/** PGNReader reads PGN formated files
 */
public class PGNReader extends ChessReader {

    /** mask for Log.debug() */
    public static final long DEBUG = Log.GameReader;

    /** for reading boards in the FEN tag */
    protected static FEN fen = new FEN();

    /**game information pattern*/
    protected static final Pattern giPattern;

    /**for reading SAN. Might one day be Localized */
    protected ChessMoveNotation notation = new SAN();

    /** used for error recovery */
    protected ChessGame game;

    /** used for error recovery */
    protected ChessGameInfo gameInfo;

    protected ChessBoard board;

    static {
        giPattern = Pattern.compile("\\[\\s*(\\w+)\\s+\"(.*)\"\\s*\\]");
    }

    public PGNReader(Reader _ir) {
        super(_ir);
    }

    public Game readGame() throws InvalidGameFormatException, IllegalMoveException, AmbiguousMoveException, IOException {
        History history = null;
        gameInfo = (ChessGameInfo) readGameInfo();
        board = (ChessBoard) readBoard();
        if (board == null) board = new ChessBoard();
        game = new ChessGame(gameInfo, board);
        history = readHistory();
        if (gameInfo == null && history == null) return null;
        return game;
    }

    /** gets the last game read.  This can be used if an exception was
    *  thrown during the reading, and you still want the game.
    */
    public Game getGame() {
        return game;
    }

    /** reads the game info header portion of the PGN file
    */
    public GameInfo readGameInfo() throws IOException {
        String line = null;
        Matcher result = null;
        ChessGameInfo gameInfo = new ChessGameInfo();
        boolean headerFound = false, headerDone = false;
        String key = null, value = null;
        while (!headerDone && (line = readLine()) != null) {
            if (line.startsWith("%")) continue;
            if (headerFound && line.equals("")) {
                headerDone = true;
            } else {
                result = giPattern.matcher(line);
                if (result.find()) {
                    headerFound = true;
                    if (Log.debug) Log.debug(DEBUG, "GameInfo header", result);
                    key = result.group(1);
                    value = result.group(2);
                    _setGameInfo(gameInfo, key, value);
                }
            }
        }
        if (!headerFound) return null; else return gameInfo;
    }

    /** reads the history list which must be in SAN format.
    */
    public History readHistory() throws InvalidGameFormatException, IllegalMoveException, AmbiguousMoveException, IOException {
        History history = game.getHistory();
        boolean finished = false, done = false;
        String line = null;
        String tok = null, tok2 = null;
        ChessMove move = null;
        ChessMove lastMove = null;
        int count = 0;
        int i = 0;
        ChessResult res = null;
        Stack forks = new Stack();
        ChessAnnotation anno = null;
        String savedComment = null;
        short nag = 0;
        StreamTokenizer st = new StreamTokenizer(this);
        StringBuffer sb = new StringBuffer();
        if (Log.debug) Log.debug(DEBUG, "reading History");
        st.ordinaryChars(33, 255);
        st.wordChars(33, 255);
        st.ordinaryChar('.');
        st.ordinaryChar('(');
        st.ordinaryChar(')');
        st.ordinaryChar('{');
        st.ordinaryChar('}');
        st.ordinaryChar(';');
        st.ordinaryChar('*');
        st.ordinaryChar('\t');
        st.eolIsSignificant(true);
        while (!finished && st.nextToken() != st.TT_EOF) {
            tok = st.sval;
            if (Log.debug) Log.debug(DEBUG, "token: " + tok);
            if (tok == null) {
                switch(st.ttype) {
                    case StreamTokenizer.TT_EOL:
                    case '.':
                        continue;
                    case ';':
                        sb = new StringBuffer();
                        st.ordinaryChar(' ');
                        while (st.nextToken() != st.TT_EOL) {
                            tok2 = st.sval;
                            if (st.ttype != '\t') if (tok2 != null) sb.append(tok2); else sb.append((char) st.ttype);
                        }
                        st.whitespaceChars(' ', ' ');
                        if (Log.debug) Log.debug(DEBUG, "eol comment: {" + sb.toString() + "}");
                        if (lastMove != null && lastMove == history.getCurrentMove()) {
                            anno = (ChessAnnotation) lastMove.getAnnotation();
                            if (anno == null || anno.getComment() == null) {
                                if (anno == null) anno = new ChessAnnotation();
                                anno.setComment(sb.toString());
                                lastMove.setAnnotation(anno);
                                if (Log.debug) Log.debug(DEBUG, "eol comment for (" + lastMove + "): " + lastMove.getAnnotation().getComment());
                            } else anno.appendComment(" " + sb.toString());
                            anno = null;
                        } else {
                            savedComment = sb.toString();
                        }
                        break;
                    case '{':
                        sb = new StringBuffer();
                        done = false;
                        st.ordinaryChar(' ');
                        while (!done && st.nextToken() != st.TT_EOF) {
                            tok2 = st.sval;
                            switch(st.ttype) {
                                case '}':
                                    done = true;
                                    break;
                                case StreamTokenizer.TT_EOL:
                                case '\t':
                                    break;
                                default:
                                    if (tok2 != null) sb.append(tok2); else sb.append((char) st.ttype);
                            }
                        }
                        st.whitespaceChars(' ', ' ');
                        if (Log.debug) Log.debug(DEBUG, "comment: {" + sb.toString() + "}");
                        if (lastMove != null && lastMove == history.getCurrentMove()) {
                            anno = (ChessAnnotation) lastMove.getAnnotation();
                            if (anno == null || anno.getComment() == null) {
                                if (anno == null) anno = new ChessAnnotation();
                                anno.setComment(sb.toString());
                                lastMove.setAnnotation(anno);
                                anno = null;
                            } else {
                                if (savedComment != null) lastMove.getAnnotation().appendComment(" " + savedComment);
                                savedComment = sb.toString();
                            }
                        } else {
                            savedComment = sb.toString();
                        }
                        break;
                    case '(':
                        history.prev();
                        forks.push(history.getCurrentMove());
                        if (Log.debug) Log.debug(DEBUG, "starting variation from " + history.getCurrentMove());
                        if (savedComment != null) {
                            anno = (ChessAnnotation) lastMove.getAnnotation();
                            if (anno == null || anno.getComment() == null) {
                                if (anno == null) anno = new ChessAnnotation();
                                anno.setComment(savedComment);
                            } else anno.appendComment(" " + savedComment);
                            savedComment = null;
                        }
                        break;
                    case ')':
                        ChessMove fork = (ChessMove) forks.pop();
                        history.goTo(fork);
                        if (Log.debug) Log.debug(DEBUG, "ending variation from " + fork);
                        history.next();
                        if (savedComment != null) {
                            anno = (ChessAnnotation) lastMove.getAnnotation();
                            if (anno == null || anno.getComment() == null) {
                                if (anno == null) anno = new ChessAnnotation();
                                anno.setComment(savedComment);
                            } else anno.appendComment(" " + savedComment);
                            savedComment = null;
                        }
                        lastMove = (ChessMove) history.getCurrentMove();
                        break;
                    case '*':
                        if (Log.debug) Log.debug(DEBUG, "Result token: " + tok);
                        if (lastMove != null) lastMove.setResult(new ChessResult(ChessResult.UNDECIDED));
                        finished = true;
                        break;
                }
            } else if ((nag = NAG.stringToNumber(tok)) != 0) {
                if (Log.debug) Log.debug(DEBUG, "NAG symbol(nag): " + tok);
                if (lastMove != null) {
                    anno = (ChessAnnotation) lastMove.getAnnotation();
                    if (anno == null) anno = new ChessAnnotation();
                    anno.addNAG(nag);
                    lastMove.setAnnotation(anno);
                }
            } else if (Character.isDigit(tok.charAt(0))) {
                if ((res = (ChessResult) notation.stringToResult(tok)) != null) {
                    finished = true;
                    if (Log.debug) Log.debug(DEBUG, "Result token: " + tok);
                    if (lastMove != null) {
                        lastMove.setResult(res);
                        if (Log.debug) {
                            Log.debug(DEBUG, "Result set(" + lastMove + "): " + res);
                            ChessMove prevTmp = (ChessMove) lastMove.getPrev();
                            if (prevTmp != null) Log.debug(DEBUG, "Result set(" + lastMove + "): " + res + " prev move: " + lastMove.getPrev().dump());
                        }
                    } else if (Log.debug) Log.debug(DEBUG, "Result not set; no last move");
                }
            } else if (Character.isLetter(tok.charAt(0))) {
                try {
                    move = (ChessMove) notation.stringToMove(board, tok);
                    if (move != null) {
                        history.add(move);
                        if (savedComment != null) {
                            anno = new ChessAnnotation();
                            anno.setComment(savedComment);
                            move.setPrenotation(anno);
                            Log.debug(DEBUG, "prenotation set: " + move.getPrenotation().getComment());
                            savedComment = null;
                        }
                        lastMove = move;
                        count++;
                    } else {
                        if (Log.debug) Log.debug(DEBUG, "Thought this was a move: " + tok);
                        throw new IOException("Thought this was a move: " + tok);
                    }
                } catch (OutOfTurnException e) {
                    if (Log.debug) {
                        Log.debug(DEBUG, e);
                        Log.debug2(DEBUG, "From Token: " + tok);
                        Log.debug2(DEBUG, "Board: \n" + board);
                    }
                    throw e;
                } catch (AmbiguousMoveException e) {
                    if (Log.debug) {
                        Log.debug(DEBUG, e);
                        Log.debug2(DEBUG, "From Token: " + tok);
                        Log.debug2(DEBUG, "Board: \n" + board);
                    }
                    throw e;
                } catch (IllegalMoveException e) {
                    if (Log.debug) {
                        Log.debug(DEBUG, e);
                        Log.debug2(DEBUG, "From Token: " + tok);
                        Log.debug2(DEBUG, "Board: \n" + board);
                    }
                    throw e;
                }
            } else {
                if (Log.debug) Log.debug(DEBUG, "No idea what this is: <" + tok + ">");
            }
        }
        if (Log.debug) {
            history.goToEnd();
            if (history.getCurrentMove() != null) {
                Log.debug(DEBUG, "final result is: " + history.getCurrentMove().getResult());
            }
        }
        history.rewind();
        if (count == 0) {
            if (Log.debug) Log.debug(DEBUG, "finished reading History: empty");
            return null;
        } else {
            if (Log.debug) Log.debug(DEBUG, "finished reading History");
            return history;
        }
    }

    /** looks for a readable board notation (FEN) in the GameInfo header
    *  @return null if there is no particular position associated with
    *                this board.
    */
    public Board readBoard() throws IOException {
        Board board = null;
        String fenStr = null;
        if (gameInfo == null) return null;
        fenStr = gameInfo.get("FEN");
        if (fenStr == null) return null; else return fen.stringToBoard(fenStr);
    }

    protected void _setGameInfo(ChessGameInfo gi, String key, String value) {
        StringTokenizer st = null;
        String tok = null;
        ChessPlayer p = null;
        if ("Event".equalsIgnoreCase(key)) {
            if (!value.equals("?")) gi.setEvent(value);
        } else if ("Site".equalsIgnoreCase(key)) {
            if (!value.equals("?")) gi.setSite(value);
        } else if ("Round".equalsIgnoreCase(key)) {
            if (!value.equals("-") && !value.equals("?")) gi.setRound(value);
        } else if ("SubRound".equalsIgnoreCase(key)) {
            if (!value.equals("-") && !value.equals("?")) gi.setSubRound(value);
        } else if ("White".equalsIgnoreCase(key)) {
            if (!value.equals("")) {
                p = new ChessPlayer(value);
                gi.setWhite(p);
            }
        } else if ("Black".equalsIgnoreCase(key)) {
            if (!value.equals("")) {
                p = new ChessPlayer(value);
                gi.setBlack(p);
            }
        } else if ("Result".equalsIgnoreCase(key)) {
            gi.setResult((ChessResult) notation.stringToResult(value));
        } else if ("WhiteElo".equalsIgnoreCase(key)) {
            try {
                gi.setWhiteRating(Integer.parseInt(value));
            } catch (NumberFormatException e) {
            }
        } else if ("BlackElo".equalsIgnoreCase(key)) {
            try {
                gi.setBlackRating(Integer.parseInt(value));
            } catch (NumberFormatException e) {
            }
        } else if ("ECO".equalsIgnoreCase(key)) {
            gi.setECO(value);
        } else if ("TimeControl".equalsIgnoreCase(key)) {
            st = new StringTokenizer(value, "+", false);
            try {
                if (st.hasMoreTokens()) {
                    gi.setTimeControlInitial(Integer.parseInt(st.nextToken()));
                    if (st.hasMoreTokens()) gi.setTimeControlIncrement(Integer.parseInt(st.nextToken()));
                }
            } catch (NumberFormatException e) {
            }
        } else if ("Date".equalsIgnoreCase(key)) {
            if (!value.equals("????.??.??")) {
                Calendar date = new GregorianCalendar();
                st = new StringTokenizer(value, "./", false);
                tok = null;
                try {
                    if (st.hasMoreTokens()) {
                        tok = st.nextToken();
                        if (!tok.startsWith("?")) {
                            date.set(Calendar.YEAR, Integer.parseInt(tok));
                            gi.setYear(Integer.parseInt(tok));
                        }
                        if (st.hasMoreTokens()) {
                            tok = st.nextToken();
                            if (!tok.startsWith("?")) {
                                date.set(Calendar.MONTH, Integer.parseInt(tok) - 1);
                                gi.setMonth(Integer.parseInt(tok));
                            }
                            if (st.hasMoreTokens()) {
                                tok = st.nextToken();
                                if (!tok.startsWith("?")) {
                                    date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tok));
                                    gi.setDay(Integer.parseInt(tok));
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                }
                gi.setDate(date);
            }
        } else {
            gi.add(key, value);
        }
    }
}
