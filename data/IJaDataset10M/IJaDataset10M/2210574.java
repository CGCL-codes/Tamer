package net.sf.vmwcmd.result;

/**
 * The result for the method {@link net.sf.vmwcmd.VmWareCommand#getConfigFile(String)}.
 * 
 * @author  T. Verhagen
 */
public interface GetConfigFileResult {

    /**
     * Returns the VmWare host configuration file <code>*.vmx</code>.
     * @return  The VmWare host configuration file <code>*.vmx</code>.
     */
    String getConfigFile();
}
