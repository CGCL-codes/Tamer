package com.panopset.flywheel;

import java.io.StringWriter;

/**
 * <h5>p - Push</h5>
 * 
 * <pre>
 * 
 * ${&#064p name}Joe${&#064q}
 * </pre>
 * 
 * Everything following this command is pushed into a String buffer, until a q
 * command is reached. 
 * @author Karl Dinwiddie
 *
 */
public class CommandPush extends MatchableCommand implements UserMatchableCommand {

    public static String getShortHTMLText() {
        return "${&#064;p name}";
    }

    public CommandPush(String source, String innerPiece, Template template) {
        super(source, innerPiece, template);
    }

    @Override
    public void resolve(StringWriter notUsed) {
        StringWriter sw = new StringWriter();
        super.resolve(sw);
        Flywheel.getFlywheel().put(getParams(), sw.toString());
    }
}
