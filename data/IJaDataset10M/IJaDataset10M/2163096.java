package org.skippyjon.flickoblog.presentation.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.skippyjon.flickoblog.util.ImplementationsFinder;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.DriverManager;

/**
 * ImplementationsLister
 *
 * @author Alexandre Normand
 *         Date: 23-Mar-2008
 * @version 1.0
 * @TODO Description
 * <p/>
 * <p>Copyright: Copyright (c) 2008 Alexandre Normand
 * http://flickr.com/people/alex_normand/ </p>
 * <p/>
 * <p>
 * The contents of this file are used with permission, subject to
 * the Mozilla Public License Version 1.1 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 * </p>
 * <p>
 * Software distributed under the License is distributed on an
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 * </p>
 */
public class ImplementationsLister {

    private List<String> mHibernateDialects;

    private List<String> mJdbcDrivers;

    public List getJdbcDrivers() throws Exception {
        if (mJdbcDrivers == null) {
            mJdbcDrivers = new LinkedList<String>();
            for (Class driverClass : ImplementationsFinder.findImplementations(Thread.currentThread().getContextClassLoader(), java.sql.Driver.class)) {
                mJdbcDrivers.add(driverClass.getName());
            }
        }
        return mJdbcDrivers;
    }

    public List getHibernateDialects() throws Exception {
        if (mHibernateDialects == null) {
            mHibernateDialects = new LinkedList<String>();
            for (Class dialectClass : ImplementationsFinder.findImplementations(Thread.currentThread().getContextClassLoader(), org.hibernate.dialect.Dialect.class)) {
                mHibernateDialects.add(dialectClass.getName());
            }
        }
        return mHibernateDialects;
    }
}
