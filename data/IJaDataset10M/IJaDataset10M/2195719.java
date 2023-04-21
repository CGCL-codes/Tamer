package org.jz.envelope.printer.russian.blocks;

/**
 * Интерфейс для представления блока, имеющего горизонтальный размер
 * (рамки, надписи, горизонтальной линии и т.п.). Имеет ширину.
 * @author Сергей Жезняковский - 2011-03-05 23:43
 */
public interface HorizontalBlock extends Block {

    /**
     * Получение текущей ширины блока.
     * @return - текущая ширина блока (в пунктах)
     */
    public double getWidth();

    /**
     * Установка ширины блока.
     * @param _Width - новая ширина блока (в пунктах)
     */
    public void setWidth(double _Width);
}
