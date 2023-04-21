package org.gocha.collection.list;

import java.util.List;

/**
 * Интерфейс для доступа к уведомлениям об изменении списка
 * @author Камнев Георгий Павлович
 */
public interface EventList<E> extends List<E>, EventListSender<E> {
}
