package desmoj.extensions.space3D;

import java.util.HashMap;
import java.util.Set;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.extensions.visualEvents.CreateVisibleObjectEvent;
import desmoj.extensions.visualEvents.RemoveEvent;
import desmoj.extensions.visualEvents.RotateEvent;
import desmoj.extensions.visualEvents.SetOrientationEvent;
import desmoj.extensions.visualEvents.SetPositionEvent;
import desmoj.extensions.visualEvents.SetVisibleEvent;
import desmoj.extensions.visualEvents.VisualEventTransmitter;

/**
 * This class represents the DESMO-J SimProcess which should be visualized
 * but not movable in the 3D space.
 * It implements the SpatialObject interface to handle with the 3D spatial
 * data.
 * 
 * @author Fred Sun
 *
 */
public abstract class SpatialSimProcess extends SimProcess implements SpatialObject, VisibleObject {

    private HashMap<String, Point3d> _entryPoints;

    private VisualEventTransmitter _eventTransmitter;

    private HashMap<String, Point3d> _exitPoints;

    protected SpatialData _spatialData;

    private String _visualModel;

    private boolean _isVisible;

    /**
	 * Constructs a SpatialSimProcess
	 * @param owner The model this entity is associated to.
	 * @param name The name of the process.
	 * @param visualModel The visual model attribute belongs to the SpatialObject interface.
	 * @param showInTrace Flag for showing entity in trace-files. Set it to true if entity should show up in trace. Set to false in entity should not be shown in trace.
	 */
    public SpatialSimProcess(Model owner, String name, String visualModel, boolean showInTrace) {
        super(owner, name, showInTrace);
        _spatialData = new SpatialData();
        _eventTransmitter = VisualEventTransmitter.getVisualEventTransmitter();
        _eventTransmitter.fireVisualEvent(new CreateVisibleObjectEvent(this, this.getName(), visualModel, this.isMovable(), this.presentTime()));
        _entryPoints = new HashMap<String, Point3d>(5, 0.9f);
        _exitPoints = new HashMap<String, Point3d>(5, 0.9f);
        _visualModel = visualModel;
        _isVisible = true;
    }

    /**
	 * Constructs a SpatialSimProcess with specific start position.
	 * @param owner The model this process is associated to.
	 * @param name The name of the process.
	 * @param visualModel The visual model attribute belongs to the SpatialObject interface.
	 * @param showInTrace Flag for showing entity in trace-files. Set it to true if entity should show up in trace. Set to false in entity should not be shown in trace.
	 * @param startPositionX The x start position in ExtendedLength.
	 * @param startPositionY The y start position in ExtendedLength.
	 * @param startPositionZ The z start position in ExtendedLength.
	 */
    public SpatialSimProcess(Model owner, String name, String visualModel, boolean showInTrace, ExtendedLength startPositionX, ExtendedLength startPositionY, ExtendedLength startPositionZ) {
        super(owner, name, showInTrace);
        _eventTransmitter = VisualEventTransmitter.getVisualEventTransmitter();
        if (startPositionX != null && startPositionY != null && startPositionZ != null) {
            double[] spatialMatrix = new double[16];
            spatialMatrix[3] = startPositionX.getValue(6);
            spatialMatrix[7] = startPositionY.getValue(6);
            spatialMatrix[11] = startPositionZ.getValue(6);
            _spatialData = new SpatialData(spatialMatrix);
        } else {
            _spatialData = new SpatialData();
            this.sendWarning("The start position isn't specified enough.", this.toString() + ": Constructor(Model, String, boolean," + " ExtendedLength, ExtendedLength, ExtendedLength)", "One or more ExtendedLength is null", "Please recheck the code");
        }
        _eventTransmitter.fireVisualEvent(new CreateVisibleObjectEvent(this, this.getName(), visualModel, this.isMovable(), this.presentTime()));
        _eventTransmitter.fireVisualEvent(new SetPositionEvent(this, this.getName(), _spatialData.getPosX(), _spatialData.getPosY(), _spatialData.getPosZ(), this.presentTime()));
        _entryPoints = new HashMap<String, Point3d>(5, 0.9f);
        _exitPoints = new HashMap<String, Point3d>(5, 0.9f);
        _visualModel = visualModel;
        _isVisible = true;
    }

    public void addEntryPoint(String name, ExtendedLength xPos, ExtendedLength yPos, ExtendedLength zPos) {
        double[] position = new double[3];
        position[0] = xPos.getValue(6);
        position[1] = yPos.getValue(6);
        position[2] = zPos.getValue(6);
        _entryPoints.put(name, new Point3d(position));
    }

    public void addExitPoint(String name, ExtendedLength xPos, ExtendedLength yPos, ExtendedLength zPos) {
        double[] position = new double[3];
        position[0] = xPos.getValue(6);
        position[1] = yPos.getValue(6);
        position[2] = zPos.getValue(6);
        _exitPoints.put(name, new Point3d(position));
    }

    public Set<String> getEntryPointNames() {
        return _entryPoints.keySet();
    }

    public Set<String> getExitPointNames() {
        return _exitPoints.keySet();
    }

    public ExtendedLength[] getEntryPoint(String name) {
        Point3d point = _entryPoints.get(name);
        if (point == null) {
            return null;
        } else {
            ExtendedLength[] result = new ExtendedLength[3];
            result[0] = new ExtendedLength(point.x, 6);
            result[1] = new ExtendedLength(point.y, 6);
            result[2] = new ExtendedLength(point.z, 6);
            return result;
        }
    }

    public ExtendedLength[] getEntryPointGlobal(String name) {
        Point3d point = new Point3d(_entryPoints.get(name));
        if (point == null) {
            return null;
        } else {
            _spatialData.getMatrix().transform(point);
            ExtendedLength[] result = new ExtendedLength[3];
            result[0] = new ExtendedLength(point.x, 6);
            result[1] = new ExtendedLength(point.y, 6);
            result[2] = new ExtendedLength(point.z, 6);
            return result;
        }
    }

    public ExtendedLength[] getExitPoint(String name) {
        Point3d point = _exitPoints.get(name);
        if (point == null) {
            return null;
        } else {
            ExtendedLength[] result = new ExtendedLength[3];
            result[0] = new ExtendedLength(point.x, 6);
            result[1] = new ExtendedLength(point.y, 6);
            result[2] = new ExtendedLength(point.z, 6);
            return result;
        }
    }

    public ExtendedLength[] getExitPointGlobal(String name) {
        Point3d point = new Point3d(_exitPoints.get(name));
        if (point == null) {
            return null;
        } else {
            _spatialData.getMatrix().transform(point);
            ExtendedLength[] result = new ExtendedLength[3];
            result[0] = new ExtendedLength(point.x, 6);
            result[1] = new ExtendedLength(point.y, 6);
            result[2] = new ExtendedLength(point.z, 6);
            return result;
        }
    }

    public Matrix4d getMatrix() {
        return _spatialData.getMatrix();
    }

    public ExtendedLength getPosX() {
        return new ExtendedLength(_spatialData.getPosX(), 6);
    }

    public ExtendedLength getPosY() {
        return new ExtendedLength(_spatialData.getPosY(), 6);
    }

    public ExtendedLength getPosZ() {
        return new ExtendedLength(_spatialData.getPosZ(), 6);
    }

    public boolean isMovable() {
        return this instanceof MovableSpatialObject;
    }

    public boolean isVisible() {
        return _isVisible;
    }

    public void removeVisible() {
        _eventTransmitter.fireVisualEvent(new RemoveEvent(this, this.getName(), this.presentTime()));
    }

    public void resetRotation() {
        _spatialData.resetRotation();
        _eventTransmitter.fireVisualEvent(new SetOrientationEvent(this, this.getName(), new Quat4d(0., 0., 0., 1.), this.presentTime()));
    }

    public String getVisualModel() {
        return _visualModel;
    }

    public void rotX(double angle) {
        _spatialData.rotX(angle);
        boolean[] axis = { true, false, false };
        Rotation rotation = new Rotation(axis, angle, 0.0);
        _eventTransmitter.fireVisualEvent(new RotateEvent(this, this.getName(), rotation, this.presentTime()));
    }

    public void rotY(double angle) {
        _spatialData.rotY(angle);
        boolean[] axis = { false, true, false };
        Rotation rotation = new Rotation(axis, angle, 0.0);
        _eventTransmitter.fireVisualEvent(new RotateEvent(this, this.getName(), rotation, this.presentTime()));
    }

    public void rotZ(double angle) {
        _spatialData.rotZ(angle);
        boolean[] axis = { false, false, true };
        Rotation rotation = new Rotation(axis, angle, 0.0);
        _eventTransmitter.fireVisualEvent(new RotateEvent(this, this.getName(), rotation, this.presentTime()));
    }

    public void setPosition(ExtendedLength x, ExtendedLength y, ExtendedLength z) {
        assert (x != null && y != null && z != null) : "The coordinates must be specified.";
        double valX = x.getValue(6);
        double valY = y.getValue(6);
        double valZ = z.getValue(6);
        _spatialData.setPosition(valX, valY, valZ);
        _eventTransmitter.fireVisualEvent(new SetPositionEvent(this, this.getName(), valX, valY, valZ, this.presentTime()));
    }

    public void setVisible(boolean visible) {
        _eventTransmitter.fireVisualEvent(new SetVisibleEvent(this, this.getName(), visible, this.presentTime()));
        _isVisible = visible;
    }
}
