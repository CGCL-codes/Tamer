package pl.edu.amu.xtr.view.dataProvider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import pl.edu.amu.xtr.cors.Correspondence;

public class CorrespondenceLableProvider implements ITableLabelProvider {

    public String getColumnText(Object arg0, int col) {
        Correspondence correspondence = (Correspondence) arg0;
        if (col == 0) {
            return correspondence.getTargetElement();
        } else if (col == 1) {
            return correspondence.getSourceElement();
        } else if (col == 2) {
            return correspondence.getAggregateFunction() != null ? correspondence.getAggregateFunction().name() : "";
        } else {
            return null;
        }
    }

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public void addListener(ILabelProviderListener arg0) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    public void removeListener(ILabelProviderListener arg0) {
    }
}
