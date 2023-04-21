package ua.snuk182.asia.view.cl.twocolumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ua.snuk182.asia.EntryPoint;
import ua.snuk182.asia.R;
import ua.snuk182.asia.core.dataentity.AccountView;
import ua.snuk182.asia.core.dataentity.Buddy;
import ua.snuk182.asia.core.dataentity.BuddyGroup;
import ua.snuk182.asia.core.dataentity.TextMessage;
import ua.snuk182.asia.services.ServiceConstants;
import ua.snuk182.asia.services.ServiceUtils;
import ua.snuk182.asia.services.api.AccountService;
import ua.snuk182.asia.view.ViewUtils;
import ua.snuk182.asia.view.cl.ContactList;
import ua.snuk182.asia.view.cl.IContactListDrawer;
import ua.snuk182.asia.view.cl.list.ContactListListItem;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DoubleContactListListDrawer extends ScrollView implements IContactListDrawer {

    private LinearLayout contactList;

    private List<DoubleContactListGroupItem> groups = new ArrayList<DoubleContactListGroupItem>();

    private DoubleContactListGroupItem unreadGroup;

    private DoubleContactListGroupItem offlineGroup;

    private DoubleContactListGroupItem notInListGroup;

    private DoubleContactListGroupItem onlineGroup;

    private DoubleContactListGroupItem chatsGroup;

    protected ContactList parent;

    boolean showGroups = false;

    boolean showOffline = false;

    private boolean showIcons = true;

    private boolean clInited = false;

    private int oldWidth = 0;

    private final List<ContactListListItem> tmpItems = new ArrayList<ContactListListItem>();

    private final Runnable updateViewRunnable = new Runnable() {

        @Override
        public void run() {
            DoubleContactListGroupItem.itemSize = DoubleContactListListDrawer.this.getWidth() / 2;
            String showGroupsStr = parent.account.options.getString(getResources().getString(R.string.key_show_groups));
            showGroups = showGroupsStr != null ? Boolean.parseBoolean(showGroupsStr) : false;
            String showOfflineStr = parent.account.options.getString(getResources().getString(R.string.key_show_offline));
            showOffline = showOfflineStr != null ? Boolean.parseBoolean(showOfflineStr) : true;
            String showIconsStr = parent.account.options.getString(getResources().getString(R.string.key_show_icons));
            showIcons = showIconsStr != null ? Boolean.parseBoolean(showIconsStr) : true;
            String itemSizeStr = getEntryPoint().getApplicationOptions().getString(getResources().getString(R.string.key_cl_item_size));
            int size;
            if (itemSizeStr == null || itemSizeStr.equals(getResources().getString(R.string.value_size_medium))) {
                size = 48;
            } else if (itemSizeStr.equals(getResources().getString(R.string.value_size_big))) {
                size = 64;
            } else if (itemSizeStr.equals(getResources().getString(R.string.value_size_small))) {
                size = 32;
            } else {
                size = 24;
            }
            ContactListListItem.resize(size);
            for (DoubleContactListGroupItem group : groups) {
                tmpItems.addAll(group.getBuddyList());
            }
            for (ContactListListItem item : tmpItems) {
                item.removeFromParent();
            }
            groups.clear();
            unreadGroup.getBuddyList().clear();
            offlineGroup.getBuddyList().clear();
            onlineGroup.getBuddyList().clear();
            notInListGroup.getBuddyList().clear();
            chatsGroup.getBuddyList().clear();
            groups.add(unreadGroup);
            if (showGroups && parent.account.getBuddyGroupList().size() > 0) {
                Collections.sort(parent.account.getBuddyGroupList());
                for (final BuddyGroup group : parent.account.getBuddyGroupList()) {
                    DoubleContactListGroupItem clgroup = new DoubleContactListGroupItem((EntryPoint) getContext(), null, contactList, group.id + "", group.name);
                    clgroup.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            try {
                                AccountView myacc = getEntryPoint().runtimeService.getAccountView(parent.account.serviceId);
                                if (myacc.getConnectionState() == AccountService.STATE_CONNECTED) {
                                    ViewUtils.groupMenu(getEntryPoint(), parent.account, group);
                                }
                            } catch (NullPointerException npe) {
                                ServiceUtils.log(npe);
                            } catch (RemoteException e) {
                                getEntryPoint().onRemoteCallFailed(e);
                            }
                            return false;
                        }
                    });
                    clgroup.setGroupId(group.id);
                    clgroup.serviceId = group.serviceId;
                    clgroup.setCollapsed(group.isCollapsed);
                    groups.add(clgroup);
                }
            } else {
                groups.add(onlineGroup);
            }
            for (Buddy buddy : parent.account.getBuddyList()) {
                ContactListListItem item = getItem(buddy, showIcons);
                item.color();
                if (showGroups && parent.account.getBuddyGroupList().size() > 0) {
                    if (buddy.unread > 0) {
                        unreadGroup.getBuddyList().add(item);
                    } else if (buddy.groupId == AccountService.NOT_IN_LIST_GROUP_ID) {
                        notInListGroup.getBuddyList().add(item);
                    } else if (buddy.visibility == Buddy.VIS_GROUPCHAT) {
                        chatsGroup.getBuddyList().add(item);
                    } else if (buddy.status == Buddy.ST_OFFLINE && !showOffline) {
                        continue;
                    } else {
                        for (int i = 1; i < groups.size(); i++) {
                            DoubleContactListGroupItem group = groups.get(i);
                            if (group.getGroupId() == buddy.groupId) {
                                group.getBuddyList().add(item);
                                break;
                            }
                        }
                    }
                } else {
                    if (buddy.unread > 0) {
                        unreadGroup.getBuddyList().add(item);
                    } else if (buddy.groupId == AccountService.NOT_IN_LIST_GROUP_ID) {
                        notInListGroup.getBuddyList().add(item);
                    } else if (buddy.visibility == Buddy.VIS_GROUPCHAT) {
                        chatsGroup.getBuddyList().add(item);
                    } else if (buddy.status == Buddy.ST_OFFLINE) {
                        offlineGroup.getBuddyList().add(item);
                    } else {
                        onlineGroup.getBuddyList().add(item);
                    }
                }
            }
            if (unreadGroup.getBuddyList().size() > 0) {
                unreadGroup.setVisibility(VISIBLE);
            } else {
                unreadGroup.setVisibility(GONE);
            }
            if (notInListGroup.getBuddyList().size() > 0) {
                groups.add(notInListGroup);
            }
            if (chatsGroup.getBuddyList().size() > 0) {
                groups.add(chatsGroup);
            }
            if (!showGroups && showOffline) {
                groups.add(offlineGroup);
            }
            contactList.removeAllViews();
            for (DoubleContactListGroupItem group : groups) {
                group.resize(size);
                group.color();
                group.forceRefresh();
            }
            clInited = true;
            parent.setClReady(true);
            tmpItems.clear();
        }
    };

    public DoubleContactListListDrawer(EntryPoint entryPoint, AccountView account, ContactList parent) {
        super(entryPoint);
        this.parent = parent;
        LayoutInflater inflate = LayoutInflater.from(entryPoint);
        inflate.inflate(R.layout.contact_list_grid_drawer, this);
        contactList = (LinearLayout) findViewById(R.id.contactgrouplist);
        unreadGroup = new DoubleContactListGroupItem(entryPoint, null, contactList, ServiceConstants.VIEWGROUP_UNREAD, getResources().getString(R.string.label_unread_group));
        unreadGroup.setVisibility(View.GONE);
        offlineGroup = new DoubleContactListGroupItem(entryPoint, null, contactList, ServiceConstants.VIEWGROUP_OFFLINE, getResources().getString(R.string.label_offline_group));
        notInListGroup = new DoubleContactListGroupItem(entryPoint, null, contactList, ServiceConstants.VIEWGROUP_NOT_IN_LIST, getResources().getString(R.string.label_not_in_list_group));
        onlineGroup = new DoubleContactListGroupItem(entryPoint, null, contactList, ServiceConstants.VIEWGROUP_ONLINE, getResources().getString(R.string.label_online_group));
        chatsGroup = new DoubleContactListGroupItem(entryPoint, null, contactList, ServiceConstants.VIEWGROUP_CHATS, getResources().getString(R.string.label_chats_group));
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        layout.weight = 0.1f;
        setLayoutParams(layout);
        setFocusable(false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getWidth();
        super.onLayout(changed, left, top, right, bottom);
        if ((width - oldWidth) != 0) {
            getEntryPoint().threadMsgHandler.post(new Runnable() {

                @Override
                public void run() {
                    oldWidth = width;
                    if (clInited && DoubleContactListGroupItem.itemSize > 0) {
                        int newItemSize = width / 2;
                        DoubleContactListGroupItem.itemSize = newItemSize;
                        for (DoubleContactListGroupItem group : groups) {
                            group.setRefreshContents(false);
                            group.refresh(true);
                        }
                    } else {
                        updateView();
                    }
                }
            });
        }
    }

    @Override
    public synchronized void updateView() {
        if (this.getWidth() < 1) {
            return;
        }
        parent.setClReady(false);
        getEntryPoint().threadMsgHandler.post(updateViewRunnable);
    }

    @Override
    public void messageReceived(TextMessage message) {
        ContactListListItem buddy = null;
        for (int i = 0; i < groups.size(); i++) {
            DoubleContactListGroupItem groupItem = groups.get(i);
            ContactListListItem bu = groupItem.removeItem(message.from);
            if (bu != null) {
                buddy = bu;
                groupItem.setRefreshContents(true);
                break;
            }
        }
        if (buddy == null) {
            return;
        }
        unreadGroup.setVisibility(View.VISIBLE);
        unreadGroup.getBuddyList().add(buddy);
        unreadGroup.setRefreshContents(true);
        contactList.removeAllViews();
        for (DoubleContactListGroupItem item : groups) {
            item.refresh(false);
        }
    }

    @Override
    public synchronized void updateBuddyState(Buddy buddy) {
        ContactListListItem item = null;
        for (int i = 0; i < groups.size(); i++) {
            DoubleContactListGroupItem groupItem = groups.get(i);
            item = groupItem.removeItem(buddy.protocolUid);
            if (item != null) {
                groupItem.setRefreshContents(true);
                break;
            }
        }
        if (item == null) {
            if (!showOffline && buddy.status != Buddy.ST_OFFLINE) {
                item = getItem(buddy, showIcons);
            } else {
                ServiceUtils.log("no item found - " + buddy.protocolUid);
                return;
            }
        }
        item.populate(buddy);
        DoubleContactListGroupItem groupItem = null;
        String tag = null;
        if (buddy.unread < 1) {
            if (buddy.groupId == AccountService.NOT_IN_LIST_GROUP_ID) {
                tag = ServiceConstants.VIEWGROUP_NOT_IN_LIST;
            } else if (buddy.visibility == Buddy.VIS_GROUPCHAT) {
                tag = ServiceConstants.VIEWGROUP_CHATS;
            } else {
                if (buddy.status == Buddy.ST_OFFLINE && !showGroups && showOffline) {
                    tag = ServiceConstants.VIEWGROUP_OFFLINE;
                } else {
                    if (showGroups && parent.account.getBuddyGroupList().size() > 0) {
                        tag = buddy.groupId + "";
                    } else {
                        tag = ServiceConstants.VIEWGROUP_ONLINE;
                    }
                }
            }
        } else {
            tag = ServiceConstants.VIEWGROUP_UNREAD;
        }
        groupItem = (DoubleContactListGroupItem) findViewWithTag(tag);
        if (groupItem == null) {
            ServiceUtils.log("cannot find group in view " + buddy.groupId);
            return;
        }
        groupItem.getBuddyList().add(item);
        groupItem.setRefreshContents(true);
        if (unreadGroup.getBuddyList().size() < 1) {
            unreadGroup.setVisibility(View.GONE);
        } else {
            unreadGroup.setVisibility(View.VISIBLE);
        }
        for (DoubleContactListGroupItem groItem : groups) {
            groItem.refresh(false);
        }
    }

    @Override
    public String getType() {
        return getContext().getResources().getString(R.string.value_list_type_doublelist);
    }

    private ContactListListItem findExistingItem(String buddyUid) {
        for (ContactListListItem item : tmpItems) {
            if (item.getTag().equals(buddyUid)) {
                return item;
            }
        }
        return (ContactListListItem) this.findViewWithTag(buddyUid);
    }

    private ContactListListItem getItem(final Buddy buddy, boolean showIcons) {
        ContactListListItem item;
        if ((item = findExistingItem(buddy.protocolUid)) == null) {
            final ContactListListItem cli = new ContactListListItem(getEntryPoint(), buddy.protocolUid);
            cli.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    cli.setBackgroundColor(0xddff7f00);
                    getEntryPoint().threadMsgHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                getEntryPoint().getConversationTab(getEntryPoint().runtimeService.getBuddy(buddy.serviceId, buddy.protocolUid));
                                cli.setBackgroundColor(0x00ffffff);
                            } catch (NullPointerException npe) {
                                ServiceUtils.log(npe);
                            } catch (RemoteException e) {
                                getEntryPoint().onRemoteCallFailed(e);
                            }
                        }
                    });
                }
            });
            cli.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (parent.account.getConnectionState() == AccountService.STATE_CONNECTED) {
                        try {
                            ViewUtils.contactMenu(getEntryPoint(), parent.account, getEntryPoint().runtimeService.getBuddy(buddy.serviceId, buddy.protocolUid));
                        } catch (NullPointerException npe) {
                            ServiceUtils.log(npe);
                        } catch (RemoteException e) {
                            getEntryPoint().onRemoteCallFailed(e);
                        }
                    }
                    return false;
                }
            });
            item = cli;
        }
        item.setLayoutParams(new LinearLayout.LayoutParams(DoubleContactListGroupItem.itemSize, (int) (ContactListListItem.itemHeight * getEntryPoint().metrics.density), 1f));
        item.removeFromParent();
        item.populate(buddy, showIcons);
        item.requestIcon(buddy);
        return item;
    }

    public EntryPoint getEntryPoint() {
        return (EntryPoint) getContext();
    }

    @Override
    public void bitmap(String uid) {
        ContactListListItem item = findExistingItem(uid);
        if (item != null) {
            try {
                item.requestIcon(getEntryPoint().runtimeService.getBuddy(parent.account.serviceId, uid));
            } catch (NullPointerException npe) {
                ServiceUtils.log(npe);
            } catch (RemoteException e) {
                getEntryPoint().onRemoteCallFailed(e);
            }
        }
    }

    @Override
    public boolean hasUnreadMessages() {
        return unreadGroup.getVisibility() == View.VISIBLE;
    }

    @Override
    public void configChanged() {
    }
}
