package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.ui.IEditorInput;

public interface DataResourceUser {

    public void loadData(DataInputStream dis) throws IOException;

    public void saveData(DataOutputStream dos) throws IOException;

    public IEditorInput getEditorInput();
}
