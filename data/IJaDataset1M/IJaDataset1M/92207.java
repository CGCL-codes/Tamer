package cn.sduo.app.util.upload;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.sduo.app.util.IOPUtil;

public class FileHandlerFactory {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, ValidationResult> fileErrorsMap = new HashMap<String, ValidationResult>();

    private String configFileLocation;

    private Properties fileConfig;

    private List<FileHandler> fileHandlers;

    public Properties getFileConfig() {
        return fileConfig;
    }

    public String getConfigFileLocation() {
        return configFileLocation;
    }

    public static FileHandlerFactory getInstance(Properties fileConfig, HttpServletRequest request) throws Exception {
        if (request.getSession(false).getAttribute(FileHandlerFactory.class.getName()) == null) {
            FileHandlerFactory fhf = new FileHandlerFactory(fileConfig);
            fhf.init();
            request.getSession(false).setAttribute(FileHandlerFactory.class.getName(), fhf);
            return fhf;
        } else {
            return (FileHandlerFactory) request.getSession(false).getAttribute(FileHandlerFactory.class.getName());
        }
    }

    public static FileHandlerFactory getInstance(String configFileLocation, HttpServletRequest request) throws Exception {
        if (request.getSession(false).getAttribute(FileHandlerFactory.class.getName()) == null) {
            FileHandlerFactory fhf = new FileHandlerFactory(configFileLocation);
            fhf.init();
            request.getSession(false).setAttribute(FileHandlerFactory.class.getName(), fhf);
            return fhf;
        } else {
            return (FileHandlerFactory) request.getSession(false).getAttribute(FileHandlerFactory.class.getName());
        }
    }

    private FileHandlerFactory(String configFileLocation) {
        super();
        this.configFileLocation = configFileLocation;
    }

    public FileHandlerFactory(Properties fileConfig) {
        super();
        this.fileConfig = fileConfig;
    }

    public Map<String, ValidationResult> getFileErrorsMap() {
        return fileErrorsMap;
    }

    public void init() throws Exception {
        logger.info("Initiating FileHandlerFactory.");
        if (fileConfig == null) {
            logger.debug("loadding upload properties file {}.", configFileLocation);
            fileConfig = new Properties();
            InputStream is = IOPUtil.getResourceAsStream(configFileLocation);
            fileConfig.load(is);
            logger.debug("upload properties file {} is successfully loaded.", configFileLocation);
        }
        String userDir = System.getProperty("user.dir");
        File directory = new File(userDir + "/../wwwroot" + fileConfig.getProperty("system.upload.persistenceFolder"));
        if (!directory.exists()) {
            boolean rt = directory.mkdirs();
            if (rt) {
                logger.debug("successfully make directory {}", fileConfig.getProperty("system.upload.persistenceFolder"));
            } else {
                logger.debug("making directory failed. {}", fileConfig.getProperty("system.upload.persistenceFolder"));
            }
        }
        fileHandlers = new ArrayList<FileHandler>();
        if ("true".equals(fileConfig.get("system.upload.FileExistenceHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.FileExistenceHandler();
            fileHandlers.add(vsh);
            logger.debug("FileExistenceHandler enabled.");
        } else {
            logger.debug("FileExistenceHandler disabled.");
        }
        if ("true".equals(fileConfig.get("system.upload.VirusScanHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.VirusScanHandler(fileConfig);
            fileHandlers.add(vsh);
            logger.debug("VirusScanHandler enabled.");
        } else {
            logger.debug("VirusScanHandler disabled.");
        }
        if ("true".equals(fileConfig.get("system.upload.FileNameValidationHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.FileNameValidationHandler(fileConfig);
            fileHandlers.add(vsh);
            logger.debug("FileNameValidationHandler enabled.");
        } else {
            logger.debug("FileNameValidationHandler disabled.");
        }
        if ("true".equals(fileConfig.get("system.upload.FileSizeValidationHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.FileSizeValidationHandler(fileConfig);
            fileHandlers.add(vsh);
            logger.debug("FileSizeValidationHandler enabled.");
        } else {
            logger.debug("FileSizeValidationHandler disabled.");
        }
        if ("true".equals(fileConfig.get("system.upload.FileExtensionValidationHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.FileExtensionValidationHandler(fileConfig);
            fileHandlers.add(vsh);
            logger.debug("FileExtensionValidationHandler enabled.");
        } else {
            logger.debug("FileExtensionValidationHandler disabled.");
        }
        if ("true".equals(fileConfig.get("system.upload.MimeTypeValidationHandler.enable"))) {
            FileHandler vsh = new cn.sduo.app.util.upload.MimeTypeValidationHandler(fileConfig);
            fileHandlers.add(vsh);
            logger.debug("MimeTypeValidationHandler enabled.");
        } else {
            logger.debug("MimeTypeValidationHandler disabled.");
        }
        logger.info("FileHandlerFactory is successfully initiated.");
    }

    public void removeFile(String file) {
        final String path = fileConfig.getProperty("system.upload.persistenceFolder") + "/" + file;
        File file1 = new File(path);
        file1.delete();
        this.getFileErrorsMap().remove(file);
    }

    public ValidationResult handleFiles(List<File> files) {
        ValidationResult vr = new ValidationResult();
        for (File file : files) {
            ValidationResult result = handleFile(file);
            vr.addErrors(result.getErrors());
        }
        return vr;
    }

    public ValidationResult handleFile(File file) {
        ValidationResult result = new ValidationResult();
        for (FileHandler fh : fileHandlers) {
            ValidationResult res = fh.handle(file);
            result.addErrors(res.getErrors());
        }
        fileErrorsMap.put(file.getName(), result);
        return result;
    }
}
