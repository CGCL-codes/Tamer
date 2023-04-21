package doolin.jarfinder.engine;

import net.sf.doolin.bus.SimpleMessage;

public class JarNameMessage extends SimpleMessage<String> {

    public JarNameMessage(String parameter) {
        super(parameter);
    }
}
