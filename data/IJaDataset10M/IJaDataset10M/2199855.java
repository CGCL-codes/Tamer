package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.core.wiring.annotations.Wire;

@Component
public class Greeter {

    Message message;

    Displayer display;

    public Greeter() {
    }

    @Wire
    public void setDisplayer(Displayer display) {
        this.display = display;
    }

    @Wire
    public void setMessage(Message message) {
        this.message = message;
    }

    public void sayHello() {
        display.display(message.getText());
    }
}
