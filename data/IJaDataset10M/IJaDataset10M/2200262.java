package org.siberia.trans.handler;

import org.siberia.trans.PluginGraph;
import org.siberia.trans.TransSiberia;
import org.siberia.trans.exception.DownloadAbortedException;
import org.siberia.trans.type.plugin.PluginBuild;
import org.siberia.utilities.task.TaskStatus;

/**
 *
 * Define an object able to modulate TransSiberia behaviour during download process
 *
 * @author alexis
 */
public interface DownloadHandler {

    /** indicate that the download of the given build has begun
     *	@param build the build taht is currently being downloaded
     *	@param partialStatus a TaskStatus
     */
    public void buildDownloadBegan(PluginBuild build, TaskStatus partialStatus);

    /** indicate that the download of the given build is finished
     *	@param build the build taht is currently being downloaded
     *	@param partialStatus a TaskStatus
     */
    public void buildDownloadFinished(PluginBuild build, TaskStatus partialStatus);

    /** set the global TaskStatus representing the download task
     *	@param status a TaskStatus
     */
    public void setGlobalTaskStatus(TaskStatus status);

    /** set the number of build to download
     *	@param count the number of build to download
     */
    public void setNumberOfBuildsToDownload(int count);

    /** ask for license acceptation
     *	@param transSiberia the TransSiberia currently being used
     *	@param build the build that ask for license acceptation
     *	@return true if the license is accepted, false else
     */
    public boolean confirmLicense(TransSiberia transSiberia, PluginBuild build);

    /** handle an error during registration phase
     *	@param graph the PluginGraph
     *	@param message the message of the error
     *	@param throwable the throwable which gives the StackTrace
     *
     *	@return true to try again
     *	
     *	@exception DownloadAbortedException if the error provoke the download stop
     */
    public boolean handleErrorOnRegistration(PluginGraph graph, String message, Throwable throwable) throws DownloadAbortedException;

    /** handle an error on download
     *	@param graph the PluginGraph
     *	@param message the messageof the error
     *	@param throwable the throwable which gives the StackTrace
     *
     *	@return true to try again
     *	
     *	@exception DownloadAbortedException if the error provoke the download stop
     */
    public boolean handleErrorOnDownload(PluginGraph graph, String message, Throwable throwable) throws DownloadAbortedException;
}
