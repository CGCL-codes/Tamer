package com.yeep.objanalyser.common.dao.ibatis;

import com.yeep.objanalyser.common.model.Association;

public class AssociationDaoIbatis extends ModelDaoIbatis<Association> {

    private static final String STATEMENT_DELETE_BY_UNIVERSE = "Association.deleteByUniverse";

    @Override
    public String getNamespace() {
        return "Association";
    }

    /**
	 * Delete all the Associations related to the specified universe
	 * @param universeId
	 */
    public void deleteByUniverse(Integer universeId) {
        super.delete(STATEMENT_DELETE_BY_UNIVERSE, universeId);
    }
}
