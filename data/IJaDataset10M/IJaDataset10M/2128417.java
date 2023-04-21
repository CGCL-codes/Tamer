package org.springframework.richclient.application.mdi;

import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.PageDescriptor;
import org.springframework.richclient.application.support.DefaultApplicationWindow;

/**
 * @author Peter De Bruycker
 */
public class DesktopApplicationWindow extends DefaultApplicationWindow {

    protected ApplicationPage createPage(PageDescriptor descriptor) {
        return new DesktopApplicationPage(this, descriptor);
    }
}
