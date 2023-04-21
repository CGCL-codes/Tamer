package org.ourgrid.worker.controller.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.worker.WorkerConstants;
import org.ourgrid.worker.sysmonitor.core.SysInfoGatheringCore;
import org.ourgrid.worker.sysmonitor.interfaces.WorkerSysInfoCollector;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

public class GatherSystemInformationsAction implements RepeatedAction {

    private SysInfoGatheringCore gatheringCore;

    private Map<String, String> systemInformations = new HashMap<String, String>();

    private int totalCores;

    public GatherSystemInformationsAction() {
        SysInfoGatheringCore.loadLibraries();
        this.gatheringCore = new SysInfoGatheringCore();
        this.systemInformations = new HashMap<String, String>();
        this.getStaticInformations();
    }

    public void run(Serializable handler, ServiceManager serviceManager) {
        ObjectDeployment objectDeployment = serviceManager.getObjectDeployment(WorkerConstants.WORKER_SYSINFO_COLLECTOR);
        if (objectDeployment == null) return;
        WorkerSysInfoCollector controller = (WorkerSysInfoCollector) objectDeployment.getProxy();
        gatheringCore.gather();
        systemInformations.put(WorkerSpec.CPU_IDLE_TIME, format(gatheringCore.getCpuIdle()));
        systemInformations.put(WorkerSpec.CPU_USER_TIME, format(gatheringCore.getCpuUser()));
        systemInformations.put(WorkerSpec.CPU_SYS_TIME, format(gatheringCore.getCpuSys()));
        systemInformations.put(WorkerSpec.CPU_NICE_TIME, format(gatheringCore.getCpuNice()));
        systemInformations.put(WorkerSpec.CPU_WAIT_TIME, format(gatheringCore.getCpuWait()));
        systemInformations.put(WorkerSpec.CPU_USED_TOTAL_TIME, format(gatheringCore.getCpuCombined()));
        systemInformations.put(WorkerSpec.CPU_LOAD_AVERAGE, format(gatheringCore.getLoadAvarage()));
        systemInformations.put(WorkerSpec.DISK_AVAIL, format(gatheringCore.getDiskAvail()));
        systemInformations.put(WorkerSpec.MEM_FREE, format(gatheringCore.getMemFree()));
        systemInformations.put(WorkerSpec.MEM_FREE_PERCENT, format(gatheringCore.getMemFreePercent()));
        systemInformations.put(WorkerSpec.CPU_PERC_SYS_ONLY, format(gatheringCore.getCpuPercSystemOnly()));
        controller.metricsChanged(systemInformations);
    }

    private String format(double[] list) {
        if (list == null) return WorkerSpec.UNKNOWN_VALUE;
        List<Double> out = new ArrayList<Double>(list.length);
        for (double d : list) {
            out.add(d);
        }
        return out.toString();
    }

    private String format(int integer) {
        return (integer == -1) ? WorkerSpec.UNKNOWN_VALUE : String.valueOf(integer);
    }

    private String format(double d) {
        return (d < 0.0) ? WorkerSpec.UNKNOWN_VALUE : String.valueOf(d);
    }

    private String format(long integer) {
        return (integer == -1) ? WorkerSpec.UNKNOWN_VALUE : String.valueOf(integer);
    }

    private void getStaticInformations() {
        this.totalCores = gatheringCore.getCpuTotalCores();
        this.systemInformations.put(WorkerSpec.CPU_CORES, format(totalCores));
        this.systemInformations.put(WorkerSpec.CPU_MHZ, format(gatheringCore.getCpuMhz()));
        this.systemInformations.put(WorkerSpec.CPU_MODEL, gatheringCore.getCpuModel());
        this.systemInformations.put(WorkerSpec.CPU_VENDOR, gatheringCore.getCpuVendor());
        this.systemInformations.put(WorkerSpec.FILE_SYSTEM_DIR_NAME, gatheringCore.getInstallationPartitionName());
        this.systemInformations.put(WorkerSpec.FILE_SYSTEM_TYPE, gatheringCore.getFileSystemType());
        this.systemInformations.put(WorkerSpec.DISK_TOTAL, format(gatheringCore.getDiskTotal()));
        this.systemInformations.put(WorkerSpec.MEM_TOTAL, format(gatheringCore.getMemTotal()));
        this.systemInformations.put(WorkerSpec.OS_NAME, gatheringCore.getOSName());
        this.systemInformations.put(WorkerSpec.OS_DESCRIPTION, gatheringCore.getOSDescription());
        this.systemInformations.put(WorkerSpec.OS_VENDOR, gatheringCore.getOSVendorName());
        this.systemInformations.put(WorkerSpec.SYS_ARCHITECTURE, gatheringCore.getArchitecture());
        this.systemInformations.put(WorkerSpec.OS_WORD_LENGTH, gatheringCore.getDataModel());
        this.systemInformations.put(WorkerSpec.OS_UP_TIME, format(gatheringCore.getUpTime()));
    }
}
