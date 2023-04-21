package edu.usc.epigenome.uecgatk.BisSNP;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.sf.samtools.SAMSequenceDictionary;
import org.broad.tribble.TribbleException;
import org.broadinstitute.sting.utils.codecs.vcf.StandardVCFWriter;
import org.broadinstitute.sting.utils.codecs.vcf.VCFConstants;
import org.broadinstitute.sting.utils.codecs.vcf.VCFFilterHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeader;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeaderVersion;

public class TcgaVCFWriter extends StandardVCFWriter {

    protected String ref = null;

    public TcgaVCFWriter(File location, SAMSequenceDictionary refDict) {
        super(location, refDict);
    }

    public TcgaVCFWriter(File location, SAMSequenceDictionary refDict, boolean enableOnTheFlyIndexing) {
        super(location, refDict, enableOnTheFlyIndexing);
    }

    public TcgaVCFWriter(OutputStream output, SAMSequenceDictionary refDict) {
        super(output, refDict, false);
    }

    public TcgaVCFWriter(OutputStream output, SAMSequenceDictionary refDict, boolean doNotWriteGenotypes) {
        super(output, refDict, doNotWriteGenotypes);
    }

    public TcgaVCFWriter(File location, OutputStream output, SAMSequenceDictionary refDict, boolean enableOnTheFlyIndexing, boolean doNotWriteGenotypes) {
        super(location, output, refDict, enableOnTheFlyIndexing, doNotWriteGenotypes);
    }

    @Override
    public void writeHeader(VCFHeader header) {
        mHeader = doNotWriteGenotypes ? new VCFHeader(header.getMetaData()) : header;
        try {
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_FORMAT, "VCFv4.1").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_DATE, now("yyyyMMdd")).toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_TCGA_VERSION, "1.0").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_LOG, "<InputVCF=<>, InputVCFSource=<" + BisSNP.getBisSNPVersionNumber() + ">, InputVCFVer=<1.0>, InputVCFParam=<> InputVCFgeneAnno=<>>").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_REF, "<ID=" + BisSNPUtils.getRefGenomeVersion() + ",Source=" + ref + ">").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_ASSEMBLY, BisSNPUtils.getRefGenomeVersion()).toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_CENTER, "USC Epigenome Center").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_PHASE, "none").toString() + "\n");
            mWriter.write(VCFHeader.METADATA_INDICATOR + new VCFHeaderLine(BisulfiteVCFConstants.VCF_HEADER_VERSION_GAF, "none").toString() + "\n");
            for (VCFHeaderLine line : mHeader.getMetaData()) {
                if (line.getKey().equals(VCFHeaderVersion.VCF4_0.getFormatString()) || line.getKey().equals(VCFHeaderVersion.VCF3_3.getFormatString()) || line.getKey().equals(VCFHeaderVersion.VCF3_2.getFormatString())) continue;
                if (line instanceof VCFFilterHeaderLine) filtersWereAppliedToContext = true;
                mWriter.write(VCFHeader.METADATA_INDICATOR);
                mWriter.write(line.toString());
                mWriter.write("\n");
            }
            mWriter.write(VCFHeader.HEADER_INDICATOR);
            for (VCFHeader.HEADER_FIELDS field : mHeader.getHeaderFields()) {
                mWriter.write(field.toString());
                mWriter.write(VCFConstants.FIELD_SEPARATOR);
            }
            if (mHeader.hasGenotypingData()) {
                mWriter.write("FORMAT");
                for (String sample : mHeader.getGenotypeSamples()) {
                    mWriter.write(VCFConstants.FIELD_SEPARATOR);
                    mWriter.write(sample);
                }
            }
            mWriter.write("\n");
            mWriter.flush();
        } catch (IOException e) {
            throw new TribbleException("IOException writing the VCF header to " + e);
        }
    }

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public void setRefSource(String ref) {
        this.ref = ref;
    }

    public void writeFlush() {
        try {
            mWriter.flush();
        } catch (IOException e) {
            throw new TribbleException("IOException writing the VCF flush to " + e);
        }
    }
}
