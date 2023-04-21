package tuner3d.control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import tuner3d.graphics.Palette;

public class PaletteAction implements ActionListener {

    private Palette palette;

    private byte identifier;

    public PaletteAction(Palette palette, byte identifier) {
        this.palette = palette;
        this.identifier = identifier;
    }

    public void actionPerformed(ActionEvent e) {
        Color color = (Color) ((JComboBox) e.getSource()).getSelectedItem();
        switch(identifier) {
            case Palette.L:
                palette.l = color;
                break;
            case Palette.S:
                palette.s = color;
                break;
            case Palette.P:
                palette.p = color;
                break;
            case Palette.R:
                palette.r = color;
                break;
            case Palette.N:
                palette.n = color;
                break;
            case Palette.J:
                palette.j = color;
                break;
            case Palette.G:
                palette.g = color;
                break;
            case Palette.M:
                palette.m = color;
                break;
            case Palette.K:
                palette.k = color;
                break;
            case Palette.D:
                palette.d = color;
                break;
            case Palette.F:
                palette.f = color;
                break;
            case Palette.Q:
                palette.q = color;
                break;
            case Palette.O:
                palette.o = color;
                break;
            case Palette.E:
                palette.e = color;
                break;
            case Palette.T:
                palette.t = color;
                break;
            case Palette.H:
                palette.h = color;
                break;
            case Palette.I:
                palette.i = color;
                break;
            case Palette.C:
                palette.c = color;
                break;
            case Palette.ARG:
                palette.arg = color;
                break;
            case Palette.CYS:
                palette.cys = color;
                break;
            case Palette.TYR:
                palette.tyr = color;
                break;
            case Palette.GLN:
                palette.gln = color;
                break;
            case Palette.ALA:
                palette.ala = color;
                break;
            case Palette.GLY:
                palette.gly = color;
                break;
            case Palette.TRP:
                palette.trp = color;
                break;
            case Palette.ILE:
                palette.ile = color;
                break;
            case Palette.ASN:
                palette.asn = color;
                break;
            case Palette.GLU:
                palette.glu = color;
                break;
            case Palette.VAL:
                palette.val = color;
                break;
            case Palette.PHE:
                palette.phe = color;
                break;
            case Palette.LYS:
                palette.lys = color;
                break;
            case Palette.ASP:
                palette.asp = color;
                break;
            case Palette.MET:
                palette.met = color;
                break;
            case Palette.SER:
                palette.ser = color;
                break;
            case Palette.LEU:
                palette.leu = color;
                break;
            case Palette.HIS:
                palette.his = color;
                break;
            case Palette.PRO:
                palette.pro = color;
                break;
            case Palette.THR:
                palette.thr = color;
                break;
            case Palette.S5:
                palette.s5 = color;
                break;
            case Palette.S16:
                palette.s16 = color;
                break;
            case Palette.S23:
                palette.s23 = color;
                break;
            case Palette.P100_90:
                palette.p100_90 = color;
                break;
            case Palette.P90_80:
                palette.p90_80 = color;
                break;
            case Palette.P80_70:
                palette.p80_70 = color;
                break;
            case Palette.P70_60:
                palette.p70_60 = color;
                break;
            case Palette.P60_50:
                palette.p60_50 = color;
                break;
            case Palette.P50_40:
                palette.p50_40 = color;
                break;
            case Palette.P40_30:
                palette.p40_30 = color;
                break;
            case Palette.P30_20:
                palette.p30_20 = color;
                break;
            case Palette.P20_10:
                palette.p20_10 = color;
                break;
            case Palette.P10_0:
                palette.p10_0 = color;
                break;
            case Palette.ABNORMAL_GC:
                palette.abnormal_gc = color;
                break;
            case Palette.ABNORMAL_CDS:
                palette.abnormal_cds = color;
                break;
            case Palette.CLUSTER_CDS:
                palette.cluster_cds = color;
                break;
            case Palette.CLUSTER_RNA:
                palette.cluster_rna = color;
                break;
            case Palette.LOW_GENE:
                palette.low_gene = color;
                break;
            case Palette.GC_COLOR:
                palette.gc_color = color;
                break;
            case Palette.FONT_COLOR:
                palette.font_color = color;
                break;
            case Palette.SKEW_COLOR1:
                palette.skew_color1 = color;
                break;
            case Palette.SKEW_COLOR2:
                palette.skew_color2 = color;
                break;
            case Palette.COORD_COLOR:
                palette.coord_color = color;
                break;
            case Palette.CDS_COLOR:
                palette.cds_color = color;
                break;
            case Palette.REGION_COLOR:
                palette.region_color = color;
                break;
            case Palette.BACKGROUND_COLOR:
                palette.background_color = color;
                break;
            case Palette.DEFAULT_PIE_COLOR:
                palette.default_pie_color = color;
                break;
            case Palette.DEFAULT_COG_COLOR:
                palette.default_cog_color = color;
                break;
            case Palette.DEFAULT_RNA_COLOR:
                palette.default_rna_color = color;
                break;
            case Palette.DEFAULT_CDS_COLOR:
                palette.default_cds_color = color;
                break;
            case Palette.DEFAULT_LINK_COLOR:
                palette.default_link_color = color;
                break;
            case Palette.DEFAULT_REGION_COLOR:
                palette.default_region_color = color;
                break;
            case Palette.BIOLOGICAL_PROCESS:
                palette.biological_process = color;
                break;
            case Palette.CELLULAR_COMPONENT:
                palette.cellular_component = color;
                break;
            case Palette.MOLECULAR_FUNCTION:
                palette.molecular_function = color;
                break;
            case Palette.CDS_LENGTH_GRADIENT_START_COLOR:
                palette.cds_length_gradient_start_color = color;
                break;
            case Palette.CDS_LENGTH_GRADIENT_END_COLOR:
                palette.cds_length_gradient_end_color = color;
                break;
            case Palette.CDS_GC_GRADIENT_START_COLOR:
                palette.cds_gc_gradient_start_color = color;
                break;
            case Palette.CDS_GC_GRADIENT_END_COLOR:
                palette.cds_gc_gradient_end_color = color;
                break;
            default:
                break;
        }
    }

    public void setIdentifier(byte identifier) {
        this.identifier = identifier;
    }
}
