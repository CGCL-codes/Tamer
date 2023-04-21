package net.sf.doolin.app.sc.client.gui;

import net.sf.doolin.app.sc.game.support.FormatUtils;
import net.sf.doolin.gui.swing.LabelInfo;
import net.sf.doolin.gui.swing.LabelInfoProvider;
import net.sf.doolin.gui.util.GUIStrings;

public class AmountLabelProvider implements LabelInfoProvider {

    @Override
    public LabelInfo getLabelIcon(Object item) {
        Long value = (Long) item;
        if (value != null) {
            return new LabelInfo(FormatUtils.formatAmount(null, value));
        } else {
            return new LabelInfo(GUIStrings.get("Amount.unknown"));
        }
    }
}
