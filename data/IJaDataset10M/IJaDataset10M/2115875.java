package clp.metadata;

import clp.ast.ASTNode;

public class AspectMetadata {

    private MethodMetadata method = null;

    private boolean execBeforeMethod = false;

    private boolean execAfterMethod = false;

    private ASTNode codeToExec = null;

    /**
	 * @return Returns the codeToExec.
	 */
    public ASTNode getCodeToExec() {
        return codeToExec;
    }

    /**
	 * @param codeToExec The codeToExec to set.
	 */
    public void setCodeToExec(ASTNode codeToExec) {
        this.codeToExec = codeToExec;
    }

    /**
	 * @return Returns the execAfterMethod.
	 */
    public boolean isExecAfterMethod() {
        return execAfterMethod;
    }

    /**
	 * @param execAfterMethod The execAfterMethod to set.
	 */
    public void setExecAfterMethod(boolean execAfterMethod) {
        this.execAfterMethod = execAfterMethod;
    }

    /**
	 * @return Returns the execBeforeMethod.
	 */
    public boolean isExecBeforeMethod() {
        return execBeforeMethod;
    }

    /**
	 * @param execBeforeMethod The execBeforeMethod to set.
	 */
    public void setExecBeforeMethod(boolean execBeforeMethod) {
        this.execBeforeMethod = execBeforeMethod;
    }

    /**
	 * @return Returns the method.
	 */
    public MethodMetadata getMethod() {
        return method;
    }

    /**
	 * @param method The method to set.
	 */
    public void setMethod(MethodMetadata method) {
        this.method = method;
    }
}
