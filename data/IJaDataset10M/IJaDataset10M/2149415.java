package net.xelnaga.screplay.programs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.xelnaga.screplay.interfaces.IAction;
import net.xelnaga.screplay.interfaces.IReplay;
import net.xelnaga.screplay.loader.ReplayLoader;
import net.xelnaga.screplay.unpacker.UnpackException;

/**
 * A simple program to print the set of actions.
 *
 * @author Russell Wilson
 *
 */
public class ActionDumper {

    /**
     * Dumper [replay file]
     *
     * @param args
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws net.xelnaga.screplay.unpacker.UnpackException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, UnpackException {
        if (args.length < 1) {
            System.out.println("A replay file must be specified");
            return;
        }
        File file = new File(args[0]);
        IReplay replay = ReplayLoader.loadReplay(file);
        for (IAction action : replay.getActions()) {
            System.out.println(action.toString());
        }
    }
}
