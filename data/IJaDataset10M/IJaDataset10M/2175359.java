package protoj.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.Copy;
import protoj.core.ProjectLayout;
import protoj.core.internal.AntTarget;

/**
 * Responsible for creating a demonstration project on the filing system.
 * 
 * @author Ashley Williams
 * 
 */
public final class DemoProjectFeature {

    /**
	 * A reference to the demo project layout.
	 */
    private ProjectLayout layout;

    /**
	 * Creates a demo project with the specified rootDir. The specified jarFile
	 * will be used to copy into the lib directory and will typically be either
	 * the minimal protoj jar file or the no dependencies jar file.
	 * 
	 * @param rootDir
	 * @param jarFile
	 */
    public void createProject(File rootDir, File jarFile) {
        StandardProject project = new StandardProject(rootDir, "demo", null);
        this.layout = project.getLayout();
        layout.createPhysicalLayout();
        copyJarFile(jarFile);
        File bashScript = new File(layout.getBinDir(), "demo.sh");
        copyResource(bashScript);
        File winScript = new File(layout.getBinDir(), "demo.bat");
        copyResource(winScript);
        File ivy = new File(layout.getResourcesDir(), "foo/ivy.xml");
        copyResource(ivy);
        File devProperties = new File(layout.getConfDir(), "dev.properties");
        copyResource(devProperties);
        File readme = new File(layout.getRootDir(), "README.txt");
        copyResource(readme);
        File notice = new File(layout.getRootDir(), "NOTICE.txt");
        copyResource(notice);
        File fooCore = new File(layout.getJavaDir(), "org/foo/core/FooCore.java");
        copyResource(fooCore);
        File fooProject = new File(layout.getJavaDir(), "org/foo/system/FooProject.java");
        copyResource(fooProject);
        relaxPermissions();
        System.out.println("created project at " + layout.getRootDir().getCanonicalPath());
    }

    /**
	 * Copies the file under the /demo resource path to the specified dest. The
	 * resource file to be copied matches the location in the dest.
	 * 
	 * @param name
	 *            the name of the resource under the demo directory
	 * @param dest
	 *            the destination of the resource copy
	 */
    private void copyResource(File dest) {
        String relativePath = layout.getRelativePath(dest).replace("\\", "/");
        String source = "/protoj/demo/" + relativePath;
        InputStream resource = getClass().getResourceAsStream(source);
        if (resource == null) {
            throw new RuntimeException("couldn't find resource " + source);
        }
        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
            dest.createNewFile();
        }
        copyResource(new InputStreamReader(resource), dest);
    }

    /**
	 * Copies the jar file into the demo lib directory.
	 * 
	 * @param jarFile
	 */
    private void copyJarFile(File jarFile) {
        AntTarget target = new AntTarget("demo");
        Copy copy = new Copy();
        copy.setTaskName("demo-copy-jar");
        target.addTask(copy);
        copy.setFile(jarFile);
        copy.setTodir(layout.getLibDir());
        target.execute();
    }

    /**
	 * Apply 777 permissions all the way down.
	 */
    private void relaxPermissions() {
        AntTarget target = new AntTarget("demo");
        Chmod chmod = new Chmod();
        chmod.setTaskName("demo-permissions");
        target.addTask(chmod);
        chmod.setDir(layout.getRootDir().getParentFile());
        chmod.setIncludes(layout.getRootName() + "/**/*.*");
        chmod.setPerm("777");
        target.execute();
    }

    /**
	 * Streams the contents of the source reader to the destination file.
	 * 
	 * @param source
	 * @param dest
	 */
    private void copyResource(Reader source, File dest) {
        BufferedReader reader = new BufferedReader(source);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(dest));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            } finally {
                writer.close();
            }
        } finally {
            reader.close();
        }
    }
}
