package com.jgeppert.struts2.jquery.components;

/**
 * 
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public interface DroppableBean {

    void setDroppable(String droppable);

    void setDroppableAccept(String droppableAccept);

    void setDroppableActiveClass(String droppableActiveClass);

    void setDroppableAddClasses(String droppableAddClasses);

    void setDroppableGreedy(String droppableGreedy);

    void setDroppableHoverClass(String droppableHoverClass);

    void setDroppableScope(String droppableScope);

    void setDroppableTolerance(String droppableTolerance);

    void setDroppableOnActivateTopics(String droppableActivate);

    void setDroppableOnDeactivateTopics(String droppableDeactivate);

    void setDroppableOnOverTopics(String droppableOver);

    void setDroppableOnOutTopics(String droppableOut);

    void setDroppableOnDropTopics(String droppableDrop);
}
