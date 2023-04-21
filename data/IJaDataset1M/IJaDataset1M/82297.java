package org.geoforge.guitlcolg.dialog.tabs.settings.oxp.panel;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineAnnotationDft;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineColorDft;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineTooltipDft;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineSliderTransparencyDft;
import org.geoforge.mdldspolg.render.globe.oxp.GfrMdlDspRndGlobeWll;
import org.geoforge.wrpbasprsdsp.render.globe.WrpRenderGlobeDimAbs.KindAnnotation;
import org.geoforge.wrpbasprsdspolg.render.oxp.WrpRenderDefaultGlobeWll;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class PnlTabsSettingsDspGlbWllTop extends PnlTabsSettingsDspGlbWllAbs {

    public PnlTabsSettingsDspGlbWllTop(ActionListener alrParent, ChangeListener clrParent) throws Exception {
        super(alrParent);
        float fltTransparency = WrpRenderDefaultGlobeWll.getInstance().getTransparency();
        fltTransparency *= 100;
        int intTransparency = Math.round(fltTransparency);
        Color colCustom = null;
        if (!WrpRenderDefaultGlobeWll.getInstance().isColorRandom()) colCustom = WrpRenderDefaultGlobeWll.getInstance().getColor();
        boolean blnHasTootip = WrpRenderDefaultGlobeWll.getInstance().isTooltip();
        String strValueInitAnnotation = WrpRenderDefaultGlobeWll.getInstance().getKindAnnotationKey();
        super._pnlColor = new PnlDspLineColorDft(alrParent, "Color:", colCustom);
        super._pnlTransparency = new PnlDspLineSliderTransparencyDft(clrParent, "Opacity:", intTransparency);
        super._pnlAnnotation = new PnlDspLineAnnotationDft(alrParent, strValueInitAnnotation);
        super._pnlTooltip = new PnlDspLineTooltipDft(alrParent, blnHasTootip);
    }

    @Override
    public void doJob() throws Exception {
        if (super._pnlColor.hasChangedValue()) {
            Color colValue = ((PnlDspLineColorDft) super._pnlColor).getValue();
            boolean bln = super._pnlColor.isApplyToAll();
            GfrMdlDspRndGlobeWll.getInstance().setColorDefault(colValue, bln);
        }
        if (super._pnlTransparency.hasChangedValue()) {
            int intValue = ((PnlDspLineSliderTransparencyDft) super._pnlTransparency).getValue();
            float fltValue = (float) intValue;
            fltValue /= 100f;
            boolean bln = super._pnlTransparency.isApplyToAll();
            GfrMdlDspRndGlobeWll.getInstance().setTransparencyDefault(fltValue, bln);
        }
        if (super._pnlAnnotation.hasChangedValue()) {
            String strKey = ((PnlDspLineAnnotationDft) super._pnlAnnotation).getValue();
            boolean bln = super._pnlAnnotation.isApplyToAll();
            String strValue = KindAnnotation.s_getValue(strKey);
            GfrMdlDspRndGlobeWll.getInstance().setKindAnnotationDefault(strValue, bln);
        }
        if (super._pnlTooltip.hasChangedValue()) {
            boolean blnValue = ((PnlDspLineTooltipDft) super._pnlTooltip).getValue();
            boolean bln = super._pnlTooltip.isApplyToAll();
            GfrMdlDspRndGlobeWll.getInstance().setTooltipDefault(blnValue, bln);
        }
        super.doJob();
    }
}
