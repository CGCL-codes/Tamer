package edu.rice.cs.drjava.project;

import java.util.List;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import edu.rice.cs.drjava.Version;
import edu.rice.cs.util.Pair;
import edu.rice.cs.util.UnexpectedException;
import static edu.rice.cs.util.StringOps.*;
import static edu.rice.cs.util.FileOps.*;

/**
 * The project file builder is responsible for
 * encoding the project file based on the information
 * given to it through the public interface offered.
 */
public class ProjectFileBuilder {

    private List<DocFile> _sourceFiles = new Vector<DocFile>();

    private List<DocFile> _auxFiles = new Vector<DocFile>();

    private List<String> _collapsedPaths = new Vector<String>();

    private File _buildDir = null;

    private List<File> _classpathFiles = new Vector<File>();

    private File _mainClass = null;

    private File _projRoot = null;

    private File _projectFile;

    public ProjectFileBuilder(String filename) {
        _projectFile = new File(filename);
    }

    public ProjectFileBuilder(File f) {
        _projectFile = f;
    }

    public void addSourceFile(DocumentInfoGetter getter) {
        if (!getter.isUntitled()) {
            try {
                _sourceFiles.add(docFileFromGetter(getter));
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    public void addAuxiliaryFile(DocumentInfoGetter getter) {
        if (!getter.isUntitled()) {
            try {
                _auxFiles.add(docFileFromGetter(getter));
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    public void addClasspathFile(File cp) {
        if (cp != null) _classpathFiles.add(cp);
    }

    public void addCollapsedPath(String cp) {
        if (cp != null) _collapsedPaths.add(cp);
    }

    public void setBuildDirectory(File dir) {
        _buildDir = dir;
    }

    public void setMainClass(File main) {
        _mainClass = main;
    }

    public void setProjectRoot(File root) {
        _projRoot = root;
    }

    /**
   * This method writes what information has been passed
   * to this builder so far to disk in s-expression format
   */
    public void write() throws IOException {
        FileWriter fw = new FileWriter(_projectFile);
        fw.write(";; DrJava project file, written by build " + Version.getBuildTimeString());
        fw.write(";; relative files are made relative to: " + _projectFile.getParentFile().getCanonicalFile());
        if (_projRoot != null) {
            fw.write("\n(proj-root");
            fw.write("\n" + encodeFile(_projRoot, "  ", true));
            fw.write(")");
        }
        if (!_sourceFiles.isEmpty()) {
            fw.write("\n(source");
            for (DocFile df : _sourceFiles) {
                fw.write("\n" + encodeDocFile(df, "  "));
            }
            fw.write(")");
        } else {
            fw.write("\n;; no source files");
        }
        if (!_auxFiles.isEmpty()) {
            fw.write("\n(auxiliary");
            for (DocFile df : _auxFiles) {
                fw.write("\n" + encodeDocFile(df, "  ", false));
            }
            fw.write(")");
        } else {
            fw.write("\n;; no aux files");
        }
        if (!_collapsedPaths.isEmpty()) {
            fw.write("\n(collapsed");
            for (String s : _collapsedPaths) {
                fw.write("\n  (path " + convertToLiteral(s) + ")");
            }
            fw.write(")");
        } else {
            fw.write("\n;; no collapsed branches");
        }
        if (!_classpathFiles.isEmpty()) {
            fw.write("\n(classpaths");
            for (File f : _classpathFiles) {
                fw.write("\n" + encodeFile(f, "  ", false));
            }
            fw.write(")");
        } else {
            fw.write("\n;; no classpaths files");
        }
        if (_buildDir != null) {
            fw.write("\n(build-dir");
            fw.write("\n" + encodeFile(_buildDir, "  ", true));
            fw.write(")");
        } else {
            fw.write("\n;; no build directory");
        }
        if (_mainClass != null) {
            fw.write("\n(main-class");
            fw.write("\n" + encodeFile(_mainClass, "  ", true));
            fw.write(")");
        } else {
            fw.write("\n;; no main class");
        }
        fw.close();
    }

    /**
   * @param getter The getter that can get all the info needed to
   * make the document file
   * @return the document that contains the information retrieved
   * from the getter
   */
    private DocFile docFileFromGetter(DocumentInfoGetter getter) throws IOException {
        return new DocFile(getter.getFile().getCanonicalPath(), getter.getSelection(), getter.getScroll(), getter.isActive(), getter.getPackage());
    }

    /** This encodes a normal file.  None of the special tags are added
   *  @param f the file to encode
   *  @param prefix the indent level to place the s-expression at
   *  @param relative whether this file should be made relative to the project path
   *  @return the s-expression syntax to describe the given file.
   */
    private String encodeFile(File f, String prefix, boolean relative) throws IOException {
        String path;
        if (relative) path = makeRelative(f); else path = f.getCanonicalPath();
        path = replace(path, File.separator, "/");
        return prefix + "(file (name " + convertToLiteral(path) + "))";
    }

    /** Encodes a file.  The path defaults to relative.
   *  @param f the file to encode
   *  @param prefix the indent level
   */
    private String encodeFile(File f, String prefix) throws IOException {
        return encodeFile(f, prefix, true);
    }

    /**
   * This encodes a docfile, adding all the special tags that store
   * document-specific information.
   * @param df the doc file to encode
   * @param prefix the indent level to place the s-expression at
   * @param relative whether this file should be made relative to the project path
   * @param hasDate whether to include the modification date
   * @return the s-expression syntax to describe the given docfile.
   */
    private String encodeDocFile(DocFile df, String prefix, boolean relative, boolean hasDate) throws IOException {
        String ret = "";
        String path;
        if (relative) {
            path = makeRelative(df);
        } else {
            path = df.getCanonicalPath();
        }
        path = replace(path, File.separator, "/");
        ret += prefix + "(file (name " + convertToLiteral(path) + ")";
        Pair<Integer, Integer> p1 = df.getSelection();
        Pair<Integer, Integer> p2 = df.getScroll();
        boolean active = df.isActive();
        long modDate = df.lastModified();
        if (p1 != null || p2 != null || active) {
            ret += "\n" + prefix + "      ";
        }
        if (p1 != null) {
            ret += "(select " + p1.getFirst() + " " + p1.getSecond() + ")";
        }
        if (p2 != null) {
            ret += "(scroll " + p2.getFirst() + " " + p2.getSecond() + ")";
        }
        if (hasDate && modDate > 0) {
            String s = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date(modDate));
            ret += "(mod-date " + convertToLiteral(s) + ")";
        }
        if (active) {
            ret += "(active)";
        }
        String pack = df.getPackage();
        if (pack != null) {
            ret += "\n" + prefix + "      ";
            ret += "(package " + convertToLiteral(pack) + ")";
        }
        ret += ")";
        return ret;
    }

    /**
   * encodes a doc file.  The path defaults to relative.
   * @param df the DocFile to encode
   * @param prefix the indent level
   */
    private String encodeDocFile(DocFile df, String prefix) throws IOException {
        return encodeDocFile(df, prefix, true, true);
    }

    private String encodeDocFile(DocFile df, String prefix, boolean relative) throws IOException {
        return encodeDocFile(df, prefix, relative, true);
    }

    /**
   * @param f the file whose path to make relative to the project path
   * @return the string name of the file's path relative to the project path
   */
    private String makeRelative(File f) throws IOException {
        return makeRelativeTo(f, _projectFile).getPath();
    }
}
