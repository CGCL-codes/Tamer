package org.vardb.web.json;

import java.util.ArrayList;
import java.util.List;
import org.vardb.analysis.CCodonHelper;
import org.vardb.analysis.CCodonUsageTable;
import org.vardb.util.CStringHelper;

public class CCodonUsageJson extends CAbstractJson {

    protected GcContent gcContent;

    protected int totalCount;

    protected List<Codon> rows = new ArrayList<Codon>();

    public CCodonUsageJson(CCodonUsageTable table) {
        this.gcContent = new GcContent(table.getGcContent());
        this.totalCount = table.getData().size();
        for (CCodonUsageTable.Frequency codon : table.getData()) {
            this.rows.add(new Codon(codon));
        }
    }

    public static class GcContent {

        protected Float g;

        protected Float c;

        protected Float a;

        protected Float t;

        protected Float gc;

        protected Float gc3;

        protected Float gc3skew;

        public GcContent(CCodonHelper.GcContent gcContent) {
            this.g = gcContent.getG();
            this.c = gcContent.getC();
            this.a = gcContent.getA();
            this.t = gcContent.getT();
            this.gc = gcContent.getGc();
            this.gc3 = gcContent.getGc3();
            this.gc3skew = gcContent.getGc3skew();
        }
    }

    public static class Codon {

        protected String codon;

        protected String aa;

        protected int count;

        protected String freq;

        protected String adapt;

        protected String rf;

        protected String rscu;

        public Codon(CCodonUsageTable.Frequency codon) {
            this.codon = codon.getCodon().name();
            this.aa = (codon.getCodon().getAminoAcid() != null) ? codon.getCodon().getAminoAcid().getCode() : "*";
            this.count = codon.getCount();
            this.freq = CStringHelper.formatDecimal(codon.getFrequency(), 2);
            this.adapt = CStringHelper.formatDecimal(codon.getRelativeAdaptiveness(), 2);
            this.rf = CStringHelper.formatDecimal(codon.getRelativeCodonFrequency(), 2);
            this.rscu = CStringHelper.formatDecimal(codon.getRelativeSynonymousCodonUsage(), 2);
        }
    }
}
