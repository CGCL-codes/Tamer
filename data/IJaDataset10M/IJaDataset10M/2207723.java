package com.google.solarchallenge.server.servlets.gwtservices;

import static com.google.solarchallenge.shared.ServletPaths.NO_OP;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.solarchallenge.client.rpc.BlobStoreService;

/**
 * Server side implementation for GWT RPC {@link BlobStoreService}.
 *
 * @author Arjun Satyapal
 */
public class BlobStoreServiceImpl extends RemoteServiceServlet implements BlobStoreService {

    @Override
    public String getUploadUrl(String callbackToken) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        return blobstoreService.createUploadUrl(NO_OP.getRelativePath());
    }
}
