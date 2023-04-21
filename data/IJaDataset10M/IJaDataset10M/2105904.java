package ffmpeg;

/**
 * Objects of Medium represent an convertable ffmpeg-medium
 * @author SebastianWe
 */
public class Medium {

    private Codec audioCodec;

    private Codec videoCodec;

    private String path;

    private String colorSpace;

    private String aspectRatio;

    private String scala;

    private String rate;

    private String mrate;

    private String aRate;

    private String aFreq;

    private String aChannel;

    /**
	 * Creates a new Medium
	 * @param audioCodec
	 * 						Codec that can handle audio
	 * @param videoCodec
	 * 						Codec that can handle video
	 * @param path
	 * 						Path to medium
	 * @param colorSpace
	 * 						Colorspace of the medium
	 * @param aspectRatio
	 * 						Aspect ratio of the medium
	 * @param scala
	 * 						Scala of the medium
	 * @param rate
	 * 						Bitrate of the video stream
	 * @param mrate
	 * 						Medium bitrate
	 * @param aRate
	 * 						Bitrate of the audio stream
	 * @param aFreq
	 * 						Frequency of the audio stream
	 * @param aChannel
	 * 						Channels in audio stream
	 */
    public Medium(Codec audioCodec, Codec videoCodec, String path, String colorSpace, String aspectRatio, String scala, String rate, String mrate, String aRate, String aFreq, String aChannel) {
        this.audioCodec = audioCodec;
        this.videoCodec = videoCodec;
        this.path = path;
        this.colorSpace = colorSpace;
        this.aspectRatio = aspectRatio;
        this.scala = scala;
        this.rate = rate;
        this.mrate = mrate;
        this.aRate = aRate;
        this.aFreq = aFreq;
        this.aChannel = aChannel;
    }

    /**
	 * Get Audio Bitrate
	 * @return aRate
	 */
    public String getARate() {
        return aRate;
    }

    /**
	 * Get Audio Frequence
	 * @return aFreq
	 */
    public String getAFreq() {
        return aFreq;
    }

    /**
	 * Get Audio Channels
	 * @return aChannel
	 */
    public String getAChannel() {
        return aChannel;
    }

    /**
	 * Get Medium Bitrate
	 * @return mrate
	 */
    public String getMRate() {
        return mrate;
    }

    /**
	 * Get Scala
	 * @return scala
	 */
    public String getScala() {
        return scala;
    }

    /**
	 * Get colorspace
	 * @return colorSpace
	 */
    public String getColorSpace() {
        return colorSpace;
    }

    /**
	 * Get aspect ratio
	 * @return aspectRatio
	 */
    public String getAspectRatio() {
        return aspectRatio;
    }

    /**
	 * Get medium path
	 * @return path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * Get audio codec
	 * @return audioCodec
	 */
    public Codec getAudioCodec() {
        return audioCodec;
    }

    /**
	 * Get video codec
	 * @return videoCodec
	 */
    public Codec getVideoCodec() {
        return videoCodec;
    }

    /**
	 * Get the video rate
	 * @return rate
	 */
    public String getRate() {
        return rate;
    }
}
