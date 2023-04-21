package cham.open.pattern.command;

import java.util.logging.Logger;

/**
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 * 
 * This the Client in Command Pattern speak
 *
 */
public class Application {

    private static Logger logger = Logger.getLogger(Application.class.getName());

    private DashBoard dashBoard;

    public DashBoard getDashBoard() {
        return dashBoard;
    }

    public void setDefaultDashBoard() {
        dashBoard = new DashBoard();
        NewCommand newCommand = new NewCommand(new Document());
        dashBoard.setButton(0, newCommand);
        OpenCommand openCommand = new OpenCommand(new Document());
        dashBoard.setButton(1, openCommand);
        DisplayAboutCommand aboutCommand = new DisplayAboutCommand(new AboutWindow());
        dashBoard.setButton(2, aboutCommand);
    }
}
