package com.mclub.client.service;

import com.gmvc.client.service.ICRUDService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mclub.client.model.GivenMovieDTO;

/**
 * @see GivenMovieServiceAsync
 * 
 * @author mdpinar
 */
@RemoteServiceRelativePath("givenMovieService")
public interface GivenMovieService extends ICRUDService<GivenMovieDTO> {
}
