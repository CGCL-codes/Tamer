package net.sf.orcc.backends.c;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sf.orcc.backends.TemplateGroupLoader;
import net.sf.orcc.network.Network;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * This class defines a C network printer.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class CMakePrinter {

    private StringTemplateGroup group;

    /**
	 * Creates a new CMake printer with the template "CMakeLists".
	 * 
	 * @throws IOException
	 *             if the template file could not be read
	 */
    public CMakePrinter() throws IOException {
        group = new TemplateGroupLoader().loadGroup("CMakeLists");
    }

    /**
	 * Prints the given network to a file whose name is given. debugFifos
	 * specifies whether debug information should be printed about FIFOs, and
	 * fifoSize is the default FIFO size.
	 * 
	 * @param path
	 *            output path
	 * @param network
	 *            the network to generate code for
	 * @throws IOException
	 *             if there is an I/O error
	 */
    public void printCMake(String path, Network network) throws IOException {
        StringTemplate template = group.getInstanceOf("CMakeLists");
        template.setAttribute("network", network);
        String fileName = path + File.separator + "CMakeLists.txt";
        byte[] b = template.toString(80).getBytes();
        OutputStream os = new FileOutputStream(fileName);
        os.write(b);
        os.close();
    }
}
