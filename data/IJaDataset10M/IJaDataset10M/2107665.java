package ala.infosource.adu;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ala.infosource.ColumnHeaders;
import ala.infosource.ExtractUtils;
import ala.infosource.SiteMapGenerator;
import ala.infosource.WebUtils;

/**
 * Site map generator for Ants Down Under   
 * 
 * @author Tommy Wang (twang@wollemisystems.com)
 */
@SiteMapGenerator(longName = "Ants Down Under", shortName = "anu")
public class GenerateAduSiteMap {

    public static void main(String[] args) throws Exception {
        String genusIndexPage = "http://anic.ento.csiro.au/ants/genus_list.aspx";
        String aduSource = "http://anic.ento.csiro.au/ants/";
        String genusIndex = WebUtils.getUrlContentAsString(genusIndexPage);
        Pattern genusIndexPattern = Pattern.compile("(?:<li><span class='FontMedium'><a href=)?" + "(biota_details\\.aspx\\?BiotaID=[0-9]{1,})" + "(?:>)?" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN_SIMPLE + "]{1,})" + "(?:</a></span><span class='FontSmall'>[ ]*\\-[ ]*Subfamily[ ]*<a href=)?" + "(biota_details\\.aspx\\?BiotaID=[0-9]{1,})" + "(?:>)?" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN_SIMPLE + "]{1,})" + "(?:</a>)?" + "(?:.*)?" + "(child_list\\.aspx\\?BiotaID=[0-9]{1,})");
        Pattern speciesIndexPattern = Pattern.compile("(?:<a href=)?" + "(biota_details\\.aspx\\?BiotaID=[0-9]{1,})" + "(?:>)?" + "([" + ExtractUtils.SCIENTIFIC_NAME_PATTERN + "]{1,})" + "(?:</a>)");
        Writer writer = ExtractUtils.getSiteMapWriter("adu");
        ExtractUtils.writeColumnHeaders(writer, new String[] { ColumnHeaders.URI, ColumnHeaders.SCIENTIFIC_NAME, ColumnHeaders.TAXON_RANK, ColumnHeaders.SUBFAMILY, ColumnHeaders.GENUS, ColumnHeaders.SPECIFIC_EPITHET });
        Matcher m = genusIndexPattern.matcher(genusIndex);
        int searchIdx = 0;
        while (m.find(searchIdx)) {
            int endIdx = m.end();
            String genusUrl = aduSource + m.group(1);
            String genusName = m.group(2);
            String subfamilyUrl = aduSource + m.group(3);
            String subfamilyName = m.group(4);
            String speciesIndexUrl = aduSource + m.group(5);
            writeOutInfo(writer, subfamilyUrl, subfamilyName, "subfamily", subfamilyName, "", "");
            writeOutInfo(writer, genusUrl, genusName, "genus", subfamilyName, genusName, "");
            String speciesIndex = WebUtils.getUrlContentAsString(speciesIndexUrl);
            Matcher m2 = speciesIndexPattern.matcher(speciesIndex);
            int searchIdx2 = 0;
            while (m2.find(searchIdx2)) {
                int endIdx2 = m2.end();
                String scientificName = m2.group(2);
                String speciesUrl = m2.group(1);
                String generatedSpeciesUrl = aduSource + speciesUrl;
                writeOutInfo(writer, generatedSpeciesUrl, genusName + " " + scientificName, "species", subfamilyName, genusName, scientificName);
                writer.flush();
                searchIdx2 = endIdx2;
            }
            searchIdx = endIdx;
        }
        writer.flush();
        writer.close();
    }

    private static void writeOutInfo(Writer writer, String pageUrl, String scientificName, String taxonRank, String subfamilyName, String genusName, String specificEpithet) throws IOException {
        writer.write(pageUrl);
        writer.write('\t');
        writer.write(scientificName);
        writer.write('\t');
        writer.write(taxonRank);
        writer.write('\t');
        writer.write(subfamilyName);
        writer.write('\t');
        writer.write(genusName);
        writer.write('\t');
        writer.write(specificEpithet);
        writer.write('\n');
    }
}
