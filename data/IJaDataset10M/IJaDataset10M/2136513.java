package de.frewert.ant.freshmeat.types;

import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Valid values for the "focus" attribute used by the nested &lt;publish&gt;
 * element 
 * (see {@link de.frewert.ant.freshmeat.types.Project#setFocus(FocusAttribute)}).
 * <p>
 * See Appendix A in <a href="http://freshmeat.net/faq/view/49/">
 * http://freshmeat.net/faq/view/49/</a>
 * </p>
 * <pre>
 * Copyright (C) 2004, 2005 Carsten Frewert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 * @author <a href="mailto:frewert@users.sourceforge.net">Carsten Frewert</a>
 * @version $Revision: 1.3 $
 */
public class FocusAttribute extends EnumeratedAttribute {

    public static final String NOT_AVAILABLE = "N/A";

    public static final String INITIAL_FRESHMEAT_ANNOUNCEMENT = "initialAnnouncement";

    public static final String DOCUMENTATION = "documentation";

    public static final String CODE_CLEANUP = "cleanup";

    public static final String MINOR_FEATURE_ENHANCEMENTS = "minorEnhancements";

    public static final String MAJOR_FEATURE_ENHANCEMENTS = "majorEnhancements";

    public static final String MINOR_BUGFIXES = "minorBugfixes";

    public static final String MAJOR_BUGFIXES = "majorBugfixes";

    public static final String MINOR_SECURITY_FIXES = "minorSecurityFixes";

    public static final String MAJOR_SECURITY_FIXES = "majorSecurityFixes";

    /**
     * The order of the entries is significant!
     * See {@link EnumeratedAttribute#getIndex()}.
     */
    private static final String[] validValues = { FocusAttribute.NOT_AVAILABLE, FocusAttribute.INITIAL_FRESHMEAT_ANNOUNCEMENT, FocusAttribute.DOCUMENTATION, FocusAttribute.CODE_CLEANUP, FocusAttribute.MINOR_FEATURE_ENHANCEMENTS, FocusAttribute.MAJOR_FEATURE_ENHANCEMENTS, FocusAttribute.MINOR_BUGFIXES, FocusAttribute.MAJOR_BUGFIXES, FocusAttribute.MINOR_SECURITY_FIXES, FocusAttribute.MAJOR_SECURITY_FIXES };

    public String[] getValues() {
        String[] copy = new String[FocusAttribute.validValues.length];
        System.arraycopy(FocusAttribute.validValues, 0, copy, 0, copy.length);
        return copy;
    }
}
