package jpcsp.HLE.modules271;

import jpcsp.HLE.HLEFunction;
import java.util.HashMap;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;
import jpcsp.HLE.kernel.Managers;
import jpcsp.HLE.kernel.types.SceKernelErrors;
import jpcsp.HLE.kernel.types.SceModule;
import jpcsp.HLE.modules.HLEModuleManager;

public class sceUtility extends jpcsp.HLE.modules200.sceUtility {

    public static final String[] utilityAvModuleNames = new String[] { "PSP_AV_MODULE_AVCODEC", "PSP_AV_MODULE_SASCORE", "PSP_AV_MODULE_ATRAC3PLUS", "PSP_AV_MODULE_MPEGBASE", "PSP_AV_MODULE_MP3", "PSP_AV_MODULE_VAUDIO", "PSP_AV_MODULE_AAC", "PSP_AV_MODULE_G729" };

    public static final String[] utilityUsbModuleNames = new String[] { "PSP_USB_MODULE_UNKNOWN_0", "PSP_USB_MODULE_PSPCM", "PSP_USB_MODULE_ACC", "PSP_USB_MODULE_MIC", "PSP_USB_MODULE_CAM", "PSP_USB_MODULE_GPS" };

    public static final int PSP_AV_MODULE_AVCODEC = 0;

    public static final int PSP_AV_MODULE_SASCORE = 1;

    public static final int PSP_AV_MODULE_ATRAC3PLUS = 2;

    public static final int PSP_AV_MODULE_MPEGBASE = 3;

    public static final int PSP_AV_MODULE_MP3 = 4;

    public static final int PSP_AV_MODULE_VAUDIO = 5;

    public static final int PSP_AV_MODULE_AAC = 6;

    public static final int PSP_AV_MODULE_G729 = 7;

    public static final int PSP_USB_MODULE_PSPCM = 1;

    public static final int PSP_USB_MODULE_ACC = 2;

    public static final int PSP_USB_MODULE_MIC = 3;

    public static final int PSP_USB_MODULE_CAM = 4;

    public static final int PSP_USB_MODULE_GPS = 5;

    protected HashMap<Integer, SceModule> loadedAvModules = new HashMap<Integer, SceModule>();

    protected HashMap<Integer, String> waitingAvModules = new HashMap<Integer, String>();

    protected HashMap<Integer, SceModule> loadedUsbModules = new HashMap<Integer, SceModule>();

    protected HashMap<Integer, String> waitingUsbModules = new HashMap<Integer, String>();

    private String getAvModuleName(int module) {
        if (module < 0 || module >= utilityAvModuleNames.length) {
            return "PSP_AV_MODULE_UNKNOWN_" + module;
        }
        return utilityAvModuleNames[module];
    }

    private String getUsbModuleName(int module) {
        if (module < 0 || module >= utilityUsbModuleNames.length) {
            return "PSP_USB_MODULE_UNKNOWN_" + module;
        }
        return utilityUsbModuleNames[module];
    }

    protected int hleUtilityLoadAvModule(int module, String moduleName) {
        HLEModuleManager moduleManager = HLEModuleManager.getInstance();
        if (loadedAvModules.containsKey(module) || waitingAvModules.containsKey(module)) {
            return SceKernelErrors.ERROR_AV_MODULE_ALREADY_LOADED;
        } else if (!moduleManager.hasFlash0Module(moduleName)) {
            waitingAvModules.put(module, moduleName);
            return SceKernelErrors.ERROR_AV_MODULE_BAD_ID;
        } else {
            int sceModuleId = moduleManager.LoadFlash0Module(moduleName);
            SceModule sceModule = Managers.modules.getModuleByUID(sceModuleId);
            loadedAvModules.put(module, sceModule);
            return 0;
        }
    }

    protected int hleUtilityLoadUsbModule(int module, String moduleName) {
        HLEModuleManager moduleManager = HLEModuleManager.getInstance();
        if (loadedUsbModules.containsKey(module) || waitingUsbModules.containsKey(module)) {
            return SceKernelErrors.ERROR_AV_MODULE_ALREADY_LOADED;
        } else if (!moduleManager.hasFlash0Module(moduleName)) {
            waitingUsbModules.put(module, moduleName);
            return SceKernelErrors.ERROR_AV_MODULE_BAD_ID;
        } else {
            int sceModuleId = moduleManager.LoadFlash0Module(moduleName);
            SceModule sceModule = Managers.modules.getModuleByUID(sceModuleId);
            loadedUsbModules.put(module, sceModule);
            return 0;
        }
    }

    protected int hleUtilityUnloadAvModule(int module) {
        if (loadedAvModules.containsKey(module)) {
            HLEModuleManager moduleManager = HLEModuleManager.getInstance();
            SceModule sceModule = loadedAvModules.remove(module);
            moduleManager.UnloadFlash0Module(sceModule);
            return 0;
        } else if (waitingAvModules.containsKey(module)) {
            waitingAvModules.remove(module);
            return 0;
        } else {
            return SceKernelErrors.ERROR_AV_MODULE_NOT_LOADED;
        }
    }

    @HLEFunction(nid = 0xC629AF26, version = 270, checkInsideInterrupt = true)
    public void sceUtilityLoadAvModule(Processor processor) {
        CpuState cpu = processor.cpu;
        int module = cpu.gpr[4];
        String moduleName = getAvModuleName(module);
        int result = hleUtilityLoadAvModule(module, moduleName);
        if (result == SceKernelErrors.ERROR_AV_MODULE_BAD_ID) {
            log.info(String.format("IGNORING: sceUtilityLoadAvModule(module=0x%04X) %s", module, moduleName));
            result = 0;
        } else {
            log.info(String.format("sceUtilityLoadAvModule(module=0x%04X) %s loaded", module, moduleName));
        }
        cpu.gpr[2] = result;
    }

    @HLEFunction(nid = 0xF7D8D092, version = 270, checkInsideInterrupt = true)
    public void sceUtilityUnloadAvModule(Processor processor) {
        CpuState cpu = processor.cpu;
        int module = cpu.gpr[4];
        String moduleName = getAvModuleName(module);
        log.info(String.format("sceUtilityUnloadAvModule(module=0x%04X) %s unloaded", module, moduleName));
        cpu.gpr[2] = hleUtilityUnloadAvModule(module);
    }

    @HLEFunction(nid = 0x4928BD96, version = 270, checkInsideInterrupt = true)
    public void sceUtilityMsgDialogAbort(Processor processor) {
        CpuState cpu = processor.cpu;
        log.warn("PARTIAL: sceUtilityMsgDialogAbort()");
        msgDialogState.abort();
        cpu.gpr[2] = 0;
    }

    @HLEFunction(nid = 0x0D5BC6D2, version = 270, checkInsideInterrupt = true)
    public void sceUtilityLoadUsbModule(Processor processor) {
        CpuState cpu = processor.cpu;
        int module = cpu.gpr[4];
        String moduleName = getUsbModuleName(module);
        int result = hleUtilityLoadUsbModule(module, moduleName);
        if (result == SceKernelErrors.ERROR_AV_MODULE_BAD_ID) {
            log.info(String.format("IGNORING: sceUtilityLoadUsbModule(module=0x%04X) %s", module, moduleName));
            result = 0;
        } else {
            log.info(String.format("sceUtilityLoadUsbModule(module=0x%04X) %s loaded", module, moduleName));
        }
        cpu.gpr[2] = result;
    }
}
