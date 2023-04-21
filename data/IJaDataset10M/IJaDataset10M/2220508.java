package net.sourceforge.appgen.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import net.sourceforge.appgen.model.Entity;
import net.sourceforge.appgen.model.GenerationInformation;

/**
 * @author Byeongkil Woo
 */
public class IndexFileGenerator extends OnceFileGenerator {

    public static final String TEMPLATE = "index.vm";

    private List<Entity> entityList;

    private boolean aleradyGenerate = false;

    public IndexFileGenerator(GenerationInformation generationInformation, List<Entity> entityList) {
        super(generationInformation);
        this.entityList = entityList;
    }

    @Override
    public File generateFile(Entity entity, String template, File file) throws Exception {
        Velocity.init(properties);
        Template modelTemplate = Velocity.getTemplate(template);
        VelocityContext context = new VelocityContext();
        context.put("entityList", entityList);
        context.put("packageName", packageName);
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            modelTemplate.merge(context, writer);
        } catch (Exception e) {
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return file;
    }

    @Override
    public File generate(Entity entity) throws Exception {
        if (!aleradyGenerate) {
            aleradyGenerate = true;
            return this.generateFile(entity, getTemplate(), getFile(entity));
        }
        return null;
    }

    @Override
    public File getFile(Entity entity) {
        return new File(getDirectory(), "index.html");
    }

    @Override
    public File getDirectory() {
        return new File(outputDir.getPath() + File.separator + "WebContent");
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }
}
