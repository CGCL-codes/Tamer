package shu.cms.measure.meter;

import java.util.*;
import com.gretagmacbeth.eyeone.*;
import shu.cms.*;
import shu.cms.colorformat.logo.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.measure.meter.Meter.*;
import shu.cms.measure.meterapi.i1api.*;
import shu.math.array.*;
import shu.util.log.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class EyeOnePro extends EyeOneDisplay2 {

    public EyeOnePro(ScreenType screenType) {
        super(screenType);
        try {
            _EYE_ONE.setMeasurementMode(EyeOneAPI.MeasurementMode.SingleEmission);
        } catch (EyeOneException ex) {
        }
    }

    public String getCalibrationDescription() {
        return "Place i1 pro on plain surface";
    }

    public Instr getType() {
        return Instr.i1Pro;
    }

    public Spectra triggerMeasurementInSpectra() {
        try {
            float[] spectrum = _EYE_ONE.triggerSpectrumMeasurement();
            double[] data = FloatArray.toDoubleArray(spectrum);
            Spectra s = new Spectra("", Spectra.SpectrumType.NO_ASSIGN, 380, 730, 10, data);
            return s;
        } catch (EyeOneException ex) {
            Logger.log.error("", ex);
            return null;
        }
    }

    public void setSpectraMode(boolean spectraMode) {
        this.spectraMode = spectraMode;
    }

    private boolean spectraMode = false;

    public void setLogoFileHeader(LogoFile logo) {
        if (spectraMode) {
            logo.setHeader(LogoFile.Reserved.Created, new Date().toString());
            logo.setHeader(LogoFile.Reserved.Instrumentation, getType().name());
            logo.setHeader(LogoFile.Reserved.MeasurementSource, "Illumination=Unknown	ObserverAngle=Unknown	WhiteBase=Abs	Filter=Unknown");
            logo.setNumberOfFields(8);
            logo.addKeyword("SampleID");
            logo.addKeyword("SAMPLE_NAME");
            logo.setDataFormat("SampleID	SAMPLE_NAME	RGB_R	RGB_G	RGB_B	XYZ_X	XYZ_Y	XYZ_Z	nm380	nm390	nm400	nm410	nm420	nm430	nm440	nm450	nm460	nm470	nm480	nm490	nm500	nm510	nm520	nm530	nm540	nm550	nm560	nm570	nm580	nm590	nm600	nm610	nm620	nm630	nm640	nm650	nm660	nm670	nm680	nm690	nm700	nm710	nm720	nm730");
        } else {
            super.setLogoFileHeader(logo);
        }
    }

    public void setLogoFileData(LogoFile logo, List<Patch> patchList) {
        if (spectraMode) {
            int size = patchList.size();
            for (int x = 0; x < size; x++) {
                Patch p = patchList.get(x);
                CIEXYZ XYZ = p.getXYZ();
                RGB rgb = p.getRGB();
                String pName = p.getName();
                int index = x + 1;
                String name = (pName == null || pName.length() == 0) ? String.valueOf(index) : pName;
                StringBuilder buf = new StringBuilder();
                buf.append(index + "\t" + name + "\t" + rgb.R + "\t" + rgb.G + "\t" + rgb.B + "\t" + XYZ.X + "\t" + XYZ.Y + " " + XYZ.Z);
                Spectra s = p.getSpectra();
                double[] data = s.getData();
                int spectracount = data.length;
                for (int y = 0; y < spectracount; y++) {
                    buf.append('\t');
                    buf.append(data[y]);
                }
                logo.addData(buf.toString());
            }
        } else {
            super.setLogoFileData(logo, patchList);
        }
    }

    public boolean isSpectraMode() {
        return spectraMode;
    }
}
