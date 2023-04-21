package org.lightmtv.generator.util;

import java.io.File;
import org.lightcommons.util.StringUtils;

public class ModelGenrator extends ProjectGenerator {

    private String modelName;

    private String subPackage;

    private String baseModelPath;

    private String generateRoot;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSubPackage() {
        if (subPackage == null) return "";
        if (subPackage.startsWith(".")) return subPackage.substring(1);
        return subPackage;
    }

    public void setSubPackage(String subPackage) {
        this.subPackage = subPackage;
    }

    public String getSubPackagePath() {
        return getSubPackageDot().replace('.', '/');
    }

    public String getModelNameUncap() {
        return StringUtils.uncapitalize(modelName);
    }

    public String getModelNameCap() {
        return StringUtils.capitalize(modelName);
    }

    public String getModelNameLower() {
        return modelName.toLowerCase();
    }

    public String getModelNameUpper() {
        return modelName.toUpperCase();
    }

    public String getBaseModelPath() {
        return baseModelPath;
    }

    public void setBaseModelPath(String modelPath) {
        this.baseModelPath = modelPath;
    }

    public String getGenerateRoot() {
        return generateRoot;
    }

    public void setGenerateRoot(String generateRoot) {
        this.generateRoot = generateRoot;
    }

    public String getAbsoluteBaseModelPath() {
        return StringUtils.cleanDirPath(getBaseDir() + "/" + baseModelPath);
    }

    public File getModelFile() {
        return new File(getBaseDir() + "/" + getBaseModelPath() + "/" + getSubPackagePath() + "/" + getModelName() + ".java");
    }

    public String getSubPackageDot() {
        if (StringUtils.hasText(subPackage) && !subPackage.startsWith(".")) return "." + subPackage; else if (subPackage == null) return "";
        return this.subPackage;
    }

    public String getSubPackageUnder() {
        return getSubPackageDot().replace('.', '_');
    }

    public boolean exists() {
        return getModelFile().exists();
    }

    public void generate(String templateName, boolean overwrite) throws Exception {
        String info = "generating " + templateName + " of " + getSubPackage() + "." + getModelName();
        System.out.println(info);
        getLog().info(info);
        GenerateUtils.generateModel(getBaseDir() + "/" + generateRoot + "/" + templateName, this, overwrite);
    }
}
