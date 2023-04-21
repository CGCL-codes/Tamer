package org.shestkoff.nimium.backtester.events;

/**
 * Project: Maxifier
 * User: Vasily
 * Date: 24.02.2010 15:50:27
 * <p/>
 * Copyright (c) 1999-2010 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Shestkov Vasily
 */
public abstract class MyTradeEvent extends AbstractExpertEvent {

    protected MyTradeEvent(long timeStamp) {
        super(timeStamp);
    }
}
