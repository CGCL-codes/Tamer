package org.echarts.edt.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import org.echarts.edt.core.EChartsCore;
import org.echarts.edt.core.IEChartsProject;
import org.echarts.edt.core.IMachine;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

/**
 * @author Stephen Gevers
 * @version $Revision: 1.16 $
 */
public class MachineDependencyManager {

    /**
	 * Field EMPTY_MACHINES.
	 */
    private static final IMachine EMPTY_MACHINES[] = new IMachine[0];

    /**
	 * Field project.
	 */
    private IEChartsProject project = null;

    /**
	 * Field qName.
	 */
    private final QualifiedName qName = new QualifiedName(EChartsCore.PLUGIN_ID + ".machine", "dependencies");

    /**
	 * Field qProjectName.
	 */
    private QualifiedName qProjectName = null;

    /**
	 * Field dependencyMap.
	 */
    HashMap dependencyMap = new HashMap();

    /**
	 * Field reverseDependencyMap.
	 */
    HashMap reverseDependencyMap = new HashMap();

    /**
	 * Constructor for MachineDependencyManager.
	 * @param project IEChartsProject
	 */
    public MachineDependencyManager(IEChartsProject project) {
        super();
        this.project = project;
        qProjectName = new QualifiedName(EChartsCore.PLUGIN_ID + ".project." + project.getProject().getName(), "dependencies");
    }

    /**
	 * Method setMachineDependency.
	 * @param target IMachine
	 * @param dependencies IMachine[]
	 * @throws CoreException
	 */
    public void setMachineDependency(IMachine target, IMachine[] dependencies) throws CoreException {
        String targetKey = target.getResource().getFullPath().toString();
        IMachine currentDeps[] = getTargetDependencies(target);
        ArrayList current = new ArrayList();
        for (int i = 0; i < currentDeps.length; i++) {
            if (currentDeps[i] != null) current.add(currentDeps[i]);
        }
        ArrayList newdeps = new ArrayList();
        String depStr[] = new String[dependencies.length];
        for (int i = 0; i < dependencies.length; i++) {
            newdeps.add(dependencies[i]);
            depStr[i] = dependencies[i].getResource().getFullPath().toString();
        }
        ArrayList adds = new ArrayList();
        adds.addAll(newdeps);
        adds.removeAll(current);
        ArrayList removes = new ArrayList();
        removes.addAll(current);
        removes.removeAll(newdeps);
        for (int i = 0, count = adds.size(); i < count; i++) {
            IMachine source = (IMachine) adds.get(i);
            ArrayList deps = getReverseDependencies(source);
            deps.add(target);
            storeReverseDependencies(source.getResource(), deps);
        }
        for (int i = 0, count = removes.size(); i < count; i++) {
            IMachine source = (IMachine) removes.get(i);
            ArrayList deps = getReverseDependencies(source);
            deps.remove(target);
            storeReverseDependencies(source.getResource(), deps);
        }
        for (int i = 0, count = newdeps.size(); i < count; i++) {
            IMachine source = (IMachine) newdeps.get(i);
            storeReverseDependencies(source.getResource(), getReverseDependencies(source));
        }
        dependencyMap.put(targetKey, dependencies);
        storeTargetDependencies(target.getResource(), depStr);
    }

    /**
	 * Method getReverseDependencies.
	 * @param source IMachine
	 * @return ArrayList
	 * @throws CoreException
	 */
    private ArrayList getReverseDependencies(IMachine source) throws CoreException {
        String machineKey = source.getResource().getFullPath().toString();
        ArrayList ret = (ArrayList) reverseDependencyMap.get(machineKey);
        if (ret == null) {
            ret = new ArrayList();
            IResource resource = source.getResource();
            String deps = null;
            deps = resource.getPersistentProperty(qProjectName);
            if (deps != null) {
                String depPaths[] = deps.split(",");
                for (int i = 0; i < depPaths.length; i++) {
                    if (depPaths[i].length() > 1) ret.add(EChartsCore.createMachine(new Path(depPaths[i])));
                }
            }
            reverseDependencyMap.put(machineKey, ret);
        }
        return ret;
    }

    /**
	 * Method getDependentMachines.
	 * @param source IMachine
	 * @return IMachine[]
	 * @throws CoreException
	 */
    public IMachine[] getDependentMachines(IMachine source) throws CoreException {
        String machineKey = source.getResource().getFullPath().toString();
        ArrayList machine = (ArrayList) reverseDependencyMap.get(machineKey);
        if (machine == null) {
            machine = new ArrayList();
            IResource resource = source.getResource();
            String deps = null;
            deps = resource.getPersistentProperty(qProjectName);
            if (deps != null) {
                String depPaths[] = deps.split(",");
                for (int i = 0; i < depPaths.length; i++) {
                    if (depPaths[i].length() > 1) {
                        IMachine depMachine = EChartsCore.createMachine(new Path(depPaths[i]));
                        if (depMachine != null) machine.add(depMachine);
                    }
                }
            }
            reverseDependencyMap.put(machineKey, machine);
        }
        return (IMachine[]) machine.toArray(EMPTY_MACHINES);
    }

    /**
	 * Method storeTargetDependencies.
	 * @param resource IResource
	 * @param dependencies String[]
	 * @throws CoreException
	 */
    private void storeTargetDependencies(IResource resource, String[] dependencies) throws CoreException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < dependencies.length; i++) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append(dependencies[i]);
        }
        resource.setPersistentProperty(qName, buf.toString());
    }

    /**
	 * Method storeReverseDependencies.
	 * @param resource IResource
	 * @param dependencies ArrayList
	 * @throws CoreException
	 */
    private void storeReverseDependencies(IResource resource, ArrayList dependencies) throws CoreException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0, count = dependencies.size(); i < count; i++) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            IMachine dep = (IMachine) dependencies.get(i);
            if (dep != null) buf.append(dep.getResource().getFullPath().toString());
        }
        resource.setPersistentProperty(qProjectName, buf.toString());
        reverseDependencyMap.put(resource.getFullPath().toString(), dependencies);
    }

    /**
	 * Method getTargetDependencies.
	 * @param machine IMachine
	 * @return IMachine[]
	 * @throws CoreException
	 */
    private IMachine[] getTargetDependencies(IMachine machine) throws CoreException {
        IResource resource = machine.getResource();
        String key = resource.getFullPath().toString();
        IMachine ret[] = (IMachine[]) dependencyMap.get(key);
        if (ret == null) {
            String deps = resource.getPersistentProperty(qName);
            if ((deps == null) || (deps.length() == 0)) {
                ret = EMPTY_MACHINES;
            } else {
                String depPaths[] = deps.split(",");
                ret = new IMachine[depPaths.length];
                for (int i = 0; i < depPaths.length; i++) {
                    ret[i] = EChartsCore.createMachine(new Path(depPaths[i]));
                }
            }
            dependencyMap.put(key, ret);
        }
        return ret;
    }

    /**
	 * Method getProject.
	 * @return IEChartsProject
	 */
    public IEChartsProject getProject() {
        return project;
    }
}
