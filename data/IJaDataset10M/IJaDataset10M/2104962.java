package jhomenet.ui.panel.weather;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import jhomenet.commons.weather.WeatherGatewayConnectionInfo;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

/**
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class WeatherGatewayConnectionInfoModel extends PresentationModel<WeatherGatewayConnectionInfo> {

    /**
	 * 
	 */
    private final ValidationResultModel validationResultModel;

    /**
	 * @param connectionInfo
	 */
    public WeatherGatewayConnectionInfoModel(WeatherGatewayConnectionInfo connectionInfo) {
        super(connectionInfo);
        validationResultModel = new DefaultValidationResultModel();
        initEventHandling();
        updateValidationResult();
    }

    /**
	 * 
	 * @return
	 */
    public ValidationResultModel getValidationResultModel() {
        return validationResultModel;
    }

    /**
	 * Listens to changes in all properties of the current Order
	 * and to Order changes.
	 */
    private void initEventHandling() {
        PropertyChangeListener handler = new ValidationUpdateHandler();
        addBeanPropertyChangeListener(handler);
        getBeanChannel().addValueChangeListener(handler);
    }

    /**
	 * Update the validation result.
	 */
    private void updateValidationResult() {
        WeatherGatewayConnectionInfo connectionInfo = getBean();
        ValidationResult result = new WeatherGatewayConnectionInfoValidator().validate(connectionInfo);
        validationResultModel.setResult(result);
    }

    /**
	 * Validates the order using an OrderValidator and
	 * updates the validation result.
	 */
    private final class ValidationUpdateHandler implements PropertyChangeListener {

        /**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
        public void propertyChange(PropertyChangeEvent evt) {
            updateValidationResult();
        }
    }
}
