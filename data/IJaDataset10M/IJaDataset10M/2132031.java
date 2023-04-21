package com.generalynx.ecos.web.controller;

import com.generalynx.ecos.data.dao.IBasicDAO;
import com.generalynx.ecos.data.dao.IBasicDAOAware;
import com.generalynx.ecos.utils.BeanConst;
import com.generalynx.ecos.web.interceptor.ObjectAttacherInterceptor;
import com.alesj.newsfeed.core.MsgCodeException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;

/**
 * Flow controller.
 * Using this controller all dates will be parsed using CustomDateEditor with format BeanConst.DEFAULT_DATETIME_FORMAT.
 * All numbers use CustomNumberEditor which allows empty strings. If empty strings shouldn't be allowed register
 * your own custom editor. For example see ADEController method bind where target specific property editors are
 * added after global default property editors.
 */
public abstract class ExceptionMultiActionController extends MultiActionController implements IBasicDAOAware {

    private IBasicDAO m_basicDAO;

    public IBasicDAO getBasicDAO() {
        return m_basicDAO;
    }

    public void setBasicDAO(IBasicDAO basicDAO) {
        m_basicDAO = basicDAO;
    }

    protected void initBinder(ServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        DateFormat df = (DateFormat) getApplicationContext().getBean(BeanConst.DEFAULT_DATETIME_FORMAT);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
        binder.registerCustomEditor(Byte.class, new CustomNumberEditor(Byte.class, true));
        binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
    }

    public ModelAndView defaultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        logger.error("Exception thrown: " + exception.getMessage());
        String view = exception instanceof MsgCodeException ? "msgView" : "exceptionView";
        ModelAndView mv = new ModelAndView(view, "exception", exception);
        HandlerInterceptor hi = new ObjectAttacherInterceptor(false);
        try {
            hi.postHandle(request, response, null, mv);
        } catch (Exception e) {
            logger.warn("Handler interceptor threw exception: " + e.getMessage());
        }
        return mv;
    }
}
