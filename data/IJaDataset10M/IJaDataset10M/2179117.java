package shu.cms.colorformat.cxf.attr;

import java.util.*;
import shu.cms.colorformat.cxf.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class Physical extends ConditionsAttributes {

    public static final String FILTER_UNKNOWN = "Unknown";

    public static final String SPECTRUM_TYPE_EMISSION = "Emission";

    public String filter;

    public int numberOfDataPointsSpectrum;

    public String spectrumType;

    public int wavelengthSpectrumMax;

    public int wavelengthSpectrumMin;

    protected static final String[] names = new String[] { "Physical.Filter", "Physical.NumberOfDataPointsSpectrum", "Physical.SpectrumType", "Physical.WavelengthSpectrumMax", "Physical.WavelengthSpectrumMin" };

    public static void main(String[] args) {
        Physical p = new Physical();
        System.out.println(p.getClass().getName());
    }

    public Physical() {
    }

    public List<Attribute> toAttributeList() {
        List<Attribute> attrs = new ArrayList<Attribute>();
        Attribute attr = new Attribute(names[0], filter);
        attrs.add(attr);
        attr = new Attribute(names[1], String.valueOf(numberOfDataPointsSpectrum));
        attrs.add(attr);
        attr = new Attribute(names[2], spectrumType);
        attrs.add(attr);
        attr = new Attribute(names[3], String.valueOf(wavelengthSpectrumMax));
        attrs.add(attr);
        attr = new Attribute(names[4], String.valueOf(wavelengthSpectrumMin));
        attrs.add(attr);
        return attrs;
    }

    public static Physical getInstance(List<Attribute> attrs) {
        Physical physical = new Physical();
        for (Attribute attr : attrs) {
            if (attr.getName().equals(names[0])) {
                physical.filter = attr.getvalue();
            } else if (attr.getName().equals(names[1])) {
                physical.numberOfDataPointsSpectrum = Integer.parseInt(attr.getvalue());
            } else if (attr.getName().equals(names[2])) {
                physical.spectrumType = attr.getvalue();
            } else if (attr.getName().equals(names[3])) {
                physical.wavelengthSpectrumMax = Integer.parseInt(attr.getvalue().replaceAll("nm", "").trim());
            } else if (attr.getName().equals(names[4])) {
                physical.wavelengthSpectrumMin = Integer.parseInt(attr.getvalue().replaceAll("nm", "").trim());
            }
        }
        return physical;
    }
}
