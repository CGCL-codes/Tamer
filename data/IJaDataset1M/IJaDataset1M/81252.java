package uk.ac.ebi.intact.psimitab.rsc;

import psidev.psi.mi.tab.model.builder.Row;
import java.util.Properties;
import java.io.*;

/**
 * 
 * @author Prem Anand (prem@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.2-SNAPSHOT
 */
public interface RelevanceScoreCalculator {

    String calculateScore(Row row);

    String calculateScore(Row row, String nameA, String nameB);

    Properties getWeights();

    void setWeights(Properties properties);

    Properties readPropertiesFile(InputStream inputStream) throws IOException;

    boolean writePropertiesFile(OutputStream outputStream) throws IOException;
}
