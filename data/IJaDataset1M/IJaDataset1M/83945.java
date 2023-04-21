package net.sourceforge.javautil.common.classloader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.javautil.common.io.IVirtualDirectory;
import net.sourceforge.javautil.common.visitor.IVisitorSimple;
import net.sourceforge.javautil.common.visitor.VisitorContextBase;

/**
 * This represents a visitor to a {@link ClassLoader} in relation to the {@link ClassLoaderScanner} framework.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: ClassLoaderVisitor.java 2297 2010-06-16 00:13:14Z ponderator $
 */
public class ClassLoaderVisitor<CL extends ClassLoader> implements IVisitorSimple<ClassLoaderVisitorContext> {

    protected final boolean single;

    protected List<ClassLoaderResource> resources = new ArrayList<ClassLoaderResource>();

    public ClassLoaderVisitor(boolean single) {
        this.single = single;
    }

    public void visit(ClassLoaderVisitorContext ctx) {
        this.resources.add(ctx.getVisited());
        if (this.single) ctx.abort();
    }

    /**
	 * @return The first resource collected, or null if none collected
	 */
    public ClassLoaderResource getFirstResource() {
        return this.resources.size() > 0 ? this.resources.get(0) : null;
    }

    /**
	 * @return The resources collected on the last call
	 */
    public List<ClassLoaderResource> getResources() {
        return resources;
    }

    /**
	 * @return True if this is a first resource only collector, otherwise false
	 */
    public boolean isSingle() {
        return single;
    }
}
