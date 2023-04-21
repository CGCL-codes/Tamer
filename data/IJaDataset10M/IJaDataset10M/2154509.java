package de.scheidgen.affirmator;

import java.util.ArrayList;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import de.scheidgen.affirmator.affirmations.Affirmations;
import de.scheidgen.affirmator.affirmations.Category;
import de.scheidgen.affirmator.affirmations.Schedule;
import de.scheidgen.affirmator.affirmations.presentation.AffirmationsEditor;
import de.scheidgen.affirmator.affirmations.presentation.AffirmationsEditorPlugin;
import de.scheidgen.affirmator.affirmations.presentation.AffirmationsEditorAdvisor.WindowActionBarAdvisor;

public class Affirmator {

    private TrayItem fTrayItem;

    private Shell fShell;

    private WindowActionBarAdvisor fActionBarAvisor;

    private IWorkbenchWindow fWindow = null;

    private int every = 60;

    private static Image trayImage = AbstractUIPlugin.imageDescriptorFromPlugin(AffirmationsEditorPlugin.PLUGIN_ID, "/icons/icon.gif").createImage();

    private static Image madImage = AbstractUIPlugin.imageDescriptorFromPlugin(AffirmationsEditorPlugin.PLUGIN_ID, "/icons/mad.gif").createImage();

    public void configure(IWorkbenchWindow window, WindowActionBarAdvisor actionBarAdvisor) {
        fShell = window.getShell();
        final Tray tray = fShell.getDisplay().getSystemTray();
        fTrayItem = new TrayItem(tray, SWT.NONE);
        fTrayItem.setImage(trayImage);
        fTrayItem.setToolTipText("TrayItem");
        fActionBarAvisor = actionBarAdvisor;
        fWindow = window;
        createMinimize();
        hookPopupMenu();
    }

    public TrayItem getTrayItem() {
        return fTrayItem;
    }

    public void start() {
        new Thread(new Runnable() {

            public void run() {
                Affirmator.this.run();
            }
        }).start();
    }

    private void update() {
        Schedule schedule = getSchedule();
        if (schedule != null) {
            every = schedule.getEvery();
        }
    }

    public void run() {
        long last = -1;
        for (; ; ) {
            try {
                Thread.sleep(5000);
                update();
                long current = System.currentTimeMillis() / (every * 60000);
                if (last < current) {
                    switchMad(true);
                }
                last = current;
            } catch (InterruptedException e) {
            }
        }
    }

    private Affirmations getAffirmations() {
        IEditorPart editor = fWindow.getActivePage().getActiveEditor();
        if (editor == null || !(editor instanceof AffirmationsEditor)) {
            return null;
        }
        Affirmations affirmations = (Affirmations) ((AffirmationsEditor) editor).getEditingDomain().getResourceSet().getResources().get(0).getContents().get(0);
        return affirmations;
    }

    private Schedule getSchedule() {
        IEditorPart editor = fWindow.getActivePage().getActiveEditor();
        if (editor == null || !(editor instanceof AffirmationsEditor)) {
            return null;
        }
        Schedule schedule = (Schedule) ((AffirmationsEditor) editor).getEditingDomain().getResourceSet().getResources().get(0).getContents().get(1);
        return schedule;
    }

    private static class RandomList<E> extends ArrayList<E> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean add(E e) {
            int index = (int) Math.round(Math.random() * size());
            add(index, e);
            return true;
        }
    }

    public String getAffirmationText() {
        Schedule schedule = getSchedule();
        RandomList<Category> categories = new RandomList<Category>();
        for (Category category : getAffirmations().getCategories()) {
            categories.add(category);
        }
        StringBuffer affirmations = new StringBuffer();
        for (int i = 0; i < schedule.getNumberOfCategories(); i++) {
            Category category = categories.get(i);
            int index = (int) Math.round(Math.random() * category.getAffirmations().size());
            affirmations.append(category.getAffirmations().get(index).getText());
            affirmations.append("\n");
        }
        return affirmations.toString();
    }

    private void hookPopupMenu() {
        fTrayItem.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                MenuManager trayMenu = new MenuManager();
                Menu menu = trayMenu.createContextMenu(fShell);
                fActionBarAvisor.fillTrayItem(trayMenu);
                menu.setVisible(true);
            }
        });
    }

    private void createMinimize() {
        fShell.addShellListener(new ShellAdapter() {

            public void shellIconified(ShellEvent e) {
                fShell.setVisible(false);
            }
        });
        fTrayItem.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event event) {
                if (!fShell.isVisible()) {
                    fShell.setVisible(true);
                    fShell.setMinimized(false);
                }
            }
        });
    }

    public void switchMad(final boolean mad) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (mad) {
                    fTrayItem.setImage(madImage);
                } else {
                    fTrayItem.setImage(trayImage);
                }
            }
        });
    }

    public void dispose() {
        fTrayItem.dispose();
    }

    public Shell getShell() {
        return fTrayItem.getDisplay().getActiveShell();
    }
}
