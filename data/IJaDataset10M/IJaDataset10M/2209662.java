package org.gocha.grid.style;

import org.gocha.skin.*;

/**
 * Соединяет стили ячейки
 * @author Камнев Георгий Павлович
 */
public class MergeCellStyle implements IMergeCellStyle {

    /**
     * Соединяет стили ячеек
     * @param src Исходный
     * @param dest Конечный
     * @return Новый стиль
     */
    public ICellStyle merge(ICellStyle src, ICellStyle dest) {
        if (src == null) {
            throw new IllegalArgumentException("src==null");
        }
        CellStyle result = new CellStyle(src);
        if (dest == null) {
            return src;
        }
        if (dest.getBackground() != null) {
            result.setBackground(dest.getBackground());
        }
        if (dest.getForeground() != null) {
            result.setForeground(dest.getForeground());
        }
        if (dest.getBorderColor() != null) {
            result.setBorderColor(dest.getBorderColor());
        }
        if (dest.getFont() != null) {
            result.setFont(dest.getFont());
        }
        if (dest.getBorder() != null) {
            result.setBorder(merge(src.getBorder(), dest.getBorder()));
        }
        return result;
    }

    private Border merge(Border src, Border dest) {
        return mergeSetDestNotNull(src, dest);
    }

    /**
     * Устаналивает факт, что блок пустой.
     * Возвращает true, если выполняются следующие условия:
     * 1. Не закрашивается фон
     * 2. Нет изображения
     * 3. Нет внутреннего бордюра
     * @param block блок
     * @return пустой (true) или нет
     */
    private boolean isEmptyBlock(GraphicBlock block) {
        if (block == null) {
            return true;
        }
        if (!block.getFillBackground() && block.getImage() == null && block.getBorder() == null) {
            return true;
        }
        return false;
    }

    /**
     * Cоздает бордюр из двух.
     * Копирует первй бордюр. Если во втором бордюре указаны не пустые блоки, то заменяет их в конечном
     *
     * @param src Первый бордюр
     * @param dest Второй бордюр
     * @return Новый бордюр
     */
    private Border mergeSetDestNotNull(Border src, Border dest) {
        if (src == null) {
            return dest;
        }
        if (dest == null) {
            return src;
        }
        BasicBorder result = new BasicBorder();
        result.setRight(src.getRight());
        result.setLeft(src.getLeft());
        result.setTop(src.getTop());
        result.setBottom(src.getBottom());
        result.setLeftTop(src.getLeftTop());
        result.setLeftBottom(src.getLeftBottom());
        result.setRightTop(src.getRightTop());
        result.setRightBottom(src.getRightBottom());
        result.setCenter(src.getCenter());
        result.setLeftColumnWidth(src.getLeftColumnWidth());
        result.setRightColumnWidth(src.getRightColumnWidth());
        result.setTopRowHeight(src.getTopRowHeight());
        result.setBottomRowHeight(src.getBottomRowHeight());
        if (!isEmptyBlock(dest.getRight())) {
            result.setRight(dest.getRight());
            result.setRightColumnWidth(dest.getRightColumnWidth());
        }
        if (!isEmptyBlock(dest.getLeft())) {
            result.setLeft(dest.getLeft());
            result.setLeftColumnWidth(dest.getLeftColumnWidth());
        }
        if (!isEmptyBlock(dest.getTop())) {
            result.setTop(dest.getTop());
            result.setTopRowHeight(dest.getTopRowHeight());
        }
        if (!isEmptyBlock(dest.getBottom())) {
            result.setBottom(dest.getBottom());
            result.setBottomRowHeight(dest.getBottomRowHeight());
        }
        if (!isEmptyBlock(dest.getLeftTop())) {
            result.setLeftTop(dest.getLeftTop());
        }
        if (!isEmptyBlock(dest.getLeftBottom())) {
            result.setLeftBottom(dest.getLeftBottom());
        }
        if (!isEmptyBlock(dest.getRightTop())) {
            result.setRightTop(dest.getRightTop());
        }
        if (!isEmptyBlock(dest.getRightBottom())) {
            result.setRightBottom(dest.getRightBottom());
        }
        if (!isEmptyBlock(dest.getCenter())) {
            result.setCenter(dest.getCenter());
        }
        return result;
    }
}
