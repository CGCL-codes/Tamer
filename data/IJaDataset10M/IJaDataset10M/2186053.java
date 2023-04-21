package org.gocha.gef;

/**
 * Слушатель/подписчик изменения свойств объекта
 * @author gocha
 */
public interface PropertyChangeListener {

    /**
     * Вызывается после изменения свойства
     * @param e Событие описание изменеия
     */
    void properyChanged(PropertyChangeEvent e);
}
