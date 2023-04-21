package org.jaffa.wsapi.apis;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import org.jaffa.util.URLHelper;
import org.jaffa.wsapi.apis.helper.AspectUpdateHelper;

/**
 *
 * @author Saravanan
 */
public class AspectUpdateService {

    private static Logger log = Logger.getLogger(AspectUpdateService.class);

    private static final String AOP_DIR_URL = "classpath:///aop/wsdocs";

    public boolean updateAspectAnnotation(String className, String propertyName, String parameterName, String returnTypeName, String description) {
        if (log.isDebugEnabled()) {
            log.debug("AspectUpdateService updateAnnotation : className: " + className + "propertName :" + propertyName + "parameterName: " + parameterName + "returnTypeName :" + returnTypeName);
            log.debug("AOP_DIR_URL :" + AOP_DIR_URL);
        }
        String xpathExp = null;
        if ((className != null && className.length() > 0) && (propertyName != null && propertyName.length() > 0) && (parameterName != null && parameterName.length() > 0)) {
            xpathExp = "//aop/metadata[@class='" + className + "']/property[@name='" + propertyName + "']/method-param[@name='" + parameterName + "']";
            if (log.isDebugEnabled()) {
                log.debug("Updating/Adding annotation for property xpathExp:" + xpathExp);
            }
        } else if ((className != null && className.length() > 0) && (propertyName != null && propertyName.length() > 0) && (returnTypeName != null && returnTypeName.length() > 0)) {
            xpathExp = "//aop/metadata[@class='" + className + "']/property[@name='" + propertyName + "']/method-return";
            if (log.isDebugEnabled()) {
                log.debug("Updating/Adding annotation for property xpathExp:" + xpathExp);
            }
        } else if ((className != null && className.length() > 0) && (propertyName != null && propertyName.length() > 0)) {
            xpathExp = "//aop/metadata[@class='" + className + "']/property[@name='" + propertyName + "']";
            if (log.isDebugEnabled()) {
                log.debug("Updating/Adding annotation for property xpathExp:" + xpathExp);
            }
        } else if (className != null || className.length() > 0) {
            xpathExp = "//aop/metadata[@class='" + className + "']";
            if (log.isDebugEnabled()) {
                log.debug("Updating/Adding annotation for metadata xpathExp:" + xpathExp);
            }
        }
        if (xpathExp != null) {
            try {
                URL url = URLHelper.newExtendedURL(AOP_DIR_URL);
                if (!new File(url.getPath()).exists()) {
                    new File(url.getPath()).mkdirs();
                }
                List<File> resultList = AspectUpdateHelper.findFileAndUpdateAspect(url.getPath(), description, xpathExp, propertyName, className, null, null, false);
                if (log.isDebugEnabled()) {
                    for (File file : resultList) {
                        log.debug("Update AOP File :" + file.getPath());
                    }
                }
                if (resultList != null && !resultList.isEmpty()) {
                    return true;
                } else if (resultList != null && resultList.isEmpty()) {
                    if ((className != null && className.length() > 0) && (propertyName != null && propertyName.length() > 0) && (parameterName != null && parameterName.length() > 0)) {
                        xpathExp = "//aop/metadata[@class='" + className + "']/property[@name='" + propertyName + "']";
                        resultList = AspectUpdateHelper.findFileAndUpdateAspect(url.getPath(), description, xpathExp, propertyName, className, parameterName, null, true);
                    } else if ((className != null && className.length() > 0) && (propertyName != null && propertyName.length() > 0) && (returnTypeName != null && returnTypeName.length() > 0)) {
                        xpathExp = "//aop/metadata[@class='" + className + "']/property[@name='" + propertyName + "']";
                        resultList = AspectUpdateHelper.findFileAndUpdateAspect(url.getPath(), description, xpathExp, propertyName, className, null, returnTypeName, true);
                    } else {
                        xpathExp = "//aop/metadata[@class='" + className + "']";
                        resultList = AspectUpdateHelper.findFileAndUpdateAspect(url.getPath(), description, xpathExp, propertyName, className, null, null, true);
                    }
                    if (resultList != null && !resultList.isEmpty()) {
                        return true;
                    } else {
                        return AspectUpdateHelper.createAOP(className, description, propertyName);
                    }
                }
            } catch (MalformedURLException e) {
                log.error(e);
            }
        }
        return false;
    }
}
