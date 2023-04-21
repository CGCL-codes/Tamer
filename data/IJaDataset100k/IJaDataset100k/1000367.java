package edu.cmu.sphinx.research.distributed.server;

import edu.cmu.sphinx.frontend.Cepstrum;
import edu.cmu.sphinx.frontend.CepstrumSource;
import edu.cmu.sphinx.frontend.Signal;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Reads cepstral data from a Socket, and returns them as Cepstrum
 * objects.
 */
public class SocketCepstrumSource implements CepstrumSource {

    private DataInputStream dataReader;

    private static final float UTTERANCE_START = Float.MAX_VALUE;

    private static final float UTTERANCE_END = Float.MIN_VALUE;

    private int cepstrumLength = 13;

    private boolean inUtterance;

    /**
     * Constructs a default SocketCepstrumSource from a Socket.
     *
     * @param socket the Socket from which cepstra data is read
     */
    public SocketCepstrumSource(Socket socket) throws IOException {
        inUtterance = false;
        this.dataReader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    /**
     * Returns the next Cepstrum object produced by this SocketCepstrumSource.
     * The Cepstra objects of an Utterance should be preceded by
     * a Cepstrum object with Signal.UTTERANCE_START and ended by
     * a Cepstrum object with Signal.UTTERANCE_END.
     *
     * @return the next available Cepstrum object, returns null if no
     *     Cepstrum object is available
     *
     * @throws java.io.IOException
     */
    public Cepstrum getCepstrum() throws IOException {
        float firstValue = dataReader.readFloat();
        if (!inUtterance) {
            if (firstValue == UTTERANCE_START) {
                inUtterance = true;
                return (new Cepstrum(Signal.UTTERANCE_START));
            } else {
                throw new IllegalStateException("No UTTERANCE_START read from socket: " + firstValue + ", while UTTERANCE_START is " + UTTERANCE_START);
            }
        } else {
            if (firstValue == UTTERANCE_END) {
                inUtterance = false;
                return (new Cepstrum(Signal.UTTERANCE_END));
            } else if (firstValue == UTTERANCE_START) {
                throw new IllegalStateException("Too many UTTERANCE_STARTs.");
            } else {
                float[] data = new float[cepstrumLength];
                data[0] = firstValue;
                for (int i = 1; i < cepstrumLength; i++) {
                    data[i] = dataReader.readFloat();
                }
                return (new Cepstrum(data));
            }
        }
    }
}
