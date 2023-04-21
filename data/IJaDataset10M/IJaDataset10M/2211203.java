package net.sf.filePiper.model;

import java.io.File;
import net.sf.sfac.file.FilePathUtils;
import net.sf.sfac.file.InvalidPathException;

/**
 * Object giving information about an input stream. <
 * 
 * @author berol
 */
public class InputFileInfo {

    private File basePath;

    private File input;

    private int inputCount;

    private String proposedPath;

    private String proposedName;

    private String proposedExtension;

    public InputFileInfo(File base, File inputFile) {
        basePath = base;
        input = inputFile;
        if (inputFile != null) inputCount = 1;
        File parentFile = inputFile.getAbsoluteFile().getParentFile();
        proposedPath = (parentFile == null) ? null : parentFile.getAbsolutePath();
        String fullName = inputFile.getName();
        int lastDotIndex = fullName.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            proposedName = fullName.substring(0, lastDotIndex);
            proposedExtension = fullName.substring(lastDotIndex + 1);
        } else {
            proposedName = fullName;
            proposedExtension = "";
        }
    }

    /**
     * Simplest method to propose a new name for the output file: just add a suffix to the current proposed name.
     * 
     * @param suffix
     */
    public void addProposedNameSuffix(String suffix) {
        proposedName += "_" + suffix;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("InputFileInfo@");
        sb.append(System.identityHashCode(this));
        sb.append("[");
        if (inputCount == 1) {
            sb.append(input.getAbsolutePath());
        } else {
            sb.append(inputCount);
            sb.append(" files, current=");
            sb.append(input.getAbsolutePath());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Merge two files (MANY to ONE cardinality).
     * 
     * @param newLine
     *            the merged lines;
     */
    public void mergeInfo(InputFileInfo mergedInfo) {
        inputCount += mergedInfo.inputCount;
    }

    /**
     * Get the full proposed path = proposedPath + proposedName + proposedExtension (with correct "/", "\" or "." separators and
     * null handing).
     */
    public String getProposedFullPath() {
        StringBuffer fullPath = new StringBuffer();
        if ((proposedPath != null) && (proposedPath.length() > 0)) {
            fullPath.append(proposedPath);
            fullPath.append(File.separatorChar);
        }
        fullPath.append(proposedName);
        if ((proposedExtension != null) && (proposedExtension.length() > 0)) {
            fullPath.append(".");
            fullPath.append(proposedExtension);
        }
        return fullPath.toString();
    }

    /**
     * Get the proposed full path minus the base input path. <br>
     * Note: There is a base input path only for multiple files are selected as input.
     */
    public String getProposedRelativePath() {
        StringBuffer relativePath = new StringBuffer();
        if ((proposedPath != null) && (proposedPath.length() > 0) && (basePath != null)) {
            FilePathUtils utils = getFilePathUtils();
            try {
                String path = utils.getRelativeFilePath(proposedPath);
                if ((path != null) && (path.length() > 0) && !".".equals(path)) {
                    relativePath.append(path);
                    relativePath.append(File.separatorChar);
                }
            } catch (InvalidPathException e) {
                throw new IllegalArgumentException("Inavlid path: " + input.getAbsolutePath(), e);
            }
        }
        relativePath.append(proposedName);
        if ((proposedExtension != null) && (proposedExtension.length() > 0)) {
            relativePath.append(".");
            relativePath.append(proposedExtension);
        }
        return relativePath.toString();
    }

    public void setProposedRelativePath(String newProposedRelativePath) {
    }

    private FilePathUtils getFilePathUtils() {
        return new FilePathUtils((basePath == null) ? input.getParentFile() : basePath);
    }

    public String getInputRelativePath() {
        try {
            if (basePath != null) return getFilePathUtils().getRelativeFilePath(input.getAbsolutePath());
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Inavlid path: " + input.getAbsolutePath(), e);
        }
        return input.getAbsolutePath();
    }

    public File getInput() {
        return input;
    }

    public void setInput(File newInput) {
        input = newInput;
    }

    public File getBasePath() {
        return basePath;
    }

    public void setBasePath(File newBasePath) {
        basePath = newBasePath;
    }

    public String getProposedExtension() {
        return proposedExtension;
    }

    public void setProposedExtension(String newProposedExtension) {
        proposedExtension = newProposedExtension;
    }

    public String getProposedName() {
        return proposedName;
    }

    public void setProposedName(String newProposedName) {
        proposedName = newProposedName;
    }

    public String getProposedPath() {
        return proposedPath;
    }

    public void setProposedPath(String newProposedPath) {
        proposedPath = newProposedPath;
    }

    public int getInputCount() {
        return inputCount;
    }
}
