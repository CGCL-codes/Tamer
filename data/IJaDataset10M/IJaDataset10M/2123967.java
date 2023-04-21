package net.sourceforge.purrpackage.model;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.PackageData;
import net.sourceforge.cobertura.coveragedata.SourceFileData;

/**
 * Factory methods for the classes of per-package coverage nodes; this enables
 * extending the classes of nodes. Mainly used for testing an error reporting
 * feature.
 */
public class CoverageDataPairFactory {

    public PackageDataPair makePackageDataPair(PackageData data, PackageData samePackageData, ProjectDataPair parent) {
        return new PackageDataPair(data, samePackageData, parent);
    }

    public SourceFileDataPair makeSourceFileDataPair(SourceFileData data, SourceFileData samePackageData, PackageDataPair parent) {
        return new SourceFileDataPair(data, samePackageData, parent);
    }

    public ClassDataPair makeClassDataPair(ClassData data, ClassData samePackageData, SourceFileDataPair parent) {
        return new ClassDataPair(data, samePackageData, parent);
    }

    public MethodDataPair makeMethodDataPair(MethodData data, MethodData samePackageData, ClassDataPair parent) {
        return new MethodDataPair(data, samePackageData, parent);
    }

    public LineDataPair makeLineDataPair(LineData data, LineData samePackageData, MethodDataPair parent) {
        return new LineDataPair(data, samePackageData, parent);
    }
}
