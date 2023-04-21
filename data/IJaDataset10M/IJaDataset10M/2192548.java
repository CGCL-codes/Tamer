package org.javanuke.tests.data;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import org.javanuke.articles.ArticleVo;
import org.javanuke.core.model.GenericManager;
import org.javanuke.downloads.DownloadVo;
import org.javanuke.downloads.DownloadsManagerIntf;
import org.javanuke.flickr.FlickrManager;
import org.javanuke.flickr.PhotosetVo;
import org.javanuke.news.NewsVo;
import org.javanuke.tests.utils.TestsConfig;
import com.jnuke.guestbook.GuestbookManagerIntf;
import com.jnuke.guestbook.GuestbookVo;

/**
 * this calss is responsible to create a listo of 100 rows in the data base, in order to have some data to make tests.
 * 
 * @author Franklin Samir
 * 
 */
public class DummyDataLoader extends TestCase {

    private static final int MAX_UNITS = 10;

    private static String[] controlTags = new String[] { "section:foo", "section:bar", "section:goo" };

    public static void main(String[] args) {
        DummyDataLoader.loadAll();
    }

    public static void loadAll() {
        DummyDataLoader.testLoadArticles();
        DummyDataLoader.testLoadNews();
        DummyDataLoader.testCreateDownloads();
    }

    public static void testCreateGuestbooks() {
        GuestbookManagerIntf manager = (GuestbookManagerIntf) TestsConfig.getApplicationContext().getBean("guestbookManager");
        GuestbookVo vo = null;
        for (int i = 0; i < MAX_UNITS; i++) {
            vo = new GuestbookVo();
            vo.setEnabled(true);
            vo.setContent(i + SMALL_CONTENT);
            vo.setTitle(i + " This is a Guestbook");
            vo.setCreated(new Date());
            manager.create(vo);
        }
    }

    public static void testCreatePhotosets() {
        FlickrManager manager = (FlickrManager) TestsConfig.getApplicationContext().getBean("flickrManager");
        String ids[] = { "72157594531572822", "72157594488238868", "72157594481128258", "72157594471460338", "72157594471230490", "72157594460799595", "72157594459572070", "72157594387309470", "72157594459514742", "72157594459461019", "72157594459370647", "72157594363359069", "72157594421666242", "72157594411081605", "72157594387329360", "72157594332689049", "72157594210414400", "72157594143610100", "72057594098368776", "72057594062262822", "72057594065061882", "72057594060397674", "592228", "1768156", "585426" };
        PhotosetVo vo = null;
        for (int i = 0; i < ids.length; i++) {
            vo = new PhotosetVo();
            vo.setFlickrId(ids[i]);
            manager.createPhotoset(vo);
        }
    }

    public static void testCreateDownloads() {
        DownloadsManagerIntf manager = (DownloadsManagerIntf) TestsConfig.getApplicationContext().getBean("downloadsManager");
        DownloadVo vo = null;
        for (int i = 0; i < MAX_UNITS; i++) {
            vo = new DownloadVo();
            vo.setEnabled(true);
            vo.setContent(i + HUGE_CONTENT);
            vo.setTitle(i + " Google Logo");
            vo.setCreated(new Date());
            vo.setUrl("http://www.google.com/intl/en_ALL/images/logo.gif");
            vo.setDescription(i + SMALL_CONTENT);
            manager.create(vo);
        }
    }

    public static void testLoadArticles() {
        GenericManager<ArticleVo> manager = (GenericManager<ArticleVo>) TestsConfig.getApplicationContext().getBean("articlesManager");
        ArticleVo vo = null;
        for (int i = 0; i < MAX_UNITS; i++) {
            vo = new ArticleVo();
            vo.setEnabled(true);
            vo.setContent(i + HUGE_CONTENT);
            vo.setCreated(new Date());
            vo.setSignature(i + SMALL_CONTENT);
            vo.setSummary(i + MEDIUM_CONTENT);
            vo.setContent(i + HUGE_CONTENT);
            vo.setSubtitle(i + " This is a subtitle");
            vo.setTitle(i + TITLE);
            manager.create(vo);
        }
    }

    public static void testLoadNews() {
        GenericManager<NewsVo> newsManager = (GenericManager<NewsVo>) TestsConfig.getApplicationContext().getBean("newsManager");
        NewsVo news = null;
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < MAX_UNITS; i++) {
            news = new NewsVo();
            news.setContent(i + HUGE_CONTENT);
            news.setEnabled(true);
            news.setContent(i + MEDIUM_CONTENT);
            news.setContent(i + SMALL_CONTENT);
            news.setTitle(i + TITLE);
            c.add(Calendar.HOUR_OF_DAY, +1);
            news.setCreated(c.getTime());
            newsManager.create(news);
        }
    }

    private static final String TITLE = " - This is a simple title";

    private static final String SMALL_CONTENT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Cras mauris. Donec volutpat vehicula nulla. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus eget est. Aenean quis ante. Integer posuere tincidunt est. Mauris lorem nisi, sodales a, rhoncus eu, porttitor et, sapien. Integer nulla odio, placerat sed, suscipit sodales, pellentesque id, libero. Mauris laoreet elementum arcu. Nam sollicitudin viverra quam. Etiam fermentum nibh vel mi. Praesent dui ligula, condimentum id, porta a, lacinia congue, nisi. Etiam facilisis vehicula magna. Duis laoreet. Duis leo diam, vehicula nec, condimentum ut, sagittis non, risus. Proin faucibus dui ut neque. Morbi fringilla enim. Nam nec nisl. Duis nunc mauris, varius et, porttitor et, nonummy eget, metus. Proin rhoncus tortor eget nibh.";

    private static final String MEDIUM_CONTENT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec magna nisl, pharetra in, accumsan pharetra, facilisis ut, ante. Curabitur ultricies lorem id mi. Donec euismod nibh ac risus. Donec laoreet, tortor at cursus pellentesque, tellus neque viverra tellus, eu pulvinar tellus ligula eu odio. Integer orci metus, fringilla ultrices, varius sit amet, congue non, ipsum. Suspendisse mauris massa, lobortis at, blandit vel, sollicitudin tincidunt, nisl. Donec sit amet felis nec nunc interdum vehicula. Proin tincidunt est a elit. Aliquam vel tellus in urna dapibus blandit. Nunc sed dolor. Mauris vitae diam sit amet felis egestas laoreet. Cras non mauris non pede iaculis facilisis. Aliquam malesuada rhoncus elit. Vestibulum tincidunt mauris eu magna. Fusce libero pede, aliquam non, ullamcorper sit amet, vulputate vel, metus." + "Cras tempus ullamcorper tortor. Nullam gravida vehicula metus. In hac habitasse platea dictumst. Pellentesque pharetra. Suspendisse vitae nibh. Sed eget velit ornare nisl hendrerit interdum. Nulla pellentesque libero. Cras eros libero, iaculis imperdiet, ultrices nec, sollicitudin eget, tortor. Sed elementum augue ullamcorper nisi. Mauris sed metus. Nullam auctor sapien gravida nunc. Duis eu massa. Proin ornare, sem sed interdum interdum, felis ante lacinia magna, et tincidunt mauris eros eu lorem. Mauris ut augue. Nullam venenatis turpis et neque. Nulla ac diam. Curabitur sit amet ipsum. Suspendisse ultrices, lorem sit amet pretium vulputate, mi arcu blandit lorem, vitae auctor elit ante vel elit. Sed pede nibh, congue a, feugiat et, volutpat id, turpis." + "Cras nunc. Nulla vel ante. Curabitur porttitor nibh vel orci. Nam venenatis nibh ut metus. Suspendisse dignissim magna in magna. Sed id nibh. Donec condimentum, sapien vel dapibus malesuada, ante libero consectetuer nisi, id dignissim nisi enim pharetra odio. Nulla luctus risus. Vestibulum neque. Curabitur semper. Proin eget libero. Integer non purus congue nibh venenatis blandit. Integer consectetuer pede quis ligula. Praesent consectetuer odio in metus. Vivamus porta, mi ut tincidunt eleifend, nisl tellus suscipit elit, id nonummy sapien mauris eget ante. Mauris sed pede non orci consequat blandit." + "Proin purus ipsum, condimentum eu, porttitor ac, commodo sit amet, mi. Cras vel metus ac ipsum porttitor pellentesque. Aenean iaculis lacus in mi. Phasellus fringilla. Etiam faucibus mauris sit amet orci. Nullam pulvinar egestas nibh. Nulla facilisi. Nam bibendum, velit vitae consequat dictum, orci nibh mattis est, non sollicitudin pede leo quis ipsum. Vivamus sit amet turpis. Nulla facilisi. Maecenas commodo pede et nibh. Vivamus pharetra faucibus erat." + "Nam ultrices mollis dolor. Duis elit. Fusce sit amet dolor sed nunc euismod luctus. Sed fringilla dolor at diam. Nunc lobortis adipiscing sem. Nam lacus. Nullam feugiat bibendum dui. Nunc suscipit dui in tortor. Vivamus tellus dui, hendrerit sed, molestie rhoncus, blandit in, odio. Fusce non pede eu odio dignissim venenatis. Praesent congue, leo id scelerisque ornare, orci massa consectetuer purus, sit amet condimentum nisi velit vel sem.";

    private static final String HUGE_CONTENT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nulla et nunc. Nullam non sem. Aliquam porta ipsum ut nisi. Mauris consectetuer ligula quis pede. Pellentesque id purus sit amet massa bibendum gravida. Suspendisse tortor. Quisque turpis. Ut a neque. Praesent facilisis, arcu ac tincidunt condimentum, arcu ligula semper justo, sit amet luctus sem erat ac nisi. Praesent enim tellus, adipiscing quis, convallis id, vestibulum eget, nibh. Ut sapien. Donec mi quam, auctor sit amet, aliquet eu, rhoncus vel, velit." + "Aliquam erat volutpat. Nulla augue. Maecenas nec ipsum a elit scelerisque dapibus. Curabitur molestie molestie turpis. Nam et odio. Sed posuere, urna vel dapibus blandit, tellus elit rhoncus risus, in facilisis sapien magna eu eros. Vestibulum rutrum, odio vitae pulvinar dignissim, urna felis fringilla diam, quis egestas mi mauris id lectus. Donec quam erat, vehicula quis, ultricies at, tincidunt vitae, risus. Morbi aliquet odio vitae urna. In ullamcorper. Nulla eu risus. In hac habitasse platea dictumst. Morbi mauris. Suspendisse potenti. Donec interdum vestibulum ligula. Mauris ut orci. Cras nulla. Donec sagittis dolor sed mi. Sed sollicitudin dictum odio.<br><br>" + "Sed fermentum. Phasellus laoreet magna. Nulla mollis faucibus neque. Morbi ac diam semper lacus viverra egestas. Suspendisse in leo at arcu semper tincidunt. Sed sed dolor ac diam rutrum euismod. Ut ipsum. Donec sollicitudin, felis quis vehicula mollis, magna tortor aliquam nulla, a viverra lectus lacus sit amet eros. Vivamus magna. Sed sodales posuere sem. Quisque aliquet risus ac tortor. Nullam vitae nibh vitae nisi vulputate faucibus. Donec tempus purus eget mi. Ut vulputate. Praesent ac massa. In gravida risus. Donec ultrices, nibh in gravida suscipit, ligula mauris sodales augue, at viverra augue nisi sit amet nulla. Fusce dignissim hendrerit quam. Aliquam facilisis elit eu nisi.<br><br>" + "Pellentesque rhoncus. Donec enim massa, sollicitudin ac, vehicula id, aliquet non, eros. Quisque congue, enim id vulputate tempus, erat odio pretium augue, vel lobortis diam purus quis lectus. Nullam mi urna, ullamcorper non, semper id, tincidunt id, pede. Vestibulum eget est eu mi vulputate fringilla. Nullam aliquet mauris eget arcu. Integer eget ante. Quisque pharetra. Vestibulum elit. Duis condimentum sodales lectus. Aenean auctor nulla quis elit. Phasellus viverra augue et diam. Proin quis massa. Donec vel metus. Morbi rutrum. Maecenas blandit semper libero. Nullam quis eros vitae justo vulputate euismod. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Mauris ligula eros, posuere tempor, condimentum ac, gravida vitae, lacus. Suspendisse porta ligula et lorem.<br><br>" + "In porttitor dignissim velit. Maecenas sed erat. Proin feugiat enim ut dui. Nulla aliquet diam id nibh. Aenean enim leo, dictum vitae, blandit vitae, sodales tincidunt, ligula. Curabitur nisl. In hac habitasse platea dictumst. Integer bibendum mi ut ipsum. Donec fermentum erat. Integer ornare ante ut nunc. Aenean odio nibh, porttitor at, tincidunt ultricies, euismod id, massa. Nulla pulvinar luctus enim. Curabitur sodales, metus id volutpat interdum, tellus nunc pellentesque mi, at placerat diam lacus ac orci. Sed suscipit quam vel lacus. Donec eu tortor ut purus mollis pharetra. Phasellus dolor purus, condimentum ac, pretium sit amet, molestie a, eros.<br><br>" + "Pron ut nulla. Pellentesque erat sem, posuere in, ullamcorper vitae, ullamcorper interdum, nulla. Fusce venenatis, lectus vitae adipiscing cursus, nisi libero sagittis orci, condimentum fringilla elit lacus ac purus. Sed ut ipsum vel purus mollis molestie. Vivamus dapibus. Curabitur augue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus vel odio. Etiam dui. Etiam aliquam augue sit amet lectus. Curabitur tortor nisl, blandit ut, mattis et, mattis quis, justo. Quisque placerat feugiat lectus. Pellentesque in tellus quis sem luctus iaculis. Nam venenatis ultricies sapien. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse fringilla pretium orci. Donec tincidunt.<br><br>" + "Sed vel est eget ipsum aliquet mattis. Nunc molestie magna ornare arcu. In hac habitasse platea dictumst. Suspendisse erat. Phasellus nec magna. Fusce laoreet eleifend mauris. Donec quis purus. Quisque bibendum. Suspendisse molestie lacinia mi. Ut gravida imperdiet velit. Integer ullamcorper mauris eu eros. Morbi mattis consectetuer pede. Phasellus porttitor consectetuer est. Aliquam odio nibh, egestas in, rhoncus nec, bibendum in, nisl.<br><br>" + "hasellus laoreet ligula ac nisl. Duis tristique. Morbi ut magna. In id massa. Aliquam mattis malesuada mi. Morbi nonummy nunc et enim. Morbi congue, nulla eget pharetra adipiscing, eros magna feugiat erat, a auctor metus lacus non est. Proin lobortis aliquet augue. Mauris velit est, ornare ac, tristique at, adipiscing quis, urna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur pretium suscipit libero. Cras massa. Duis pulvinar facilisis purus. Duis elit purus, convallis ac, tristique eget, rutrum eu, justo. Nam sed dui et lacus tempus elementum. Nunc tempus neque vitae augue. Pellentesque a mi. Nam ante diam, aliquam in, pharetra fermentum, accumsan eget, libero. Integer vel augue sit amet felis ornare aliquam.<br><br>" + "Duis mi. Phasellus aliquam mauris eu lectus. Suspendisse eu pede ut elit facilisis porta. Vivamus congue. Proin pharetra elit eu lorem. Suspendisse nec justo. Cras aliquet dictum justo. Vestibulum nisl ligula, scelerisque sed, egestas nec, porta sed, eros. Maecenas ut nibh sed metus pulvinar vehicula. Donec rutrum. Praesent pharetra tortor ac metus. Ut fermentum, magna eleifend cursus vehicula, arcu sapien malesuada mauris, non fringilla pede felis vitae enim. Nam id lorem imperdiet dui molestie pellentesque. Mauris non pede ac sapien bibendum pharetra. Donec condimentum mattis ante. Fusce viverra purus sed est. Proin ultrices mi eget pede.<br><br>" + "Morbi porttitor, lectus eu bibendum mattis, enim pede fermentum elit, sed venenatis neque pede faucibus pede. Integer quis ipsum. Nulla facilisi. Sed ante. Curabitur eget ligula. Vestibulum mollis, massa vitae fermentum tincidunt, dolor nulla rhoncus libero, ac iaculis mauris lacus ac justo. Etiam facilisis, felis id tincidunt sodales, quam dui tempor dui, at elementum tortor tortor vel sapien. Sed id leo. Vestibulum neque nulla, rutrum iaculis, molestie vel, lacinia id, augue. Maecenas sem ligula, aliquam ut, convallis ac, auctor id, elit. Praesent enim. Nunc orci. Pellentesque tincidunt, ligula eu viverra feugiat, velit est commodo metus, vitae placerat nibh ipsum ac tellus. Duis velit ante, aliquet sed, feugiat sed, tincidunt ut, mi. Cras non erat eget arcu dictum ornare. Integer rhoncus, magna bibendum sollicitudin tincidunt, magna erat sodales turpis, ut molestie sem purus lacinia odio.";
}
