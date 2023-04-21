package com.liferay.util.ant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.axis.tools.ant.wsdl.Java2WsdlAntTask;
import org.apache.axis.tools.ant.wsdl.NamespaceMapping;
import org.apache.axis.tools.ant.wsdl.Wsdl2javaAntTask;
import org.apache.tools.ant.Project;
import org.dom4j.DocumentException;
import com.dotmarketing.util.Logger;
import com.liferay.util.FileUtil;
import com.liferay.util.Html;
import com.liferay.util.StringUtil;
import com.liferay.util.xml.XMLFormatter;

/**
 * <a href="Java2WsddTask.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Unknown
 * @version $Rev: $
 *
 */
public class Java2WsddTask {

    public static String[] generateWsdd(String className, String serviceName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmssSSS");
        File tempDir = new File(sdf.format(new Date()));
        tempDir.mkdir();
        String wsdlFileName = tempDir + "/service.wsdl";
        int pos = className.lastIndexOf(".");
        String packagePath = className.substring(0, pos);
        String[] packagePaths = StringUtil.split(packagePath, ".");
        String namespace = "urn:";
        for (int i = packagePaths.length - 1; i >= 0; i--) {
            namespace += packagePaths[i];
            if (i > 0) {
                namespace += ".";
            }
        }
        String location = "http://localhost/services/" + serviceName;
        String mappingPackage = packagePath.substring(0, packagePath.lastIndexOf(".")) + ".ws";
        Project project = AntUtil.getProject();
        Java2WsdlAntTask java2Wsdl = new Java2WsdlAntTask();
        NamespaceMapping mapping = new NamespaceMapping();
        mapping.setNamespace(namespace);
        mapping.setPackage(mappingPackage);
        java2Wsdl.setProject(project);
        java2Wsdl.setClassName(className);
        java2Wsdl.setOutput(new File(wsdlFileName));
        java2Wsdl.setLocation(location);
        java2Wsdl.setNamespace(namespace);
        java2Wsdl.addMapping(mapping);
        java2Wsdl.execute();
        Wsdl2javaAntTask wsdl2Java = new Wsdl2javaAntTask();
        wsdl2Java.setProject(project);
        wsdl2Java.setURL(wsdlFileName);
        wsdl2Java.setOutput(tempDir);
        wsdl2Java.setServerSide(true);
        wsdl2Java.setTestCase(false);
        wsdl2Java.setVerbose(false);
        wsdl2Java.execute();
        String deployContent = FileUtil.read(tempDir + "/" + StringUtil.replace(packagePath, ".", "/") + "/deploy.wsdd");
        deployContent = StringUtil.replace(deployContent, packagePath + "." + serviceName + "SoapBindingImpl", className);
        deployContent = _format(deployContent);
        String undeployContent = FileUtil.read(tempDir + "/" + StringUtil.replace(packagePath, ".", "/") + "/undeploy.wsdd");
        undeployContent = _format(undeployContent);
        DeleteTask.deleteDirectory(tempDir);
        return new String[] { deployContent, undeployContent };
    }

    private static String _format(String content) throws IOException {
        content = Html.stripComments(content);
        try {
            content = XMLFormatter.toString(content);
        } catch (DocumentException de) {
            Logger.error(Java2WsddTask.class, de.getMessage(), de);
        }
        return content;
    }
}
