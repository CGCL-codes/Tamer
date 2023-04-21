package de.sicari.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import de.sicari.kernel.Environment;
import de.sicari.service.AbstractService;
import de.sicari.util.ArgsParser;
import de.sicari.util.ArgsParserException;
import de.sicari.util.MemoryCache;
import de.sicari.util.WhatIs;

/**
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @author Jan Peters
 * @version "$Id: HttpResourceStoreImpl.java 204 2007-07-11 19:26:55Z jpeters $"
 */
public class HttpResourceStoreImpl extends AbstractService implements HttpResourceStore {

    public static final long DEFAULT_TIMEOUT = 300000;

    public static final long DEFAULT_SIZE = 1000;

    private static final String CMD_INFO_ = "info";

    private static final String CMD_UNREG_ = "unregister";

    private static final String CMD_REG_ = "register";

    private static final String CMD_TIME_ = "timeout";

    private static final String CMD_MIME_ = "mime";

    private static final String CMD_PATH_ = "file";

    private static final String CMD_HELP_ = "help";

    private static final String CMD_ID_ = "id";

    private static final String CMD_DESCR = CMD_INFO_ + ":!," + CMD_UNREG_ + ":!," + CMD_REG_ + ":!," + CMD_PATH_ + ":F," + CMD_ID_ + ":s," + CMD_MIME_ + ":s," + CMD_TIME_ + ":n," + CMD_HELP_ + ":!";

    protected MemoryCache resources_;

    public HttpResourceStoreImpl() {
        resources_ = new MemoryCache(DEFAULT_SIZE);
    }

    /**
     * The dependencies to other objects in the global <code>
     * Environment</code>. 
     */
    private static final String[] DEPEND_ = {};

    /**
     * Returns the String with the service's short description.
     *
     * @return The String with the service's short description.
     */
    public String info() {
        return new String("HTTP Resource Store");
    }

    /**
     * Returns the String with the author's name.
     *
     * @return The String with the author's name.
     */
    public String author() {
        return "Jan Peters";
    }

    /**
     * Returns the revision number of this class as a string.
     *
     * @return The revision number of this class as a string.
     */
    public String revision() {
        return new String("$Revision: 204 $/$Date: 2007-07-11 15:26:55 -0400 (Wed, 11 Jul 2007) $");
    }

    /**
     * Returns the dependencies of this service as described in
     * {@link de.sicari.service.Service Service}. 
     *
     * @return The array of dependencies.
     */
    public String[] dependencies() {
        return (String[]) DEPEND_.clone();
    }

    /**
     * @return The string representation of this instance.
     */
    public String toString() {
        StringBuffer buf;
        buf = new StringBuffer();
        buf.append("\nGeneral info");
        buf.append("\n------------\n");
        buf.append(super.toString());
        buf.append("\n\nResource Store info");
        buf.append("\n-------------------\n");
        buf.append(resources_);
        return buf.toString();
    }

    public void registerResource(String id, byte[] data) {
        registerResource(id, data, DEFAULT_TIMEOUT);
    }

    public void registerResource(String id, byte[] data, String mime) {
        registerResource(id, data, mime, DEFAULT_TIMEOUT);
    }

    public void registerResource(String id, HttpResource resource) {
        if (id == null || resource == null) {
            return;
        }
        registerResource(id, resource, DEFAULT_TIMEOUT);
    }

    public void registerResource(String id, byte[] data, long timeout) {
        HttpResource resource;
        if (id == null || data == null) {
            return;
        }
        resource = new HttpResource(data, "text/html");
        registerResource(id, resource, timeout);
    }

    public void registerResource(String id, byte[] data, String mime, long timeout) {
        HttpResource resource;
        if (id == null || data == null) {
            return;
        }
        resource = new HttpResource(data, mime);
        registerResource(id, resource, timeout);
    }

    public void registerResource(String id, HttpResource resource, long timeout) {
        if (id == null || resource == null) {
            return;
        }
        resources_.add(id, resource, timeout);
    }

    public HttpResource removeResource(String id) {
        if (id == null) {
            return null;
        }
        return (HttpResource) resources_.remove(id);
    }

    public boolean isRegistered(String id) {
        if (resources_.get(id) != null) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] getData(String id) {
        HttpResource resource;
        resource = getResource(id);
        if (resource != null) {
            return resource.getData();
        } else {
            return null;
        }
    }

    public String getMimeType(String id) {
        HttpResource resource;
        resource = getResource(id);
        if (resource != null) {
            return resource.getMimeType();
        } else {
            return null;
        }
    }

    public HttpResource getResource(String id) {
        HttpResource resource;
        if (id == null) {
            return null;
        }
        resource = (HttpResource) resources_.get(id);
        if (resource != null) {
            return resource.deepClone();
        } else {
            return null;
        }
    }

    /**
     * Registers a data file under a distinct id from command line.
	 * 
	 * Parameters: 
     * <pre>
	 * -register -id <id> -path <file> [ -mime <String> ] [ -timeout <int> ]
	 * -unregister -id <id>
     * </pre>
     * 
     * Example: in order to register a GIF image at the id 'semoa-logo' type 
     * the following command at the SeMoA shell. <br>
     * 
     * <code>
     * java de.fhg.igd.semoa.web.HttpResourceStoreImpl -register -id semoa-logo -file semoa_logo.gif
     * </code>
	 */
    public static void main(String args[]) {
        HttpResourceStore res;
        DataInputStream in;
        Environment env;
        ArgsParser ap;
        long timeout;
        String mime;
        byte[] buf;
        String key;
        String id;
        File path;
        ap = new ArgsParser(CMD_DESCR);
        try {
            ap.parse(args);
            if (ap.isDefined(CMD_HELP_)) {
                System.out.println(usage());
                return;
            }
            if (ap.isDefined(CMD_INFO_)) {
                env = Environment.getEnvironment();
                key = WhatIs.stringValue(HttpResourceStore.WHATIS);
                res = (HttpResourceStore) env.lookup(key);
                System.out.println(res.toString());
                return;
            }
            if (!ap.isDefined(CMD_REG_) && !ap.isDefined(CMD_UNREG_)) {
                System.out.println("Do not know whether to register or to unregister");
                return;
            }
            if (!ap.isDefined(CMD_ID_)) {
                System.out.println("Need an id of the file");
                return;
            }
            id = ap.stringValue(CMD_ID_);
            if (ap.isDefined(CMD_REG_)) {
                if (!ap.isDefined(CMD_PATH_)) {
                    System.out.println("Need file to register");
                    return;
                }
                path = ap.fileValue(CMD_PATH_);
                if (!path.exists() || !path.canRead()) {
                    System.out.println("File cannot be accessed.");
                    return;
                }
                try {
                    buf = new byte[(int) path.length()];
                    in = new DataInputStream(new FileInputStream(path));
                    in.readFully(buf);
                    env = Environment.getEnvironment();
                    key = WhatIs.stringValue(HttpResourceStore.WHATIS);
                    res = (HttpResourceStore) env.lookup(key);
                    if (ap.isDefined(CMD_MIME_) && ap.isDefined(CMD_TIME_)) {
                        timeout = ap.longValue(CMD_TIME_);
                        mime = ap.stringValue(CMD_MIME_);
                        res.registerResource(id, buf, mime, timeout);
                        return;
                    }
                    if (!ap.isDefined(CMD_MIME_) && !ap.isDefined(CMD_TIME_)) {
                        res.registerResource(id, buf);
                        return;
                    }
                    if (ap.isDefined(CMD_TIME_)) {
                        timeout = ap.longValue(CMD_TIME_);
                        res.registerResource(id, buf, timeout);
                        return;
                    }
                    mime = ap.stringValue(CMD_MIME_);
                    res.registerResource(id, buf, mime);
                    return;
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            if (ap.isDefined(CMD_UNREG_)) {
                env = Environment.getEnvironment();
                key = WhatIs.stringValue(HttpResourceStore.WHATIS);
                res = (HttpResourceStore) env.lookup(key);
                res.removeResource(id);
                System.out.println("Removed file " + id);
            }
        } catch (ArgsParserException ex) {
            System.out.println("Option " + ex.getMessage() + " not recognized");
        }
    }

    /**
     * Print usage info describing all parameters of the 
     * {@link #main(String[]) main()} method
     * 
     * @return the usage info
     */
    protected static String usage() {
        return ("Usage: java HttpRecourceStoreImpl\n" + "\t-" + CMD_HELP_ + "\n" + "\t-" + CMD_INFO_ + "\n" + "\t-" + CMD_REG_ + " -" + CMD_PATH_ + " <file>" + " -" + CMD_ID_ + " <id>" + " [-" + CMD_MIME_ + " <String>]" + " [-" + CMD_TIME_ + " <int>]\n" + "\t-" + CMD_UNREG_ + " -" + CMD_ID_ + " <id>" + "\nwhere:\n" + "\n-" + CMD_HELP_ + "\n" + "\tShows this text.\n" + "\n-" + CMD_INFO_ + "\n" + "\tPrints some information about the status of the resource store.\n" + "\n-" + CMD_REG_ + "\n" + "\tRegisters a file with a given identifier.\n" + "\t-" + CMD_PATH_ + " <file>\n" + "\t\tThe path of the file which shall be registered.\n" + "\t-" + CMD_ID_ + " <id>\n" + "\t\tThe identifier under which the file shall be registered.\n" + "\t-" + CMD_MIME_ + " <String>\n" + "\t\t(optional)MIME type of the registered object.\n" + "\t-" + CMD_TIME_ + " <int>\n" + "\t\t(optional)The time period for which the file should be registered (in ms).\n" + "\t\tIf no value is given the default value of 300000ms is used.\n" + "\n-" + CMD_UNREG_ + "\n" + "\tUnregisters an object.\n" + "\t-" + CMD_ID_ + " <id>\n" + "\t\tRefers to the object which shall be unregistered.\n");
    }
}
