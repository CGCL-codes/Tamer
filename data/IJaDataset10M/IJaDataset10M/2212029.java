package cross.io.misc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import cross.Factory;
import cross.IConfigurable;
import cross.Logging;
import cross.datastructures.workflow.IWorkflow;
import cross.datastructures.workflow.IWorkflowFileResult;
import cross.datastructures.workflow.IWorkflowResult;
import cross.tools.FileTools;

/**
 * If configured to do so, zips all elements of a given <code>IWorkflow</code>
 * matching the given <code>FileFilter</code>. Marks directories and files which
 * are unmatched for deletion on exit of the virtual machine if configured.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class WorkflowZipper implements IConfigurable {

    private IWorkflow iw = null;

    private FileFilter ff = null;

    private final Logger log = Logging.getLogger(this.getClass());

    private boolean zipWorkflow = true;

    private boolean deleteOnExit = false;

    private HashSet<String> zipEntries = new HashSet<String>();

    private void addZipEntry(final int bufsize, final ZipOutputStream zos, final byte[] input_buffer, final File file) throws IOException {
        this.log.debug("Adding zip entry for file {}", file);
        if (file.exists() && file.isFile()) {
            final ZipEntry zip_entry = new ZipEntry(file.getName());
            if (this.zipEntries.contains(file.getName())) {
                this.log.info("Skipping duplicate zip entry {}", file.getName());
                return;
            } else {
                this.zipEntries.add(file.getName());
            }
            zos.putNextEntry(zip_entry);
            final FileInputStream in = new FileInputStream(file);
            final BufferedInputStream source = new BufferedInputStream(in, bufsize);
            int len = 0;
            while ((len = source.read(input_buffer, 0, bufsize)) != -1) {
                zos.write(input_buffer, 0, len);
            }
            zos.flush();
            source.close();
            zos.closeEntry();
        } else {
            this.log.warn("Skipping nonexistant file or directory {}", file);
        }
    }

    @Override
    public void configure(final Configuration cfg) {
        this.zipWorkflow = cfg.getBoolean(this.getClass().getName() + ".zipWorkflow", true);
        this.deleteOnExit = cfg.getBoolean(this.getClass().getName() + ".deleteOnExit", false);
    }

    /**
	 * @return the deleteOnExit
	 */
    public boolean isDeleteOnExit() {
        return this.deleteOnExit;
    }

    /**
	 * @return the zipWorkflow
	 */
    public boolean isZipWorkflow() {
        return this.zipWorkflow;
    }

    /**
	 * Saves the currently assigned workflow elements, matching currently
	 * assigned FileFilter to File. Marks all files for deletion on exit.
	 * 
	 * @param f
	 * @return
	 */
    public boolean save(final File f) {
        if (this.zipWorkflow) {
            this.zipEntries.clear();
            final int bufsize = 1024;
            final File zipFile = f;
            ZipOutputStream zos;
            try {
                final FileOutputStream fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                this.log.info("Created zip output stream");
                final byte[] input_buffer = new byte[bufsize];
                final Iterator<IWorkflowResult> iter = this.iw.getResults();
                File basedir = FileTools.prependDefaultDirsWithPrefix("", null, this.iw.getStartupDate());
                this.log.info("marked basedir for deletion on exit: {}", basedir);
                if (this.deleteOnExit) {
                    basedir.deleteOnExit();
                }
                this.log.info("setting basedir to parent file: {}", basedir.getParentFile());
                basedir = basedir.getParentFile();
                while (iter.hasNext()) {
                    final IWorkflowResult iwr = iter.next();
                    if (iwr instanceof IWorkflowFileResult) {
                        final IWorkflowFileResult iwfr = (IWorkflowFileResult) iwr;
                        final File file = iwfr.getFile();
                        this.log.info("Retrieving file result {}", file);
                        final File parent = file.getParentFile();
                        this.log.info("Retrieving parent of file result {}", parent);
                        if (parent.getAbsolutePath().startsWith(basedir.getAbsolutePath()) && !parent.getAbsolutePath().equals(basedir.getAbsolutePath())) {
                            this.log.info("Marking file and parent for deletion");
                            if (this.deleteOnExit) {
                                parent.deleteOnExit();
                                file.deleteOnExit();
                            }
                        }
                        if (file.getAbsolutePath().startsWith(basedir.getAbsolutePath())) {
                            this.log.info("Marking file for deletion");
                            if (this.deleteOnExit) {
                                file.deleteOnExit();
                            }
                        }
                        if ((this.ff != null) && !this.ff.accept(file)) {
                            continue;
                        } else {
                            this.log.info("Adding zip entry!");
                            addZipEntry(bufsize, zos, input_buffer, file);
                        }
                    }
                }
                final File runtimeProps = FileTools.prependDefaultDirsWithPrefix("", null, this.iw.getStartupDate());
                this.log.info("Saving config to {}", runtimeProps);
                Factory.saveConfiguration(Factory.getInstance().getConfiguration(), runtimeProps);
                if (runtimeProps.exists() && runtimeProps.canRead()) {
                    this.log.info("Marking file for deletion");
                    if (this.deleteOnExit) {
                        runtimeProps.deleteOnExit();
                    }
                    addZipEntry(bufsize, zos, input_buffer, runtimeProps);
                }
                try {
                    zos.flush();
                    zos.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            this.log.debug("Configured to not zip Workflow results!");
            return false;
        }
    }

    public boolean save(final File parentDir, final String filename) {
        return save(new File(parentDir, filename));
    }

    /**
	 * @param deleteOnExit
	 *            the deleteOnExit to set
	 */
    public void setDeleteOnExit(final boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    /**
	 * @param fileFilter
	 */
    public void setFileFilter(final FileFilter fileFilter) {
        this.ff = fileFilter;
    }

    /**
	 * @param workflow
	 */
    public void setIWorkflow(final IWorkflow workflow) {
        this.iw = workflow;
    }

    /**
	 * @param zipWorkflow
	 *            the zipWorkflow to set
	 */
    public void setZipWorkflow(final boolean zipWorkflow) {
        this.zipWorkflow = zipWorkflow;
    }
}
