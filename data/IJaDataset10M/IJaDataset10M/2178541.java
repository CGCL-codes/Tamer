package com.mindtree.techworks.insight.releng.mvn.nsis.velocityutil;

import com.mindtree.techworks.insight.releng.mvn.nsis.actions.MojoInfo;

/**
 * A generic tool to check for null values in velocity template. To use this
 * tool, set an instance of this tool in your context.
 * 
 * If you set the instance with a key of 'null', then you can check the value of
 * references using:
 * 
 * <pre>
 * 	$null.isNull($foo)		-&gt; true if foo is null else false.
 *  $null.isNotNull($foo)   -&gt; false if foo is null else true.
 * </pre>
 * 
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 90 $ $Date: 2008-01-07 03:16:24 -0500 (Mon, 07 Jan 2008) $
 * 
 * @plexus.component role="com.mindtree.techworks.insight.releng.mvn.nsis.velocityutil.VelocityTool" role-hint="null-tool"
 */
public class NullTool implements VelocityTool {

    /**
	 * Checks of the test object is null.
	 * 
	 * @param o
	 *            The object to check
	 * @return <code>true</code> if <code>o</code> is <code>null</code>,
	 *         else <code>false</code>.
	 */
    public boolean isNull(Object o) {
        return (null == o);
    }

    /**
	 * Checks of the test object is not null.
	 * 
	 * @param o
	 *            The object to check
	 * @return <code>false</code> if <code>o</code> is <code>null</code>,
	 *         else <code>true</code>.
	 */
    public boolean isNotNull(Object o) {
        return !isNull(o);
    }

    public void setMojoInfo(MojoInfo mojoInfo) {
    }
}
