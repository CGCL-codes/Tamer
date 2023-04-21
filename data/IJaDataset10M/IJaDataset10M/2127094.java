package org.jactr.tools.masterslave.master;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.chunktype.IChunkType;
import org.jactr.core.chunktype.ISymbolicChunkType;
import org.jactr.core.extensions.IExtension;
import org.jactr.core.model.IModel;
import org.jactr.core.model.basic.BasicModel;
import org.jactr.core.production.IProduction;
import org.jactr.core.queue.timedevents.TerminationTimedEvent;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.core.runtime.controller.IController;
import org.jactr.core.slot.BasicSlot;
import org.jactr.core.slot.IMutableSlot;
import org.jactr.core.slot.ISlot;
import org.jactr.core.slot.IUniqueSlotContainer;
import org.jactr.core.slot.UniqueSlotContainer;
import org.jactr.io.IOUtilities;
import org.jactr.tools.masterslave.slave.SlaveExtension;
import org.jactr.tools.masterslave.slave.SlaveStateCondition;

public class MasterExtension implements IExtension {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(MasterExtension.class);

    private IModel _model;

    private Map<String, IModel> _slaveModels = new TreeMap<String, IModel>();

    private Map<String, IUniqueSlotContainer> _slaveVariables = new TreeMap<String, IUniqueSlotContainer>();

    /**
   * finds the installed instanceof the master extension in the given model
   * 
   * @param model
   * @return
   */
    public static MasterExtension getMaster(IModel model) {
        return (MasterExtension) model.getExtension(MasterExtension.class);
    }

    public IModel getModel() {
        return _model;
    }

    public String getName() {
        return "master";
    }

    public void install(IModel model) {
        _model = model;
    }

    public void uninstall(IModel model) {
        _model = null;
    }

    public String getParameter(String key) {
        return null;
    }

    public Collection<String> getPossibleParameters() {
        return Collections.EMPTY_LIST;
    }

    public Collection<String> getSetableParameters() {
        return Collections.EMPTY_LIST;
    }

    public void setParameter(String key, String value) {
    }

    public void initialize() throws Exception {
    }

    public IModel getSlaveModel(String alias) {
        return _slaveModels.get(alias.toLowerCase());
    }

    public IUniqueSlotContainer getSlaveVariables(String alias) {
        IUniqueSlotContainer container = _slaveVariables.get(alias.toLowerCase());
        if (container == null) container = createSlaveVariables(alias);
        return container;
    }

    private IUniqueSlotContainer createSlaveVariables(String alias) {
        IUniqueSlotContainer container = new UniqueSlotContainer(true);
        container.addSlot(new BasicSlot(SlaveStateCondition.ALIAS_SLOT, alias));
        container.addSlot(new BasicSlot(SlaveStateCondition.IS_LOADED_SLOT, Boolean.FALSE));
        container.addSlot(new BasicSlot(SlaveStateCondition.IS_RUNNING_SLOT, Boolean.FALSE));
        container.addSlot(new BasicSlot(SlaveStateCondition.HAS_COMPLETED_SLOT, Boolean.FALSE));
        return container;
    }

    protected IModel loadModelAs(URL modelFile, String alias) throws Exception {
        Collection<Exception> warnings = new HashSet<Exception>();
        Collection<Exception> errors = new HashSet<Exception>();
        IModel model = null;
        CommonTree modelDescriptor = IOUtilities.loadModelFile(modelFile, warnings, errors);
        if (errors.size() != 0) throw errors.iterator().next();
        if (IOUtilities.compileModelDescriptor(modelDescriptor, warnings, errors)) model = IOUtilities.constructModel(modelDescriptor, warnings, errors);
        if (errors.size() != 0) throw errors.iterator().next();
        if (model instanceof BasicModel) ((BasicModel) model).setName(alias);
        _slaveModels.put(alias.toLowerCase(), model);
        SlaveExtension se = (SlaveExtension) model.getExtension(SlaveExtension.class);
        if (se == null) {
            if (LOGGER.isWarnEnabled()) LOGGER.warn(String.format("%s has no SlaveExtension installed, forcing installation", alias));
            se = new SlaveExtension();
            model.install(se);
        }
        se.setMaster(this);
        IUniqueSlotContainer container = createSlaveVariables(alias);
        ((IMutableSlot) container.getSlot(SlaveStateCondition.IS_LOADED_SLOT)).setValue(Boolean.TRUE);
        container.addSlot(new BasicSlot(SlaveStateCondition.MODEL_SLOT, model));
        _slaveVariables.put(alias, container);
        return model;
    }

    protected void startModel(IModel model) {
        ACTRRuntime runtime = ACTRRuntime.getRuntime();
        IController controller = runtime.getController();
        if (runtime.getModels().contains(model)) throw new IllegalStateException("Already running");
        IUniqueSlotContainer container = getSlaveVariables(model.getName());
        ((IMutableSlot) container.getSlot(SlaveStateCondition.IS_RUNNING_SLOT)).setValue(Boolean.TRUE);
        ((IMutableSlot) container.getSlot(SlaveStateCondition.HAS_COMPLETED_SLOT)).setValue(Boolean.FALSE);
        runtime.addModel(model);
        if (!controller.isRunning()) controller.start();
    }

    protected void stopModel(IModel model) {
        ACTRRuntime runtime = ACTRRuntime.getRuntime();
        IController controller = runtime.getController();
        if (controller.getRunningModels().contains(model)) {
            double now = runtime.getClock(model).getTime();
            model.getTimedEventQueue().enqueue(new TerminationTimedEvent(now, now));
        }
    }

    protected void cleanUp(IModel model) throws IllegalStateException {
        ACTRRuntime runtime = ACTRRuntime.getRuntime();
        Collection<IModel> terminated = runtime.getController().getTerminatedModels();
        _slaveModels.remove(model.getName().toLowerCase());
        _slaveVariables.remove(model.getName().toLowerCase());
        if (terminated.contains(model)) {
            runtime.removeModel(model);
            model.dispose();
        } else if (LOGGER.isWarnEnabled()) LOGGER.warn(String.format("%s is not in the terminated set %s, something strange has occured", model, terminated));
    }

    public void copyInto(IChunkType chunkType, IModel destination) {
    }

    public void copyInto(IChunk chunk, IModel destination) {
    }

    /**
   * @bug this code will not work w/ multiple inheritance
   * @param source
   * @param destination
   * @param sourceToCopy
   */
    protected void createChunkType(IChunkType source, IModel destination, Map sourceToCopy) {
        try {
            ISymbolicChunkType sct = source.getSymbolicChunkType();
            IChunkType copy = destination.getDeclarativeModule().getChunkType(sct.getName()).get();
            if (copy != null) return;
            if (sourceToCopy.get(source) != null) return;
            IChunkType parent = sct.getParent();
            if (parent != null) {
                createChunkType(parent, destination, sourceToCopy);
                parent = (IChunkType) sourceToCopy.get(parent);
            }
            copy = destination.getDeclarativeModule().createChunkType(parent, sct.getName()).get();
            sourceToCopy.put(source, copy);
            for (ISlot slot : sct.getSlots()) if (slot.getValue() instanceof IChunk) createChunk((IChunk) slot.getValue(), destination, sourceToCopy); else if (slot.getValue() instanceof IChunkType) createChunkType((IChunkType) slot.getValue(), destination, sourceToCopy); else if (slot.getValue() instanceof IProduction) LOGGER.warn("Copying of productions between models is not currently supported");
        } catch (Exception e) {
            LOGGER.error("unknown exception while getting chunktype ", e);
        }
    }

    protected void createChunk(IChunk source, IModel destination, Map sourceToCopy) {
    }
}
