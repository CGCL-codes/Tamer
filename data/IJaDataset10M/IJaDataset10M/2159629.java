package org.apache.mina.example.tapedeck;

/**
 * Represents the <code>play</code> command.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev: 587133 $, $Date: 2008/05/29 07:04:19 $
 */
public class PlayCommand extends Command {

    public static final String NAME = "play";

    @Override
    public String getName() {
        return NAME;
    }
}
