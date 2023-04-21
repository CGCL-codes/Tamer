package org.maestroframework.maestro.modules.admin;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.maestroframework.maestro.annotations.Action;
import org.maestroframework.maestro.annotations.Module;
import org.maestroframework.maestro.annotations.Pref;
import org.maestroframework.maestro.annotations.Prefs;
import org.maestroframework.maestro.interfaces.MaestroModule;
import org.maestroframework.maestro.managers.MaestroPreferencesManager;
import org.maestroframework.maestro.model.MaestroAction;
import org.maestroframework.maestro.model.MaestroContext;
import org.maestroframework.maestro.preferences.MaestroPreferences;
import org.maestroframework.maestro.widgets.AbstractMaestroForm;
import org.maestroframework.maestro.widgets.AbstractMaestroForm.FORMTYPES;
import org.maestroframework.maestro.widgets.MForm;
import org.maestroframework.maestro.widgets.MFormFieldSet;
import org.maestroframework.markup.Component;
import org.maestroframework.markup.html.AnchorTag;
import org.maestroframework.markup.html.HeaderTag;
import org.maestroframework.markup.html.InputTag;
import org.maestroframework.markup.html.ParagraphTag;
import org.maestroframework.markup.html.SelectList;
import org.maestroframework.markup.html.Table;
import org.maestroframework.markup.html.TableRow;
import org.maestroframework.utils.ReflectUtils;
import org.w3c.dom.Node;

@Module(defaultAction = "preferencesForm", acl = "g:admin")
public class PreferencesManager extends MaestroModule {

    private static final Logger LOG = Logger.getLogger(PreferencesManager.class);

    @Action
    public void preferencesForm(Component parent, MaestroContext ctx, Node node) {
        parent.add(new HeaderTag(3, "Maestro Preferences"));
        MaestroPreferencesManager mpm = MaestroPreferencesManager.getInstance();
        MaestroPreferences sitePrefs = MaestroPreferences.getSitePreferences();
        MForm siteForm = parent.add(new MForm("savePref", ctx));
        siteForm.setMethod("POST");
        siteForm.addHidden("className", "MAESTRO-SITE");
        MFormFieldSet sfs = siteForm.addFieldSet("Site Preferences");
        for (String name : sitePrefs.getParameterNames()) {
            sfs.addTextbox(name, name, sitePrefs.get(name)).setSize(55);
        }
        sfs.addSubmit("BUTTON", "Save");
        sfs.addReset("Reset");
        List<String> classNames = mpm.getPreferenceClassNames();
        Collections.sort(classNames);
        for (String name : classNames) {
            Class<?> prefClass = null;
            try {
                prefClass = ReflectUtils.getClass(name);
            } catch (ClassNotFoundException e) {
                LOG.warn("Couldn't load the class for these preferences, removing");
                mpm.removePreferencesForClass(name);
                continue;
            }
            Pref pref = prefClass.getAnnotation(Pref.class);
            Prefs prefs = prefClass.getAnnotation(Prefs.class);
            List<Pref> allPrefs = new ArrayList<Pref>();
            if (pref != null) allPrefs.add(pref);
            if (prefs != null) allPrefs.addAll(Arrays.asList(prefs.value()));
            if (allPrefs.size() == 0) continue;
            AnchorTag formAnchor = parent.add(new AnchorTag());
            formAnchor.setName(name);
            MForm form = parent.add(new MForm("savePref", ctx));
            form.setMethod("POST");
            form.addHidden("className", name);
            MFormFieldSet fs = form.addFieldSet(name);
            MaestroPreferences classPrefs = MaestroPreferences.getPreferences(prefClass);
            for (Pref p : allPrefs) {
                this.addPreferenceField(fs, p, classPrefs.get(p.name()));
            }
            fs.addSubmit("BUTTON", "Save");
            fs.addReset("Reset");
        }
        this.showExportLink(parent, ctx, node);
    }

    @Action
    public void savePref(Component parent, MaestroContext ctx, Node node) throws Exception {
        String prefClassName = ctx.getParameter("className");
        MaestroPreferences classPrefs = null;
        if ("MAESTRO-SITE".equals(prefClassName)) {
            classPrefs = MaestroPreferences.getSitePreferences();
        } else {
            classPrefs = MaestroPreferences.getPreferences(prefClassName);
        }
        for (String paramName : classPrefs.getParameterNames()) {
            if (ctx.hasParameter(paramName)) {
                classPrefs.put(paramName, ctx.getParameter(paramName));
            }
        }
        MaestroAction redirect = ctx.getCurrentPageAction();
        redirect.setAnchor(prefClassName);
        ctx.setRedirect(redirect);
    }

    @Action
    public void showExportLink(Component parent, MaestroContext ctx, Node node) {
        MaestroAction exportAction = ctx.getCurrentAction("exportPreferences");
        parent.add(new ParagraphTag(exportAction.makeAnchorTag("Export Preferences")));
    }

    @Action
    public void exportPreferences(Component parent, MaestroContext ctx, Node node) throws Exception {
        OutputStream out = ctx.openOutputStream("text/xml", null);
        MaestroPreferencesManager.getInstance().exportPreferences(out);
    }

    private void addPreferenceField(AbstractMaestroForm form, Pref p, String value) {
        FORMTYPES type = p.type();
        String label = ("".equals(p.label()) ? p.name() : p.label());
        String name = p.name();
        if (type.equals(FORMTYPES.textbox)) {
            form.addTextbox(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.email)) {
            form.addEmailBox(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.telephone)) {
            form.addTelephoneBox(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.number)) {
            form.addNumberBox(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.url)) {
            form.addUrlBox(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.password)) {
            form.addPassword(label, name, value).setSize(55);
        } else if (type.equals(FORMTYPES.selectList)) {
            SelectList list = form.addSelectList(label, name);
            list.setSelected(value);
            list.addOptions(p.options());
        } else if (type.equals(FORMTYPES.dataSourceList)) {
            form.addDataSourceList(label, name, value);
        } else if (type.equals(FORMTYPES.yesNo)) {
            form.addYesNoList(label, name, Boolean.valueOf(value));
        }
    }
}
