package com.foursoft.fourever.draw.model;

import java.util.List;
import com.foursoft.fourever.draw.QualifiedId;
import com.foursoft.fourever.objectmodel.ComplexInstance;

/**
 * @author sihling
 * 
 */
public class Join extends Node {

    private final List<JoinEntry> entries;

    public Join(ComplexInstance instance, QualifiedId qualId, List<JoinEntry> entries) {
        super(instance, qualId);
        this.entries = entries;
        for (JoinEntry je : entries) {
            je.setJoin(this);
        }
    }

    public List<JoinEntry> getEntries() {
        return entries;
    }
}
