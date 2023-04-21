package gui;

import utils.defines.Defines;

/**
 * La clase <code>NeuralNetworkBaseItem</code> representa una �tem (red neuronal)
 * que puede ser almacenado en la base de datos de redes neuronales.
 */
public class NeuralNetworkBaseItem {

    public static final String FIELD_SEPARATOR = "#";

    private String path;

    private boolean selected;

    private String name;

    private String description;

    private int type;

    public NeuralNetworkBaseItem() {
        super();
    }

    public NeuralNetworkBaseItem(String line) {
        super();
        String[] items = line.split(FIELD_SEPARATOR);
        if (items.length == 2) {
            path = items[0];
            selected = (items[1].compareTo("1") == 0);
        } else {
            path = null;
            selected = false;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public String toString() {
        return path + FIELD_SEPARATOR + (selected ? "1" : "0") + Defines.CR;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
