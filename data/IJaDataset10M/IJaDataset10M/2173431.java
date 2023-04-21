package org.pustefixframework.xmlgenerator.targets;

import java.util.TreeMap;
import org.w3c.dom.Document;

/**
 *
 *
 */
public interface Target extends Comparable<Target> {

    TargetType getType();

    String getTargetKey();

    TargetGenerator getTargetGenerator();

    Target getXMLSource();

    Target getXSLSource();

    Themes getThemes();

    TreeMap<String, Object> getParams();

    long getModTime();

    String toString();

    String getFullName();

    /**
     * Get the value of the target. Depending on the 
     * circumstances this will trigger a recursive 
     * generation of the target.</br> 
     * @return the value of this target.
     * @throws TargetGenerationException on 
     * known errors which can occur on target 
     * generation. 
     */
    Object getValue() throws TargetGenerationException;

    boolean needsUpdate() throws Exception;

    Document getDOM() throws TargetGenerationException;

    void invalidate();
}
