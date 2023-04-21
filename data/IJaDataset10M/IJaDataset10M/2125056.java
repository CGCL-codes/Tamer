package com.intel.gpe.client2.providers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.intel.gpe.client2.transfers.FileExport;
import com.intel.gpe.client2.transfers.FileImport;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.exceptions.GPEException;

/**
 * 
 * @author Alexander Lukichev
 * @version $Id: FileProvider.java,v 1.1 2006/04/14 12:21:21 lukichev Exp $
 *
 */
public class FileProvider {

    private static Logger logger = Logger.getLogger("com.intel.gpe");

    private Map<String, Class> fileGetters;

    private Map<String, Class> filePutters;

    public void addFileGetter(String protocol, Class clazz) {
        if (fileGetters == null) {
            fileGetters = new HashMap<String, Class>();
        }
        fileGetters.put(protocol, clazz);
    }

    public void addFilePutter(String protocol, Class clazz) {
        if (filePutters == null) {
            filePutters = new HashMap<String, Class>();
        }
        filePutters.put(protocol, clazz);
    }

    public List<FileExport> prepareGetters(StorageClient storage) {
        List<FileExport> result = new ArrayList<FileExport>();
        if (fileGetters != null && fileGetters.size() > 0) {
            String[] protocols;
            try {
                protocols = storage.getSupportedProtocols();
                if (protocols != null && protocols.length > 0) {
                    for (int i = 0; i < protocols.length; i++) {
                        Class clazz = (Class) fileGetters.get(protocols[i]);
                        try {
                            Constructor constructor = clazz.getConstructor(new Class[] { StorageClient.class });
                            result.add((FileExport) constructor.newInstance(new Object[] { storage }));
                        } catch (Throwable e) {
                            logger.log(Level.WARNING, "Cannot prepare getter: " + clazz, e);
                        }
                    }
                }
            } catch (GPEException e) {
            }
        }
        return result;
    }

    public List<FileImport> preparePutters(StorageClient storage) {
        List<FileImport> result = new ArrayList<FileImport>();
        if (filePutters != null && filePutters.size() > 0) {
            String[] protocols;
            try {
                protocols = storage.getSupportedProtocols();
                if (protocols != null && protocols.length > 0) {
                    for (int i = 0; i < protocols.length; i++) {
                        Class clazz = (Class) filePutters.get(protocols[i]);
                        try {
                            Constructor constructor = clazz.getConstructor(new Class[] { StorageClient.class });
                            result.add((FileImport) constructor.newInstance(new Object[] { storage }));
                        } catch (Throwable e) {
                            logger.log(Level.WARNING, "Cannot prepare putter: " + clazz, e);
                        }
                    }
                }
            } catch (GPEException e) {
            }
        }
        return result;
    }
}
