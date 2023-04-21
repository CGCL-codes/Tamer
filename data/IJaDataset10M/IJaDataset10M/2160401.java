package org.abreslav.java2ecore.transformation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class VariableResolver extends ClasspathVariableInitializer {

    public static final String VARIABLE = "Java2Ecore";

    @Override
    public void initialize(String variable) {
        try {
            URL entry = Platform.getBundle("org.abreslav.java2ecore.transformation").getEntry("/lib/java2ecore.jar");
            try {
                URL fileURL = FileLocator.toFileURL(entry);
                String absolutePath = new File(fileURL.getPath()).getAbsolutePath();
                JavaCore.setClasspathVariable(variable, Path.fromOSString(absolutePath), null);
            } catch (IOException e) {
                JavaCore.setClasspathVariable(variable, Path.fromOSString("Cannot resolve jar path"), null);
            }
        } catch (JavaModelException e) {
        }
    }
}
