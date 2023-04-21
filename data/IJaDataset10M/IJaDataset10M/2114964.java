package org.openeccos.demo;

import java.util.Calendar;
import java.util.Random;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.openeccos.EccosAppInstance;
import org.openeccos.EccosDesktop;
import org.openeccos.EccosPerspective;
import org.openeccos.dao.EccosRepository;
import org.openeccos.dao.EccosService;
import org.openeccos.demo.action.CmdOpenDemoDialog;
import org.openeccos.demo.gui.FrmDemo;
import org.openeccos.demo.gui.WzdDemo;
import org.openeccos.gui.EccLE.DISPLAY_MODE;
import org.openeccos.model.MTask;
import org.openeccos.model.MUser;
import org.openeccos.util.EccosDefaultLookAndFeel;
import org.openeccos.util.ILookAndFeel;
import com.sas.framework.expojo.ModelExposer;
import echopointng.Menu;
import echopointng.MenuBar;
import echopointng.MenuItem;

/**
 * The default perspective for the Eccos Demo. The perspective defines the menus and the L&F
 * @author cgspinner@web.de
 */
public class DemoPerspective extends EccosPerspective {

    private ILookAndFeel colorScheme;

    @Override
    public ILookAndFeel getLookAndFeel() {
        if (colorScheme == null) {
            colorScheme = new EccosDefaultLookAndFeel() {

                public String getFooterLogo() {
                    return "!eccOS Demo";
                }

                @Override
                public String getDesktopLogo() {
                    return "img/eccOS.gif";
                }
            };
        }
        return colorScheme;
    }

    @Override
    public void updateMenu() {
        MenuBar menuBar = getMenuBar();
        menuBar.removeAll();
        Menu mnu = getMenu("Demo");
        menuBar.add(mnu);
        MenuItem mit = getMenuItem("eccLE Demo 1");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FrmDemo frm = new FrmDemo(DISPLAY_MODE.SIDE_SCROLL);
                frm.setWidth(new Extent(690));
                frm.setHeight(new Extent(470));
                desktop.addWindow(frm);
            }
        });
        mnu.add(mit);
        mit = getMenuItem("eccLE Demo 2");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FrmDemo frm = new FrmDemo(DISPLAY_MODE.EXPANDABLE);
                frm.setWidth(new Extent(500));
                frm.setHeight(new Extent(530));
                desktop.addWindow(frm);
            }
        });
        mnu.add(mit);
        mit = getMenuItem("Wizard");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                desktop.addWindow(new WzdDemo());
            }
        });
        mnu.add(mit);
        mit = getMenuItem("Make Task");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mitTaskClicked();
            }
        });
        mnu.add(mit);
        mit = getMenuItem("Make Shortcut");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mitShortcutClicked();
            }
        });
        mnu.add(mit);
        mit = getMenuItem("Change Perspective");
        mit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                desktop.switchPerspective(new DemoPerspective2());
            }
        });
        mnu.add(mit);
    }

    protected void mitTaskClicked() {
        MTask task = new MTask();
        task.setActionClass(CmdOpenDemoDialog.class.getCanonicalName());
        task.setAssignedUser(EccosDesktop.getCurrentUser());
        task.setCreatedBy(EccosDesktop.getCurrentUser());
        task.setCreatedDate(Calendar.getInstance().getTime());
        task.setName("New Task");
        task.setDescription("This is a demo task");
        EccosService service = (EccosService) ModelExposer.get().getService(EccosService.NAME);
        service.persist(task);
        desktop.refreshTaskDisplay(true);
    }

    protected void mitShortcutClicked() {
        MTask task = new MTask();
        task.setActionClass(CmdOpenDemoDialog.class.getCanonicalName());
        task.setAssignedUser(EccosDesktop.getCurrentUser());
        task.setCreatedBy(EccosDesktop.getCurrentUser());
        task.setCreatedDate(Calendar.getInstance().getTime());
        task.setName("Hello World Shortcut");
        task.setShortcut(true);
        task.setShortcutX(250 + new Random().nextInt(400));
        task.setShortcutY(50 + new Random().nextInt(120));
        EccosService service = (EccosService) ModelExposer.get().getService(EccosService.NAME);
        service.persist(task);
        desktop.refreshTaskDisplay();
    }

    @Override
    protected void onInit() {
        EccosRepository repo = (EccosRepository) ModelExposer.get().getRepository(EccosRepository.NAME);
        String remoteIP = EccosAppInstance.getBrowserSession().getRemoteHost();
        MUser user = repo.findUserByLogin(remoteIP, "eccos");
        if (user == null) {
            user = new MUser();
            user.setLogin(remoteIP);
            user.setPassword("eccos");
            EccosService service = (EccosService) ModelExposer.get().getService(EccosService.NAME);
            service.persist(user);
        }
        EccosAppInstance.getInstance().getUserSession().doLogin(user.getLogin(), "eccos");
        desktop.updateLoginState();
    }
}
