package kr.pe.silent.etweeter.views;

import com.swtdesigner.ResourceManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import kr.pe.silent.etweeter.ETweeter;

/**
 * @author Sanghoon Lee
 * $Id$
 */
public class ETweeterView extends ViewPart {

    /**
	 * The ID of the view as specified by the extension.
	 */
    public static final String ID = "kr.pe.silent.etweeter.views.ETweeterView";

    private Composite parent;

    private Combo cmbUsername;

    private Action action1;

    private Action action2;

    private Action doubleClickAction;

    private Twitter twitter;

    private ResponseList<Status> timeline;

    private Composite content;

    private Composite body;

    private ScrolledComposite scrolledComposite;

    /**
	 * The constructor.
	 */
    public ETweeterView() {
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    public void createPartControl(Composite parent) {
        this.parent = parent;
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        cmbUsername = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
        cmbUsername.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String username = cmbUsername.getText();
                long userId = ETweeter.getUserId(username);
                initTwitter(userId);
            }
        });
        cmbUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setExpandHorizontal(true);
        content = new Composite(scrolledComposite, SWT.NONE);
        content.setBackground(ResourceManager.getColor(SWT.COLOR_GRAY));
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 1;
        gridLayout.horizontalSpacing = 0;
        content.setLayout(gridLayout);
        scrolledComposite.setContent(content);
        scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                scrolledCompositeResized();
            }
        });
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        String[] usernames = ETweeter.getUsernames();
        cmbUsername.setItems(usernames);
        if (usernames.length == 0) {
            initTwitter(0);
        } else {
            cmbUsername.select(0);
            String username = cmbUsername.getText();
            long userId = ETweeter.getUserId(username);
            initTwitter(userId);
        }
    }

    private void initTwitter(long userId) {
        twitter = ETweeter.getTwitter(userId);
        if (twitter == null) {
            final IntroPanel introPanel = new IntroPanel(parent);
            introPanel.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    String pin = introPanel.getPin();
                    twitter = ETweeter.getTwitter(pin);
                    if (twitter == null) {
                        return;
                    }
                    introPanel.dispose();
                    updateTimeline();
                    try {
                        cmbUsername.add(twitter.getScreenName(), 0);
                        cmbUsername.select(0);
                    } catch (IllegalStateException e1) {
                        e1.printStackTrace();
                    } catch (TwitterException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            introPanel.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            ETweeter.openOAuthBrowser();
        } else {
            updateTimeline();
        }
    }

    private void updateTimeline() {
        new Thread() {

            @Override
            public void run() {
                try {
                    if (twitter != null) timeline = twitter.getFriendsTimeline();
                    for (int i = timeline.size() - 1; i >= 0; i--) {
                        final Status status = timeline.get(i);
                        getSite().getShell().getDisplay().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                if (content.isDisposed()) return;
                                TweetItem item = new TweetItem(content, SWT.NONE);
                                item.setStatus(status);
                                if (content.getChildren().length != 0) {
                                    item.moveAbove(content.getChildren()[0]);
                                }
                                item.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                                content.layout();
                                scrolledCompositeResized();
                            }
                        });
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void scrolledCompositeResized() {
        Rectangle r = scrolledComposite.getClientArea();
        scrolledComposite.setMinSize(content.computeSize(r.width, SWT.DEFAULT));
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                ETweeterView.this.fillContextMenu(manager);
            }
        });
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        manager.add(action2);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(action2);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(action2);
    }

    private void makeActions() {
        action1 = new Action() {

            public void run() {
                showMessage("Action 1 executed");
            }
        };
        action1.setText("Action 1");
        action1.setToolTipText("Action 1 tooltip");
        action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
        action2 = new Action() {

            public void run() {
                showMessage("Action 2 executed");
            }
        };
        action2.setText("Action 2");
        action2.setToolTipText("Action 2 tooltip");
        action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    }

    private void hookDoubleClickAction() {
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(content.getShell(), "eTweeter", message);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        cmbUsername.setFocus();
    }
}
