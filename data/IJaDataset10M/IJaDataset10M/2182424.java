package net.sf.javascribe;

import java.io.File;
import net.sf.javascribe.appdef.JavaScribeApplicationDefinitionReader;
import net.sf.javascribe.appdef.JavaScribeApplication;
import net.sf.javascribe.appdef.JavaScribeDesignDefinitionReader;
import net.sf.javascribe.appdef.wrapper.design.DesignDefinitionWrapper;
import net.sf.javascribe.generator.ApplicationBuilder;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavaScribe {

    public void processApplication(File designFile, File applicationFile) throws ProcessingException {
        JavaScribeApplicationDefinitionReader appReader = null;
        DesignDefinitionWrapper design = null;
        ApplicationBuilder builder = null;
        JavaScribeApplication app = null;
        appReader = new JavaScribeApplicationDefinitionReader();
        builder = new ApplicationBuilder();
        design = JavaScribeDesignDefinitionReader.readApplicationDesign(designFile);
        app = appReader.readApplicationDefinition(applicationFile, design);
        builder.buildApplication(app);
    }
}
