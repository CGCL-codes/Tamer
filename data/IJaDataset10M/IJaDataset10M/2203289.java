package org.designerator.common.interfaces;

import java.util.List;
import org.eclipse.jface.viewers.ISelection;

public interface IThumbSelection extends ISelection {

    public void setThumbs(List<IThumb> thumbs);

    public boolean isEmpty();

    public List<IThumb> getThumbs();
}
