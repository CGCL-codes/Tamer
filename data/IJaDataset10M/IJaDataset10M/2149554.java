package com.koutra.dist.proc.designer;

import java.io.File;
import java.util.ArrayList;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import com.koutra.dist.proc.designer.preferences.PreferencesConstants;

public class KoutraXformClasspathContainer implements IClasspathContainer {

    public static final Path ID = new Path("com.koutra.dist.proc.designer.koutraXformClasspathContainerInitializer");

    private IPath containerPath;

    public KoutraXformClasspathContainer(IPath containerPath, IJavaProject project) {
        this.containerPath = containerPath;
    }

    @Override
    public IClasspathEntry[] getClasspathEntries() {
        ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
        String libPath = KoutraXformPlugin.getDefault().getPreferenceStore().getString(PreferencesConstants.KoutraXformLibLocation);
        File f = new File(libPath);
        if (!f.isDirectory()) return new IClasspathEntry[0];
        File[] libFiles = f.listFiles();
        for (File libFile : libFiles) {
            if (!libFile.getName().endsWith(".jar")) continue;
            entryList.add(JavaCore.newLibraryEntry(new Path(libFile.getAbsolutePath()), null, null));
        }
        IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
        return (IClasspathEntry[]) entryList.toArray(entryArray);
    }

    @Override
    public String getDescription() {
        return Messages.KoutraXformClasspathContainer_KoutraXformLibDescription;
    }

    @Override
    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    @Override
    public IPath getPath() {
        return containerPath;
    }

    public boolean isValid() {
        String value = KoutraXformPlugin.getDefault().getPreferenceStore().getString(PreferencesConstants.KoutraXformLibLocation);
        File f = new File(value);
        return f.isDirectory();
    }
}
