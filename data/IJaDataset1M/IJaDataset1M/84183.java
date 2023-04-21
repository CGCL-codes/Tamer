package bntu.iss.diplom.creator.main;

import bntu.iss.diplom.creator.gui.InputFileForm;
import bntu.iss.diplom.creator.providers.ConfigProvider;

public class Application {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SplashWindow wnd = new SplashWindow();
        wnd.show();
        ConfigProvider.getInstance();
        InputFileForm form = new InputFileForm();
        form.setVisible(true);
        wnd.setVisible(false);
        wnd = null;
    }
}
