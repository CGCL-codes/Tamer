package br.usp.iterador.io;

import java.io.File;
import java.io.IOException;
import br.usp.iterador.model.Application;

/**
 * Compatibility version
 *
 * @author Guilherme Silveira
 */
public class CompatibilityVersion5 implements CompatibleVersionMaker {

    public void execute(Application data) {
    }

    public boolean shouldUpdateFile() {
        return false;
    }

    public void updateFile(File file) throws IOException {
    }
}
