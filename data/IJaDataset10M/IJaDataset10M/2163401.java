package org.jtools.iofs.memory.samples.javac;

import java.net.URI;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.jtools.filemanager.FileManagerUtils;
import org.jtools.filemanager.SimpleFileManager;
import org.jtools.iofs.core.samples.javac.AbstractIOJavaC;
import org.jtools.iofs.memory.TFS;
import org.jtools.iofs.memory.TFileSystem;

/**
 * Transient Java Compiler - Sample application. 
 * 
 * This application illustrates the usage of the transient filesystem (TFS) in combination with 
 * the system java compiler. <br/> 
 * A HelloWorld sourcecode has to be compiled and executed 
 * without using the OS-filesystem : <code>
 * public class HelloWorld {<br/>
 *   public static void execute() {<br/>
 *     System.out.println("Hello World.");<br/>
 *   }<br/>
 * }</code>
 * <ul>
 * <li>This String is set as content of a file in the TFS.</li> 
 * <li>A filemanager is configured to use the TFS-directories as java-sourcepath and class-file output directory.</li> 
 * <li>The system java compiler is executed with this filemanager.</li> 
 * <li>A new classloader is created with the transient class-file output-directory as search path.</li> 
 * <li>The compiled class is loaded and invoked.</li> 
 * <li>The invoked method writes 'Hello World.' to System.out.</li> 
 * </ul>
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class TransientJavaC extends AbstractIOJavaC {

    @Override
    protected URI startup() {
        return TFS.defaultFileSystem().getDir("/").toURI();
    }

    public static void main(String[] args) {
        new TransientJavaC().execute();
    }
}
