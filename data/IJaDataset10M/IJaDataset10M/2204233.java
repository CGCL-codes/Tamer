package pl.wcislo.sbql4j.tools.doclets.internal.toolkit.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import pl.wcislo.sbql4j.javadoc.PackageDoc;
import pl.wcislo.sbql4j.javadoc.RootDoc;
import pl.wcislo.sbql4j.tools.doclets.internal.toolkit.Configuration;

/**
 * Write out the package index.
 *
 * This code is not part of an API.
 * It is implementation that is subject to change.
 * Do not use it as an API
 *
 * @see pl.wcislo.sbql4j.javadoc.PackageDoc
 * @author Atul M Dambalkar
 */
public class PackageListWriter extends PrintWriter {

    private Configuration configuration;

    /**
     * Constructor.
     *
     * @param configuration the current configuration of the doclet.
     */
    public PackageListWriter(Configuration configuration) throws IOException {
        super(Util.genWriter(configuration, configuration.destDirName, DocletConstants.PACKAGE_LIST_FILE_NAME, configuration.docencoding));
        this.configuration = configuration;
    }

    /**
     * Generate the package index.
     *
     * @param configuration the current configuration of the doclet.
     * @throws DocletAbortException
     */
    public static void generate(Configuration configuration) {
        PackageListWriter packgen;
        try {
            packgen = new PackageListWriter(configuration);
            packgen.generatePackageListFile(configuration.root);
            packgen.close();
        } catch (IOException exc) {
            configuration.message.error("doclet.exception_encountered", exc.toString(), DocletConstants.PACKAGE_LIST_FILE_NAME);
            throw new DocletAbortException();
        }
    }

    protected void generatePackageListFile(RootDoc root) {
        PackageDoc[] packages = configuration.packages;
        String[] names = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            names[i] = packages[i].name();
        }
        Arrays.sort(names);
        for (int i = 0; i < packages.length; i++) {
            println(names[i]);
        }
    }
}
