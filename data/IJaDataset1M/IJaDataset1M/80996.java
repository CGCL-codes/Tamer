package net.sourceforge.appgen.generator;

import java.io.File;
import net.sourceforge.appgen.model.Entity;
import net.sourceforge.appgen.model.GenerationInformation;

/**
 * @author Byeongkil Woo
 */
public abstract class DirectoryGenerator extends OnceFileGenerator {

    public DirectoryGenerator(GenerationInformation generationInformation) {
        super(generationInformation);
    }

    @Override
    public File generate(Entity entity) throws Exception {
        if (alreadyGenerated) {
            return null;
        }
        File dir = getDirectory();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        alreadyGenerated = true;
        return dir;
    }

    @Override
    public File getFile(Entity entity) {
        return getDirectory();
    }

    @Override
    public String getTemplate() {
        return null;
    }
}
