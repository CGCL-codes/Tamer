package com.apelon.dts.client.attribute;

/**
 * The <code>DTSAttribute</code> provides access to name and value of an
 * attribute.
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon, Inc.
 * @version    DTS 3.0
 */
public interface DTSAttribute {

    /**
   * Gets the name of the attribute.
   *
   * @return the name of the attribute.
   */
    public String getName();

    /**
   * Gets the value of the attribute.
   *
   * @return the value of the attribute.
   */
    public String getValue();
}
