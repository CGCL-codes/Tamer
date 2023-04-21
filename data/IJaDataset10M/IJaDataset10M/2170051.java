package cz.cuni.mff.ksi.jinfer.projecttype;

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.properties.JInferCustomizerProvider;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Represents jInfer project in Projects window.
 * @author sviro
 */
public class JInferProject implements Project {

    /**
   * Name of output folder in jInfer project file hierarchy saved on disk.
   */
    public static final String OUTPUT_DIR = "output";

    /**
   * Name of jInfer project property file.
   */
    public static final String JINFER_PROJECT_NAME_PROPERTY = "jInferProjectProperty";

    private static final Logger LOG = Logger.getLogger(JInferProject.class);

    private final FileObject projectDir;

    private final ProjectState state;

    private Lookup lookup;

    private Input input;

    private Properties properties;

    /**
   * Default jInfer Project contructor. Creates new jInfer project located in
   * path definted by projectDir parameter.
   * @param projectDir Path to the project directory on disk.
   * @param state {@link ProjectState State} of the project.
   *
   * @see ProjectState
   */
    public JInferProject(final FileObject projectDir, final ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = Lookups.fixed(new Object[] { this, state, loadProperties(), loadInput(), new OutputHandlerImpl(this), new JInferCustomizerProvider(this), new JInferProjectInformation(this), new JInferLogicalView(this), new ActionProviderImpl(this), new JInferCopyOperation(), new JInferDeleteOperation(this), new JInferMoveOrRenameOperation(this) });
        }
        return lookup;
    }

    /**
   * Get {@link FileObject} of output folder. If not exist and create is true, then it's created.
   * 
   * @param create If folder is created when it not exist.
   * @return FileObject of output folder.
   */
    public FileObject getOutputFolder(final boolean create) {
        FileObject result = projectDir.getFileObject(OUTPUT_DIR);
        if (result != null && result.isData()) {
            try {
                result.delete();
            } catch (IOException ex) {
                LOG.error("file " + result.getPath() + "cannot be deleted.", ex);
                throw new RuntimeException(ex);
            }
            result = null;
        }
        if (result == null && create) {
            try {
                result = projectDir.createFolder(OUTPUT_DIR);
            } catch (IOException ex) {
                LOG.error("Output folder cannot be created.", ex);
            }
        }
        return result;
    }

    /**
   * Loads jInfer project properties from Properties file in jinferproject folder.
   *
   * @return Properties loaded from properties file of project.
   */
    private Properties loadProperties() {
        if (properties == null) {
            final FileObject fob = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR + "/" + JInferProjectFactory.PROJECT_PROPFILE);
            properties = new NotifyProperties(state);
            if (fob != null && fob.isData()) {
                try {
                    properties.load(fob.getInputStream());
                } catch (IOException ex) {
                    LOG.error(ex);
                }
            }
        }
        return properties;
    }

    /**
   * Loads jInfer project Input file names from Input file in jinferproject folder.
   *
   * @return Model of jInfer Project input files.
   */
    private Input loadInput() {
        if (input == null) {
            final FileObject inputFilesFileOb = projectDir.getFileObject(JInferProjectFactory.PROJECT_DIR + "/" + JInferProjectFactory.PROJECT_INPUTFILE);
            input = new Input(new InputFilesList(), new InputFilesList(), new InputFilesList(), new InputFilesList());
            if (inputFilesFileOb != null && inputFilesFileOb.isData()) {
                try {
                    InputFiles.load(inputFilesFileOb.getInputStream(), input, FileUtil.toFile(getProjectDirectory()));
                } catch (IOException ex) {
                    LOG.error(ex);
                }
            }
        }
        return input;
    }

    /**
   * Properties class which mark jInfer project as modified when some value is added to project properties.
   *
   */
    private static class NotifyProperties extends Properties {

        private static final long serialVersionUID = 3534789;

        private final ProjectState state;

        NotifyProperties(final ProjectState state) {
            super();
            this.state = state;
        }

        @Override
        public Object put(final Object key, final Object val) {
            final Object result = super.put(key, val);
            if (((result == null) != (val == null)) || (result != null && val != null && !val.equals(result))) {
                state.markModified();
            }
            return result;
        }
    }
}
