package org.xmlvm.proc.lib;

import java.util.ArrayList;
import java.util.List;
import org.xmlvm.main.Targets;
import org.xmlvm.util.universalfile.UniversalFile;
import org.xmlvm.util.universalfile.UniversalFileCreator;

/**
 * Android compatibility library, which is mostly developed against the
 * Java-based cocoa library and is for use in iphone targets only.
 */
public class IPhoneAndroidLibrary extends Library {

    private static final String ONE_JAR_LOCATION = "/lib/iphone-android-java.jar";

    private static final String FILE_SYSTEM_LOCATION = "bin-android2iphone";

    @Override
    public boolean isMonolithic() {
        return true;
    }

    @Override
    protected UniversalFile getLibraryUncached() {
        UniversalFile result = UniversalFileCreator.createDirectory(ONE_JAR_LOCATION, prepareTempJar(FILE_SYSTEM_LOCATION, ""));
        return result;
    }

    @Override
    protected List<Targets> includedTargets() {
        List<Targets> included = new ArrayList<Targets>();
        included.add(Targets.IPHONECANDROID);
        return included;
    }

    @Override
    protected List<Targets> excludedTargets() {
        return null;
    }
}
