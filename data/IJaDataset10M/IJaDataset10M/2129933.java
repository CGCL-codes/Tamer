package reproductor;

import java.util.Map;

public interface ReproductorLanzador {

    /**
     * Open callback, stream is ready to play.
     *
     * properties map includes audio format dependant features such as
     * bitrate, duration, frequency, channels, number of frames, vbr flag,
     * id3v2/id3v1 (for MP3 only), comments (for Ogg Vorbis), ...  
     *
     * @param stream could be File, URL or InputStream
     * @param properties audio stream properties.
     */
    public void abierto(Map properties);

    /**
     * Progress callback while playing.
     * 
     * This method is called severals time per seconds while playing.
     * properties map includes audio format features such as
     * instant bitrate, microseconds position, current frame number, ... 
     * 
     * @param bytesread from encoded stream.
     * @param microseconds elapsed (<b>reseted after a seek !</b>).
     * @param pcmdata PCM samples.
     * @param properties audio stream parameters.
     */
    public void progreso(int bytesread, long microseconds, byte[] pcmdata, Map properties);

    /**
     * Notification callback for basicplayer events such as opened, eom ...
     *  
     * @param event
     */
    public void estadoActualizado(ReproductorEvento event);

    /**
     * A handle to the BasicPlayer, plugins may control the player through
     * the controller (play, stop, ...)
     * @param controller : a handle to the player
     */
    public void setControlador(Controlador controller);
}
