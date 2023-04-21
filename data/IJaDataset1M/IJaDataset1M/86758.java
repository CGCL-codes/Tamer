package org.plazmaforge.studio.devassistant.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.plazmaforge.studio.devassistant.core.jdt.CodeUtils;

/**
 * CreateInterfaceProxyAction.java
 * 
 */
public class CreateInterfaceProxyAction extends ActionOnType {

    public void run(IAction action, IType type) throws Exception {
        SelectionDialog dlg = JavaUI.createTypeDialog(getShell(), new ProgressMonitorDialog(getShell()), SearchEngine.createWorkspaceScope(), IJavaElementSearchConstants.CONSIDER_INTERFACES, false);
        dlg.setTitle("Interface");
        dlg.setMessage("Select an interface to delegate...");
        dlg.open();
        if (dlg.getResult() != null) {
            IProgressMonitor progressMonitor = null;
            IType itype = (IType) (dlg.getResult()[0]);
            String fullItypeName = itype.getFullyQualifiedName();
            String itypeName = itype.getElementName();
            String instName = " __" + itype.getElementName();
            String code = null;
            code = "private " + fullItypeName + " " + instName + " = null;";
            code = CodeUtils.format(code);
            type.createField(code, null, true, progressMonitor);
            code = "public " + type.getElementName() + "(" + fullItypeName + " inst) { super(); " + instName + " = inst; }";
            code = CodeUtils.format(code);
            type.createMethod(code, null, true, progressMonitor);
            IMethod[] methods = itype.getMethods();
            for (int i = 0; i < methods.length; i++) {
                IMethod m = methods[i];
                String mname = m.getElementName();
                String msig = m.getSignature();
                int pcount = Signature.getParameterCount(msig);
                String[] psigs = Signature.getParameterTypes(msig);
                String[] pnames = new String[psigs.length];
                for (int j = 0; j < pnames.length; j++) {
                    pnames[j] = "arg" + j;
                }
                String rtype = Signature.toString(Signature.getReturnType(msig)).replaceAll("/", ".");
                String[] exsigs = m.getExceptionTypes();
                StringBuffer buf = new StringBuffer();
                buf.append("public " + rtype + " " + mname + "(");
                for (int j = 0; j < pnames.length; j++) {
                    buf.append(Signature.toString(psigs[j]).replaceAll("/", ".") + " " + pnames[j]);
                    if (j < pnames.length - 1) {
                        buf.append(",");
                    }
                }
                buf.append(") ");
                if (exsigs.length > 0) {
                    buf.append(" throws ");
                    for (int j = 0; j < exsigs.length; j++) {
                        buf.append(Signature.toString(exsigs[j]).replaceAll("/", "."));
                        if (j < exsigs.length - 1) {
                            buf.append(",");
                        }
                    }
                }
                buf.append(" {");
                if (!"void".equals(rtype)) {
                    buf.append("return ");
                }
                buf.append(instName + "." + mname + "(");
                for (int j = 0; j < pnames.length; j++) {
                    buf.append(pnames[j]);
                    if (j < pnames.length - 1) {
                        buf.append(",");
                    }
                }
                buf.append(");");
                buf.append("}");
                code = CodeUtils.format(buf.toString());
                type.createMethod(code, null, true, progressMonitor);
            }
        }
    }
}
