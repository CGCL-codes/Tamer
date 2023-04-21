package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.Option;
import com.volantis.mcs.protocols.OptionVisitor;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.utilities.StringConvertor;
import java.util.List;

/**
 * This class will traverse a list of Option objects and calculate the
 * set of initial values
 */
public final class InitialValueHandler {

    private final SingleSelectInitialValueVisitor singleSelectVisitor = new SingleSelectInitialValueVisitor();

    private final MultiSelectInitialValueVisitor multiSelectVisitor = new MultiSelectInitialValueVisitor();

    public String getInitialValue(XFSelectAttributes attributes) throws ProtocolException {
        if (attributes.isMultiple()) {
            return multiSelectVisitor.getInitialValue(attributes);
        } else {
            return singleSelectVisitor.getInitialValue(attributes);
        }
    }

    private class SingleSelectInitialValueVisitor implements OptionVisitor {

        private int optionIndex;

        private boolean valueFound;

        public String getInitialValue(XFSelectAttributes attributes) throws ProtocolException {
            optionIndex = 0;
            valueFound = false;
            visitOptions(attributes.getOptions(), null);
            return (valueFound) ? StringConvertor.valueOf(optionIndex) : null;
        }

        private void visitOptions(List options, Object object) throws ProtocolException {
            Option option;
            for (int i = 0; i < options.size() && !valueFound; i++) {
                option = (Option) options.get(i);
                option.visit(this, object);
            }
        }

        public void visit(SelectOption selectOption, Object object) {
            optionIndex++;
            if (selectOption.isSelected()) {
                valueFound = true;
            }
        }

        public void visit(SelectOptionGroup selectOptionGroup, Object object) throws ProtocolException {
            visitOptions(selectOptionGroup.getSelectOptionList(), object);
        }
    }

    /**
     * Class that calculates the ivalue for a WML select element
     */
    private class MultiSelectInitialValueVisitor implements OptionVisitor {

        private int optionIndex;

        /**
         * return the initial value for a multiple select control
         *
         * @param attributes the XFSelectAttributes
         * @return the initial value as a colon seperated String or null of no
         *         Options are selected.
         */
        public String getInitialValue(XFSelectAttributes attributes) throws ProtocolException {
            optionIndex = 0;
            StringBuffer valueBuffer = new StringBuffer();
            visitOptions(attributes.getOptions(), valueBuffer);
            return (0 == valueBuffer.length()) ? null : valueBuffer.toString();
        }

        private void visitOptions(List options, Object object) throws ProtocolException {
            Option option;
            for (int i = 0; i < options.size(); i++) {
                option = (Option) options.get(i);
                option.visit(this, object);
            }
        }

        public void visit(SelectOption selectOption, Object object) {
            optionIndex++;
            if (selectOption.isSelected()) {
                StringBuffer sb = (StringBuffer) object;
                if (sb.length() > 0) {
                    sb.append(';');
                }
                sb.append(optionIndex);
            }
        }

        public void visit(SelectOptionGroup selectOptionGroup, Object object) throws ProtocolException {
            visitOptions(selectOptionGroup.getSelectOptionList(), object);
        }
    }
}
