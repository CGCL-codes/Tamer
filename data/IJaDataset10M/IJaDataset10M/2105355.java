package vimage;

import java.lang.Character;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.awt.Toolkit;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.textarea.TextArea;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.gjt.sp.jedit.msg.EditorStarted;
import org.gjt.sp.jedit.msg.RegisterChanged;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.Buffer;

public class VimagePlugin extends EBPlugin {

    public static final String NAME = "Vimage";

    public static final String OPTION_PREFIX = "options.vimage";

    public void handleMessage(EBMessage msg) {
        if (msg instanceof EditorStarted) {
            startVimage();
        } else if (msg instanceof ViewUpdate) {
            ViewUpdate vu = (ViewUpdate) msg;
            View view = vu.getView();
            if (view == null) return;
            if (vu.getWhat() == ViewUpdate.CREATED) {
                if (!(view.getInputHandler() instanceof VimageInputHandler)) {
                    view.setInputHandler(new VimageInputHandler(view, loadMaps()));
                }
            } else if (vu.getWhat() == ViewUpdate.ACTIVATED) {
                if (!(view.getInputHandler() instanceof VimageInputHandler)) {
                    view.setInputHandler(new VimageInputHandler(view, loadMaps()));
                }
            }
        } else if (msg instanceof EditPaneUpdate) {
            EditPaneUpdate eu = (EditPaneUpdate) msg;
            if (eu.getWhat() == EditPaneUpdate.CREATED) {
                TextArea textArea = eu.getEditPane().getTextArea();
                View v = eu.getEditPane().getView();
                if (v.getInputHandler() instanceof VimageInputHandler) {
                    VimageInputHandler vih = (VimageInputHandler) v.getInputHandler();
                    boolean bc = !vih.getMode().equals("imap");
                    textArea.getPainter().setBlockCaretEnabled(bc);
                    textArea.invalidateLine(textArea.getCaretLine());
                }
            }
        }
    }

    public static void startVimage() {
        VimageMap map = loadMaps();
        for (View v : jEdit.getViews()) {
            if (!(v.getInputHandler() instanceof VimageInputHandler)) {
                v.setInputHandler(new VimageInputHandler(v, map));
            }
        }
    }

    public static void stopVimage() {
        for (View v : jEdit.getViews()) {
            VimageInputHandler vih = (VimageInputHandler) v.getInputHandler();
            vih.remove();
        }
    }

    public void start() {
        if (jEdit.getViews().length > 0) startVimage();
    }

    public void stop() {
        stopVimage();
    }

    protected static VimageInputHandler getVimageInputHandler(View view) {
        VimageInputHandler vih;
        try {
            vih = (VimageInputHandler) view.getInputHandler();
        } catch (java.lang.ClassCastException ex) {
            return null;
        }
        return vih;
    }

    public static void reloadMaps() {
        VimageMap map = loadMaps();
        for (View v : jEdit.getViews()) {
            VimageInputHandler vih = getVimageInputHandler(v);
            if (vih == null) return;
            vih.setMap(map);
        }
    }

    protected static VimageMap loadMaps() {
        String jar_path = jEdit.getSettingsDirectory() + File.separator + "jars" + File.separator + "Vimage.jar";
        org.gjt.sp.jedit.PluginJAR jar = jEdit.getPluginJAR(jar_path);
        VimageMap map = new VimageMap();
        try {
            ZipFile zip = jar.getZipFile();
            Enumeration entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    String path = entry.getName();
                    if (path.startsWith("defaults/")) {
                        InputStream input = zip.getInputStream(entry);
                        try {
                            VimageParser.parse(map, new BufferedReader(new InputStreamReader(input)));
                        } catch (VimageParser.ParseError ex) {
                            Log.log(Log.ERROR, entry, "Error parsing " + entry.getName());
                        }
                    }
                }
            }
        } catch (java.io.IOException ex) {
            Log.log(Log.DEBUG, ex, ex);
        }
        String user_path = jEdit.getSettingsDirectory() + File.separator + "mappings" + File.separator;
        File user_dir = new File(user_path);
        if (user_dir.exists()) {
            for (String path : user_dir.list()) {
                File user_map = new File(user_path + path);
                if (user_map.isFile()) {
                    try {
                        VimageParser.parse(map, new BufferedReader(new FileReader(user_map)));
                    } catch (java.io.FileNotFoundException ex) {
                        ;
                    } catch (java.io.IOException ex) {
                        Log.log(Log.DEBUG, ex, ex);
                    } catch (VimageParser.ParseError ex) {
                        Log.log(Log.ERROR, user_map, "Error parsing " + user_map.getName());
                    }
                }
            }
        }
        return map;
    }

    public static void gotoExBar(View view, String text) {
        ExBar ex_bar = new ExBar(view);
        view.addToolBar(View.BOTTOM_GROUP, view.ACTION_BAR_LAYER, ex_bar);
        ex_bar.goToExBar(text);
    }

    public static void printKeyBindings(View view) {
        Buffer b = jEdit.newFile(view);
        if (b == null) {
            Log.log(Log.ERROR, view, "Could not create new buffer for bindings; returning.");
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        VimageInputHandler vih = getVimageInputHandler(view);
        if (vih == null) {
            Log.log(Log.ERROR, view, "Vimage isn't running; returning.");
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        VimageMap map = vih.getMap();
        b.insert(0, map.toString());
    }
}
