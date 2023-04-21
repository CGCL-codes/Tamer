package pl.omtt.compiler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class OmttCompiler {

    String fTargetDirectory = "./";

    Set<String> fClasspath = new TreeSet<String>();

    public OmttCompiler() {
        fClasspath.add("lib/omtt-core.jar");
    }

    public void setEnvorionmentDirectory(String directory) {
        fTargetDirectory = directory;
        fClasspath.add(directory);
    }

    public OmttCompilationTask getTask(List<URI> sources, URI target, List<URI> classPath) {
        return new OmttCompilationTask(this, sources, target, classPath);
    }

    public OmttCompilationTask getTask(URI source, URI target, List<URI> classPath) {
        ArrayList<URI> sources = new ArrayList<URI>();
        sources.add(source);
        return getTask(sources, target, classPath);
    }

    JavaCompiler getJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    protected List<String> getJavaCompilerOptions(URI target, List<URI> classPath) {
        List<String> options = new ArrayList<String>();
        options.add("-d");
        options.add(target.getSchemeSpecificPart());
        if (classPath.size() > 0) {
            options.add("-classpath");
            StringBuffer buf = new StringBuffer();
            for (URI cp : classPath) buf.append(cp.getSchemeSpecificPart()).append(":");
            options.add(buf.toString());
        }
        return options;
    }
}
