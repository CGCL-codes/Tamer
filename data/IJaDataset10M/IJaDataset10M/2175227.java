package ch.sahits.codegen.java.generator.jettemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import ch.sahits.codegen.extensions.JETemplate;
import ch.sahits.codegen.generator.JETGateway;
import ch.sahits.codegen.i18n.JavaMessages;
import ch.sahits.codegen.java.model.util.ConvenientCodegen;
import ch.sahits.codegen.model.IConfigurationBuilder;
import ch.sahits.codegen.model.IJetGatewayConfiguration;
import ch.sahits.codegen.model.ImmutableObjectFactory;
import ch.sahits.model.IBuildable;
import ch.sahits.model.ModelBuilderFactory;
import ch.sahits.model.java.IGeneratedJavaDBClass2;
import ch.sahits.model.java.IGeneratedJavaDBClassBuilder;
import ch.sahits.model.java.db.IJavaDataBaseTableBuilder;
import ch.sahits.model.java.db.IJavaDatabaseTable;

/**
 * This class provides common method to create a JET template file
 * @author Andi Hotz
 * @since 0.9.0
 */
public abstract class AbstractJETTemplateGenerator extends JETGateway {

    /**
	 * This file is the handle to the jet-template
	 */
    protected File jetTemplateFile;

    /** List with all the imports */
    private List<String> imports;

    /** Handle to write into the file */
    private BufferedWriter writer;

    /** Flag indicating if the directive was written */
    private boolean jetDirectiveWritten = false;

    /** String that holds the contents of the file */
    private StringBuffer contents;

    /**
	 * Constructor that passes the call to the super class {@link JETGateway}
	 */
    public AbstractJETTemplateGenerator() {
        super();
    }

    /**
	 * Initialize the configuration and generator
	 */
    @Override
    public void init(IJetGatewayConfiguration config) {
        super.init(config);
        try {
            initGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Initialize the internal structure
	 * @throws IOException
	 */
    private void initGenerator() throws IOException {
        String dirname = System.getProperty("java.io.tmpdir");
        Random rand = new Random();
        jetTemplateFile = new File(dirname + File.separator + "jetTemplate" + rand.nextInt() + ".javajet");
        writer = new BufferedWriter(new FileWriter(jetTemplateFile));
        contents = new StringBuffer();
        imports = new Vector<String>();
    }

    /**
	 * Execute the cleanup defined in the super class. Then delete the temporary file.
	 * @see ch.sahits.codegen.generator.JETGateway#cleanup()
	 */
    @Override
    public void cleanup() throws CoreException {
        super.cleanup();
        if (jetTemplateFile.exists() && jetTemplateFile.isFile()) {
            if (!jetTemplateFile.delete()) {
                jetTemplateFile.deleteOnExit();
            }
        }
    }

    /**
	 * Add an import directive to the list
	 * @param imp class or package to be imported
	 */
    protected final void addImport(String imp) {
        imports.add(imp);
    }

    /**
	 * Append a string to the jetemplate and add a line break
	 * @param line contents of the line.
	 */
    protected final void appendLn(String line) {
        contents.append(line + "\n");
    }

    /**
	 * Append a string to the jetemplate
	 * @param s string to be appended
	 */
    protected final void append(String s) {
        contents.append(s);
    }

    /**
	 * Write the jet directive as the first line of the jetemplate.
	 * This can be done at any time of the creation process.
	 */
    @SuppressWarnings("unchecked")
    protected final void writeDirective() {
        String s = "<%@ jet imports=\"";
        for (Iterator iterator = imports.iterator(); iterator.hasNext(); ) {
            String imp = (String) iterator.next();
            s += imp + " ";
        }
        s = s.trim() + "\"";
        s += "package=\"ch.sahits.codegen.java.jet\" class=\"" + ConvenientCodegen.getSimpleClassName(getClass()) + "Generator\" %>\n";
        if (!jetDirectiveWritten) {
            jetDirectiveWritten = true;
            contents.insert(0, s);
        } else {
            contents.replace(0, contents.indexOf("\n"), s);
        }
    }

    /**
	 * Finalize the writing process. Flush the contents of the writer into
	 * the file and close the writer
	 * @throws IOException
	 */
    private void flush() throws IOException {
        writer.write(contents.toString());
        writer.flush();
        writer.close();
    }

    /**
	 * Add all imports
	 */
    protected abstract void addImports();

    /**
	 * Create the template file
	 */
    protected abstract void generateTemplate();

    /**
	 * This generation method creates a jetemplate and then calls onto the generation of the super class
	 * @see ch.sahits.codegen.generator.JETGateway#generate(org.eclipse.core.runtime.IProgressMonitor)
	 */
    @Override
    public String generate(IProgressMonitor monitor) throws CoreException {
        if (!isInitialized()) {
            throw new RuntimeException(JavaMessages.AbstractJETTemplateGenerator_11);
        }
        addImports();
        generateTemplate();
        IGeneratedJavaDBClassBuilder b = (IGeneratedJavaDBClassBuilder) ModelBuilderFactory.newCrossBuilder((IBuildable) getConfig().getModel());
        Object m = getConfig().getModel();
        assert m instanceof IGeneratedJavaDBClass2 || m instanceof IJavaDatabaseTable;
        if (m instanceof IJavaDatabaseTable) ((IJavaDataBaseTableBuilder) b).databaseTable((IJavaDatabaseTable) m); else b.databaseTable((IGeneratedJavaDBClass2) m);
        b.jeTemplatePath(jetTemplateFile.getAbsolutePath());
        IConfigurationBuilder configB = (IConfigurationBuilder) ImmutableObjectFactory.newBuilder(IConfigurationBuilder.class);
        configB.model(b.build()).packageName(getConfig().getPackageName()).targetFile(getConfig().getTargetFile());
        configB.targetFolder(getConfig().getTargetFolder()).project(getConfig().getProject());
        configB.bundledRecource(false).classpathVariable(getConfig().getClasspathVariable()).pluginId(getConfig().getPluginId());
        configB.templateRelativeUri(getConfig().getTemplateRelativeUri());
        setConfig(configB.build());
        try {
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.generate(monitor);
    }

    /**
	 * Add the jetemplate plugin id to the builder
	 * @param builder Configuration builder
	 * @param jetTempPath template pathe of the jetemplate
	 * @since 2.1.0
	 */
    protected final void addJetPluginID2Builder(IConfigurationBuilder builder, String jetTempPath) {
        Map<String, String> jets = JETemplate.getJetemplate4DisplaySelection();
        for (Entry<String, String> e : jets.entrySet()) {
            String key = e.getKey();
            if (key.endsWith(jetTempPath)) {
                builder.pluginIdJetemplate(key.split("@")[0]);
                return;
            }
        }
    }
}
