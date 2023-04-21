package jalk;

import java.awt.Frame;

public interface ClientTrayIconInterface {

    public boolean init(final Frame parentFrame);

    public boolean isAvailable();

    public void exit();
}
