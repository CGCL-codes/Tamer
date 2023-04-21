package org.mss.db.hibernateapp.wizard;

import java.util.Date;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

/**
 * Validation Model for DataBinding
 * @author Markus
 *
 */
public class ValidationModel {

    IObservableValue StringValue = new WritableValue(null, String.class);

    IObservableValue dateValue = new WritableValue(null, Date.class);
}
