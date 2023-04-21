package fr.amille.animebrowser.model.serie;

import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;
import fr.amille.animebrowser.model.exception.SerieException;
import fr.amille.animebrowser.model.site.Site;

/**
 * 
 * @author amille
 * 
 */
public class SerieTest {

    String serieTitle;

    String serieNum;

    Serie serie;

    String serieTitle2;

    String serieNum2;

    Serie serie2;

    Vector<Integer> missingEpisodes;

    EpisodeManager episodeManager;

    Episode episode;

    Site site;

    @Test
    public void testAddMissingEpisode() throws SerieException {
        this.serieTitle = "toto";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(1);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(4);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("2"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,2,102"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.addMissingEpisode(105);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,102"));
    }

    @Test
    public void testContainEpisode() throws SerieException {
        this.serieTitle = "toto";
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.toString(), this.serie.isEpisodeAlreadyFound(101));
        Assert.assertFalse(this.serie.toString(), this.serie.isEpisodeAlreadyFound(102));
    }

    @Test
    public void testgetEpisodeMissingNumbersString() throws SerieException {
        this.serieTitle = "toto,titi";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,3"));
        this.serieNum = "1,2,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "1,2";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "101";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("101"));
        this.serieNum = "101";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.episodeManager = EpisodeManager.getInstance();
        this.episodeManager.clear();
        this.episode = new Episode(this.serie, 101);
        this.site = new Site("foo", null, null, null);
        this.episodeManager.addEpisode(this.episode, this.site);
        this.episode.setFind(true);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("102"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.episodeManager = EpisodeManager.getInstance();
        this.episodeManager.clear();
        this.episode = new Episode(this.serie, 2);
        this.site = new Site("foo", null, null, null);
        this.episodeManager.addEpisode(this.episode, this.site);
        this.episode.setFind(false);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,2,102"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.episodeManager = EpisodeManager.getInstance();
        this.episodeManager.clear();
        this.episode = new Episode(this.serie, 104);
        this.site = new Site("foo", null, null, null);
        this.episodeManager.addEpisode(this.episode, this.site);
        this.episode.setFind(false);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,102"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.episodeManager = EpisodeManager.getInstance();
        this.episodeManager.clear();
        this.episode = new Episode(this.serie, 104);
        this.site = new Site("foo", null, null, null);
        this.episodeManager.addEpisode(this.episode, this.site);
        this.episode.setFind(true);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,102,103,105"));
        this.serieNum = "1,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.episodeManager = EpisodeManager.getInstance();
        this.episodeManager.clear();
        this.episode = new Episode(this.serie, 105);
        this.site = new Site("foo", null, null, null);
        this.episodeManager.addEpisode(this.episode, this.site);
        this.episode.setFind(true);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,102-104,106"));
    }

    @Test
    public void testGetEpisodeMissingNumbersString() throws SerieException {
        this.serieTitle = "toto,titi";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,3"));
        this.serieTitle = "toto,titi";
        this.serieNum = "1,2,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieTitle = "toto,titi";
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieTitle = "toto,titi";
        this.serieNum = "101";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("101"));
    }

    @Test
    public void testGetFirstSerieTitle() throws SerieException {
        this.serieTitle = "toto,titi";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getFirstSerieTitle(), this.serie.getFirstSerieTitle().equals("toto"));
    }

    @Test
    public void testHasName() throws SerieException {
        this.serieTitle = "toto,tiét48&i";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serie.hasName("toto"));
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serie.hasName("tiét48&i"));
    }

    @Test
    public void testHasSameNameThat() throws SerieException {
        this.serieTitle = "toto,titi";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serieTitle2 = "titi";
        this.serieNum2 = "1,3";
        this.serie2 = new Serie(this.serieTitle2, this.serieNum2, null);
        Assert.assertTrue(this.serie.getSerieTitlesString() + " : " + this.serie2.getSerieTitlesString(), this.serie.hasSameNameThat(this.serie2));
        this.serieTitle = "toto,titi";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serieTitle2 = "titii";
        this.serieNum2 = "1,3";
        this.serie2 = new Serie(this.serieTitle2, this.serieNum2, null);
        Assert.assertFalse(this.serie.getSerieTitlesString() + " : " + this.serie2.getSerieTitlesString(), this.serie.hasSameNameThat(this.serie2));
    }

    @Test
    public void testRemoveMissingEpisode() throws SerieException {
        this.serieTitle = "toto";
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,3"));
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,3"));
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(1);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("2"));
        this.serieNum = "";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(1);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("2"));
        this.serieNum = "";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,3"));
        this.serieNum = "1,2,102";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeMissingEpisode(2);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1,102"));
    }

    @Test
    public void testRemoveName() throws SerieException {
        this.serieTitle = "toto,tiét48&i";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        this.serie.removeName("toto");
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serie.getSerieTitlesString().equals("tiét48&i"));
        this.serie.removeName("tiét48&i");
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serie.getSerieTitlesString().equals("tiét48&i"));
    }

    @Test
    public void testSerie() throws SerieException {
        this.serieTitle = "toto";
        this.serieNum = "1";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serieTitle.equals(this.serie.getSerieTitlesString()));
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serieNum.equals(this.serie.getEpisodeMissingNumbersString()));
        this.serieTitle = "toto,tiùé&  t445i";
        this.serieNum = "1,3";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serie.getSerieTitlesString().equals(this.serieTitle));
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serieNum.equals(this.serie.getEpisodeMissingNumbersString()));
        this.serieNum = "0";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.getEpisodeMissingNumbersString().equals("1"));
        this.serieNum = "101 ";
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serie.isEpisodeAlreadyFound(99));
        this.serieTitle = null;
        this.serieNum = null;
        this.serie = new Serie(this.serieTitle, this.serieNum, null);
        Assert.assertTrue(this.serie.getSerieTitlesString(), this.serieTitle == null);
        Assert.assertTrue(this.serie.getEpisodeMissingNumbersString(), this.serieNum == null);
    }
}
