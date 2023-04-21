package net.sourceforge.javautil.developer.web.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import net.sourceforge.javautil.common.IOUtil;
import net.sourceforge.javautil.common.io.IVirtualArtifact;
import net.sourceforge.javautil.common.io.IVirtualDirectory;
import net.sourceforge.javautil.common.io.IVirtualFile;
import net.sourceforge.javautil.common.io.impl.Directory;
import net.sourceforge.javautil.common.io.impl.DirectoryRoot;
import net.sourceforge.javautil.common.io.impl.DirectoryFile;
import net.sourceforge.javautil.common.io.impl.SystemDirectory;
import net.sourceforge.javautil.common.io.impl.SystemFile;
import net.sourceforge.javautil.web.server.application.IWebApplicationResourceResolver;
import net.sourceforge.javautil.web.server.application.impl.DirectoryResolver;
import net.sourceforge.javautil.web.server.application.impl.VirtualDirectoryResolver;
import org.testng.annotations.Test;

/**
 * 
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: WebServerImplementations.java 2298 2010-06-16 00:20:18Z ponderator $
 */
@Test
public class WebServerImplementations {

    public void resolverTest() throws Exception {
        File goodapp = new File("src/test/webapp/good");
        IWebApplicationResourceResolver resolver = new DirectoryResolver(goodapp);
        System.out.println(resolver.getFile("/view1.xhtml"));
        System.out.println(resolver.getResource("/subdir/subview2.groovy"));
        System.out.println(resolver.getResourcePaths("/WEB-INF"));
        System.out.println(resolver.getResourcePaths("/"));
        DirectoryRoot directory = new DirectoryRoot();
        directory.link(new SystemDirectory(goodapp));
        Directory webInf = (Directory) directory.createDirectory("WEB-INF");
        DirectoryFile context = (DirectoryFile) webInf.createFile("context.xml");
        context.write("test");
        resolver = new VirtualDirectoryResolver(directory);
        System.out.println(resolver.getFile("/view1.xhtml"));
        System.out.println(resolver.getResource("/subdir/subview2.groovy"));
        System.out.println(resolver.getResourcePaths("/WEB-INF"));
        System.out.println(resolver.getResourcePaths("/"));
        IVirtualArtifact[] copies = new IVirtualArtifact[2];
        for (IVirtualArtifact artifact : new ArrayList<IVirtualArtifact>(directory.getRecursiveArtifacts())) {
            if (artifact.getName().equals("WEB-INF")) {
                copies[copies[0] == null ? 0 : 1] = artifact;
            }
        }
        System.out.println(copies[0].compareTo(copies[1]) + " / " + copies[0].equals(copies[1]));
    }
}
