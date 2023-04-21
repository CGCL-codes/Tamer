package net.sourceforge.javautil.common.test;

import java.util.List;
import java.util.Map;
import net.sourceforge.javautil.common.io.IVirtualDirectory;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;
import net.sourceforge.javautil.common.reflection.cache.ClassDescriptor;
import net.sourceforge.javautil.common.reflection.cache.ClassMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test the {@link ClassDescriptor} functionality.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@Test
public class ClassDescriptorTest {

    /**
	 * Make sure {@link ClassDescriptor}'s work correctly in relation to <code>interfaces</code>
	 */
    public void testInterfaceDescriptor() {
        ClassDescriptor<IVirtualDirectory> vdd = ClassCache.getFor(IVirtualDirectory.class);
        Assert.assertTrue(vdd.findMethod("getOwner").getReturnType() == IVirtualDirectory.class, "Interface based ClassDescriptor's returning overriden interface methods.");
    }
}
