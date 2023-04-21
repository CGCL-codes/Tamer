package wotlas.libs.sound;

import java.util.Properties;

/** A Sound Player for reading short WAV, AU, etc. sound files.
 *  Any class implementing this interface must have a constructor with no arguments.
 *
 * @author Aldiss
 */
public interface SoundPlayer {

    /** To init the sound player. The resource locator can be used to get a stream on
     *  a sound file.
     * @param props properties for init.
     * @param resourceLocator to locate sound resources.
     */
    public void init(Properties props, SoundResourceLocator resourceLocator);

    /** Closes this sound player.
     */
    public void close();

    /** To play a sound.
     * @param soundName sound file name in the sound database.
     *        we'll search the file via the resourceLocator.
     */
    public void playSound(String soundName);

    /** To get the sound volume in [0, 100].
     * @return volume new volume in [0,100]
     */
    public short getSoundVolume();

    /** To set the sound volume ( wave sounds ) in the [0,100] range.
     * @return volume new volume in [0,100]
     */
    public void setSoundVolume(short soundVolume);

    /** Tells if we want the player to play sounds or just ignore sounds 'play' requests.
     * @return true if we must ignore sound play requests
     */
    public boolean getNoSoundState();

    /** To set/unset the "No Sound" option.
     * @param noSoundState true if requests to play sounds must be ignored, false to play sounds
     *        when asked to.
     */
    public void setNoSoundState(boolean noSoundState);

    /** To get the name of this sound player.
     */
    public String getSoundPlayerName();
}
