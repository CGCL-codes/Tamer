package co.edu.unal.ungrid.services.client.applet.bimler.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
import co.edu.unal.ungrid.registration.evolutionary.genetic.GeneticIndividual;
import co.edu.unal.ungrid.transformation.AffineTransform;

public class XmlLoader implements XmlFileListener {

    public XmlLoader(String sDir) {
        m_parser = new DirectoryParser(sDir);
        m_parser.addListener(this);
        m_tests = new Vector<DoctorTest>();
    }

    public void run() {
        m_parser.run();
    }

    public void load(File f) {
        Properties doc = new Properties();
        try {
            FileInputStream fis = new FileInputStream(f);
            doc.loadFromXML(fis);
            fis.close();
        } catch (Exception exc) {
        }
        double[] param = { Double.parseDouble(doc.getProperty("rotate_angle")), Double.parseDouble(doc.getProperty("scale_factor")), Double.parseDouble(doc.getProperty("translate_x")), Double.parseDouble(doc.getProperty("translate_y")) };
        GeneticIndividual ind = new GeneticIndividual(new AffineTransform(param));
        ind.setFitness(Double.parseDouble(doc.getProperty("correlation_ratio")));
        File fReference = new File(doc.getProperty("reference"));
        File fFloating = new File(doc.getProperty("floating"));
        String sDoctor = String.format("%22s", doc.getProperty("doctor"));
        String sReference = String.format("%22s", fReference.getName());
        String sFloating = String.format("%22s", fFloating.getName());
        DoctorTest dt = new DoctorTest(sDoctor, sReference, sFloating, ind);
        m_tests.add(dt);
    }

    public void dump() {
        for (DoctorTest dt : m_tests) {
            System.out.println(dt);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            XmlLoader xl = new XmlLoader(args[0]);
            xl.run();
            xl.dump();
        }
    }

    private DirectoryParser m_parser;

    private Vector<DoctorTest> m_tests;
}
