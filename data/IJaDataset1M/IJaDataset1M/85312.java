package com.ateam.webstore.service;

import java.io.Serializable;
import java.util.Collection;
import com.ateam.webstore.dao.common.BaseModel;

/**
 * @author Hendrix Tavarez
 *
 */
@SuppressWarnings("hiding")
public interface RepositoryService<BaseModel> {

    /**
	 * Save BaseModel in database
	 * 
	 * @param BaseModel
	 * @return
	 */
    public BaseModel store(BaseModel BaseModel);

    /**
	 * Remove BaseModel from database
	 * 
	 * @param BaseModel
	 */
    public void remove(BaseModel BaseModel);

    /**
	 * Get all BaseModels in the database
	 * 
	 * @return all BaseModels
	 */
    public Collection<BaseModel> getAll();

    public BaseModel getById(Serializable id);
}
