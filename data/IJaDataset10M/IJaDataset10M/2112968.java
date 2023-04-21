package cn.houseout.snapscreen.ops;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import cn.houseout.snapscreen.demo.ImageEditor;

public class AffineTransformProcess extends JPanel implements ImageProcessInterface {

    private static final long serialVersionUID = 8727674136476063954L;

    JTabbedPane tabbedPane = new JTabbedPane();

    Panel page00 = new Panel();

    TextField page00TextFieldHorizontal = new TextField("0.5", 6);

    TextField page00TextFieldVertical = new TextField("0.5", 6);

    CheckboxGroup Page00Group = new CheckboxGroup();

    Checkbox page00NearestNeighbor = new Checkbox("Nearest Neighbor Interpolation", Page00Group, false);

    Checkbox page00Bilinear = new Checkbox("Bilinear Interpolation", Page00Group, false);

    Panel page01 = new Panel();

    TextField page01TextFieldHorizontal = new TextField("5", 6);

    TextField page01TextFieldVertical = new TextField("10", 6);

    Panel page02 = new Panel();

    TextField page02TextField = new TextField("45.0", 6);

    Panel page03 = new Panel();

    public AffineTransformProcess(ImageEditor parent) {
        final ImageEditor p = parent;
        constructPage00();
        tabbedPane.add(page00);
        constructPage01();
        tabbedPane.add(page01);
        constructPage02();
        tabbedPane.add(page02);
        constructPage03();
        tabbedPane.add(page03);
        setLayout(new BorderLayout());
        add(tabbedPane);
        JButton doBtn = new JButton("do");
        doBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                p.refreshDisplay(true);
            }
        });
        JPanel pane = new JPanel();
        pane.add(doBtn);
        add(pane, BorderLayout.EAST);
    }

    public Dimension getPreferredSize() {
        return this.getSize();
    }

    public Dimension getMinimumSize() {
        return this.getSize();
    }

    public Dimension getSize() {
        return new Dimension(600, 100);
    }

    void constructPage00() {
        page00.setName("Scaling");
        page00.setLayout(new BorderLayout());
        Panel page00ControlPanel = new Panel();
        page00ControlPanel.setLayout(new GridLayout(2, 1));
        Panel subControlPanel00 = new Panel();
        subControlPanel00.setLayout(new FlowLayout(FlowLayout.LEFT));
        subControlPanel00.add(page00NearestNeighbor);
        subControlPanel00.add(page00Bilinear);
        page00ControlPanel.add(subControlPanel00);
        Panel subControlPanel03 = new Panel();
        subControlPanel03.setLayout(new FlowLayout(FlowLayout.LEFT));
        subControlPanel03.add(new Label("Horizontal Scale Factor"));
        subControlPanel03.add(page00TextFieldHorizontal);
        subControlPanel03.add(new Label("    Vertical Scale Factor"));
        subControlPanel03.add(page00TextFieldVertical);
        page00ControlPanel.add(subControlPanel03);
        page00.add(page00ControlPanel, BorderLayout.CENTER);
    }

    BufferedImage scaleOperation(BufferedImage theImage) {
        double horizontalScale = 0.001;
        try {
            horizontalScale = Double.parseDouble(page00TextFieldHorizontal.getText());
        } catch (java.lang.NumberFormatException e) {
            page00TextFieldHorizontal.setText("Bad Input");
            horizontalScale = 0.001;
        }
        if ((horizontalScale < 0.001) || (horizontalScale > 10.0)) {
            page00TextFieldHorizontal.setText("Bad Input");
            horizontalScale = 0.001;
        }
        double verticalScale = 0.001;
        try {
            verticalScale = Double.parseDouble(page00TextFieldVertical.getText());
        } catch (java.lang.NumberFormatException e) {
            page00TextFieldHorizontal.setText("Bad Input");
            verticalScale = 0.001;
        }
        if ((verticalScale < 0.001) || (verticalScale > 10.0)) {
            page00TextFieldHorizontal.setText("Bad Input");
            verticalScale = 0.001;
        }
        int interpolationScheme;
        if (page00Bilinear.getState() == true) {
            interpolationScheme = AffineTransformOp.TYPE_BILINEAR;
        } else {
            interpolationScheme = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        }
        AffineTransform transformObj = AffineTransform.getScaleInstance(horizontalScale, verticalScale);
        AffineTransformOp filterObj = new AffineTransformOp(transformObj, interpolationScheme);
        BufferedImage dest = filterObj.createCompatibleDestImage(theImage, theImage.getColorModel());
        filterObj.filter(theImage, dest);
        return dest;
    }

    void constructPage01() {
        page01.setName("Translation");
        page01.setLayout(new BorderLayout());
        Panel page01ControlPanel = new Panel();
        page01ControlPanel.setLayout(new GridLayout(3, 1));
        Panel subControlPanel00 = new Panel();
        subControlPanel00.setLayout(new FlowLayout(FlowLayout.LEFT));
        subControlPanel00.add(new Label("Horizontal Translation Distance in Pixels"));
        subControlPanel00.add(page01TextFieldHorizontal);
        page01ControlPanel.add(subControlPanel00);
        Panel subControlPanel01 = new Panel();
        subControlPanel01.setLayout(new FlowLayout(FlowLayout.LEFT));
        subControlPanel01.add(new Label("Vertical Translation Distance in Pixels"));
        subControlPanel01.add(page01TextFieldVertical);
        page01ControlPanel.add(subControlPanel01);
        page01.add(page01ControlPanel, BorderLayout.CENTER);
    }

    BufferedImage translateOperation(BufferedImage theImage) {
        double horizontalDistance = 0.0;
        try {
            horizontalDistance = Double.parseDouble(page01TextFieldHorizontal.getText());
        } catch (java.lang.NumberFormatException e) {
            page01TextFieldHorizontal.setText("Bad Input");
            horizontalDistance = 0.0;
        }
        if ((horizontalDistance < -1000.0) || (horizontalDistance > 1000.0)) {
            page01TextFieldHorizontal.setText("Bad Input");
            horizontalDistance = 0.0;
        }
        double verticalDistance = 0.0;
        try {
            verticalDistance = Double.parseDouble(page01TextFieldVertical.getText());
        } catch (java.lang.NumberFormatException e) {
            page01TextFieldHorizontal.setText("Bad Input");
            verticalDistance = 0.0;
        }
        if ((verticalDistance < -1000.0) || (verticalDistance > 1000.0)) {
            page01TextFieldHorizontal.setText("Bad Input");
            verticalDistance = 0.0;
        }
        int interpolationScheme = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        AffineTransform transformObj = AffineTransform.getTranslateInstance(horizontalDistance, verticalDistance);
        AffineTransformOp filterObj = new AffineTransformOp(transformObj, interpolationScheme);
        BufferedImage dest = filterObj.createCompatibleDestImage(theImage, theImage.getColorModel());
        filterObj.filter(theImage, dest);
        return dest;
    }

    void constructPage02() {
        page02.setName("Rotation");
        page02.setLayout(new BorderLayout());
        Panel page02ControlPanel = new Panel();
        page02ControlPanel.setLayout(new GridLayout(3, 1));
        Panel subControlPanel00 = new Panel();
        subControlPanel00.setLayout(new FlowLayout(FlowLayout.LEFT));
        subControlPanel00.add(new Label("Rotation in Degrees"));
        subControlPanel00.add(page02TextField);
        page02ControlPanel.add(subControlPanel00);
        page02.add(page02ControlPanel, BorderLayout.CENTER);
    }

    BufferedImage rotateOperation(BufferedImage theImage) {
        double rotationAngleInDegrees = 0.0;
        try {
            rotationAngleInDegrees = Double.parseDouble(page02TextField.getText());
        } catch (java.lang.NumberFormatException e) {
            page02TextField.setText("Bad Input");
            rotationAngleInDegrees = 0.0;
        }
        double rotationAngleInRadians = rotationAngleInDegrees * Math.PI / 180.0;
        int interpolationScheme = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        int halfDiagonal = (int) (Math.sqrt(theImage.getWidth() * theImage.getWidth() + theImage.getHeight() * theImage.getHeight()) / 2.0);
        int horizontalDistance = halfDiagonal - theImage.getWidth() / 2;
        int verticalDistance = halfDiagonal - theImage.getHeight() / 2;
        AffineTransform transformObj = AffineTransform.getTranslateInstance(horizontalDistance, verticalDistance);
        AffineTransformOp filterObj = new AffineTransformOp(transformObj, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage translatedImage = filterObj.filter(theImage, null);
        transformObj = AffineTransform.getRotateInstance(rotationAngleInRadians, horizontalDistance + theImage.getWidth() / 2, verticalDistance + theImage.getHeight() / 2);
        filterObj = new AffineTransformOp(transformObj, interpolationScheme);
        BufferedImage dest = filterObj.createCompatibleDestImage(translatedImage, theImage.getColorModel());
        filterObj.filter(translatedImage, dest);
        return dest;
    }

    void constructPage03() {
        page03.setName("Mirror Image");
        page03.setLayout(new BorderLayout());
    }

    BufferedImage mirrorOperation(BufferedImage theImage) {
        AffineTransform transformObj = AffineTransform.getTranslateInstance(theImage.getWidth(), 0);
        transformObj.scale(-1.0, 1.0);
        double[] theMatrix = new double[6];
        transformObj.getMatrix(theMatrix);
        for (int cnt = 0; cnt < 6; cnt += 2) {
            System.out.print(theMatrix[cnt] + "t");
        }
        System.out.println();
        for (int cnt = 1; cnt < 6; cnt += 2) {
            System.out.print(theMatrix[cnt] + "t");
        }
        System.out.println();
        System.out.println();
        AffineTransformOp filterObj = new AffineTransformOp(transformObj, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage dest = filterObj.createCompatibleDestImage(theImage, theImage.getColorModel());
        filterObj.filter(theImage, dest);
        return dest;
    }

    public BufferedImage processImg(BufferedImage theImage) {
        BufferedImage outputImage = null;
        switch(tabbedPane.getSelectedIndex()) {
            case 0:
                outputImage = scaleOperation(theImage);
                break;
            case 1:
                outputImage = translateOperation(theImage);
                break;
            case 2:
                outputImage = rotateOperation(theImage);
                break;
            case 3:
                outputImage = mirrorOperation(theImage);
                break;
        }
        return outputImage;
    }
}
