package n3_project;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import n3_project.helpers.GuiResourceBundle;

public class HelpMenu extends JMenu {

    private static final long serialVersionUID = 1L;

    public static final String HTTP_EULERGUI_SVN_SOURCEFORGE = "http://eulergui.svn.sourceforge.net/svnroot/eulergui/trunk/eulergui";

    HelpMenu(ProjectGUI projectGUI) {
        setName(GuiResourceBundle.getString("help"));
        setText(GuiResourceBundle.getString("help"));
        add(new ShowHelp());
        add(new ShowVersion());
        projectGUI.getEulerMenuBar().add(this);
    }

    class ShowHelp extends AbstractAction {

        private static final long serialVersionUID = 1L;

        ShowHelp() {
            putValue(NAME, GuiResourceBundle.getString("documentation"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(HTTP_EULERGUI_SVN_SOURCEFORGE + "/html/documentation.html"));
                } catch (final IOException e1) {
                    e1.printStackTrace();
                } catch (final URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
