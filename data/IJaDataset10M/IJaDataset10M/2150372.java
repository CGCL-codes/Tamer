package com.calipso.reportgenerator.userinterface;

import com.calipso.reportgenerator.common.exception.InfoException;
import java.util.EventListener;
import java.awt.*;

/**
 * Interface utilizada en los momentos de drag and drop para el intercambio de los paneles
 */
public interface FieldPositionListener extends EventListener {

    public void fieldPositionChanged(String positionName, String fieldName, Point location) throws InfoException;
}
