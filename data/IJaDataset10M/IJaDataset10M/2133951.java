package net.sourceforge.smartconversion.example.simple.easymap.ui.modeltree;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.sourceforge.smartconversion.api.port.java.JavaObjectPort;
import net.sourceforge.smartconversion.api.port.java.JavaObjectPortImpl;
import net.sourceforge.smartconversion.easymap.ui.modeltree.ModelViewTree;
import net.sourceforge.smartconversion.example.simple.direct.modela.Bicycle;
import net.sourceforge.smartconversion.example.simple.direct.modela.Car;
import net.sourceforge.smartconversion.example.simple.direct.modela.Make;
import net.sourceforge.smartconversion.example.simple.direct.modela.Model;
import net.sourceforge.smartconversion.example.simple.direct.modela.SpecialParameters;
import net.sourceforge.smartconversion.example.simple.direct.modela.SpecialParametersCategoryA;
import net.sourceforge.smartconversion.example.simple.direct.modela.SpecialParametersCategoryB;
import net.sourceforge.smartconversion.example.simple.direct.modela.SpecialParametersCategoryC;
import net.sourceforge.smartconversion.example.simple.direct.modela.SpecialParametersCategoryD;
import net.sourceforge.smartconversion.example.simple.direct.modela.Vehicle;
import net.sourceforge.smartconversion.example.simple.direct.modelb.VehicleData;

public class ModelViewTreeSample {

    /**
   * @param args
   */
    public static void main(String[] args) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(Bicycle.class);
        classes.add(Car.class);
        classes.add(Make.class);
        classes.add(Model.class);
        classes.add(SpecialParameters.class);
        classes.add(SpecialParametersCategoryA.class);
        classes.add(SpecialParametersCategoryB.class);
        classes.add(SpecialParametersCategoryC.class);
        classes.add(SpecialParametersCategoryD.class);
        classes.add(Vehicle.class);
        classes.add(VehicleData.class);
        JavaObjectPort port = new JavaObjectPortImpl(Vehicle.class, classes);
        port.inferDefaultModelDefinition();
        ModelViewTree modelViewTree = new ModelViewTree(port.getModelDefinition());
        JFrame f = new JFrame();
        f.setSize(600, 800);
        f.add(modelViewTree);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setBackground(Color.LIGHT_GRAY);
        f.setVisible(true);
    }
}
