package com.ma_la.myRunning.converter;

import com.ma_la.myRunning.domain.TrainingRoute;
import com.ma_la.myRunning.DaoFactory;
import com.ma_la.myRunning.RunningSystemBean;
import com.ma_la.myRunning.dao.TrainingRouteDao;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converter fuer ne Trainings-Strecke
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class TrainingRouteConverter implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String text) {
        if (!RunningSystemBean.checkValueForLong(text)) return null;
        try {
            TrainingRouteDao trainingRouteDao = DaoFactory.getInstance().getTrainingRouteDao();
            return trainingRouteDao.findById(new Long(text));
        } catch (Exception e) {
            throw new ConverterException("TrainingRouteConverter.ConverterException", e);
        }
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object component) {
        if (component == null || !(component instanceof TrainingRoute)) {
            return null;
        }
        return ((TrainingRoute) component).getId().toString();
    }

    public static String getAsString(TrainingRoute trainingRoute) {
        if (trainingRoute == null) {
            return null;
        }
        return trainingRoute.getName();
    }
}
