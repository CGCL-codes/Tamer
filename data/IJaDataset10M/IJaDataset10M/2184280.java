package org.nsu.learn.gui.client.dialog.view;

import org.nsu.learn.gui.client.window.model.ThemeBlockModel;

/**
 * @author makarov
 * @version 1.0.26.02.2011
 *
 */
public class ThemeBlockViewDialog extends AbstractViewDialog {

    public ThemeBlockViewDialog(ThemeBlockModel baseModel) {
        super(baseModel);
        setHeading("Просмотр тематического блока \"" + baseModel.getThemeBlockCode() + "\"");
        addText("<h1>Тематический блок</h1><br/>");
        addText("<b>ИД:</b> " + baseModel.getId());
        addText("<b>Код тематического блока:</b> " + baseModel.getThemeBlockCode());
        addText("<b>Название тематического блока: </b>" + baseModel.getThemeBlockName());
    }
}
