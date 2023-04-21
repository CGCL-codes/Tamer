package net.sf.openforge.frontend.slim.builder;

import java.util.*;
import net.sf.openforge.app.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.memory.*;
import net.sf.openforge.lim.op.*;
import net.sf.openforge.util.MathStuff;
import org.w3c.dom.*;

/**
 * XOperationFactory generates the appropriate Component for each SLIM
 * operation.  In some cases this may be a composite Component such as
 * a Branch or Block.
 *
 * <p>Created: Tue Jul 12 11:46:41 2005
 *
 * @author imiller, last modified by $Author: imiller $
 */
public class XOperationFactory extends XFactory {

    /** The cache of design level resources. */
    private ResourceCache resourceCache;

    private GenericJob gj;

    XOperationFactory(ResourceCache resources) {
        this.resourceCache = resources;
        gj = EngineThread.getEngine().getGenericJob();
    }

    /**
     * Generate the Component for the specified Node, updating the
     * port cache with the mapping of port nodes to {@link Port} and
     * {@link Bus} objects.
     *
     * @param node an Element Node
     * @param portCache a non-null PortCache which will be updated
     * with the port mappings for the generated component.
     * @return a non-null Component
     */
    public Component makeOperation(Node node, PortCache portCache) {
        assert node.getNodeType() == Node.ELEMENT_NODE;
        Element element = (Element) node;
        String elementTag = element.getNodeName();
        String type = element.getAttribute(SLIMConstants.ELEMENT_KIND);
        Component comp = null;
        if (elementTag.equals(SLIMConstants.MODULE)) {
            String moduleType = element.getAttribute(SLIMConstants.MODULE_STYLE);
            XModuleFactory factory;
            if (moduleType.equals(SLIMConstants.IF)) {
                factory = new XBranchFactory(this.resourceCache);
                comp = ((XBranchFactory) factory).buildBranch(element);
            } else if (moduleType.equals(SLIMConstants.LOOP)) {
                factory = new XLoopFactory(this.resourceCache);
                comp = ((XLoopFactory) factory).buildLoop(element);
            } else {
                factory = new XModuleFactory(this.resourceCache, element.getAttribute(SLIMConstants.MODULE_MUTEX).equals(SLIMConstants.TRUE));
                comp = factory.buildComponent(element);
            }
            factory.publishPorts(portCache);
        } else if (type.equals(SLIMConstants.PIN_READ)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getReadAccess(element);
        } else if (type.equals(SLIMConstants.PIN_WRITE)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getWriteAccess(element);
        } else if (type.equals(SLIMConstants.PIN_COUNT)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getTokenCountAccess();
        } else if (type.equals(SLIMConstants.PIN_STALL)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getStallAccess();
        } else if (type.equals(SLIMConstants.PIN_PEEK)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getTokenPeekAccess();
        } else if (type.equals(SLIMConstants.PIN_STATUS)) {
            String pinName = element.getAttribute("portName");
            ActionIOHandler ioHandler = this.resourceCache.getIOHandler(pinName);
            comp = ioHandler.getStatusAccess();
        } else if (type.equals(SLIMConstants.TASKCALL)) {
            comp = new TaskCall();
            this.resourceCache.addTaskCall(element, (TaskCall) comp);
        } else if (type.equals(SLIMConstants.VAR_REF)) {
            String scope = element.getAttribute("var-scope");
            String name = element.getAttribute("name");
            if (scope.equals("local")) {
                comp = new NoOp(0, Exit.DONE);
                comp.makeDataPort();
                comp.getExit(Exit.DONE).makeDataBus();
            } else {
                final List ports = getChildNodesByTag(element, SLIMConstants.PORT);
                final Element outBus = getSingleOutputPort(element);
                final Location targetLocation = this.resourceCache.getLocation(name);
                final LogicalMemoryPort memPort = (LogicalMemoryPort) targetLocation.getLogicalMemory().getLogicalMemoryPorts().iterator().next();
                final AddressStridePolicy addrPolicy = targetLocation.getAbsoluteBase().getInitialValue().getAddressStridePolicy();
                if (ports.size() == 0) {
                    comp = new AbsoluteMemoryRead(targetLocation, SLIMConstants.MAX_ADDR_WIDTH, isSignedPort(outBus));
                    memPort.addAccess((LValue) comp, targetLocation);
                } else if (ports.size() == 1) {
                    int dataSize = getPortSize(outBus);
                    assert dataSize > 0 : "Read from memory must be of width > 0: " + dataSize + " on " + outBus.getAttribute("tag");
                    assert dataSize % addrPolicy.getStride() == 0 : "Data input size (" + dataSize + ") must be an integer multiple of the address stride size (" + addrPolicy.getStride() + ") (read)" + outBus.getAttribute("tag");
                    assert (dataSize / addrPolicy.getStride()) > 0 : "Stride is larger than data size for " + outBus.getAttribute("tag");
                    if (dataSize % addrPolicy.getStride() != 0) gj.warn("Data input size (" + dataSize + ") must be an integer multiple of the address stride size (" + addrPolicy.getStride() + ") (read)" + outBus.getAttribute("tag"));
                    if (dataSize / addrPolicy.getStride() <= 0) gj.warn("Address stride is larger than data size for " + outBus.getAttribute("tag"));
                    HeapRead read = new HeapRead(dataSize / addrPolicy.getStride(), SLIMConstants.MAX_ADDR_WIDTH, 0, isSignedPort(outBus), addrPolicy);
                    CastOp castOp = new CastOp(getPortSize(outBus), isSignedPort(outBus));
                    Block block = buildAddressedBlock(read, targetLocation, Collections.singletonList(castOp));
                    Bus result = block.getExit(Exit.DONE).makeDataBus();
                    ((Entry) castOp.getEntries().get(0)).addDependency(castOp.getDataPort(), new DataDependency(read.getResultBus()));
                    ((Entry) result.getPeer().getOwner().getEntries().get(0)).addDependency(result.getPeer(), new DataDependency(castOp.getResultBus()));
                    memPort.addAccess((LValue) read, targetLocation);
                    comp = block;
                } else {
                    assert false : "memory read (var_ref) has illegal number of ports " + ports.size();
                }
            }
        } else if (type.equals(SLIMConstants.ASSIGN)) {
            final String varName = element.getAttribute(SLIMConstants.RESOURCE_TARGET);
            final Location targetLocation = this.resourceCache.getLocation(varName);
            final LogicalMemoryPort memPort = (LogicalMemoryPort) targetLocation.getLogicalMemory().getLogicalMemoryPorts().iterator().next();
            final AddressStridePolicy addrPolicy = targetLocation.getAbsoluteBase().getInitialValue().getAddressStridePolicy();
            final List ports = getChildNodesByTag(element, SLIMConstants.PORT);
            if (ports.size() == 1) {
                final Element dataPort = (Element) ports.get(0);
                comp = new AbsoluteMemoryWrite(targetLocation, SLIMConstants.MAX_ADDR_WIDTH, isSignedPort(dataPort));
                memPort.addAccess((LValue) comp, targetLocation);
            } else if (ports.size() == 2) {
                final Element dataPort = (Element) ports.get(1);
                int dataSize = getPortSize(dataPort);
                assert dataSize % addrPolicy.getStride() == 0 : "Data input size must be an integer multiple of the address stride size (write) " + dataPort.getAttribute("tag");
                HeapWrite heapWrite = new HeapWrite(dataSize / addrPolicy.getStride(), SLIMConstants.MAX_ADDR_WIDTH, 0, isSignedPort(dataPort), addrPolicy);
                Block block = buildAddressedBlock(heapWrite, targetLocation, Collections.EMPTY_LIST);
                Port data = block.makeDataPort();
                ((Entry) heapWrite.getEntries().get(0)).addDependency(heapWrite.getValuePort(), new DataDependency(data.getPeer()));
                memPort.addAccess((LValue) heapWrite, targetLocation);
                comp = block;
            } else {
                assert false : "memory write (assign) has illegal number of ports " + ports.size();
            }
        } else if (type.equals(SLIMConstants.CONSTANT)) {
            final String valueAttr = element.getAttribute("value");
            final long value = Long.parseLong(valueAttr);
            final Element outBus = getSingleOutputPort(element);
            comp = new SimpleConstant(value, getPortSize(outBus), isSignedPort(outBus));
        } else if (type.equals(SLIMConstants.ADD)) {
            comp = new AddOp();
        } else if (type.equals(SLIMConstants.AND) || type.equals("bitand")) {
            Element result = getSingleOutputPort(element);
            assert result != null;
            if (getPortSize(result) > 1) {
                comp = new AndOp();
            } else {
                List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
                comp = new And(inputPorts.size());
            }
        } else if (type.equals(SLIMConstants.DIV)) {
            final Element outBus = getSingleOutputPort(element);
            comp = new DivideOp(getPortSize(outBus));
        } else if (type.equals(SLIMConstants.EQ)) {
            comp = new EqualsOp();
        } else if (type.equals(SLIMConstants.GE) || type.equals("$gte")) {
            comp = new GreaterThanEqualToOp();
        } else if (type.equals(SLIMConstants.GT)) {
            comp = new GreaterThanOp();
        } else if (type.equals(SLIMConstants.LE)) {
            comp = new LessThanEqualToOp();
        } else if (type.equals(SLIMConstants.LT)) {
            comp = new LessThanOp();
        } else if (type.equals(SLIMConstants.MUL)) {
            final Element outBus = getSingleOutputPort(element);
            comp = new MultiplyOp(getPortSize(outBus));
        } else if (type.equals(SLIMConstants.NE)) {
            comp = new NotEqualsOp();
        } else if (type.equals(SLIMConstants.NEGATE)) {
            comp = new MinusOp();
        } else if (type.equals(SLIMConstants.NOT)) {
            comp = new NotOp();
        } else if (type.equals(SLIMConstants.BITNOT)) {
            comp = new ComplementOp();
        } else if (type.equals(SLIMConstants.OR) || type.equals("bitor")) {
            Element result = getSingleOutputPort(element);
            assert result != null;
            if (getPortSize(result) > 1) {
                comp = new OrOp();
            } else {
                List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
                comp = new Or(inputPorts.size());
            }
        } else if (type.equals(SLIMConstants.SUB)) {
            comp = new SubtractOp();
        } else if (type.equals(SLIMConstants.XOR) || type.equals("bitxor")) {
            comp = new XorOp();
        } else if (type.equals(SLIMConstants.LSHIFT)) {
            List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
            assert inputPorts.size() == 2;
            int dataSize = getPortSize((Node) inputPorts.get(0));
            int log2N = MathStuff.log2(dataSize);
            comp = new LeftShiftOp(log2N);
        } else if (type.equals(SLIMConstants.RSHIFT)) {
            List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
            assert inputPorts.size() == 2;
            int dataSize = getPortSize((Node) inputPorts.get(0));
            int log2N = MathStuff.log2(dataSize);
            comp = new RightShiftOp(log2N);
        } else if (type.equals(SLIMConstants.URSHIFT)) {
            List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
            assert inputPorts.size() == 2;
            int dataSize = getPortSize((Node) inputPorts.get(0));
            int log2N = MathStuff.log2(dataSize);
            comp = new RightShiftUnsignedOp(log2N);
        } else if (type.equals(SLIMConstants.NOOP)) {
            List inputPorts = getChildNodesByTag(element, SLIMConstants.PORT);
            List exits = getChildNodesByTag(element, SLIMConstants.EXIT);
            assert exits.size() == 1;
            List outputPorts = getChildNodesByTag(((Element) exits.get(0)), SLIMConstants.PORT);
            int dataOutputPorts = 0;
            for (Iterator outputIter = outputPorts.iterator(); outputIter.hasNext(); ) {
                if (!((Element) outputIter.next()).getAttribute(SLIMConstants.PORT_TYPE).equals(SLIMConstants.CONTROL_TYPE)) dataOutputPorts++;
            }
            assert inputPorts.size() == dataOutputPorts : "input and output sizes dont match";
            comp = new NoOp(inputPorts.size(), Exit.DONE);
        } else if (type.equals(SLIMConstants.CAST)) {
            Element result = getSingleOutputPort(element);
            int size = getPortSize(result);
            boolean signed = isSignedPort(result);
            comp = new CastOp(size, signed);
        } else {
            throw new UnsupportedOperationException("Unhandled node.  Tag=" + elementTag + " kind=" + type);
        }
        if (_parser.db) {
            String tagId = "unknown tag";
            try {
                tagId = ((Element) node).getAttribute("tag");
            } catch (Exception e) {
            }
            System.out.println("Mapping " + tagId + " to component " + comp.show());
        }
        setAttributes(node, comp);
        mapPorts(node, comp, portCache);
        this.resourceCache.registerConfigurable(node, comp);
        return comp;
    }

    private Block buildAddressedBlock(OffsetMemoryAccess memAccess, Location targetLocation, List otherComps) {
        final LocationConstant locationConst = new LocationConstant(targetLocation, SLIMConstants.MAX_ADDR_WIDTH, targetLocation.getAbsoluteBase().getLogicalMemory().getAddressStridePolicy());
        final AddOp adder = new AddOp();
        final CastOp cast = new CastOp(SLIMConstants.MAX_ADDR_WIDTH, false);
        final Block block = new Block(false);
        final Exit done = block.makeExit(0, Exit.DONE);
        final List comps = new ArrayList();
        comps.add(locationConst);
        comps.add(cast);
        comps.add(adder);
        comps.add(memAccess);
        comps.addAll(otherComps);
        XModuleFactory.populateModule(block, comps);
        final Port index = block.makeDataPort();
        ((Entry) cast.getEntries().get(0)).addDependency(cast.getDataPort(), new DataDependency(index.getPeer()));
        ((Entry) adder.getEntries().get(0)).addDependency(adder.getLeftDataPort(), new DataDependency(locationConst.getValueBus()));
        ((Entry) adder.getEntries().get(0)).addDependency(adder.getRightDataPort(), new DataDependency(cast.getResultBus()));
        ((Entry) memAccess.getEntries().get(0)).addDependency(memAccess.getBaseAddressPort(), new DataDependency(adder.getResultBus()));
        ((Entry) done.getPeer().getEntries().get(0)).addDependency(done.getDoneBus().getPeer(), new ControlDependency(memAccess.getExit(Exit.DONE).getDoneBus()));
        return block;
    }

    /**
     * Ensure that there are the same number of port elements on the
     * node as there are data ports on the operation.  
     *
     * @param node a value of type 'Node'
     * @param op a value of type 'Component'
     * @param portCache a value of type 'PortCache'
     */
    private void mapPorts(Node node, Component op, PortCache portCache) {
        final NodeList children = node.getChildNodes();
        final String identifier = ((Element) node).getAttribute("tag");
        Node exit = null;
        Iterator iter = op.getDataPorts().iterator();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeName().equals("port")) {
                Port port;
                if (((Element) child).getAttribute(SLIMConstants.PORT_TYPE).equals(SLIMConstants.CONTROL_TYPE)) {
                    port = op.getGoPort();
                } else {
                    assert iter.hasNext() : "Fewer component ports than operation(element) ports " + identifier + " " + op.show();
                    port = (Port) iter.next();
                    port.setSize(getPortSize(child), isSignedPort(child));
                }
                portCache.putTarget(child, port);
            }
            if (child.getNodeName().equals("exit")) {
                assert exit == null : "Only expecting one exit on component";
                exit = child;
            }
        }
        assert !iter.hasNext() : "More component ports than operation(element) ports " + identifier + " " + op.show();
        assert exit != null : "Could not find an exit for the element";
        iter = op.getExit(Exit.DONE).getDataBuses().iterator();
        final NodeList exitChildren = exit.getChildNodes();
        for (int i = 0; i < exitChildren.getLength(); i++) {
            final Node child = exitChildren.item(i);
            if (child.getNodeName().equals("port")) {
                Bus bus;
                if (((Element) child).getAttribute(SLIMConstants.ELEMENT_KIND).equals(SLIMConstants.CONTROL_TYPE)) {
                    bus = op.getExit(Exit.DONE).getDoneBus();
                } else {
                    assert iter.hasNext() : "Fewer component buses than operation(element) ports " + identifier + " " + op.show();
                    bus = (Bus) iter.next();
                    if (bus.getValue() == null) {
                        bus.setSize(getPortSize(child), isSignedPort(child));
                    }
                }
                portCache.putSource(child, bus);
            }
        }
        assert !iter.hasNext() : "More component buses than operation output element ports " + identifier + " " + op.show();
    }

    /**
     * Returns the single XLIM port node for the specified operation
     * element.  If the element has more than 1 output port (of non
     * control type) then an error will be thrown.
     *
     * @param operation a non-null Element
     * @return a non-null Element, of type port
     */
    private final Element getSingleOutputPort(Element operation) {
        final List exits = getChildNodesByTag(operation, SLIMConstants.EXIT);
        assert exits.size() == 1 : exits.size() + " exits " + operation.getAttribute("tag");
        final Element exitNode = (Element) exits.get(0);
        final List outBuses = getChildNodesByTag(exitNode, SLIMConstants.PORT);
        Element outBus = null;
        for (Iterator outBusIter = outBuses.iterator(); outBusIter.hasNext(); ) {
            final Element busNode = (Element) outBusIter.next();
            if (!busNode.getAttribute(SLIMConstants.ELEMENT_KIND).equals(SLIMConstants.CONTROL_TYPE)) {
                assert outBus == null : operation.getAttribute("tag") + " Must have only one non-control bus";
                outBus = busNode;
            }
        }
        if (outBus == null) throw new IllegalArgumentException("Operation does not have non-control output port");
        return outBus;
    }
}
