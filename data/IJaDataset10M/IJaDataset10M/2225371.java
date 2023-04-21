package org.eclipse.osgi.internal.baseadaptor;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;
import org.eclipse.osgi.baseadaptor.*;
import org.eclipse.osgi.baseadaptor.hooks.StorageHook;
import org.eclipse.osgi.framework.adaptor.BundleData;
import org.eclipse.osgi.framework.adaptor.BundleOperation;
import org.eclipse.osgi.framework.debug.Debug;
import org.eclipse.osgi.framework.internal.core.ReferenceInputStream;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.*;

public class BundleInstall implements BundleOperation {

    private BaseData data;

    private URLConnection source;

    private BaseStorage storage;

    public BundleInstall(BaseData data, URLConnection source, BaseStorage storage) {
        this.data = data;
        this.source = source;
        this.storage = storage;
    }

    /**
	 * Begin the operation on the bundle (install, update, uninstall).
	 *
	 * @return BundleData object for the target bundle.
	 * @throws BundleException If a failure occured modifiying peristent storage.
	 */
    public BundleData begin() throws BundleException {
        try {
            InputStream in = null;
            try {
                data.setLastModified(System.currentTimeMillis());
                data.setStartLevel(storage.getInitialBundleStartLevel());
                StorageHook[] storageHooks = data.getAdaptor().getHookRegistry().getStorageHooks();
                StorageHook[] instanceHooks = new StorageHook[storageHooks.length];
                for (int i = 0; i < storageHooks.length; i++) instanceHooks[i] = storageHooks[i].create(data);
                data.setStorageHooks(instanceHooks);
                BaseStorageHook storageHook = (BaseStorageHook) data.getStorageHook(BaseStorageHook.KEY);
                in = source.getInputStream();
                URL sourceURL = source.getURL();
                String protocol = sourceURL == null ? null : sourceURL.getProtocol();
                if (in instanceof ReferenceInputStream) {
                    URL reference = ((ReferenceInputStream) in).getReference();
                    if (!"file".equals(reference.getProtocol())) throw new BundleException(NLS.bind(AdaptorMsg.ADAPTOR_URL_CREATE_EXCEPTION, reference));
                    storageHook.setReference(true);
                    storageHook.setFileName(reference.getPath());
                } else {
                    File genDir = storageHook.createGenerationDir();
                    if (!genDir.exists()) throw new IOException(NLS.bind(AdaptorMsg.ADAPTOR_DIRECTORY_CREATE_EXCEPTION, genDir.getPath()));
                    storageHook.setReference(false);
                    storageHook.setFileName(BaseStorage.BUNDLEFILE_NAME);
                    File outFile = new File(genDir, storageHook.getFileName());
                    if ("file".equals(protocol)) {
                        File inFile = new File(source.getURL().getPath());
                        if (inFile.isDirectory()) AdaptorUtil.copyDir(inFile, outFile); else AdaptorUtil.readFile(in, outFile);
                    } else {
                        AdaptorUtil.readFile(in, outFile);
                    }
                }
                Dictionary manifest = storage.loadManifest(data, true);
                for (int i = 0; i < instanceHooks.length; i++) instanceHooks[i].initialize(manifest);
            } finally {
                try {
                    if (in != null) in.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException ioe) {
            throw new BundleException(AdaptorMsg.BUNDLE_READ_EXCEPTION, ioe);
        }
        return (data);
    }

    public void undo() {
        if (data != null) {
            try {
                data.close();
            } catch (IOException e) {
                if (Debug.DEBUG && Debug.DEBUG_GENERAL) Debug.println("Unable to close " + data + ": " + e.getMessage());
            }
        }
        if (data != null) {
            BaseStorageHook storageHook = (BaseStorageHook) data.getStorageHook(BaseStorageHook.KEY);
            try {
                if (storageHook != null) storageHook.delete(false, BaseStorageHook.DEL_BUNDLE_STORE);
            } catch (IOException e) {
                data.getAdaptor().getEventPublisher().publishFrameworkEvent(FrameworkEvent.ERROR, data.getBundle(), e);
            }
        }
    }

    public void commit(boolean postpone) throws BundleException {
        storage.processExtension(data, BaseStorage.EXTENSION_INSTALLED);
        try {
            data.save();
        } catch (IOException e) {
            throw new BundleException(AdaptorMsg.ADAPTOR_STORAGE_EXCEPTION, e);
        }
        storage.updateState(data, BundleEvent.INSTALLED);
    }
}
