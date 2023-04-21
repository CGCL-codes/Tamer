package ca.gc.drdc_rddc.atlantic.vmsa;

import hla.rti13.java1.RTIexception;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import virtualship.coords.Matrix;
import virtualship.coords.Vector;
import virtualship.federate.ReflectedCompositeEntity;
import ca.gc.drdc_rddc.atlantic.hla.HLALogicalTime;
import ca.gc.drdc_rddc.atlantic.hla.HLAObject;
import ca.gc.drdc_rddc.atlantic.hla.HLAObjectClass;
import ca.gc.drdc_rddc.atlantic.hla.HLAObjectClassListener;
import ca.gc.drdc_rddc.atlantic.hla.HLAObjectInstanceListener;
import ca.gc.drdc_rddc.atlantic.hla.SomObject;
import ca.gc.drdc_rddc.atlantic.hla.ValueHashMap;
import ca.gc.drdc_rddc.atlantic.hla.ValueMap;

/**
 * Mar 21, 2005 2:23:32 PM
 * 
 * @author Dillman
 */
public class AdaptedComponentEntity extends SomObject implements HLAObjectClassListener, HLAObjectInstanceListener {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(AdaptedComponentEntity.class);

    /** The parent composite entity, if known, otherwise null. */
    public AdaptedCompositeEntity parent;

    /** Name of parent composite entity. */
    public String parentName;

    /** Name of this component. */
    public String componentName;

    /** Status of the component. */
    public int status;

    /** Relative coordinates. */
    public double relX, relY, relZ;

    /** Relative orientation. */
    public double relOmegaX, relOmegaY, relOmegaZ;

    /** Absolute coordinates based on parent position + relative position. */
    public double x, y, z, vx, vy, vz, ax, ay, az, omegaX, omegaY, omegaZ, omegaDotX, omegaDotY, omegaDotZ;

    /**
	 * Constructor.
	 */
    public AdaptedComponentEntity() {
        super();
        parent = null;
        parentName = null;
    }

    /**
	 * Set the parent composite entity of this component.
	 * 
	 * @param theParent
	 *            the parent composite entity of this component.
	 */
    public void setParent(AdaptedCompositeEntity theParent) {
        if (parent != null) parent.removeComponent(this);
        parent = theParent;
        parent.addComponent(this);
    }

    /**
	 * User application calls this to delete the object from the RTI.
	 * 
	 * @throws RTIexception
	 */
    public void delete(byte[] tag) throws Exception {
        if (parent != null) {
            parent.removeComponent(this);
        }
        super.delete(tag);
    }

    /**
	 * Search composite entites for parent.
	 */
    public void scanForParent() {
        HLAObjectClass cls = getModel().lookupObjectClass("CompositeEntity");
        HLAObject obj = cls.object(parentName);
        if (obj != null) {
            Set mos = obj.getManagedObjects();
            if (mos.size() > 0) {
                for (Iterator i = mos.iterator(); i.hasNext(); ) {
                    try {
                        Object sObj = i.next();
                        if (sObj instanceof AdaptedCompositeEntity) {
                            parent = (AdaptedCompositeEntity) sObj;
                            logger.debug("found parent " + parentName);
                            obj.registerListener(this);
                            computeAbsolutePosition();
                        }
                    } catch (ClassCastException e) {
                        logger.warn("incompatible composite entity managed class", e);
                    }
                }
            }
        }
    }

    /**
	 * Called when link to som object is set, immediately scan composite
	 * entities for parent.
	 */
    public void linked() {
        if (parent == null && parentName != null) {
            scanForParent();
        }
        if (parent == null) {
            HLAObjectClass cls = getModel().lookupObjectClass("CompositeEntity");
            cls.registerListener(this);
        }
    }

    /**
	 * Called when a refelection is received from the RTI.
	 * 
	 * @param attrs
	 *            ValueMap of changed (String attrName, Object attrValue).
	 */
    public void reflect(ValueMap attrs) {
        ValueMap fields;
        String newParentName = (String) attrs.get("ParentCompositeEntityObjectInstanceName");
        if (newParentName != null) {
            if (parentName != null && !newParentName.equals(parentName)) {
                setParent(null);
            }
            parentName = newParentName;
            if (parent == null) {
                scanForParent();
            }
        }
        componentName = (String) attrs.get("ComponentName");
        status = attrs.get("Status", status);
        fields = (ValueMap) attrs.get("RelativePosition");
        if (fields != null) {
            relX = fields.get("X", relX);
            relY = fields.get("Y", relY);
            relZ = fields.get("Z", relZ);
        }
        fields = (ValueMap) attrs.get("RelativeOrientation");
        if (fields != null) {
            relOmegaX = fields.get("OmegaX", relOmegaX);
            relOmegaY = fields.get("OmegaY", relOmegaY);
            relOmegaZ = fields.get("OmegaZ", relOmegaZ);
        }
        if (parent != null) {
            computeAbsolutePosition();
        }
    }

    /**
	 * Compute absolute position, etc. for this component given knowledge of
	 * parent. NOTE: this transform should account for the motion and rotation
	 * of the parent, not simply treat the parent as a point.
	 */
    public void computeAbsolutePosition() {
        ReflectedCompositeEntity rce = parent.getReflectedCompositeEntity();
        double[] d;
        d = rce.getOrientation();
        Matrix rComposite = Matrix.getRotationMatrix(d[0], d[1], d[2]);
        Vector relPos = new Vector(relX, relY, relZ);
        Vector correctRelPos = Vector.multiply(rComposite, relPos);
        d = rce.getPosition();
        x = d[0] + correctRelPos.get(0);
        y = d[1] + correctRelPos.get(1);
        z = d[2] + correctRelPos.get(2);
        d = rce.getVelocity();
        vx = d[0];
        vy = d[1];
        vz = d[2];
        d = rce.getAcceleration();
        ax = d[0];
        ay = d[1];
        az = d[2];
        d = rce.getOrientation();
        omegaX = d[0] + relOmegaX;
        omegaY = d[1] + relOmegaY;
        omegaZ = d[2] + relOmegaZ;
        d = rce.getOrientationRate();
        omegaDotX = d[0];
        omegaDotY = d[1];
        omegaDotZ = d[2];
    }

    /**
	 * Copy local values to SomObject before update.
	 */
    public void copyToSomObject() {
        ValueHashMap fields = new ValueHashMap();
        set("ComponentName", componentName);
        set("Status", status);
        fields.clear();
        fields.set("X", relX);
        fields.set("Y", relY);
        fields.set("Z", relZ);
        set("RelativePosition", fields);
        fields.clear();
        fields.set("OmegaX", relOmegaX);
        fields.set("OmegaY", relOmegaY);
        fields.set("OmegaZ", relOmegaZ);
        set("RelativeOrientation", fields);
    }

    /**
	 * @throws RTIexception
	 */
    public void update(byte[] tag) throws Exception {
        if (getHlaObject() != null) {
            copyToSomObject();
            super.update(tag);
        }
    }

    /**
	 * @param timeStamp
	 * @throws RTIexception
	 */
    public void update(HLALogicalTime timeStamp, byte[] tag) throws Exception {
        if (getHlaObject() != null) {
            copyToSomObject();
            super.update(timeStamp, tag);
        }
    }

    /**
	 * Unlink from parent when removed.
	 */
    public void removed() {
        if (parent != null) {
            parent.removeComponent(this);
        }
    }

    /**
	 * Called when parent composite entity is removed.
	 * 
	 * @param obj
	 *            the parent composite entity.
	 */
    public void objectRemoved(HLAObject obj, Object tag) {
    }

    /**
	 * Called when parent composite entity is removed.
	 * 
	 * @param obj
	 *            the parent composite entity.
	 */
    public void objectRemoved(HLAObject obj, HLALogicalTime time, Object tag) {
        objectRemoved(obj, tag);
    }

    /**
	 * @param obj
	 * @param attrToValue
	 */
    public void reflectAttributeValues(HLAObject obj, Map attrToValue, Object tag) {
        logger.debug("parent moved, recomputing position");
        computeAbsolutePosition();
    }

    public void reflectAttributeValues(HLAObject obj, Map attrToValue, HLALogicalTime time, Object tag) {
        reflectAttributeValues(obj, attrToValue, tag);
    }

    /**
	 * Called when a new composite entity is discovered.
	 * 
	 * @param obj
	 *            the discovered composite entity.
	 */
    public void objectDiscovered(HLAObject obj) {
        if (parent == null && parentName != null) {
            scanForParent();
        }
    }

    /**
	 * @param obj
	 * @see ca.gc.drdc_rddc.atlantic.hla.HLAObjectClassListener#objectCreated(ca.gc.drdc_rddc.atlantic.hla.HLAObject)
	 */
    public void objectCreated(HLAObject obj) {
    }

    /**
	 * @param attrOffset
	 * @return
	 * @see ca.gc.drdc_rddc.atlantic.hla.HLAInstance#get(int)
	 */
    public Object get(int attrOffset) {
        return null;
    }

    /**
	 * @param attrName
	 * @return
	 * @see ca.gc.drdc_rddc.atlantic.hla.HLAInstance#get(java.lang.String)
	 */
    public Object get(String attrName) {
        return null;
    }

    /**
	 * @param obj
	 * @see ca.gc.drdc_rddc.atlantic.hla.HLAObjectInstanceListener#objectDeleted(ca.gc.drdc_rddc.atlantic.hla.HLAObject)
	 */
    public void objectDeleted(HLAObject obj, Object tag) {
    }

    public void objectDeleted(HLAObject obj, HLALogicalTime time, Object tag) {
    }
}
