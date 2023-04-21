package ch.nostromo.tiffanys.engines;

public class EngineSettings {

    private int searchDepth = 4;

    public EngineSettings(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }
}
