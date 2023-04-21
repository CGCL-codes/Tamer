package fi.arcmaster.gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import com.cloudgarden.resource.SWTResourceManager;
import fi.arcmaster.gui.factories.CompetitionsTableFactory;
import fi.arcmaster.gui.factories.LayoutFactory;

public class CompetitionsCustomTabItem extends CustomTabItem {

    public CompetitionsCustomTabItem(CTabFolder parent, int style) {
        super(parent, style);
    }

    public void init() {
        this.setText("Kilpailut");
        this.setImage(SWTResourceManager.getImage("media/tango-icon-theme-0.8.90/32x32/categories/applications-games.png"));
        {
            Composite layoutFrame = new Composite(this.getParent(), SWT.SHADOW_ETCHED_IN | SWT.WRAP);
            this.setControl(layoutFrame);
            FormLayout composite2Layout = new FormLayout();
            layoutFrame.setLayout(composite2Layout);
            initLeftMenu(layoutFrame);
            initContext(layoutFrame);
        }
    }

    private void initContext(Composite layoutFrame) {
        {
            CompetitionsTableFactory contextTableFactory = new CompetitionsTableFactory(layoutFrame, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
            contextTableFactory.createContextTable();
        }
    }

    private void initLeftMenu(Composite layoutFrame) {
        LayoutFactory layoutFactory;
        layoutFactory = new LayoutFactory(layoutFrame, SWT.FLAT);
        Composite leftMenu = layoutFactory.createLeftMenu();
        layoutFactory = new LayoutFactory(leftMenu, SWT.FLAT);
        Composite menuButtons = layoutFactory.createLeftMenuButtonFrame();
        layoutFactory = new LayoutFactory(menuButtons, SWT.FLAT);
        layoutFactory.createMenuButton("Uusi kilpailu", "media/tango-icon-theme-0.8.90/32x32/actions/document-new.png");
        layoutFactory.createMenuButton("Tulokset", "media/tango-icon-theme-0.8.90/32x32/status/folder-open.png");
        layoutFactory.createMenuButton("Tulosta", "media/tango-icon-theme-0.8.90/32x32/devices/printer.png");
        layoutFactory.createMenuButton("Muokkaa kilpailua", "media/tango-icon-theme-0.8.90/32x32/categories/preferences-system.png");
        additionalInfo(leftMenu);
    }

    private void additionalInfo(Composite leftMenu) {
        {
            Text text2 = new Text(leftMenu, SWT.READ_ONLY);
            text2.setText("Kilpailun tiedot");
            text2.setFont(SWTResourceManager.getFont("Verdana", 8, 1, false, false));
        }
        {
            Text text1_ = new Text(leftMenu, SWT.MULTI | SWT.READ_ONLY);
            text1_.setText("5 kilpailijaa\n2 seuraa\n3 sarjaa\n\n");
            text1_.setFont(SWTResourceManager.getFont("Verdana", 8, 0, false, false));
        }
    }
}
