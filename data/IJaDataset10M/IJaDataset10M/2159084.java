package com.antilia.web.progress;

import java.awt.Color;
import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class ProgressPanel extends Panel {

    private ProgressImg progressImg;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param id
	 */
    public ProgressPanel(String id) {
        super(id);
        this.progressImg = newProgressImg();
        Image image = new NonCachingImage("image") {

            private static final long serialVersionUID = 1L;

            @Override
            protected Resource getImageResource() {
                ProgressPanel.this.progressImg.invalidate();
                return ProgressPanel.this.progressImg;
            }
        };
        addOrReplace(image);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        this.progressImg.setProgress(getProgress());
    }

    protected ProgressImg newProgressImg() {
        return new ProgressImg(Color.BLUE, false);
    }

    protected abstract int getProgress();
}
