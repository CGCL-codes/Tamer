package org.designerator.common.interfaces;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;

public interface IImageModifier {

    public void dispose();

    public void update();

    public void savePreferences(IDialogSettings settings);

    public void setCanvas(IPaintCanvas canvas);

    public void setCanvasContainer(IPaintCanvasContainer canvasContainer);

    public String getDiscription();

    public void createContents(Composite composite, IDialogSettings dialogSetting);

    public String getName();

    public void setCanvas(IPaintCanvas canvas, IProcessor processor);

    public void saveImage(String imageFilePath);
}
