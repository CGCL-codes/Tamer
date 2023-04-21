package org.jmove.java.loader.typeanalyzer.sourcecode;

import org.jmove.core.util.Log;
import org.jmove.java.loader.io.ClassCollectionEntry;
import org.jmove.java.loader.progress.ProgressObserver;
import org.jmove.java.loader.progress.ProgressSubject;
import org.jmove.java.loader.typeanalyzer.TypeLoadStrategy;
import org.jmove.java.model.JModel;
import org.jmove.oo.Module;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Michael Juergens
 */
public class SourcecodeLoadStrategy implements TypeLoadStrategy, ProgressSubject {

    /**
     * the global parser
     * todo should be substituted by an instance
     */
    private static JavaParser theParser = null;

    /**
     * Number of entries processed by this strategy
     */
    private int myDeclarationCount = 0;

    public void addProgressObserver(ProgressObserver aProgressListener) {
        if (theParser != null && theParser.getProcessor() != null) {
            theParser.getProcessor().addProgressObserver(aProgressListener);
        }
    }

    /**
     * Finish loading a set of types.
     */
    public void finishLoading() {
        if (theParser != null) {
            theParser.buildDetails();
            theParser = null;
        }
    }

    /**
     * Load declarations from given resource of the given module.
     */
    public void loadDeclarationsFromResource(ClassCollectionEntry aResource, Module aModule) {
        if (theParser == null) {
            theParser = new JavaParser((JModel) aModule.model());
        }
        try {
            Log.debug(this, myDeclarationCount++ + ") Parsing source '" + aResource.getResourcePath() + "'... ");
            theParser.parse(aResource);
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            Log.error(this, "Source '" + aResource.getResourcePath() + "' can not be parsed! " + e.getMessage());
        }
    }

    /**
     * Processes a single task to load a type definition.
     *
     * @param task
     * @return A collection of future tasks.
     */
    public Collection processTask(Object task, JModel model) {
        return new ArrayList();
    }
}
