package ru.jasatoo;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import ru.jnano.app.bean.converter.DoubleStringConverter;

public class PlanetModelJPanel extends JPanel {

    private JTextField teEllipseB;

    private JTextField teEllipseA;

    private JTextField teMu;

    private JTextField teMu2;

    private JTextField teMu4;

    private JTextField teW;

    private PlanetModel model;

    public PlanetModelJPanel(PlanetModel model) {
        this.model = model;
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("70dlu"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));
        JLabel label = new JLabel("Малая полуось, м:");
        add(label, "2, 2");
        teEllipseB = new JTextField();
        add(teEllipseB, "4, 2");
        teEllipseB.setColumns(10);
        JLabel label_1 = new JLabel("Большая полуось, м:");
        add(label_1, "2, 4");
        teEllipseA = new JTextField();
        add(teEllipseA, "4, 4");
        teEllipseA.setColumns(10);
        JLabel label_2 = new JLabel("Грав. постоянная, м3/с2:");
        add(label_2, "2, 6");
        teMu = new JTextField();
        add(teMu, "4, 6");
        teMu.setColumns(10);
        JLabel label_3 = new JLabel("Вторая гармоника, б/р:");
        add(label_3, "2, 8");
        teMu2 = new JTextField();
        add(teMu2, "4, 8");
        teMu2.setColumns(10);
        JLabel label_4 = new JLabel("Четвертая гармоника, б/р:");
        add(label_4, "2, 10");
        teMu4 = new JTextField();
        add(teMu4, "4, 10");
        teMu4.setColumns(10);
        JLabel label_5 = new JLabel("Уг. скорость вращ., рад/с:");
        add(label_5, "2, 12");
        teW = new JTextField();
        add(teW, "4, 12");
        teW.setColumns(10);
        initDataBindings();
    }

    protected void initDataBindings() {
        BeanProperty<PlanetModel, Double> planetModelBeanProperty = BeanProperty.create("b");
        BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty, teEllipseB, jTextFieldBeanProperty);
        autoBinding.setConverter(new DoubleStringConverter("%.3f"));
        autoBinding.bind();
        BeanProperty<PlanetModel, Double> planetModelBeanProperty_1 = BeanProperty.create("a");
        BeanProperty<JTextField, String> jTextFieldBeanProperty_1 = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty_1, teEllipseA, jTextFieldBeanProperty_1);
        autoBinding_1.setConverter(new DoubleStringConverter("%.3f"));
        autoBinding_1.bind();
        BeanProperty<PlanetModel, Double> planetModelBeanProperty_2 = BeanProperty.create("mu");
        BeanProperty<JTextField, String> jTextFieldBeanProperty_2 = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding_2 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty_2, teMu, jTextFieldBeanProperty_2);
        autoBinding_2.setConverter(new DoubleStringConverter("%.3f"));
        autoBinding_2.bind();
        BeanProperty<PlanetModel, Double> planetModelBeanProperty_3 = BeanProperty.create("mu2");
        BeanProperty<JTextField, String> jTextFieldBeanProperty_3 = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding_3 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty_3, teMu2, jTextFieldBeanProperty_3);
        autoBinding_3.setConverter(new DoubleStringConverter("%.15f"));
        autoBinding_3.bind();
        BeanProperty<PlanetModel, Double> planetModelBeanProperty_4 = BeanProperty.create("mu4");
        BeanProperty<JTextField, String> jTextFieldBeanProperty_4 = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding_4 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty_4, teMu4, jTextFieldBeanProperty_4);
        autoBinding_4.setConverter(new DoubleStringConverter("%.15f"));
        autoBinding_4.bind();
        BeanProperty<PlanetModel, Double> planetModelBeanProperty_5 = BeanProperty.create("w");
        BeanProperty<JTextField, String> jTextFieldBeanProperty_5 = BeanProperty.create("text");
        AutoBinding<PlanetModel, Double, JTextField, String> autoBinding_5 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, model, planetModelBeanProperty_5, teW, jTextFieldBeanProperty_5);
        autoBinding_5.setConverter(new DoubleStringConverter("%.10f"));
        autoBinding_5.bind();
    }
}
