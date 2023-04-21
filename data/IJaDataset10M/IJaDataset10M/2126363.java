package com.warserver;

import java.beans.Beans;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import com.warserver.ssl.*;

/** 
 * I represent the java virtual machine that i am running in.
 *
 * @author  Kurt Olsen
 * @version 1.0 1.0
 */
public class Jvm {

    /** shared storage */
    public static Cache cache = new Cache();

    private static Debug _debug = new Debug();

    private static boolean sslEnabled = false;

    private static String version = "2.8";

    /** initialization */
    static {
    }

    /** 
     * main is used to print my current version number.
     */
    public static void main(String args[]) {
        System.out.println("WarServer " + version);
    }

    /** used by jsp to reference this class*/
    public Jvm() {
    }

    /**
     * returns the warserver version number 
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Tries to find the named object in the cache, failing that it
     * will use Beans.instantiate(loader, beanName) to create one
     * and if successfull it is both placed in the cache and returned.
     * Thus, this provides a convienent way of getting beans into the cache.
     * Remember that the key can contain a 'path' seperated with '/'
     * due to the fact that the cache creates a 'dir' for each path.
     * So, you can organize your beans into paths for cleanliness.
     */
    public static Object getInstance(String key, String beanName, Class loader) throws ClassNotFoundException {
        synchronized (Jvm.cache) {
            Object obj = Jvm.cache.get(key);
            if (obj != null) {
                System.out.println("Jvm.getInstance found bean named " + beanName + " in the cache.");
                return obj;
            }
            Jvm.println("Jvm.getInstance is trying to create a new object of class " + beanName);
            try {
                obj = Beans.instantiate(loader.getClassLoader(), beanName);
                Jvm.cache.put(key, obj);
                Jvm.println("Jvm.getInstance succeeded in creating a new " + beanName + " object.");
                return obj;
            } catch (IOException iox) {
                throw new ClassNotFoundException(iox.toString());
            }
        }
    }

    /**	Uses reflection to invoke a method on the target object.
     *	The target object is interrogated to see if it has a method
     *	matching the signature 'void methodName(String s, Object o)'.
     *	If it does, the method is executed, passing it methodName and datum
     *	as arguments. If the target doesn't contain such a method
     *	it's superclass (then it's super etc.) are tried until a
     *	matching method is found, or we run out of classes to try.
     */
    public static Object invokeNoArgMethod(Object tgt, String methodName) {
        Class types[] = {};
        Object args[] = {};
        return invokeMethod(tgt, methodName, types, args);
    }

    /**	Uses reflection to invoke a method on the target object.
     *	The target object is interrogated to see if it has a method
     *	matching the signature 'void methodName(String s, Object o)'.
     *	If it does, the method is executed, passing it methodName and datum
     *	as arguments. If the target doesn't contain such a method
     *	it's superclass (then it's super etc.) are tried until a
     *	matching method is found, or we run out of classes to try.
     */
    public static Object invoke1ArgMethod(Object tgt, String methodName, Object argument) {
        Class types[] = { java.lang.Object.class };
        Object args[] = { argument };
        return invokeMethod(tgt, methodName, types, args);
    }

    /** Saves a Serializable object to an OutputStream.
     *
     * The OutputStream is not flushed or closed, allowing multiple
     * objects to be saved to the stream.
     *
     * @param obj the Object to be saved.
     * @param stream the OutputStream the object is saved in.
     * @exception java.io.IOException if anything goes wrong
     * @see #writeSerialFile
     */
    public static void writeSerial(Object obj, OutputStream stream) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(stream);
        os.writeObject(obj);
    }

    /** Read an object from an InputStream 
     *
     * The InputStream is not flushed or closed, allowing multiple
     * objects to be read from the stream.
     *
     * @param stream the InputStream containing an Object
     * @return a Serializable Object
     * @exception java.io.IOException if anything goes wrong
     * @exception java.lang.ClassNotFoundException if anything goes wrong
     * @see #readSerialFile
     */
    public static Object readSerial(InputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream os = new ObjectInputStream(stream);
        Object obj = os.readObject();
        return obj;
    }

    /** Save a Serializable object to a file
     *
     * @param obj the Object to be saved in the file.
     * @param file the file the object is saved in.
     * @exception java.io.IOException if anything goes wrong
     */
    public static void writeSerialFile(Object obj, File file) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        writeSerial(obj, os);
        os.close();
    }

    /** Read a Serializable object from a file
     *
     * @param file the file containing a Serializable object.
     * @return the Serializable object saved in the file.
     *
     * @exception java.io.IOException if anything goes wrong
     * @exception java.lang.ClassNotFoundException if anything goes wrong
     */
    public static Object readSerialFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream is = new FileInputStream(file);
        Object obj = readSerial(is);
        is.close();
        return obj;
    }

    /**
     * Writes the string to the given file.
     * @param data java.lang.String the data to put in the file.
     * @param file java.io.File the file I overwrite without warning!
     * @exception java.io.IOException The exception description.
     */
    public static void writeTextFile(String data, File file) throws IOException {
        FileWriter fwRpt1 = new FileWriter(file);
        fwRpt1.write(data);
        fwRpt1.close();
    }

    /**
     * Return a String from the contents of the given text file.
     * @return java.lang.String the contents of the file.
     * @param file java.io.File a text file right?
     */
    public static String readTextFile(File file) throws IOException {
        LineNumberReader lineReader = null;
        String retv = "";
        String newline = System.getProperty("line.separator");
        lineReader = new LineNumberReader(new FileReader(file));
        String s;
        while ((s = lineReader.readLine()) != null) retv += (s + newline);
        lineReader.close();
        return retv;
    }

    /**
     * Converts a Vector, each element of which is string into
     * a composite string using the systems line separators.
     */
    public static String getStringFromVector(Vector inputVector) {
        String retv = "";
        String newline = System.getProperty("line.separator");
        for (Enumeration e = inputVector.elements(); e.hasMoreElements(); ) retv += e.nextElement().toString() + newline;
        return retv;
    }

    /**
	 * Reads the specified text file from the local file system and
	 * converts it into a Vector in which each element is a string.
	 */
    public static Vector getVectorFromTextFile(File textFile) throws IOException {
        Vector _inputVector = new Vector();
        LineNumberReader lineReader = new LineNumberReader(new FileReader(textFile));
        String s;
        while ((s = lineReader.readLine()) != null) _inputVector.addElement(s);
        lineReader.close();
        return _inputVector;
    }

    /**
     * Copies the input stream to the output stream. This method synchronizes the streams
     * to prevent another thread from using them and blocks until the entire copy is complete.
     * The streams are not closed when complete.
     *
     * @param InputStream the stream to read from.
     * @param OutputStream the stream to write to.
     * @param PrintStream if non null (usually Jvm) then progress info is printed to the stream.
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream, PrintStream out) throws IOException {
        synchronized (inputStream) {
            synchronized (outputStream) {
                long totalBytes = 0;
                StringBuffer statusBuf = new StringBuffer("" + totalBytes);
                byte[] buffer = new byte[8192];
                if (out != null) {
                    out.print("Received " + statusBuf.toString());
                    out.flush();
                }
                while (true) {
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead == -1) break; else totalBytes += bytesRead;
                    outputStream.write(buffer, 0, bytesRead);
                    if (out != null) {
                        for (int i = 0; i < statusBuf.length(); i++) {
                            out.print("\b");
                            out.flush();
                        }
                        statusBuf.delete(0, statusBuf.length());
                        statusBuf.append("" + totalBytes);
                        out.print(statusBuf.toString());
                        out.flush();
                    }
                }
                if (out != null) out.println();
            }
        }
    }

    /**
     * A non-blocking way to copy part of the InputStream to the OutputStream 
     * using the given buffer. The input and output streams are not closed or synchronized.
     * 
     * @param InputStream the stream to read from.
     * @param OutputStream the stream to write to.
     * @param buffer the buffer used to transfer the data.
     * @return the number of bytes copied, or -1 if all data has been read from the input.
     */
    public static int copyPartialStream(InputStream inputStream, OutputStream outputStream, byte buffer[]) throws IOException {
        int bytesRead = 0;
        int bytesReadable = buffer.length;
        int bytesAvailable = inputStream.available();
        if (bytesAvailable == 0) {
            bytesRead = inputStream.read(buffer);
        } else {
            if (bytesAvailable <= buffer.length) bytesReadable = bytesAvailable;
            bytesRead = inputStream.read(buffer, 0, bytesReadable);
        }
        if (bytesRead != -1) outputStream.write(buffer, 0, bytesRead);
        return bytesRead;
    }

    /**
     * copies the inFile to the outFile, obvious huh?
     */
    public static void copyFile(File inFile, File outFile, PrintStream out) throws IOException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            fin = new FileInputStream(inFile);
            fout = new FileOutputStream(outFile);
            copyStream(fin, fout, out);
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException e) {
            }
            try {
                if (fout != null) fout.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Creates a subdirectory of the given file.
     */
    public static File mksubdir(File root, String subDirName) throws IOException {
        root.mkdirs();
        File newsubdir = new File(root, subDirName);
        newsubdir.mkdirs();
        return newsubdir;
    }

    /**
     * Deletes the given file
     */
    public static void deleteFile(File f) throws IOException {
        if (!f.exists()) return;
        f.delete();
        if (f.exists()) {
            throw new IOException("Unable to delete file " + f.toString());
        }
    }

    /** Serializes the given Object and sends it to the url.<br>
     * If an object is sent back as a reply it is returned <br>
     * otherwise the return value will be null.
     * @throws IOException if the transmission of the object fails.
     */
    public static Object transmitObject(String servletURL, Object obj) throws IOException {
        URL url;
        URLConnection conn;
        InputStream is;
        OutputStream os;
        try {
            if (servletURL.startsWith("https") || servletURL.startsWith("HTTPS")) {
                System.out.println(Jvm.class.getName() + ".transmitObject is initializing ssl");
                Jvm.initSSL();
            }
        } catch (Throwable t) {
            System.out.println(Jvm.class.getName() + ".transmitObject could not initialize ssl");
        }
        url = new URL(servletURL);
        conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setUseCaches(false);
        os = conn.getOutputStream();
        writeSerial(obj, os);
        os.flush();
        os.close();
        try {
            is = conn.getInputStream();
            Object rcvObj = readSerial(is);
            is.close();
            return rcvObj;
        } catch (IOException x2) {
            x2.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    /** 
     * Uses reflection to get a property of the given object
     */
    public static Object invokeMethod(Object tgt, String methodName, Class types[], Object args[]) {
        for (Class c = tgt.getClass(); c != null; c = c.getSuperclass()) {
            if (c.getName().equals("java.lang.Object")) return null;
            Method method = null;
            try {
                method = c.getDeclaredMethod(methodName, types);
                try {
                    return method.invoke(tgt, args);
                } catch (IllegalAccessException iax) {
                    iax.printStackTrace();
                } catch (IllegalArgumentException iargx) {
                    iargx.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                    return null;
                }
            } catch (NoSuchMethodException nsmx) {
                nsmx.printStackTrace();
            } catch (SecurityException sex) {
                sex.printStackTrace();
            }
        }
        return null;
    }

    /**                                                                               
     * Replaces the current/default Debug object.
     */
    public static void setDebug(Debug dbg) {
        if (dbg == null) throw new IllegalArgumentException();
        _debug = dbg;
    }

    /**                                         
     * Retrieves the Debug object.  
     */
    public static Debug debug() {
        return _debug;
    }

    /**
     * Retrieve the Debug mapped to the given Class's classname.
     */
    public static Debug debug(Class clazz) {
        return debug(clazz.getName());
    }

    /**
     * Retrieve the Debug mapped to the given Object's classname.
     */
    public static Debug debug(Object o) {
        return debug(o.getClass().getName());
    }

    /**
     * Retrieve the Debug mapped to key.
     */
    public static Debug debug(String key) {
        Debug dbg = getDebugFor(key);
        if (dbg == null) dbg = _debug;
        return dbg;
    }

    /**
     * Add's another Debug to the current ones internal hashtable, mapped to key.  
     */
    public static void addDebugFor(String key, Debug dbg) {
        _debug.addDebugFor(key, dbg);
    }

    /**                                                           
     * Remove the Debug mapped to the key and return it or null.  
     */
    public static Debug removeDebugFor(String key) {
        return _debug.removeDebugFor(key);
    }

    /**
     * Retrieive the Debug mapped to key.
     */
    public static Debug getDebugFor(String key) {
        return _debug.getDebugFor(key);
    }

    /**
     * Convienence method for printing part of a line of text
     */
    public static synchronized void print(Object o) {
        System.out.print(o);
    }

    /**
     * Convienence method for printing a blank line.
     */
    public static synchronized void println() {
        System.out.println();
    }

    /**
     * Convienence methods to print the object as a string?
     */
    public static synchronized void println(Object o) {
        System.out.println(o);
    }

    /**
     * displays the prompt then waits for and returns a string input by user 
     */
    public static String input(String prompt) {
        String dflt = "";
        print(prompt);
        try {
            dflt = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception X) {
        }
        return dflt;
    }

    /**
     * Tests to see if the local filesystem can be read and written.
     */
    public static boolean isFileSystemAvailable() {
        try {
            File file = new File("" + System.currentTimeMillis() + ".txt");
            if (file.exists()) file = new File("" + System.currentTimeMillis() + ".txt");
            if (file.exists()) {
                Jvm.println("com.warserver.Jvm.isFileSystemAvailable: temp file already exists");
                return false;
            }
            writeTextFile("Created by java program: com.warserver.Jvm.isFileSystemAvailable()", file);
            String data = readTextFile(file);
            file.delete();
            if (data == null) return false;
            return true;
        } catch (Throwable t) {
        }
        return false;
    }

    /**
     * Creates a new ThreadPool with the given number of threads and stores it in the cache under the given key. 
     *
     * @param name the cache key the ThreadPool is stored under
     * @param nThreads the number of threads in the pool
     * @return the newly create ThreadPool
     * @exception java.lang.IllegalArgumentException if name is null, or nThreads isn't between 1-10.
     */
    public static ThreadPool ThreadPool(String name, int nThreads) {
        if ((name == null) || (name.equals(""))) throw new IllegalArgumentException("Name was null or empty");
        ThreadPool threads = new ThreadPool(nThreads);
        cache.put(name, threads);
        return threads;
    }

    /**
     * Removes a ThreadPool created by <code>createThreadPool</code>.
     * This does not stop it's threads from continuing to execute.
     * Does nothing if the object under the key isn't a ThreadPool.
     * 
     * @param the cache name of the ThreadPool
     * @return the ThreadPool mapped to the name or null if none.
     */
    public static ThreadPool removeThreadPool(String name) {
        Object obj = cache.get(name);
        if (obj instanceof ThreadPool) return (ThreadPool) cache.remove(name); else return null;
    }

    /**
     * Creates, or returns an existing ThreadPool from the cache using the key.
     * If it is created then nThreads is used, otherwise it is ignored.
     *
     * @param key the cache key the ThreadPool is stored with.
     * @param nThreads the number of threads in the pool.
     * @return The newly created ThreadPool
     */
    public static ThreadPool threadpool(Object key, int nThreads) {
        Object obj = cache.get(key);
        if (obj == null) {
            obj = new ThreadPool(nThreads);
            cache.put(key, obj);
        }
        return (ThreadPool) obj;
    }

    /**
     * Creates, or returns an existing ThreadPool from the cache using the key.
     * A newly created ThreadPool is assigned 4 threads by default.
     *
     * @param key the cache key the ThreadPool is stored with.
     * @param nThreads the number of threads in the pool.
     * @return The newly created ThreadPool
     */
    public static ThreadPool threadpool(Object key) {
        return threadpool(key, 4);
    }

    /**
     * Creates, or returns an existing Hashtable from the cache using the key.
     */
    public static Hashtable hashtable(Object key) {
        Object obj = cache.get(key);
        if (obj == null) {
            obj = new Hashtable();
            cache.put(key, obj);
        }
        return (Hashtable) obj;
    }

    /**
     * Creates, or returns an existing Vector from the cache using the key.
     */
    public static Vector vector(Object key) {
        Object obj = cache.get(key);
        if (obj == null) {
            obj = new Vector();
            cache.put(key, obj);
        }
        return (Vector) obj;
    }

    /**
     * Creates, or returns an existing Properties from the cache using the key.
     */
    public static Properties properties(Object key) {
        Object obj = cache.get(key);
        if (obj == null) {
            obj = new Properties();
            cache.put(key, obj);
        }
        return (Properties) obj;
    }

    public static void setProperty(Object key, String name, String value) {
        properties(key).setProperty(name, value);
    }

    public static String getProperty(Object key, String name, String defaultValue) {
        return properties(key).getProperty(name, defaultValue);
    }

    public static String getProperty(Object key, String name) {
        return properties(key).getProperty(name);
    }

    public static Enumeration elements(Object key) {
        Object obj = cache.get(key);
        if (obj == null) return null;
        if (obj instanceof Cache) return (Enumeration) ((Cache) obj).elements(); else if (obj instanceof Hashtable) return (Enumeration) ((Hashtable) obj).elements(); else return null;
    }

    public static Enumeration keys(Object key) {
        Object obj = cache.get(key);
        if (obj == null) return null;
        if (obj instanceof Cache) return (Enumeration) ((Cache) obj).keys(); else if (obj instanceof Hashtable) return (Enumeration) ((Hashtable) obj).keys(); else if (obj instanceof Properties) return (Enumeration) ((Properties) obj).propertyNames(); else return null;
    }

    /**
     * Does the one-time initialization required to let this jvm support https.
     * Assumes jsse 1.02 is installed. It adds the ssl SecurityProvider and 
     * set the property allowing the https protocol. It also creates a TrustManager
     * that trusts all certificates in order to workaround a jsse bug that reports
     * a certificate error when there is no problem.
     * <p>
     * This method will detect if initialization has alredy been done.
     * If so then this call returns immediately.
     */
    public static void initSSL() throws SSLException {
        try {
            if (sslEnabled) return;
            Class clazz = Class.forName("com.warserver.ssl.SSLInitializer");
            SSLInitializer initializer = (SSLInitializer) clazz.newInstance();
            initializer.init();
            sslEnabled = true;
        } catch (Exception x) {
            x.printStackTrace();
            throw new SSLException(x.getMessage());
        }
    }
}
