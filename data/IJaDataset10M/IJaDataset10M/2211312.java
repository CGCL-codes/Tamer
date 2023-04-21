package org.apache.ws.jaxme.js;

import java.io.IOException;

/** <p>Implements a Java method.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class JavaMethod extends AbstractJavaMethod {

    private boolean isSynchronized;

    /** <p>Creates a new JavaMethod with the given name, return
   * type and protection.</p>
   */
    JavaMethod(String pName, JavaQName pType, JavaSource.Protection pProtection) {
        super(pName, pType, pProtection);
    }

    /** <p>Sets whether this method is synchronized.</p>
   */
    public void setSynchronized(boolean pSynchronized) {
        isSynchronized = pSynchronized;
    }

    /** <p>Returns whether this method is synchronized.</p>
	*/
    public boolean isSynchronized() {
        return isSynchronized;
    }

    /** <p>Adds a header line.</p>
   */
    protected void writeHeader(IndentationTarget pTarget) throws IOException {
        pTarget.indent(0);
        JavaSource.Protection protection = getProtection();
        if (protection != null && !protection.equals(JavaSource.DEFAULT_PROTECTION)) {
            pTarget.write(getProtection().toString());
            pTarget.write(" ");
        }
        if (isStatic()) {
            pTarget.write("static ");
        }
        if (isFinal()) {
            pTarget.write("final ");
        }
        if (isAbstract()) {
            pTarget.write("abstract ");
        }
        if (isSynchronized() && !pTarget.isInterface()) {
            pTarget.write("synchronized ");
        }
        pTarget.write(pTarget.asString(getType()));
        pTarget.write(" ");
        pTarget.write(getName());
        pTarget.write("(");
        Parameter[] params = getParams();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                pTarget.write(", ");
            }
            Parameter p = params[i];
            pTarget.write(pTarget.asString(p.getType()));
            pTarget.write(" ");
            pTarget.write(p.getName());
        }
        pTarget.write(")");
        JavaQName[] exceptions = getExceptions();
        for (int i = 0; i < exceptions.length; i++) {
            if (i > 0) {
                pTarget.write(", ");
            } else {
                pTarget.write(" throws ");
            }
            pTarget.write(pTarget.asString(exceptions[i]));
        }
        if (pTarget.isInterface() || isAbstract()) {
            pTarget.write(";");
        } else {
            pTarget.write(" {");
        }
        pTarget.write();
    }

    /** <p>Returns the abbreviated method signature: The method name, followed
   * by the parameter types. This is typically used in logging statements.</p>
   */
    public String getLoggingSignature() {
        StringBuffer result = new StringBuffer(getName());
        Parameter[] params = getParams();
        if (params.length > 0) {
            result.append('(');
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    result.append(',');
                }
                result.append(params[i].getType().getClassName());
            }
            result.append(')');
        }
        return result.toString();
    }

    /** <p>Returns whether the JavaMethod is void.</p>
   */
    public boolean isVoid() {
        return getType().equals(JavaQNameImpl.VOID);
    }
}
