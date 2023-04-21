package ch.unizh.ini.jaer.projects.opticalflow.io;

import ch.unizh.ini.jaer.projects.opticalflow.*;
import java.io.*;
import java.util.logging.*;

/**
 * An output stream for motion data. Can be used to output motion data to a file or network socket using serilization.
 
 * @author tobi
 */
public class MotionOutputStream extends DataOutputStream {

    private static Logger log = Logger.getLogger("MotionOutputStream");

    /** Creates a new instance of MotionOutputStream */
    public MotionOutputStream(OutputStream os) throws IOException {
        super(os);
    }

    /**
     * Writes the serialized frame of motion data to the stream.
     *@param data a single MotionData frame
     */
    public void writeData(MotionData data) throws IOException {
        data.write(this);
    }
}
