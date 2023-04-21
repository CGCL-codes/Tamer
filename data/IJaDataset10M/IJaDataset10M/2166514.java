package de.mpiwg.vspace.navigation.util.provider;

import java.io.File;

public interface DefaultNavigationEntryProvider {

    public String getNavigationEntryText(String propName);

    public File getNavigationEntryImageFile(String propName);
}
