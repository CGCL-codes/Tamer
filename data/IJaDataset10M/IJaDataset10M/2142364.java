package org.dasein.cloud.nimbula;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.dasein.cloud.AbstractCloud;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.nimbula.compute.NimbulaComputeServices;
import org.dasein.cloud.nimbula.network.NimbulaNetworkServices;
import org.dasein.cloud.storage.BlobStoreSupport;
import org.dasein.cloud.storage.StorageServices;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NimbulaDirector extends AbstractCloud {

    @Nonnull
    private static String getLastItem(@Nonnull String name) {
        int idx = name.lastIndexOf('.');
        if (idx < 0) {
            return name;
        } else if (idx == (name.length() - 1)) {
            return "";
        }
        return name.substring(idx + 1);
    }

    @Nonnull
    public static Logger getLogger(@Nonnull Class<?> cls) {
        String pkg = getLastItem(cls.getPackage().getName());
        if (pkg.equals("nimbula")) {
            pkg = "";
        } else {
            pkg = pkg + ".";
        }
        return Logger.getLogger("dasein.cloud.nimbula.std." + pkg + getLastItem(cls.getName()));
    }

    @Nonnull
    public static Logger getWireLogger(@Nonnull Class<?> cls) {
        return Logger.getLogger("dasein.cloud.nimbula.wire." + getLastItem(cls.getPackage().getName()) + "." + getLastItem(cls.getName()));
    }

    private static final Logger logger = getLogger(NimbulaDirector.class);

    public NimbulaDirector() {
    }

    @Override
    @Nonnull
    public String getCloudName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "Private Nimbula Cloud";
        }
        String name = ctx.getCloudName();
        return (name == null ? "Private Nimbula Cloud" : name);
    }

    @Override
    @Nonnull
    public NimbulaComputeServices getComputeServices() {
        return new NimbulaComputeServices(this);
    }

    @Override
    @Nonnull
    public Site getDataCenterServices() {
        return new Site(this);
    }

    @Nonnull
    public String getNamePrefix() throws CloudException, InternalException {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            throw new CloudException("No context was set for this request");
        }
        try {
            String user = new String(ctx.getAccessPublic(), "utf-8");
            return ("/" + ctx.getAccountNumber() + "/" + user);
        } catch (UnsupportedEncodingException e) {
            throw new InternalException(e);
        }
    }

    @Nonnull
    public NimbulaNetworkServices getNetworkServices() {
        return new NimbulaNetworkServices(this);
    }

    @Override
    @Nonnull
    public String getProviderName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "Nimbula";
        }
        String name = ctx.getCloudName();
        return (name == null ? "Nimbula" : name);
    }

    @Nonnull
    String getURL(@Nonnull String resource) throws CloudException {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            throw new CloudException("No context was set for this request");
        }
        String endpoint = ctx.getEndpoint();
        if (endpoint == null) {
            throw new CloudException("No context was set for this request");
        }
        if (endpoint.endsWith("/")) {
            return endpoint + resource;
        } else {
            return endpoint + "/" + resource;
        }
    }

    @Nonnull
    public String[] parseId(@Nonnull String name) {
        int idx = name.indexOf("//");
        while (idx > -1) {
            name = name.replaceAll("//", "/");
        }
        while (name.startsWith("/") && name.length() > 1) {
            name = name.substring(1);
        }
        return name.split("/");
    }

    @Nonnegative
    public long parseTimestamp(@Nonnull String tsString) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return fmt.parse(tsString).getTime();
    }

    @Override
    @Nullable
    public String testContext() {
        try {
            ProviderContext ctx = getContext();
            if (ctx == null) {
                return null;
            }
            if (!getComputeServices().getVirtualMachineSupport().isSubscribed()) {
                return null;
            }
            if (hasStorageServices()) {
                StorageServices services = getStorageServices();
                if (services != null && services.hasBlobStoreSupport()) {
                    BlobStoreSupport support = services.getBlobStoreSupport();
                    if (support != null && !support.isSubscribed()) {
                        return null;
                    }
                }
            }
            return ctx.getAccountNumber();
        } catch (Throwable t) {
            logger.warn("Failed to test Nimbula context: " + t.getMessage());
            if (logger.isDebugEnabled()) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
