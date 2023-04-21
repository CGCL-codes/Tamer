package net.sourceforge.javautil.developer.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.javautil.classloader.boot.EntryPoint;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.common.encryption.IEncryptionProvider;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.OutputStreamRegistry;
import net.sourceforge.javautil.common.io.impl.DirectoryFile;
import net.sourceforge.javautil.common.password.IPassword;
import net.sourceforge.javautil.common.password.IPasswordLocker;
import net.sourceforge.javautil.common.password.impl.StandardPasswordLocker;
import net.sourceforge.javautil.datasource.IDataSourceDescriptor;
import net.sourceforge.javautil.ui.IUserInterface;

/**
 * This is the basis for a developer context, the context in which a developer is working
 * in a single thread.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: DeveloperContext.java 2586 2010-11-15 04:14:32Z ponderator $
 */
public class DeveloperContext {

    /**
	 * The context thread local.
	 */
    private static ThreadLocal<DeveloperContext> context = new ThreadLocal<DeveloperContext>();

    private static DeveloperContext global;

    /**
	 * @return The current developer context, or null if none has been set for this thread
	 */
    public static DeveloperContext getInstance() {
        return context.get() == null ? global : context.get();
    }

    /**
	 * Setup the context to be used in this thread.
	 * 
	 * @param ctx The developer context to set for the current thread
	 */
    public static void setInstance(DeveloperContext ctx) {
        context.set(ctx);
    }

    public static void setGlobalInstance(DeveloperContext ctx) {
        global = ctx;
    }

    protected static Map<String, String> classLookup = new LinkedHashMap<String, String>();

    public static String getId(Class clazz) {
        for (String id : classLookup.keySet()) {
            if (classLookup.get(id).equals(clazz.getName())) return id;
        }
        return clazz.getName();
    }

    public static Class getClass(String id) {
        return ReflectionUtil.getClass(classLookup.containsKey(id) ? classLookup.get(id) : id);
    }

    public static void registerClassMapping(String mapping, Class clazz) {
        registerClassMapping(mapping, clazz.getName());
    }

    public static void registerClassMapping(String mapping, String clazzName) {
        classLookup.put(mapping, clazzName);
    }

    protected final IPasswordLocker passwords;

    protected final IPasswordLocker tempPasswords;

    /**
	 * The registry of sub systems that have been loaded
	 */
    protected List<Object> registry = new ArrayList<Object>();

    protected OutputStreamRegistry outputRegistry;

    protected Map<String, IDataSourceDescriptor> datasources = new LinkedHashMap<String, IDataSourceDescriptor>();

    public DeveloperContext(IEncryptionProvider provider, IPasswordLocker passwords) {
        if (EntryPoint.getEntryPointType() != null) {
            this.outputRegistry = EntryPoint.getEntryPointType().getEntryPoint().getOutputRegistry();
        } else {
            this.outputRegistry = new OutputStreamRegistry();
        }
        this.passwords = passwords;
        try {
            this.tempPasswords = new StandardPasswordLocker("Temporary Developer Passwords", provider, String.valueOf(System.currentTimeMillis()).getBytes());
        } catch (InvalidKeyException e) {
            throw ThrowableManagerRegistry.caught(e);
        } catch (NoSuchAlgorithmException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * @param prompt The prompt to use to get the password if not saved
	 * @param id The id of the password
	 * @return The password
	 */
    public IPassword getPassword(String prompt, String id) {
        IPassword pw = passwords.getPassword(id);
        if (pw == null) pw = tempPasswords.getPassword(id);
        if (pw == null) {
            pw = find(IUserInterface.class).password(prompt, id, passwords, tempPasswords);
        }
        return pw;
    }

    /**
	 * This will allow other parts of the system to locate the system in a standard
	 * central way.
	 * 
	 * @param system The system to register/add to the registry
	 */
    public void register(Object system) {
        registry.add(system);
    }

    /**
	 * @param <S> The type of sub system
	 * @param systemType The system class
	 * @return The first sub system that is an instanceof or a sub class of the specified type
	 */
    public <S> S find(Class<S> systemType) {
        for (Object system : this.registry) if (systemType.isAssignableFrom(system.getClass())) return (S) system;
        return null;
    }

    /**
	 * @return The registry for the context.
	 */
    public OutputStreamRegistry getOutputRegistry() {
        return outputRegistry;
    }

    /**
	 * Register a datasource for global discovery. 
	 * It will be associated with its name.
	 * 
	 * @param descriptor The descriptor
	 */
    public void registerDataSource(IDataSourceDescriptor descriptor) {
        this.datasources.put(descriptor.getName(), descriptor);
    }

    /**
	 * @param name The name of the data source descriptor
	 * @return The descriptor or null if it has not been previously registered
	 */
    public IDataSourceDescriptor getDataSource(String name) {
        return this.datasources.get(name);
    }

    /**
	 * @return A copy of the list of currently registered data sources
	 */
    public List<IDataSourceDescriptor> getDataSources() {
        return new ArrayList<IDataSourceDescriptor>(this.datasources.values());
    }

    /**
	 * @return The password locker for the developer context.
	 */
    public IPasswordLocker getPasswords() {
        return passwords;
    }

    /**
	 * @return The temporary password locker during the JVM life
	 */
    public IPasswordLocker getTemporaryPasswords() {
        return tempPasswords;
    }
}
