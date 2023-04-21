package net.sf.ocmscripting.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;

/**
 * Helper class to allow execution of Groovy scripts from VFS files.
 * <p>
 * 
 */
public class CmsGroovyVFSExecutor {

    /**
     * The logging channel for this class.
     */
    private static Log LOG = CmsLog.getLog(CmsGroovyVFSExecutor.class);

    /**
     * The CmsObject used to read the scripts.
     */
    private CmsObject cms;

    /**
     * Constructs a new instance. The CmsObject is used for reading the script
     * files.
     * 
     * @param cms
     *            The CmsObject used for reading the script files
     */
    public CmsGroovyVFSExecutor(CmsObject cms) {
        if (cms == null) {
            throw new NullPointerException("CmsObject passed into " + this.getClass().getName() + " constructor must not be null.");
        }
        this.cms = cms;
    }

    /**
     * Executes the script denoted by the path with the provided binding.
     * <p>
     * 
     * Beware that the CmsObject supplied in the constructor is used to read the
     * script. So the path must match the request context (e.g. site root) in
     * the supplied CmsObject.
     * 
     * @param path
     *            The VFS path to script
     * @param binding
     *            The binding used for the script
     * @return The return value of the evaluated script
     * @throws CmsException
     *             If an OpenCms operation fails
     * @throws NullPointerException
     *             If a null path is supplied
     */
    public Object executeScript(String path, Binding binding) throws CmsException {
        if (path == null) {
            if (LOG.isErrorEnabled()) {
                LOG.error(Messages.get().getBundle().key(Messages.ERROR_VFS_EXECUTOR_SCRIPT_NULL_0));
            }
            throw new NullPointerException(Messages.get().getBundle().key(Messages.ERROR_VFS_EXECUTOR_SCRIPT_NULL_0));
        }
        Object returnValue = null;
        GroovyShell shell = new GroovyShell(binding);
        try {
            CmsFile scriptFile = readScriptResource(path);
            returnValue = shell.evaluate(new String(scriptFile.getContents()));
        } catch (CmsException cmse) {
            if (LOG.isErrorEnabled()) {
                LOG.error(Messages.get().getBundle().key(Messages.ERROR_VFS_EXECUTOR_SCRIPT_ERR_0), cmse);
            }
            throw cmse;
        } catch (RuntimeException re) {
            if (LOG.isErrorEnabled()) {
                LOG.error(Messages.get().getBundle().key(Messages.ERROR_VFS_EXECUTOR_SCRIPT_ERR_0), re);
            }
            throw re;
        }
        return returnValue;
    }

    /**
     * Utility method to read the CmsFile from the VFS.
     * <p>
     * 
     * @param path
     *            The path to read the script from
     * @return The read CmsFile
     * @throws CmsException
     *             If some VFS operation fails or the script is not readable
     */
    private CmsFile readScriptResource(String path) throws CmsException {
        CmsResource resource = cms.readResource(path);
        CmsFile scriptFile = CmsFile.upgrade(resource, cms);
        return scriptFile;
    }
}
