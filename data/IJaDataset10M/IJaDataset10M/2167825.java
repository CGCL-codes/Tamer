package net.sf.stump.eclipse.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IEditorInput;

/**
 * @author Joni Suominen
 */
public class JavaProjectHelper {

    public List<String> getFullyQualifiedNames(Collection<IType> types) {
        List<String> testNames = new ArrayList<String>();
        for (IType type : types) {
            testNames.add(type.getFullyQualifiedName());
        }
        return testNames;
    }

    public String getSimpleTypeName(IEditorInput input) {
        int index = input.getName().lastIndexOf(".");
        return input.getName().substring(0, index);
    }

    public String getSimpleTypeName(IFile file) {
        int index = file.getName().lastIndexOf(".");
        return file.getName().substring(0, index);
    }
}
