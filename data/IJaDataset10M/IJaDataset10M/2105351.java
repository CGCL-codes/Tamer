package com.generalynx.ecos.web.form;

import com.generalynx.ecos.data.dao.IBasicDAO;
import com.generalynx.ecos.data.dao.IBasicDAOAware;
import com.generalynx.ecos.utils.BeanConst;
import com.generalynx.ecos.data.dao.IBasicDAO;
import com.generalynx.ecos.data.dao.IBasicDAO;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.Date;

public class AbstractForm extends SimpleFormController implements IBasicDAOAware {

    private IBasicDAO m_basicDAO;

    public IBasicDAO getBasicDAO() {
        return m_basicDAO;
    }

    public void setBasicDAO(IBasicDAO basicDAO) {
        m_basicDAO = basicDAO;
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateFormat dateFormat = (DateFormat) getApplicationContext().getBean(BeanConst.DEFAULT_DATE_FORMAT);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
