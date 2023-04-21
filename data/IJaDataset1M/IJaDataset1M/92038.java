package depth;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class JavaAppResourceHandler implements IResourceHandler {

    private IPath diskLocation;

    private IPath workingDir;

    public JavaAppResourceHandler() {
        diskLocation = new Path(computeDiskLocation());
        workingDir = diskLocation.removeLastSegments(1);
    }

    public String getFileLocation(String resource) {
        return workingDir.append(resource).toOSString();
    }

    private String computeDiskLocation() {
        boolean inJar = false;
        Class c = JavaAppResourceHandler.class;
        String pathName = c.getName().replace('.', '/') + ".class";
        ClassLoader loader = c.getClassLoader();
        URL url = (loader != null) ? loader.getResource(pathName) : ClassLoader.getSystemResource(pathName);
        String path = url.getPath();
        path = path.replaceAll("\\+", "%2b");
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        boolean removeFirstSlash = !File.separator.equals("/");
        path = path.substring(path.indexOf("/") + (removeFirstSlash ? 1 : 0), path.length());
        if (path.contains("!/")) {
            path = path.substring(0, path.indexOf("!/"));
            inJar = true;
        }
        File f = new File(path);
        f = f.getParentFile();
        if (!inJar) {
            f = f.getParentFile();
        }
        return f.getAbsolutePath();
    }
}
