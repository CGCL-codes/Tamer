package org.utupiu.nibbana.waf.view;

import org.utupiu.nibbana.core.NibbanaException;
import org.utupiu.nibbana.waf.WAFEngine;

public class FileRegionBindingEntry extends AbstractRegionBindingEntry {

    private String fileSourceWithoutLocale;

    public String fileSource;

    public ScreenEntry screen;

    public String localeName;

    private FileRegionBindingEntry() {
    }

    public FileRegionBindingEntry(ScreenEntry screen, String regionName, String fileSource) throws NibbanaException {
        this.screen = screen;
        this.regionName = regionName;
        this.localeName = screen.localeName;
        this.fileSourceWithoutLocale = fileSource;
        while (fileSourceWithoutLocale.startsWith("/")) {
            fileSourceWithoutLocale = fileSourceWithoutLocale.substring(1);
        }
        this.fileSource = localeName + "/" + fileSourceWithoutLocale;
    }

    public FileRegionBindingEntry clone(String localeName) {
        FileRegionBindingEntry clone = new FileRegionBindingEntry();
        clone.regionName = this.regionName;
        String clonedScreenName = localeName + "/" + this.screen.screenNameWithoutLocale;
        clone.screen = WAFEngine.screens.get(clonedScreenName);
        clone.localeName = localeName;
        clone.fileSourceWithoutLocale = this.fileSourceWithoutLocale;
        clone.fileSource = localeName + "/" + this.fileSourceWithoutLocale;
        return clone;
    }
}
