package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.SortedSet;

public class FolderDiff {

    private final File _sourceFolder;

    private final File _targetFolder;

    private final SortedSet<PathInfo> _filesDifferent;

    private final SortedSet<PathInfo> _filesSourceOnly;

    private final SortedSet<PathInfo> _filesTargetOnly;

    private final SortedSet<PathInfo> _filesEqual;

    private final SortedSet<PathInfo> _filesAll;

    FolderDiff(File sourceFolder, File targetFolder, SortedSet<PathInfo> all, SortedSet<PathInfo> equal, SortedSet<PathInfo> different, SortedSet<PathInfo> sourceOnly, SortedSet<PathInfo> targetOnly) {
        _sourceFolder = sourceFolder;
        _targetFolder = targetFolder;
        _filesDifferent = different;
        _filesSourceOnly = sourceOnly;
        _filesTargetOnly = targetOnly;
        _filesEqual = equal;
        _filesAll = all;
    }

    public File getSourceFolder() {
        return _sourceFolder;
    }

    public File getTargetFolder() {
        return _targetFolder;
    }

    public SortedSet<PathInfo> getDifferent() {
        return _filesDifferent;
    }

    public SortedSet<PathInfo> getSourceOnly() {
        return _filesSourceOnly;
    }

    public SortedSet<PathInfo> getTargetOnly() {
        return _filesTargetOnly;
    }

    public SortedSet<PathInfo> getEqual() {
        return _filesEqual;
    }

    public SortedSet<PathInfo> getAll() {
        return _filesAll;
    }
}
