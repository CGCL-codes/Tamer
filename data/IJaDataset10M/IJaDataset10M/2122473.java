package jasci.terminal;

import java.io.IOException;
import jasci.util.Color;

public interface Terminal {

    public void setColor(Color color) throws IOException;

    public void setPosition(int x, int y) throws IOException;

    public void put(char c) throws IOException;

    public void processInput(TerminalInputHandler handler) throws IOException;

    public void clear() throws IOException;

    public void flush() throws IOException;
}
