package conga.param.spec;

import conga.param.Parameter;

/** @author Justin Caballero */
public interface ParameterSpecification {

    ParameterSpecification ALL = new ParameterSpecification() {

        public boolean isSatisfiedBy(Parameter p) {
            return true;
        }

        public boolean requiresContext() {
            return false;
        }

        public ContextualParameterSpecification contextual() {
            throw new UnsupportedOperationException();
        }

        public ParameterSpecification and(ParameterSpecification spec) {
            return spec;
        }

        @Override
        public String toString() {
            return "ParameterSpecification[ALL]";
        }
    };

    boolean isSatisfiedBy(Parameter p);

    boolean requiresContext();

    ContextualParameterSpecification contextual();

    ParameterSpecification and(ParameterSpecification spec);
}
