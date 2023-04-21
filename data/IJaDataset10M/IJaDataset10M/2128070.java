package org.shestkoff.nimium.backtester.events;

import org.shestkoff.nimium.expert.AbstractExpert;

/**
 * Project: Maxifier
 * User: Vasily
 * Date: 25.02.2010 19:02:04
 * <p/>
 * Copyright (c) 1999-2010 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Shestkov Vasily
 */
public class InitEvent extends AbstractExpertEvent {

    protected InitEvent(long timeStamp) {
        super(timeStamp);
    }

    public void notify(AbstractExpert expert) {
        expert.onInit();
    }
}
