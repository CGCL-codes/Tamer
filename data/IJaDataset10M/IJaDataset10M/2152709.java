package ala.infosource.ozanimals;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ala.infosource.ColumnHeaders;
import ala.infosource.ExtractUtils;
import ala.infosource.SiteMapGenerator;
import ala.infosource.WebUtils;

/**
 * Site map generator for Oz Animals  
 * 
 * @author Tommy Wang (tommy.wang@csiro.au)
 */
@SiteMapGenerator(longName = "ozanimals", shortName = "ozanimals")
public class GenerateOzAnimalsSiteMap implements Runnable {

    public static void main(String[] args) throws Exception {
        List<String> speciesIndexPageList = new ArrayList<String>();
        String[] speciesIndexPages = { "http://www.ozanimals.com/australian-bird-index.html", "http://www.ozanimals.com/australian-mammal-index.html", "http://www.ozanimals.com/australian-reptile-index.html", "http://www.ozanimals.com/australian-fish-index.html", "http://www.ozanimals.com/australian-insect-index.html", "http://www.ozanimals.com/australian-spider-index.html", "http://www.ozanimals.com/australian-frog-index.html" };
        Writer writer = ExtractUtils.getSiteMapWriter("ozanimals");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME });
        for (String speciesIndexPage : speciesIndexPages) {
            String speciesIndex = WebUtils.getUrlContentAsString(speciesIndexPage);
            Pattern p = Pattern.compile("(?:<a href = \")" + "(http://www\\.ozanimals\\.com/[" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "/]{1,}\\.html)" + "(?:\" title=\"[" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "]{0,}\">[\\s]{0,})" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "]{1,})" + "(?:[\\s]{0,}</a>)");
            Matcher m = p.matcher(speciesIndex);
            int searchIdx = 0;
            while (m.find(searchIdx)) {
                int endIdx = m.end();
                String url = m.group(1);
                String sciName = m.group(2);
                writer.write(url);
                writer.write('\t');
                writer.write(sciName);
                writer.write('\n');
                writer.flush();
                System.out.println("URL:" + url + "\tSCI NAME:" + sciName);
                searchIdx = endIdx;
            }
        }
        writer.flush();
        writer.close();
    }

    /**
	 * @see java.lang.Runnable#run()
	 */
    @Override
    public void run() {
    }
}
