package com.loribel.tools.file.ant;

import org.apache.tools.ant.BuildException;
import com.loribel.commons.ant.GB_FileSetsTask;
import com.loribel.tools.file.xml.GB_File2Xml;

/**
 * Ant task to use GB_File2Xml.
 *
 * @author Gr�gory Borelli
 */
public class GB_File2XmlTask extends GB_FileSetsTask {

    /**
     * Attribute file2Xml.
     */
    private GB_File2Xml file2Xml;

    public GB_File2XmlTask() {
        super();
        file2Xml = new GB_File2Xml();
        this.setTask(file2Xml);
    }

    /**
     * Execute the task.
     */
    public void execute() throws BuildException {
        file2Xml.setAcceptedFiles(getFileList());
        super.execute();
    }
}
