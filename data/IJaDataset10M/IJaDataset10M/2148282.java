package jmelib.codegen.project;

import java.io.IOException;

/**
 * @author Dmitry Shyshkin
 *         Date: 3/4/2007 19:35:33
 */
public interface ModuleWriter {

    void writeModule(Bundle module) throws IOException;
}
