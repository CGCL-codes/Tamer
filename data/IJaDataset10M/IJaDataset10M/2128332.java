package logiklabor.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import logiklabor.config.InternationalizationTool;
import logiklabor.data.formulas.FormulaTool;
import logiklabor.data.formulas.LogicFormula;
import logiklabor.data.formulas.LogicSubFormula;
import logiklabor.data.formulas.LogicVariable;
import logiklabor.exceptions.CollisionException;
import logiklabor.exceptions.IllegalMethodCallException;
import logiklabor.exceptions.OutOfBoundsException;
import logiklabor.gui.datamodels.AxisParallelLine;
import logiklabor.gui.datamodels.RatedVertexes;
import logiklabor.gui.datamodels.Rectangle;
import logiklabor.gui.graphicelements.GraphicalConnection;
import logiklabor.gui.graphicelements.GraphicalConnectionSegment;
import logiklabor.gui.graphicelements.GraphicalGateDevice;
import logiklabor.gui.graphicelements.GraphicalInputDevice;
import logiklabor.gui.graphicelements.GraphicalLogicDevice;
import logiklabor.gui.graphicelements.GraphicalLogicDeviceConnection;
import logiklabor.gui.graphicelements.GraphicalOutputDevice;

/**
 * This class ensures that the elements on the main panel do not collide with
 * each other.
 * 
 * @author Sebastian Mehrbreier
 * 
 */
public class FreeSpaceManager {

    /**
	 * the length in pixels the path will be corrected if it did not work out
	 */
    public static final int CONNECTION_CALCULATION_STEPLENGTH = 10;

    /**
	 * Maximal number of segments a connection could be split into. This ensures
	 * that the algorithm is efficient enough
	 */
    public static final int MAX_SEGMENTS_COUNT = 5;

    /**
	 * Maximal number of outputs
	 */
    public static final int MAX_OUTPUTS_COUNT = 4;

    /**
	 * Maximal number of inputs
	 */
    public static final int MAX_INPUTS_COUNT = 4;

    private InternationalizationTool translationTool = InternationalizationTool.getIntenationalizationTool();

    private final LogicCircuitPanel assignedPanel;

    private Rectangle freeSpace;

    private FreeSpaceReservingUnit[][] reservingUnits;

    private List<GraphicalInputDevice> inputs = new ArrayList<GraphicalInputDevice>();

    private List<GraphicalOutputDevice> outputs = new ArrayList<GraphicalOutputDevice>();

    /**
	 * @param log
	 *            a logger, the log messages should be send to
	 * @param panelWidth
	 *            the width of the main panel whose free space this manager
	 *            manages
	 * @param panelHeight
	 *            the height of the main panel whose free space this manager
	 *            manages
	 */
    public FreeSpaceManager(LogicCircuitPanel assignedPanel) {
        this.assignedPanel = assignedPanel;
        resetReservedPanelBorders(false);
        resetPanel();
    }

    /**
	 * deletes the device and frees its reserved Space. This includes deleting
	 * the connections associated with the device
	 * 
	 * @param connection
	 */
    public void deleteDevice(GraphicalLogicDevice device) {
        unregisterDevice(device);
        deleteDeviceWithoutUnregistering(device);
    }

    private void deleteDeviceWithoutUnregistering(GraphicalLogicDevice device) {
        Iterator<GraphicalLogicDeviceConnection> it = device.getIncomingConnections().iterator();
        while (it.hasNext()) {
            GraphicalLogicDeviceConnection nextConnection = it.next();
            it.remove();
            deleteConnection(nextConnection);
        }
        it = device.getOutgoingConnections().iterator();
        while (it.hasNext()) {
            GraphicalLogicDeviceConnection nextConnection = it.next();
            it.remove();
            deleteConnection(nextConnection);
        }
    }

    /**
	 * Remove the device's reservations. Please note that this does NOT delete
	 * the device itself. Use {@link #deleteDevice(GraphicalLogicDevice)} to do
	 * so
	 * 
	 * @param device
	 */
    private void unregisterDevice(GraphicalLogicDevice device) {
        Rectangle oldReservingRectangle = device.getReservingRectangle();
        try {
            List<FreeSpaceReservingUnit> coveredUnits = getUnitsOverlapingWithRectangle(oldReservingRectangle);
            for (FreeSpaceReservingUnit u : coveredUnits) {
                u.removeContainingDevice(device);
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, translationTool.getText("NoUnreg"), e);
        }
    }

    /**
	 * deletes the connection and frees its reserved Space
	 * 
	 * @param connection
	 */
    public void deleteConnection(GraphicalLogicDeviceConnection connection) {
        GraphicalLogicDevice end = connection.getConnectionEnd();
        clearConnection(connection);
        connection.delete();
        for (GraphicalLogicDeviceConnection con : end.getIncomingConnections()) {
            if (end.getCenterLocation() != null) {
                if (con.getVertexes().get(0) != end.getInputLocationOfConnection(con)) {
                    clearConnection(con);
                    calculateConnectionVertexes(con);
                }
            }
        }
    }

    /**
	 * clears the connection vertexes and remove the reservations of the
	 * connections segments. This does not delete the connection! Use
	 * {@link #deleteConnection(GraphicalLogicDeviceConnection)} to do so.
	 * 
	 * @param connection
	 */
    private void clearConnection(GraphicalLogicDeviceConnection connection) {
        if (connection.getVertexes() != null) {
            for (GraphicalConnectionSegment segment : connection.getSegments()) {
                for (FreeSpaceReservingUnit unit : getUnitsOverlapingWithRectangle(segment.getReservingRectangle())) unit.removeContainingConnectionSegment(segment);
            }
            connection.clearVertexes();
        }
    }

    /**
	 * connect the two passed devices. This connection includes a connection on
	 * the data-level as well as calculating and creating a
	 * {@link GraphicalLogicDeviceConnection} and adding it to the connected
	 * devices
	 * 
	 * @param connectionBegin
	 * @param connectionEnd
	 * @throws IllegalArgumentException
	 *             if the connection cannot be established
	 */
    public void connectLogicDevices(GraphicalLogicDevice connectionBegin, GraphicalLogicDevice connectionEnd) throws IllegalArgumentException {
        GraphicalLogicDeviceConnection connection = new GraphicalLogicDeviceConnection(connectionBegin, connectionEnd);
        for (GraphicalLogicDeviceConnection con : connectionEnd.getIncomingConnections()) {
            if (con != connection && con.getVertexes().get(0) != connectionEnd.getInputLocationOfConnection(con)) {
                clearConnection(con);
                calculateConnectionVertexes(con);
            }
        }
        calculateConnectionVertexes(connection);
    }

    /**
	 * Check if the device can be placed at the location pos without causing any
	 * collision or being out of range. If the check was OK the space will be
	 * reserved for the passed device. A collision with a
	 * {@link GraphicalLogicDeviceConnection} does not abort the assigning
	 * process. Instead of adding it to the return value the connection will be
	 * recalculated
	 * 
	 * @param pos
	 *            location where the device should be added (this point will be
	 *            located in the center of the device)
	 * @param device
	 *            the device to be placed
	 * @throws OutOfBoundsException
	 *             if the new device would exceed the main panel's borders
	 * @throws CollisionException
	 *             if a collision with another device was detected
	 */
    public void registerDevice(GraphicalLogicDevice device) throws OutOfBoundsException, CollisionException {
        assertCompletelyInsideFreeSpace(device.getRectangle());
        List<FreeSpaceReservingUnit> overlappingUnits = getUnitsOverlapingWithRectangle(device.getReservingRectangle());
        Set<GraphicalLogicDevice> potentiallyCollidingDevices = new HashSet<GraphicalLogicDevice>();
        Set<GraphicalConnectionSegment> potentiallyCollidingConnectionSegments = new HashSet<GraphicalConnectionSegment>();
        for (FreeSpaceReservingUnit u : overlappingUnits) {
            potentiallyCollidingDevices.addAll(u.getContainingDevices());
            potentiallyCollidingConnectionSegments.addAll(u.getContainingConnectionSegments());
        }
        List<GraphicalLogicDevice> collidedDevices = new ArrayList<GraphicalLogicDevice>();
        for (GraphicalLogicDevice d : potentiallyCollidingDevices) {
            if (d.getIntersectionWithRectangle(device.getRectangle()) != null) collidedDevices.add(d);
        }
        if (collidedDevices.size() > 0) throw new CollisionException(translationTool.getText("CollDetect", device.toString()), null, collidedDevices, null);
        for (FreeSpaceReservingUnit u : overlappingUnits) {
            u.addContainingDevice(device);
        }
        Set<GraphicalLogicDeviceConnection> connections = new HashSet<GraphicalLogicDeviceConnection>();
        for (GraphicalConnectionSegment segment : potentiallyCollidingConnectionSegments) {
            if (segment.getIntersectionWithLine(segment) != null) connections.add(segment.getConnection());
        }
        for (GraphicalLogicDeviceConnection connection : connections) calculateConnectionVertexes(connection);
    }

    /**
	 * Adds a new Output to the panel and returns the created
	 * {@link GraphicalOutputDevice}
	 */
    public GraphicalOutputDevice addNewOutput() {
        GraphicalOutputDevice output = new GraphicalOutputDevice(getLogger());
        char OutputVar = (char) ('A' + outputs.size());
        output.setName(OutputVar + "");
        addOutput(output);
        return output;
    }

    private void addOutput(GraphicalOutputDevice output) {
        if (outputs.size() > (FreeSpaceManager.MAX_OUTPUTS_COUNT - 1)) throw new IllegalMethodCallException(translationTool.getText("NoMoreOutput"));
        int outputsPadding = getPanelHeight() / (outputs.size() + 2);
        int outputXLocation = getPanelWidth() - 90;
        resetReservedPanelBorders(true);
        int i = 1;
        for (GraphicalOutputDevice out : outputs) {
            out.setMovingTo(new Point(outputXLocation, outputsPadding * i));
            moveDeviceToNewPosition(out);
            i++;
        }
        output.setCenterLocation(new Point(outputXLocation, outputsPadding * i));
        registerDevice(output);
        outputs.add(output);
        resetReservedPanelBorders(false);
    }

    /**
	 * removes the given output from the panel. If null is passed the last one
	 * will be removed. Furthermore all Connections to this output will be
	 * deleted
	 * 
	 * @param output
	 */
    public void deleteOutput(GraphicalOutputDevice output) {
        deleteOutput(output, false);
    }

    /**
	 * removes the given output from the panel. If null is passed the last one
	 * will be removed. Furthermore all Connections to this output will be
	 * deleted.
	 * 
	 * @param output
	 * @param lastOneCanBeDeleted
	 *            if this is false and the output to be removed is the only
	 *            output this method will throw an exception
	 */
    private void deleteOutput(GraphicalOutputDevice output, boolean lastOneCanBeDeleted) {
        int minOutputCount = 1;
        if (lastOneCanBeDeleted) minOutputCount = 0;
        if (outputs.size() <= minOutputCount) throw new IllegalMethodCallException(translationTool.getText("NoOutput"));
        if (output == null) output = outputs.get(outputs.size() - 1);
        outputs.remove(output);
        deleteDevice(output);
        int outputsPadding = getPanelHeight() / (outputs.size() + 1);
        int outputXLocation = getPanelWidth() - 60;
        resetReservedPanelBorders(true);
        int i = 1;
        for (GraphicalOutputDevice out : outputs) {
            out.setMovingTo(new Point(outputXLocation, outputsPadding * i));
            moveDeviceToNewPosition(out);
            i++;
        }
        resetReservedPanelBorders(false);
    }

    /**
	 * Arrange Inputs so that there isn't one slot missing and that they are
	 * sorted by their Name Do not use
	 * {@link #registerDevice(GraphicalLogicDevice)} on inputs. Just add the
	 * input to {@link #inputs} and call this function
	 */
    private void arrangeInputs() {
        for (int i = 0; i < inputs.size() - 1; i++) {
            boolean sorted = true;
            for (int j = i; j < inputs.size() - 1; j++) {
                if (inputs.get(j).getVariable() > inputs.get(j + 1).getVariable()) {
                    GraphicalInputDevice temp = inputs.get(j + 1);
                    inputs.set(j + 1, inputs.get(j));
                    inputs.set(j, temp);
                    sorted = false;
                }
            }
            if (sorted) break;
        }
        for (GraphicalInputDevice input : inputs) {
            if (input.getCenterLocation() != null) {
                unregisterDevice(input);
                for (GraphicalLogicDeviceConnection conn : input.getOutgoingConnections()) clearConnection(conn);
            }
        }
        resetReservedPanelBorders(true);
        for (int i = 0; i < inputs.size(); i++) {
            Point loc = new Point(200 - i * 50, 50);
            GraphicalInputDevice input = inputs.get(i);
            input.setCenterLocation(loc);
            registerDevice(input);
            for (GraphicalLogicDeviceConnection conn : input.getOutgoingConnections()) calculateConnectionVertexes(conn);
        }
        resetReservedPanelBorders(false);
    }

    /**
	 * Adds a new Input to the panel and returns the created
	 * {@link GraphicalInputDevice}
	 */
    public GraphicalInputDevice addNewInput() {
        char variable = 'a';
        for (GraphicalInputDevice in : inputs) {
            if (in.getVariable() > variable) break;
            variable++;
        }
        return addNewInput(variable);
    }

    /**
	 * @return a list containing the inputs managed by the
	 *         {@link FreeSpaceManager}
	 */
    public List<GraphicalInputDevice> listInputs() {
        List<GraphicalInputDevice> inputsCopy = new ArrayList<GraphicalInputDevice>();
        inputsCopy.addAll(inputs);
        return inputsCopy;
    }

    /**
	 * Adds an input device with the specified variable to the panel and returns
	 * it.
	 * 
	 * @throws IllegalArgumentException
	 *             if the input was already added to the panel
	 */
    private GraphicalInputDevice addNewInput(char variable) {
        for (GraphicalInputDevice input : inputs) {
            if (input.getVariable() == variable) throw new IllegalArgumentException(translationTool.getText("VarOnPanel", variable + ""));
        }
        if (inputs.size() > (FreeSpaceManager.MAX_INPUTS_COUNT - 1)) throw new IllegalMethodCallException(translationTool.getText("NoMoreInput"));
        if (variable < 'a' || variable > 'z') throw new IllegalArgumentException(translationTool.getText("VarNotValid", variable + ""));
        GraphicalInputDevice input = GraphicalInputDevice.getInput(assignedPanel, variable);
        addNewInput(input);
        return input;
    }

    private void addNewInput(GraphicalInputDevice input) {
        inputs.add(input);
        arrangeInputs();
    }

    /**
	 * removes the given input from the panel. If null is passed the last one
	 * will be removed. Furthermore all Connections from this input will be
	 * deleted
	 * 
	 * @param input
	 */
    public void deleteInput(GraphicalInputDevice input) {
        deleteInput(input, false);
    }

    /**
	 * removes the given input from the panel. If null is passed the last one
	 * will be removed. Furthermore all Connections from this input will be
	 * deleted
	 * 
	 * @param input
	 * @param lastOneCanBeDeleted
	 *            if this is false and the output to be removed is the only
	 *            output this method will throw an exception
	 */
    private void deleteInput(GraphicalInputDevice input, boolean lastOneCanBeDeleted) {
        int minOutputCount = 1;
        if (lastOneCanBeDeleted) minOutputCount = 0;
        if (inputs.size() <= minOutputCount) throw new IllegalMethodCallException(translationTool.getText("NoInput"));
        if (input == null) input = inputs.get(inputs.size() - 1);
        inputs.remove(input);
        deleteDevice(input);
        arrangeInputs();
    }

    public void deleteUnusedInputs() {
        for (int i = 0; i < inputs.size(); i++) {
            GraphicalInputDevice input = inputs.get(i);
            if (input.getOutgoingConnections().size() == 0) {
                deleteInput(input, true);
                i--;
            }
        }
    }

    /**
	 * Check if the device can be moved to location pos without causing any
	 * collision or being out of range. If the check was OK the space will be
	 * reserved for the passed device and the old space will be set free again
	 * 
	 * @param pos
	 *            location where the device should be moved (this point will be
	 *            located in the center of the device)
	 * @param device
	 *            the device to be moved
	 * @throws OutOfBoundsException
	 *             if the new device would exceed the main panel's borders
	 * @throws CollisionException
	 *             if a collision with another device was detected
	 */
    public void moveDeviceToNewPosition(GraphicalLogicDevice device) throws OutOfBoundsException, CollisionException {
        Point oldPosition = device.getCenterLocation();
        unregisterDevice(device);
        try {
            device.setCenterLocation(device.getMovingTo());
            registerDevice(device);
            for (GraphicalLogicDeviceConnection con : device.getIncomingConnections()) {
                if (con.getVertexes() == null || !con.getVertexes().get(con.getVertexes().size() - 1).equals(device.getInputLocationOfConnection(con))) {
                    clearConnection(con);
                    calculateConnectionVertexes(con);
                }
            }
            for (GraphicalLogicDeviceConnection con : device.getOutgoingConnections()) {
                if (con.getVertexes() == null || !con.getVertexes().get(0).equals(device.getOutputLocation())) {
                    clearConnection(con);
                    calculateConnectionVertexes(con);
                }
            }
        } catch (CollisionException e) {
            try {
                unregisterDevice(device);
                device.setCenterLocation(oldPosition);
                registerDevice(device);
            } catch (Exception e1) {
                throw new IllegalStateException(translationTool.getText("IllState"), e1);
            }
            throw e;
        } catch (OutOfBoundsException e) {
            try {
                unregisterDevice(device);
                device.setCenterLocation(oldPosition);
                registerDevice(device);
            } catch (Exception e1) {
                throw new IllegalStateException(translationTool.getText("IllState"), e1);
            }
            throw e;
        } catch (RuntimeException e) {
            try {
                unregisterDevice(device);
                device.setCenterLocation(oldPosition);
                registerDevice(device);
            } catch (Exception e1) {
                throw new IllegalStateException(translationTool.getText("IllState"), e1);
            }
            throw e;
        }
    }

    /**
	 * get the {@link GraphicalLogicDevice} which is placed at point p. Returns
	 * null if there isn't any device at the point
	 * 
	 * @param p
	 * @return
	 */
    public GraphicalLogicDevice getLogicDeviceAt(Point p) {
        List<GraphicalLogicDevice> devices = getUnitContainingPoint(p).getContainingDevices();
        for (GraphicalLogicDevice device : devices) {
            if (device.isLocatedAt(p)) return device;
        }
        return null;
    }

    /**
	 * get the {@link GraphicalConnection} which is placed at point p. Returns
	 * null if there isn't any connection at the point
	 * 
	 * @param p
	 *            the point where the returning connection is located
	 * @return
	 */
    public GraphicalConnection getConnectionAt(Point p) {
        FreeSpaceReservingUnit unit = getUnitContainingPoint(p);
        for (GraphicalConnectionSegment seg : unit.getContainingConnectionSegments()) {
            if (seg.isLocatedAt(p)) return seg.getConnection();
        }
        for (GraphicalLogicDevice device : unit.getContainingDevices()) {
            GraphicalConnection conn = device.getPseudoConnectionAt(p);
            if (conn != null) return conn;
        }
        return null;
    }

    /**
	 * Resets the Panel. This will erase all data added to it
	 */
    public void resetPanel() {
        resetReservingUnits();
        inputs = new ArrayList<GraphicalInputDevice>();
        outputs = new ArrayList<GraphicalOutputDevice>();
    }

    /**
	 * (re-)set all the reserving units
	 */
    private void resetReservingUnits() {
        int reservingUnitsXCount = (getPanelWidth() / FreeSpaceReservingUnit.UNIT_WIDTH) + 1;
        int reservingUnitsYCount = (getPanelHeight() / FreeSpaceReservingUnit.UNIT_HEIGHT) + 1;
        reservingUnits = new FreeSpaceReservingUnit[reservingUnitsXCount][reservingUnitsYCount];
        for (int i = 0; i < reservingUnitsXCount; i++) {
            for (int j = 0; j < reservingUnitsYCount; j++) {
                reservingUnits[i][j] = new FreeSpaceReservingUnit(i * FreeSpaceReservingUnit.UNIT_WIDTH, j * FreeSpaceReservingUnit.UNIT_HEIGHT);
            }
        }
    }

    /**
	 * clears the data of all reserving units
	 * 
	 * @return the {@link GraphicalGateDevice}s which where registered in the
	 *         reservingUnits
	 */
    private Set<GraphicalGateDevice> clearReservingUnits() {
        Set<GraphicalGateDevice> unregisteredGates = new HashSet<GraphicalGateDevice>();
        for (int i = 0; i < reservingUnits.length; i++) {
            for (int j = 0; j < reservingUnits[i].length; j++) {
                for (GraphicalLogicDevice device : reservingUnits[i][j].getContainingDevices()) {
                    if (device instanceof GraphicalGateDevice) unregisteredGates.add((GraphicalGateDevice) device);
                }
                reservingUnits[i][j].clear();
            }
        }
        return unregisteredGates;
    }

    public Rectangle getFreeSpace() {
        return freeSpace;
    }

    public List<LogicFormula> getOutputFormulas() {
        for (GraphicalInputDevice input : inputs) {
            input.assignFormula();
        }
        List<LogicFormula> formulas = new ArrayList<LogicFormula>();
        for (GraphicalOutputDevice output : outputs) {
            try {
                LogicFormula formula = output.getFormula();
                if (formula != null) formulas.add(formula);
            } catch (Exception e) {
            }
        }
        return formulas;
    }

    /**
	 * add the formulas managed by the passed {@link FormulaTool} to the panel.
	 * Please note, that this function may move the gates of the existing
	 * formulas {@link FormulaTool#optimizeFormulas()} Please note that this
	 * function will neither add the existing formulas to the
	 * {@link FormulaTool} nor use {@link FormulaTool#optimizeFormulas()} to
	 * optimize the used space. If this is wanted use this functionality
	 * yourself.
	 * 
	 * @return the list of all gates which have been placed on the panel
	 * 
	 * @param tool
	 *            the formulaTool containing the formulas
	 */
    public List<GraphicalLogicDevice> addFormulasUsingFormulaTool(FormulaTool tool) {
        List<LogicFormula> outputFormulas = getOutputFormulas();
        Iterator<LogicFormula> formulaIt = tool.getFormulas().iterator();
        Set<LogicVariable> usedVariables = formulaIt.next().getUsedVariables();
        while (formulaIt.hasNext()) {
            usedVariables.addAll(formulaIt.next().getUsedVariables());
        }
        formulaIt = outputFormulas.iterator();
        while (formulaIt.hasNext()) {
            usedVariables.addAll(formulaIt.next().getUsedVariables());
        }
        if (usedVariables.size() > 4) throw new IllegalArgumentException(translationTool.getText("NoMoreInput"));
        if (tool.getFormulas().size() + outputFormulas.size() > 4) throw new IllegalArgumentException(translationTool.getText("NoMoreOutput"));
        List<GraphicalInputDevice> oldInputs = new ArrayList<GraphicalInputDevice>();
        Iterator<GraphicalInputDevice> inputsIt = inputs.iterator();
        while (inputsIt.hasNext()) {
            GraphicalInputDevice input = inputsIt.next();
            if (!usedVariables.contains(LogicVariable.getLogicVariable(input.getVariable()))) {
                unregisterDevice(input);
                inputsIt.remove();
                oldInputs.add(input);
            }
        }
        List<GraphicalInputDevice> newInputs = new ArrayList<GraphicalInputDevice>();
        for (LogicVariable var : usedVariables) {
            GraphicalInputDevice input = GraphicalInputDevice.getInput(assignedPanel, var.getVariable());
            if (!inputs.contains(input)) {
                newInputs.add(input);
                addNewInput(input);
            }
            input.assignFormula();
        }
        List<List<GraphicalLogicDevice>> gatesByDepths = null;
        List<GraphicalLogicDeviceConnection> newConnections = new ArrayList<GraphicalLogicDeviceConnection>();
        try {
            gatesByDepths = distributeGatesOntoDepths(tool, newConnections);
        } catch (RuntimeException ex) {
            for (GraphicalLogicDeviceConnection con : newConnections) {
                con.delete();
            }
            inputsIt = newInputs.iterator();
            while (inputsIt.hasNext()) {
                GraphicalInputDevice input = inputsIt.next();
                unregisterDevice(input);
                inputs.remove(input);
            }
            inputsIt = oldInputs.iterator();
            resetReservedPanelBorders(true);
            while (inputsIt.hasNext()) {
                GraphicalInputDevice input = inputsIt.next();
                registerDevice(input);
                inputs.add(input);
            }
            resetReservedPanelBorders(false);
            arrangeInputs();
            throw ex;
        }
        Set<GraphicalGateDevice> oldDevices = clearReservingUnits();
        List<GraphicalLogicDevice> placedDevices = new ArrayList<GraphicalLogicDevice>();
        for (int i = 0; i < gatesByDepths.size(); i++) {
            List<GraphicalLogicDevice> gatesAtDepth = gatesByDepths.get(i);
            for (int j = 0; j < gatesAtDepth.size(); j++) {
                GraphicalLogicDevice gate = gatesAtDepth.get(j);
                int x = getFreeSpace().getLeftBorder() + ((getFreeSpace().getWidth() / gatesByDepths.size()) * (2 * i + 1)) / 2;
                int y = getFreeSpace().getTopBorder() + ((getFreeSpace().getHeight() / gatesAtDepth.size()) * (2 * j + 1)) / 2;
                gate.setCenterLocation(new Point(x, y));
                registerDevice(gate);
                for (GraphicalLogicDeviceConnection con : gate.getIncomingConnections()) {
                    clearConnection(con);
                    calculateConnectionVertexes(con);
                }
                placedDevices.add(gate);
            }
        }
        arrangeInputs();
        for (GraphicalGateDevice oldDevice : oldDevices) {
            if (!placedDevices.contains(oldDevice)) {
                deleteDeviceWithoutUnregistering(oldDevice);
            }
        }
        resetReservedPanelBorders(true);
        for (int i = 0; i < outputs.size(); i++) {
            GraphicalOutputDevice output = outputs.get(i);
            try {
                output.getTruthValue();
                registerDevice(output);
            } catch (Exception e) {
                unregisterDevice(output);
                outputs.remove(i);
                i--;
            }
        }
        resetReservedPanelBorders(false);
        resetReservedPanelBorders(true);
        for (LogicFormula formula : tool.getFormulas()) {
            GraphicalOutputDevice output = addNewOutput();
            connectLogicDevices(formula.getAssignedLogicDevice(), output);
        }
        return placedDevices;
    }

    /**
	 * Adds the formula to the passed gatesByDepth list. If it has already been
	 * added (listed in the alreadyAddedDevices) or is a {@link LogicVariable}
	 * nothing will be done. Otherwise all Sub-formulas will be recursively
	 * added and afterwards the gate assigned to the formula will be added to
	 * the list at the best possible place. If the formula did not have an
	 * assignedGate yet it will be created
	 * 
	 * The place-finding algorithm for a gate includes: At first it tries to add
	 * the gate at its preferred level {@link LogicFormula#getDepth()}. If this
	 * level is already full it will be added in a following level which still
	 * has free space. If the gate should be added at the maxDepth-level and no
	 * place is found, an {@link IndexOutOfBoundsException} will be thrown as
	 * the gate cannot be added to a valid place.
	 * 
	 * @param formula
	 *            the formula whose gate should be added
	 * @param gatesByDepths
	 *            the list where the gate should be added
	 * @param alreadyAddedDevices
	 *            a set of all gates in gatesByDepths. This is just needed for
	 *            better access. If this function adds a gate to gatesByDepth
	 *            this will be updated, too.
	 * @param maxDepth
	 *            size of the outer List
	 * @param maxGatesPerDepth
	 *            size of the inner list
	 * @param newConnections
	 *            this List can be used for errer states as it contains the
	 *            added connection even when an exception is thrown
	 * @throws IndexOutOfBoundsException
	 *             if the List cannot be created with the passed parameters
	 */
    private void addFormulaGatesToDepthList(LogicFormula formula, List<List<GraphicalLogicDevice>> gatesByDepths, Set<GraphicalGateDevice> alreadyAddedDevices, int maxDepth, int maxGatesPerDepth, List<GraphicalLogicDeviceConnection> newConnections) {
        if (formula instanceof LogicVariable) return;
        LogicSubFormula formulaAsSub = (LogicSubFormula) formula;
        for (LogicFormula subformula : formulaAsSub.getSubFormulas()) {
            addFormulaGatesToDepthList(subformula, gatesByDepths, alreadyAddedDevices, maxDepth, maxGatesPerDepth, newConnections);
        }
        GraphicalGateDevice assignedDevice = (GraphicalGateDevice) formulaAsSub.getAssignedLogicDevice();
        if (assignedDevice == null) {
            assignedDevice = new GraphicalGateDevice(formulaAsSub.getGateType(), getLogger());
            formula.setAssignedLogicDevice(assignedDevice);
            for (LogicFormula subformula : formulaAsSub.getSubFormulas()) {
                newConnections.add(new GraphicalLogicDeviceConnection(subformula.getAssignedLogicDevice(), assignedDevice));
            }
        } else if (alreadyAddedDevices.contains(assignedDevice)) {
            return;
        }
        for (int depth = formulaAsSub.getDepth() - 1; depth < maxDepth; depth++) {
            List<GraphicalLogicDevice> gatesAtDepth = gatesByDepths.get(depth);
            if (gatesAtDepth.size() < maxGatesPerDepth) {
                gatesAtDepth.add(gatesAtDepth.size(), assignedDevice);
                alreadyAddedDevices.add(assignedDevice);
                return;
            }
        }
        throw new IndexOutOfBoundsException(translationTool.getText("GatesListTooSmall"));
    }

    /**
	 * Creates a 2 dimensional list containing all gates at the depth where they
	 * should be added to.
	 * 
	 * @param tool
	 *            the tool containing the formulas whose gates should be
	 *            distributed
	 * @param newConnections
	 *            this List can be used for errer states as it contains the
	 *            added connection even when an exception is thrown
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             if the list cannot be created for the panel without the gates
	 *             being place too close to each other.
	 */
    private List<List<GraphicalLogicDevice>> distributeGatesOntoDepths(FormulaTool tool, List<GraphicalLogicDeviceConnection> newConnections) {
        List<List<GraphicalLogicDevice>> gatesByDepths = null;
        int maxDepth = tool.getMaxDepth(true);
        int maxGatesPerDepth = getFreeSpace().getHeight() / (GraphicalGateDevice.maxGateHeight + GraphicalLogicDevice.DEVICE_DEVICE_PADDING + 20);
        int maxPossibleDepth = getFreeSpace().getWidth() / (GraphicalGateDevice.maxGateWidth + GraphicalLogicDevice.DEVICE_DEVICE_PADDING + 20);
        List<LogicFormula> outputFormulas = getOutputFormulas();
        while (maxDepth < maxPossibleDepth) {
            gatesByDepths = new ArrayList<List<GraphicalLogicDevice>>();
            Set<GraphicalGateDevice> alreadyAddedDevices = new HashSet<GraphicalGateDevice>();
            for (int i = 0; i < maxDepth; i++) {
                gatesByDepths.add(i, new ArrayList<GraphicalLogicDevice>());
            }
            try {
                for (LogicFormula formula : outputFormulas) {
                    addFormulaGatesToDepthList(formula, gatesByDepths, alreadyAddedDevices, maxDepth, maxGatesPerDepth, newConnections);
                }
                for (LogicFormula formula : tool.getFormulas()) {
                    addFormulaGatesToDepthList(formula, gatesByDepths, alreadyAddedDevices, maxDepth, maxGatesPerDepth, newConnections);
                }
                return gatesByDepths;
            } catch (IndexOutOfBoundsException e) {
                maxDepth++;
            }
        }
        throw new IndexOutOfBoundsException(translationTool.getText("GatesNotFitToPanel"));
    }

    /**
	 * paints the reserving Space using the passed {@link Graphics2D}-object if
	 * the parameter movingState is true. Furthermore it paints the
	 * {@link GraphicalInputDevice}s and {@link GraphicalOutputDevice}s as they
	 * are managed here. Be sure to call this paint-operation as soon as
	 * possible while painting because this method might override already
	 * painted items in the panel
	 * 
	 * @param g
	 * @param movingState
	 *            this indicates if the Panel is currently in the state
	 *            {@value LogicCircuitPanel#STATE_MOVING_DEVICE}
	 */
    public void paint(Graphics2D g, boolean movingState, boolean paintTruthValue, Set<GraphicalLogicDeviceConnection> connectionPaintSpooler) {
        if (movingState) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getPanelWidth(), getPanelHeight());
            g.setColor(Color.WHITE);
            g.fillRect(freeSpace.getLeftBorder(), freeSpace.getTopBorder(), freeSpace.getWidth(), freeSpace.getHeight());
        }
        for (GraphicalOutputDevice output : outputs) output.paint(g, paintTruthValue, connectionPaintSpooler);
        for (GraphicalInputDevice input : inputs) input.paint(g, paintTruthValue, connectionPaintSpooler);
    }

    /**
	 * This function is used to recalculate the Panel border where no Element
	 * can be set. If the parameter noBoder is set their won't be any
	 * ReservingBorders. This function is normally called when resizing the
	 * Panel or for adding {@link GraphicalInputDevice}s and
	 * {@link GraphicalOutputDevice}s because they will be added at a location
	 * where is normally the ReservingBorder.
	 * 
	 * @param noBorder
	 */
    private void resetReservedPanelBorders(boolean noBorder) {
        if (noBorder) {
            freeSpace = new Rectangle(0, 0, getPanelWidth(), getPanelHeight());
        } else {
            freeSpace = new Rectangle(250, 50, getPanelWidth() - 120, getPanelHeight() - 50);
        }
    }

    /**
	 * Calculate a path from
	 * {@link GraphicalLogicDeviceConnection#getConnectionBegin()} to
	 * {@link GraphicalLogicDeviceConnection#getConnectionEnd()} that causes no
	 * collisions. The calculated path is saved in the passed connection and the
	 * needed space will be reserved. If a path has already been calculated the
	 * old path will be overwritten.
	 * 
	 * @param connection
	 *            the connection for which a (new) path is calculated
	 */
    private void calculateConnectionVertexes(GraphicalLogicDeviceConnection connection) throws IllegalArgumentException {
        if (connection.getVertexes() != null) clearConnection(connection);
        Point start = connection.getConnectionBegin().getOutputLocation();
        Point end = connection.getConnectionEnd().getInputLocationOfConnection(connection);
        Point startOutsideDevice = connection.getConnectionBegin().getCollisionFreeOutputLocation();
        Point endOutsideDevice = connection.getConnectionEnd().getCollisionFreeInputLocationOfConnection(connection);
        RatedVertexes vertexes = null;
        if (connection.getConnectionBegin() instanceof GraphicalInputDevice) vertexes = calculateConnectionVertexesVertical(startOutsideDevice, endOutsideDevice, connection, RatedVertexes.MAX_OVERFLOW_COUNT, 0); else {
            vertexes = calculateConnectionVertexesHorizontal(startOutsideDevice, endOutsideDevice, connection, RatedVertexes.MAX_OVERFLOW_COUNT, 0);
        }
        if (vertexes == null) {
            getLogger().warning(translationTool.getText("ConnNoCalc", connection.toString()));
            return;
        }
        vertexes.add(start);
        vertexes.addToFront(end);
        connection.setVertexes(vertexes.getVertexes());
        for (GraphicalConnectionSegment seg : connection.getSegments()) {
            for (FreeSpaceReservingUnit unit : getUnitsOverlapingWithRectangle(seg.getReservingRectangle())) {
                unit.addContainingConnectionSegment(seg);
            }
        }
    }

    /**
	 * Calculate a collision-free path from the passed variable start to the
	 * passed variable end beginning with an horizontal line.
	 * 
	 * @param start
	 *            start of the segment
	 * @param end
	 *            end of the connection
	 * @param targetConnection
	 *            the connection for which the vertexes should be calculated
	 * @param overflowsLeft
	 *            maximal count of overflows that can be added to the returned
	 *            {@link RatedVertexes}
	 * @param SegmentDepth
	 *            depth of the segment. This is used to ensure that the
	 *            connection isn't split into too many segments
	 * @return the calculated vertexes for the collision free path or null if a
	 *         path could not be created the path does NOT include the start
	 *         position and is inverse speaking the end is the first element in
	 *         the list
	 */
    private RatedVertexes calculateConnectionVertexesHorizontal(Point start, Point end, GraphicalLogicDeviceConnection targetConnection, int overflowsLeft, int segmentDepth) {
        int xDifference = end.x - start.x;
        if (xDifference == 0) return null;
        int directionModifier = 1;
        if (xDifference < 0) directionModifier = -1;
        int maxLength = getMaxLengthForCollisionFreeLine(start, xDifference, true, targetConnection);
        int currentDefiance = 0;
        if (start.y == end.y) {
            if (xDifference == maxLength * directionModifier) {
                RatedVertexes vertexes = new RatedVertexes();
                return vertexes;
            }
        }
        if (segmentDepth >= FreeSpaceManager.MAX_SEGMENTS_COUNT - 2) return null;
        int optimalLength = 0;
        if (directionModifier == 1) optimalLength = Math.min(xDifference / 2, maxLength); else optimalLength = maxLength;
        RatedVertexes bestYetFoundConnection = null;
        Point testPoint = new Point(start.x + optimalLength * directionModifier, start.y);
        RatedVertexes vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
        if (vertexes != null) {
            vertexes.add(testPoint);
            if (vertexes.size() == 2) return vertexes;
            bestYetFoundConnection = vertexes;
            overflowsLeft = vertexes.getUsedOverflows();
        }
        currentDefiance += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
        while ((optimalLength + currentDefiance) < maxLength) {
            testPoint = new Point(start.x + (optimalLength - currentDefiance) * directionModifier, start.y);
            vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
            if (vertexes != null) {
                vertexes.add(testPoint);
                if (vertexes.size() == 2) return vertexes;
                if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                    bestYetFoundConnection = vertexes;
                    overflowsLeft = vertexes.getUsedOverflows();
                }
            }
            testPoint = new Point(start.x + (optimalLength + currentDefiance) * directionModifier, start.y);
            vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
            if (vertexes != null) {
                vertexes.add(testPoint);
                if (vertexes.size() == 2) return vertexes;
                if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                    bestYetFoundConnection = vertexes;
                    overflowsLeft = vertexes.getUsedOverflows();
                }
            }
            currentDefiance += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
        }
        while (optimalLength - currentDefiance > 0) {
            testPoint = new Point(start.x + (optimalLength - currentDefiance) * directionModifier, start.y);
            vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
            if (vertexes != null) {
                vertexes.add(testPoint);
                if (vertexes.size() == 2) return vertexes;
                if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                    bestYetFoundConnection = vertexes;
                    overflowsLeft = vertexes.getUsedOverflows();
                }
            }
            currentDefiance += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
        }
        if (overflowsLeft > 0) {
            if (xDifference == maxLength * directionModifier) {
                Point endPoint = new Point(start.x + xDifference, start.y);
                int maxOverlowLength = getMaxLengthForCollisionFreeLine(endPoint, overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW * directionModifier, true, targetConnection);
                int currentOverflow = FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
                while (currentOverflow < overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW && currentOverflow <= maxOverlowLength) {
                    testPoint = new Point(end.x + currentOverflow * directionModifier, start.y);
                    vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
                    if (vertexes != null) {
                        vertexes.add(testPoint);
                        if (vertexes.size() == 2) return vertexes;
                        if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                            bestYetFoundConnection = vertexes;
                            overflowsLeft = vertexes.getUsedOverflows() + (currentOverflow - 1) / RatedVertexes.MAX_LENGTH_PER_OVERFLOW + 1;
                        }
                    }
                    currentOverflow += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
                }
            }
            int maxNegativeOverflowLength = getMaxLengthForCollisionFreeLine(start, -overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW * directionModifier, true, targetConnection);
            int currentOverflow = FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
            while (currentOverflow < overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW && currentOverflow <= maxNegativeOverflowLength) {
                testPoint = new Point(start.x - currentOverflow * directionModifier, start.y);
                vertexes = calculateConnectionVertexesVertical(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
                if (vertexes != null) {
                    vertexes.add(testPoint);
                    if (vertexes.size() == 2) return vertexes;
                    if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                        bestYetFoundConnection = vertexes;
                        overflowsLeft = vertexes.getUsedOverflows() + (currentOverflow - 1) / RatedVertexes.MAX_LENGTH_PER_OVERFLOW + 1;
                    }
                }
                currentOverflow += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
            }
        }
        return bestYetFoundConnection;
    }

    /**
	 * Calculate a collision-free path from the passed variable start to the
	 * passed variable end beginning with an vertical line
	 * 
	 * @param start
	 *            start of the segment
	 * @param end
	 *            end of the connection
	 * @param targetConnection
	 *            the connection for which the vertexes should be calculated
	 * @param overflowsLeft
	 *            maximal count of overflows that can be added to the returned
	 *            {@link RatedVertexes}
	 * @param segmentDepth
	 *            depth of the segment. This is used to ensure that the
	 *            connection isn't split into too many segments
	 * @return the calculated vertexes for the collision free path or null if a
	 *         path could not be created the path does NOT include the start
	 *         position and is inverse speaking the end is the first element in
	 *         the list
	 */
    private RatedVertexes calculateConnectionVertexesVertical(Point start, Point end, GraphicalLogicDeviceConnection targetConnection, int overflowsLeft, int segmentDepth) {
        int yDifference = end.y - start.y;
        int directionModifier = 1;
        if (yDifference < 0) directionModifier = -1;
        int maxLength = getMaxLengthForCollisionFreeLine(start, yDifference, false, targetConnection);
        int currentlyTestedLength = maxLength;
        RatedVertexes bestYetFoundConnection = null;
        if (maxLength * directionModifier == yDifference) if (start.x == end.x) {
            RatedVertexes vertexes = new RatedVertexes();
            vertexes.add(end);
            return vertexes;
        } else {
            Point testPoint = new Point(start.x, end.y);
            RatedVertexes vertexes = calculateConnectionVertexesHorizontal(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
            if (vertexes != null) {
                vertexes.add(testPoint);
                if (vertexes.size() == 1) return vertexes;
                bestYetFoundConnection = vertexes;
                overflowsLeft = vertexes.getUsedOverflows();
            }
            currentlyTestedLength -= FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
        }
        while (currentlyTestedLength > 0) {
            Point testPoint = new Point(start.x, start.y + currentlyTestedLength * directionModifier);
            RatedVertexes vertexes = calculateConnectionVertexesHorizontal(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
            if (vertexes != null) {
                vertexes.add(testPoint);
                if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                    bestYetFoundConnection = vertexes;
                    overflowsLeft = vertexes.getUsedOverflows();
                }
            }
            currentlyTestedLength -= FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
        }
        if (overflowsLeft > 0) {
            if (yDifference == maxLength * directionModifier) {
                Point endPoint = new Point(start.x, start.y + yDifference);
                int maxOverlowLength = getMaxLengthForCollisionFreeLine(endPoint, overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW * directionModifier, false, targetConnection);
                int currentOverflow = FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
                while (currentOverflow < overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW && currentOverflow <= maxOverlowLength) {
                    Point testPoint = new Point(start.x, end.y + currentOverflow * directionModifier);
                    RatedVertexes vertexes = calculateConnectionVertexesHorizontal(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
                    if (vertexes != null) {
                        vertexes.add(testPoint);
                        if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                            bestYetFoundConnection = vertexes;
                            overflowsLeft = vertexes.getUsedOverflows() + (currentOverflow - 1) / RatedVertexes.MAX_LENGTH_PER_OVERFLOW + 1;
                        }
                    }
                    currentOverflow += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
                }
            }
            int maxNegativeOverflowLength = getMaxLengthForCollisionFreeLine(start, -overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW * directionModifier, false, targetConnection);
            int currentOverflow = FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
            while (currentOverflow < overflowsLeft * RatedVertexes.MAX_LENGTH_PER_OVERFLOW && currentOverflow <= maxNegativeOverflowLength) {
                Point testPoint = new Point(start.x, start.y - currentOverflow * directionModifier);
                RatedVertexes vertexes = calculateConnectionVertexesHorizontal(testPoint, end, targetConnection, overflowsLeft, segmentDepth + 1);
                if (vertexes != null) {
                    vertexes.add(testPoint);
                    if (bestYetFoundConnection == null || vertexes.getUsedOverflows() < overflowsLeft || vertexes.size() < bestYetFoundConnection.size()) {
                        bestYetFoundConnection = vertexes;
                        overflowsLeft = vertexes.getUsedOverflows() + (currentOverflow - 1) / RatedVertexes.MAX_LENGTH_PER_OVERFLOW + 1;
                    }
                }
                currentOverflow += FreeSpaceManager.CONNECTION_CALCULATION_STEPLENGTH;
            }
        }
        return bestYetFoundConnection;
    }

    /**
	 * calculate the maximal possible length of a line that starts in the passed
	 * variable start. a line is either horizontal (horizontal=true) or vertical
	 * (horizontal=false) and has a maximal length that is reasonable for the
	 * line.
	 * 
	 * @param Start
	 * @param maxLenght
	 * @param horizontal
	 * @param targetConnection
	 *            the connection this line is about to be a part of
	 * @return the maximum length. This value will always be positive
	 */
    private int getMaxLengthForCollisionFreeLine(Point start, int maxLenght, boolean horizontal, GraphicalLogicDeviceConnection targetConnection) {
        AxisParallelLine line = getAxisParallelLineWithinPanel(start, maxLenght, horizontal);
        Rectangle r = line.getSurroundingRectangle();
        List<FreeSpaceReservingUnit> overlappingUnits = getUnitsOverlapingWithRectangle(r);
        Set<GraphicalLogicDevice> potentiallyCollidingDevices = new HashSet<GraphicalLogicDevice>();
        Set<GraphicalConnectionSegment> potentiallyCollidingConnectionSegments = new HashSet<GraphicalConnectionSegment>();
        for (FreeSpaceReservingUnit u : overlappingUnits) {
            potentiallyCollidingDevices.addAll(u.getContainingDevices());
            potentiallyCollidingConnectionSegments.addAll(u.getContainingConnectionSegments());
        }
        List<GraphicalLogicDevice> devices = new ArrayList<GraphicalLogicDevice>();
        devices.addAll(potentiallyCollidingDevices);
        List<GraphicalConnectionSegment> segments = new ArrayList<GraphicalConnectionSegment>();
        segments.addAll(potentiallyCollidingConnectionSegments);
        try {
            line.shortenLineToAvoidCollisions(devices, segments, targetConnection);
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return line.getLength();
    }

    /**
	 * Gets an AxisParallelLine starting at start with that is
	 * horizontal(horizonal=true) or vertical (horizontal=false) that is
	 * completely inside the panel and does not exceed the maxLength
	 * 
	 * @param start
	 * @param maxLenght
	 * @param horizontal
	 * @return
	 */
    private AxisParallelLine getAxisParallelLineWithinPanel(Point start, int maxLenght, boolean horizontal) {
        AxisParallelLine line = null;
        if (maxLenght > 0) {
            if (horizontal) maxLenght = Math.min(maxLenght, getPanelWidth() - start.x - GraphicalLogicDeviceConnection.CONNECTION_CONNECTION_PADDING); else maxLenght = Math.min(maxLenght, getPanelHeight() - start.y - GraphicalLogicDeviceConnection.CONNECTION_CONNECTION_PADDING);
            line = new AxisParallelLine(start, maxLenght, horizontal, 1);
        } else {
            if (horizontal) maxLenght = Math.max(maxLenght, GraphicalLogicDeviceConnection.CONNECTION_CONNECTION_PADDING - start.x); else maxLenght = Math.max(maxLenght, GraphicalLogicDeviceConnection.CONNECTION_CONNECTION_PADDING - start.y);
            line = new AxisParallelLine(start, -maxLenght, horizontal, -1);
        }
        return line;
    }

    /**
	 * assert that a rectangle is within the {@link Rectangle}
	 * {@link #freeSpace}
	 * 
	 * @param rect
	 *            the rectangle to be looked at
	 * @throws OutOfBoundsException
	 *             if the rectangle exceeds the main panel's borders
	 */
    private void assertCompletelyInsideFreeSpace(Rectangle rect) throws OutOfBoundsException {
        if ((rect.getLeftBorder() < freeSpace.getLeftBorder()) || (rect.getRightBorder() > freeSpace.getRightBorder()) || (rect.getTopBorder() < freeSpace.getTopBorder()) || (rect.getBottomBorder() > freeSpace.getBottomBorder())) throw new OutOfBoundsException(translationTool.getText("RectOutside", rect.toString(), freeSpace.toString()));
    }

    /**
	 * assert that a rectangle is within the Panel. In Contrast to
	 * {@link #assertCompletelyInsideFreeSpace(Rectangle)} this function does
	 * take the reserving borders into account
	 * 
	 * @param rect
	 *            the rectangle to be looked at
	 * @throws OutOfBoundsException
	 *             if the rectangle exceeds the main panel's borders
	 */
    private void assertCompletelyInsidePanel(Rectangle rect) throws OutOfBoundsException {
        rect.cutToFitToPanel(getPanelWidth(), getPanelHeight());
    }

    /**
	 * assert that the passed point is within the Panel. In Contrast to
	 * {@link #assertInFreeSpace(Point)} this function does take the reserving
	 * borders into account
	 * 
	 * @param point
	 *            the point to be looked at
	 * @throws OutOfBoundsException
	 *             if the point is outside the main panel's borders
	 */
    private void assertInPanel(Point p) throws OutOfBoundsException {
        if ((p.x < 0) || (p.x > getPanelWidth()) || (p.y < 0) || (p.y > getPanelHeight())) throw new OutOfBoundsException(translationTool.getText("PointOutside", p.toString()));
    }

    /**
	 * Get all {@link FreeSpaceReservingUnit} that overlap with the passed
	 * rectangle.
	 * 
	 * @param rect
	 *            the rectangle to be looked at
	 * @return a list of {@link FreeSpaceReservingUnit}s overlapping with the
	 *         passed rectangle
	 * @throws OutOfBoundsException
	 *             if the rectangle exceeds the main panel's borders
	 */
    private List<FreeSpaceReservingUnit> getUnitsOverlapingWithRectangle(Rectangle rect) throws OutOfBoundsException {
        assertCompletelyInsidePanel(rect);
        int mostLefttUnitContainingRectangle = rect.getLeftBorder() / FreeSpaceReservingUnit.UNIT_WIDTH;
        int mostRightUnitContainingRectangle = rect.getRightBorder() / FreeSpaceReservingUnit.UNIT_WIDTH;
        int highestUnitContainingRectangle = rect.getTopBorder() / FreeSpaceReservingUnit.UNIT_HEIGHT;
        int lowestUnitContainingRectangle = rect.getBottomBorder() / FreeSpaceReservingUnit.UNIT_HEIGHT;
        List<FreeSpaceReservingUnit> overlappingUnits = new ArrayList<FreeSpaceReservingUnit>();
        for (int i = mostLefttUnitContainingRectangle; i <= mostRightUnitContainingRectangle; i++) {
            for (int j = highestUnitContainingRectangle; j <= lowestUnitContainingRectangle; j++) {
                overlappingUnits.add(reservingUnits[i][j]);
            }
        }
        return overlappingUnits;
    }

    /**
	 * Get the {@link FreeSpaceReservingUnit} that contains the passed point.
	 * 
	 * @param point
	 *            the point to be looked at
	 * @return the {@link FreeSpaceReservingUnit} the passed point is in
	 * @throws OutOfBoundsException
	 *             if the point is outside the main panel's borders
	 */
    private FreeSpaceReservingUnit getUnitContainingPoint(Point p) throws OutOfBoundsException {
        assertInPanel(p);
        int ReservingUnitX = p.x / FreeSpaceReservingUnit.UNIT_WIDTH;
        int ReservingUnitY = p.y / FreeSpaceReservingUnit.UNIT_WIDTH;
        return reservingUnits[ReservingUnitX][ReservingUnitY];
    }

    /**
	 * deletes all devices which are not connected to a valid output
	 */
    public void deleteUnboundDevices(List<GraphicalLogicDevice> devicesToBeChecked) {
        for (int i = 0; i < outputs.size(); i++) {
            GraphicalOutputDevice output = outputs.get(i);
            try {
                output.getTruthValue();
            } catch (IllegalStateException e) {
                deleteOutput(output, true);
                i--;
            }
        }
        for (int i = 0; i < devicesToBeChecked.size(); i++) {
            GraphicalLogicDevice device = devicesToBeChecked.get(i);
            try {
                device.getTruthValue();
                if (device.getOutgoingConnections().size() == 0) {
                    List<GraphicalLogicDevice> inputConnectedTo = device.getInputDevices();
                    deleteDevice(device);
                    deleteUnboundDevices(inputConnectedTo);
                    i--;
                }
            } catch (IllegalStateException e) {
                List<GraphicalLogicDevice> inputConnectedTo = device.getInputDevices();
                deleteDevice(device);
                deleteUnboundDevices(inputConnectedTo);
                i--;
            }
        }
        for (int i = 0; i < inputs.size(); i++) {
            GraphicalInputDevice input = inputs.get(i);
            if (input.getOutgoingConnections().size() == 0) {
                deleteInput(input, true);
                i--;
            }
        }
    }

    private int getPanelWidth() {
        return assignedPanel.getWidth();
    }

    private int getPanelHeight() {
        return assignedPanel.getHeight();
    }

    public Logger getLogger() {
        return assignedPanel.getLogger();
    }
}
