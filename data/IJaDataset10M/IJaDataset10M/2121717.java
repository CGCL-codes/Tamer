package ala.infosource.fishnames;

import java.io.InputStream;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import ala.infosource.ColumnHeaders;
import ala.infosource.ExtractUtils;
import ala.infosource.SiteMapGenerator;

/**
 * Attempts to derive a site map from fishnames.com.au
 * 
 * This site is makes use of HTTP POST form submission....
 * Currently relies on a copy of HTML search results
 * 
 * @author Dave Martin (David.Martin@csiro.au)
 */
@SiteMapGenerator(longName = "Seafood Services Australia", shortName = "fishnames")
public class GenerateFishnameSiteMap {

    public static void main(String[] args) throws Exception {
        Writer writer = ExtractUtils.getSiteMapWriter("fishnames");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME });
        InputStream searchIndexInput = GenerateFishnameSiteMap.class.getResourceAsStream("/fishnames/searchIndex.html");
        String content = IOUtils.toString(searchIndexInput);
        Pattern p = Pattern.compile("(?:<a title='[A-Za-z'\\-,\\. ]'[ ]*href=')?" + "(/fishnames/fishnames.php\\?pid=[0-9]{1,})" + "(?:'>)" + "(?:[\n\r\t ]*<i>)" + "(?:</i>)?" + "([A-Za-z'\\-\\., ]{2,})" + "(?:<i>)?" + "(?:[\n\r\t ]*</i>)" + "(?:[\t\r\n ]*</a>)?");
        Matcher m = p.matcher(content);
        int searchIdx = 0;
        while (m.find(searchIdx)) {
            int endIdx = m.end();
            if (m.groupCount() == 2) {
                writer.write("http://www.fishnames.com.au" + m.group(1));
                writer.write("\t");
                String scientificName = m.group(2);
                scientificName = scientificName.replaceFirst(" \\- undifferentiated", "");
                writer.write(scientificName);
                writer.write("\n");
            }
            searchIdx = endIdx;
        }
        writer.flush();
        writer.close();
    }
}
