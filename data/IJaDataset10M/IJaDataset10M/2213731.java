package net.sourceforge.plantuml.suggest;

public class VariatorSwapChar extends VariatorIteratorAdaptor {

    private final String data;

    private int i;

    public VariatorSwapChar(String data) {
        this.data = data;
    }

    @Override
    Variator getVariator() {
        return new Variator() {

            public String getData() {
                if (i >= data.length() - 1) {
                    return null;
                }
                return data.substring(0, i) + data.charAt(i + 1) + data.charAt(i) + data.substring(i + 2);
            }

            public void nextStep() {
                i++;
            }
        };
    }
}
