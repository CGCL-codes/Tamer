package br.eteg.curso.jsf.helloworld;

import javax.faces.context.FacesContext;
import br.eteg.curso.jsf.utils.BackingBeanUtils;

public class HelloWorldBean {

    private String input;

    private String output;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String action() {
        output = getWelcomeMessage() + input;
        return output;
    }

    public String getWelcomeMessage() {
        String text = BackingBeanUtils.getApplicationResourceString("message", null);
        return text;
    }
}
