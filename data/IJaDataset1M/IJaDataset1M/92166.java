package ala.infosource.eol;

import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ala.infosource.ColumnHeaders;
import ala.infosource.ExtractUtils;
import ala.infosource.SiteMapGenerator;
import ala.infosource.WebUtils;

/**
 * Site map generator for Encyclopedia of Life
 * 
 * @author Tommy Wang (tommy.wang@csiro.au)
 */
@SiteMapGenerator(longName = "Encyclopedia of Life", shortName = "eol")
public class GenerateEolSiteMap implements Runnable {

    static final String UNKNOWN_ID = "Unknown identifier";

    static final String MEDIA_URL = "mediaURL";

    static final int MAX_ID = 3000000;

    public static void main(String[] args) throws Exception {
        String prefix = "http://www.eol.org/api/pages/1.0/";
        String appendix = "?common_names=1&details=1&images=2&subjects=all&text=2";
        Pattern p = Pattern.compile("(?:<dwc:scientificName>[\\s]{0,})" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "]{1,})" + "(?:[\\s]{0,}</dwc:scientificName>)");
        int taxonId = 1;
        Writer writer = ExtractUtils.getSiteMapWriter("eol");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME });
        while (true) {
            String taxonPageUrl = prefix + taxonId + appendix;
            String taxonPageStr = new String();
            try {
                taxonPageStr = WebUtils.getUrlContentAsString(taxonPageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (taxonPageStr.contains(UNKNOWN_ID) || !taxonPageStr.contains(MEDIA_URL)) {
                taxonId++;
                continue;
            }
            Matcher m = p.matcher(taxonPageStr);
            if (m.find(0)) {
                String sciName = m.group(1);
                writer.write(taxonPageUrl);
                writer.write('\t');
                writer.write(sciName);
                writer.write('\n');
                writer.flush();
                System.out.println("URL:" + taxonPageUrl);
                System.out.println("NAME:" + sciName);
            }
            taxonId++;
            if (taxonId > MAX_ID) {
                break;
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
