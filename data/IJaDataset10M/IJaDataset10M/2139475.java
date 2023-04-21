package com.bbn.vessel.author.ocmEditor;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class OCMEditorTreeTableNode extends DefaultMutableTreeTableNode {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasureTypeOrThreshold() {
        return measureTypeOrThreshold;
    }

    public void setMeasureTypeOrThreshold(String measureTypeOrThreshold) {
        this.measureTypeOrThreshold = measureTypeOrThreshold;
    }

    String type;

    String description;

    String measureTypeOrThreshold;

    public OCMEditorTreeTableNode(String type, String description, String measureTypeOrThreshold) {
        super();
        this.type = type;
        this.description = description;
        this.measureTypeOrThreshold = measureTypeOrThreshold;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Object getValueAt(int column) {
        switch(column) {
            case 0:
                return type;
            case 1:
                return description;
            case 2:
                return measureTypeOrThreshold;
            default:
                return null;
        }
    }
}
