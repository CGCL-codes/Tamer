package newsatort.ui.view.debug;

import newsatort.debug.DebuggedObject;
import org.eclipse.jface.viewers.LabelProvider;

class ViewLabelProvider extends LabelProvider {

    @Override
    public String getText(Object obj) {
        DebuggedObject debuggedObject = (DebuggedObject) obj;
        return debuggedObject.getName() + " (" + debuggedObject.getDeclaredClassName() + " -> " + debuggedObject.getInstanceClassName() + ")";
    }
}
