package net.sf.pged.pmanager.format;

import java.io.IOException;
import java.io.InputStream;
import net.sf.pged.pmanager.Plugin;
import net.sf.pged.project.GraphProject;

/**
 * @author dude03
 *
 */
public interface ImportFormatPlugin extends Plugin {

    public GraphProject importProject(InputStream in) throws IOException;
}
