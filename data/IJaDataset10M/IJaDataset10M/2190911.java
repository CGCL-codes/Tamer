package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io;

import java.util.Iterator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;

public class OutputNode extends IONode {

    private String output;

    public OutputNode() {
        super("Output: <not specified>");
    }

    public void setOutput(String output) {
        this.output = output;
        setName("Output: " + this.output);
        Iterator<IOperationChangedListener> it = OperationsManager.getInstance().getOperationChangedListeners();
        while (it.hasNext()) {
            it.next().update(this);
        }
    }

    public String getOutput() {
        return output;
    }

    public ImageDescriptor getImageDescriptor() {
        return FlexiProviderOperationsPlugin.getImageDescriptor("/icons/16x16/folder_outbox.png");
    }
}
