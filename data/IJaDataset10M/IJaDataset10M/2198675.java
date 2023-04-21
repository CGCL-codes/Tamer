package edu.princeton.wordnet.orm.interceptor;

import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import edu.princeton.wordnet.orm.hibernate.HibernateInterceptor;
import edu.princeton.wordnet.orm.jpa.JPAInterceptor;

/**
 * Interceptor instrumentation
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public abstract class Interceptor {

    protected abstract String prologueReturn();

    protected abstract String prologueClass();

    protected abstract String[] importedPackages();

    /**
	 * Logger
	 */
    private static final Logger LOG = Logger.getLogger(Interceptor.class);

    /**
	 * Intercept method
	 * 
	 * @param thisClassName
	 *            class name
	 * @param thisMethodName
	 *            method name
	 * @param thisParameterName
	 *            parameter name
	 * @param write
	 *            whether to write changes to file
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws CannotCompileException
	 */
    public void intercept(final String thisClassName, final String thisMethodName, final String thisParameterName, final boolean write) throws NotFoundException, IOException, CannotCompileException {
        final ClassPool thisPool = ClassPool.getDefault();
        thisPool.importPackage("edu.princeton.wordnet.orm.interceptor");
        for (final String thisPackage : importedPackages()) {
            thisPool.importPackage(thisPackage);
        }
        final CtClass thisClass = thisPool.get(thisClassName);
        final CtMethod thisInterceptedMethod = thisClass.getDeclaredMethod(thisMethodName);
        final String thisInterceptedName = thisMethodName + "$impl";
        thisInterceptedMethod.setName(thisInterceptedName);
        final CtMethod thisInterceptingMethod = CtNewMethod.copy(thisInterceptedMethod, thisMethodName, thisClass, null);
        final String thisReturnType = thisInterceptedMethod.getReturnType().getName();
        final StringBuffer thisMethodBody = new StringBuffer();
        thisMethodBody.append("{");
        thisMethodBody.append("\n");
        thisMethodBody.append(prologueReturn());
        thisMethodBody.append(" s=");
        thisMethodBody.append(prologueClass());
        thisMethodBody.append(".prologue(");
        thisMethodBody.append(thisParameterName);
        thisMethodBody.append(");\n");
        if (!"void".equals(thisReturnType)) {
            thisMethodBody.append(thisReturnType + " result = ");
        }
        thisMethodBody.append(thisInterceptedName + "($$);");
        thisMethodBody.append("\n");
        thisMethodBody.append(prologueClass());
        thisMethodBody.append(".epilogue(s);\n");
        if (!"void".equals(thisReturnType)) {
            thisMethodBody.append("return result;\n");
        }
        thisMethodBody.append("}");
        Interceptor.LOG.debug("interceptor:\n" + thisReturnType + " " + thisMethodName + "(...)");
        Interceptor.LOG.debug(thisMethodBody.toString());
        thisInterceptingMethod.setBody(thisMethodBody.toString());
        thisClass.addMethod(thisInterceptingMethod);
        if (write) {
            thisClass.writeFile();
        } else {
            thisClass.toClass();
        }
    }

    /**
	 * Setup interception
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws CannotCompileException
	 */
    public static void setup(final boolean JPA) throws NotFoundException, IOException, CannotCompileException {
        if (JPA) {
            new JPAInterceptor().intercept("edu.princeton.wordnet.pojos.tree.LinksTreeNode", "loadChildren", "this", false);
        } else {
            new HibernateInterceptor().intercept("edu.princeton.wordnet.pojos.tree.LinksTreeNode", "loadChildren", "this", false);
        }
    }
}
