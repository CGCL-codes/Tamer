package com.novocode.naf.gui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.*;
import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.image.*;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.*;

/**
 * A shell or dialog that was created from NGComponent blueprints.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 */
public abstract class WindowInstance {

    public final ModelMap models;

    public final ResourceImageManager imageManager;

    public final FontManager fontManager;

    public final NAFApplication application;

    protected final Display display;

    protected final NGComponent blueprint;

    protected final Thread swtThread;

    private final List<String> persistModels = new ArrayList<String>();

    public WindowInstance(NGComponent blueprint, ModelMap models, NAFApplication application) {
        this.blueprint = blueprint;
        this.models = models;
        this.application = application;
        this.imageManager = application.getImageManager();
        this.fontManager = application.getFontManager();
        this.display = application.getDisplay();
        this.swtThread = display.getThread();
    }

    public abstract void open();

    public void close() {
    }

    public void dispose() {
    }

    public abstract Shell getShell(boolean createDummy);

    public abstract Widget getDisposeSender();

    void addPersistModel(String s) {
        persistModels.add(s);
    }

    public final ModelMap getPersistModels() {
        ModelMap res = new ModelMap();
        for (String s : persistModels) res.put(s, models.get(s));
        return res;
    }
}
