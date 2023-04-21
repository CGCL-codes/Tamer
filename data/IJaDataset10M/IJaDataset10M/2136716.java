package storyteller.ui;

import java.io.File;

public interface Dialog {

    public File openDialog();

    public File saveDialog();

    public void messageDialog(String message);
}
