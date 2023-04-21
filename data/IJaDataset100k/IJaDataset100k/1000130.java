package com.mclub.client.forms.given;

import com.gmvc.client.base.AbstractBaseController;
import com.gmvc.client.base.AbstractMiniController;
import com.gmvc.client.base.IBrowser;
import com.gmvc.client.base.IController;
import com.gmvc.client.base.IEditor;
import com.gmvc.client.meta.Event;
import com.gmvc.client.util.Tags;
import com.gmvc.client.util.Utils;
import com.mclub.client.app.SpecialEvent;
import com.mclub.client.model.GivenMovieDetailDTO;

/**
 * Verilen filmler altindaki ekli filmler kismi, Controller sinifi
 * 
 * @see AbstractBaseController
 * 
 * @author mdpinar
 * 
 */
public class GivenMovieMiniController extends AbstractMiniController<GivenMovieDetailDTO> {

    public GivenMovieMiniController(IController<?> parent) {
        super(parent, "GivenMovieDetail");
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getWidth() {
        return 230;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IBrowser<GivenMovieDetailDTO> createBrowser() {
        return new GivenMovieMiniBrowser(this);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IEditor<GivenMovieDetailDTO> createEditor() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void beforeEvent(Event event) {
        switch(event.getType()) {
            case SpecialEvent.Delete:
                {
                    GivenMovieDetailDTO detail = (GivenMovieDetailDTO) event.getModel();
                    if (detail.getId() != null && !detail.getTransId().equals(detail.getMovie().getLastTransId())) {
                        event.setCancel(true);
                        Utils.showAlert(Tags.get("cantDeleteThisMovie"));
                        break;
                    }
                }
        }
    }
}
