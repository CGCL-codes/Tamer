package com.kescom.matrix.core.related;

import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.series.IPrivacyLevel;
import com.kescom.matrix.core.series.ISeries;
import com.kescom.matrix.core.series.Series;
import com.kescom.matrix.core.user.IUser;

public class SameTemplateSeriesRelatedEngine implements IRelatedEngine<ISeries, ISeries> {

    @SuppressWarnings("unchecked")
    public List<ISeries> getRelated(ISeries series, IUser requestingUser, Map<String, String> hints) {
        return (List<ISeries>) MatrixContext.getSession().createCriteria(Series.class).add(Restrictions.eq("template", series.getTemplate())).add(Restrictions.ne("index", series.getIndex())).add(Restrictions.ge("size", 3)).add(Restrictions.ge("privacyLevel", IPrivacyLevel.VISIBLE)).addOrder(Order.desc("size")).list();
    }
}
