package org.npsnet.v.models.cameras;

import javax.media.j3d.*;
import javax.vecmath.*;
import org.npsnet.v.kernel.Module;
import org.npsnet.v.kernel.ModuleDestructionEvent;
import org.npsnet.v.kernel.ModuleEvent;
import org.npsnet.v.kernel.ModuleEventListener;
import org.npsnet.v.kernel.ModuleReplacementEvent;
import org.npsnet.v.properties.foundation.Targeted;
import org.npsnet.v.properties.model.EntityModel;
import org.npsnet.v.properties.model.Transformable;

/**
 * A viewing window whose transform is specified relative to
 * the position and heading of a <code>Transformable</code> target.
 *
 * @author Andrzej Kapolka
 */
public class POVViewingWindow extends BasicViewingWindow implements Targeted {

    /**
     * The viewing window's relative transform.
     */
    private Transform3D relativeTransform;

    /**
     * The target to track.
     */
    private Transformable target;

    /**
     * A scratch transform to reuse.
     */
    private Transform3D scratchTransform;

    /**
     * The target listener.
     */
    private ModuleEventListener targetListener;

    /**
     * Constructor.
     */
    public POVViewingWindow() {
        super();
        relativeTransform = new Transform3D();
        scratchTransform = new Transform3D();
        targetListener = new ModuleEventListener() {

            public void moduleEventFired(ModuleEvent me) {
                if (me instanceof ModuleReplacementEvent) {
                    setTarget(((ModuleReplacementEvent) me).getReplacementModule());
                } else {
                    setTarget(null);
                }
            }
        };
    }

    /**
     * Destroys this module.
     */
    public void destroy() {
        setTarget(null);
        super.destroy();
    }

    /**
     * Checks to see if the specified module represents a
     * possible target.
     *
     * @param module the module to check
     * @return <code>true</code> if <code>module</code> represents
     * a compatible target, otherwise <code>false</code>
     */
    public boolean isCompatibleTarget(Module module) {
        return module instanceof Transformable;
    }

    /**
     * Sets this module's target.
     *
     * @param pTarget the new target
     */
    public void setTarget(Module pTarget) {
        if (target != null) {
            ((Module) target).removeEventListener(ModuleDestructionEvent.class, targetListener);
            ((Module) target).removeEventListener(ModuleReplacementEvent.class, targetListener);
        }
        target = (Transformable) pTarget;
        if (target != null) {
            ((Module) target).addEventListener(ModuleDestructionEvent.class, targetListener);
            ((Module) target).addEventListener(ModuleReplacementEvent.class, targetListener);
        }
        propertyChanged(Targeted.class);
    }

    /**
     * Returns this module's target.
     *
     * @return this module's target, or <code>null</code>
     * for none
     */
    public Module getTarget() {
        return (Module) target;
    }

    /**
     * Puts the entity's transform into <code>trans</code>.
     *
     * @param trans the <code>Transform3D</code> to hold the
     * transform
     * @param absolute whether or not to read the absolute, as opposed
     * to the relative, transform
     */
    public void getTransform(Transform3D trans, boolean absolute) {
        if (target == null) {
            trans.set(relativeTransform);
        } else {
            Vector3d targetPos = new Vector3d(), relativePos = new Vector3d(), dir = new Vector3d(0, 0, 1);
            target.getTransform(scratchTransform);
            scratchTransform.get(targetPos);
            relativeTransform.get(relativePos);
            scratchTransform.transform(dir);
            scratchTransform.invert(relativeTransform);
            scratchTransform.transform(dir);
            targetPos.add(relativePos);
            trans.set(relativeTransform);
            trans.setTranslation(targetPos);
            scratchTransform.rotZ(Math.PI / 2 + Math.atan2(dir.y, dir.x));
            trans.mul(scratchTransform);
        }
    }

    /**
     * Sets the entity's transform to <code>trans</code>.
     *
     * @param trans the <code>Transform3D</code> from which the
     * transform will be read
     * @param absolute whether or not to set the absolute, as opposed to the
     * relative, transform
     * @exception UnsupportedOperationException if the set method is not supported
     */
    public void setTransform(Transform3D trans, boolean absolute) {
        relativeTransform.set(trans);
        propertyChanged(Transformable.class);
    }

    /**
     * Checks whether the specified entity is in focus--that is, whether it
     * should receive input events.  Typically, this method returns <code>true</code>
     * for the viewing window itself and for any dependent or target entities.
     *
     * @param em the entity model to check
     * @return <code>true</code> if the entity is in focus, <code>false</code>
     * otherwise
     */
    public boolean isInFocus(EntityModel em) {
        return super.isInFocus(em) || em == target;
    }
}
