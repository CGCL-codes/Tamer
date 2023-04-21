package de.beas.explicanto.client.sec.actions;

import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.sec.model.ScreenplayModel;
import de.beas.explicanto.client.sec.views.model.MemberConsts;

public class DeleteImageAction extends DeleteAction {

    public DeleteImageAction(ScreenplayModel screenplayModel) {
        super(screenplayModel);
    }

    protected void init() {
        super.init();
        setText(I18N.translate("sec.scene.deleteImage"));
        setToolTipText(I18N.translate("sec.scene.deleteImage.tooltip"));
        setDescription(I18N.translate("sec.scene.deleteImage.desc"));
        castType = MemberConsts.IMAGE_CLASS;
        infoText = I18N.translate("sec.scene.deleteImageInfo");
    }
}
