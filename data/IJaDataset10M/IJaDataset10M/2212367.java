package com.trohko.jfsim.aircraft.cockpit.c2d;

import com.trohko.jfsim.aircraft.cockpit.base.ICockpit;
import com.trohko.jfsim.aircraft.cockpit.c2d.elements.IPanel2D;
import com.trohko.jfsim.core.IPosition2D;
import com.trohko.jfsim.core.ISize2D;
import com.trohko.jfsim.core.IVisualElement2D;

public interface ICockpit2D extends ICockpit<IPosition2D, ISize2D, IVisualElement2D, IPanel2D>, IVisualElement2D {
}
