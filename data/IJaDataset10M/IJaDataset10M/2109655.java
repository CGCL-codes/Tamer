package org.hightides.annotations.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hightides.annotations.bean.SyncMode;
import org.hightides.annotations.filter.ProcessorFilter;
import org.hightides.annotations.param.BaseParamReader;
import org.hightides.annotations.util.CodeUtil;
import org.hightides.annotations.util.PackageUtil;
import org.opentides.util.FileUtil;
import org.opentides.util.StringUtil;

public abstract class BaseCloningProcessor {

    private static Logger _log = Logger.getLogger(BaseCloningProcessor.class);

    public static final String ENCODING = "utf-8";

    private PackageUtil packager;

    private ProcessorFilter filter;

    /**
	 * Constructor with outputFolder defaulted to same directory of model
	 * @param templatePath
	 */
    public BaseCloningProcessor(String templateFolder) {
        this(templateFolder, ".");
    }

    /**
	 * Constructor
	 * @param templatePath
	 */
    public BaseCloningProcessor(String templateFolder, String outputFolder) {
        packager = new PackageUtil(templateFolder, outputFolder);
    }

    /**
	 * Recurse all the directories under templatePath and clone all vm files.
	 */
    public abstract void recurseTemplates(File path, Map<String, Object> params);

    /**
	 * Returns location of the template
	 * @param template
	 * @return
	 */
    public abstract String getTemplateForMerge(File template);

    public void execute(Map<String, Object> params) {
        File filePath = packager.getTemplateFolder();
        recurseTemplates(filePath, params);
        updateSpringConfig(params);
        updateMessages(params);
        if (BaseParamReader.isValidation()) addValidators(params);
    }

    /**
	 * Updates the necessary spring configuration file.
	 * @param params
	 */
    private void updateSpringConfig(Map<String, Object> params) {
        SpringConfigProcessor scp = new SpringConfigProcessor();
        scp.execute(params);
    }

    /**
	 * Updates the message property file for default language.
	 * @param params
	 */
    private void updateMessages(Map<String, Object> params) {
        MessagePropertyProcessor mpp = new MessagePropertyProcessor();
        mpp.execute(params);
    }

    /**
	 * Add validation if a field is required.   
	 */
    private void addValidators(Map<String, Object> params) {
        ValidatorProcessor vp = new ValidatorProcessor();
        vp.execute(params);
    }

    /**
	 * Clones the velocity file and injects params accordingly
	 */
    public void clone(File path, Map<String, Object> params) {
        File outputFile = getOutputFile(path, params);
        params.put("package", packager.getPackage(outputFile));
        SyncMode syncMode = (SyncMode) params.get("syncMode");
        if (syncMode == SyncMode.CREATE) {
            if (outputFile.exists()) {
                _log.info(outputFile.getName() + " not updated. File already exist. SyncMode: [" + syncMode + "]");
            } else {
                writeTemplateFile(path, params, outputFile);
            }
        } else if (syncMode == SyncMode.UPDATE) {
            if (outputFile.exists()) {
                String code = FileUtil.readFile(outputFile);
                String newHash = StringUtil.hashSourceCode(code);
                String oldHash = CodeUtil.getHashCode(params.get("package") + "." + outputFile.getName());
                if (!StringUtil.isEmpty(oldHash) && newHash.equals(oldHash)) {
                    writeTemplateFile(path, params, outputFile);
                } else {
                    _log.info(outputFile.getName() + " not updated. File already exist in path [" + outputFile.getAbsolutePath() + "] and changes were found. SyncMode: [" + syncMode + "]");
                }
            } else {
                writeTemplateFile(path, params, outputFile);
            }
        } else if (syncMode == SyncMode.OVERWRITE) {
            writeTemplateFile(path, params, outputFile);
        }
    }

    /**
	 * Private helper to write velocity template into file.
	 * 
	 * @param path
	 * @param params
	 * @param outputFile
	 */
    private void writeTemplateFile(File path, Map<String, Object> params, File outputFile) {
        try {
            FileUtil.createDirectory(outputFile.getParent());
            outputFile.createNewFile();
            mergeTemplate(path, params, new FileWriter(outputFile));
            CodeUtil.updateHashCode(outputFile, params.get("package") + "." + outputFile.getName());
            _log.info(outputFile.getName() + " successfully generated.");
        } catch (IOException e) {
            _log.error("Failed to create target file. [" + outputFile.getAbsoluteFile() + "]", e);
        }
    }

    /**
	 * Outputs the velocity template along with specified parameters
	 * to the given output writer.
	 * @param path
	 * @param params
	 * @param output
	 */
    private void mergeTemplate(File path, Map<String, Object> params, Writer output) {
        try {
            if (filter != null) filter.doFilter(path, params);
            VelocityContext context = new VelocityContext();
            for (Entry<String, Object> entry : params.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
            String relativePath = getTemplateForMerge(path);
            Velocity.mergeTemplate(relativePath, ENCODING, context, output);
        } catch (ResourceNotFoundException e) {
            _log.error(e, e);
        } catch (ParseErrorException e) {
            _log.error(e, e);
        } catch (MethodInvocationException e) {
            _log.error(e, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
            ;
        }
    }

    /**
	 * Builds the path to the output file.
	 * 
	 * @param template
	 * @param params
	 * @return
	 */
    private File getOutputFile(File template, Map<String, Object> params) {
        String outputDir = packager.getPackageFolder("" + params.get("modelPackage"), template);
        outputDir = outputDir.replace("className", params.get("className").toString()).replace("modelName", params.get("modelName").toString());
        try {
            FileUtil.createDirectory(outputDir);
        } catch (Exception e) {
            _log.error("Failed to create output directory [" + outputDir + "]", e);
            throw new RuntimeException(e);
        }
        String outputName = template.getName().replace("className", params.get("className").toString()).replace("modelName", params.get("modelName").toString()).replaceAll(".vm$", "");
        return new File(outputDir + "/" + outputName);
    }

    /**
	 * @param filter the filter to set
	 */
    public final void setFilter(ProcessorFilter filter) {
        this.filter = filter;
    }

    /**
	 * @return the filter
	 */
    protected final ProcessorFilter getFilter() {
        return filter;
    }

    /**
	 * @return the packager
	 */
    protected final PackageUtil getPackager() {
        return packager;
    }
}
