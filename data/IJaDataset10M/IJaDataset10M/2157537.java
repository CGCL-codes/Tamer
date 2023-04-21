package org.dasein.cloud.openstack.nova.os;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dasein.cloud.AbstractCloud;
import org.dasein.cloud.CloudErrorType;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.openstack.nova.os.compute.NovaComputeServices;
import org.dasein.cloud.openstack.nova.os.ext.hp.HPPlatformServices;
import org.dasein.cloud.openstack.nova.os.ext.rackspace.RackspacePlatformServices;
import org.dasein.cloud.openstack.nova.os.identity.NovaIdentityServices;
import org.dasein.cloud.openstack.nova.os.network.NovaNetworkServices;
import org.dasein.cloud.openstack.nova.os.storage.SwiftStorageServices;
import org.dasein.cloud.platform.PlatformServices;
import org.dasein.cloud.storage.StorageServices;

public class NovaOpenStack extends AbstractCloud {

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
    public static Logger getLogger(@Nonnull Class<?> cls, @Nonnull String type) {
        String pkg = getLastItem(cls.getPackage().getName());
        if (pkg.equals("os")) {
            pkg = "";
        } else {
            pkg = pkg + ".";
        }
        return Logger.getLogger("dasein.cloud.nova." + type + "." + pkg + getLastItem(cls.getName()));
    }

    public static boolean isSupported(@Nonnull String version) {
        int idx = version.indexOf('.');
        int major, minor;
        if (idx < 0) {
            major = Integer.parseInt(version);
            minor = 0;
        } else {
            String[] parts = version.split("\\.");
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
        }
        return (major <= 2 && minor < 10);
    }

    private AuthenticationContext authenticationContext;

    public NovaOpenStack() {
    }

    @Nonnull
    public synchronized AuthenticationContext getAuthenticationContext() throws CloudException, InternalException {
        if (authenticationContext == null) {
            NovaMethod method = new NovaMethod(this);
            authenticationContext = method.authenticate();
            if (authenticationContext == null) {
                NovaException.ExceptionItems items = new NovaException.ExceptionItems();
                items.code = HttpServletResponse.SC_UNAUTHORIZED;
                items.type = CloudErrorType.AUTHENTICATION;
                items.message = "unauthorized";
                items.details = "The API keys failed to authentication with the specified endpoint.";
                throw new NovaException(items);
            }
        }
        return authenticationContext;
    }

    @Override
    @Nonnull
    public String getCloudName() {
        ProviderContext ctx = getContext();
        String name = (ctx == null ? null : ctx.getCloudName());
        return (name != null ? name : "OpenStack");
    }

    @Override
    @Nonnull
    public NovaComputeServices getComputeServices() {
        return new NovaComputeServices(this);
    }

    @Override
    @Nonnull
    public NovaIdentityServices getIdentityServices() {
        return new NovaIdentityServices(this);
    }

    @Override
    @Nonnull
    public NovaLocationServices getDataCenterServices() {
        return new NovaLocationServices(this);
    }

    @Override
    @Nonnull
    public PlatformServices getPlatformServices() {
        if (getProviderName().equals("HP")) {
            return new HPPlatformServices(this);
        } else if (getProviderName().equals("Rackspace")) {
            return new RackspacePlatformServices(this);
        }
        return super.getPlatformServices();
    }

    @Override
    @Nullable
    public StorageServices getStorageServices() {
        ProviderContext pc = getContext();
        if (pc == null) {
            return null;
        }
        if (pc.getStorageEndpoint() == null) {
            try {
                AuthenticationContext ctx = getAuthenticationContext();
                if (ctx.getStorageUrl() == null) {
                    return super.getStorageServices();
                }
            } catch (CloudException e) {
                e.printStackTrace();
                return null;
            } catch (InternalException e) {
                e.printStackTrace();
                return null;
            }
            return new SwiftStorageServices(this);
        }
        return super.getStorageServices();
    }

    @Override
    @Nonnull
    public String getProviderName() {
        ProviderContext ctx = getContext();
        String name = (ctx == null ? null : ctx.getProviderName());
        return (name != null ? name : "OpenStack");
    }

    @Nonnegative
    public int getMajorVersion() throws CloudException, InternalException {
        AuthenticationContext ctx = getAuthenticationContext();
        String endpoint = ctx.getComputeUrl();
        if (endpoint == null) {
            endpoint = ctx.getStorageUrl();
            if (endpoint == null) {
                return 1;
            }
        }
        while (endpoint.endsWith("/") && endpoint.length() > 1) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        String[] parts = endpoint.split("/");
        int idx = parts.length - 1;
        do {
            endpoint = parts[idx];
            while (!Character.isDigit(endpoint.charAt(0)) && endpoint.length() > 1) {
                endpoint = endpoint.substring(1);
            }
            if (Character.isDigit(endpoint.charAt(0))) {
                int i = endpoint.indexOf('.');
                try {
                    if (i == -1) {
                        return Integer.parseInt(endpoint);
                    }
                    String[] d = endpoint.split("\\.");
                    return Integer.parseInt(d[0]);
                } catch (NumberFormatException ignore) {
                }
            }
        } while ((idx--) > 0);
        return 1;
    }

    @Nonnegative
    public int getMinorVersion() throws CloudException, InternalException {
        AuthenticationContext ctx = getAuthenticationContext();
        String endpoint = ctx.getComputeUrl();
        if (endpoint == null) {
            endpoint = ctx.getStorageUrl();
            if (endpoint == null) {
                return 1;
            }
        }
        while (endpoint.endsWith("/") && endpoint.length() > 1) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        String[] parts = endpoint.split("/");
        int idx = parts.length - 1;
        do {
            endpoint = parts[idx];
            while (!Character.isDigit(endpoint.charAt(0)) && endpoint.length() > 1) {
                endpoint = endpoint.substring(1);
            }
            if (Character.isDigit(endpoint.charAt(0))) {
                int i = endpoint.indexOf('.');
                try {
                    if (i == -1) {
                        return Integer.parseInt(endpoint);
                    }
                    String[] d = endpoint.split("\\.");
                    return Integer.parseInt(d[1]);
                } catch (NumberFormatException ignore) {
                }
            }
        } while ((idx--) > 0);
        return 1;
    }

    @Override
    @Nonnull
    public NovaNetworkServices getNetworkServices() {
        return new NovaNetworkServices(this);
    }

    public boolean isPostCactus() throws CloudException, InternalException {
        return (getMajorVersion() > 1 || getMinorVersion() > 0);
    }

    public long parseTimestamp(String time) throws CloudException {
        if (time == null) {
            return 0L;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if (time.length() > 0) {
            try {
                return fmt.parse(time).getTime();
            } catch (ParseException e) {
                fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    return fmt.parse(time).getTime();
                } catch (ParseException encore) {
                    throw new CloudException("Could not parse date: " + time);
                }
            }
        }
        return 0L;
    }

    @Override
    @Nullable
    public String testContext() {
        Logger logger = getLogger(NovaOpenStack.class, "std");
        if (logger.isTraceEnabled()) {
            logger.trace("enter - " + NovaOpenStack.class.getName() + ".textContext()");
        }
        try {
            try {
                NovaMethod method = new NovaMethod(this);
                AuthenticationContext ctx = method.authenticate();
                return (ctx == null ? null : ctx.getTenantId());
            } catch (Throwable t) {
                logger.warn("Failed to test OpenStack connection context: " + t.getMessage());
                if (logger.isTraceEnabled()) {
                    t.printStackTrace();
                }
                return null;
            }
        } finally {
            if (logger.isTraceEnabled()) {
                logger.trace("exit - " + NovaOpenStack.class.getName() + ".testContext()");
            }
        }
    }
}
