package com.drakulo.games.ais.ui.component;

import de.matthiasmann.twl.EditField;

public class NumberField extends EditField {

    public NumberField() {
        addCallback(new Callback() {

            @Override
            public void callback(int arg0) {
                String text = NumberField.this.getText();
                final int textLength = text.length();
                if (textLength != 0) {
                    final String lastChat = String.valueOf(text.charAt(textLength - 1));
                    if (!"0123456789".contains(lastChat)) {
                        if (textLength == 1) {
                            NumberField.this.setText("");
                        } else {
                            NumberField.this.setText(text.substring(0, textLength - 1));
                        }
                    }
                }
            }
        });
    }
}
