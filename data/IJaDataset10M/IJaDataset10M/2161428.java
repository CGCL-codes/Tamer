package org.saosis.core.controllers.hla;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.saosis.core.controllers.StringUtility;
import org.saosis.core.controllers.emblbank.EMBLBankEntryReader;
import org.saosis.core.controllers.features.FeatureFinder;
import org.saosis.core.models.Entry;
import org.saosis.core.models.FeatureQualifier;
import org.saosis.core.models.SAOSISException;
import org.saosis.core.models.Vocabularies;

/**
 * A parser for IMGT/HLA files.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public class HLAEntryReader extends EMBLBankEntryReader {

    protected static Pattern PATTERN_ID = Pattern.compile("^([^\\s]+)\\s+standard;\\s+DNA;\\s+HUM;\\s+\\d+\\s+BP\\.$");

    @Override
    protected void parseID(Entry entry, ArrayList<String> lines) throws SAOSISException {
        String line = StringUtility.join(lines, " ");
        Matcher matcher = PATTERN_ID.matcher(line);
        if (matcher.matches()) {
            String primaryAccessionNumberField = matcher.group(1).trim();
            String topologyField = "linear";
            entry.setPrimaryAccessionNumber(primaryAccessionNumberField);
            entry.setTopology(Vocabularies.Topologies.fromINSDCString(topologyField));
            entry.setTaxonomicDivision(Vocabularies.TaxonomicDivisions.HUMAN);
            entry.setMoleculeType(Vocabularies.MoleculeTypes.GENOMIC_DNA);
            entry.setDataClass(Vocabularies.DataClasses.STANDARD);
        } else {
            throw new SAOSISException(122000, new String[] { line });
        }
    }

    @Override
    protected String repairDatabaseCrossReferences(String line) {
        if (line.startsWith("EMBL;")) {
            addWarning(122003);
            line = line.replaceFirst("EMBL;", "ENSEMBL;");
        }
        return line;
    }

    @Override
    protected String repairFeatureName(Entry entry, String name) {
        if ("UTR".equals(name)) {
            if (FeatureFinder.findFivePrimeUTRFeatures(entry).size() == 0) name = "5'UTR"; else name = "3'UTR";
            addWarning(122001, new String[] { name });
        }
        return name;
    }

    @Override
    protected void repairFeatureQualifiers(Entry entry, ArrayList<FeatureQualifier> qualifiers) {
        ArrayList<FeatureQualifier> toRemove = new ArrayList<FeatureQualifier>();
        for (FeatureQualifier qualifier : qualifiers) {
            if ("partial".equals(qualifier.getName())) {
                addWarning(122002);
                toRemove.add(qualifier);
            }
        }
        qualifiers.removeAll(toRemove);
    }

    @Override
    protected String repairVersionHistory(String line) {
        if (line.contains("Sequence Updated,")) {
            addWarning(122004);
            line = line.replace("Sequence Updated,", "Last Sequence Update,");
        }
        return line;
    }
}
