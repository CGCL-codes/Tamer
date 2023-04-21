package org.aspencloud.simple9.builder.processor;

import static org.aspencloud.simple9.util.StringUtils.tableName;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.aspencloud.simple9.builder.dom.ModelDefinition;
import org.aspencloud.simple9.builder.gen.db.SqlGenerator;
import org.aspencloud.simple9.builder.gen.src.ControllerGenerator;
import org.aspencloud.simple9.builder.gen.src.ModelGenerator;
import org.aspencloud.simple9.builder.gen.src.SourceFile;
import org.aspencloud.simple9.builder.gen.src.ViewGenerator;
import org.aspencloud.simple9.builder.views.dom.EspParser;
import org.aspencloud.simple9.server.annotations.Model;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ModelProcessor extends AbstractProcessor {

    private static final Set<String> supportedOptions = new TreeSet<String>();

    private static final Set<String> supportedTypes = new TreeSet<String>();

    static {
        supportedOptions.add("src");
        supportedOptions.add("gen");
        supportedOptions.add("ovr");
        supportedTypes.add(Model.class.getCanonicalName());
    }

    @Override
    public Set<String> getSupportedOptions() {
        return supportedOptions;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processingEnv.getOptions().containsKey("src") && processingEnv.getOptions().containsKey("gen")) {
            if (!annotations.isEmpty() && !roundEnv.getRootElements().isEmpty()) {
                List<ModelDefinition> models = new ArrayList<ModelDefinition>();
                for (Element element : roundEnv.getRootElements()) {
                    Model annotation = element.getAnnotation(Model.class);
                    if (annotation != null) {
                        String name = element.asType().toString();
                        ModelDefinition model = new ModelDefinition(name, annotation);
                        if (model.build()) {
                            models.add(model);
                        }
                    }
                }
                setOpposites(models);
                String srcPath = processingEnv.getOptions().get("src");
                String genPath = processingEnv.getOptions().get("gen");
                List<ModelDefinition> newModels = generateModelFiles(models, genPath);
                generateViewFiles(newModels, srcPath, genPath);
                generateControllerFiles(newModels, srcPath);
                generateMigrationFile(models, genPath);
            }
        }
        return true;
    }

    private void setOpposites(List<ModelDefinition> models) {
        for (ModelDefinition model : models) {
            model.setOpposites(models);
        }
    }

    private void generateControllerFiles(List<ModelDefinition> models, String path) {
        for (ModelDefinition model : models) {
            String name = model.getPackageName();
            name = name.substring(0, name.lastIndexOf('.') + 1);
            name = ((name.length() == 0) ? "controllers." : (name + ".controllers.")) + model.getSimpleName() + "Controller";
            String src = ControllerGenerator.generate(model);
            writeJavaFile(path, name, src);
        }
    }

    private void generateMigrationFile(List<ModelDefinition> models, String path) {
        String packageName = "";
        for (ModelDefinition model : models) {
            String name = model.getPackageName();
            if (!name.equals(packageName) && name.startsWith(packageName)) {
                packageName = name;
            }
        }
        String name = packageName.replace('.', File.separatorChar) + File.separator + "schema.sql";
        String src = SqlGenerator.generate(models);
        writeFile(path, name, src);
    }

    private List<ModelDefinition> generateModelFiles(List<ModelDefinition> models, String path) {
        List<ModelDefinition> newModels = new ArrayList<ModelDefinition>();
        for (ModelDefinition model : models) {
            String name = model.getCanonicalName() + "Model";
            String src = ModelGenerator.generate(model);
            if (writeJavaFile(path, name, src)) {
                newModels.add(model);
            }
        }
        return newModels;
    }

    private void generateViewFiles(List<ModelDefinition> models, String srcPath, String genPath) {
        for (ModelDefinition model : models) {
            String baseName = model.getPackageName();
            baseName = baseName.substring(0, baseName.lastIndexOf('.') + 1).replace('.', File.separatorChar);
            baseName = baseName + File.separator + "views" + File.separator + tableName(model.getSimpleName()) + File.separator;
            String name;
            String src;
            ViewGenerator gen = new ViewGenerator(model);
            name = baseName + "edit.esp";
            src = gen.generateEditView();
            writeFile(srcPath, name, src);
            EspParser parser = new EspParser(src);
            parser.parse();
            SourceFile sf = parser.createModelView(model, "EditView");
            writeJavaFile(genPath, sf.getCanonicalName(), sf.toSource());
            name = baseName + "index.esp";
            src = gen.generateIndexView();
            writeFile(srcPath, name, src);
            parser = new EspParser(src);
            parser.parse();
            sf = parser.createModelView(model, "IndexView", true);
            writeJavaFile(genPath, sf.getCanonicalName(), sf.toSource());
            name = baseName + "new.esp";
            src = gen.generateNewView();
            writeFile(srcPath, name, src);
            parser = new EspParser(src);
            parser.parse();
            sf = parser.createModelView(model, "NewView");
            writeJavaFile(genPath, sf.getCanonicalName(), sf.toSource());
            name = baseName + "show.esp";
            src = gen.generateShowView();
            writeFile(srcPath, name, src);
            parser = new EspParser(src);
            parser.parse();
            sf = parser.createModelView(model, "ShowView");
            writeJavaFile(genPath, sf.getCanonicalName(), sf.toSource());
            name = baseName + "layout.esp";
            src = gen.generateLayout();
            writeFile(srcPath, name, src);
            parser = new EspParser(src);
            parser.parse();
            sf = parser.createModelView(model, "Layout");
            writeJavaFile(genPath, sf.getCanonicalName(), sf.toSource());
        }
    }

    private boolean writeFile(String path, String fileName, String src) {
        try {
            File file = new File(path + File.separator + fileName);
            boolean isNew = !file.exists();
            if (isNew) {
                file.getParentFile().mkdirs();
            }
            PrintWriter writer = new PrintWriter(file);
            writer.write(src);
            writer.flush();
            writer.close();
            return isNew;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean writeJavaFile(String path, String fileName, String src) {
        return writeFile(path, fileName.replace('.', File.separatorChar) + ".java", src);
    }
}
