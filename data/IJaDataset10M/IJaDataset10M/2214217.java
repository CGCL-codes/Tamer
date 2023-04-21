package com.abstratt.modelviewer;

import java.net.URI;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import com.abstratt.graphviz.ui.DOTGraphicalContentProvider;
import com.abstratt.mdd.modelrenderer.dot.DOTRendering;
import com.abstratt.modelrenderer.IRendererSelector;
import com.abstratt.modelrenderer.IRenderingSettings;

public abstract class AbstractModelGraphicalContentProvider extends DOTGraphicalContentProvider {

    protected abstract IRendererSelector<?, ?> getRendererSelector();

    public Image loadImage(Display display, Point desiredSize, Object newInput) throws CoreException {
        if (newInput == null) return new Image(display, 1, 1);
        byte[] dotContents = DOTRendering.generateDOTFromModel((URI) newInput, getRendererSelector(), getSettings());
        if (dotContents != null) return super.loadImage(display, desiredSize, dotContents);
        return new Image(display, 1, 1);
    }

    @Override
    public void saveImage(Display display, Point suggestedSize, Object input, IPath outputLocation, int fileFormat) throws CoreException {
        byte[] dotContents = DOTRendering.generateDOTFromModel((URI) input, getRendererSelector(), getSettings());
        if (dotContents == null) dotContents = new byte[0];
        super.saveImage(display, suggestedSize, dotContents, outputLocation, fileFormat);
    }

    protected IRenderingSettings getSettings() {
        return IRenderingSettings.NO_SETTINGS;
    }
}
