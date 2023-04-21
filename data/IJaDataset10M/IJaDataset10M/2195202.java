package org.chemicalcovers.search;

import org.chemicalcovers.ChemicalCovers;
import org.chemicalcovers.model.CoverInfoResult;
import org.chemicalcovers.model.CoverListModel;
import org.chemicalcovers.model.ICoverInfo;
import org.chemicalcovers.model.ICoverList;

/**
 * SearchEngineRunner
 * @author P. Dal Farra
 *
 */
class SearchEngineRunner extends Thread {

    private ISearchEngine searchEngine;

    private String artist;

    private String album;

    private ICoverList coverList;

    public SearchEngineRunner(ISearchEngine engine, String artist, String album, final CoverListModel coverListModel) {
        this.searchEngine = engine;
        this.artist = artist;
        this.album = album;
        this.coverList = new ICoverList() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void add(ICoverInfo coverInfo) {
                coverListModel.add(new CoverInfoResult(coverInfo, SearchEngineRunner.this.searchEngine));
            }
        };
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        try {
            searchEngine.run(artist, album, coverList);
            ChemicalCovers.LOGGER.info("Engine " + searchEngine.getName() + " has terminated normally");
        } catch (StoppedEngineException ex) {
            ChemicalCovers.LOGGER.info(ex.getMessage());
        } catch (Throwable ex) {
            ChemicalCovers.LOGGER.severe("Engine '" + searchEngine.getName() + "' failed : " + ex);
        }
    }
}
