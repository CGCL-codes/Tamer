package org.dasein.cloud.jclouds.terremark.compute;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.Tag;
import org.dasein.cloud.compute.Architecture;
import org.dasein.cloud.compute.MachineImage;
import org.dasein.cloud.compute.Platform;
import org.dasein.cloud.compute.VirtualMachine;
import org.dasein.cloud.compute.VirtualMachineProduct;
import org.dasein.cloud.compute.VirtualMachineSupport;
import org.dasein.cloud.compute.VmState;
import org.dasein.cloud.compute.VmStatistics;
import org.dasein.cloud.dc.DataCenter;
import org.dasein.cloud.dc.Region;
import org.dasein.cloud.identity.ServiceAction;
import org.dasein.cloud.jclouds.terremark.Terremark;
import org.dasein.cloud.jclouds.terremark.Vdc;
import org.dasein.cloud.jclouds.terremark.network.PublicIp;
import org.jclouds.cim.ResourceAllocationSettingData;
import org.jclouds.cim.ResourceAllocationSettingData.ResourceType;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.RestContext;
import org.jclouds.trmk.vcloud_0_8.VCloudResponseException;
import org.jclouds.trmk.vcloud_0_8.domain.CatalogItem;
import org.jclouds.trmk.vcloud_0_8.domain.CustomizationParameters;
import org.jclouds.trmk.vcloud_0_8.domain.KeyPair;
import org.jclouds.trmk.vcloud_0_8.domain.Org;
import org.jclouds.trmk.vcloud_0_8.domain.ReferenceType;
import org.jclouds.trmk.vcloud_0_8.domain.Task;
import org.jclouds.trmk.vcloud_0_8.domain.TasksList;
import org.jclouds.trmk.vcloud_0_8.domain.TaskStatus;
import org.jclouds.trmk.vcloud_0_8.domain.VAppTemplate;
import org.jclouds.trmk.vcloud_0_8.domain.VDC;
import org.jclouds.trmk.vcloud_0_8.options.CloneVAppOptions;
import org.jclouds.trmk.vcloud_0_8.options.InstantiateVAppTemplateOptions;
import org.jclouds.trmk.vcloudexpress.TerremarkVCloudExpressAsyncClient;
import org.jclouds.trmk.vcloudexpress.TerremarkVCloudExpressClient;
import org.jclouds.trmk.vcloudexpress.TerremarkVCloudExpressMediaType;
import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import javax.annotation.Nonnull;

public class VApp implements VirtualMachineSupport {

    private static final Logger logger = Logger.getLogger(VApp.class);

    public static URI getVAppUri(RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx, String id) {
        if (id.startsWith("http")) {
            return URI.create(id);
        }
        return URI.create(ctx.getEndpoint() + "/v" + ctx.getApiVersion() + "/vapp/" + id);
    }

    private Terremark provider = null;

    public VApp(Terremark t) {
        provider = t;
    }

    @Override
    public void boot(String serverId) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            try {
                waitFor(ctx.getApi().powerOnVApp(getVAppUri(ctx, serverId)));
            } catch (RuntimeException e) {
                logger.error(e);
                e.printStackTrace();
                throw new CloudException(e);
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public VirtualMachine clone(String serverId, String intoDcId, String name, String description, boolean powerOn, String... firewallIds) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            try {
                CloneVAppOptions[] options = new CloneVAppOptions[powerOn ? 3 : 2];
                URI newServerId;
                Task task;
                options[0] = CloneVAppOptions.Builder.withDescription(description);
                options[1] = CloneVAppOptions.Builder.deploy();
                if (powerOn) {
                    options[2] = CloneVAppOptions.Builder.powerOn();
                }
                task = ctx.getApi().cloneVAppInVDC(Vdc.getVdcUri(ctx, intoDcId), getVAppUri(ctx, serverId), name, options);
                waitFor(task);
                newServerId = task.getOwner().getHref();
                return getVirtualMachine(newServerId.toASCIIString());
            } catch (RuntimeException e) {
                logger.error(e);
                e.printStackTrace();
                throw new CloudException(e);
            }
        } finally {
            ctx.close();
        }
    }

    private VirtualMachine define(String templateId, VirtualMachineProduct size, String dataCenterId, String name, String withKey, boolean withAnalytics, String... firewalls) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            String[] parts = size.getProductId().split(":");
            org.jclouds.trmk.vcloud_0_8.domain.VApp vapp;
            name = validateName(name, templateId);
            try {
                TerremarkVCloudExpressClient client = ctx.getApi();
                int memory = Integer.parseInt(parts[0]);
                int cpuCount = Integer.parseInt(parts[1]);
                String password = null;
                URI templateUri = Template.getTemplateUri(ctx, templateId);
                VAppTemplate template = client.getVAppTemplate(templateUri);
                CatalogItem item = client.findCatalogItemInOrgCatalogNamed(null, null, template.getName());
                CustomizationParameters customizationOptions = client.getCustomizationOptions(item.getCustomizationOptions().getHref());
                InstantiateVAppTemplateOptions opt;
                if (customizationOptions.canCustomizePassword()) {
                    password = getRandomPassword();
                    opt = InstantiateVAppTemplateOptions.Builder.memory(memory).processorCount(cpuCount).withPassword(password);
                } else if (withKey != null) {
                    Org org = client.findOrgNamed(null);
                    KeyPair key = client.findKeyPairInOrg(org.getHref(), withKey);
                    opt = InstantiateVAppTemplateOptions.Builder.memory(memory).processorCount(cpuCount).sshKeyFingerprint(key.getFingerPrint());
                } else {
                    opt = InstantiateVAppTemplateOptions.Builder.memory(memory).processorCount(cpuCount);
                    Matcher matcher = Template.LINUX_USER_PASSWORD_PATTERN.matcher(template.getDescription());
                    if (matcher.find()) {
                        password = matcher.group(2);
                    } else {
                        password = "UNKNOWN";
                    }
                }
                if (cpuCount < 1) {
                    cpuCount = 1;
                }
                vapp = client.instantiateVAppTemplateInVDC(Vdc.getVdcUri(ctx, dataCenterId), Template.getTemplateUri(ctx, templateId), name, opt);
                if (vapp == null) {
                    return null;
                }
                URI serverId = vapp.getHref();
                waitFor(client.deployVApp(serverId));
                VirtualMachine server = getVirtualMachine(Terremark.toId(serverId.toASCIIString()));
                while (server == null) {
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                    }
                    server = getVirtualMachine(Terremark.toId(serverId.toASCIIString()));
                }
                server.setRootPassword(password);
                server.setProviderMachineImageId(templateId);
                return server;
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                throw new CloudException(e);
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public String getConsoleOutput(String serverId) throws InternalException, CloudException {
        return "";
    }

    @Override
    public Collection<String> listFirewalls(String serverId) throws InternalException, CloudException {
        return Collections.emptyList();
    }

    private HashMap<String, String> imageMap = new HashMap<String, String>();

    private String guessImageId(String osDescription) throws CloudException, InternalException {
        String searchString;
        if (osDescription == null) {
            return null;
        }
        if (imageMap.containsKey(osDescription)) {
            return imageMap.get(osDescription);
        }
        if (osDescription.contains("RHEL") || osDescription.contains("Red Hat")) {
            searchString = "RHEL";
        } else if (osDescription.contains("CentOS")) {
            searchString = "CentOS";
        } else if (osDescription.contains("Ubuntu")) {
            searchString = "Ubuntu Server";
        } else if (osDescription.contains("Windows") && osDescription.contains("2003")) {
            if (osDescription.contains("Enterprise")) {
                searchString = "Windows 2003 Enterprise";
            } else {
                searchString = "Windows 2003 Standard";
            }
        } else if (osDescription.contains("Windows") && osDescription.contains("2008")) {
            if (osDescription.contains("Enterprise")) {
                searchString = "Windows Server 2008 Enterprise";
            } else if (osDescription.contains("Web")) {
                searchString = "Windows Web Server 2008";
            } else {
                searchString = "Windows Server 2008 Standard";
            }
        } else {
            searchString = osDescription;
        }
        boolean s32 = osDescription.contains("32-bit") || osDescription.contains("32 bit");
        for (MachineImage image : provider.getComputeServices().getImageSupport().listMachineImages()) {
            if (image.getDescription().startsWith(searchString)) {
                if (s32 == image.getArchitecture().equals(Architecture.I32)) {
                    imageMap.put(osDescription, image.getProviderMachineImageId());
                    return image.getProviderMachineImageId();
                }
            }
        }
        return null;
    }

    @Override
    public String getProviderTermForServer(Locale locale) {
        return "server";
    }

    private static final Random random = new Random();

    public static String alphabet = "ABCEFGHJKMNPRSUVWXYZabcdefghjkmnpqrstuvwxyz0123456789#@()=+/";

    public String getRandomPassword() {
        StringBuilder password = new StringBuilder();
        int rnd = random.nextInt();
        int length = 8;
        if (rnd < 0) {
            rnd = -rnd;
        }
        length = length + (rnd % 8);
        while (password.length() < length) {
            char c;
            rnd = random.nextInt();
            if (rnd < 0) {
                rnd = -rnd;
            }
            c = (char) (rnd % 255);
            if (alphabet.contains(String.valueOf(c))) {
                password.append(c);
            }
        }
        return password.toString();
    }

    @Override
    public VirtualMachine getVirtualMachine(String serverId) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            org.jclouds.trmk.vcloud_0_8.domain.VApp server;
            try {
                server = ctx.getApi().getVApp(getVAppUri(ctx, serverId));
            } catch (RuntimeException e) {
                String msg = e.getMessage();
                if (msg != null && msg.contains("NotFound")) {
                    server = null;
                } else {
                    logger.error("Error loading server " + serverId + " from cloud: " + e.getMessage());
                    e.printStackTrace();
                    throw new CloudException(e);
                }
            }
            if (server == null) {
                return null;
            }
            return toServer(server);
        } finally {
            ctx.close();
        }
    }

    @Override
    public VmStatistics getVMStatistics(String serverId, long start, long end) throws InternalException, CloudException {
        return new VmStatistics();
    }

    @Override
    public Collection<VmStatistics> getVMStatisticsForPeriod(String serverId, long start, long end) throws InternalException, CloudException {
        return Collections.emptyList();
    }

    private VirtualMachineProduct getProduct(org.jclouds.trmk.vcloud_0_8.domain.VApp server2) {
        Multimap<ResourceType, ResourceAllocationSettingData> resourceAllocation = Multimaps.index(server2.getResourceAllocations(), new Function<ResourceAllocationSettingData, ResourceType>() {

            @Override
            public ResourceType apply(ResourceAllocationSettingData arg0) {
                return arg0.getResourceType();
            }
        });
        ResourceAllocationSettingData memory, processor;
        String str;
        if (resourceAllocation == null) {
            return null;
        }
        Collection<ResourceAllocationSettingData> alloc = resourceAllocation.get(ResourceType.MEMORY);
        if (alloc != null && alloc.size() > 0) {
            memory = alloc.iterator().next();
        } else {
            memory = null;
        }
        alloc = resourceAllocation.get(ResourceType.PROCESSOR);
        if (alloc != null && alloc.size() > 0) {
            processor = alloc.iterator().next();
        } else {
            processor = null;
        }
        if (memory == null || processor == null) {
            return null;
        }
        str = memory.getVirtualQuantity() + ":" + processor.getVirtualQuantity();
        VirtualMachineProduct product = new VirtualMachineProduct();
        product.setProductId(str);
        product.setName(processor.getVirtualQuantity() + " CPU, " + memory.getVirtualQuantity() + "MB RAM ");
        product.setCpuCount(processor.getVirtualQuantity().intValue());
        product.setRamInMb(memory.getVirtualQuantity().intValue());
        product.setDiskSizeInGb(1);
        product.setDescription(product.getName());
        return product;
    }

    private static Iterable<VirtualMachineProduct> products = null;

    @Override
    public Iterable<VirtualMachineProduct> listProducts(Architecture architecture) throws InternalException, CloudException {
        if (products == null) {
            ArrayList<VirtualMachineProduct> sizes = new ArrayList<VirtualMachineProduct>();
            for (int ram : new int[] { 512, 1024, 1536, 2048, 4096, 8192, 12288, 16384 }) {
                for (int cpu : new int[] { 1, 2, 4, 8 }) {
                    VirtualMachineProduct product = new VirtualMachineProduct();
                    product.setProductId(ram + ":" + cpu);
                    product.setName(cpu + " CPU, " + ram + "M RAM");
                    product.setDescription(cpu + " CPU, " + ram + "M RAM");
                    product.setCpuCount(cpu);
                    product.setDiskSizeInGb(4);
                    product.setRamInMb(ram);
                    sizes.add(product);
                }
            }
            products = Collections.unmodifiableList(sizes);
        }
        return products;
    }

    @Override
    public VirtualMachine launch(String templateId, VirtualMachineProduct size, String dataCenterId, String serverName, String description, String withKey, String inVlanId, boolean withMonitoring, boolean forImaging, String... firewalls) throws InternalException, CloudException {
        return launch(templateId, size, dataCenterId, serverName, description, withKey, inVlanId, withMonitoring, forImaging, firewalls, new Tag[0]);
    }

    public VirtualMachine launch(String templateId, VirtualMachineProduct size, String dataCenterId, String serverName, String description, String withKey, String inVlanId, boolean withMonitoring, boolean forImaging, String[] firewalls, Tag... tags) throws InternalException, CloudException {
        VirtualMachine server = define(templateId, size, dataCenterId, serverName, withKey, withMonitoring, firewalls);
        boot(server.getProviderVirtualMachineId());
        return server;
    }

    @Override
    public Iterable<VirtualMachine> listVirtualMachines() throws InternalException, CloudException {
        ArrayList<VirtualMachine> servers = new ArrayList<VirtualMachine>();
        Vdc dsnVdc = (Vdc) provider.getDataCenterServices();
        for (Region region : dsnVdc.listRegions()) {
            for (DataCenter dc : dsnVdc.listDataCenters(region.getProviderRegionId())) {
                Collection<ReferenceType> resources;
                try {
                    VDC vdc = dsnVdc.getVdc(dc.getProviderDataCenterId());
                    if (vdc.getResourceEntities() != null) {
                        resources = vdc.getResourceEntities().values();
                    } else {
                        resources = Collections.emptyList();
                    }
                } catch (RuntimeException e) {
                    logger.error("Could not load servers: " + e.getMessage());
                    e.printStackTrace();
                    throw new CloudException(e);
                }
                for (ReferenceType resource : resources) {
                    if (resource.getType().equals(TerremarkVCloudExpressMediaType.VAPP_XML)) {
                        VirtualMachine server = getVirtualMachine(resource.getHref().toASCIIString());
                        if (server != null) {
                            servers.add(server);
                        }
                    }
                }
            }
        }
        return servers;
    }

    @Override
    public void enableAnalytics(String serverId) throws InternalException, CloudException {
    }

    @Override
    @Nonnull
    public String[] mapServiceAction(@Nonnull ServiceAction action) {
        return new String[0];
    }

    @Override
    public void pause(String serverId) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            try {
                ctx.getApi().powerOffVApp(getVAppUri(ctx, serverId));
            } catch (RuntimeException e) {
                logger.error(e);
                e.printStackTrace();
                throw new CloudException(e);
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public void reboot(String serverId) throws CloudException, InternalException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            ctx.getApi().resetVApp(getVAppUri(ctx, serverId));
        } finally {
            ctx.close();
        }
    }

    @Override
    public void terminate(String serverId) throws InternalException, CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            TerremarkVCloudExpressClient client = ctx.getApi();
            VirtualMachine vm = getVirtualMachine(serverId);
            if (!vm.getCurrentState().equals(VmState.PAUSED)) {
                waitFor(client.powerOffVApp(getVAppUri(ctx, serverId)));
            }
            int timeout = 0;
            vm = getVirtualMachine(serverId);
            while (!vm.getCurrentState().equals(VmState.PAUSED) && timeout < 10) {
                timeout++;
                try {
                    Thread.sleep(25000L);
                } catch (InterruptedException e) {
                }
                vm.setCurrentState(getVirtualMachine(serverId).getCurrentState());
            }
            if (!getVirtualMachine(serverId).getCurrentState().equals(VmState.PAUSED)) {
                logger.warn("Virtual machine won't pause.");
            }
            try {
                TasksList tasks = client.findTasksListInOrgNamed(null, null);
                for (Task task : tasks.getTasks()) {
                    TaskStatus status = task.getStatus();
                    URI TastUri = task.getHref();
                    while (status.equals(TaskStatus.QUEUED) || status.equals(TaskStatus.RUNNING)) {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                        }
                        status = client.getTask(TastUri).getStatus();
                    }
                }
                ((PublicIp) provider.getNetworkServices().getIpAddressSupport()).clear(serverId);
                client.deleteVApp(getVAppUri(ctx, serverId));
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                throw new CloudException(e);
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public void disableAnalytics(String serverId) throws InternalException, CloudException {
    }

    private VirtualMachine toServer(org.jclouds.trmk.vcloud_0_8.domain.VApp server2) throws CloudException, InternalException {
        VirtualMachine server = new VirtualMachine();
        VmState currentState;
        server.setProviderVirtualMachineId(Terremark.toId(server2.getHref().toASCIIString()));
        switch(server2.getStatus()) {
            case ON:
                currentState = VmState.RUNNING;
                break;
            case OFF:
                currentState = VmState.PAUSED;
                break;
            case SUSPENDED:
                currentState = VmState.PAUSED;
                break;
            default:
                currentState = VmState.PENDING;
                break;
        }
        server.setCurrentState(currentState);
        server.setName(server2.getName());
        String str = server2.getOperatingSystemDescription();
        if (str == null || str.indexOf("32-bit") != -1 || str.indexOf("32 bit") != -1) {
            server.setArchitecture(Architecture.I32);
        } else if (str.indexOf("64-bit") != -1 || str.indexOf("64 bit") != -1) {
            server.setArchitecture(Architecture.I64);
        } else {
            server.setArchitecture(Architecture.I64);
        }
        server.setCreationTimestamp(0L);
        server.setDescription(server2.getName() + " - " + server2.getOperatingSystemDescription());
        server.setPersistent(true);
        server.setPlatform(Platform.guess(server2.getOperatingSystemDescription()));
        VirtualMachineProduct size = getProduct(server2);
        if (size == null) {
            return null;
        } else {
            server.setProduct(size);
        }
        String name = server2.getName();
        int idx = name.lastIndexOf("-");
        String templateId = null;
        if (idx != -1 && idx < (name.length() - 1)) {
            String tid = name.substring(idx + 1);
            try {
                Integer.parseInt(tid);
                templateId = tid;
            } catch (NumberFormatException ignore) {
            }
        }
        if (templateId != null) {
            server.setProviderMachineImageId(Terremark.toId(templateId));
        } else {
            server.setProviderMachineImageId(guessImageId(server2.getOperatingSystemDescription()));
        }
        Collection<String> addresses;
        try {
            addresses = server2.getNetworkToAddresses().values();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new CloudException(e);
        }
        if (addresses == null || addresses.size() < 1) {
            server.setPrivateIpAddresses(new String[0]);
        } else {
            String[] ips = new String[addresses.size()];
            int i = 0;
            for (String addr : addresses) {
                if (i == 0) {
                    server.setPrivateDnsAddress(addr);
                }
                ips[i++] = addr;
            }
            server.setPrivateIpAddresses(ips);
        }
        server.setPublicDnsAddress(null);
        server.setPublicIpAddresses(new String[0]);
        server.setProviderRegionId("us-miami");
        server.setProviderDataCenterId(Terremark.toId(server2.getVDC().getHref().toASCIIString()));
        server.setProviderAssignedIpAddressId(null);
        if (server.getProviderMachineImageId() != null) {
            server.setRootPassword(((Template) provider.getComputeServices().getImageSupport()).getPassword(server.getProviderMachineImageId()));
        }
        server.setLastPauseTimestamp(-1L);
        server.setTerminationTimestamp(-1L);
        server.setLastBootTimestamp(0L);
        server.setProviderOwnerId(provider.getContext().getAccountNumber());
        return server;
    }

    private String validateName(String name, String suffix) {
        int len = (suffix == null ? 0 : (1 + suffix.length()));
        name = name.toLowerCase().replaceAll("_", "-");
        if (name.length() <= (15 - len)) {
            return (suffix == null ? name : name + "-" + suffix);
        }
        return name.substring(0, 15 - (len + 1)) + (suffix == null ? "" : "-" + suffix);
    }

    private void waitFor(Task task) throws CloudException {
        RestContext<TerremarkVCloudExpressClient, TerremarkVCloudExpressAsyncClient> ctx = provider.getCloudClient();
        try {
            TaskStatus status = task.getStatus();
            while (status.equals(TaskStatus.RUNNING) || status.equals(TaskStatus.QUEUED)) {
                if (status.equals(TaskStatus.ERROR) || status.equals(TaskStatus.UNRECOGNIZED)) {
                    throw new CloudException(task.getOwner().getName());
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                task = ctx.getApi().getTask(task.getHref());
                status = task.getStatus();
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public VirtualMachineProduct getProduct(String productId) throws InternalException, CloudException {
        for (VirtualMachineProduct product : listProducts(Architecture.I64)) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        try {
            String[] parts = productId.split(":");
            int ram = Integer.parseInt(parts[0]);
            int cpu = Integer.parseInt(parts[1]);
            VirtualMachineProduct product = new VirtualMachineProduct();
            product.setCpuCount(cpu);
            product.setRamInMb(ram);
            product.setDiskSizeInGb(4);
            product.setProductId(productId);
            product.setName(productId);
            product.setDescription(productId);
            return product;
        } catch (Throwable ignore) {
            return null;
        }
    }

    @Override
    public boolean isSubscribed() throws CloudException, InternalException {
        try {
            listVirtualMachines();
            return true;
        } catch (AuthorizationException e) {
            return false;
        } catch (VCloudResponseException e) {
            logger.warn("Could not determine subscription status for VMs in Terremark for " + provider.getContext().getAccountNumber() + ": " + e.getMessage());
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw new CloudException(e);
        } catch (RuntimeException e) {
            logger.warn("Could not determine subscription status for VMs in Terremark for " + provider.getContext().getAccountNumber() + ": " + e.getMessage());
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw new InternalException(e);
        }
    }

    @Override
    public boolean supportsAnalytics() throws CloudException, InternalException {
        return false;
    }
}
