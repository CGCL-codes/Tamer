package org.dasein.cloud.jclouds.atmos;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.dasein.cloud.AbstractCloud;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.CloudProvider;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.dc.DataCenterServices;
import org.dasein.cloud.jclouds.atmos.storage.AtmosStorageServices;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.concurrent.MoreExecutors;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import com.google.inject.Module;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Atmos extends AbstractCloud {

    private static final Logger logger = Logger.getLogger(Atmos.class);

    public Atmos() {
    }

    @Override
    @Nonnull
    public String getCloudName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "Atmos";
        }
        String name = ctx.getCloudName();
        return (name == null ? "Atmos" : name);
    }

    @Nonnull
    public BlobStoreContext getAtmosContext() throws CloudException {
        logger.debug("enter - getAtmosContext()");
        try {
            ProviderContext ctx = getContext();
            if (ctx == null) {
                throw new CloudException("No context has been established for this request");
            }
            String endpoint = ctx.getEndpoint();
            if (endpoint == null) {
                throw new CloudException("No endpoint has been defined for this request");
            }
            String account = new String(ctx.getAccessPublic());
            String key = new String(ctx.getAccessPrivate());
            String userId = account + "/" + ctx.getAccountNumber();
            ArrayList<Module> modules = new ArrayList<Module>();
            modules.add(new Log4JLoggingModule());
            modules.add(new ExecutorServiceModule(MoreExecutors.sameThreadExecutor(), MoreExecutors.sameThreadExecutor()));
            if (endpoint.startsWith("https://storage.synaptic.att.com")) {
                return (new BlobStoreContextFactory()).createContext("synaptic-storage", userId, key, modules);
            } else {
                throw new CloudException("Unknown atmos endpoint: " + endpoint);
            }
        } finally {
            logger.debug("exit - getAtmosContext()");
        }
    }

    @Override
    @Nonnull
    public DataCenterServices getDataCenterServices() {
        CloudProvider compute = getComputeCloud();
        if (compute != null) {
            return compute.getDataCenterServices();
        }
        return new DataCenters(this);
    }

    @Override
    @Nonnull
    public String getProviderName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "EMC";
        }
        String name = ctx.getProviderName();
        return (name == null ? "EMC" : name);
    }

    @Override
    @Nonnull
    public AtmosStorageServices getStorageServices() {
        return new AtmosStorageServices(this);
    }

    public boolean isATT() throws CloudException {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            throw new CloudException("No context exists for this request");
        }
        String endpoint = ctx.getEndpoint();
        return (endpoint != null && endpoint.startsWith("https://storage.synaptic.att.com"));
    }

    @Override
    @Nullable
    public String testContext() {
        try {
            ProviderContext ctx = getContext();
            if (ctx == null) {
                return null;
            }
            if (!getStorageServices().getBlobStoreSupport().isSubscribed()) {
                return null;
            }
            return ctx.getAccountNumber();
        } catch (Throwable t) {
            logger.warn("Failed to test Atmos connection context: " + t.getMessage());
            if (logger.isDebugEnabled()) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
