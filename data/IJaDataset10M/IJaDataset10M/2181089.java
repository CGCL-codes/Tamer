package org.spbu.pldoctoolkit.clones.view;

import java.util.List;
import org.eclipse.jface.viewers.LabelProvider;
import org.spbu.pldoctoolkit.clones.ICloneInst;
import org.spbu.pldoctoolkit.clones.IClonesGroup;

final class ClonesGroupLabelProvider extends LabelProvider {

    @Override
    public String getText(Object element) {
        if (element instanceof IClonesGroup) return ((IClonesGroup) element).getName(); else if (element instanceof ICloneInst) {
            return ((ICloneInst) element).getName();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
