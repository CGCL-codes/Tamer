package shu.cms.colorformat.trans;

import java.util.*;
import shu.cms.colorformat.cxf.*;
import shu.cms.colorformat.legend.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �ΨӱN�榡�ഫ��CxF�ϥ�
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class CxFTransformer {

    private static ObjectFactory objFactory = new ObjectFactory();

    private static SampleSet spectraWinToSampleSet(SpectraWinAsciiFile[] files) {
        if (files.length < 1) {
            throw new IllegalArgumentException("files.length <1");
        }
        SampleSet sampleSet = objFactory.createSampleSet();
        sampleSet.setName("Data");
        sampleSet.setCreated(files[0].getHeader().dateTime);
        List<Sample> samples = sampleSet.getSample();
        int x = 0;
        for (SpectraWinAsciiFile file : files) {
            samples.add(spectraWinToSample(file, "A" + x));
            x++;
        }
        return sampleSet;
    }

    /**
   * �NSpectraWinASCIIFile�নSample
   * @param file SpectraWinASCIIFile
   * @param sampleName String
   * @return Sample
   */
    private static Sample spectraWinToSample(SpectraWinAsciiFile file, String sampleName) {
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        }
        Sample sample = objFactory.createSample();
        if (sampleName != null) {
            sample.setName(sampleName);
        } else {
            sample.setName(file.getHeader().title);
        }
        sample.setCreated(file.getHeader().dateTime);
        sample.setDescription(file.getHeader().description);
        Spectrum spectrum = objFactory.createSpectrum();
        spectrum.setConditions("1");
        spectrum.setCreated(file.getHeader().dateTime);
        List<Value> values = spectrum.getValue();
        SpectraWinAsciiFile.Spectral spectral = file.getSpectral();
        int start = spectral.start;
        int interval = spectral.interval;
        ArrayList<Double> info = spectral.info;
        int x = 0;
        for (Double o : info) {
            Value v = objFactory.createValue();
            v.setName(Integer.toString(start + x * interval));
            x++;
            v.setvalue(Double.toString(o));
            values.add(v);
        }
        SampleAttribute sampleAttr = objFactory.createSampleAttribute();
        sampleAttr.setSpectrum(spectrum);
        sample.getSampleAttribute().add(sampleAttr);
        return sample;
    }

    private static Conditions spectraWinToConditions(SpectraWinAsciiFile file) {
        SpectraWinAsciiFile.Spectral spectral = file.getSpectral();
        String min = Integer.toString(spectral.start);
        String max = Integer.toString(spectral.end);
        String num = Integer.toString((spectral.end - spectral.start) / spectral.interval + 1);
        Conditions conditions = objFactory.createConditions();
        conditions.setID("1");
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("Physical.Filter", "Unknown"));
        attrs.add(new Attribute("Physical.NumberOfDataPointsSpectrum", num));
        attrs.add(new Attribute("Physical.SpectrumType", "Emission"));
        attrs.add(new Attribute("Physical.WavelengthSpectrumMax", max));
        attrs.add(new Attribute("Physical.WavelengthSpectrumMin", min));
        return conditions;
    }

    public static CXF spectraWinToCxF(SpectraWinAsciiFile[] files) {
        CXF cxf = objFactory.createCXF();
        cxf.getConditions().add(spectraWinToConditions(files[0]));
        cxf.getSampleSet().add(spectraWinToSampleSet(files));
        cxf.setName("");
        cxf.setDescription(SpectraWinAsciiFile2CxF.FROM_SPECTRAWIN);
        return cxf;
    }

    public static CXF TRDatabaseToCxF(TRDatabaseFile file) {
        CXF cxf = objFactory.createCXF();
        cxf.getConditions().add(TRDatabaseToConditions(file));
        cxf.getSampleSet().add(TRDatabaseToSampleSet(file));
        String name = file.filename;
        name = name.substring(name.lastIndexOf('\\') + 1, name.length());
        cxf.setName(name);
        cxf.setDescription("From TRDatabase");
        return cxf;
    }

    public static CXF RGBICCProfileToCxF(GretagMacbethAsciiFile file) {
        CXF cxf = objFactory.createCXF();
        cxf.getConditions().add(RGBICCProfileToConditions(2));
        cxf.getSampleSet().add(RGBICCProfileToSampleSet(file));
        cxf.setName("");
        cxf.setDescription("From ICCProfile");
        return cxf;
    }

    public static void main(String[] args) {
        GretagMacbethAsciiParser parser = new GretagMacbethAsciiParser("Reference Files\\Monitor\\LCD Monitor Reference 742.txt");
        GretagMacbethAsciiFile file = parser.getGretagMacbethAsciiFile();
        CXF cxf = RGBICCProfileToCxF(file);
        CXFOperator.saveCXF(cxf, "Reference Files\\Monitor\\LCD Monitor Reference 742.cxf");
    }

    public static CXF XYZICCProfileToCxF(GretagMacbethAsciiFile file) {
        CXF cxf = objFactory.createCXF();
        cxf.getConditions().add(XYZICCProfileToConditions(1));
        cxf.getSampleSet().add(XYZICCProfileToSampleSet(file));
        cxf.setName("");
        cxf.setDescription("From ICCProfile");
        return cxf;
    }

    public static CXF PDDEToCxF(PrepressDigitalDataExchangeFile file) {
        CXF cxf = objFactory.createCXF();
        cxf.getConditions().add(PDDEToConditions(file));
        cxf.getSampleSet().add(PDDEToSampleSet(file));
        cxf.setName(file.filename);
        cxf.setDescription(file.pddeHeader.descriptor);
        cxf.setCreator(file.pddeHeader.originator);
        return cxf;
    }

    private static Conditions PDDEToConditions(PrepressDigitalDataExchangeFile file) {
        Conditions conditions = objFactory.createConditions();
        conditions.setID("1");
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("Physical.WavelengthSpectrumMax", "700"));
        attrs.add(new Attribute("Physical.WavelengthSpectrumMin", "400"));
        attrs.add(new Attribute("Physical.NumberOfDataPointsSpectrum", "31"));
        return conditions;
    }

    private static SampleSet PDDEToSampleSet(PrepressDigitalDataExchangeFile file) {
        String created = file.pddeHeader.created;
        SampleSet sampleSet = objFactory.createSampleSet();
        sampleSet.setName("Data");
        sampleSet.setCreated(created);
        List<Sample> samples = sampleSet.getSample();
        GretagMacbethAsciiFile.DataSet dataSet = file.pddeDataSet;
        int size = dataSet.size();
        for (int x = 0; x < size; x++) {
            GretagMacbethAsciiFile.SpectraData spectraData = dataSet.getSpectraData(x);
            Sample sample = objFactory.createSample();
            sample.setName(Integer.toString(spectraData.sampleID));
            sample.setDescription(spectraData.sampleName);
            SampleAttribute sampleAttr = objFactory.createSampleAttribute();
            Spectrum spectrum = objFactory.createSpectrum();
            spectrum.setConditions("1");
            double[] spectra = spectraData.spectra;
            for (int y = 0; y < 31; y++) {
                Value v = objFactory.createValue();
                v.setName(Integer.toString(400 + 10 * y));
                v.setvalue(Double.toString(spectra[y]));
                spectrum.getValue().add(v);
            }
            sampleAttr.setSpectrum(spectrum);
            sample.getSampleAttribute().add(sampleAttr);
            samples.add(sample);
        }
        return sampleSet;
    }

    private static Conditions TRDatabaseToConditions(TRDatabaseFile file) {
        Conditions conditions = objFactory.createConditions();
        conditions.setID("1");
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("Physical.WavelengthSpectrumMax", "700"));
        attrs.add(new Attribute("Physical.WavelengthSpectrumMin", "400"));
        attrs.add(new Attribute("Physical.NumberOfDataPointsSpectrum", "31"));
        return conditions;
    }

    private static SampleSet TRDatabaseToSampleSet(TRDatabaseFile file) {
        SampleSet sampleSet = objFactory.createSampleSet();
        sampleSet.setName("Data");
        List<Sample> samples = sampleSet.getSample();
        double[][] data = file.data;
        int size = data.length;
        for (int x = 0; x < size; x++) {
            Sample sample = objFactory.createSample();
            sample.setName(Integer.toString(x));
            SampleAttribute sampleAttr = objFactory.createSampleAttribute();
            Spectrum spectrum = objFactory.createSpectrum();
            spectrum.setConditions("1");
            double[] spectra = data[x];
            for (int y = 0; y < 31; y++) {
                Value v = objFactory.createValue();
                v.setName(Integer.toString(400 + 10 * y));
                v.setvalue(Double.toString(spectra[y]));
                spectrum.getValue().add(v);
            }
            sampleAttr.setSpectrum(spectrum);
            sample.getSampleAttribute().add(sampleAttr);
            samples.add(sample);
        }
        return sampleSet;
    }

    private static Conditions RGBICCProfileToConditions(int id) {
        Conditions conditions = objFactory.createConditions();
        conditions.setID(String.valueOf(id));
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("ColorSpaceDescription.Type", "RGB"));
        return conditions;
    }

    private static Conditions LabICCProfileToConditions(int id) {
        Conditions conditions = objFactory.createConditions();
        conditions.setID(String.valueOf(id));
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("ColorSpaceDescription.Type", "CIELab"));
        return conditions;
    }

    private static Conditions XYZICCProfileToConditions(int id) {
        Conditions conditions = objFactory.createConditions();
        conditions.setID(String.valueOf(id));
        List<Attribute> attrs = conditions.getAttribute();
        attrs.add(new Attribute("ColorSpaceDescription.Type", "CIEXYZ"));
        return conditions;
    }

    private static SampleSet RGBICCProfileToSampleSet(GretagMacbethAsciiFile file) {
        String created = file.getHeader().created;
        SampleSet sampleSet = objFactory.createSampleSet();
        sampleSet.setName("Data");
        sampleSet.setCreated(created);
        List<Sample> samples = sampleSet.getSample();
        GretagMacbethAsciiFile.DataSet dataSet = file.getDataSet();
        int size = dataSet.size();
        for (int x = 0; x < size; x++) {
            GretagMacbethAsciiFile.TestChartData testChartData = dataSet.getTestChartData(x);
            Sample sample = objFactory.createSample();
            sample.setName(testChartData.sampleName);
            SampleAttribute sampleAttr = objFactory.createSampleAttribute();
            ColorVector colorVector = objFactory.createColorVector();
            double[] rgb = testChartData.RGB;
            Value r = objFactory.createValue();
            r.setName("R");
            r.setvalue(Double.toString(rgb[0]));
            Value g = objFactory.createValue();
            g.setName("G");
            g.setvalue(Double.toString(rgb[1]));
            Value b = objFactory.createValue();
            b.setName("B");
            b.setvalue(Double.toString(rgb[2]));
            List<Value> values = colorVector.getValue();
            colorVector.setConditions("2");
            values.add(r);
            values.add(g);
            values.add(b);
            sampleAttr.setColorVector(colorVector);
            sample.getSampleAttribute().add(sampleAttr);
            samples.add(sample);
        }
        return sampleSet;
    }

    private static SampleSet XYZICCProfileToSampleSet(GretagMacbethAsciiFile file) {
        String created = file.getHeader().created;
        SampleSet sampleSet = objFactory.createSampleSet();
        sampleSet.setName("Data");
        sampleSet.setCreated(created);
        List<Sample> samples = sampleSet.getSample();
        GretagMacbethAsciiFile.DataSet dataSet = file.getDataSet();
        int size = dataSet.size();
        for (int x = 0; x < size; x++) {
            GretagMacbethAsciiFile.LabData labData = dataSet.getLabData(x);
            Sample sample = objFactory.createSample();
            sample.setName(labData.sampleName);
            SampleAttribute sampleAttr = objFactory.createSampleAttribute();
            ColorVector colorVector = objFactory.createColorVector();
            double[] XYZ = labData.XYZ;
            Value X = objFactory.createValue();
            X.setName("X");
            X.setvalue(Double.toString(XYZ[0]));
            Value Y = objFactory.createValue();
            Y.setName("Y");
            Y.setvalue(Double.toString(XYZ[1]));
            Value Z = objFactory.createValue();
            Z.setName("Z");
            Z.setvalue(Double.toString(XYZ[2]));
            List<Value> values = colorVector.getValue();
            colorVector.setConditions("1");
            values.add(X);
            values.add(Y);
            values.add(Z);
            sampleAttr.setColorVector(colorVector);
            sample.getSampleAttribute().add(sampleAttr);
            samples.add(sample);
        }
        return sampleSet;
    }
}
