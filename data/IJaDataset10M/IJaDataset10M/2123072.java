package uk.ac.ebi.pride.mzgraph;

import uk.ac.ebi.pride.mol.NeutralLoss;
import uk.ac.ebi.pride.mol.ion.FragmentIonType;
import uk.ac.ebi.pride.mzgraph.chart.data.annotation.IonAnnotation;
import uk.ac.ebi.pride.mzgraph.chart.data.annotation.IonAnnotationInfo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 15-Aug-2010
 * Time: 14:45:11
 */
public class SpectrumBrowserTest {

    public static void main(String[] args) {
        Runnable runner = new Runnable() {

            public void run() {
                createGUI();
            }
        };
        EventQueue.invokeLater(runner);
    }

    private static void createGUI() {
        double[] mzArr = new double[] { 1.0, 2.012312313, 3.0, 4.234, 6.0, 7.34342 };
        double[] intentArr = new double[] { 0.05, 4.345345345, 6.0, 1.4545, 5.0, 8.23423 };
        SpectrumBrowser browser = new SpectrumBrowser();
        browser.setPeaks(mzArr, intentArr);
        browser.setSource("Test");
        browser.setId("1111111");
        JFrame frame = new JFrame("Side Bar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(browser, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        IonAnnotationInfo ionInfo1 = new IonAnnotationInfo();
        IonAnnotationInfo.Item annotationItem = new IonAnnotationInfo.Item(-2, FragmentIonType.Y_ION, 2, NeutralLoss.WATER_LOSS);
        ionInfo1.addItem(annotationItem);
        IonAnnotation ion1 = new IonAnnotation(1.0, 0.05, ionInfo1);
        IonAnnotationInfo ionInfo2 = new IonAnnotationInfo();
        IonAnnotationInfo.Item annotationItem2 = new IonAnnotationInfo.Item(-2, FragmentIonType.B_ION, 2, null);
        ionInfo2.addItem(annotationItem2);
        IonAnnotation ion2 = new IonAnnotation(2.012312313, 4.345345345, ionInfo2);
        IonAnnotationInfo ionInfo3 = new IonAnnotationInfo();
        IonAnnotationInfo.Item annotationItem3 = new IonAnnotationInfo.Item(-2, FragmentIonType.B_ION, 2, null);
        ionInfo3.addItem(annotationItem3);
        IonAnnotationInfo.Item annotationItem4 = new IonAnnotationInfo.Item(-2, FragmentIonType.Y_ION, 2, null);
        ionInfo3.addItem(annotationItem4);
        IonAnnotation ion3 = new IonAnnotation(3.012312313, 7.345345345, ionInfo3);
        java.util.List<IonAnnotation> ions = new ArrayList<IonAnnotation>();
        ions.add(ion1);
        ions.add(ion2);
        ions.add(ion3);
        browser.addFragmentIons(ions);
    }
}
