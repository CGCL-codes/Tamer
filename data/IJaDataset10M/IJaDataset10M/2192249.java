package polr.client.ui;

import polr.client.ui.base.Container;

public class BadgePane extends Container {

    public BadgePane() {
        super();
        initGUI();
    }

    private void initGUI() {
        this.setSize(224, 256);
    }

    public void addBadge(String badge) {
        switch(badge.charAt(0)) {
            case 'k':
                break;
            case 'j':
                break;
            case 'h':
                break;
            case 's':
                break;
            case 'o':
                break;
        }
    }
}
