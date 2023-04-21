package it.paolomind.pwge.python;

import it.paolomind.pwge.exceptions.GameException;
import it.paolomind.pwge.interfaces.bo.IActionEventBO;
import it.paolomind.pwge.interfaces.bo.IResourceStatusBO;
import org.python.core.PyInteger;
import org.python.core.PyJavaInstance;
import org.python.core.PyList;
import org.python.core.PyString;

public final class JythonActionEvent implements IActionEventBO {

    private IResourceStatusBO[] resources;

    private int result;

    private String message;

    public JythonActionEvent(PyInteger result, PyString message, PyList resources) {
        setupResources(resources);
        setupResult(result);
        setupMessage(message);
    }

    private void setupResult(PyInteger result) {
        this.result = result.getValue();
    }

    private void setupMessage(PyString message) {
        this.message = message.toString();
    }

    private void setupResources(PyList resources) {
        if (resources == null) this.resources = null; else {
            this.resources = new IResourceStatusBO[resources.__len__()];
            for (int i = 0; i < this.resources.length; i++) {
                PyJavaInstance pyobj = null;
                IResourceStatusBO obj = null;
                try {
                    pyobj = (PyJavaInstance) resources.__getitem__(i);
                } catch (ClassCastException e) {
                    throw new GameException("not a java object", e);
                }
                try {
                    obj = (IResourceStatusBO) pyobj.__tojava__(IResourceStatusBO.class);
                } catch (ClassCastException e) {
                    throw new GameException("not a IResourceBO object", e);
                }
                this.resources[i] = obj;
            }
        }
    }

    public String getActionMessage() {
        return message;
    }

    public int getActionResult() {
        return result;
    }

    public IResourceStatusBO[] getAffectedResources() {
        return resources;
    }
}
