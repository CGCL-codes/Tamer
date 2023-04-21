package org.deft.representationrenderer;

import java.io.InputStream;
import org.deft.repository.datamodel.ArtifactRepresentation;

/**
 * A RepresentationRenderer converts a representation into a stream or an object
 * that can be understood by an ArtifactViewer. So a RepresentationRenderer is similar
 * to an Integrator for ArtifactViewers.
 * 
 * Note: it is not uncommon that a renderer does no conversion at all 
 * and just serves as a bridge between the result of an operation chain 
 * and the artifact viewer. (see e.g. ImageRepresentationRenderer) 
 */
public interface RepresentationRenderer {

    /**
	 * Returns the ID of the renderer
	 */
    public String getId();

    /**
	 * Returns a short description of the renderer. The description
	 * is used as the menu item text in the Open As menu of the
	 * project explorer.
	 */
    public String getDescription();

    /**
	 * Returns whether this RepresentationRenderer knows how to render
	 * a certain representation type.
	 */
    public boolean canRender(String representationType);

    /**
	 * Converts the representation into an object that the artifact viewer can handle.
	 * If the viewer cannot handle objects because it relies on streams, consider using
	 * {@link #getInputStream(ArtifactRepresentation)} instead.
	 */
    public Object render(ArtifactRepresentation representation);

    /**
	 * Converts the representation into an InputStream that the artifact viewer can
	 * handle. If the viewer cannot handle streams, consider using 
	 * {@link #render(ArtifactRepresentation)} instead.
	 */
    public InputStream getInputStream(ArtifactRepresentation representation);
}
