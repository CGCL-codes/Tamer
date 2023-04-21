package soundengine;

public class SoundUnitConverterManager {

    private static final SoundUnitConverterManager manager = new SoundUnitConverterManager();

    private SoundUnitConverterDouble m_converterDouble = null;

    private SoundUnitConverterManager() {
    }

    public static void initialise() {
        initialise(0);
    }

    public static void initialise(int maxSoundFileAmplitude) {
        manager.m_converterDouble = new SoundUnitConverterDouble(maxSoundFileAmplitude);
    }

    public static Number pitchToFrequency(Number pitch) {
        return manager.m_converterDouble.pitchToFrequency(pitch.doubleValue());
    }

    public static Number frequencyToPitch(Number frequency) {
        return manager.m_converterDouble.frequencyToPitch(frequency.doubleValue());
    }

    public static Number volumeToAmplitude(Number volume) {
        return manager.m_converterDouble.volumeToAmplitude(volume.doubleValue());
    }

    public static Number amplitudeToVolume(Number soundFileAmplitude) {
        return manager.m_converterDouble.amplitudeToVolume(soundFileAmplitude.doubleValue());
    }
}
