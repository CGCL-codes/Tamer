package net.sf.openforge.schedule;

import java.util.*;
import net.sf.openforge.app.EngineThread;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.io.*;
import net.sf.openforge.lim.memory.*;
import net.sf.openforge.util.naming.*;

/**
 * MemoryConnectionVisitor is responsible for traversing the LIM and
 * establishing all the sideband connections necessary for attaching
 * each {@link MemoryAccess} to the backing structural memory.  This
 * involves generating a {@link MemoryGateway} at each module level,
 * and a {@link MemoryReferee} at the top level to interface with the
 * targetted memory port.
 *
 * <p>Created: Tue Mar 11 11:32:43 2003
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: MemoryConnectionVisitor.java 282 2006-08-14 21:25:33Z imiller $
 */
public class MemoryConnectionVisitor extends DefaultVisitor {

    private static final String _RCS_ = "$Rev: 282 $";

    private Stack accessFrames = new Stack();

    private Frame currentFrame = new Frame();

    private Map designResourceBundles = new HashMap();

    public MemoryConnectionVisitor() {
    }

    /**
     * Visits the design, generating MemoryGateways at each module,
     * then connecting up the top level accesses to a memory referee.
     */
    public void visit(Design design) {
        super.visit(design);
        for (Iterator memories = design.getLogicalMemories().iterator(); memories.hasNext(); ) {
            LogicalMemory memory = (LogicalMemory) memories.next();
            StructuralMemory structMem = memory.getStructuralMemory();
            for (Iterator memports = memory.getLogicalMemoryPorts().iterator(); memports.hasNext(); ) {
                LogicalMemoryPort memport = (LogicalMemoryPort) memports.next();
                connectMemoryPort(memport, structMem, design);
            }
            structMem.getClockPort().setUsed(true);
            structMem.getResetPort().setUsed(true);
            structMem.setConsumesReset(true);
        }
    }

    private void connectMemoryPort(LogicalMemoryPort memport, StructuralMemory structMem, Design design) {
        StructuralMemory.StructuralMemoryPort structPort = structMem.getStructuralMemoryPort(memport);
        ResourceBundle bundle = (ResourceBundle) designResourceBundles.get(memport);
        if (bundle == null) return;
        if (bundle.getReads().isEmpty() && bundle.getWrites().isEmpty()) return;
        assert bundle.getReads().size() == bundle.getWrites().size();
        MemoryReferee referee = memport.makePhysicalComponent(bundle.getReads(), bundle.getWrites());
        design.addComponentToDesign(referee);
        design.addComponentToDesign(structMem);
        referee.connectImplementation(structPort);
        for (int i = 0; i < referee.getTaskSlots().size(); i++) {
            MemoryReferee.TaskSlot slot = (MemoryReferee.TaskSlot) referee.getTaskSlots().get(i);
            if (bundle.getReads().get(i) != null) {
                ((MemAccess) bundle.getReads().get(i)).connect(slot);
            }
            if (bundle.getWrites().get(i) != null) {
                ((MemAccess) bundle.getWrites().get(i)).connect(slot);
            }
        }
    }

    /**
     * Traverses the task and then stores the top level accesses
     * (if any) into the resource bundle for each targetted resource.
     */
    public void visit(Task task) {
        final Frame superFrame = this.currentFrame;
        this.currentFrame = new Frame();
        super.visit(task);
        for (Iterator iter = currentFrame.getResources().iterator(); iter.hasNext(); ) {
            Resource res = (Resource) iter.next();
            ResourceBundle bundle = currentFrame.getBundle(res);
            assert bundle.getReads().size() < 2;
            assert bundle.getWrites().size() < 2;
            if (bundle.getReads().isEmpty() && bundle.getWrites().isEmpty()) {
                continue;
            }
            ResourceBundle designBundle = (ResourceBundle) designResourceBundles.get(res);
            if (designBundle == null) {
                designBundle = new ResourceBundle(res);
                designResourceBundles.put(res, designBundle);
            }
            if (bundle.getReads().size() > 0) designBundle.addRead((MemAccess) bundle.getReads().get(0)); else designBundle.addRead(null);
            if (bundle.getWrites().size() > 0) designBundle.addWrite((MemAccess) bundle.getWrites().get(0)); else designBundle.addWrite(null);
        }
        this.currentFrame = superFrame;
    }

    /**
     * Traverse the call, making special effort to ensure that the
     * accesses that percolated up to the procedure, get duplicated on
     * the call.
     */
    public void visit(Call call) {
        Frame superFrame = currentFrame;
        currentFrame = new Frame();
        super.visit(call);
        for (Iterator iter = currentFrame.getAllAccesses().iterator(); iter.hasNext(); ) {
            MemAccess acc = (MemAccess) iter.next();
            MemAccess pushed = acc.pushAcrossCall(call);
            superFrame.addAccess(pushed);
        }
        currentFrame = superFrame;
        for (Iterator iter = call.getBuses().iterator(); iter.hasNext(); ) {
            Bus callBus = (Bus) iter.next();
            Bus procBus = call.getProcedureBus(callBus);
        }
    }

    /**
     * Record the memory access.
     */
    public void visit(MemoryRead access) {
        super.visit(access);
        access.getOwner().addComponent(access.makePhysicalComponent());
        currentFrame.addAccess(new MemReadAccess(access));
    }

    /**
     * Record the memory access.
     */
    public void visit(MemoryWrite access) {
        super.visit(access);
        access.getOwner().addComponent(access.makePhysicalComponent());
        currentFrame.addAccess(new MemWriteAccess(access));
    }

    public void visit(AbsoluteMemoryRead module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(AbsoluteMemoryWrite module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(ArrayRead module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(ArrayWrite module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Block module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Branch module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Decision module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(SimplePinAccess module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(FifoAccess module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(FifoRead module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(FifoWrite module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(ForBody module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(HeapRead module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(HeapWrite module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Kicker module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Latch module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(MemoryGateway module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(MemoryReferee module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(PinReferee module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(PriorityMux module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(RegisterGateway module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(RegisterReferee module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Scoreboard module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Switch module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(UntilBody module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(WhileBody module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    public void visit(Loop module) {
        enterModule();
        super.visit(module);
        exitModule(module);
    }

    private void enterModule() {
        accessFrames.push(currentFrame);
        currentFrame = new Frame();
    }

    /**
     * Pop the frame, create a gateway for each resource accessed
     * in the traversed module, and create accesses in the current
     * frame for the global side of each gateway from the subframe.
     */
    private void exitModule(Module module) {
        Frame frame = currentFrame;
        currentFrame = (Frame) accessFrames.pop();
        for (Iterator iter = frame.getResources().iterator(); iter.hasNext(); ) {
            final LogicalMemoryPort memoryPort = (LogicalMemoryPort) iter.next();
            ResourceBundle bundle = frame.getBundle(memoryPort);
            List reads = bundle.getReads();
            List writes = bundle.getWrites();
            MemoryGateway gateway = new MemoryGateway(memoryPort, reads.size(), writes.size(), memoryPort.getMaxAddressWidth());
            module.addComponent(gateway);
            for (int i = 0; i < reads.size(); i++) {
                ((MemAccess) reads.get(i)).connect(gateway, i);
            }
            for (int i = 0; i < writes.size(); i++) {
                ((MemAccess) writes.get(i)).connect(gateway, i);
            }
            Map duplicate = new HashMap();
            if (reads.size() > 0) {
                currentFrame.addAccess(new MemReadAccess(gateway, module, duplicate));
            }
            if (writes.size() > 0) {
                currentFrame.addAccess(new MemWriteAccess(gateway, module, duplicate));
            }
        }
    }

    /**
     * A Frame is a scoping unit for tracking accesses.  It is used to
     * track all accesses to all resources in a given scope.  The
     * accesses are stored in a map of {@link Resource} to
     * ResourceBundle.
     */
    private static class Frame {

        /** A map of Resource (memory port) to ResourceBundle. */
        private Map resourceToBundle = new HashMap();

        /**
         * Adds a MemAccess to this frame.
         */
        public void addAccess(MemAccess acc) {
            ResourceBundle bundle = (ResourceBundle) resourceToBundle.get(acc.getTarget());
            if (bundle == null) {
                bundle = new ResourceBundle(acc.getTarget());
                resourceToBundle.put(acc.getTarget(), bundle);
            }
            if (acc.isRead()) bundle.addRead(acc); else bundle.addWrite(acc);
        }

        /**
         * Retrieve the ResourceBundle for a given Resource.
         */
        public ResourceBundle getBundle(Resource target) {
            return (ResourceBundle) resourceToBundle.get(target);
        }

        /**
         * Retrieve all the Resoruces tracked in this frame.
         */
        public Set getResources() {
            return resourceToBundle.keySet();
        }

        /**
         * Retrieve every MemAccess allocated in this frame.
         */
        public Set getAllAccesses() {
            Set accesses = new HashSet();
            for (Iterator iter = resourceToBundle.values().iterator(); iter.hasNext(); ) {
                ResourceBundle bundle = (ResourceBundle) iter.next();
                accesses.addAll(bundle.getReads());
                accesses.addAll(bundle.getWrites());
            }
            return accesses;
        }
    }

    /**
     * A ResourceBundle tracks all read and write accesses to a given
     * Resource.
     */
    private static class ResourceBundle {

        /** A List of MemAccess objects */
        private List reads = new ArrayList();

        /** A List of MemAccess objects */
        private List writes = new ArrayList();

        private Resource target;

        public ResourceBundle(Resource target) {
            this.target = target;
        }

        /** acc MAY be null @ task level */
        public void addRead(MemAccess acc) {
            this.reads.add(acc);
        }

        /** acc MAY be null @ task level */
        public void addWrite(MemAccess acc) {
            this.writes.add(acc);
        }

        /**
         * Retrieves a List of MemAccess objects, each of which is a
         * read access
         */
        public List getReads() {
            return this.reads;
        }

        /**
         * Retrieves a List of MemAccess objects, each of which is a
         * write access
         */
        public List getWrites() {
            return this.writes;
        }

        public int getAccessCount() {
            return this.reads.size() + this.writes.size();
        }
    }

    /**
     * MemAccess is a class which ties together all the ports and
     * buses related to a single access of a given resource.  
     */
    private abstract static class MemAccess {

        private List ports = new ArrayList(7);

        private List buses = new ArrayList(7);

        private boolean isRead = false;

        private Resource target;

        protected MemAccess(boolean read, Resource target) {
            this.isRead = read;
            this.target = target;
        }

        public boolean isRead() {
            return this.isRead;
        }

        public Resource getTarget() {
            return this.target;
        }

        protected void addPort(Port p) {
            this.ports.add(p);
        }

        protected void addBus(Bus b) {
            this.buses.add(b);
        }

        protected Port getPort(int index) {
            return (Port) this.ports.get(index);
        }

        protected Bus getBus(int index) {
            return (Bus) this.buses.get(index);
        }

        /**
         * Copies the ports and buses (creates new sideband ones) of
         * this MemAccess to the specified call.
         *
         * @param call the {@link Call} to which this access needs to
         * be copied.
         * @return a MemAccess tying together the duplicated ports/buses.
         */
        public MemAccess pushAcrossCall(Call call) {
            MemAccess copy = copy();
            for (Iterator iter = this.ports.iterator(); iter.hasNext(); ) {
                Port orig = (Port) iter.next();
                Port cPort = call.getPortFromProcedurePort(orig);
                if (cPort == null) {
                    cPort = call.makeDataPort(Component.SIDEBAND);
                    call.setProcedurePort(cPort, orig);
                }
                copy.addPort(cPort);
            }
            for (Iterator iter = this.buses.iterator(); iter.hasNext(); ) {
                Bus orig = (Bus) iter.next();
                Bus cBus = call.getBusFromProcedureBus(orig);
                if (cBus == null) {
                    Exit exit = call.getExit(Exit.SIDEBAND);
                    if (exit == null) {
                        exit = call.makeExit(0, Exit.SIDEBAND);
                        call.setProcedureBus(exit.getDoneBus(), orig.getOwner().getDoneBus());
                    }
                    cBus = exit.makeDataBus(Component.SIDEBAND);
                    cBus.setIDLogical(ID.showLogical(orig));
                    cBus.copyAttributes(orig);
                    call.setProcedureBus(cBus, orig);
                }
                copy.addBus(cBus);
            }
            return copy;
        }

        /** Connect this MemAccess to the given MemoryGateway */
        public abstract void connect(MemoryGateway gateway, int index);

        /** Connect this MemAccess to the given MemoryReferee task slot */
        public abstract void connect(MemoryReferee.TaskSlot slot);

        /** Returns a new MemAccess which is of the same subclass. */
        public abstract MemAccess copy();
    }

    private static class MemReadAccess extends MemAccess {

        private MemReadAccess(Resource target) {
            super(true, target);
        }

        public MemReadAccess(MemoryRead memRead) {
            this(memRead.getMemoryPort());
            MemoryRead.Physical physical = (MemoryRead.Physical) memRead.getPhysicalComponent();
            addBus(physical.getSideEnableBus());
            addBus(physical.getSideAddressBus());
            addBus(physical.getSideSizeBus());
            addPort(physical.getSideDataReadyPort());
            addPort(physical.getSideDataPort());
        }

        /**
         * Create a new MemReadAccess representing the 'global' side
         * of the MemoryGateway by duplicating the global ports/buses
         * on the containing module (as provided) and grouping those
         * newly created ports/buses.  Avoids duplication with
         * ports/buses shared with a write access via the duplicate
         * map.
         */
        public MemReadAccess(MemoryGateway gateway, Module mod, Map duplicate) {
            this(gateway.getResource());
            Exit exit = mod.getExit(Exit.SIDEBAND);
            if (exit == null) exit = mod.makeExit(0, Exit.SIDEBAND);
            Bus en = (Bus) duplicate.get(gateway.getMemoryReadEnableBus());
            if (en == null) {
                en = exit.makeDataBus(Component.SIDEBAND);
                en.setIDLogical(getTarget().showIDLogical() + "_RE");
                en.getPeer().setBus(gateway.getMemoryReadEnableBus());
            }
            Bus addr = (Bus) duplicate.get(gateway.getMemoryAddressBus());
            if (addr == null) {
                addr = exit.makeDataBus(Component.SIDEBAND);
                addr.setIDLogical(getTarget().showIDLogical() + "_ADDR");
                addr.getPeer().setBus(gateway.getMemoryAddressBus());
            }
            Bus size = (Bus) duplicate.get(gateway.getMemorySizeBus());
            if (size == null) {
                size = exit.makeDataBus(Component.SIDEBAND);
                size.setIDLogical(getTarget().showIDLogical() + "_SIZE");
                size.getPeer().setBus(gateway.getMemorySizeBus());
            }
            Port ready = (Port) duplicate.get(gateway.getMemoryDonePort());
            if (ready == null) {
                ready = mod.makeDataPort(Component.SIDEBAND);
                ready.getPeer().setIDLogical(getTarget().showIDLogical() + "_DONE");
                gateway.getMemoryDonePort().setBus(ready.getPeer());
            }
            Port data = (Port) duplicate.get(gateway.getMemoryDataReadPort());
            if (data == null) {
                data = mod.makeDataPort(Component.SIDEBAND);
                data.getPeer().setIDLogical(getTarget().showIDLogical() + "_RDATA");
                gateway.getMemoryDataReadPort().setBus(data.getPeer());
            }
            addBus(en);
            addBus(addr);
            addBus(size);
            addPort(ready);
            addPort(data);
        }

        public void connect(MemoryGateway gateway, int index) {
            MemoryGateway.ReadSlot slot = (MemoryGateway.ReadSlot) gateway.getReadSlots().get(index);
            slot.getEnable().setBus(getBus(0));
            slot.getAddress().setBus(getBus(1));
            slot.getSizePort().setBus(getBus(2));
            getPort(0).setBus(slot.getReady());
            getPort(1).setBus(slot.getData());
        }

        public void connect(MemoryReferee.TaskSlot slot) {
            slot.getGoRPort().setBus(getBus(0));
            slot.getAddressPort().setBus(getBus(1));
            slot.getSizePort().setBus(getBus(2));
            getPort(0).setBus(slot.getDoneBus());
            getPort(1).setBus(slot.getDataOutBus());
        }

        public MemAccess copy() {
            return new MemReadAccess(getTarget());
        }
    }

    private static class MemWriteAccess extends MemAccess {

        private MemWriteAccess(Resource target) {
            super(false, target);
        }

        public MemWriteAccess(MemoryWrite memWrite) {
            this(memWrite.getMemoryPort());
            MemoryWrite.Physical physical = (MemoryWrite.Physical) memWrite.getPhysicalComponent();
            addBus(physical.getSideEnableBus());
            addBus(physical.getSideAddressBus());
            addBus(physical.getSideDataBus());
            addBus(physical.getSideSizeBus());
            addPort(physical.getSideWriteFinishedPort());
        }

        /**
         * Create a new MemWriteAccess representing the 'global' side
         * of the MemoryGateway by duplicating the global ports/buses
         * on the containing module (as provided) and grouping those
         * newly created ports/buses.  Avoids duplication with
         * ports/buses shared with a read access via the duplicate
         * map.
         */
        public MemWriteAccess(MemoryGateway gateway, Module mod, Map duplicate) {
            this(gateway.getResource());
            Exit exit = mod.getExit(Exit.SIDEBAND);
            if (exit == null) exit = mod.makeExit(0, Exit.SIDEBAND);
            Bus en = (Bus) duplicate.get(gateway.getMemoryWriteEnableBus());
            if (en == null) {
                en = exit.makeDataBus(Component.SIDEBAND);
                en.setIDLogical(getTarget().showIDLogical() + "_WE");
                en.getPeer().setBus(gateway.getMemoryWriteEnableBus());
            }
            Bus addr = (Bus) duplicate.get(gateway.getMemoryAddressBus());
            if (addr == null) {
                addr = exit.makeDataBus(Component.SIDEBAND);
                addr.setIDLogical(getTarget().showIDLogical() + "_ADDR");
                addr.getPeer().setBus(gateway.getMemoryAddressBus());
            }
            Bus data = (Bus) duplicate.get(gateway.getMemoryDataWriteBus());
            if (data == null) {
                data = exit.makeDataBus(Component.SIDEBAND);
                data.setIDLogical(getTarget().showIDLogical() + "_WDATA");
                data.getPeer().setBus(gateway.getMemoryDataWriteBus());
            }
            Bus size = (Bus) duplicate.get(gateway.getMemorySizeBus());
            if (size == null) {
                size = exit.makeDataBus(Component.SIDEBAND);
                size.setIDLogical(getTarget().showIDLogical() + "_SIZE");
                size.getPeer().setBus(gateway.getMemorySizeBus());
            }
            Port ready = (Port) duplicate.get(gateway.getMemoryDonePort());
            if (ready == null) {
                ready = mod.makeDataPort(Component.SIDEBAND);
                ready.getPeer().setIDLogical(getTarget().showIDLogical() + "_DONE");
                gateway.getMemoryDonePort().setBus(ready.getPeer());
            }
            addBus(en);
            addBus(addr);
            addBus(data);
            addBus(size);
            addPort(ready);
        }

        public void connect(MemoryGateway gateway, int index) {
            MemoryGateway.WriteSlot slot = (MemoryGateway.WriteSlot) gateway.getWriteSlots().get(index);
            getPort(0).setBus(slot.getDone());
            slot.getEnable().setBus(getBus(0));
            slot.getAddress().setBus(getBus(1));
            slot.getData().setBus(getBus(2));
            slot.getSizePort().setBus(getBus(3));
        }

        public void connect(MemoryReferee.TaskSlot slot) {
            slot.getGoWPort().setBus(getBus(0));
            slot.getAddressPort().setBus(getBus(1));
            slot.getDataInPort().setBus(getBus(2));
            slot.getSizePort().setBus(getBus(3));
            getPort(0).setBus(slot.getDoneBus());
        }

        public MemAccess copy() {
            return new MemWriteAccess(getTarget());
        }
    }
}
