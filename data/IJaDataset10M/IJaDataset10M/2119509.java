package net.community.chest.apache.maven.helpers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import net.community.chest.CoVariantReturn;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * <P>Provides a <code>filePath</code> attribute that can be used to specify
 * the location to/from which this project was saved/load.</P></BR>
 * 
 * <P><B>Note:</B> the to/from XML conversions do not include the file path in
 * the processed/generated XML</P>
 * 
 * @author Lyor G.
 * @since Aug 14, 2008 10:22:39 AM
 */
public class BuildProjectFile extends BuildProject implements ParentTargetResolver {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5295326989230164789L;

    public BuildProjectFile() {
        super();
    }

    private String _filePath;

    public String getFilePath() {
        return _filePath;
    }

    public void setFilePath(String filePath) {
        _filePath = filePath;
    }

    public BuildProjectFile(String path) throws Exception {
        super(path);
        setFilePath(path);
    }

    public BuildProjectFile(File f) throws Exception {
        this(f.getAbsolutePath());
    }

    public BuildProjectFile(URL url) throws Exception {
        super(url);
        final URI uri = url.toURI();
        final File f = new File(uri);
        setFilePath(f.getAbsolutePath());
    }

    public BuildProjectFile(Document doc) throws Exception {
        super(doc);
    }

    public BuildProjectFile(Element elem) throws Exception {
        super(elem);
    }

    public BuildProjectFile(InputStream in) throws Exception {
        super(in);
    }

    @Override
    @CoVariantReturn
    public BuildProjectFile resolveParentProject(final BuildProject proj) throws Exception {
        final String filePath = getFilePath();
        if ((null == filePath) || (filePath.length() <= 0)) throw new IllegalStateException("resolveParentProject(" + filePath + ") no file path set");
        if (proj != this) throw new IllegalArgumentException("resolveParentProject(" + filePath + ") resolving project not same as 'this' instance");
        final ParentTargetDetails parent = proj.getParentTarget();
        if (null == parent) return null;
        final File curFile = new File(filePath), curDir = curFile.getParentFile(), parDir = curDir.getParentFile();
        final String curName = curFile.getName();
        final String relPath = parent.getRelativePath(), effSubPath;
        if ((null == relPath) || (relPath.length() <= 0)) {
            final String parentId = parent.getArtifactId();
            if ((null == parentId) || (parentId.length() <= 0)) throw new IllegalArgumentException("resolveParentProject(" + filePath + ") no parent artifact ID specified");
            effSubPath = "parent";
        } else {
            throw new UnsupportedOperationException("resolveParentProject(" + filePath + ") relative path=" + relPath + " N/A");
        }
        final File effDir = new File(parDir, effSubPath), parFile = new File(effDir, curName);
        final String parPath = parFile.getAbsolutePath();
        return new BuildProjectFile(parPath);
    }

    private BuildProjectFile _parentProject;

    public BuildProjectFile getParentProject() {
        return _parentProject;
    }

    public void setParentProject(BuildProjectFile parentProject) {
        _parentProject = parentProject;
    }

    public BuildProjectFile resolveParentProject() throws Exception {
        if (null == _parentProject) _parentProject = resolveParentProject(this);
        return _parentProject;
    }

    /**
	 * Loads all POM(s) starting from given one and following all parents
	 * @param filePath The initial POM file
	 * @return A {@link Map} whose key=the POM file path (case <U>insensitive</U>),
	 * value=the {@link BuildProjectFile} that was loaded from that POM
	 * @throws Exception If failed to load any POM (initial or follow-up parent)
	 */
    public static final Map<String, BuildProjectFile> loadAllPOMs(final String filePath) throws Exception {
        final BuildProjectFile rootProj = new BuildProjectFile(filePath);
        final Map<String, BuildProjectFile> res = new TreeMap<String, BuildProjectFile>(String.CASE_INSENSITIVE_ORDER);
        for (BuildProjectFile curProj = rootProj; curProj != null; curProj = curProj.resolveParentProject()) {
            final String pomPath = curProj.getFilePath();
            if ((null == pomPath) || (pomPath.length() <= 0)) throw new IllegalStateException("loadAllPOMs(" + filePath + ") no file path specified in intermediate instance");
            final BuildProjectFile prev = res.put(pomPath, curProj);
            if (prev != null) throw new IllegalStateException("loadAllPOMs(" + filePath + ") duplicate entries for POM=" + pomPath);
        }
        return res;
    }
}
