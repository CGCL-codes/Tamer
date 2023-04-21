package net.stickycode.bootstrap.guice3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassReader;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.devsurf.injection.guice.scanner.ClasspathScanner;
import de.devsurf.injection.guice.scanner.PackageFilter;
import de.devsurf.injection.guice.scanner.asm.AnnotationCollector;
import de.devsurf.injection.guice.scanner.features.ScannerFeature;

public class StickyClasspathScanner implements ClasspathScanner {

    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    @Inject
    @Named("classpath")
    private URL[] classPath;

    private List<Pattern> patterns = new ArrayList<Pattern>();

    private Set<String> visited;

    private AnnotationCollector collector;

    @Inject
    public StickyClasspathScanner(Set<ScannerFeature> listeners, @Named("packages") PackageFilter... filter) {
        this.collector = new AnnotationCollector();
        for (PackageFilter p : filter) {
            includePackage(p);
        }
        for (ScannerFeature listener : listeners) {
            addFeature(listener);
        }
        visited = new HashSet<String>();
    }

    @Override
    public void addFeature(ScannerFeature feature) {
        collector.addScannerFeature(feature);
    }

    @Override
    public void removeFeature(ScannerFeature feature) {
        collector.addScannerFeature(feature);
    }

    @Override
    public List<ScannerFeature> getFeatures() {
        return collector.getScannerFeatures();
    }

    @Override
    public void includePackage(final PackageFilter filter) {
        String packageName = filter.getPackage();
        String pattern = ".*" + packageName.replace(".", "/");
        if (filter.deep()) {
            pattern = pattern + "/(?:\\w|/)*([A-Z](?:\\w|\\$)+)\\.class$";
        } else {
            pattern = pattern + "/([A-Z](?:\\w|\\$)+)\\.class$";
        }
        patterns.add(Pattern.compile(pattern));
    }

    @Override
    public void excludePackage(final PackageFilter filter) {
    }

    public void scan() throws IOException {
        for (final URL url : classPath) {
            visitUrl(url);
        }
        destroy();
    }

    protected void visitUrl(URL url) {
        try {
            if (url.toString().startsWith("jar:")) {
                visitJar(url);
                return;
            }
            URI uri;
            File entry;
            try {
                uri = url.toURI();
                entry = new File(uri);
                if (!entry.exists()) {
                    return;
                }
            } catch (URISyntaxException e) {
                return;
            } catch (Throwable e) {
                return;
            }
            if (entry.isDirectory()) {
                visitFolder(entry);
            } else {
                String path = uri.toString();
                if (matches(path)) {
                    if (!visited.contains(entry.getAbsolutePath())) {
                        visitClass(new FileInputStream(entry));
                        visited.add(entry.getAbsolutePath());
                    }
                } else if (path.endsWith(".jar")) {
                    visitJar(entry);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (Throwable e) {
        }
    }

    public void destroy() {
        classPath = null;
        collector = null;
        patterns.clear();
        patterns = null;
        visited.clear();
        visited = null;
    }

    private void visitFolder(File folder) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                visitFolder(file);
            } else {
                String path = file.toURI().toString();
                if (matches(path)) {
                    if (!visited.contains(file.getAbsolutePath())) {
                        visitClass(new FileInputStream(file));
                        visited.add(file.getAbsolutePath());
                    }
                } else if (path.endsWith(".jar")) {
                    visitJar(file);
                }
            }
        }
    }

    private void visitJar(URL url) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        _visitJar(conn.getJarFile());
    }

    private void visitJar(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        _visitJar(jarFile);
    }

    private void _visitJar(JarFile jarFile) throws IOException {
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        for (JarEntry jarEntry = null; jarEntries.hasMoreElements(); ) {
            jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (!jarEntry.isDirectory() && matches(name)) {
                if (!visited.contains(name)) {
                    visitClass(jarFile.getInputStream(jarEntry));
                    visited.add(name);
                }
            }
        }
    }

    private void visitClass(InputStream in) throws IOException {
        ClassReader reader = new ClassReader(new BufferedInputStream(in));
        reader.accept(collector, AnnotationCollector.ASM_FLAGS);
    }

    private boolean matches(String name) {
        for (Pattern pattern : patterns) {
            if (pattern.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }
}
