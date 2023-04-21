package com.monad.homerun.viewui;

import com.monad.homerun.model.ModelStatus;

/**
 * @author richard
 *
 */
public interface ModelView {

    public void refresh(ModelStatus[] stats);
}
