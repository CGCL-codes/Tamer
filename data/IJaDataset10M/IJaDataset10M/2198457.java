package sequime.descriptors;

/**
 * Abstract design of an amino acid. Use static method 'createAA' to get an instace with actual
 * physicochemical properties.
 * 
 * @author micha
 *
 */
public class DescriptorAminoacid {

    protected double pI;

    protected double pK1;

    protected double pK2;

    protected double mass;

    protected int polar;

    protected double hI;

    protected double solubility;

    protected double vdwVolume;

    protected DescriptorAminoacid(double pI, double pK1, double pK2, double mass, int polar, double hI, double solubility, double vdwVolume) {
        this.pI = pI;
        this.pK1 = pK1;
        this.pK2 = pK2;
        this.mass = mass;
        this.polar = polar;
        this.hI = hI;
        this.solubility = solubility;
        this.vdwVolume = vdwVolume;
    }

    public double get(String prop) {
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_pI) == 0) return pI;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_pK1) == 0) return pK1;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_pK2) == 0) return pK2;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_mass) == 0) return mass;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_polar) == 0) return polar;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_hI) == 0) return hI;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_solubility) == 0) return solubility;
        if (prop.compareTo(DescriptorNodeModel.CFGKEY_vanDerWaalsV) == 0) return vdwVolume;
        return 0.0;
    }

    public static DescriptorAminoacid createAA(String abbrev) {
        if (abbrev.compareTo("A") == 0) return new DescriptorAminoacid(6.01, 2.35, 9.87, 84.09404, 0, 1.8, 16.65, 67);
        if (abbrev.compareTo("C") == 0) return new DescriptorAminoacid(5.05, 1.92, 10.70, 121.15404, 0, 2.5, 100.0, 86);
        if (abbrev.compareTo("D") == 0) return new DescriptorAminoacid(2.85, 1.99, 9.9, 133.10384, -1, -3.5, 0.778, 91);
        if (abbrev.compareTo("E") == 0) return new DescriptorAminoacid(3.15, 2.10, 9.47, 147.13074, -1, -3.5, 0.864, 109);
        if (abbrev.compareTo("F") == 0) return new DescriptorAminoacid(5.49, 2.20, 9.31, 165.19184, 0, 2.8, 2.965, 135);
        if (abbrev.compareTo("G") == 0) return new DescriptorAminoacid(6.06, 2.35, 9.78, 75.06714, 0, -0.4, 24.99, 48);
        if (abbrev.compareTo("H") == 0) return new DescriptorAminoacid(7.6, 1.8, 9.33, 155.15634, 0, -3.2, 4.19, 118);
        if (abbrev.compareTo("I") == 0) return new DescriptorAminoacid(6.05, 2.32, 9.76, 131.17464, 0, 4.5, 4.117, 124);
        if (abbrev.compareTo("K") == 0) return new DescriptorAminoacid(9.60, 2.16, 9.06, 146.18934, 1, -3.9, 100.0, 135);
        if (abbrev.compareTo("L") == 0) return new DescriptorAminoacid(6.01, 2.33, 9.74, 131.17464, 0, 3.8, 2.426, 124);
        if (abbrev.compareTo("M") == 0) return new DescriptorAminoacid(5.74, 2.13, 9.28, 149.20784, 0, 1.9, 3.381, 124);
        if (abbrev.compareTo("N") == 0) return new DescriptorAminoacid(5.41, 2.14, 8.72, 132.11904, 0, -3.5, 3.53, 96);
        if (abbrev.compareTo("P") == 0) return new DescriptorAminoacid(6.30, 1.95, 10.64, 115.13194, 0, -1.6, 162.3, 90);
        if (abbrev.compareTo("Q") == 0) return new DescriptorAminoacid(5.65, 2.17, 9.13, 146.14594, 0, -3.5, 2.5, 114);
        if (abbrev.compareTo("R") == 0) return new DescriptorAminoacid(10.76, 1.82, 8.99, 174.20274, 1, -4.5, 15.00, 148);
        if (abbrev.compareTo("S") == 0) return new DescriptorAminoacid(5.68, 2.19, 9.21, 105.09344, 0, -0.8, 5.023, 73);
        if (abbrev.compareTo("T") == 0) return new DescriptorAminoacid(5.6, 2.09, 9.10, 119.12034, 0, -0.7, 100.0, 93);
        if (abbrev.compareTo("V") == 0) return new DescriptorAminoacid(6.00, 2.39, 9.74, 117.14784, 0, 4.2, 8.85, 105);
        if (abbrev.compareTo("W") == 0) return new DescriptorAminoacid(5.89, 2.46, 9.41, 204.22844, 0, -0.9, 1.136, 163);
        if (abbrev.compareTo("Y") == 0) return new DescriptorAminoacid(5.64, 2.20, 9.21, 181.19124, 0, -1.3, 0.0453, 141);
        return null;
    }
}
