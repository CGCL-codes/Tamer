package tests.ellipse;

import java.awt.Color;
import java.io.IOException;
import org.junit.Test;
import pl.edu.amu.wmi.kino.visualjavafx.javafxcodegenerators.api.JavaFXCodeGenerator;
import pl.edu.amu.wmi.kino.visualjavafx.model.generators.ModelEllipseGenerator;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.Application;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.shapes.Elipse;
import tests.helpers.Test4Provider;

/**
 *PASSED - what's with those artifacts ? 
 * @author Admin
 */
public class EllipseTest4 {

    @Test
    public void main() throws IOException {
        Test4Provider m = new Test4Provider();
        Elipse c = ModelEllipseGenerator.generateElipse(500, 300, 50, 100, Color.ORANGE, Color.BLUE, 5);
        Application app = m.getModelForObject(c);
        String res = JavaFXCodeGenerator.generateCode(app);
        System.out.println(res);
    }
}
