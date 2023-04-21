package com.apachetune.core.ui;

import com.apachetune.core.WorkItem;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public interface SmartPart {

    void initialize(WorkItem workItem);

    void run();

    void dispose();
}
