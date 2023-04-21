package tudresden.ocl20.pivot.ocl2java.template.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import tudresden.ocl20.pivot.ocl2java.template.ITemplate;
import tudresden.ocl20.pivot.ocl2java.template.ITemplateEngine;

/**
 * <p>
 * The {@link StringTemplateEngine} is an adapter for the
 * {@link StringTemplateGroup} class. It loads a list of {@link ITemplate}s and
 * sub-{@link ITemplate}s and creates a {@link ITemplate}s hierarchy according
 * to the order of the provided {@link ITemplate}s groups.
 * </p>
 * 
 * @author Florian Heidenreich
 */
public class StringTemplateEngine implements ITemplateEngine {

    /**
	 * The adapted template group.
	 */
    private StringTemplateGroup templateGroup;

    /**
	 * <p>
	 * Creates a new {@link StringTemplateEngine} and loads a list of
	 * {@link ITemplate}s and sub-{@link ITemplate}s and creates a
	 * {@link ITemplate} hierarchy according to the order of the provided
	 * {@link ITemplate} groups.
	 * 
	 * @param groupFiles
	 *            The names of the group files as relative paths to the projects
	 *            resource folder.
	 * @throws IOException
	 *             Thrown, if a given File name can not be found.
	 */
    public StringTemplateEngine(LinkedList<String> groupFiles) throws IOException {
        assert (groupFiles.size() > 0);
        Reader groupReader;
        StringTemplateGroup lastGroup;
        groupReader = new InputStreamReader(this.getClass().getResourceAsStream(groupFiles.getLast()));
        templateGroup = new StringTemplateGroup(groupReader);
        lastGroup = templateGroup;
        for (int i = 1; i < groupFiles.size(); i++) {
            StringTemplateGroup superGroup;
            String templatePath;
            templatePath = groupFiles.get(groupFiles.size() - i - 1);
            groupReader = new InputStreamReader(this.getClass().getResourceAsStream(templatePath));
            superGroup = new StringTemplateGroup(groupReader);
            lastGroup.setSuperGroup(superGroup);
            lastGroup = superGroup;
        }
    }

    /**
	 * @return The {@link StringTemplateAdapter} specified by the given template
	 *         name.
	 */
    public StringTemplateAdapter getTemplate(String name) {
        return new StringTemplateAdapter(templateGroup.getInstanceOf(name));
    }

    /**
	 * <p>
	 * A helper method to return the full path of the template file.
	 * </p>
	 * 
	 * @return The full path of the template file.
	 * @throws IOException
	 *             Thrown, if a given File name can not be found.
	 */
    protected String getFullTemplateFileName(String templateGroupFile) throws IOException {
        String result;
        Bundle bundle;
        Path templatePath;
        URL templateUrl;
        bundle = Platform.getBundle("tudresden.ocl20.pivot.ocl2java");
        templatePath = new Path(templateGroupFile);
        templateUrl = FileLocator.find(bundle, templatePath, null);
        result = null;
        result = FileLocator.resolve(templateUrl).getPath();
        return result;
    }
}
