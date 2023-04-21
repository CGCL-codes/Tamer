package recording;

import video.RobotVideoSource;
import audio.MicrophoneAudioSource;
import com.cattura.packet_multibroadcaster.constants.AudioVideoTypes;
import com.cattura.packet_multibroadcaster.implementations.PacketMultibroadcaster;
import com.cattura.packet_multibroadcaster.implementations.SourceGroup;
import configurations.AudioSettings;
import configurations.CaptureSettings;
import constants.VideoConstants;
import encode.Container;

/**
 * Recording class is responsible for creating video and audio sources and
 * writers which are registered to source group.
 * It is possible to record video with audio or just plain audio or video.
 * Contains implementation for setup, start and stop recording tasks.
 * Screen size and location are customizable.
 * 
 * @author Petri Tuononen
 *
 */
public class Record {

    /**
     * Default constructor.
     * For full screen recording.
     * 
     * @param settings
     * @param audioSettings
     */
    public Record(CaptureSettings settings, AudioSettings audioSettings) {
        try {
            RobotVideoSource robotVideoSource = new RobotVideoSource("RobotVideoSource", VideoConstants.SCREEN_WIDTH, VideoConstants.SCREEN_HEIGHT);
            MicrophoneAudioSource microphoneAudioSource = new MicrophoneAudioSource("MicrophoneAudioSource", audioSettings);
            SourceGroup sourceGroup = PacketMultibroadcaster.makeSourceGroup(robotVideoSource, microphoneAudioSource);
            registerWriterToSourceGroup(sourceGroup, settings, audioSettings);
        } catch (Exception e) {
        }
    }

    /**
     * Constructor.
     * For custom size screen recording.
     * 
     * @param settings
     * @param audioSettings
     * @param screenWidth
     * @param screenHeight
     */
    public Record(CaptureSettings settings, AudioSettings audioSettings, int screenWidth, int screenHeight) {
        try {
            RobotVideoSource robotVideoSource = new RobotVideoSource("RobotVideoSource", screenWidth, screenHeight);
            MicrophoneAudioSource microphoneAudioSource = new MicrophoneAudioSource("MicrophoneAudioSource", audioSettings);
            SourceGroup sourceGroup = PacketMultibroadcaster.makeSourceGroup(robotVideoSource, microphoneAudioSource);
            registerWriterToSourceGroup(sourceGroup, settings, audioSettings);
        } catch (Exception e) {
        }
    }

    /**
     * Constructor.
     * For custom size screen in specific location.
     * 
     * @param settings
     * @param audioSettings
     * @param screenWidth
     * @param screenHeight
     * @param startLocX
     * @param startLocY
     */
    public Record(CaptureSettings settings, AudioSettings audioSettings, int screenWidth, int screenHeight, int startLocX, int startLocY) {
        try {
            RobotVideoSource robotVideoSource = new RobotVideoSource("RobotVideoSource", screenWidth, screenHeight, startLocX, startLocY);
            MicrophoneAudioSource microphoneAudioSource = new MicrophoneAudioSource("MicrophoneAudioSource", audioSettings);
            SourceGroup sourceGroup = PacketMultibroadcaster.makeSourceGroup(robotVideoSource, microphoneAudioSource);
            registerWriterToSourceGroup(sourceGroup, settings, audioSettings);
        } catch (Exception e) {
        }
    }

    /**
     * Register Writer to SourceGroup
     * 
     * @param sg SourceGroup
     * @param settings CaptureSettings
     */
    private void registerWriterToSourceGroup(SourceGroup sg, CaptureSettings settings, AudioSettings audioSettings) {
        try {
            if (settings.getAudioVideoType().equalsIgnoreCase(AudioVideoTypes.AUDIO_AND_VIDEO)) {
                PacketMultibroadcaster.registerWriterToSourceGroup(sg, new Container(settings.getOutputDirPath(), settings.getFileName(), settings.getFileType(), AudioVideoTypes.AUDIO_AND_VIDEO, audioSettings));
            } else if (settings.getAudioVideoType().equalsIgnoreCase(AudioVideoTypes.AUDIO)) {
                PacketMultibroadcaster.registerWriterToSourceGroup(sg, new Container(settings.getOutputDirPath(), settings.getFileName(), settings.getFileType(), AudioVideoTypes.AUDIO, audioSettings));
            } else if (settings.getAudioVideoType().equalsIgnoreCase(AudioVideoTypes.VIDEO)) {
                PacketMultibroadcaster.registerWriterToSourceGroup(sg, new Container(settings.getOutputDirPath(), settings.getFileName(), settings.getFileType(), AudioVideoTypes.VIDEO, audioSettings));
            }
        } catch (Exception e) {
            System.out.println("Failed to register writers to a source group.");
        }
    }

    /**
     * Start recording process.
     */
    public void startRecording() {
        try {
            SourceGroup.setupProcessingOnAllSources();
        } catch (Exception e) {
            System.out.println("Error when trying to setup processes.");
        }
        try {
            SourceGroup.beginProcessingOnAllSources();
        } catch (Exception e) {
            System.out.println("Error when trying to begin processing.");
        }
    }

    /**
     * Stop recording process.
     */
    public void stopRecording() {
        try {
            SourceGroup.stopProcessingOnAllSources();
        } catch (Exception e) {
            System.out.println("Error when trying to stop processes.");
        }
    }
}
