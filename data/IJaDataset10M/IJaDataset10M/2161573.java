package org.skippyjon.flickoblog.presentation.util;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TestImplementationsLister
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
public class TestImplementationsLister extends TestCase {

    private static final Log LOGGER = LogFactory.getLog(TestImplementationsLister.class);

    public void testJdbcDrivers() throws Exception {
        ImplementationsLister implementationsLister = new ImplementationsLister();
        LOGGER.debug(implementationsLister.getJdbcDrivers());
        assertNotNull(implementationsLister.getJdbcDrivers());
    }
}
