package org.dasein.cloud.cloudstack;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.dasein.cloud.AbstractCloud;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.cloudstack.compute.CloudstackComputeServices;
import org.dasein.cloud.cloudstack.network.CloudstackNetworkServices;
import org.dasein.cloud.storage.StorageServices;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CloudstackProvider extends AbstractCloud {

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
        if (pkg.equals("cloudstack")) {
            pkg = "";
        } else {
            pkg = pkg + ".";
        }
        return Logger.getLogger("dasein.cloud.cloudstack." + type + "." + pkg + getLastItem(cls.getName()));
    }

    public CloudstackProvider() {
    }

    @Override
    @Nonnull
    public String getCloudName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "Citrix";
        }
        String name = ctx.getCloudName();
        if (name == null) {
            return "Citrix";
        }
        return name;
    }

    @Override
    @Nonnull
    public CloudstackComputeServices getComputeServices() {
        return new CloudstackComputeServices(this);
    }

    @Override
    @Nonnull
    public Zones getDataCenterServices() {
        return new Zones(this);
    }

    @Override
    @Nonnull
    public CloudstackNetworkServices getNetworkServices() {
        return new CloudstackNetworkServices(this);
    }

    @Override
    @Nonnull
    public String getProviderName() {
        ProviderContext ctx = getContext();
        if (ctx == null) {
            return "Citrix";
        }
        String name = ctx.getProviderName();
        if (name == null) {
            return "Citrix";
        }
        return name;
    }

    @Nonnull
    public CloudstackVersion getVersion() {
        return CloudstackVersion.CS21;
    }

    @Nonnegative
    public long parseTime(@Nonnull String timestamp) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return df.parse(timestamp).getTime();
        } catch (ParseException e) {
            df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                return df.parse(timestamp).getTime();
            } catch (ParseException another) {
                return 0L;
            }
        }
    }

    @Override
    @Nullable
    public String testContext() {
        Logger logger = getLogger(CloudstackProvider.class, "std");
        if (logger.isTraceEnabled()) {
            logger.trace("enter - " + CloudstackProvider.class.getName() + ".testContext()");
        }
        try {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("testContext(): Checking Cloudstack compute credentials");
                }
                if (!getComputeServices().getVirtualMachineSupport().isSubscribed()) {
                    logger.warn("testContext(): Cloudstack compute credentials are not subscribed for VM services");
                    return null;
                }
                if (hasStorageServices()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("testContext(): Checking " + getContext().getStorage() + " storage credentials");
                    }
                    StorageServices services = getStorageServices();
                    if (services.hasBlobStoreSupport()) {
                        try {
                            services.getBlobStoreSupport().listFiles(null).iterator().hasNext();
                        } catch (Throwable t) {
                            logger.warn("testContext(): Storage credentials failed: " + t.getMessage());
                            if (logger.isTraceEnabled()) {
                                t.printStackTrace();
                            }
                            return null;
                        }
                    }
                }
                if (logger.isInfoEnabled()) {
                    logger.info("testContext(): Credentials validated");
                }
                return getContext().getAccountNumber();
            } catch (Throwable t) {
                logger.warn("testContext(): Failed to test cloudstack context: " + t.getMessage());
                if (logger.isTraceEnabled()) {
                    t.printStackTrace();
                }
                return null;
            }
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("exit - " + CloudstackProvider.class.getName() + ".testContext()");
            }
        }
    }

    public Document waitForJob(Document doc, String jobName) throws CloudException, InternalException {
        NodeList matches = doc.getElementsByTagName("jobid");
        if (matches.getLength() > 0) {
            return waitForJob(matches.item(0).getFirstChild().getNodeValue(), jobName);
        }
        return null;
    }

    public Document waitForJob(String jobId, String jobName) throws CloudException, InternalException {
        CloudstackMethod method = new CloudstackMethod(this);
        String url = method.buildUrl("queryAsyncJobResult", new Param("jobId", jobId));
        while (true) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            }
            Document doc = method.get(url);
            NodeList matches = doc.getElementsByTagName("jobstatus");
            int status = 0;
            if (matches.getLength() > 0) {
                status = Integer.parseInt(matches.item(0).getFirstChild().getNodeValue());
            }
            if (status > 0) {
                int code = status;
                if (status == 1) {
                    return doc;
                }
                if (status == 2) {
                    matches = doc.getElementsByTagName("jobresult");
                    if (matches.getLength() > 0) {
                        String str = matches.item(0).getFirstChild().getNodeValue();
                        if (str == null || str.trim().length() < 1) {
                            NodeList nodes = matches.item(0).getChildNodes();
                            String message = null;
                            for (int i = 0; i < nodes.getLength(); i++) {
                                Node n = nodes.item(i);
                                if (n.getNodeName().equalsIgnoreCase("errorcode")) {
                                    try {
                                        code = Integer.parseInt(n.getFirstChild().getNodeValue().trim());
                                    } catch (NumberFormatException ignore) {
                                    }
                                } else if (n.getNodeName().equalsIgnoreCase("errortext")) {
                                    message = n.getFirstChild().getNodeValue().trim();
                                }
                            }
                            CloudstackMethod.ParsedError error = new CloudstackMethod.ParsedError();
                            error.code = code;
                            error.message = message;
                            throw new CloudstackException(error);
                        } else {
                            throw new CloudException(str);
                        }
                    } else {
                        throw new CloudException(jobName + " failed with an unexplained error.");
                    }
                }
            }
        }
    }
}
