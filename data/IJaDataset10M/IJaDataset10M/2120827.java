package com.sun.persistence.tools.apt;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;
import javax.persistence.Entity;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * This is the actual annotation processor. It gets created by
 * {@link ListPersistenceClassApf}.
 *
 * @author Sanjeeb.Sahoo@Sun.COM
 */
class ListPersistenceClassAp implements AnnotationProcessor {

    private AnnotationProcessorEnvironment env;

    /**
     * Represents value for -Add command line option.
     */
    private String inputDDPath = "persistence.xml";

    /**
     * Represents value for -Apu command line option.
     */
    private String puName;

    /**
     * Inmemory representation of input XML DD.
     */
    private PersistenceDescriptor dd;

    /**
     * The persistence-unit node of the dd XML graph
     */
    private PersistenceDescriptor.PersistenceUnit pu;

    public ListPersistenceClassAp(AnnotationProcessorEnvironment env) {
        this.env = env;
        parseOptions();
        readDD();
    }

    public void process() {
        try {
            for (TypeDeclaration decl : env.getSpecifiedTypeDeclarations()) {
                decl.accept(new ListPersistenceClassVisitor());
            }
            PrintWriter out = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, "", new File("META-INF", "persistence.xml"), null);
            new XMLReaderWriter().write(dd, out);
            out.flush();
            env.getMessager().printNotice("New DD is available at " + "META-INF/persistence.xml in same dir where classes are generated.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This class is responsible for writing out class name of each of
     * the managed persistence class. A class is called managed persistence class
     * if it is annotated as {@link Entity} or {@link Embeddable} or
     * {@link EmbeddableSuperclass}.
     */
    class ListPersistenceClassVisitor extends SimpleDeclarationVisitor {

        public void visitClassDeclaration(ClassDeclaration cd) {
            if (cd.getAnnotation(Entity.class) != null || cd.getAnnotation(Embeddable.class) != null || cd.getAnnotation(MappedSuperclass.class) != null) {
                final String className = cd.getQualifiedName();
                env.getMessager().printNotice(className);
                List<String> pcClassNames = pu.getPersistenceCapableClass();
                if (pcClassNames.contains(className)) {
                    env.getMessager().printNotice("input persistence.xml already contains " + className);
                } else {
                    pcClassNames.add(className);
                }
            }
        }
    }

    /**
     * Parse command line arguments supplied.
     */
    private void parseOptions() {
        Map<String, String> options = env.getOptions();
        for (String key : options.keySet()) {
            env.getMessager().printNotice(key + "=>" + options.get(key));
            final String prefixForDDOption = "-Add=";
            final String prefixForPUOption = "-Apu=";
            if (key.startsWith(prefixForDDOption)) {
                inputDDPath = key.substring(prefixForDDOption.length());
            }
            if (key.startsWith(prefixForPUOption)) {
                puName = key.substring(prefixForPUOption.length());
            }
        }
        env.getMessager().printNotice("XML Deployment Descriptor path = " + inputDDPath);
        if (puName != null) {
            env.getMessager().printNotice("Persistence Unit name = " + inputDDPath);
        }
    }

    /**
     * Read the input persistence.xml and initialise the inmemory structure.
     */
    private void readDD() {
        try {
            dd = new XMLReaderWriter().read(new FileInputStream(new File(inputDDPath).getAbsoluteFile()));
            final List<PersistenceDescriptor.PersistenceUnit> pUnits = dd.getPersistenceUnit();
            if (pUnits.size() == 1) {
                pu = pUnits.get(0);
            } else if (pUnits.size() == 0) {
                throw new RuntimeException("No persistence-unit" + " found in persistence.xml.");
            } else if (pUnits.size() > 1) {
                for (PersistenceDescriptor.PersistenceUnit pUnit : pUnits) {
                    if (pUnit.getName().equals(puName)) {
                        pu = pUnit;
                        break;
                    }
                }
                if (pu == null) {
                    throw new RuntimeException(pUnits.size() + " no. of " + "persistence-units. Specify which one to use " + "using -Apu option.");
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
