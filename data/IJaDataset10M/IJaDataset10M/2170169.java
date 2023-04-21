package org.springframework.richclient.command.support;

import org.springframework.richclient.application.ApplicationServices;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.PageDescriptor;
import org.springframework.richclient.application.PageDescriptorRegistry;
import org.springframework.richclient.application.config.ApplicationWindowAware;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.util.Assert;

/**
 * A menu containing a collection of sub-menu items that each display a given
 * page.
 *
 * @author Keith Donald
 * @author Rogan Dawes
 */
public class ShowPageMenu extends CommandGroup implements ApplicationWindowAware {

    /** The identifier of this command. */
    public static final String ID = "showPageMenu";

    private ApplicationWindow window;

    /**
	 * Creates a new {@code ShowPageMenu} with an id of {@value #ID}.
	 */
    public ShowPageMenu() {
        super(ID);
    }

    /**
	 * {@inheritDoc}
	 */
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
    }

    /**
	 * Called after dependencies have been set, populates this menu with action
	 * command objects that will each show a given page when executed. The
	 * collection of 'show page' commands will be determined by querying the
	 * {@link PageDescriptorRegistry} retrieved from {@link ApplicationServices}.
	 */
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(window, "Application window cannot be null.");
        populate();
    }

    private void populate() {
        PageDescriptorRegistry pageDescriptorRegistry = (PageDescriptorRegistry) ApplicationServicesLocator.services().getService(PageDescriptorRegistry.class);
        PageDescriptor[] pages = pageDescriptorRegistry.getPageDescriptors();
        for (int i = 0; i < pages.length; i++) {
            PageDescriptor page = pages[i];
            addInternal(page.createShowPageCommand(window));
        }
    }
}
