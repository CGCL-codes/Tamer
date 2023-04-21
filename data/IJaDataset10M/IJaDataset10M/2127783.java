package net.sf.orcc.backends.xlim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sf.orcc.backends.TemplateGroupLoader;
import net.sf.orcc.network.Network;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * This class defines a C network printer.
 * 
 * @author Matthieu Wipliez
 * @author Herve Yviquel
 * 
 */
public class XlimCMakePrinter {

    private STGroup group;

    /**
	 * Creates a new CMake printer with the template "CMakeLists".
	 * 
	 * @throws IOException
	 *             if the template file could not be read
	 */
    public XlimCMakePrinter() throws IOException {
        group = TemplateGroupLoader.loadGroup("XLIM_CMakeLists");
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
        ST template = group.getInstanceOf("CMakeLists");
        template.add("network", network);
        String fileName = path + File.separator + "CMakeLists.txt";
        byte[] b = template.render(80).getBytes();
        OutputStream os = new FileOutputStream(fileName);
        os.write(b);
        os.close();
    }
}
