package org.proclos.etlcore.target;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.proclos.etlcore.component.ComponentFactory;
import org.proclos.etlcore.component.IManager;
import org.proclos.etlcore.component.IComponent;
import org.proclos.etlcore.component.Manager;
import org.proclos.etlcore.config.Config;

public class TargetManager extends Manager {

    private LinkedList<ITarget> targets = new LinkedList<ITarget>();

    private static final Log log = LogFactory.getLog(TargetManager.class);

    /**
	 * adds a dataTarget
	 * @param target
	 */
    public ITarget add(IComponent target) {
        if (target instanceof ITarget) {
            ITarget old = (ITarget) super.add(target);
            targets.remove(old);
            targets.add((ITarget) target);
            return old;
        } else {
            log.warn("Failed to add non target object");
        }
        return null;
    }

    /**
	 * gets the target by name
	 * @param name
	 * @return
	 */
    public ITarget get(String name) {
        return (ITarget) super.get(name);
    }

    public ITarget remove(String name) {
        ITarget target = (ITarget) super.remove(name);
        if (target != null) {
            targets.remove(target);
        }
        return target;
    }

    /**
	 * gets all managed targets 
	 */
    public ITarget[] getAll() {
        IComponent[] components = super.getAll();
        ITarget[] targets = new ITarget[components.length];
        for (int i = 0; i < components.length; i++) {
            targets[i] = (ITarget) components[i];
        }
        return targets;
    }

    public List<ITarget> getTarget() {
        LinkedList<ITarget> targets = new LinkedList<ITarget>();
        for (ITarget s : targets) {
            if (!(s instanceof ITarget)) targets.add((ITarget) s);
        }
        return targets;
    }

    public ITarget addTargetInternal(Element target, boolean fireNotification) {
        String type = target.getAttributeValue("type");
        ITarget ds = ComponentFactory.getInstance().createTarget(type, this, target);
        Element oldConfig = getComponentConfig(ds.getName());
        add(ds);
        if (fireNotification) fireConfigChanged(oldConfig, ds.getConfigurator().getXML());
        return ds;
    }

    public ITarget add(Element target) {
        return addTargetInternal(target, true);
    }

    public void process(Element targets) {
        setParameter(Config.getInstance().processParameters(targets.getChildren("parameter")));
        if (targets == null) return;
        log.info("Processing targets ...");
        List<?> ds = targets.getChildren("target");
        for (int i = 0; i < ds.size(); i++) {
            Element target = (Element) ds.get(i);
            addTargetInternal(target, false);
        }
    }

    public Element getXML() {
        Element targetsElement = new Element(IManager.Targets);
        targetsElement.addContent(Config.getInstance().getXML(getParameter()));
        for (ITarget s : targets) {
            if (!(s instanceof ITarget)) targetsElement.addContent(s.getConfigurator().getXML().detach());
        }
        return targetsElement;
    }

    public String getName() {
        return IManager.Targets;
    }
}
