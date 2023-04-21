package ala.infosource.ausmarinv;

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
 * Site map generator for Online  photographic  guide  to  marine  invertebrates  of  southern  Australia.
 * 
 * @author Dave Martin (David.Martin@csiro.au)
 */
@SiteMapGenerator(longName = "Marine invertebrates of Southern Australia", shortName = "ausmarinv")
public class GenerateAusMarineInvertSiteMap {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String rootUrl = "http://www.ausmarinverts.net/Species_list.html";
        Writer writer = ExtractUtils.getSiteMapWriter("ausmarinv");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME, ColumnHeaders.GENUS, ColumnHeaders.PHYLUM });
        String content = WebUtils.getUrlContentAsString(rootUrl);
        Pattern p = Pattern.compile("(?:href=\")?" + "([A-Za-z\\_\\-]{1,}\\.html)" + "(?:.*>)?" + "(?:<em>)?" + "([A-Za-z'\\.\\- ]{1,})" + "(?:</em>)?" + "(?:</a>)?");
        List<String> ignoreUrls = new ArrayList<String>();
        ignoreUrls.add("index.html");
        Matcher m = p.matcher(content);
        int startIdx = 0;
        while (m.find(startIdx)) {
            if (m.groupCount() == 2) {
                if (!ignoreUrls.contains(m.group(1))) {
                    String url = "http://www.ausmarinverts.net/" + m.group(1);
                    String scientificName = m.group(2);
                    writer.write(url);
                    writer.write('\t');
                    writer.write(scientificName);
                    writer.write('\t');
                    writer.write(ExtractUtils.extractGenericName(scientificName));
                    writer.write('\t');
                    writer.write("Mollusca");
                    writer.write('\n');
                }
            }
            startIdx = m.end();
        }
        writer.flush();
        writer.close();
    }
}
