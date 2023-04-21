package com.luzan.app.map.tool;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Junction;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import com.luzan.app.map.utils.Configuration;
import com.luzan.app.map.bean.user.UserMapOriginal;
import com.luzan.db.DBConnectionFactoryImpl;
import com.luzan.db.hibernate.dao.UserMapOriginalHibernateDAO;
import com.luzan.db.dao.UserMapOriginalDAO;

public class Exporter {

    private static final Logger logger = Logger.getLogger(Exporter.class);

    private String cfg;

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    private void doIt() throws Throwable {
        UserMapOriginalDAO dao = new UserMapOriginalHibernateDAO(UserMapOriginal.class);
        try {
            Junction exp = Expression.conjunction().add(Expression.ne("substate", UserMapOriginal.SubState.BROKEN)).add(Expression.eq("state", UserMapOriginal.State.UPLOAD));
            List<UserMapOriginal> list = dao.findByCriteria(exp);
            for (UserMapOriginal m : list) {
                String guid = m.getGuid();
                String region = m.getRegion();
                Double neLat = m.getNELat();
                Double neLon = m.getNELon();
                Double swLat = m.getSWLat();
                Double swLon = m.getSWLon();
                if (!StringUtils.isEmpty(guid) && !StringUtils.isEmpty(region) && neLat != null && neLon != null && swLat != null && swLon != null) {
                    String[] regParts = region.split(" ");
                    String rLeftTopX = regParts[1].split(":")[0];
                    String rLeftTopY = regParts[1].split(":")[1];
                    String rLeftBottomX = regParts[0].split(":")[0];
                    String rLeftBottomY = regParts[0].split(":")[1];
                    String rRightTopX = regParts[2].split(":")[0];
                    String rRightTopY = regParts[2].split(":")[1];
                    String rRightBottomX = regParts[3].split(":")[0];
                    String rRightBottomY = regParts[3].split(":")[1];
                    String r = String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s", guid, rLeftTopX, rLeftTopY, swLon, neLat, rRightTopX, rRightTopY, neLon, neLat, rRightBottomX, rRightBottomY, neLon, swLat, rLeftBottomX, rLeftBottomY, swLon, swLat);
                    System.out.println(r);
                }
            }
        } catch (Throwable e) {
            logger.error("Error exporting", e);
        }
    }

    public static void main(String args[]) {
        Exporter importer = new Exporter();
        String allArgs = StringUtils.join(args, ' ');
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(Exporter.class, Object.class);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                Pattern p = Pattern.compile("-" + pd.getName() + "\\s*([\\S]*)", Pattern.CASE_INSENSITIVE);
                final Matcher m = p.matcher(allArgs);
                if (m.find()) {
                    pd.getWriteMethod().invoke(importer, m.group(1));
                }
            }
            Configuration.getInstance().load(importer.cfg);
            DBConnectionFactoryImpl.configure(importer.cfg);
            importer.doIt();
        } catch (Throwable e) {
            logger.error("error", e);
            System.out.println(e.getMessage());
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(Exporter.class);
                System.err.println("Options:");
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    System.err.println("-" + pd.getName());
                }
            } catch (Throwable t) {
                System.out.print("Internal error");
            }
        }
    }
}
