package it.tukano.jps.modules.structurebuilder;

import it.tukano.jps.math.CCNumber;
import it.tukano.jps.math.Material;
import it.tukano.jps.math.NColor;
import it.tukano.jps.math.NSegment;
import it.tukano.jps.math.NTuple3;
import it.tukano.jps.math.OutOfRangeException;
import it.tukano.jps.scene.DefaultMaterialElement;
import it.tukano.jps.scene.SceneElement;
import it.tukano.jps.scene.SceneElementID;
import it.tukano.jps.scene.SegmentArrayElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SegmentArrayBuilder implements PathBuilder {

    private final List<NTuple3> points = new ArrayList<NTuple3>(1000);

    private SceneElementID currentElementId;

    private SegmentArrayElement currentElement;

    private final Material green = new Material() {

        private final NColor green;

        {
            try {
                green = new NColor(new CCNumber(0), new CCNumber(1), new CCNumber(0), new CCNumber(1));
            } catch (OutOfRangeException ex) {
                throw new RuntimeException(ex);
            }
        }

        public NColor getColor(NTuple3 point) {
            return green;
        }
    };

    private DefaultMaterialElement defaultMaterial;

    public void addPoint(NTuple3 point, Hook hook) {
        points.add(point);
        updateGeometry(hook);
    }

    public void moveLastPoint(NTuple3 newValue, Hook hook) {
        if (getPointCount() == 1) {
            addPoint(newValue, hook);
        } else if (getPointCount() > 1) {
            points.set(points.size() - 1, newValue);
            updateGeometry(hook);
        }
    }

    public void removeLastPoint(Hook hook) {
        if (getPointCount() > 0) {
            points.remove(points.size() - 1);
            updateGeometry(hook);
        }
    }

    public void closePath(NTuple3 point, Hook hook) {
        if (getCurrentElement() != null) {
            PostDrawAction action = hook.getPostDrawAction();
            action.execute(getCurrentElement(), hook);
        }
        setCurrentElement(null);
        points.clear();
    }

    private DefaultMaterialElement getMaterialElement(Hook hook) {
        if (defaultMaterial != null) {
            return defaultMaterial;
        } else {
            defaultMaterial = new DefaultMaterialElement(hook.generateId(), green, Collections.<SceneElementID>emptyList());
            return defaultMaterial;
        }
    }

    private void setMaterialElement(DefaultMaterialElement element) {
        defaultMaterial = element;
    }

    private void updateGeometry(Hook hook) {
        int size = points.size();
        if (size < 1 && getCurrentElement() != null) {
            hook.removeElement(getCurrentElement());
            removeMaterialBinding(getCurrentElement(), hook);
            setCurrentElement(null);
        } else {
            if (getCurrentElementID() == null) {
                setCurrentElementID(hook.generateId());
            }
            int segCount = size - 1;
            LinkedList<NSegment> segments = new LinkedList<NSegment>();
            for (int i = 0; i < size - 1; i++) {
                NTuple3 v0 = points.get(i);
                NTuple3 v1 = points.get(i + 1);
                segments.add(new NSegment(v0, v1));
            }
            SegmentArrayElement element = new SegmentArrayElement(getCurrentElementID(), segments);
            setCurrentElement(element);
            hook.addOrUpdateElement(getCurrentElement());
            addMaterialBinding(element, hook);
        }
    }

    public int getPointCount() {
        return points.size();
    }

    private void setCurrentElementID(SceneElementID generateId) {
        currentElementId = generateId;
    }

    private SceneElementID getCurrentElementID() {
        return currentElementId;
    }

    private SegmentArrayElement getCurrentElement() {
        return currentElement;
    }

    private void setCurrentElement(SegmentArrayElement currentElement) {
        this.currentElement = currentElement;
        if (getCurrentElement() == null) {
            setCurrentElementID(null);
        } else {
            setCurrentElementID(getCurrentElement().getID());
        }
    }

    private void addMaterialBinding(SegmentArrayElement element, Hook hook) {
        DefaultMaterialElement m = getMaterialElement(hook);
        LinkedList<SceneElementID> ids = new LinkedList<SceneElementID>();
        for (SceneElementID id : m) {
            if (id.equals(element.getID())) {
                return;
            } else {
                ids.add(id);
            }
        }
        ids.add(element.getID());
        setMaterialElement(new DefaultMaterialElement(m.getID(), green, ids));
        hook.addOrUpdateElement(getMaterialElement(hook));
        System.out.println("material updated: add another element to the material binding list");
    }

    private void removeMaterialBinding(SceneElement currentElement, Hook hook) {
        DefaultMaterialElement m = getMaterialElement(hook);
        LinkedList<SceneElementID> ids = new LinkedList<SceneElementID>();
        boolean update = false;
        for (SceneElementID id : m) {
            if (!id.equals(currentElement.getID())) {
                ids.add(id);
            } else {
                update = true;
            }
        }
        if (update) {
            setMaterialElement(new DefaultMaterialElement(m.getID(), green, ids));
            hook.addOrUpdateElement(getMaterialElement(hook));
            System.out.println("material updated: removed an element from the binding list");
        }
    }
}
