package net.sf.avocado_cad.eclipse.ui.views;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.eclipse.ui.IGLView;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import ui.menuet.Menuet;
import ui.menuet.MenuetBuilder;
import ui.menuet.MenuetToolboxDialog;
import ui.paramdialog.DynParamDialog;
import ui.quicksettings.QuickSettings;
import ui.treeviewer.TreeViewer;
import ui.utilities.AvoColors;

public class MainView extends ViewPart {

    public static final String MAIN_VIEW_ID = "net.sf.avocado_cad.eclipse.ui.MainView";

    SashForm comp2topSash;

    Composite mainViewComp;

    @Override
    public void createPartControl(Composite parent) {
        GridLayout gl = new GridLayout();
        gl.numColumns = 2;
        gl.marginWidth = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.verticalSpacing = 0;
        gl.horizontalSpacing = 0;
        parent.setLayout(gl);
        AvoGlobal.menuet = new Menuet(parent, SWT.NONE);
        MenuetBuilder.buildMenuet(AvoGlobal.menuet);
        AvoGlobal.menuet.setBackground(AvoColors.COLOR_MENUET_PROJECT);
        GridData gd0 = new GridData(GridData.FILL_VERTICAL);
        gd0.grabExcessVerticalSpace = true;
        gd0.widthHint = Menuet.MENUET_WIDTH;
        gd0.minimumWidth = Menuet.MENUET_WIDTH;
        AvoGlobal.menuet.setLayoutData(gd0);
        Composite comp2 = new Composite(parent, SWT.NONE);
        GridData gd1 = new GridData(GridData.FILL_BOTH);
        gd1.grabExcessHorizontalSpace = true;
        gd1.grabExcessVerticalSpace = true;
        comp2.setLayoutData(gd1);
        comp2.setBackground(new Color(parent.getDisplay(), 100, 100, 200));
        GridLayout gl2 = new GridLayout();
        gl2.numColumns = 1;
        gl2.marginWidth = 0;
        gl2.marginHeight = 0;
        gl2.marginWidth = 0;
        gl2.verticalSpacing = 0;
        gl2.horizontalSpacing = 0;
        comp2.setLayout(gl2);
        comp2topSash = new SashForm(comp2, SWT.NONE);
        comp2topSash.setBackground(new Color(parent.getDisplay(), 200, 100, 100));
        comp2topSash.SASH_WIDTH = 5;
        GridData gd2top = new GridData(GridData.FILL_BOTH);
        gd2top.grabExcessHorizontalSpace = true;
        gd2top.grabExcessVerticalSpace = true;
        comp2topSash.setLayoutData(gd2top);
        mainViewComp = new Composite(comp2topSash, SWT.NONE);
        AvoGlobal.paramDialog = new DynParamDialog(mainViewComp);
        AvoGlobal.toolboxDialog = new MenuetToolboxDialog(mainViewComp);
        IExtension[] exs = Platform.getExtensionRegistry().getExtensionPoint("net.sf.avocado_cad.eclipse.ui.ViewContributor").getExtensions();
        for (int j = 0; j < exs.length; j++) {
            IConfigurationElement[] elements = exs[j].getConfigurationElements();
            for (int i = 0; i < elements.length; i++) {
                String vcc = elements[i].getAttribute("viewContributorClass");
                if (vcc != null) {
                    try {
                        AvoGlobal.glView = (IGLView) elements[i].createExecutableExtension("viewContributorClass");
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }
        AvoGlobal.glView.setup(mainViewComp);
        mainViewComp.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
            }

            public void controlResized(ControlEvent e) {
                AvoGlobal.glView.setBounds(mainViewComp.getBounds());
                AvoGlobal.paramDialog.positionParamDialog();
                AvoGlobal.toolboxDialog.positionToolboxDialog();
            }
        });
        AvoGlobal.treeViewer = new TreeViewer(comp2topSash, SWT.NONE);
        comp2topSash.setWeights(new int[] { 5, 1 });
        QuickSettings comp2bot = new QuickSettings(comp2, SWT.NONE);
        GridData gd2bot = new GridData(GridData.FILL_HORIZONTAL);
        gd2bot.grabExcessHorizontalSpace = true;
        gd2bot.heightHint = 30;
        gd2bot.minimumHeight = 30;
        comp2bot.setLayoutData(gd2bot);
    }

    @Override
    public void setFocus() {
    }
}
