package at.gp.web.jsf.extval.validation.metadata.priority;

import org.apache.myfaces.extensions.validator.core.validation.parameter.ValidationParameter;
import org.apache.myfaces.extensions.validator.core.validation.parameter.ParameterKey;
import org.apache.myfaces.extensions.validator.core.validation.parameter.ParameterValue;

/**
 * @author Gerhard Petracek
 *
 * @since 1.x.3
 */
public interface ValidationPriority {

    interface Highest extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.HIGHEST;
    }

    interface High extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.HIGH;
    }

    interface Important extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.IMPORTANT;
    }

    interface Medium extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.MEDIUM;
    }

    interface Low extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.LOW;
    }

    interface Lowest extends ValidationParameter {

        @ParameterKey
        public Class KEY = ValidationPriority.class;

        @ParameterValue
        public Priority VALUE = Priority.LOWEST;
    }
}
