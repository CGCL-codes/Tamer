package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.common.LayoutType;

/**
 * This interface contains a single method which is called when a Layout object
 * is to be created.
 */
public interface LayoutFactory {

    /**
   * Create a device specific Layout depending on the type.
   * @param type Type of Layout to create.
   * @return The device specific Layout.
   */
    public Layout createDeviceLayout(LayoutType type) throws LayoutException;

    /**
   * Create different Format objects depending on the type. This method is
   * reponsible for instantiating a new format object and adding it to its
   * parent.
   *
   * @param type The type of Format object to create.
   * @param parent The parent format object, or null if this is the root format.
   * @param index The index of the Format object within the parent.
   * @return The new Format or null if the type was not recognised.
   */
    public Format createFormat(FormatType type, Format parent, int index) throws LayoutException;

    /**
   * Do any attribute dependent initialisation.
   * @param format A Format returned by the create method.
   */
    public void formatAttributesHaveBeenSet(Format format) throws LayoutException;

    /**
   * Do any children dependent initialisation.
   * @param format A Format returned by the create method.
   */
    public void formatChildrenHaveBeenCreated(Format format) throws LayoutException;
}
