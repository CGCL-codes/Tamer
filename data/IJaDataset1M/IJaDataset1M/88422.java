package org.vesuf.tools.generator;

import ru.novosoft.mdf.mof.model.*;
import java.util.*;
import java.io.*;

/**
 *  Utility class to create generators from descriptor files.
 *
 *  @property generators	A comma seperated list of sub generator names.
 *  @property &lt;generatorname&gt;_class	The fully qualified class name of a sub generator.
 *  @property &lt;generatorname&gt;_properties	The name of the sub generators
 *    property file, relative to the property file path of this generator.
 */
public class GeneratorCreator {

    /**
	 *  Create (sub) generators from properties.
	 *  Supports empty, and (generator, file) constructor.
	 *  @param parent	The parent of the sub generators (if any).
	 *  @param properties	The properties containing the sub generator specs.
	 *  @param dir	The directory to search for properties of the sub generators.
	 *  @throw Exception When the generators could not be created.
	 */
    public static IGenerator[] createGenerators(Generator parent, Properties properties, File dir) throws Exception {
        Vector vgenerators = new Vector();
        String generators = properties.getProperty("generators", "");
        StringTokenizer stok = new StringTokenizer(generators, ",");
        while (stok.hasMoreTokens()) {
            String generator = stok.nextToken().trim();
            String clazzname = properties.getProperty(generator + "_class");
            String propsname = properties.getProperty(generator + "_properties");
            File props = null;
            if (propsname != null) {
                props = new File(dir, propsname);
            }
            Class clazz = Class.forName(clazzname);
            try {
                vgenerators.addElement(clazz.getConstructor(new Class[] { Generator.class, File.class }).newInstance(new Object[] { parent, props }));
            } catch (NoSuchMethodException e) {
                vgenerators.addElement(clazz.newInstance());
            }
        }
        IGenerator[] agenerators = new IGenerator[vgenerators.size()];
        vgenerators.copyInto(agenerators);
        return agenerators;
    }
}
