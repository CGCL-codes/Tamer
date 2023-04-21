package com.googlecode.lazifier.project;

import java.util.HashMap;
import java.util.Map;
import com.googlecode.lazifier.generate.AbstractGenerator;
import com.googlecode.lazifier.generate.Source;

/**
 * The Projector class. A generator class to initialize a project by creating a number of resources.
 * 
 * @author Donny A. Wijaya
 * @version 0.0.9
 * @since April 2010
 */
public class Projector extends AbstractGenerator<ProjectOptions> {

    public void execute(ProjectOptions options) throws Exception {
        final String key = options.getKey().toLowerCase();
        final String name = options.getName().toLowerCase();
        final String type = options.getType().toLowerCase();
        final String _package = options.getPackage();
        final Map<String, Object> models = new HashMap<String, Object>();
        addKeyValuePairToModels(models, key, options);
        Source source = null;
        for (String setting : retrieveSettings(key, type)) {
            source = createSource(_package, setting, false);
            source.setRootPackage(_package);
            addKeyValuePairToModels(models, setting, source);
        }
        bulkWrite(models, name);
    }
}
