package org.insightech.er.ant_task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.insightech.er.ResourceString;
import org.insightech.er.common.exception.InputException;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.persistent.Persistent;

public abstract class ERMasterAntTaskBase extends Task {

    private String diagramFile;

    public void setDiagramFile(String diagramFile) {
        this.diagramFile = diagramFile;
    }

    protected String getAbsolutePath(String path) {
        if (path == null) {
            path = this.getProject().getBaseDir().getAbsolutePath();
        } else if (!new File(path).isAbsolute() && !path.startsWith("/")) {
            path = this.getProject().getBaseDir().getAbsolutePath() + File.separator + path;
        }
        return path;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void execute() throws BuildException {
        this.logUsage();
        Persistent persistent = Persistent.getInstance();
        InputStream in = null;
        try {
            if (this.diagramFile == null || this.diagramFile.trim().equals("")) {
                throw new BuildException("diagramFile attribute must be set!");
            }
            this.log("Load the diagram file : " + this.diagramFile);
            File file = new File(this.getLocation().getFileName());
            in = new BufferedInputStream(new FileInputStream(new File(file.getParent(), this.diagramFile)));
            ERDiagram diagram = persistent.load(in);
            this.log("Output beginning...");
            this.doTask(diagram);
            this.log("Output finish!");
        } catch (InputException e) {
            throw new BuildException(ResourceString.getResourceString(e.getMessage()));
        } catch (BuildException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw new BuildException(e);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new BuildException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
        }
    }

    protected abstract void logUsage();

    protected abstract void doTask(ERDiagram diagram) throws Exception;
}
