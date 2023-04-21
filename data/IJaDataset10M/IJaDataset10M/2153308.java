package org.gocha.collection.list;

/**
 * Аргументы события измения списка
 * @author Камнев Георгий Павлович
 */
public class BasicEventListArgs<T> implements EventListArgs<T> {

    private EventListAction action;

    private T item;

    private EventList<T> list;

    private int index;

    /**
	 * Конструктор
	 * @param list Cписок
	 * @param action Тип события с элементом списка
	 * @param item Элемент списка
	 */
    public BasicEventListArgs(EventList<T> list, EventListAction action, T item, int index) {
        this.action = action;
        this.item = item;
        this.list = list;
        this.index = index;
    }

    @Override
    public EventListAction getAction() {
        return action;
    }

    @Override
    public T getItem() {
        return item;
    }

    @Override
    public EventList<T> getList() {
        return list;
    }

    public int getItemIndex() {
        return index;
    }
}
