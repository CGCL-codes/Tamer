package gui.propertysheet.types;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import gui.propertysheet.BasicProperty;
import gui.propertysheet.types.QualitySettingPropertyConverter;
import gui.visualization.QualityPreset;
import javax.swing.JPanel;

/**
 * A property that allows to switch the quality of visualization. The quality
 * is stored in an enumeration {@link QualityPreset}. There, several presets are
 * defined. The property allows to store one of the presets and provides a
 * {@link JPanel} containing a combo box to select the quality.
 * @author Jan-Philipp Kappmeier
 */
@XStreamAlias("comboBoxNodeQuality")
@XStreamConverter(QualitySettingPropertyConverter.class)
public class QualitySettingProperty extends BasicProperty<QualityPreset> {
}
