package org.jikesrvm.adaptive.controller;

/**
 * Abstract parent class for events from organizers to the controller.
 */
public interface ControllerInputEvent {

    /**
   * This method is called by the controller upon dequeuing this
   * event from the controller input queue
   */
    void process();
}
