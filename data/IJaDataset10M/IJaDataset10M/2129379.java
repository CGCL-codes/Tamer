package org.newsml.toolkit;

import java.io.IOException;

/**
 * A provider identifier.
 *
 * <p>A provider identifier is similar to a regular {@link
 * FormalName}, but it does not contain the local Duid or Euid
 * identifiers or a local scheme name.  It is also similar to a {@link
 * NewsItemId}, except for the missing local scheme information. See
 * {@link FormalName} for details on vocabulary resolution.</p>
 *
 * @author Reuters PLC
 * @version 2.0
 */
public interface ProviderId extends BaseNode, FormalNameNode, Text {
}
