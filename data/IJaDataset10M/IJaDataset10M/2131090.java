package src.main.java.vinko.beans;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import src.main.java.vinko.WineLover;
import src.main.java.vinko.WinePage;

public class WinePageBean implements WinePage {

    private String name;

    private String winePageInfo;

    private LinkedList<WineLover> wineLovers = new LinkedList<WineLover>();

    public WinePageBean() {
    }

    public WinePageBean(String name, String winePageInfo) {
        super();
        this.name = name;
        this.winePageInfo = winePageInfo;
    }

    @Override
    public LinkedList<WineLover> getWineLovers() {
        return wineLovers;
    }

    public void setWineLovers(LinkedList<WineLover> wineLovers) {
        this.wineLovers = wineLovers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getWinePageInfo() {
        return winePageInfo;
    }

    public void setWinePageInfo(String winePageInfo) {
        this.winePageInfo = winePageInfo;
    }
}
