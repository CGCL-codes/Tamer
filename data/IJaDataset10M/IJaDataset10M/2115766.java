package org.lokee.punchcard.webui.render;

import java.util.LinkedHashMap;
import org.lokee.punchcard.config.CardConfig;

/**
 * @author claguerre
 *
 */
public class CardComponent {

    private CardConfig cardConfig;

    private LinkedHashMap fieldComponents;

    public CardComponent(CardConfig cardConfig) {
        this.cardConfig = cardConfig;
        fieldComponents = new LinkedHashMap();
    }

    public void addFieldComponent(FieldComponent fieldComponent) {
        fieldComponents.put(fieldComponent.getName(), fieldComponent);
    }

    public FieldComponent getFieldComponent(String name) {
        return (FieldComponent) fieldComponents.get(name);
    }

    public FieldComponent[] getFieldComponents() {
        return (FieldComponent[]) fieldComponents.values().toArray(new FieldComponent[fieldComponents.size()]);
    }

    public CardConfig getCardConfig() {
        return this.cardConfig;
    }

    public String getName() {
        return this.cardConfig.getName();
    }
}
