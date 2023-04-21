package net.sourceforge.appgen.generator;

import java.io.File;
import net.sourceforge.appgen.model.AttachFilePropertyEditor;
import net.sourceforge.appgen.model.Entity;
import net.sourceforge.appgen.model.GenerationInformation;
import net.sourceforge.appgen.util.ConventionUtils;

/**
 * @author Byeongkil Woo
 */
public class AttachFilePropertyEditorFileGenerator extends OnceFileGenerator {

    public static final String TEMPLATE = "attachFilePropertyEditor.vm";

    public AttachFilePropertyEditorFileGenerator(GenerationInformation generationInformation) {
        super(generationInformation);
    }

    @Override
    public File getFile(Entity entity) {
        AttachFilePropertyEditor attachFilePropertyEditor = new AttachFilePropertyEditor(packageName);
        return new File(getDirectory(), attachFilePropertyEditor.getClassName() + ".java");
    }

    @Override
    public File getDirectory() {
        AttachFilePropertyEditor attachFilePropertyEditor = new AttachFilePropertyEditor(packageName);
        return new File(outputDir.getPath() + File.separator + "src" + File.separator + ConventionUtils.getPath(attachFilePropertyEditor.getFullPackageName()));
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }
}
