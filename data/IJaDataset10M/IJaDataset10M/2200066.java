package org.exmaralda.coma;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.sfb538.coma2.actions.CollectCorpusAction;
import org.sfb538.coma2.actions.DumpCorpusAction;
import org.sfb538.coma2.actions.UpdateCheckAction;
import org.sfb538.coma2.fileActions.CreateCorpusFromTranscriptionsAction;
import org.sfb538.coma2.fileActions.NewAction;
import org.sfb538.coma2.fileActions.OpenAction;
import org.sfb538.coma2.fileActions.QuitAction;
import org.sfb538.coma2.fileActions.SaveAction;
import org.sfb538.coma2.helpers.RecentFiles;

/**
 * coma2/org.sfb538.coma2.toolbars/FileBar.java
 * @author woerner
 */
public class ComaMenuBar extends JMenuBar {

    public Coma coma;

    RecentFiles recentFiles;

    public javax.swing.AbstractAction newAction;

    public ComaMenuBar(Coma coma) {
        recentFiles = coma.getRecentFiles();
        coma.status("@create menubar");
        JMenu filemenu = new JMenu(Ui.getText("name.fileMenu"));
        filemenu.add(new NewAction(coma));
        filemenu.add(new OpenAction(coma));
        filemenu.add(new SaveAction(coma));
        filemenu.add(new SaveAction(coma, true));
        filemenu.addSeparator();
        for (int rf = recentFiles.getNrOfRecentFiles() - 1; rf > -1; rf--) {
            filemenu.add(new OpenAction(coma, recentFiles.getRecentFile(rf)));
        }
        filemenu.add(new CollectCorpusAction(coma));
        filemenu.addSeparator();
        filemenu.add(new CreateCorpusFromTranscriptionsAction(coma));
        filemenu.addSeparator();
        filemenu.add(new QuitAction(coma));
        JMenu toolsmenu = new JMenu(Ui.getText("name.toolsMenu"));
        toolsmenu.add(new DumpCorpusAction(coma));
        JMenu helpmenu = new JMenu(Ui.getText("name.helpMenu"));
        helpmenu.add(new HelpAction(coma, IconFactory.createImageIcon("images/help.png")));
        helpmenu.add(new UpdateCheckAction(coma, IconFactory.createImageIcon("images/updateCheck.png")));
        helpmenu.addSeparator();
        helpmenu.add(new AboutAction(coma, IconFactory.createImageIcon("images/info.png")));
        helpmenu.add(new EasterEggAction(coma, IconFactory.createImageIcon("images/aboutness.png")));
        add(filemenu);
        add(toolsmenu);
        add(Box.createHorizontalGlue());
        add(helpmenu);
    }
}
