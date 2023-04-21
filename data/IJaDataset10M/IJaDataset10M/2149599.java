package com.dotmarketing.portlets.events.factories;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import net.sf.hibernate.HibernateException;
import org.apache.commons.beanutils.BeanUtils;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.Inode;
import com.dotmarketing.db.DotHibernate;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.events.model.Event;
import com.dotmarketing.portlets.events.model.EventRegistration;
import com.dotmarketing.portlets.events.model.Recurance;
import com.dotmarketing.portlets.facilities.model.Facility;
import com.dotmarketing.portlets.files.model.File;

/**
 *
 * @author  will
 */
public class RecuranceFactory {

    public static Recurance newInstance() {
        Recurance r = new Recurance();
        return r;
    }

    public static void buildRecurringEvents(Recurance r, Event e) throws InvocationTargetException, IllegalAccessException, HibernateException {
        if (r == null || e == null || r.getInode() == 0 || e.getInode() == 0) {
            return;
        }
        List l = InodeFactory.getParentsOfClass(r, Event.class);
        Iterator i = l.iterator();
        while (i.hasNext()) {
            Event evt = (Event) i.next();
            if (evt.getSetupDate().before(e.getSetupDate())) {
                e.setSetupDate(evt.getSetupDate());
                e.setBreakDate(evt.getBreakDate());
            }
        }
        int interval = r.getInterval();
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        GregorianCalendar setupDate = new GregorianCalendar();
        GregorianCalendar breakDate = new GregorianCalendar();
        startDate.setTime(r.getStarting());
        endDate.setTime(r.getEnding());
        endTime.setTime(r.getEnding());
        setupDate.setTime(e.getSetupDate());
        breakDate.setTime(e.getBreakDate());
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        new DotHibernate().getSession().refresh(e);
        List parents = InodeFactory.getParentsOfClass(e, Category.class);
        parents.addAll(InodeFactory.getParentsOfClass(e, Facility.class));
        List children = InodeFactory.getChildrenClass(e, File.class);
        children.addAll(InodeFactory.getChildrenClass(e, EventRegistration.class));
        children.addAll(InodeFactory.getChildrenClass(e, Recurance.class));
        children.addAll(InodeFactory.getChildrenClass(e, Identifier.class));
        i = l.iterator();
        while (i.hasNext()) {
            Event event = (Event) i.next();
            if (event.getInode() != e.getInode()) {
                InodeFactory.deleteInode(event);
            }
        }
        while (startDate.getTime().before(endDate.getTime())) {
            Event recEvent = new Event();
            BeanUtils.copyProperties(recEvent, e);
            recEvent.setInode(0);
            if ("day".equals(r.getOccurs())) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(startDate.getTime());
                cal.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
                recEvent.setStartDate(cal.getTime());
                cal = new GregorianCalendar();
                cal.setTime(startDate.getTime());
                cal.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
                recEvent.setEndDate(cal.getTime());
                recEvent.setSetupDate(setupDate.getTime());
                recEvent.setBreakDate(breakDate.getTime());
                if (cal.getTime().after(endDate.getTime())) break;
                InodeFactory.saveInode(recEvent);
                recEvent.addChild(r);
                Iterator ii = parents.iterator();
                while (ii.hasNext()) {
                    Inode inode = (Inode) ii.next();
                    inode.addChild(recEvent);
                }
                ii = children.iterator();
                while (ii.hasNext()) {
                    Inode inode = (Inode) ii.next();
                    recEvent.addChild(inode);
                }
                startDate.add(Calendar.DAY_OF_MONTH, interval);
                setupDate.add(Calendar.DAY_OF_MONTH, interval);
                breakDate.add(Calendar.DAY_OF_MONTH, interval);
            } else if ("week".equals(r.getOccurs())) {
                if (r.getDaysOfWeek() == null) {
                    InodeFactory.deleteInode(r);
                    return;
                }
                for (int j = 1; j < 8; j++) {
                    String x = Integer.toString(startDate.get(Calendar.DAY_OF_WEEK));
                    if (r.getDaysOfWeek().indexOf(x) > -1) {
                        recEvent = new Event();
                        BeanUtils.copyProperties(recEvent, e);
                        recEvent.setInode(0);
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(startDate.getTime());
                        cal.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
                        recEvent.setStartDate(cal.getTime());
                        cal = new GregorianCalendar();
                        cal.setTime(startDate.getTime());
                        cal.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
                        recEvent.setEndDate(cal.getTime());
                        recEvent.setSetupDate(setupDate.getTime());
                        recEvent.setBreakDate(breakDate.getTime());
                        if (cal.getTime().after(endDate.getTime())) break;
                        InodeFactory.saveInode(recEvent);
                        recEvent.addChild(r);
                        Iterator ii = parents.iterator();
                        while (ii.hasNext()) {
                            Inode inode = (Inode) ii.next();
                            inode.addChild(recEvent);
                        }
                        ii = children.iterator();
                        while (ii.hasNext()) {
                            Inode inode = (Inode) ii.next();
                            recEvent.addChild(inode);
                        }
                    }
                    startDate.add(Calendar.DAY_OF_MONTH, 1);
                    setupDate.add(Calendar.DAY_OF_MONTH, 1);
                    breakDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                startDate.add(Calendar.WEEK_OF_YEAR, interval - 1);
                setupDate.add(Calendar.WEEK_OF_YEAR, interval - 1);
                breakDate.add(Calendar.WEEK_OF_YEAR, interval - 1);
            } else {
                if (startDate.get(Calendar.DAY_OF_MONTH) > r.getDayOfMonth()) {
                    startDate.add(Calendar.MONTH, 1);
                }
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(startDate.getTime());
                cal.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
                cal.set(Calendar.DAY_OF_MONTH, r.getDayOfMonth());
                recEvent.setStartDate(cal.getTime());
                cal = new GregorianCalendar();
                cal.setTime(startDate.getTime());
                cal.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
                cal.set(Calendar.DAY_OF_MONTH, r.getDayOfMonth());
                recEvent.setEndDate(cal.getTime());
                recEvent.setSetupDate(setupDate.getTime());
                recEvent.setBreakDate(breakDate.getTime());
                if (cal.getTime().after(endDate.getTime())) break;
                InodeFactory.saveInode(recEvent);
                recEvent.addChild(r);
                Iterator ii = parents.iterator();
                while (ii.hasNext()) {
                    Inode inode = (Inode) ii.next();
                    inode.addChild(recEvent);
                }
                ii = children.iterator();
                while (ii.hasNext()) {
                    Inode inode = (Inode) ii.next();
                    recEvent.addChild(inode);
                }
                startDate.add(Calendar.MONTH, interval);
                setupDate.add(Calendar.MONTH, interval);
                breakDate.add(Calendar.MONTH, interval);
            }
        }
        InodeFactory.deleteInode(e);
    }

    public static void deleteRecurringEvents(Recurance r) {
        List l = InodeFactory.getParentsOfClass(r, Event.class);
        Iterator i = l.iterator();
        while (i.hasNext()) {
            Event event = (Event) i.next();
            InodeFactory.deleteInode(event);
        }
        InodeFactory.deleteInode(r);
    }

    public static void deleteRecurringEvents(String r) {
        deleteRecurringEvents((Recurance) InodeFactory.getInode(r, Recurance.class));
    }
}
