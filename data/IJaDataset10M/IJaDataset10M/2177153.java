package com.ebixio.virtmus.options;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public final class VirtmusOptionsCategory extends OptionsCategory {

    @Override
    public Icon getIcon() {
        return new ImageIcon(ImageUtilities.loadImage("com/ebixio/virtmus/resources/VirtMus32x32.png"));
    }

    @Override
    public String getCategoryName() {
        return NbBundle.getMessage(VirtmusOptionsCategory.class, "OptionsCategory_Name_VirtMusOpt");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(VirtmusOptionsCategory.class, "OptionsCategory_Title_VirtMusOpt");
    }

    @Override
    public OptionsPanelController create() {
        return new VirtmusOptionsPanelController();
    }
}
