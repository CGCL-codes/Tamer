package org.bibnet.playground;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.grlea.log.SimpleLogger;

public class KohaFrameworks {

    private static final SimpleLogger LOG = new SimpleLogger(KohaFrameworks.class);

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        String zeile = "";
        BufferedReader sqlVorlage = null;
        BufferedReader codesVorlage = null;
        BufferedWriter out = null;
        final StringBuffer sql = new StringBuffer();
        try {
            sqlVorlage = new BufferedReader(new FileReader("/home/flyingfischer/Desktop/Import/koha/marc_subfield_vorlage.txt"));
            while ((zeile = sqlVorlage.readLine()) != null) {
                sql.append(zeile);
                sql.append('\012');
            }
            codesVorlage = new BufferedReader(new FileReader("/home/flyingfischer/Desktop/Import/koha/frameworkCode.txt"));
            out = new BufferedWriter(new FileWriter("/home/flyingfischer/Desktop/Import/koha/newMarcSubfields.sql"));
            while ((zeile = codesVorlage.readLine()) != null) {
                final String code = "'" + zeile + "'";
                out.write(sql.toString().replaceAll("'ART'", code));
                out.newLine();
            }
        } catch (final IOException e) {
            System.err.println("Error reading file!");
        } finally {
            try {
                sqlVorlage.close();
                codesVorlage.close();
                out.close();
                System.out.println("fertig!");
            } catch (final Exception e) {
                LOG.error(e.toString());
            }
        }
    }
}
