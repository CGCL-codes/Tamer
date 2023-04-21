package cham.open.pattern.command;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 * 
 * This is a Concreate Command
 */
class DisplayAboutCommand implements Command {

    private AboutWindow aboutWindow;

    public DisplayAboutCommand(AboutWindow aboutWindow) {
        this.aboutWindow = aboutWindow;
    }

    /**
     * @inheritDoc
     */
    public void excute() {
        aboutWindow.popUp();
    }
}
