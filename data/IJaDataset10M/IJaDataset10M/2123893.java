package yocef.devguide.client.glue;

import yocef.client.Event;
import yocef.client.EventBind;
import yocef.client.EventGlue;
import yocef.client.EventParams;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Glue a GWT TextBox or PasswordTextBox with a "textChanged" event
 */
@SuppressWarnings("nls")
public class GwtTextBoxGlue extends EventGlue {

    /**
	 * Automatic glue
	 */
    public GwtTextBoxGlue() {
        super(TextBox.class);
    }

    @Override
    public boolean accept(final EventBind binding) {
        if (binding.getEvent() == Event.get("textChanged")) {
            final TextBox textBox = (TextBox) binding.getSource();
            final KeyUpHandler handler = new KeyUpHandler() {

                @Override
                public void onKeyUp(KeyUpEvent event) {
                    final EventParams param = new EventParams().put("keycode", event.getNativeKeyCode());
                    binding.getAction().handle(binding.getEvent(), textBox, binding.getContext(), param);
                }
            };
            HandlerRegistration hReg = textBox.addKeyUpHandler(handler);
            binding.addData("handler", hReg);
            return true;
        }
        if (binding.getEvent() == Event.get("gotFocus")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean release(final EventBind binding) {
        if (binding.getEvent() == Event.get("textChanged")) {
            final HandlerRegistration hReg = (HandlerRegistration) binding.getData("handler");
            hReg.removeHandler();
            return true;
        }
        return false;
    }
}
