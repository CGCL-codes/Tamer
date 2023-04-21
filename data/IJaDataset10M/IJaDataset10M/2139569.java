package com.solido.objectkitchen.vm;

import com.solido.objectkitchen.*;
import com.solido.objectkitchen.config.*;
import com.solido.objectkitchen.data.*;
import com.solido.objectkitchen.log.*;
import java.util.*;

public class VMHandler implements Runnable, ServiceProvider, VMProvider {

    private long timelimit;

    private int cyclelimit;

    private int vmlimit;

    private List vmlist;

    private List freevmlist;

    private boolean shouldberunning;

    private boolean isrunning;

    private DataProvider dataprovider;

    public void run() {
        isrunning = true;
        while (shouldberunning) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
            }
            for (int i = 0; i < vmlist.size(); i++) {
                VirtualMachine vm = (VirtualMachine) vmlist.get(i);
                if (vm.isRunning()) {
                    if (timelimit > -1) {
                        if ((System.currentTimeMillis() - vm.getStartTime()) / 1000 > timelimit) {
                            vm.kill();
                        }
                    }
                    if (cyclelimit > -1) {
                        if (vm.getCycles() > cyclelimit) {
                            vm.kill();
                        }
                    }
                }
            }
        }
        isrunning = false;
    }

    public boolean isRunning() {
        return isrunning;
    }

    public void start() {
        shouldberunning = true;
        new Thread(this).start();
    }

    public void stop() {
        shouldberunning = false;
        while (isrunning) {
            try {
                Thread.currentThread().sleep(250);
            } catch (Exception e) {
            }
        }
    }

    public boolean acceptConfiguration(ConfigurationSection config) {
        if (config.containsKey("CYCLELIMIT")) {
            try {
                cyclelimit = Integer.parseInt(config.getValue("CYCLELIMIT"));
            } catch (Exception e) {
                System.err.println(" ! cyclelimit must be an integer");
                return false;
            }
        } else {
            cyclelimit = -1;
        }
        if (config.containsKey("TIMELIMIT")) {
            try {
                timelimit = Long.parseLong(config.getValue("TIMELIMIT"));
            } catch (Exception e) {
                System.err.println(" ! timelimit must be an integer");
                return false;
            }
        } else {
            timelimit = -1;
        }
        if (config.containsKey("VMLIMIT")) {
            try {
                vmlimit = Integer.parseInt(config.getValue("VMLIMIT"));
            } catch (Exception e) {
                System.err.println(" ! vmlimit must be an integer");
                return false;
            }
        } else {
            vmlimit = 8;
        }
        vmlist = new ArrayList();
        for (int i = 0; i < vmlimit; i++) {
            VirtualMachine vm = new VirtualMachine();
            vmlist.add(vm);
            freevmlist.add(vm);
        }
        return true;
    }

    public void releaseVM(VirtualMachine vm) {
        synchronized (freevmlist) {
            freevmlist.add(vm);
        }
        freevmlist.notify();
    }

    public VirtualMachine grabVM() {
        VirtualMachine vm = null;
        while (vm == null) {
            try {
                synchronized (freevmlist) {
                    if (freevmlist.size() > 0) {
                        vm = (VirtualMachine) freevmlist.remove(0);
                    }
                }
                if (vm == null) {
                    freevmlist.wait();
                }
            } catch (Exception e) {
            }
        }
        return vm;
    }

    public void setDataProvider(DataProvider provider) {
        dataprovider = provider;
    }

    public ResultSet executePackage(ExecutionPackage epackage, String dbname, Hashtable environment) {
        ResultSet rs = null;
        VirtualMachine vm = grabVM();
        StorageEngine storage = dataprovider.getDatabase(dbname);
        try {
            if (storage != null) {
                rs = vm.executePackage(epackage, storage, environment);
            }
        } catch (Exception e) {
        }
        releaseVM(vm);
        return rs;
    }
}
