package org.doit.muffin.filter;

import java.awt.*;
import org.doit.util.*;

public class NoCodeFrame extends AbstractFrame {

    /**
     * @see org.doit.muffin.filter.AbstractFrame#AbstractFrame(AbstractFilterFactory)
     */
    public NoCodeFrame(NoCode factory) {
        super(factory);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFrame#doMakeContent()
     */
    protected Panel doMakeContent() {
        Panel panel = new Panel(new BorderLayout());
        panel.add(BorderLayout.NORTH, makeConfigPanel());
        getFactory().getMessages().setEditable(false);
        panel.add(BorderLayout.CENTER, getFactory().getMessages());
        panel.add(BorderLayout.SOUTH, makeButtonPanel());
        return panel;
    }

    private Panel makeConfigPanel() {
        Panel panel = new Panel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c;
        noJavaScript = new Checkbox(Strings.getString("NoCode.noJavaScript"));
        noJavaScript.setState(getFactory().getPrefsBoolean(NoCode.NOJAVASCRIPT));
        noVBScript = new Checkbox(Strings.getString("NoCode.noVBScript"));
        noVBScript.setState(getFactory().getPrefsBoolean(NoCode.NOVBSCRIPT));
        noOtherScript = new Checkbox(Strings.getString("NoCode.noOtherScript"));
        noOtherScript.setState(getFactory().getPrefsBoolean(NoCode.NOOTHERSCRIPT));
        noEncodedScript = new Checkbox(Strings.getString("NoCode.noEncodedScript"));
        noEncodedScript.setState(getFactory().getPrefsBoolean(NoCode.NOENCODEDSCRIPT));
        noEvalInScript = new Checkbox(Strings.getString("NoCode.noEvalInScript"));
        noEvalInScript.setState(getFactory().getPrefsBoolean(NoCode.NOEVALINSCRIPT));
        noJava = new Checkbox(Strings.getString("NoCode.noJava"));
        noJava.setState(getFactory().getPrefsBoolean(NoCode.NOJAVA));
        c = new GridBagConstraints();
        layout.setConstraints(noJavaScript, c);
        panel.add(noJavaScript);
        layout.setConstraints(noVBScript, c);
        panel.add(noVBScript);
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(noOtherScript, c);
        panel.add(noOtherScript);
        c = new GridBagConstraints();
        layout.setConstraints(noEncodedScript, c);
        panel.add(noEncodedScript);
        layout.setConstraints(noEvalInScript, c);
        panel.add(noEvalInScript);
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(noJava, c);
        panel.add(noJava);
        return panel;
    }

    /**
     * @see org.doit.muffin.filter.AbstractFrame#doApply()
     */
    protected void doApply() {
        getFactory().putPrefsBoolean(NoCode.NOJAVASCRIPT, noJavaScript.getState());
        getFactory().putPrefsBoolean(NoCode.NOVBSCRIPT, noVBScript.getState());
        getFactory().putPrefsBoolean(NoCode.NOOTHERSCRIPT, noOtherScript.getState());
        getFactory().putPrefsBoolean(NoCode.NOENCODEDSCRIPT, noEncodedScript.getState());
        getFactory().putPrefsBoolean(NoCode.NOEVALINSCRIPT, noEvalInScript.getState());
        getFactory().putPrefsBoolean(NoCode.NOJAVA, noJava.getState());
    }

    /**
     * @see org.doit.muffin.filter.AbstractFrame#doMakeButtonList()
     */
    protected String[] doMakeButtonList() {
        return new String[] { APPLY_CMD, SAVE_CMD, CLEAR_CMD, CLOSE_CMD, HELP_CMD };
    }

    private Checkbox noJava, noJavaScript, noVBScript, noOtherScript;

    private Checkbox noEncodedScript, noEvalInScript;
}
