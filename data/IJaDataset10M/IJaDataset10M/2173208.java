package ch.amotta.qweely.wave.chunks;

/**
 *
 * @author Alessandro
 */
public class WAVEDataChunk extends WAVEChunk {

    private int _numbChannels;

    private int _byteDepth;

    private long _numbSamples;

    private long[][] _channels;

    public WAVEDataChunk(int numbChannels, int bitDepth) {
        super("data");
        _numbChannels = numbChannels;
        _byteDepth = (int) Math.ceil(bitDepth * 0.125);
    }

    public void parseChunkBody() {
        _numbSamples = _length / (_numbChannels * _byteDepth);
        if (_numbSamples > Integer.MAX_VALUE) System.out.println("WAVEDataChunk.parseChunk: Die Datei beinhaltet zu viele Samples");
        _channels = new long[_numbChannels][];
        for (int i = 0; i < _numbChannels; i++) {
            _channels[i] = new long[(int) _numbSamples];
        }
        for (int currentSample = 0; currentSample < _numbSamples; currentSample++) {
            for (int currentChannel = 0; currentChannel < _numbChannels; currentChannel++) {
                _channels[currentChannel][currentSample] = _stream.readData();
            }
        }
    }

    public long[][] getChannels() {
        return _channels;
    }
}
