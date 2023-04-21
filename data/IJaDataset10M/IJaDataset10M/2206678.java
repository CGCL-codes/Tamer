package org.eclipse.core.internal.variables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.osgi.util.NLS;

/**
 * Performs string substitution for context and value variables.
 */
public class StringSubstitutionEngine {

    private static final String VARIABLE_START = "${";

    private static final char VARIABLE_END = '}';

    private static final char VARIABLE_ARG = ':';

    private static final int SCAN_FOR_START = 0;

    private static final int SCAN_FOR_END = 1;

    /**
	 * Resulting string
	 */
    private StringBuffer fResult;

    /**
	 * Whether substitutions were performed
	 */
    private boolean fSubs;

    /**
	 * Stack of variables to resolve
	 */
    private Stack fStack;

    class VariableReference {

        private StringBuffer fText;

        public VariableReference() {
            fText = new StringBuffer();
        }

        public void append(String text) {
            fText.append(text);
        }

        public String getText() {
            return fText.toString();
        }
    }

    /**
	 * Performs recursive string substitution and returns the resulting string.
	 * 
	 * @param expression expression to resolve
	 * @param reportUndefinedVariables whether to report undefined variables as an error
	 * @param manager registry of variables
	 * @return the resulting string with all variables recursively
	 *  substituted
	 * @exception CoreException if unable to resolve a referenced variable or if a cycle exists
	 *  in referenced variables
	 */
    public String performStringSubstitution(String expression, boolean reportUndefinedVariables, boolean resolveVariables, IStringVariableManager manager) throws CoreException {
        substitute(expression, reportUndefinedVariables, resolveVariables, manager);
        List resolvedVariableSets = new ArrayList();
        while (fSubs) {
            HashSet resolved = substitute(fResult.toString(), reportUndefinedVariables, true, manager);
            for (int i = resolvedVariableSets.size() - 1; i >= 0; i--) {
                HashSet prevSet = (HashSet) resolvedVariableSets.get(i);
                if (prevSet.equals(resolved)) {
                    HashSet conflictingSet = new HashSet();
                    for (; i < resolvedVariableSets.size(); i++) conflictingSet.addAll((HashSet) resolvedVariableSets.get(i));
                    StringBuffer problemVariableList = new StringBuffer();
                    for (Iterator it = conflictingSet.iterator(); it.hasNext(); ) {
                        problemVariableList.append(it.next().toString());
                        problemVariableList.append(", ");
                    }
                    problemVariableList.setLength(problemVariableList.length() - 2);
                    throw new CoreException(new Status(IStatus.ERROR, VariablesPlugin.getUniqueIdentifier(), VariablesPlugin.REFERENCE_CYCLE_ERROR, NLS.bind(VariablesMessages.StringSubstitutionEngine_4, new String[] { problemVariableList.toString() }), null));
                }
            }
            resolvedVariableSets.add(resolved);
        }
        return fResult.toString();
    }

    /**
	 * Performs recursive string validation to ensure that all of the variables
	 * contained in the expression exist
	 * @param expression expression to validate
	 * @param manager registry of variables
	 * @exception CoreException if a referenced variable does not exist or if a cycle exists
	 *  in referenced variables
	 */
    public void validateStringVariables(String expression, IStringVariableManager manager) throws CoreException {
        performStringSubstitution(expression, true, false, manager);
    }

    /**
	 * Makes a substitution pass of the given expression returns a Set of the variables that were resolved in this
	 *  pass
	 *  
	 * @param expression source expression
	 * @param reportUndefinedVariables whether to report undefined variables as an error
	 * @param resolveVariables whether to resolve the value of any variables
	 * @exception CoreException if unable to resolve a variable
	 */
    private HashSet substitute(String expression, boolean reportUndefinedVariables, boolean resolveVariables, IStringVariableManager manager) throws CoreException {
        fResult = new StringBuffer(expression.length());
        fStack = new Stack();
        fSubs = false;
        HashSet resolvedVariables = new HashSet();
        int pos = 0;
        int state = SCAN_FOR_START;
        while (pos < expression.length()) {
            switch(state) {
                case SCAN_FOR_START:
                    int start = expression.indexOf(VARIABLE_START, pos);
                    if (start >= 0) {
                        int length = start - pos;
                        if (length > 0) {
                            fResult.append(expression.substring(pos, start));
                        }
                        pos = start + 2;
                        state = SCAN_FOR_END;
                        fStack.push(new VariableReference());
                    } else {
                        fResult.append(expression.substring(pos));
                        pos = expression.length();
                    }
                    break;
                case SCAN_FOR_END:
                    start = expression.indexOf(VARIABLE_START, pos);
                    int end = expression.indexOf(VARIABLE_END, pos);
                    if (end < 0) {
                        VariableReference tos = (VariableReference) fStack.peek();
                        tos.append(expression.substring(pos));
                        pos = expression.length();
                    } else {
                        if (start >= 0 && start < end) {
                            int length = start - pos;
                            if (length > 0) {
                                VariableReference tos = (VariableReference) fStack.peek();
                                tos.append(expression.substring(pos, start));
                            }
                            pos = start + 2;
                            fStack.push(new VariableReference());
                        } else {
                            VariableReference tos = (VariableReference) fStack.pop();
                            String substring = expression.substring(pos, end);
                            tos.append(substring);
                            resolvedVariables.add(substring);
                            pos = end + 1;
                            String value = resolve(tos, reportUndefinedVariables, resolveVariables, manager);
                            if (value == null) {
                                value = "";
                            }
                            if (fStack.isEmpty()) {
                                fResult.append(value);
                                state = SCAN_FOR_START;
                            } else {
                                tos = (VariableReference) fStack.peek();
                                tos.append(value);
                            }
                        }
                    }
                    break;
            }
        }
        while (!fStack.isEmpty()) {
            VariableReference tos = (VariableReference) fStack.pop();
            if (fStack.isEmpty()) {
                fResult.append(VARIABLE_START);
                fResult.append(tos.getText());
            } else {
                VariableReference var = (VariableReference) fStack.peek();
                var.append(VARIABLE_START);
                var.append(tos.getText());
            }
        }
        return resolvedVariables;
    }

    /**
	 * Resolve and return the value of the given variable reference,
	 * possibly <code>null</code>. 
	 * 
	 * @param var
	 * @param reportUndefinedVariables whether to report undefined variables as
	 *  an error
	 * @param resolveVariables whether to resolve the variables value or just to validate that this variable is valid
	 * @param manager variable registry
	 * @return variable value, possibly <code>null</code>
	 * @exception CoreException if unable to resolve a value
	 */
    private String resolve(VariableReference var, boolean reportUndefinedVariables, boolean resolveVariables, IStringVariableManager manager) throws CoreException {
        String text = var.getText();
        int pos = text.indexOf(VARIABLE_ARG);
        String name = null;
        String arg = null;
        if (pos > 0) {
            name = text.substring(0, pos);
            pos++;
            if (pos < text.length()) {
                arg = text.substring(pos);
            }
        } else {
            name = text;
        }
        IValueVariable valueVariable = manager.getValueVariable(name);
        if (valueVariable == null) {
            IDynamicVariable dynamicVariable = manager.getDynamicVariable(name);
            if (dynamicVariable == null) {
                if (reportUndefinedVariables) {
                    throw new CoreException(new Status(IStatus.ERROR, VariablesPlugin.getUniqueIdentifier(), VariablesPlugin.INTERNAL_ERROR, NLS.bind(VariablesMessages.StringSubstitutionEngine_3, new String[] { name }), null));
                }
                return getOriginalVarText(var);
            }
            if (resolveVariables) {
                fSubs = true;
                return dynamicVariable.getValue(arg);
            }
            return getOriginalVarText(var);
        }
        if (arg == null) {
            if (resolveVariables) {
                fSubs = true;
                return valueVariable.getValue();
            }
            return getOriginalVarText(var);
        }
        throw new CoreException(new Status(IStatus.ERROR, VariablesPlugin.getUniqueIdentifier(), VariablesPlugin.INTERNAL_ERROR, NLS.bind(VariablesMessages.StringSubstitutionEngine_4, new String[] { valueVariable.getName() }), null));
    }

    private String getOriginalVarText(VariableReference var) {
        StringBuffer res = new StringBuffer(var.getText());
        res.insert(0, VARIABLE_START);
        res.append(VARIABLE_END);
        return res.toString();
    }
}
