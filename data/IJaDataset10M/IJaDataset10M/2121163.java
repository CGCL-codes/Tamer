package org.damour.base.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree.Resources;

public interface BaseImageBundle extends ClientBundle, Resources {

    public static final BaseImageBundle images = (BaseImageBundle) GWT.create(BaseImageBundle.class);

    ImageResource player_play_32();

    ImageResource player_pause_32();

    ImageResource player_next_32();

    ImageResource player_prev_32();

    ImageResource player_hide_32();

    ImageResource player_close_32();

    ImageResource player_stop_32();

    ImageResource play16();

    ImageResource play16_disabled();

    ImageResource stop16();

    ImageResource stop16_disabled();

    ImageResource treeOpen();

    ImageResource treeClosed();

    ImageResource treeLeaf();

    ImageResource file16x16();

    ImageResource open_32();

    ImageResource open_disabled_32();

    ImageResource save_16();

    ImageResource save_disabled_16();

    ImageResource upload();

    ImageResource upload_disabled();

    ImageResource download();

    ImageResource download_disabled();

    ImageResource newFolder();

    ImageResource newFolder_disabled();

    ImageResource rename();

    ImageResource rename_disabled();

    ImageResource share();

    ImageResource share_disabled();

    ImageResource properties16();

    ImageResource properties_disabled_16();

    ImageResource lock16();

    ImageResource settings16();

    ImageResource folder32();

    ImageResource file32();

    ImageResource archive32();

    ImageResource png32();

    ImageResource gif32();

    ImageResource jpg32();

    ImageResource bmp32();

    ImageResource image16();

    ImageResource movie32();

    ImageResource audio32();

    ImageResource text32();

    ImageResource html32();

    ImageResource jar32();

    ImageResource location_16();

    ImageResource location_disabled_16();

    ImageResource showHide16();

    ImageResource find16x16();

    ImageResource edit16();

    ImageResource editPencil16();

    ImageResource next();

    ImageResource previous();

    ImageResource first();

    ImageResource last();

    ImageResource hierarchy();

    ImageResource flatten();

    ImageResource sort();

    ImageResource refresh_16();

    ImageResource refresh_disabled_16();

    ImageResource reply();

    ImageResource add();

    ImageResource delete();

    ImageResource delete_disabled();

    ImageResource approve();

    ImageResource empty16x16();

    ImageResource empty8x8();

    ImageResource downArrow();

    ImageResource downArrowDisabled();

    ImageResource closeTab();

    ImageResource closeTabHover();

    ImageResource starNoVotes();

    ImageResource starFull();

    ImageResource starHalf();

    ImageResource starEmpty();

    ImageResource starHover();

    ImageResource advisoryNR();

    ImageResource advisoryG();

    ImageResource advisoryPG();

    ImageResource advisoryPG13();

    ImageResource advisoryR();

    ImageResource advisoryNC17();

    ImageResource thumbUp();

    ImageResource thumbDown();

    ImageResource email16x16();

    ImageResource permalink();

    ImageResource disclosureOpen();

    ImageResource disclosureClose();
}
