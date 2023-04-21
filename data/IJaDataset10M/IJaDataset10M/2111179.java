package org.parallelj.mda.controlflow.properties.sections;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.parallelj.mda.controlflow.properties.core.Section;
import org.parallelj.mda.controlflow.properties.zones.PredicateZone;

/**
 */
public class PredicateSection extends Section {

    @Override
    protected void addLayoutsToParts() {
        fData = new FormData();
        fData.left = new FormAttachment(0, 5);
        fData.right = new FormAttachment(100, -5);
        fData.top = new FormAttachment(0, 5);
        getZone("predicateZone").setLayoutData(fData);
    }

    @Override
    protected void initParts() {
        getZones().put("predicateZone", new PredicateZone(getBackGround()));
    }
}
