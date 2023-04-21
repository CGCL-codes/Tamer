package org.jpedal.external;

import org.jpedal.objects.acroforms.formData.GUIData;
import java.awt.*;

public interface CustomFormPrint {

    boolean print(Graphics2D g2, int currentComp, Component comp, GUIData ref);
}
