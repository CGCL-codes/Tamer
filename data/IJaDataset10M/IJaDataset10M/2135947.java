package com.apelon.dts.client.events;

import java.util.EventListener;

/**
 * Description
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public interface SubsetListener extends EventListener {

    public void subsetActionOccurred(SubsetEvent se);
}
