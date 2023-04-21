package lamao.soh.core;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * Game entity, which has model.
 * @author lamao
 *
 */
public class SHEntity {

    /** Type of the entity */
    private String _type = "default";

    /** Root node for entity */
    private Node _root = new Node("");

    /** Collidable model for this root. Other spatials attached to the root
	 * node are treated as decoration, effect etc. 
	 */
    private Spatial _model = null;

    /** Name of this entity */
    private String _name = null;

    public SHEntity(String type, String name, Spatial model) {
        this(model);
        _type = type;
        setName(name);
    }

    public SHEntity(Spatial model) {
        this();
        _model = model;
        _root.attachChild(model);
    }

    public SHEntity() {
    }

    public Node getRoot() {
        return _root;
    }

    public Spatial getModel() {
        return _model;
    }

    public void setModel(Spatial model) {
        if (_model != null) {
            _root.detachChild(_model);
        }
        _model = model;
        _root.attachChild(model);
        _root.updateRenderState();
        _root.updateGeometricState(0, true);
    }

    /** 
	 * Changes model local translation. <br>
	 * <b>NOTE: </b>Model must be not null
	 */
    public void setLocation(Vector3f location) {
        _root.setLocalTranslation(location);
        _root.updateModelBound();
    }

    /** 
	 * @see #setLocation(Vector3f)
	 */
    public void setLocation(float x, float y, float z) {
        _root.setLocalTranslation(x, y, z);
        _root.updateModelBound();
    }

    public Vector3f getLocation() {
        return _root.getLocalTranslation();
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    public void setName(String name) {
        _name = name;
        _root.setName(name);
    }

    public String getName() {
        return _name;
    }
}
