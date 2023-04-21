package org.ximtec.igesture.app.helloworld;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sigtec.ink.Note;
import org.sigtec.ink.Point;
import org.ximtec.igesture.Recogniser;
import org.ximtec.igesture.algorithm.AlgorithmException;
import org.ximtec.igesture.algorithm.siger.SigerAlgorithm;
import org.ximtec.igesture.configuration.Configuration;
import org.ximtec.igesture.core.Gesture;
import org.ximtec.igesture.core.GestureClass;
import org.ximtec.igesture.core.GestureSet;
import org.ximtec.igesture.core.ResultSet;
import org.ximtec.igesture.core.TextDescriptor;
import org.ximtec.igesture.io.GestureDevice;
import org.ximtec.igesture.io.GestureEventListener;
import org.ximtec.igesture.io.mouseclient.MouseReader;

/**
 * @version 1.0 Nov 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class HelloWorld implements GestureEventListener {

    private static final Logger LOGGER = Logger.getLogger(HelloWorld.class.getName());

    private static final String INITIALISED = "Initialised.";

    private static final String NOT_RECOGNISED = "Not recognised.";

    private Recogniser recogniser;

    private GestureDevice<Note, Point> client;

    public HelloWorld() throws AlgorithmException {
        init();
        LOGGER.log(Level.INFO, INITIALISED);
    }

    private void init() throws AlgorithmException {
        GestureClass leftRightLine = new GestureClass("LeftRight");
        leftRightLine.addDescriptor(new TextDescriptor("E"));
        GestureClass downRight = new GestureClass("DownRight");
        downRight.addDescriptor(new TextDescriptor("S,E"));
        GestureClass upLeft = new GestureClass("UpLeft");
        upLeft.addDescriptor(new TextDescriptor("N,W"));
        GestureSet gestureSet = new GestureSet("GestureSet");
        gestureSet.addGestureClass(leftRightLine);
        gestureSet.addGestureClass(upLeft);
        gestureSet.addGestureClass(downRight);
        Configuration configuration = new Configuration();
        configuration.addGestureSet(gestureSet);
        configuration.addAlgorithm(SigerAlgorithm.class.getName());
        recogniser = new Recogniser(configuration);
        client = new MouseReader();
        client.init();
        client.addGestureHandler(this);
    }

    @Override
    public void handleChunks(List<?> chunks) {
        LOGGER.log(Level.INFO, "Not implemented");
    }

    @Override
    public void handleGesture(Gesture<?> gesture) {
        if (gesture.getGesture() instanceof Note) {
            Note note = (Note) gesture.getGesture();
            ResultSet result = recogniser.recognise(note);
            if (result.isEmpty()) {
                LOGGER.log(Level.INFO, NOT_RECOGNISED);
            } else {
                LOGGER.log(Level.INFO, result.getResult().getGestureClassName());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new HelloWorld();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
