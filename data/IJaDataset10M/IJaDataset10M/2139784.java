package uk.ac.ebi.intact.util.uniprotExport.writers.golinewriters;

import uk.ac.ebi.intact.util.uniprotExport.parameters.golineparameters.GOParameters2;
import uk.ac.ebi.intact.util.uniprotExport.writers.WriterUtils;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * writer of GO lines, version 2
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>04/01/12</pre>
 */
public class GOLineWriter2 implements GOLineWriter<GOParameters2> {

    /**
     * The writer
     */
    private OutputStreamWriter writer;

    public GOLineWriter2(OutputStreamWriter outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("You must give a non null OutputStream writer");
        }
        writer = outputStream;
        writeHeader();
    }

    private void writeHeader() throws IOException {
        writer.write("!gaf-version: 2.0");
        writer.write(WriterUtils.NEW_LINE);
        writer.flush();
    }

    @Override
    public void writeGOLine(GOParameters2 parameters) throws IOException {
        if (parameters != null) {
            String uniprot1 = parameters.getFirstProtein();
            String uniprot2 = parameters.getSecondProtein();
            String master = parameters.getMasterProtein();
            boolean self = false;
            if (uniprot1.equals(uniprot2)) {
                self = true;
            }
            writeGOLine(master, uniprot1, uniprot2, self, parameters.getPubmedId(), parameters.getComponentXrefs());
            writer.flush();
        }
    }

    @Override
    public void writeGOLines(List<GOParameters2> GOLines) throws IOException {
        for (GOParameters2 parameter : GOLines) {
            writeGOLine(parameter);
        }
    }

    /**
     * Write a pubmed id
     * @param pubmedIds
     * @throws IOException
     */
    private void writePubmedLine(String pubmedIds) throws IOException {
        if (pubmedIds == null) {
            System.out.println("ERROR: No PubMed ID found in that set of experiments. ");
            return;
        }
        writer.write("PMID:");
        writer.write(pubmedIds);
        writer.write(WriterUtils.TABULATION);
    }

    /**
     * Write a list of go ref
     * @param goRefs
     * @throws IOException
     */
    private void writeGOAnnotationLine(Set<String> goRefs) throws IOException {
        if (!goRefs.isEmpty()) {
            for (Iterator iterator = goRefs.iterator(); iterator.hasNext(); ) {
                String go = (String) iterator.next();
                writer.write("occurs_in(");
                writer.write(go);
                writer.write(")");
                if (iterator.hasNext()) {
                    writer.write('|');
                }
            }
            writer.write(WriterUtils.TABULATION);
        }
    }

    /**
     * Write the GO line
     * @param uniprot1
     * @param uniprot2
     * @param self
     * @param pubmedBuffer
     * @throws IOException
     */
    private void writeGOLine(String master1, String uniprot1, String uniprot2, boolean self, String pubmedBuffer, Set<String> goRefs) throws IOException {
        if (master1 != null && !master1.equalsIgnoreCase(uniprot1)) {
            writeFirstInteractor(master1);
            writeBindingType(self);
            writeGeneralLine(uniprot2, pubmedBuffer, uniprot1, goRefs);
        } else {
            writeFirstInteractor(uniprot1);
            writeBindingType(self);
            writeGeneralLine(uniprot2, pubmedBuffer, null, goRefs);
        }
        writer.flush();
    }

    /**
     * Write details of GO line
     * @param uniprot2
     * @param pubmedBuffer
     * @throws IOException
     */
    private void writeGeneralLine(String uniprot2, String pubmedBuffer, String isoform, Set<String> goRefs) throws IOException {
        writePubmedLine(pubmedBuffer);
        String fixedSecondInteractor = uniprot2.replace("-PRO_", ":PRO_");
        writer.write("IPI");
        writer.write(WriterUtils.TABULATION);
        writer.write("UniProtKB:");
        writer.write(fixedSecondInteractor);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write("IntAct");
        writer.write(WriterUtils.TABULATION);
        writeGOAnnotationLine(goRefs);
        writer.write(WriterUtils.TABULATION);
        if (isoform != null) {
            String fixedIsoform = isoform.replace("-PRO_", ":PRO_");
            writer.write("UniProtKB:");
            writer.write(fixedIsoform);
        }
        writer.write(WriterUtils.NEW_LINE);
    }

    /**
     * Write binding type
     * @param self
     * @throws IOException
     */
    private void writeBindingType(boolean self) throws IOException {
        if (self) {
            writer.write("GO:0042802");
            writer.write(WriterUtils.TABULATION);
            self = true;
        } else {
            writer.write("GO:0005515");
            writer.write(WriterUtils.TABULATION);
        }
    }

    /**
     * Write the first interactor
     * @param uniprot1
     * @throws IOException
     */
    private void writeFirstInteractor(String uniprot1) throws IOException {
        writer.write("UniProtKB");
        writer.write(WriterUtils.TABULATION);
        writer.write(uniprot1);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
        writer.write(WriterUtils.TABULATION);
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }
}
