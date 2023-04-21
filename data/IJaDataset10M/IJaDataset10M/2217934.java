package ru.adv.event;

/**
 * $Id:
 * $Name:
 * User: vic
 * Date: 26.12.2003
 * Time: 16:31:43
 */
public interface EventHandler {

    boolean hasEvent();

    QuestionEvent getEvent();

    void setResponse(Response response);
}
