package com.hifi.plugin.ui.theme.metal.plaf.sourcetree.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.LinearGradientPaint;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;
import com.hifi.plugin.ui.theme.metal.plaf.basic.menu.BasicSmoothMenuBorder;

public class SourceTreeMenuBorder extends BasicSmoothMenuBorder {

    @InjectedResource
    protected Color outerBorderColor;

    @InjectedResource
    protected Color innerBorderColor;

    @InjectedResource
    protected LinearGradientPaint headerGradient;

    @InjectedResource
    protected BasicStroke headerStroke;

    @InjectedResource
    protected Font headerFont;

    @InjectedResource
    protected int headerHeight;

    @InjectedResource
    protected Color headerTextColor;

    public SourceTreeMenuBorder() {
        super();
        ResourceInjector.get().inject(this);
        super.outerBorderColor = outerBorderColor;
        super.innerBorderColor = innerBorderColor;
        super.headerGradient = headerGradient;
        super.headerStroke = headerStroke;
        super.headerFont = headerFont;
        super.headerHeight = headerHeight;
        super.headerTextColor = headerTextColor;
        init("", null);
    }
}
