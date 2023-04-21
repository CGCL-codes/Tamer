package org.proclos.etlcore.job;

import org.proclos.etlcore.execution.IExecuteable;
import org.proclos.etlcore.export.ExportManager;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public interface IJob extends IExecuteable {

    public void setName(String name);

    public ExportManager getExportManager();
}
