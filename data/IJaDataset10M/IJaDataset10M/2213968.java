package musite.prediction.feature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import musite.Proteins;
import musite.PTM;
import musite.prediction.feature.disorder.DisorderUtil;
import musite.io.MusiteIOUtil;
import musite.io.xml.ProteinsXMLReader;
import musite.util.AminoAcid;
import musite.util.CollectionUtil;
import musite.util.StringUtil;

/**
 *
 * @author Jianjiong Gao
 */
public class TestFeature extends TestCase {

    public void testDisorderFeature() throws IOException {
        String dirPro = "data/H.sapiens/H.sapiens.uniprot.v15.14.sprot.v57.14.20100209.all.acetyl.musite.xml";
        Set<AminoAcid> aminoAcids = CollectionUtil.getSet(AminoAcid.LYSINE);
        PTM ptm = PTM.ACETYLATION;
        ProteinsXMLReader proReader = DisorderUtil.getDisorderXMLReader();
        Proteins proteins = MusiteIOUtil.read(proReader, dirPro);
        int offset = 0;
        int[] disWindowOffsets = new int[2 * offset + 1];
        for (int i = 0; i < 2 * offset + 1; i++) {
            disWindowOffsets[i] = i - offset;
        }
        DisorderFeatureExtractor disorderExtractor = new DisorderFeatureExtractor(disWindowOffsets, false);
        InstancesExtractorFromProteins posInsExtractor = new InstancesExtractorFromProteins(proteins, aminoAcids);
        posInsExtractor.setExtractOption(InstancesExtractorFromProteins.ExtractOption.MODIFIED_SITES, ptm);
        posInsExtractor.setInstanceFilter(new OffsetInstanceFilter(offset));
        List<Instance> instances_positive = posInsExtractor.fetch(-1);
        System.out.println(instances_positive.size() + " sites were extracted.");
        String dirOut = dirPro + "disorder.txt";
        FileWriter writer = new FileWriter(dirOut);
        BufferedWriter out = new BufferedWriter(writer);
        for (Instance ins : instances_positive) {
            List diss = disorderExtractor.extract(ins, false);
            if (diss == null) continue;
            out.write(StringUtil.implode(diss, "\t"));
            out.newLine();
        }
        out.close();
        writer.close();
        new File(dirOut).delete();
    }
}
