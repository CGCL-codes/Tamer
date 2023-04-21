package petrinets;

import java.util.ArrayList;
import view.AppFrame;
import data.Data;
import data.elements.Element;
import data.modeling.EmulationManager;

/**
 * Entry point of application.
 * 
 * @author <a href="mailto:sukharevd@gmail.com">Sukharev Dmitriy</a>
 * @author <a href="mailto:ydzyuban@gmail.com">Dzyuban Yuriy</a>
 * @author <a href="mailto:vixentael@gmail.com">Voitova Anastasiia</a>
 * 
 */
public final class Main {

    /**
     * Data of the application.
     */
    private static Data data = new Data(new ArrayList<Element>());

    /**
     * Emulation manager of application, it emulates the work of petri-net. 
     */
    private static EmulationManager emulator = new EmulationManager();

    /**
     * Frame of the application.
     */
    private static AppFrame appFrame = new AppFrame(data, emulator);

    /**
     * Starts GUI shell.
     * 
     * @param args
     *            the args of cmd line
     */
    public static synchronized void main(final String[] args) {
        appFrame.createAndShowGUI();
    }
}
