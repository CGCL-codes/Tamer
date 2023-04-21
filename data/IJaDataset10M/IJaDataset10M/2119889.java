package mediathek.io.starter;

import java.util.EventListener;

public interface RuntimeListener extends EventListener {

    void starter(RuntimeExecEvent e);
}
