package keyboardhero;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.sound.midi.*;
import keyboardhero.MidiSequencer.*;
import keyboardhero.MidiSong.*;
import keyboardhero.Graphs.*;

final class Game {

    /** C-style structure class to hold the information of the current game state. */
    static final class State {

        static final Note[] NULL_NOTES = new Note[0];

        static final MidiFileInfo NULL_INFO = new MidiFileInfo("", "", Byte.MIN_VALUE);

        /** The current score of the player. */
        int score = 0;

        int totalScore = 0;

        /** The current time position expressed in midi ticks. */
        long time = 0;

        /** Contains the information (all the notes) associated with a song track */
        Note[] notes = NULL_NOTES;

        MidiFileInfo songInfo = NULL_INFO;

        /** The duration of a note to fall down expressed in midi ticks. */
        int fallingTime = 1000;

        /**
		 * Describes whether the song is started to play yet or it is just the initial preparation
		 * period.
		 */
        boolean isPlaying;

        float speed = 1;

        long msLength;

        String msLengthStr;

        long msPosition;

        SortedMap<Integer, SortedMap<Integer, SortedSet<Key>>> activeKeys = Collections.synchronizedSortedMap(new TreeMap<Integer, SortedMap<Integer, SortedSet<Key>>>());

        private State() {
        }
    }

    /** The sleeping time of the main thread of game in milliseconds. */
    private static int sleepTime = Util.getPropInt("performance");

    static final int FALLING_TIME_IN_MILLISECONDS = 2000;

    static final TreeMap<Integer, String> PERFORMANCES = new TreeMap<Integer, String>();

    static {
        PERFORMANCES.put(10, "BestQuality");
        PERFORMANCES.put(25, "GoodQuality");
        PERFORMANCES.put(50, "Avarage");
        PERFORMANCES.put(75, "GoodSpeed");
        PERFORMANCES.put(100, "BestSpeed");
    }

    private static State state = new State();

    private static final MidiSequencer SEQUENCER = MidiSequencer.getInstance();

    private static int deviceRefreshing = 0;

    /**
	 * Indicates whether the player is in game or not. If this field is true the game runs otherwise
	 * it is paused or stopped.
	 */
    private static boolean inGame = false;

    /**
	 * Indicates whether the game has been paused. If so, then the game will resumed at the next
	 * click.
	 */
    private static boolean paused = false;

    /**
	 * Indicates whether the game has been paused by debugging purposes or not. This field only used
	 * for debugging or testing.
	 * 
	 * @see Game#debugPause()
	 * @see Game#debugResume()
	 */
    private static boolean debugPaused = false;

    /**
	 * True indicates that the game should be automatically paused when the user clicks a menu item
	 * or leaves the application.
	 */
    private static boolean autoPause = Util.getPropBool("autoPause");

    private static int loopTime = 100;

    static final void newGame(URL file) {
        newGame(file, ((DialogNewGame) KeyboardHero.getDialogs().get("newGame")).getSpeed());
    }

    /**
	 * Creates a new game. Sets every value connecting to the game to its default and stops the
	 * currently possibly running game.
	 * 
	 * @param file
	 *            the midi file of the song the be played.
	 * @param speed
	 *            floating point number indicating the tempo factor in which the song should be
	 *            played.
	 * @see MidiSequencer#loadSong(URL, float)
	 */
    static void newGame(URL file, float speed) {
        try {
            final boolean pInGame = inGame;
            inGame = false;
            SEQUENCER.stopSong();
            if (pInGame) {
                Connection.checkToplist();
            }
            SEQUENCER.loadSong(file, speed);
            state.fallingTime = SEQUENCER.getFallingTime();
            loopTime = SEQUENCER.getLoopTime();
            if (loopTime == 0) loopTime = 1;
            state.time = -(state.fallingTime / loopTime) * loopTime;
            state.isPlaying = false;
            state.msLength = SEQUENCER.getLength();
            state.msLengthStr = " / " + Util.microToStr(state.msLength);
            state.msPosition = 0;
            state.score = 0;
            state.speed = speed;
            resetSituationVariables();
            gameStarted();
        } catch (InvalidMidiDataException e) {
            Util.error(Util.getMsg("Err_InvalidMidiData"), e.getLocalizedMessage());
        } catch (MidiUnavailableException e) {
            Util.error(Util.getMsg("Err_SequencerUnavailable"), e.getLocalizedMessage());
        } catch (IOException e) {
            Util.error(Util.getMsg("Err_CouldntAccessFile"), e.getLocalizedMessage());
        }
    }

    static void newRandomGame() {
        URL[] files = DialogSongList.SongSelector.getFiles();
        newGame(files[Util.RAND.nextInt(files.length)]);
    }

    static void newGameFromFile() {
        try {
            final File file = KeyboardHero.midiFile();
            if (file != null) newGame(file.toURI().toURL());
        } catch (MalformedURLException e) {
            Util.error(Util.getMsg("Err_CouldntAccessFile"), e.getLocalizedMessage());
        }
    }

    /**
	 * Runs a cycle of the game.
	 */
    static void doRun() {
        try {
            if (inGame && !paused) {
                if (state.isPlaying) {
                    state.time = SEQUENCER.getTickPosition();
                    state.msPosition = SEQUENCER.getPosition();
                    if (!SEQUENCER.isRunning()) {
                        inGame = false;
                        gameEnded();
                        SEQUENCER.closeSong();
                    }
                } else {
                    state.time += loopTime;
                    if (state.time >= 0) {
                        state.isPlaying = true;
                        SEQUENCER.startSong();
                    }
                }
                Graphs.getCanvas().repaint();
            } else if (!ScoreMessage.MESSAGES.isEmpty()) {
                Graphs.getCanvas().repaint();
            }
            if (++deviceRefreshing == MidiDevicer.REFRESH_TIME) {
                deviceRefreshing = 0;
                MidiDevicer.refreshDevices();
            }
            Thread.sleep(sleepTime);
        } catch (Throwable e) {
            if (Util.getDebugLevel() > 2) e.printStackTrace();
        }
    }

    static void exit() {
    }

    static void gameStarted() {
        Graphs.gameStarted();
        KeyboardHero.resetTitle(state.songInfo.getTitle());
        Connection.sendTitle();
        Connection.sendActivity();
    }

    static void gameEnded() {
        Graphs.gameEnded();
        KeyboardHero.resetTitle(null);
        Connection.sendActivity();
        Connection.checkToplist();
    }

    static void resetSituationVariables() {
        inGame = true;
        paused = false;
        debugPaused = false;
    }

    static void togglePause() {
        if (paused) {
            resume();
        } else if (debugPaused) {
            debugResume();
        } else {
            if (Util.isDebugMode()) {
                debugPause();
            } else {
                pause();
            }
        }
    }

    static void pause() {
        if (inGame) {
            paused = true;
            doPause();
        }
    }

    static void resume() {
        if (inGame) {
            paused = false;
            doResume();
        }
    }

    /**
	 * Pauses the game for debugging or testing purposes. It is used when the debugging menu is used
	 * or a {@link Util#waitKeyPress() waitKeyPress} method have been called.
	 */
    static void debugPause() {
        if (inGame) {
            debugPaused = true;
            inGame = false;
            doPause();
        }
    }

    /**
	 * Resumes the game from a pause that have been made for debugging or testing purposes. It is
	 * used when the debugging menu is used or a {@link Util#waitKeyPress() waitKeyPress} method
	 * have been called.
	 */
    static void debugResume() {
        debugPaused = false;
        inGame = true;
        doResume();
    }

    private static void doPause() {
        SEQUENCER.stopSong();
        Graphs.gamePaused();
        Connection.sendActivity();
    }

    private static void doResume() {
        if (state.isPlaying) SEQUENCER.startSong();
        Graphs.gameResumed();
        Connection.sendActivity();
    }

    static void activateKey(ShortMessage message, int device) {
        SortedMap<Integer, SortedSet<Key>> deviceKeys = state.activeKeys.get(device);
        if (deviceKeys == null) {
            deviceKeys = Collections.synchronizedSortedMap(new TreeMap<Integer, SortedSet<Key>>());
            state.activeKeys.put(device, deviceKeys);
        }
        final int channel;
        SortedSet<Key> channelKeys = deviceKeys.get(channel = message.getChannel());
        if (channelKeys == null) {
            channelKeys = Collections.synchronizedSortedSet(new TreeSet<Key>());
            deviceKeys.put(channel, channelKeys);
        }
        final Key key = new Key(message);
        channelKeys.remove(key);
        channelKeys.add(key);
        if (!isScreenRefreshing()) Graphs.activateKey(key);
    }

    static void deactivateKey(ShortMessage message, int device) {
        if (Util.getDebugLevel() > 30) Util.debug("REMOVE FROM DEVICE:" + device);
        SortedMap<Integer, SortedSet<Key>> deviceKeys = state.activeKeys.get(device);
        final Key key = new Key(message);
        if (deviceKeys != null) {
            if (Util.getDebugLevel() > 30) Util.debug("REMOVE FROM CHANNEL:" + message.getChannel());
            SortedSet<Key> channelKeys = deviceKeys.get(message.getChannel());
            if (channelKeys != null) {
                if (Util.getDebugLevel() > 30) Util.debug("REMOVE KEY:" + key);
                channelKeys.remove(key);
            }
        }
        if (!isScreenRefreshing()) {
            Key newKey = null;
            deviceloop: for (SortedMap<Integer, SortedSet<Key>> devKeys : state.activeKeys.values()) {
                for (SortedSet<Key> channelKeys : devKeys.values()) {
                    if (channelKeys != null) {
                        if (channelKeys.contains(key)) {
                            newKey = channelKeys.tailSet(key).first();
                            break deviceloop;
                        }
                    }
                }
            }
            if (newKey == null) {
                Graphs.deactivateKey(key);
            } else {
                Graphs.activateKey(newKey);
            }
        }
    }

    static void reactivateKeys(int device, int channel, int velocity) {
        SortedMap<Integer, SortedSet<Key>> deviceKeys = state.activeKeys.get(device);
        if (deviceKeys != null) {
            SortedSet<Key> channelKeys = deviceKeys.get(channel);
            if (channelKeys != null) for (Key key : channelKeys) {
                key.setVelocity(velocity);
                if (!isScreenRefreshing()) Graphs.activateKey(key);
            }
        }
    }

    static void noteScored(Key key, int score) {
        final int factoredScore = (int) (score * state.speed);
        state.score += factoredScore;
        state.totalScore += factoredScore;
        if (Util.getDebugLevel() >= 128) Util.debug("NOTE SCORED: " + score + " | " + factoredScore);
        if (score != 0) {
            Connection.sendScore();
        }
        new ScoreMessage(key, score);
    }

    static void closure() {
        Connection.writeTotalScore();
        Util.setProp("autoPause", autoPause);
        Util.setProp("performance", sleepTime);
    }

    static final State getState() {
        return state;
    }

    static final void setNoteSequence(Note[] noteSequence) {
        state.notes = noteSequence;
        Graphs.notesReseted();
    }

    static final void setSongInfo(MidiFileInfo songInfo) {
        state.songInfo = songInfo;
    }

    /**
	 * Sets whether the game should be automatically paused or not.
	 * 
	 * @param value
	 *            the new value of the {@link Game#autoPause} field.
	 * @see #isAutoPause()
	 */
    static final void setAutoPause(boolean value) {
        autoPause = value;
    }

    /**
	 * Indicates whether the game should be automatically paused or not.
	 * 
	 * @return the value of the {@link Game#autoPause} field.
	 * @see #setAutoPause(boolean)
	 */
    static final boolean isAutoPause() {
        return autoPause;
    }

    static final int getSleepTime() {
        return sleepTime;
    }

    static final void setSleepTime(int sleepTime) {
        if (sleepTime != Game.sleepTime) {
            Game.sleepTime = sleepTime;
            if (!state.isPlaying) {
                loopTime = SEQUENCER.getLoopTime();
            }
        }
    }

    /**
	 * Indicates whether the player is in game or not.
	 * 
	 * @return the value of the {@link Game#inGame} field.
	 */
    static final boolean isGameActive() {
        return inGame && !paused;
    }

    static final boolean isScreenRefreshing() {
        return (inGame && !paused) || !ScoreMessage.MESSAGES.isEmpty();
    }

    static final boolean isInGame() {
        return inGame;
    }

    static final String getActivityString() {
        if (inGame) {
            if (paused) {
                return "2";
            } else if (debugPaused) {
                return "3";
            } else {
                return "1";
            }
        }
        return "4";
    }

    /**
	 * Indicates whether the game is in a paused state.
	 * 
	 * @return the value of the {@link Game#paused} field.
	 */
    static final boolean isPaused() {
        return paused;
    }

    /**
	 * Indicates whether the game have been paused because of debugging or testing purposes.
	 * 
	 * @return the value of the {@link Game#debugPaused} field.
	 */
    static final boolean isDebugPaused() {
        return debugPaused;
    }

    /**
	 * Calculates the maximum score that is reachable until the given level. It is used to check
	 * whether the given score can be valid.
	 * 
	 * @param factor
	 *            the speed used during the game.
	 * @return the calculated maximum score.
	 */
    static int getMaxScore(float factor) {
        return (int) (SEQUENCER.getNoteNum() * 100 * factor) + 1;
    }

    static {
        Connection.readTotalScore();
    }

    /**
	 * Creates a string containing the most important information about the game. This method is
	 * used only for debugging and testing purposes.
	 * 
	 * @return the created string.
	 */
    static String getString() {
        return "Game(level=" + state.speed + "; score=" + state.score + "; isPaused=" + paused + ")";
    }

    /**
	 * This method serves security purposes. Provides an integrity string that will be checked by
	 * the {@link Connection#integrityCheck()} method; thus the application can only be altered if
	 * the source is known. Every class in the {@link keyboardhero} package has an integrity string.
	 * 
	 * @return the string of this class used for integrity checking.
	 */
    static String getIntegrityString() {
        return "psA'LÍMAsÁŰSJő!5-öpp";
    }

    /**
	 * The tester object of this class. It provides a debugging menu and unit tests for this class.
	 * Its only purpose is debugging or testing.
	 */
    static final Tester TESTER = new Tester("Game", new String[] { "Quit", "getString()", "sleepTime" }) {

        void menu(int choice) throws Exception {
            switch(choice) {
                case 5:
                    System.out.println(getString());
                    break;
                case 6:
                    System.out.println("sleepTime = " + sleepTime);
                    sleepTime = readInt("int sleepTime");
                    break;
                default:
                    baseMenu(choice);
                    break;
            }
        }

        void runUnitTests() throws Exception {
            higherTestStart("Game");
            testEq("getIntegrityString()", "psA'LÍMAsÁŰSJő!5-öpp", Game.getIntegrityString());
            higherTestEnd();
        }
    };

    /**
	 * Starts the class's developing menu. If this build is a developer's one it starts the
	 * application in a normal way with the exception that it starts the debugging tool for this
	 * class as well; otherwise exits with an error message.
	 * 
	 * @param args
	 *            the arguments given to the program.
	 * @see KeyboardHero#startApp()
	 */
    public static void main(String[] args) {
        Tester.mainer(args, TESTER);
    }
}
