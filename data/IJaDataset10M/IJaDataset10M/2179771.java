package org.twdata.pkgscanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Accepts ExportPackage objects and creates a sorted list with duplicates removed.
 * <p/>
 * Explicit version numbers are always preferred over "unknown".
 * When duplicate jars are found with different versions associated, a warning will be logged.
 *
 * @since 0.7.11
 */
public class ExportPackageListBuilder {

    private static final Logger log = LoggerFactory.getLogger(ExportPackageListBuilder.class);

    private Map<String, ExportPackage> packageMap = new HashMap<String, ExportPackage>();

    public void add(final ExportPackage exportPackage) {
        String packageName = exportPackage.getPackageName();
        ExportPackage currentExportPackage = packageMap.get(packageName);
        if (currentExportPackage == null) {
            packageMap.put(packageName, exportPackage);
        } else {
            if (exportPackage.getVersion() == null) {
                if (currentExportPackage.getVersion() != null) {
                    logDuplicateOneVersion(exportPackage, currentExportPackage, currentExportPackage.getVersion());
                }
            } else {
                if (currentExportPackage.getVersion() == null) {
                    packageMap.put(packageName, exportPackage);
                    logDuplicateOneVersion(exportPackage, currentExportPackage, exportPackage.getVersion());
                } else {
                    if (!currentExportPackage.getVersion().equals(exportPackage.getVersion())) {
                        logDuplicateWarning(exportPackage, currentExportPackage);
                    }
                    packageMap.put(packageName, exportPackage);
                }
            }
        }
    }

    private void logDuplicateOneVersion(ExportPackage exportPackage1, ExportPackage exportPackage2, String acceptedVersion) {
        log.info("Package Scanner found duplicates for package '" + exportPackage1.getPackageName() + "' - accepting version '" + acceptedVersion + "'. Files: " + exportPackage1.getLocation().getName() + " and " + exportPackage2.getLocation().getName() + "\n  '" + exportPackage1.getLocation().getAbsolutePath() + "'" + "\n  '" + exportPackage2.getLocation().getAbsolutePath() + "'");
    }

    private void logDuplicateWarning(final ExportPackage exportPackage1, final ExportPackage exportPackage2) {
        log.warn("Package Scanner found duplicates for package '" + exportPackage1.getPackageName() + "' with different versions. Files: " + exportPackage1.getLocation().getName() + " and " + exportPackage2.getLocation().getName() + "\n  '" + exportPackage1.getLocation().getAbsolutePath() + "'" + "\n  '" + exportPackage2.getLocation().getAbsolutePath() + "'");
    }

    /**
     * Returns the list of packages, ordered by package name.
     *
     * @return the list of packages, ordered by package name.
     */
    public List<ExportPackage> getPackageList() {
        List<ExportPackage> packageList = new ArrayList<ExportPackage>(packageMap.values());
        Collections.sort(packageList);
        return packageList;
    }
}
