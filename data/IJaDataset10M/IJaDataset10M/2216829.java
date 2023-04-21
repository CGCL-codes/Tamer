package com.jacum.cms.rcp.ui.editors.item.stylededitor.painters;

import org.eclipse.jface.text.projection.ProjectionDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;

/**
 * BoldPainter find bold constructions like: <b>bold text</b>
 * and set style of the nested text to bold 
 * 
 * @author rich
 */
public class BoldPainter extends AbstractDoublePainter {

    private static final String CATEGORY = "bold";

    /**
	 * Constructs new BoldPainter
	 * 
	 * @param document projection document
	 * @param viewer source viewer 
	 */
    public BoldPainter(ProjectionDocument document, ISourceViewer viewer) {
        super(document, viewer, CATEGORY);
    }

    @Override
    public String[] getConstructions() {
        return IConstructions.BOLD_CONSTRUCTIONS;
    }

    @Override
    public String getImageId() {
        return null;
    }

    /** 
	 * Bold text style is <code>SWT.BOLD</code>  
	 * 
	 * @return <code>SWT.BOLD</code>
	 */
    @Override
    protected int getStyle() {
        return SWT.BOLD;
    }

    @Override
    protected boolean showBody() {
        return true;
    }
}
