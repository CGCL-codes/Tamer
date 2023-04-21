package org.zclasspath;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import org.apache.log4j.Logger;

public class ZClassPathScanner {

    static Logger log = Logger.getLogger(ZClassPathScanner.class);

    private final List<ZIClassPathItem> classPathItems = new ArrayList<ZIClassPathItem>();

    private final Stack<ZIClassPathFilter> filter = new Stack<ZIClassPathFilter>();

    private final Stack<ZIClassPathVisitor> visitor = new Stack<ZIClassPathVisitor>();

    private final Stack<ZIClassPathErrorHandler> errorHandler = new Stack<ZIClassPathErrorHandler>();

    public ZClassPathScanner() {
        pushFilter(new ZIClassPathFilter() {

            public boolean acceptClasspathPart(String name) {
                return true;
            }

            public boolean acceptClass(String name) {
                return true;
            }

            public boolean acceptResource(String resource) {
                return true;
            }
        });
        pushVisitor(new ZIClassPathVisitor() {

            public void visitResource(ZIClassPathElement element) throws Exception {
                log.debug(element.getName());
            }

            public void visitClass(ZClassPathClass classPathClass) throws Exception {
                log.debug(classPathClass.getClassName());
            }
        });
        pushErrorHandler(new ZIClassPathErrorHandler() {

            public void handleError(String msg, Throwable t) {
                log.error(" ******** " + t.getClass() + " ******** ");
                if (t instanceof NoClassDefFoundError) {
                    if (log.isDebugEnabled()) {
                        log.debug(msg, t);
                    }
                } else {
                    log.error(msg, t);
                }
            }
        });
    }

    public void scan() throws Exception {
        List<ZIClassPathItem> roots;
        if (classPathItems.isEmpty()) {
            roots = ZJavaClassPath.getItems();
        } else {
            roots = classPathItems;
        }
        for (ZIClassPathItem root : roots) {
            scanRoot(root);
        }
    }

    private void scanRoot(ZIClassPathItem root) throws Exception {
        if (filter.peek().acceptClasspathPart(root.getName())) {
            log.debug(root.getName());
            if (root.isDirectory()) {
                ZClassPathLocation location = new ZClassPathLocation(root);
                scan(location, root);
            } else if (isJar(root)) {
                log.debug("scanJar " + root.getName());
                InputStream in = root.getInputStream();
                if (in != null) {
                    try {
                        JarInputStream jarIn = new JarInputStream(in);
                        try {
                            ZClassPathLocation location = new ZClassPathLocation(root);
                            scanJar(location, jarIn);
                        } finally {
                            jarIn.close();
                        }
                    } finally {
                        in.close();
                    }
                } else {
                    log.warn("not found: " + root.getName());
                }
            }
        }
    }

    protected boolean isJar(String name) throws Exception {
        return name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear") || name.endsWith(".sar") || name.endsWith(".rar");
    }

    protected boolean isJar(ZIClassPathItem root) throws Exception {
        boolean b = root.isFile() && isJar(root.getName());
        return b;
    }

    protected void scan(final ZIClassPathLocation location, ZIClassPathItem file) throws Exception {
        ZIClassPathItem[] list = file.list();
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
                log.debug(list[i].getName());
                scan(location, list[i]);
            } else if (isJar(list[i])) {
                log.debug(list[i].getName());
                InputStream in = list[i].getInputStream();
                try {
                    JarInputStream jarIn = new JarInputStream(in);
                    try {
                        ZClassPathLocation jarLocation = new ZClassPathLocation(location, list[i].getName());
                        scanJar(jarLocation, jarIn);
                    } finally {
                        jarIn.close();
                    }
                } finally {
                    in.close();
                }
            } else {
                log.debug(list[i].getName());
                final ZIClassPathItem crt = list[i];
                ZIClassPathElement element = new ZIClassPathElement() {

                    public InputStream getInputStream() throws Exception {
                        return crt.getInputStream();
                    }

                    public String getName() throws Exception {
                        return crt.getRelativePath(location.getClassPathItem());
                    }

                    public ZIClassPathLocation getClassPathLocation() throws Exception {
                        return location;
                    }

                    public Manifest getManifest() throws Exception {
                        return null;
                    }
                };
                process(element);
            }
        }
    }

    protected void scanJar(final ZIClassPathLocation location, final JarInputStream jarIn) throws Exception {
        for (ZipEntry ze = jarIn.getNextEntry(); ze != null; ze = jarIn.getNextEntry()) {
            String crtName = ze.getName();
            if (crtName.startsWith("WEB-INF/classes/")) {
                crtName = crtName.substring("WEB-INF/classes/".length());
            } else if (crtName.startsWith("WEB-INF/lib/")) {
                crtName = crtName.substring("WEB-INF/lib/".length());
            }
            final String name = crtName;
            log.debug(name);
            ZIClassPathElement element = new ZIClassPathElement() {

                public InputStream getInputStream() throws Exception {
                    return wrap(jarIn);
                }

                public String getName() {
                    return name;
                }

                public ZIClassPathLocation getClassPathLocation() throws Exception {
                    return location;
                }

                public Manifest getManifest() throws Exception {
                    return jarIn.getManifest();
                }
            };
            process(element);
        }
    }

    private static InputStream wrap(final JarInputStream in) throws Exception {
        InputStream ret = new FilterInputStream(in) {

            @Override
            public void close() throws IOException {
            }
        };
        return ret;
    }

    /**
     * process
     *
     * @param name
     *          String
     */
    protected void process(ZIClassPathElement element) throws Exception {
        log.debug(element.getName());
        if (isClass(element.getName())) {
            processClass(element);
        } else if (isJar(element.getName())) {
            JarInputStream jarIn = new JarInputStream(element.getInputStream());
            try {
                ZClassPathLocation jarLocation = new ZClassPathLocation(element.getClassPathLocation(), element.getName());
                scanJar(jarLocation, jarIn);
            } finally {
                jarIn.close();
            }
        } else {
            processResource(element);
        }
    }

    protected boolean isClass(String name) {
        return name.endsWith(".class");
    }

    /**
     * processClass
     *
     * @param name
     *          String
     */
    protected void processClass(ZIClassPathElement element) throws Exception {
        String className = element.getName().replace('/', '.').replace('\\', '.');
        className = className.substring(0, className.length() - 6);
        ZClassPathClass classPathClass = new ZClassPathClass(className, element);
        if (filter.peek().acceptClass(className)) {
            try {
                getVisitor().visitClass(classPathClass);
            } catch (Throwable e) {
                if (e instanceof NoClassDefFoundError) {
                    log.error("Error while processing " + className + " --- " + e);
                } else {
                    log.error("Error while processing " + className, e);
                }
            }
        }
    }

    /**
     * processResource
     *
     * @param name
     *          String
     */
    protected void processResource(ZIClassPathElement element) throws Exception {
        if (filter.peek().acceptResource(element.getName())) {
            try {
                getVisitor().visitResource(element);
            } catch (Throwable e) {
                log.error("Error while processing " + element.getName(), e);
            }
        }
    }

    public void setErrorHandler(ZIClassPathErrorHandler errorHandler) {
        popErrorHandler();
        pushErrorHandler(errorHandler);
    }

    public ZIClassPathErrorHandler getErrorHandler() {
        return errorHandler.peek();
    }

    public void pushErrorHandler(ZIClassPathErrorHandler errorHandler) {
        this.errorHandler.push(errorHandler);
    }

    public ZIClassPathErrorHandler popErrorHandler() {
        return this.errorHandler.pop();
    }

    public ZIClassPathFilter getFilter() {
        return filter.peek();
    }

    public void setFilter(ZIClassPathFilter filter) {
        this.filter.pop();
        this.filter.push(filter);
    }

    public void pushFilter(ZIClassPathFilter filter) {
        this.filter.push(filter);
    }

    public ZIClassPathFilter popFilter() {
        return filter.pop();
    }

    public ZIClassPathVisitor getVisitor() {
        return visitor.peek();
    }

    public void setVisitor(ZIClassPathVisitor visitor) {
        popVisitor();
        pushVisitor(visitor);
    }

    public void pushVisitor(ZIClassPathVisitor visitor) {
        this.visitor.push(visitor);
    }

    public ZIClassPathVisitor popVisitor() {
        return visitor.pop();
    }

    public List<ZIClassPathItem> getClassPathItems() {
        return classPathItems;
    }

    public void setClassPathItems(List<ZIClassPathItem> classPathItems) {
        this.classPathItems.clear();
        this.classPathItems.addAll(classPathItems);
    }
}
