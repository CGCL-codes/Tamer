package com.hifi.plugin.ui.core.modules.displaypane;

import java.awt.image.BufferedImage;
import javax.swing.Icon;
import com.hifi.core.api.ui.constants.IconType;
import com.hifi.core.api.ui.modules.IModulePlaf;

public interface IDisplayPanePlaf extends IModulePlaf {

    public Icon getPaneDecorationIcon();

    public Icon getLeftFlipIcon(IconType type);

    public Icon getRightFlipIcon(IconType type);

    public BufferedImage getPaneLeftBackground();

    public BufferedImage getPaneCenterBackground();

    public BufferedImage getPaneRightBackground();
}
