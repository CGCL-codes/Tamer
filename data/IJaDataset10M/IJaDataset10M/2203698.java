package org.melati.template.webmacro;

import org.melati.template.TemplateEngine;
import org.melati.util.MelatiStringWriter;
import org.webmacro.WM;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * {@link MelatiStringWriter} that can be used with WebMacro.
 *
 * @see WebmacroTemplate#write(org.melati.util.MelatiWriter, 
 *                             org.melati.template.TemplateContext, 
 *                             org.melati.template.TemplateEngine)
 */
public class MelatiWebmacroStringWriter extends MelatiStringWriter implements MelatiWebmacroWriter {

    /**
   * Return a <code>FastWriter</code> that can be used for a while.
   *
   * @see #stopUsingFastWriter(FastWriter)
   */
    public FastWriter getFastWriter(TemplateEngine engine) {
        WebmacroTemplateEngine wte = (WebmacroTemplateEngine) engine;
        WM wm = (WM) wte.getEngine();
        try {
            return FastWriter.getInstance(wm.getBroker(), "UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            assert false : "All Java platforms & webmacro support UTF-16BE";
            return null;
        }
    }

    /**
   * Stop using the given <code>FastWriter</code> obtained from
   * this object.
   *
   * @see #getFastWriter(TemplateEngine)
   */
    public void stopUsingFastWriter(FastWriter fw) throws IOException {
        write(fw.toString());
        fw.close();
    }
}
