package org.bing.engine.controller.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bing.engine.controller.helper.TemplateHelper2;
import org.bing.engine.core.domain.BalancerInstance;
import org.bing.engine.core.domain.BalancerSetting;
import org.bing.engine.utility.helper.FileHelper;
import org.bing.engine.utility.helper.GuidGenerator;
import org.bing.engine.utility.helper.JdkHelper;
import org.bing.engine.utility.helper.StringHelper;

public class GeneralBalancerLifecycle extends AbstractControllerEnvirment {

    private Map<String, String> balancerMapping;

    public void setBalancerMapping(Map<String, String> mapping) {
        this.balancerMapping = mapping;
    }

    public String showCmdLine(BalancerSetting setting) {
        throw new RuntimeException("Unsupport method, impl by subclass!");
    }

    public BalancerInstance startup(BalancerSetting setting) {
        String globalId = guid();
        BalancerInstance result = new BalancerInstance(globalId);
        List<String> args = new ArrayList<String>();
        String balancerBase = getBalancerBase(setting);
        args.add(JdkHelper.findJavaBin());
        if ("true".equalsIgnoreCase(System.getProperty("engine.debug"))) {
            String debug = "-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=y";
            if (debug != null) {
                String[] dts = debug.split(" ");
                if (dts != null && dts.length > 0) {
                    for (int i = 0; i < dts.length; i++) {
                        if (!StringHelper.isBlank(dts[i])) {
                            args.add(dts[i]);
                        }
                    }
                }
            }
        }
        if (setting.getJvmargs() != null) {
            String[] jvms = setting.getJvmargs().split(" ");
            if (jvms != null && jvms.length > 0) {
                for (int i = 0; i < jvms.length; i++) {
                    if (!StringHelper.isBlank(jvms[i])) {
                        args.add(jvms[i]);
                    }
                }
            }
        }
        args.add("-D" + BalancerInstance.INST_GUID_PREFIX + "=" + result.getGlobalId());
        String confFile = balancerBase + "conf" + File.separator + "balancer.xml";
        String lastFile = TemplateHelper2.transform(setting.getSetting(), args);
        FileHelper.write(lastFile, confFile);
        args.add("-classpath");
        args.add(getBalancerClasspath(setting));
        String main = balancerMapping.get(setting.getBalancer().getName());
        args.add(main);
        args.add(confFile);
        logger.info("CmdLine:" + args);
        processManager.start(globalId, balancerBase, args.toArray(new String[args.size()]));
        if (globalId == null) {
            throw new RuntimeException("Cant start jetty processManager!");
        }
        result.setSetting(setting);
        return result;
    }

    public void shutdown(BalancerInstance inst) {
        String guid = inst.getGlobalId();
        if (processManager.exists(guid)) {
            processManager.destroy(inst.getGlobalId());
        } else {
        }
    }

    private String getBalancerClasspath(BalancerSetting setting) {
        String base = configurationManager.getEngineBase();
        File file = new File(base + "lib");
        StringBuilder sb = new StringBuilder(256);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    String name = f.getName();
                    if (name.startsWith("dynamic-common")) {
                        sb.append(f.getPath()).append(File.pathSeparator);
                    } else if (name.startsWith("dynamic-container-balancer")) {
                        sb.append(f.getPath()).append(File.pathSeparator);
                    } else if (name.startsWith("netty-")) {
                        sb.append(f.getPath()).append(File.pathSeparator);
                    } else if (name.startsWith("jetty-")) {
                        sb.append(f.getPath()).append(File.pathSeparator);
                    } else if (name.startsWith("servlet-api-")) {
                        sb.append(f.getPath()).append(File.pathSeparator);
                    }
                }
            }
        }
        if (sb.length() > 0) {
            sb.setCharAt(sb.length() - 1, ' ');
        }
        return sb.toString();
    }

    private String getBalancerBase(BalancerSetting setting) {
        String base = configurationManager.getWorkDirectory();
        base += setting.getBalancer().getName();
        String home = base + "-" + GuidGenerator.random(5) + File.separator;
        while (new File(home).exists()) {
            home = base + GuidGenerator.random(5) + File.separator;
        }
        return home;
    }
}
