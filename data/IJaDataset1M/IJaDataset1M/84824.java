package src.main.java.vinko.tapestry.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import src.main.java.vinko.WineLover;
import src.main.java.vinko.beans.WineLoverBean;
import src.main.java.vinko.service.WineLovers;

public class Index {

    @Property
    @Persist("flash")
    private WineLover wineLover;

    @Inject
    private WineLovers wineLovers;

    @SetupRender
    public void createObject() {
        wineLover = new WineLoverBean();
    }

    void onSuccess() {
        wineLovers.add(wineLover);
    }
}
