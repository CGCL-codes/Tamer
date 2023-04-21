package ibex.runtime;

import java.io.IOException;

public interface IStringParser extends IParser {

    String scan() throws IOException;
}
