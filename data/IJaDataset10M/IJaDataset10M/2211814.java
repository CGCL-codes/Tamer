package jmelib.codegen;

import jmelib.codegen.project.*;
import jmelib.codegen.processor.Processor;
import jmelib.codegen.processor.ProcessorFactory;
import jmelib.codegen.processor.ProcessorFactoryImpl;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Dmitry Shyshkin
 *         Date: 31/3/2007 18:48:09
 */
public class Entry {

    private static ProcessorFactory factory = new ProcessorFactoryImpl();

    public static void main(String[] args) throws IOException {
        File inJar = null, outJar = null;
        List<File> librariesJar = new ArrayList<File>();
        int i = 0;
        while (i < args.length) {
            if ("--injar".equals(args[i])) {
                inJar = new File(args[i + 1]);
                ++i;
                ++i;
                continue;
            }
            if ("--libraryjar".equals(args[i])) {
                librariesJar.add(new File(args[i + 1]));
                ++i;
                ++i;
                continue;
            }
            if ("--outjar".equals(args[i])) {
                outJar = new File(args[i + 1]);
                ++i;
                ++i;
                continue;
            }
            throw new IllegalArgumentException(args[i]);
        }
        if (inJar == null) {
            System.err.println("--injar must be specified");
            System.exit(1);
        }
        if (outJar == null) {
            System.err.println("--outjar must be specified");
            System.exit(1);
        }
        ModuleReader reader = new JarModuleReader(inJar);
        Project project = new Project(reader.readModule());
        for (File libraryJar : librariesJar) {
            ResourceCollection resourceCollection = new JarResourceCollection(libraryJar);
            project.getLibraries().add(new ClassCollection(resourceCollection));
        }
        factory.createProcessor("backport").process(project);
        factory.createProcessor("beans").process(project);
        ModuleWriter writer = new JarModuleWriter(outJar);
        writer.writeModule(project.getModule());
    }
}
