package org.mondemand.fromjmx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class identifies one or more MBeans and the attributes for them
 * that should be exported.
 * 
 * @author Joel Meyer (joel.meyer@openx.org)
 *
 */
public class ExportedBean {

    protected final String objectNameIdentifier;

    protected final List<ExportedAttribute> exportedAttributes;

    public ExportedBean(String objectNameIdentifier) {
        this.objectNameIdentifier = objectNameIdentifier;
        this.exportedAttributes = new ArrayList<ExportedAttribute>();
    }

    /**
   * @return The string pattern that is used to identify one or more
   *         {@link ObjectName}s.
   */
    public String getObjectNameIdentifier() {
        return objectNameIdentifier;
    }

    /**
   * Add an {@link ExportedAttribute} to the list of attributes that will
   * be exported for this MBean.
   * @param exportedAttribute Attribute to export.
   */
    public void addExportedAttribute(ExportedAttribute exportedAttribute) {
        exportedAttributes.add(exportedAttribute);
    }

    /**
   * @return An immutable list of the attributes to be exported.
   */
    public List<ExportedAttribute> getExportedAttributes() {
        return Collections.unmodifiableList(exportedAttributes);
    }
}
