package com.sun.pept.encoding;

import com.sun.pept.ept.MessageInfo;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * <p>
 *
 * @author Arun Gupta
 * </p>
 */
public interface Encoder {

    /**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param messageInfo ...
 * </p>
 */
    public void encodeAndSend(MessageInfo messageInfo);

    /**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a ByteBuffer with ...
 * </p><p>
 * @param messageInfo ...
 * </p>
 */
    public ByteBuffer encode(MessageInfo messageInfo);
}
