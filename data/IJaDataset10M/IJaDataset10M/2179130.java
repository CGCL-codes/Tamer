package org.eclipse.emf.converter.ui.contribution;

import org.eclipse.swt.graphics.Image;

/**
 * <p>Basic interface to describe the objects responsible to import and export
 * the Genmodel and Ecore models.  Usually these models are contributed through
 * extension points.</p>
 * <p>It is highly recommended not to implement this interface.  Implementations
 * are provided in the importer and exporter plugins.</p>
 * 
 * @since 2.2.0
 */
public interface ModelConverterDescriptor {

    String getID();

    String getName();

    Image getIcon();

    String getDescription();
}
