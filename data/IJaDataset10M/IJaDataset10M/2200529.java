package src;

import java.io.File;
import java.util.Scanner;
import src.game.GameWorld;
import src.game.SoundThread;
import src.gui.SplashFrame;

/**
 * Main class for the game.  Calls a new CreatePlayerFrame.
 * @author Darren Watts
 * date 11/10/07
 *
 */
public class NitsLoch {

    /**
	 * Main program.  Prints the GPL message and then calls the CreatePlayerFrame.
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-debug")) src.Constants.SCENARIO_DEBUG = true;
        }
        String GPLmessage = "NitsLoch v" + Constants.getVersion() + " Copyright (C) 2007-2008 Darren Watts and Jonathan Irons\n" + "This program comes with ABSOLUTELY NO WARRANTY;\n" + "This is free software, and you are welcome to redistribute it\n" + "under certain conditions; see the included COPYING.txt in the docs directory.\n";
        System.out.println(GPLmessage);
        try {
            Scanner scan = new Scanner(new File("ScenarioFile"));
            String scenarioFile = scan.nextLine();
            src.scenario.MiscScenarioData.SCENARIO_FILE = scenarioFile;
        } catch (Exception ex) {
            System.err.println("A file pointing to the current scenario is missing.\n" + "This should be named ScenarioFile and should have a line of text\n" + "with the path to your scenario .nits file.");
        }
        SplashFrame splash = new SplashFrame();
        new src.scenario.loader.ScenarioLoader();
        GameWorld.getInstance().loadMapFromFile();
        splash.dispose();
        new src.gui.CreatePlayerFrame();
        SoundThread.getInstance().run();
    }
}
