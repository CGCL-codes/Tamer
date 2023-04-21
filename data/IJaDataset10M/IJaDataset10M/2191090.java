package com.tirsen.hanoi.event;

/**
 *
 * @author  Kenneth Ljunggren
 */
public interface ProcessInstanceListener {

    void instanceChanged(ProcessInstanceEvent event);
}
