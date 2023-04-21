package net.planetrenner.picit.sdcc.managedbuild.scannerconfig;

import java.util.ArrayList;
import java.util.List;
import net.planetrenner.picit.sdcc.preferences.PropertyManager;
import net.planetrenner.picit.sdcc.preferences.SdccProperties;
import org.eclipse.cdt.make.core.scannerconfig.ScannerInfoTypes;
import org.eclipse.cdt.make.internal.core.scannerconfig2.PerProjectSICollector;
import org.eclipse.core.resources.IProject;

public class SdccScannerInfoCollector extends PerProjectSICollector {

    @Override
    public List<?> getCollectedScannerInfo(Object resource, ScannerInfoTypes type) {
        if (!ScannerInfoTypes.TARGET_SPECIFIC_OPTION.equals(type)) {
            return super.getCollectedScannerInfo(resource, type);
        }
        IProject project = (IProject) resource;
        SdccProperties properties = PropertyManager.getSdccProperties(project);
        List<String> targetSpecificOptions = new ArrayList<String>();
        targetSpecificOptions.add("-m" + properties.getPort().getArgument());
        targetSpecificOptions.add("-p" + properties.getProcessor());
        return targetSpecificOptions;
    }
}
