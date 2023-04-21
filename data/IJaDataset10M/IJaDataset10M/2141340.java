package mark.utils.bind.modifier;

import javax.swing.text.JTextComponent;
import mark.utils.el.FieldResolver;

public class JTextComponentModifier extends ComponentModifier {

    private JTextComponent comp;

    public JTextComponentModifier(JTextComponent comp, FieldResolver resolver) {
        super(resolver);
        this.comp = comp;
    }

    @Override
    public void updateComponent(Object obj) {
        String value = (String) getResolver().getValue(obj);
        comp.setText(value);
    }

    @Override
    public void updateModel(Object obj) {
        String value = comp.getText();
        getResolver().setValue(obj, value);
    }
}
