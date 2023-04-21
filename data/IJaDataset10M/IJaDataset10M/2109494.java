package org.jazzteam.geom.actions;

import org.jazzteam.geom.ShapesRunnable;
import org.jazzteam.geom.frames.Playground;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Класс описывает событие при изменение состояния(ползунка)
 * компонента JSlider, меняет скорость передвижения фигур.
 *
 * @author: Andrey Avtuchovich
 * @date: 21.03.12 9:27
 */
public class ChangeSpeedListener implements ChangeListener {

    /**
     * Скорость движения фигур может быть от 0 до 100.
     * Минимальная скорость установлена 10, т.к. при уменьшении
     * этой скорости на 0 не увидим движения обьекта.
     * Расчет идет по формуле 110 - значение ползунка.
     *
     * @param changeEvent Событие, с помощью которого получаем состояния обьекта JSlider
     */
    public void stateChanged(ChangeEvent changeEvent) {
        ShapesRunnable.DELAY = 110 - Playground.sliderSpeed.getValue();
    }
}
