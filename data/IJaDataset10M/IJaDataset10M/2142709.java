package defuddle;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.lang.System;

public class DefuddleSOAPBindingImpl implements defuddle.Defuddle {

    private java.util.Hashtable schemas = new java.util.Hashtable();

    public void addSchema(Schema schema) throws java.rmi.RemoteException {
        this.schemas.put(schema.getName(), schema);
    }

    public java.lang.String parse(java.lang.String dfdlSchemaName, java.lang.String xsltSchemaName, byte[] data) throws java.rmi.RemoteException {
        String result = "";
        byte[] resultbyte = performTranslation(dfdlSchemaName, xsltSchemaName, new ByteArrayInputStream(data));
        if (resultbyte != null) {
            return new String(resultbyte);
        } else {
            return null;
        }
    }

    public java.lang.String parseWURL(java.lang.String dfdlSchemaName, java.lang.String xsltSchemaName, String url) throws java.rmi.RemoteException {
        System.out.println("parsing using url " + url);
        try {
            URI dataURI = new URI(url);
            File inputFile = new File(dataURI);
            if (inputFile.exists()) {
                byte[] resultbyte = performTranslation(dfdlSchemaName, xsltSchemaName, new FileInputStream(inputFile));
                if (resultbyte != null) {
                    return new String(resultbyte);
                } else {
                    return null;
                }
            } else {
                throw new java.rmi.RemoteException("Invalid data: " + url + " not found");
            }
        } catch (java.net.URISyntaxException e) {
            throw new java.rmi.RemoteException(e.getMessage());
        } catch (java.io.FileNotFoundException e) {
            throw new java.rmi.RemoteException(e.getMessage());
        }
    }

    private byte[] performTranslation(java.lang.String dfdlSchemaName, java.lang.String xsltSchemaName, InputStream dataStream) throws java.rmi.RemoteException {
        InputStream dfdlInStream = null;
        InputStream xsltInStream = null;
        InputStream[] dataStreams = new InputStream[1];
        String packageName = "";
        String tomcat_home = System.getProperty("catalina.home");
        if (tomcat_home != null) {
            String buildPath = tomcat_home + "/webapps/axis";
            String buildDir = "WEB-INF";
            if (dfdlSchemaName != null) {
                packageName = dfdlSchemaName;
                if (packageName.indexOf("/") >= 0) {
                    packageName = packageName.substring(packageName.lastIndexOf("/") + 1);
                } else if (packageName.indexOf("\\") >= 0) {
                    packageName = packageName.substring(packageName.lastIndexOf("\\") + 1);
                }
                if (packageName.indexOf(".") >= 0) {
                    packageName = packageName.substring(0, packageName.lastIndexOf("."));
                }
                packageName = packageName + "Pkg";
                if (schemas.get(dfdlSchemaName) != null) {
                    Schema s = (Schema) schemas.get(dfdlSchemaName);
                    dfdlInStream = new ByteArrayInputStream(s.getFile());
                }
            }
            if (xsltSchemaName != null && schemas.get(xsltSchemaName) != null) {
                Schema s = (Schema) schemas.get(xsltSchemaName);
                xsltInStream = new ByteArrayInputStream(s.getFile());
            }
            dataStreams[0] = dataStream;
            byte[] resultbyte = null;
            try {
                resultbyte = translators.TransformerUtil.dfdlTransform(dfdlInStream, xsltInStream, dataStreams, packageName, buildPath, buildDir, buildPath + "/" + buildDir + "/lib");
            } catch (Throwable e) {
                System.err.println("Exception while performing dfdl transform " + e);
                throw new java.rmi.RemoteException(e.getMessage());
            }
            return resultbyte;
        } else {
            java.util.Enumeration props = System.getProperties().keys();
            while (props.hasMoreElements()) {
                String propName = props.nextElement().toString();
                String prop = System.getProperty(propName);
            }
            throw new java.rmi.RemoteException("CATALINA_HOME variable must be set");
        }
    }
}
