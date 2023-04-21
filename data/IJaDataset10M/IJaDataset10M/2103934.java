package vavi.apps.editablePanel.beans;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * {@link vavi.apps.editablePanel.beans.Slider} �̃J�X�^�}�C�U�ł��D�D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          1.00 010830 nsano let it beans <br>
 */
public class SliderCustomizer extends JComponent implements Customizer {

    /** ���\�[�X�o���h�� */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** �ҏW�Ώ� */
    private Slider slider;

    /** �ŏ��l�̃t�B�[���h */
    private JTextField minField = new JTextField();

    /** �ő�l�̃t�B�[���h */
    private JTextField maxField = new JTextField();

    /** �ڐ���̕������̃t�B�[���h */
    private JTextField divField = new JTextField();

    /** �X���C�_�[�̃v���p�e�B�G�f�B�^ */
    public SliderCustomizer() {
        this.setLayout(new GridLayout(3, 2));
        this.add(new JLabel(rb.getString("sliderCustomizer.label.min")));
        this.add(minField);
        this.add(new JLabel(rb.getString("sliderCustomizer.label.max")));
        this.add(maxField);
        this.add(new JLabel(rb.getString("sliderCustomizer.label.div")));
        this.add(divField);
        minField.addActionListener(actionListener);
        maxField.addActionListener(actionListener);
        divField.addActionListener(actionListener);
    }

    /** TODO */
    private ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            slider.setMinimum(new Double(minField.getText()).doubleValue());
            slider.setMaximum(new Double(maxField.getText()).doubleValue());
            slider.setDividingCount(new Integer(divField.getText()).intValue());
        }
    };

    /**
     * �ҏW�Ώۂ�ݒ肵�܂��D
     */
    public void setObject(Object object) {
        slider = (Slider) object;
        minField.removeActionListener(actionListener);
        maxField.removeActionListener(actionListener);
        divField.removeActionListener(actionListener);
        minField.setText(String.valueOf(slider.getMinimum()));
        maxField.setText(String.valueOf(slider.getMaximum()));
        divField.setText(String.valueOf(slider.getDividingCount()));
        minField.addActionListener(actionListener);
        maxField.addActionListener(actionListener);
        divField.addActionListener(actionListener);
    }
}
