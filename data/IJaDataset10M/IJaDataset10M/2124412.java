package uk.ac.ebi.intact.bridges.blast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import uk.ac.ebi.intact.bridges.blast.client.BlastClientException;
import uk.ac.ebi.intact.bridges.blast.model.BlastInput;
import uk.ac.ebi.intact.bridges.blast.model.BlastJobStatus;
import uk.ac.ebi.intact.bridges.blast.model.BlastOutput;
import uk.ac.ebi.intact.bridges.blast.model.BlastResult;
import uk.ac.ebi.intact.bridges.blast.model.Job;
import uk.ac.ebi.intact.bridges.blast.model.UniprotAc;

/**
 * TODO comment this ... someday + implement it
 * 
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version
 * @since
 * 
 * <pre>
 * 13 Sep 2007
 * </pre>
 */
public class NcbiWuBlast extends AbstractBlastService {

    NcbiWuBlast() throws BlastServiceException {
        super(new File("testDir"), "testTableName", 20);
    }

    private Float threshold;

    private File workDir;

    public BlastJobStatus checkStatus(Job job) {
        return null;
    }

    public String getResult(Job job, Boolean isXml) {
        return null;
    }

    public List<Job> runBlast(Set<UniprotAc> uniprotAcs) {
        return null;
    }

    public Job runBlast(UniprotAc uniprotAc) {
        return null;
    }

    @Override
    public Job runBlast(BlastInput blastInput) throws BlastClientException {
        return null;
    }

    public BlastOutput getResult(Job job) {
        return null;
    }

    public BlastResult processOutput(File blastFile) {
        return null;
    }

    private String processTxtOutput(String ac, Set<String> againstProteins) {
        String result = "";
        try {
            FileReader fr = new FileReader(new File(workDir.getPath(), ac + ".txt"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            boolean start = false;
            boolean stop = false;
            while (((line = br.readLine()) != null) && !stop) {
                if (!start && Pattern.matches("^Sequences.*", line)) {
                    start = true;
                } else if (start) {
                    if (Pattern.matches("^UNIPROT.*\\w{6,6}.*", line)) {
                        String[] strs = line.split("\\s+");
                        String accession = strs[1];
                        Float evalue = new Float(strs[strs.length - 2]);
                        if (ac.equals(accession)) {
                            result = ac;
                        } else {
                            if (evalue < threshold && againstProteins.contains(accession)) {
                                result += "," + accession;
                            }
                        }
                    }
                    if (Pattern.matches("^>UNIPROT", line)) {
                        stop = true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
