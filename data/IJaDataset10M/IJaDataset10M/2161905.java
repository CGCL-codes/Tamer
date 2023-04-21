package org.gocha.textbox;

import java.io.Closeable;
import java.util.List;

/**
 * Навигаци по истории
 * @author gocha
 */
public interface TextHistoryNavigator extends Closeable {

    /**
     * Указывает используемую историю изменения
     * @return История изменения
     */
    List<TextHistoryAction> getHistory();

    /**
     * Указывает максимальное значение индекса
     * @return максимальное значение
     */
    int getMaxIndex();

    /**
     * Указывает минимальное значение индекса
     * @return минимальное значение
     */
    int getMinIndex();

    /**
     * Указывает текущее значение индекса
     * @return текущее значение
     */
    int getIndex();

    /**
     * Откат назад
     * @return true - успешно
     */
    boolean goBack();

    /**
     * Накат вперед
     * @return true - успешно
     */
    boolean goForward();

    /**
     * Указывает возможность отката "назад"
     * @return true - возможно
     */
    boolean isAllowBack();

    /**
     * Указывает возможность наката "вперед"
     * @return true - возможно
     */
    boolean isAllowForward();
}
