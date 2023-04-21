package be.vds.jtbdive.client.view;

import java.awt.Component;
import javax.swing.Icon;
import net.infonode.docking.View;
import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.swing.component.I18nable;

public class MyView extends View {

    private String name;

    private Component component;

    private static I18nResourceManager i18Mgr = I18nResourceManager.sharedInstance();

    public MyView(String name, Icon arg1, Component component) {
        super(name, arg1, component);
        this.component = component;
        this.name = name;
    }

    public void adaptTitle() {
        getViewProperties().setTitle(i18Mgr.getString(name));
        if (component instanceof I18nable) ((I18nable) component).changeLanguage();
    }
}
