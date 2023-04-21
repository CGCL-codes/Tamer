package org.dykman.dexter.dexterity;

import org.dykman.dexter.base.PathEval;
import org.dykman.dexter.descriptor.Descriptor;
import org.dykman.dexter.descriptor.PathDescriptor;
import org.w3c.dom.Node;

public class CopyElementDescriptor extends PathDescriptor {

    public CopyElementDescriptor(Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void children() {
        boolean useDefault = false;
        boolean children = false;
        if (value.startsWith("!")) {
            value = value.substring(1);
            useDefault = true;
        }
        if (value.startsWith("#")) {
            value = value.substring(1);
            children = true;
        }
        Node def = useDefault ? getChildren(element) : null;
        sequencer.copyNodes(PathEval.parseSingle(value), def, children);
    }
}
