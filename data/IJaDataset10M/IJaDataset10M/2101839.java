package com.hs.action;

import com.hs.core.BaseAction;
import com.hs.dao.NationalityDao;
import com.hs.domain.Nationality;

/**
 * @author <a href="mailto:guangzong@gmail.com">Guangzong Syu</a>
 *
 */
public class NationalityAction extends BaseAction {

    private static final long serialVersionUID = 4468563806339323045L;

    private NationalityDao nationalityDao;

    private Nationality nationality;

    /**
	 * @return the nationality
	 */
    public Nationality getNationality() {
        return nationality;
    }

    /**
	 * @param nationality the nationality to set
	 */
    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    /**
	 * @param nationalityDao the nationalityDao to set
	 */
    public void setNationalityDao(NationalityDao nationalityDao) {
        this.nationalityDao = nationalityDao;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
