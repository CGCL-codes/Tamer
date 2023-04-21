package org.cycads.generators;

import java.io.File;
import java.util.Collection;
import org.biojava.bio.seq.Sequence;
import org.biojavax.bio.seq.RichFeature;
import org.cycads.entities.biojava.CDS;
import org.cycads.entities.biojava.Organism;
import org.cycads.general.CacheCleanerController;
import org.cycads.general.biojava.BioSql;
import org.cycads.general.biojava.TermsAndOntologies;
import org.cycads.ui.progress.Progress;

public class PFFileGenerator {

    Progress progress;

    CacheCleanerController cacheCleaner;

    String fileName;

    public PFFileGenerator(Progress progress, CacheCleanerController cacheCleaner, String fileName, String headerPFFile) {
        this.progress = progress;
        this.cacheCleaner = cacheCleaner;
        this.fileName = fileName;
    }

    public void generate(Organism organism, int version, File directory, boolean pfForSequence, boolean createFastaFile) {
        progress.init();
        int cdsCounter = 0, tRNACounter = 0, miscRNACounter = 0;
        int pos = 0;
        Collection<Sequence> sequences = organism.getSequences(version);
        for (Sequence sequence : sequences) {
            bioCycDB.write(sequence);
            Collection<CDS> SeqFeatures = sequence.getCDSs();
        }
        Collection<Integer> featureIds;
        Collection<Integer> seqIds = BioSql.getSequencesIdCDStRNAmRNA(organism, version);
        BioCycRecord record;
        for (Integer seqId : seqIds) {
            featureIds = BioSql.getCDSMiscRNATRNABySeqId(seqId);
            for (Integer featureId : featureIds) {
                try {
                    RichFeature feature = BioSql.getFeature(featureId);
                    Sequence seq = feature.getSequence();
                    if (feature.getTypeTerm() == TermsAndOntologies.getTermTypeCDS()) {
                        record = new CDSRecord(feature);
                        cdsCounter++;
                    } else if (feature.getTypeTerm() == TermsAndOntologies.getTermTypeMiscRNA()) {
                        record = new MiscRNARecord(feature);
                        miscRNACounter++;
                    } else {
                        record = new TRNARecord(feature);
                        tRNACounter++;
                    }
                    if (fastaFile != null) {
                        record.shiftLocation(pos);
                    }
                    pfFile.print(record);
                    bioCycIdsFile.write(record.getExternalId(), record.getId());
                    progress.completeStep();
                } catch (GeneRecordInvalidException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fastaFile != null) {
                pos += fastaFile.write(seqId);
            }
            cacheCleaner.incCache();
        }
        if (fastaFile != null) {
            fastaFile.flush();
        }
        Object[] a = { progress.getStep(), cdsCounter, tRNACounter, miscRNACounter };
        progress.finish(a);
    }

    public void generate(Sequence sequence, PFFileStream pfFile, FastaFileForPFFile fastaFile, BioCycIdsFileForPFFile bioCycIdsFile) {
    }
}
