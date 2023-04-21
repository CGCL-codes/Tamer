package org.jumpmind.pos.ui;

import java.util.ArrayList;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jumpmind.pos.IActivity;
import org.jumpmind.pos.IApplication;
import org.jumpmind.pos.IMenuManager;
import org.jumpmind.pos.IPOSContextInternal;
import org.jumpmind.pos.IScreen;
import org.jumpmind.pos.activity.ActivityStatus;
import org.jumpmind.pos.domain.security.Operator;
import org.jumpmind.pos.service.security.ISecurityService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MenuManager implements IMenuManager {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource
    private Set<MenuItem> menuItems;

    @Resource
    private IApplication application;

    @Resource
    private ISecurityService securityService;

    private MenuItemTreeNode rootMenuLocation;

    @Resource
    private IPOSContextInternal context;

    @PostConstruct
    public void init() {
        rootMenuLocation = new MenuItemTreeNode(menuItems);
        context.setCurrentMenuLocation(rootMenuLocation);
    }

    protected boolean shouldEscape(MenuItemTreeNode menu) {
        return menu != null && menu.getMenuItem().isEscape();
    }

    @Async
    public void select(MenuItemTreeNode menu) {
        if (shouldEscape(menu)) {
            menu = menu.getParentNode();
            if (menu != null) {
                menu = menu.getParentNode();
            }
        }
        if (menu == null) {
            menu = rootMenuLocation;
        }
        IActivity newActivity = context.getActivities().get(menu.getMenuItem().getActivityToExecute());
        IActivity oldActivity = context.getCurrentActivity();
        MenuItemTreeNode oldMenu = context.getCurrentMenuLocation();
        MenuItemTreeNode parentOfNewMenu = menu.getParentNode();
        MenuItemTreeNode parentOfOldMenu = oldMenu != null ? oldMenu.getParentNode() : null;
        boolean isChild = parentOfNewMenu != null && parentOfNewMenu.equals(oldMenu);
        boolean isParent = parentOfOldMenu != null && parentOfOldMenu.equals(menu);
        if (newActivity != null) {
            boolean abort = false;
            if (oldActivity != null && !oldActivity.equals(newActivity)) {
                if (isChild) {
                    oldActivity.pause(newActivity);
                } else {
                    abort = oldActivity.done(newActivity) == ActivityStatus.ABORT;
                }
            }
            if (!abort) {
                context.setCurrentMenuLocation(menu);
                context.setCurrentActivity(newActivity);
                ActivityStatus status = null;
                if (isParent || (oldActivity != null && oldActivity.equals(newActivity))) {
                    status = newActivity.reenter(oldActivity);
                } else {
                    status = newActivity.enter(oldActivity);
                }
                if (status == ActivityStatus.DONE || status == ActivityStatus.ROOT) {
                    if (!isChild || status == ActivityStatus.ROOT) {
                        oldMenu = rootMenuLocation;
                        oldActivity = context.getActivities().get(rootMenuLocation.getMenuItem().getActivityToExecute());
                    }
                    newActivity.done(oldActivity);
                    context.setCurrentMenuLocation(oldMenu);
                    context.setCurrentActivity(oldActivity);
                    if (oldActivity != null) {
                        oldActivity.reenter(newActivity);
                    }
                } else {
                    logger.info("Idle for " + menu.getMenuItem().getId());
                }
            } else {
                logger.info("Aborting " + menu.getMenuItem().getId());
            }
        }
        refreshScreens();
    }

    protected void refreshScreens() {
        ArrayList<IScreen> screenList = new ArrayList<IScreen>();
        if (!context.isLoggedIn()) {
            screenList.add(context.getScreens().get(Screens.LOGIN_SCREEN));
        }
        if (context.getCurrentMenuLocation().getChildrenNodes(MenuItemType.MENU, context).size() > 0) {
            screenList.add(context.getScreens().get(Screens.MENU_SCREEN));
        }
        IScreen screen = context.getScreens().get(context.getCurrentMenuLocation().getMenuItem().getScreenToShow());
        if (screen != null) {
            screenList.add(screen);
            context.setCurrentScreen(screen);
        }
        application.displayScreen(screenList.toArray(new IScreen[screenList.size()]));
    }

    public boolean login(String userName, byte[] password) {
        Operator operator = securityService.findOperator(userName);
        boolean validated = operator != null && operator.getHashedPassword() != null && operator.getHashedPassword().equals(new String(password));
        if (validated) {
            context.setOperator(operator);
            select(context.getCurrentMenuLocation());
        }
        return validated;
    }

    @Override
    public void refresh() {
        select(context.getCurrentMenuLocation());
    }
}
