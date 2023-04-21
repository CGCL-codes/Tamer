package com.intel.gpe.gridbeans.zip;

import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import com.intel.gpe.client2.common.panels.remotefs.PathElement;
import com.intel.gpe.clients.api.FileSystem;
import com.intel.gpe.clients.api.GridFile;
import com.intel.gpe.clients.api.Job;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.jsdl.gpe.GPEJob;
import com.intel.gpe.constants.GPEConstants;
import com.intel.gpe.gridbeans.AbstractGridBean;
import com.intel.gpe.gridbeans.GridBeanException;
import com.intel.gpe.gridbeans.GridBeanParameter;
import com.intel.gpe.gridbeans.GridBeanParameterType;
import com.intel.gpe.gridbeans.IGridBean;
import com.intel.gpe.gridbeans.OutputFileParameterValue;

/**
 * 
 * @author Max Lukichev
 * @version $ Id: ZipGridBean, Jun 29, 2006 1:18:06 PM mlukiche Exp $
 */
public class ZipGridBean extends AbstractGridBean implements IGridBean {

    private static String APPLICATION_NAME = "Zip";

    private static String APPLICATION_VERSION = "1.0";

    private static String SOURCE_FIELD = "SOURCE";

    private static String TARGET_FIELD = "TARGET";

    private static String FS_NAME_FIELD = "FSNAME";

    public static final String NAMESPACE = "http://gpe.intel.com/gridbeans/zip";

    public static final QName TARGET_FILE_NAME = new QName(NAMESPACE, "TARGET_FILE_NAME", "zip");

    public static final QName TARGET = new QName(NAMESPACE, "TARGET_FILE", "zip");

    public static final QName SOURCE = new QName(NAMESPACE, "SOURCE_FILE", "zip");

    private static final GridBeanParameter[] INPUT_PARAMETERS = {};

    private static final GridBeanParameter[] OUTPUT_PARAMETERS = { new GridBeanParameter(TARGET, GridBeanParameterType.GPE_FILE) };

    private OutputFileParameterValue targetFile = new OutputFileParameterValue("target.zip");

    public ZipGridBean() {
        set(JOBNAME, "ZipJob");
        set(TARGET_FILE_NAME, targetFile.getTargetSystemFile());
        set(TARGET, targetFile);
        set(SOURCE, new PathElement[0]);
    }

    public List<GridBeanParameter> getInputParameters() {
        return Arrays.asList(INPUT_PARAMETERS);
    }

    public List<GridBeanParameter> getOutputParameters() {
        return Arrays.asList(OUTPUT_PARAMETERS);
    }

    public void setupJobDefinition(Job job) throws GridBeanException {
        if (job instanceof GPEJob) {
            try {
                GPEJob gpeJob = (GPEJob) job;
                gpeJob.setApplicationName(APPLICATION_NAME);
                gpeJob.setApplicationVersion(APPLICATION_VERSION);
                gpeJob.setWorkingDirectory(GPEConstants.JobManagement.TEMPORARY_DIR_NAME);
                gpeJob.setId((String) get(JOBNAME));
                PathElement[] path = (PathElement[]) get(SOURCE);
                if (path.length >= 2) {
                    String fsName = ((FileSystem) ((StorageClient) path[1].getValue()).getFileSystem()).getName();
                    gpeJob.addField(FS_NAME_FIELD, fsName);
                    String fileName;
                    if (path.length > 2) {
                        fileName = ((GridFile) path[path.length - 1].getValue()).getPath();
                    } else {
                        fileName = ".";
                    }
                    gpeJob.addField(SOURCE_FIELD, fileName);
                }
                String target = (String) get(TARGET_FILE_NAME);
                targetFile.setTargetSystemFile(target);
                gpeJob.addField(TARGET_FIELD, target);
            } catch (Exception e) {
                throw new GridBeanException("Cannot setup job definition", e);
            }
        } else {
            throw new GridBeanException("Unsupported job class: " + job.getClass().getName());
        }
    }

    public void parseJobDefinition(Job job) throws GridBeanException {
        if (job instanceof GPEJob) {
            try {
                GPEJob gpeJob = (GPEJob) job;
                set(JOBNAME, gpeJob.getId());
                String tFileName = gpeJob.getField(TARGET_FIELD);
                set(TARGET_FILE_NAME, tFileName);
                targetFile.setTargetSystemFile(tFileName);
            } catch (Exception e) {
                throw new GridBeanException("Cannot parse job definition", e);
            }
        }
    }

    public String getName() {
        return "ZipGridBean";
    }
}
