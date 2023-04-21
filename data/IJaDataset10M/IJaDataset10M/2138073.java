package org.jspar.model;

import java.util.ArrayList;
import java.util.List;

public class NetList {

    private List<Module> modules = new ArrayList<Module>();

    private List<Net> nets = new ArrayList<Net>();

    private List<Terminal> extTerms = new ArrayList<Terminal>();

    private List<Module> extMods = new ArrayList<Module>();

    public Module[] modules() {
        return modules.toArray(new Module[modules.size()]);
    }

    public Net[] nets() {
        return nets.toArray(new Net[nets.size()]);
    }

    public void removeNet(Net net) {
        nets.remove(net);
    }

    public Module[] extMods() {
        return extMods.toArray(new Module[extMods.size()]);
    }

    public Net get_net(String name) {
        for (Net n : nets) {
            if (n.name().equals(name)) return n;
        }
        return null;
    }

    public void addExternal(Terminal t, Module tmod) {
        this.extTerms.add(t);
        this.extMods.add(tmod);
    }

    public void addModule(Module module) {
        this.modules.add(module);
    }

    public void addNet(Net net) {
        nets.add(net);
    }

    public List<Terminal> extTerms() {
        return extTerms;
    }

    public Module getModule(String moduleName) {
        for (Module m : modules) {
            if (m.name().equals(moduleName)) return m;
        }
        for (Module m : extMods) {
            if (m.name().equals(moduleName)) return m;
        }
        return null;
    }
}
