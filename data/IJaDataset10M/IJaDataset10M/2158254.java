package net.sourceforge.appgen.generator;

import java.io.File;
import net.sourceforge.appgen.model.Entity;
import net.sourceforge.appgen.model.GenerationInformation;
import net.sourceforge.appgen.util.FileUtils;

/**
 * @author Byeongkil Woo
 */
public class PomFileGenerator extends OnceFileGenerator {

    public static final String TEMPLATE = "pom.xml";

    public PomFileGenerator(GenerationInformation generationInformation) {
        super(generationInformation);
    }

    public File generateFile(Entity entity, File in, File out) throws Exception {
        FileUtils.copy(in, out);
        return out;
    }

    @Override
    public File generate(Entity entity) throws Exception {
        if (!alreadyGenerated) {
            alreadyGenerated = true;
            return this.generateFile(entity, new File(templateDir.getPath(), TEMPLATE), getFile(entity));
        }
        return null;
    }

    @Override
    public boolean existFile(Entity entity) {
        if (!alreadyGenerated) {
            return super.existFile(entity);
        }
        return false;
    }

    @Override
    public File getFile(Entity entity) {
        return new File(getDirectory(), TEMPLATE);
    }

    @Override
    public File getDirectory() {
        return new File(outputDir.getPath());
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }
}
