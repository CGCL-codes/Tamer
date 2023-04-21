package net.sf.jvdr.http.servlet.settings;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jvdr.Jvdr.JvdrModule;
import net.sf.jvdr.util.JvdrTranslation;
import net.sf.jwan.servlet.exception.WanRenderException;
import net.sf.jwan.servlet.gui.elements.HtmlHref;
import net.sf.jwan.servlet.gui.elements.WanDiv;
import net.sf.jwan.servlet.gui.layer.AbstractWanServletLayer;
import net.sf.jwan.servlet.gui.layer.WanLayer;
import net.sf.jwan.servlet.gui.menu.WanMenu;
import net.sf.jwan.servlet.gui.menu.WanMenuEntry;
import net.sf.jwan.servlet.gui.renderable.WanRenderable;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SettingsOverviewServlet extends AbstractWanServletLayer {

    static Log logger = LogFactory.getLog(SettingsOverviewServlet.class);

    public static final long serialVersionUID = 1;

    private Configuration config;

    private WanLayer lyrShowChannels, lyrTimer, lyrEpgEntries, lyrSmartEpgOvw, lyrIcal;

    public SettingsOverviewServlet(Configuration config) {
        super("lCnfOvw");
        this.config = config;
        layerTitle = JvdrTranslation.get("settings", "settings");
        layerServletPath = "async";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        alWanRenderables.clear();
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
        WanDiv wd = new WanDiv();
        wd.setDivclass(WanDiv.DivClass.iMenu);
        wd.addContent(createSettingsMenu());
        alWanRenderables.add(wd);
        PrintWriter out = response.getWriter();
        try {
            out.println(renderAsync());
        } catch (WanRenderException e) {
            logger.error(e);
        } finally {
            out.close();
        }
    }

    public WanRenderable createSettingsMenu() {
        boolean svdrpActive = config.getBoolean("modules/module[@typ='" + JvdrModule.Svdrp.toString().toLowerCase() + "']");
        WanMenu wmPersonal = new WanMenu();
        if (svdrpActive) {
            WanMenuEntry wmi = new WanMenuEntry();
            wmi.setName(JvdrTranslation.get("settings", "showchannel"));
            HtmlHref href = lyrShowChannels.getLayerTarget();
            href.setRev(HtmlHref.Rev.async);
            wmi.setHtmlref(href);
            wmPersonal.addItem(wmi);
        }
        WanMenuEntry wmi = new WanMenuEntry();
        wmi.setName(JvdrTranslation.get("settings", "timer"));
        HtmlHref href = lyrTimer.getLayerTarget();
        href.setRev(HtmlHref.Rev.async);
        wmi.setHtmlref(href);
        wmPersonal.addItem(wmi);
        wmi = new WanMenuEntry();
        wmi.setName(JvdrTranslation.get("settings", "overview"));
        href = lyrEpgEntries.getLayerTarget();
        href.setRev(HtmlHref.Rev.async);
        wmi.setHtmlref(href);
        wmPersonal.addItem(wmi);
        if (svdrpActive) {
            wmi = new WanMenuEntry();
            wmi.setName("SmartEpg Verwaltung");
            href = lyrSmartEpgOvw.getLayerTarget();
            href.setRev(HtmlHref.Rev.async);
            wmi.setHtmlref(href);
            wmPersonal.addItem(wmi);
        }
        wmi = new WanMenuEntry();
        wmi.setName("iCal Support");
        href = lyrIcal.getLayerTarget();
        href.setRev(HtmlHref.Rev.async);
        wmi.setHtmlref(href);
        wmPersonal.addItem(wmi);
        return wmPersonal;
    }

    public void setLyrShowChannels(WanLayer lyrShowChannels) {
        this.lyrShowChannels = lyrShowChannels;
    }

    public void setLyrTimer(WanLayer lyrTimer) {
        this.lyrTimer = lyrTimer;
    }

    public void setLyrEpgEntries(WanLayer lyrEpgEntries) {
        this.lyrEpgEntries = lyrEpgEntries;
    }

    public void setLyrSmartEpgOvw(WanLayer lyrSmartEpgOvw) {
        this.lyrSmartEpgOvw = lyrSmartEpgOvw;
    }

    public void setLyrIcal(WanLayer lyrIcal) {
        this.lyrIcal = lyrIcal;
    }
}
