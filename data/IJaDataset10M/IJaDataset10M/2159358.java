package net.sourceforge.jnipp.proxyGen;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.common.FormattedFileWriter;
import net.sourceforge.jnipp.project.ProxyGenSettings;
import java.io.File;
import java.util.Iterator;

/**
 * C++ proxy class forward declaration header file generator.
 *
 * This class is responsible for generating the C++ proxy forward declaration header
 * file for a specified Java class.  The specifics of the code generation process are
 * guided by the settings supplied in the call to the <code>generate()</code>
 * method.
 *
 * @author $Author: ptrewhella $
 * @version $Revision: 1.11 $
 */
public class CPPProxyForwardHeaderGenerator {

    /**
	 * Settings specifying the code generation options.
	 *
	 * This reference points to the <code>ProxyGenSettings</code> instance passed
	 * into the <code>generate()</code> method and is used by various methods to
	 * guide the code generation.
	 *
	 * @see #generate
	 */
    private ProxyGenSettings proxyGenSettings = null;

    public CPPProxyForwardHeaderGenerator() {
    }

    public void generate(ClassNode root, ProxyGenSettings proxyGenSettings) throws java.io.IOException {
        if (root.isPrimitive() == true || root.needsProxy() == false) return;
        this.proxyGenSettings = proxyGenSettings;
        String fullFileName = proxyGenSettings.getProject().getCPPOutputDir() + File.separatorChar;
        if (root.getPackageName().equals("") == true) fullFileName += root.getCPPClassName() + "ProxyForward.h"; else fullFileName += root.getPackageName().replace('.', File.separatorChar) + File.separatorChar + root.getCPPClassName() + "ProxyForward.h";
        FormattedFileWriter writer = new FormattedFileWriter(fullFileName, true);
        String def = "__" + root.getPackageName().replace('.', '_') + "_" + root.getCPPClassName() + "ProxyForward_H";
        writer.outputLine("#ifndef " + def);
        writer.outputLine("#define " + def);
        writer.newLine(2);
        Iterator it = root.getNamespaceElements();
        while (it.hasNext() == true) {
            String current = (String) it.next();
            writer.outputLine("namespace " + current);
            writer.outputLine("{");
            writer.incTabLevel();
        }
        writer.outputLine("class " + root.getCPPClassName() + "Proxy;");
        for (it = root.getNamespaceElements(); it.hasNext() == true; it.next()) {
            writer.decTabLevel();
            writer.outputLine("}");
        }
        writer.newLine(2);
        writer.outputLine("#endif");
        writer.flush();
        writer.close();
    }
}
