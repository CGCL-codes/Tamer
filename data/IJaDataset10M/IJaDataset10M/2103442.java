package simple.page;

import java.lang.reflect.Constructor;
import simple.http.serve.Context;

/**
 * The <code>Composer</code> object is used to convert a requested JSP
 * source file to an executable Java object. This is essentially the
 * only object that needs to be dealt with to translate, compile, and
 * create a page object. JSP pages are translated and compiled with
 * this using a URI path reference, such as "/path/to/Source.jsp".
 * <p>
 * To avoid re-translation and compilation delays this makes use of
 * background compilation. Background compilation enables pages to be
 * requested rapidly after the first compilation, and updated with
 * and atomic operation when the JSP page or its includes change. 
 * 
 * @author Niall Gallagher
 */
public class Composer {

    /**
    * This performs background compilation of all JSP resources.
    */
    private Maintainer maintainer;

    /**
    * This is the JSP source root context for this composer object.
    */
    private Context context;

    /**
    * Constructor for the <code>Composer</code> object. This requires
    * a file system context, which is used to determine where the JSP
    * pages are acquired from. It allows URI paths like "/Example.jsp" 
    * to be mapped on to the appropriate JSP source file within the 
    * file system. This context creates a default configuraiton.
    * 
    * @param context this is the interface to the OS file system 
    */
    public Composer(Context context) throws Exception {
        this(context, new Workspace(context));
    }

    /**
    * Constructor for the <code>Composer</code> object. This requires
    * a file system context and a project configuration. With this
    * constructor the <code>Workspace</code> object, rather than
    * the file system context, is used to determine where the JSP 
    * source files are acquired from. So requests like "/Example.jsp"
    * are evaluated by the configuration in order to determine the
    * file system location. The context is passed to the page.
    * 
    * @param context this is the interface to the OS file system 
    * @param project this specifies the layout for the project
    */
    public Composer(Context context, Workspace project) throws Exception {
        this.maintainer = new Maintainer(project);
        this.context = context;
    }

    /**
    * This is used to acquire the <code>Page</code> instance for the
    * named JSP source. If the requested JSP source has not been
    * previously compiled this will perform the translation and
    * compilation process before creating a an instance of the page.
    * 
    * @param target this is the JSP source to be created as a page
    *
    * @return this returns a page object to render the JSP page
    */
    public Page compose(String target) throws Exception {
        return (Page) getConstructor(target).newInstance(new Object[] { context, target });
    }

    /**
    * This is used to acquire a factory <code>Constructor</code> for
    * the requested JSP page. This asks the background compiler for
    * the most recent sucessful build of the requested JSP.
    *
    * @param target this is the URI path referencing the JSP
    *
    * @return this returns the constructor for the compiled page
    */
    private Constructor getConstructor(String target) throws Exception {
        Class type = maintainer.retrieve(target);
        return getConstructor(type);
    }

    /**
    * This is used to acquire a factory <code>Constructor</code> for
    * the specified page class. This requires a constructor that takes
    * two arguments. A <code>Context</code> and <code>String</code>
    * object must be acceptable by the constructor for instantiation.
    *
    * @param type this is the compiled page class to instantiate
    *
    * @return this returns the constructor for the compiled page
    */
    private Constructor getConstructor(Class type) throws Exception {
        Class[] types = new Class[] { Context.class, String.class };
        return type.getDeclaredConstructor(types);
    }
}
