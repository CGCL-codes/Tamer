package org.marko.rendering;

import java.io.File;
import java.io.Writer;
import org.marko.View;

/**
 * Renders views.
 * 
 * <p>
 * The default renderer is the {@link org.marko.rendering.FreemarkerRenderer FreemarkerRenderer}
 * but you can change it in the web.xml file. Check the samples to see how to do it.
 * 
 * @author Celio Cidral Junior
 */
public interface Renderer {

    /**
	 * Renders a view based on a template file.
	 * 
	 * @param view The view. Normally, the view name corresponds to the template file name.
	 * @param writer The output.
	 */
    public void render(View view, Writer writer);

    /**
	 * Sets the directory where the template files can be found.
	 * 
	 * @param directory The directory.
	 */
    public void setTemplateDirectory(File directory);
}
