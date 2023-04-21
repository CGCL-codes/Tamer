package frostcode.icetasks.action.info;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import frostcode.icetasks.action.MnemonicAction;
import frostcode.icetasks.gui.info.InfoDialog;
import frostcode.icetasks.i18n.I18n;

@Singleton
public class About extends MnemonicAction {

    private static final long serialVersionUID = 1L;

    @Inject
    private InfoDialog infoDialog;

    @Inject
    public About(final I18n i18n) {
        super(i18n.get("menu.about"));
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("icons/information.png")));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        infoDialog.setVisible(true);
    }
}
