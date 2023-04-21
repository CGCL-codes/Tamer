package org.zmpp.vm;

import org.zmpp.base.StoryFileHeader;
import org.zmpp.windowing.StatusLine;
import org.zmpp.windowing.ScreenModel6;
import org.zmpp.windowing.ScreenModel;
import org.zmpp.base.Memory;
import org.zmpp.encoding.IZsciiEncoding;
import org.zmpp.media.PictureManager;
import org.zmpp.media.Resources;
import org.zmpp.media.SoundSystem;

/**
 * This interface acts as a central access point to the Z-Machine's components.
 * It is mainly provided as a service point for the instructions to manipulate
 * and read the VM's internal state.
 * 
 * @author Wei-ju Wu
 * @version 1.5
 */
public interface Machine extends ObjectTree, Input, Output, Cpu, Memory, IZsciiEncoding {

    /**
   * Initialization function.
   * 
   * @param data the story data
   * @param resources Blorb resources
   */
    void initialize(byte[] data, Resources resources);

    /**
   * Returns the story file version.
   * @return the story file version
   */
    int getVersion();

    /**
   * Returns the release.
   * @return the release
   */
    int getRelease();

    boolean hasValidChecksum();

    StoryFileHeader getFileHeader();

    Resources getResources();

    int lookupToken(int dictionaryAddress, String token);

    String getDictionaryDelimiters();

    void encode(int source, int length, int destination);

    String decode2Zscii(int address, int length);

    int getNumZEncodedBytes(int address);

    /**
   * Returns the current run state of the machine
   * @return the run state
   */
    MachineRunState getRunState();

    /**
   * Sets the current run state of the machine
   * @param runstate the run state
   */
    void setRunState(MachineRunState runstate);

    /**
   * Halts the machine with the specified error message.
   * @param errormsg the error message
   */
    void halt(String errormsg);

    /**
   * Restarts the virtual machine.
   */
    void restart();

    /**
   * Starts the virtual machine.
   */
    void start();

    /**
   * Exists the virtual machine.
   */
    void quit();

    /**
   * Outputs a warning message.
   *  
   * @param msg
   */
    void warn(String msg);

    /**
   * Tokenizes the text in the text buffer using the specified parse buffer.
   * @param textbuffer the text buffer
   * @param parsebuffer the parse buffer
   * @param dictionaryAddress the dictionary address or 0 for the default
   * dictionary
   * @param flag if set, unrecognized words are not written into the parse
   * buffer and their slots are left unchanged
   */
    void tokenize(int textbuffer, int parsebuffer, int dictionaryAddress, boolean flag);

    /**
   * Reads a string from the currently selected input stream into
   * the text buffer address.
   * @param textbuffer the text buffer address
   * @return the terminator character
   */
    char readLine(int textbuffer);

    /**
   * Reads a ZSCII char from the selected input stream.
   * @return the selected ZSCII char
   */
    char readChar();

    /**
   * Returns the sound system.
   * @return the sound system
   */
    SoundSystem getSoundSystem();

    /**
   * Returns the picture manager.
   * @return the picture manager
   */
    PictureManager getPictureManager();

    /**
   * Generates a number in the range between 1 and <i>range</i>. If range is
   * negative, the random generator will be seeded to abs(range), if
   * range is 0, the random generator will be initialized to a new
   * random seed. In both latter cases, the result will be 0.
   * @param range the range
   * @return a random number
   */
    char random(short range);

    /**
   * Updates the status line.
   */
    void updateStatusLine();

    /**
   * Sets the Z-machine's status line.
   * @param statusline the status line
   */
    void setStatusLine(StatusLine statusline);

    /**
   * Sets the game screen.
   * @param screen the screen model
   */
    void setScreen(ScreenModel screen);

    /**
   * Gets the game screen.
   * @return the game screen
   */
    ScreenModel getScreen();

    /**
   * Returns screen model 6.
   * @return screen model 6
   */
    ScreenModel6 getScreen6();

    /**
   * Sets the save game data store.
   * @param datastore the data store
   */
    void setSaveGameDataStore(SaveGameDataStore datastore);

    /**
   * Saves the current state.
   * @param savepc the save pc
   * @return true on success, false otherwise
   */
    boolean save(int savepc);

    /**
   * Saves the current state in memory.
   * @param savepc the save pc
   * @return true on success, false otherwise
   */
    boolean save_undo(int savepc);

    /**
   * Restores a previously saved state.
   * @return the portable game state
   */
    PortableGameState restore();

    /**
   * Restores a previously saved state from memory.
   * @return the portable game state
   */
    PortableGameState restore_undo();
}
