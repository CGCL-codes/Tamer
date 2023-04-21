package eu.goldenak.ircbot;

import eu.goldenak.ircbot.module.ModuleBase;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class will load the specified Module. Modules are loaded this way
 * because then we're able to unload it from the memory as well, without
 * killing the JVM. This functionality can be used for... well anything really.
 */
public class ModuleClassLoader extends ClassLoader {

    /**
	 * This is the Class object of the Module that's being loaded or already
	 * loaded.
	 */
    private Class<?> m_pModuleClass = null;

    /**
	 * In here the (only!) pointer to the loaded module will be stored. Note 
	 * that when you store a pointer to the module somewhere in your own
	 * modules, unloading this module will not work anymore.
	 */
    private ModuleBase m_pModule = null;

    /**
	 * The constructor will load up the classloader. To unload this module, 
	 * simply set the variable holding the pointer to this ClassLoader to null.
	 */
    public ModuleClassLoader(String sModuleName) throws ClassNotFoundException {
        m_pModuleClass = loadClass("eu.goldenak.ircbot.module." + sModuleName);
        if (m_pModuleClass == null) {
            throw new ClassNotFoundException("Class object is null.");
        }
    }

    /**
	 * This method will try to instantiate the Module. Throws an Exception if it
	 * fails somehow, which means that this is not a properly programmed Module.
	 */
    public void init() throws Exception {
        try {
            m_pModule = (ModuleBase) m_pModuleClass.newInstance();
        } catch (Exception pException) {
            throw new Exception(pException);
        }
    }

    /**
	 * Returns the pointer to the loaded Module. Always use this. Never store
	 * the pointer.
	 */
    public ModuleBase getModule() {
        return m_pModule;
    }

    /**
	 * This method will try to load the specified class. Any classes not in the
	 * package classloader.module will be loaded using the system classloader.
	 * This also applies to the ModuleBase abstract class in the module package,
	 * because otherwise we can't cast the loaded class to ModuleBase.
	 */
    public Class<?> loadClass(String sName) throws ClassNotFoundException {
        if (!sName.startsWith("eu.goldenak.ircbot.module.") || sName.equals("eu.goldenak.ircbot.module.ModuleBase")) {
            return ClassLoader.getSystemClassLoader().loadClass(sName);
        }
        String sClassPath = sName.replace('.', File.separatorChar) + ".class";
        byte[] aBytes;
        try {
            aBytes = loadClassData(sClassPath);
        } catch (Exception pException) {
            throw new ClassNotFoundException(pException.getMessage(), pException);
        }
        try {
            return defineClass(sName, aBytes);
        } catch (Exception pException) {
            throw new ClassNotFoundException(pException.getMessage(), pException);
        }
    }

    /**
	 * This method will define the class from the provided bytecode.
	 */
    protected Class<?> defineClass(String sName, byte[] aBytes) {
        return defineClass(sName, aBytes, 0, aBytes.length, null);
    }

    /**
	 * This method reads the classfile of a compiled Java class and returns the
	 * read bytes in an array. This array can then be used to define the class.
	 */
    protected byte[] loadClassData(String sClassPath) throws FileNotFoundException, IOException {
        File pClassFile = new File(sClassPath);
        if (!pClassFile.exists()) {
            throw new FileNotFoundException(sClassPath);
        }
        int nFileSize = (int) pClassFile.length();
        byte[] aBytes = new byte[nFileSize];
        DataInputStream pInput = new DataInputStream(new FileInputStream(pClassFile));
        pInput.readFully(aBytes);
        pInput.close();
        return aBytes;
    }
}
