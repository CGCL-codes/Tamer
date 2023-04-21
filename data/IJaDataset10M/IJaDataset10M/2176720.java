package com.makeabyte.jhosting.client.session;

import java.util.List;
import com.icesoft.faces.component.menubar.MenuItem;

public interface ApplicationMenu {

    public List<MenuItem> getModel();

    public void prepareMenu();
}
