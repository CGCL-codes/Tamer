package ala.infosource.qge;

import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ala.infosource.ColumnHeaders;
import ala.infosource.ExtractUtils;
import ala.infosource.SiteMapGenerator;
import ala.infosource.WebUtils;

/**
 * Site map generator for Queensland Government - Endangered (Department of Environment and Resource Management)    
 * 
 * @author Tommy Wang (twang@wollemisystems.com)
 */
@SiteMapGenerator(longName = "Queensland Government - Endangered", shortName = "qge")
public class GenerateQgeSiteMap {

    public static void main(String[] args) throws Exception {
        final String speciesIndexPage = "http://www.derm.qld.gov.au/wildlife-ecosystems/wildlife/threatened_plants_and_animals/endangered/index.html";
        final String source = "http://www.derm.qld.gov.au";
        String content = WebUtils.getUrlContentAsString(speciesIndexPage);
        Pattern p = Pattern.compile("(?:<li><a)" + "(?: href=\")" + "(/wildlife\\-ecosystems/wildlife/threatened_plants_and_animals/endangered/[\\w/]{1,}\\.html)" + "(?:\">)" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "]{1,})" + "(?:</a></li>)");
        Writer writer = ExtractUtils.getSiteMapWriter("qge");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME });
        Matcher m = p.matcher(content);
        int searchIdx = 0;
        while (m.find(searchIdx)) {
            int endIdx = m.end();
            String url = m.group(1);
            String scientificName = m.group(2);
            scientificName = scientificName.replaceAll("\\&#39;", "\'");
            if (scientificName.equals("Endangered species")) {
                searchIdx = endIdx;
                continue;
            }
            String generatedUrl = source + url;
            System.out.println(generatedUrl);
            writer.write(generatedUrl);
            writer.write('\t');
            writer.write(scientificName);
            writer.write('\n');
            searchIdx = endIdx;
        }
        writer.flush();
        writer.close();
    }
}
