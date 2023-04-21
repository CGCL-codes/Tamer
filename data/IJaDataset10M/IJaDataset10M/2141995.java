package org.isqlviewer.sql.embedded;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.isqlviewer.ServiceReference;
import org.isqlviewer.bookmarks.BookmarkFolder;
import org.isqlviewer.history.CommandType;
import org.isqlviewer.history.HistoricalCommand;
import org.isqlviewer.util.IsqlToolkit;
import org.isqlviewer.util.LocalMessages;
import org.isqlviewer.util.StringUtilities;
import org.isqlviewer.xml.BookmarkDigester;
import org.isqlviewer.xml.ServiceDigester;
import org.xml.sax.InputSource;

/**
 * Class for providing backward compatability to previous versions of ISQL-Viewer resources.
 * <p>
 * 
 * @author Mark A. Kobold &lt;mkobold at isqlviewer dot com&gt;
 * @version 1.0
 */
public class CompatabilityKit {

    private static final String CACHE_FILE = "services.cache";

    private static final String KEY_SERVICE_LIST = "SERVICE_ORDER";

    private static final String SERVICE_SEPERATOR = "";

    private static final String HISTORY_FIELD_SEPERATOR = "";

    private static final char SERVICE_FIELD_SEPERATOR = '=';

    private static final String BUNDLE_NAME = "org.isqlviewer.sql.embedded.ResourceBundle";

    private static LocalMessages messages = new LocalMessages(BUNDLE_NAME);

    private static Logger logger = IsqlToolkit.getApplicationLogger();

    /**
     * Gets all the bookmarks from the bookmarks.xml file from the iSQL-Viewer base directory.
     * <p>
     * 
     * @return root bookmark folder containing all bookmarks and sub-folders.
     */
    public static BookmarkFolder get2xxBookmarks() {
        File bookmarksFile = new File(IsqlToolkit.getBaseDirectory(), "bookmarks.xml");
        BookmarkFolder rootFolder = null;
        if (bookmarksFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(bookmarksFile);
                InputSource source = new InputSource(fis);
                rootFolder = BookmarkDigester.parseBookmarks(source);
            } catch (FileNotFoundException error) {
                logger.warn(messages.format("compatability_kit.no_bookmarks_xml"));
            } catch (Exception error) {
                logger.error(messages.format("compatability_kit.generic_bookmark_error"), error);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception ignored) {
                    }
                }
                if (rootFolder == null) {
                    rootFolder = BookmarkFolder.createRootFolder();
                }
            }
        }
        return rootFolder;
    }

    /**
     * Loads all service definitions defined in the services.cache file in the iSQL-Viewer base directory.
     * <p>
     * 
     * @return all existing service definitions declared within the old caches file.
     */
    public static Collection<ServiceReference> get2xxServices() {
        ArrayList<ServiceReference> references = new ArrayList<ServiceReference>();
        Map<String, File> serviceFileMapping = loadServiceCache();
        int order = 0;
        Set<Map.Entry<String, File>> serviceSet = serviceFileMapping.entrySet();
        for (Map.Entry<String, File> entry : serviceSet) {
            ServiceReference reference = new ServiceReference();
            reference.setName(entry.getKey());
            reference.setOrder(order++);
            try {
                reference.setResourceURL(entry.getValue().toURL());
            } catch (MalformedURLException ignored) {
            }
            references.add(reference);
        }
        return references;
    }

    /**
     * Aquires the autoloaded history elements from previous versions of iSQL-Viewer.
     * <p>
     * 
     * @return collection of all history elements logged in the 'autoload.history' file.
     */
    public static Collection<HistoricalCommand> get2xxHistory() {
        ArrayList<HistoricalCommand> history = new ArrayList<HistoricalCommand>();
        FileReader fileReader = null;
        StringBuilder buffer = new StringBuilder("");
        File baseDirectory = new File(IsqlToolkit.getBaseDirectory(), "history");
        try {
            File historyFile = new File(baseDirectory, "autoload.history");
            if (historyFile.exists() && historyFile.isFile()) {
                fileReader = new FileReader(historyFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    if (line.trim().length() == 0) {
                        String content = buffer.toString();
                        history.add(decodeHistoryItem(content));
                        buffer.setLength(0);
                        continue;
                    }
                    buffer.append(line);
                    buffer.append('\n');
                }
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Exception ignored) {
            }
        }
        return history;
    }

    private static HistoricalCommand decodeHistoryItem(String s) {
        HistoricalCommand command = new HistoricalCommand();
        command.setType(CommandType.QUERY);
        String b64Decoded = StringUtilities.decodeBase64(s);
        StringTokenizer st = new StringTokenizer(b64Decoded, HISTORY_FIELD_SEPERATOR);
        long executionTime = Long.parseLong(st.nextToken());
        command.setQueryTime(new Date(executionTime));
        command.setService(st.nextToken());
        command.setCommandText(st.nextToken());
        return command;
    }

    private static Map<String, File> loadServiceCache() {
        ArrayList<String> preferredOrder = new ArrayList<String>();
        HashMap<String, File> serviceFileMapping = new HashMap<String, File>();
        File file = new File(IsqlToolkit.getBaseDirectory(), CACHE_FILE);
        if (!file.exists()) {
            return serviceFileMapping;
        }
        if (file.canRead()) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                BufferedReader lineReader = new BufferedReader(fileReader);
                while (lineReader.ready()) {
                    String data = lineReader.readLine();
                    if (data.charAt(0) == '#') {
                        continue;
                    }
                    int idx0 = 0;
                    int idx1 = data.indexOf(SERVICE_FIELD_SEPERATOR);
                    String name = StringUtilities.decodeASCII(data.substring(idx0, idx1));
                    String uri = StringUtilities.decodeASCII(data.substring(idx1 + 1));
                    if (name.equalsIgnoreCase(KEY_SERVICE_LIST)) {
                        StringTokenizer st = new StringTokenizer(uri, SERVICE_SEPERATOR);
                        while (st.hasMoreTokens()) {
                            String serviceName = st.nextToken();
                            preferredOrder.add(serviceName.toLowerCase().trim());
                        }
                        continue;
                    }
                    try {
                        URL url = new URL(uri);
                        File serviceFile = new File(url.getFile());
                        if (serviceFile.isDirectory()) {
                            logger.warn(messages.format("compatability_kit.service_mapped_to_directory", name, uri));
                            continue;
                        } else if (!serviceFile.canRead()) {
                            logger.warn(messages.format("compatability_kit.service_not_readable", name, uri));
                            continue;
                        } else if (!serviceFile.exists()) {
                            logger.warn(messages.format("compatability_kit.service_does_not_exist", name, uri));
                            continue;
                        }
                        String bindName = name.toLowerCase().trim();
                        InputStream inputStream = null;
                        try {
                            inputStream = url.openStream();
                            InputSource inputSource = new InputSource(inputStream);
                            bindName = ServiceDigester.parseService(inputSource, IsqlToolkit.getSharedEntityResolver()).getName();
                        } catch (Exception error) {
                            continue;
                        }
                        if (serviceFileMapping.put(bindName, serviceFile) != null) {
                            logger.warn(messages.format("compatability_kit.service_duplicate_name_error", name, uri));
                        }
                    } catch (MalformedURLException e) {
                        logger.error(messages.format("compatability_kit.service_uri_error", name, uri), e);
                    }
                }
            } catch (IOException ioe) {
                logger.error("compatability_kit.service_generic_error", ioe);
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
        return serviceFileMapping;
    }
}
