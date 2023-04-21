package core.view;

import java.util.HashMap;
import java.util.Observable;
import javax.swing.JTextArea;
import core.View;
import core.annotations.XMLView;
import core.xml.XMLAdaptable;

public class TextArea extends JTextArea implements JaxilComponent, XMLAdaptable {

    private static final long serialVersionUID = 2759416542449443016L;

    private static final String FACTORY = "core.view.component.JTextAreaFactory";

    @Override
    public String getComponentFactory() {
        return FACTORY;
    }

    @Override
    public void addView(View v, String path) {
    }

    @Override
    public Object getVar(String s) {
        return null;
    }

    @Override
    public void removeView(String path) {
    }

    @Override
    public void update(Observable o, Object arg) {
        setText(arg.toString());
    }

    @Override
    public String toString() {
        return getName();
    }

    @XMLView(parametres = { "Text" }, defaults = { "" })
    @Override
    public void addXML(HashMap<String, String> args) {
        if (args.containsKey("Text")) {
            setText(args.get("Text"));
        }
    }
}
