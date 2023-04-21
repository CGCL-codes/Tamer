package com.apelon.beans.apelmsg;

/**
 * Message resource information for a product.
 * <p>Each product implements this interface in a subpackage of com.apelon.modules.apelmsg package
 */
public interface ApelMsgConfig {

    /**
   * Returns the path of the resource file which is not in the jar for the message type.
   * This will override the resource in the jar file.
   * @param msgType Message type
   * @return message type or null if message type is not supported
   */
    String getExternalResourcePath(String msgType);

    /**
   * Returns the path of the resource file in the jar for the message type
   * @param msgType Message type
   * @return message type or null if message type is not supported
   */
    String getResourcePath(String msgType);

    /**
   * Returns all supported message types
   * @return Message types
   */
    String[] getSupportedMsgTypes();
}
