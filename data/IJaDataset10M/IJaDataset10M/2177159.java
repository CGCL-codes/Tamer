package org.gvsig.gpe.containers;

import java.util.ArrayList;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class Layer {

    private String name = null;

    private String description = null;

    private String id = null;

    private Layer parentLayer = null;

    private ArrayList features = new ArrayList();

    private ArrayList layers = new ArrayList();

    private String srs = null;

    private Bbox bbox = null;

    /**
	 * @return the bbox
	 */
    public Bbox getBbox() {
        return bbox;
    }

    /**
	 * @param bbox the bbox to set
	 */
    public void setBbox(Object bbox) {
        if (bbox != null) {
            this.bbox = (Bbox) bbox;
        }
    }

    /**
	 * @return the srs
	 */
    public String getSrs() {
        return srs;
    }

    /**
	 * @param srs the srs to set
	 */
    public void setSrs(String srs) {
        this.srs = srs;
    }

    public Layer() {
        super();
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the features
	 */
    public ArrayList getFeatures() {
        return features;
    }

    /**
	 * Adds a new feature
	 * @param layer
	 */
    public void addFeature(Feature feature) {
        features.add(feature);
    }

    /**
	 * Adds a new layer
	 * @param layer
	 */
    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    /**
	 * Adds a new layer
	 * @param layer
	 */
    public void addFeature(Object feature) {
        if (feature instanceof Feature) {
            features.add(feature);
        }
    }

    /**
	 * @return the parentLayer
	 */
    public Layer getParentLayer() {
        return parentLayer;
    }

    /**
	 * @param parentLayer the parentLayer to set
	 */
    public void setParentLayer(Object parentLayer) {
        if (parentLayer != null) {
            this.parentLayer = (Layer) parentLayer;
        }
    }

    /**
	 * @return the layers
	 */
    public ArrayList getLayers() {
        return layers;
    }

    /**
	 * @return the layer at position i
	 * @param i
	 * Layer position
	 */
    public Layer getLayerAt(int i) {
        return (Layer) layers.get(i);
    }
}
