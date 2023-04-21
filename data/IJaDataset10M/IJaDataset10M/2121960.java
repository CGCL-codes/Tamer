package annone.engine;

import java.util.Map;

public interface Constructor {

    Argument[] getArguments();

    Pointer newInstance(Map<Argument, Pointer> arguments);
}
