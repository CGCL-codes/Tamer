package darwevo.ui.menu;

import darwevo.ui.states.MapMode;

@SuppressWarnings("serial")
public class NutrientsItem extends ValueChoiceItem<Integer> {

    public NutrientsItem(final MapMode mode) {
        super("Nutrients", mode, MapMode.NUTRIENTS);
    }
}
