package net.sourceforge.pyrus.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import net.sourceforge.pyrus.gfx.api.ComponentGfx;
import net.sourceforge.pyrus.hal.annotation.Dependency;
import net.sourceforge.pyrus.screen.api.Theme;
import net.sourceforge.pyrus.widget.api.InputConverter;
import net.sourceforge.pyrus.widget.api.PButton;
import net.sourceforge.pyrus.widget.api.PButtonsMenu;
import net.sourceforge.pyrus.widget.api.PIcon;
import net.sourceforge.pyrus.widget.api.PIconsExplorer;
import net.sourceforge.pyrus.widget.api.PLabel;
import net.sourceforge.pyrus.widget.api.PListWithImage;
import net.sourceforge.pyrus.widget.api.PTextIcon;
import net.sourceforge.pyrus.widget.api.PTextIconsExplorer;
import net.sourceforge.pyrus.widget.api.PWidget;
import net.sourceforge.pyrus.widget.api.WidgetFactory;
import net.sourceforge.pyrus.widget.api.InputConverter.Listener;
import net.sourceforge.pyrus.widget.api.PIcon.ResizePolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WidgetFactoryDevice implements WidgetFactory {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(WidgetFactoryDevice.class);

    @Dependency
    private Theme theme;

    @Dependency
    private ComponentGfx compGfx;

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return "Widgets factory";
    }

    public void start() {
    }

    public void stop() {
    }

    public PButton getButton(String text, String iconName) {
        return new PButtonImpl(this, compGfx, theme.getImage("button"), theme.getImage(iconName), text);
    }

    public PButtonsMenu getButtonsMenu() {
        return new PButtonsMenuImpl();
    }

    public PIconsExplorer getIconsExplorer(String title, List<PIcon> icons) {
        return new PIconsExplorerImpl(title, icons);
    }

    public PTextIconsExplorer getTextIconsExplorer(String title, List<PTextIcon> covers) {
        return new PTextIconsExplorerImpl(title, covers);
    }

    public PTextIcon getTextIcon(String title, Image icon) {
        return new PTextIconImpl(this, compGfx, title, icon);
    }

    public PListWithImage getListWithImage(int rows, String title, PIcon icon, List<PLabel> labels) {
        return new PListWithImageImpl(rows, title, icon, labels);
    }

    public PLabel getLabel(String text, Color color, Font font) {
        return new PLabelImpl(this, compGfx, text, color, font);
    }

    public PIcon getIcon(Image image, ResizePolicy resizePolicy) {
        return new PIconImpl(this, compGfx, image, resizePolicy);
    }

    public InputConverter attachInputConverter(PWidget widget, Listener listener) {
        InputConverter ic = new InputConverterImpl();
        ic.setWidget(widget, listener);
        return ic;
    }
}
