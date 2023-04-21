package org.pachyderm.apollo.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WModel;
import com.webobjects.directtoweb.Rule;
import com.webobjects.foundation.NSArray;

/**
 * The MCModel class manages rules used by the Managed Component system. This class is an extension
 * of WebObject's D2WModel class that adds a method to return the second inferred value of a key.
 */
public class MCModel extends D2WModel {

    private static Logger LOG = Logger.getLogger(MCModel.class);

    public static final String TOPTABS = "contextualIdentifiers";

    public MCModel(NSArray<Rule> rules) {
        super(rules);
    }

    public static MCModel cxDefaultModel() {
        return (MCModel) D2WModel.defaultModel();
    }

    @Override
    public void mergePathURL(URL modelPathURL) {
        if (modelPathURL.getPath().indexOf("JavaDirectToWeb") == -1) {
            super.mergePathURL(modelPathURL);
        }
    }

    @Override
    public void loadRules() {
        LOG.info("loadRules()");
        NSArray pluginRules = ((MC) D2W.factory())._pluginRules();
        for (int i = 0; i < pluginRules.count(); i++) {
            addRules(((NSArray) pluginRules.objectAtIndex(i)));
        }
        super.loadRules();
        LOG.trace(this);
    }

    /**
   * Returns the second inferred value for the given keyPath. If <code>firstFirstIfAvailable</code> is 
   * true, then this method will return the first possible value if a second value does not exist.
   */
    public Object fireSecondRuleForKeyPathInContext(String keyPath, com.webobjects.directtoweb.D2WContext context, boolean fireFirstIfAvailable) {
        Vector<Rule> candidates = candidates(keyPath, context);
        if (candidates != null) {
            Rule firstRule = null;
            for (Rule rule : candidates) {
                if (rule.canFireInContext(context)) {
                    if (firstRule == null) {
                        firstRule = rule;
                    } else {
                        if (!((String) firstRule.fire(context)).equals((String) rule.fire(context))) {
                            return rule.fire(context);
                        }
                    }
                }
            }
            if (fireFirstIfAvailable && firstRule != null) {
                return firstRule.fire(context);
            }
        }
        return null;
    }

    @Override
    public void toFile(File filename) {
        try {
            super.toFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
