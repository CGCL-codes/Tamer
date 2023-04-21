package net.pepperbytes.plaf.gui;

public class DoubleInputComponent extends TextFieldInputComponent {

    /**
	 * TODO
	 */
    private static final long serialVersionUID = -605181902061158695L;

    @Override
    public Object getInput() throws NumberFormatException, Exception {
        String toParse = super.getInput().toString();
        if (toParse == null || toParse.isEmpty()) {
            toParse = "0";
        }
        return Double.parseDouble(super.getInput().toString());
    }
}
