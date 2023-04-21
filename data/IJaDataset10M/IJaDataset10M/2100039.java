package org.jbjf.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * The <code>WindowsCopyFile</code> class is an upgrade/fix to the
 * original <code>org.jbjf.tasks.CopyFile</code>.  Windows has a
 * limitation on 64mb files and you get the following exception:
 * <pre>
 * Message: Insufficient system resources exist to complete the requested service
 * Exception: java.io.IOException: Insufficient system resources exist to complete the requested service
 * Stack Trace: sun.nio.ch.FileDispatcher.write0(Native Method)sun.nio.ch.FileDispatcher.write(FileDispatcher.java:44)sun.nio.ch.IOUtil.writeFromNativeBuffer(IOUtil.java:104)sun.nio.ch.IOUtil.write(IOUtil.java:60)sun.nio.ch.FileChannelImpl.write(FileChannelImpl.java:204)org.jbjf.tasks.CopyFile.runTask(Unknown Source)org.jbjf.core.AbstractBatch.runBatch(Unknown Source)org.jbjf.core.AbstractBatch._runBatch(Unknown Source)org.jbjf.core.AbstractBatch._main(Unknown Source)
 * </pre>
 * The <code>WindowsCopyFile</code> compensates for this and loops
 * through the file in 32mb blocks, thus working around the original
 * limitation.
 * <p>
 * <h3>Dependencies:</h3>
 * <ul>
 * <li>JBJF 1.x.x (+)
 * </ul>
 * <h3>Resources:</h3>
 * <code>WindowsCopyFile</code> depends on the following &lt;resource&gt; 
 * elements to function correctly:
 * <ul>
 * <li>source - Full or partial directory path with filename for the
 * source file.
 * <li>target - Full or partial directory path with filename for the
 * destination.
 * </ul>
 * <p>
 * <h3>Details</h3>
 * <hr>
 * <h4>Input Resources</h4>
 * <table border='1' width='65%'>
 * <thead>
 *  <tr>
 *      <td width='15%'>Location</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='15%'>Id/Name</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='25%'>Type</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='10%'>Required</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td>Description/Comments</td>
 *  </tr>
 * </thead>
 *  <tr valign='top'>
 *      <td>&lt;task&gt;</td>
 *      <td>&nbsp;</td>
 *      <td>source</td>
 *      <td>&nbsp;</td>
 *      <td>String</td>
 *      <td>&nbsp;</td>
 *      <td>True</td>
 *      <td>&nbsp;</td>
 *      <td>Text/String - Location and name of the file to copy.
 *      </td>
 *  </tr>
 *  <tr valign='top'>
 *      <td>&lt;task&gt;</td>
 *      <td>&nbsp;</td>
 *      <td>target</td>
 *      <td>&nbsp;</td>
 *      <td>String</td>
 *      <td>&nbsp;</td>
 *      <td>True</td>
 *      <td>&nbsp;</td>
 *      <td>Text/String - Location and name of the destination file being copied.
 *      </td>
 *  </tr>
 * </table>
 * <p>
 * The following is an example XML &lt;task&gt; element:
 * <p>
 * <pre>
 *         &lt;task name="t001" order="1" active="true"&gt;
 *             &lt;class&gt;org.jbjf.tasks.WindowsCopyFile&lt;/class&gt;
 *             &lt;resource type="source"&gt;.\working\my-working-file.txt&lt;/resource&gt;
 *             &lt;resource type="target"&gt;\\network-drive\sub-dir\my-working-file.txt&lt;/resource&gt;
 *         &lt;/task&gt;
 * </pre>
 * @author Adym S. Lincoln<br>
 *         Copyright (C) 2007. JBJF All rights reserved.
 * @version 1.3.0
 * @since   1.1.2
 * @see org.jbjf.tasks.CopyFile
 * @deprecated
 */
public class WindowsCopyFile extends CopyFile {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = WindowsCopyFile.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "WindowsCopyFile()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * Default constructor.
     */
    public WindowsCopyFile() {
        super();
    }
}
