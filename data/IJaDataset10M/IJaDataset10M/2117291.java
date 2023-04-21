package de.tudresden.inf.rn.mobilis.groups.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.tudresden.inf.rn.mobilis.groups.ConstMGroups;
import de.tudresden.inf.rn.mobilis.groups.Parceller;
import de.tudresden.inf.rn.mobilis.groups.R;
import de.tudresden.inf.rn.mobilis.groups.XMPPManager;
import de.tudresden.inf.rn.mobilis.mxa.ConstMXA;
import de.tudresden.inf.rn.mobilis.mxa.IXMPPService;
import de.tudresden.inf.rn.mobilis.mxa.MXAController;
import de.tudresden.inf.rn.mobilis.mxa.ConstMXA.RosterItems;
import de.tudresden.inf.rn.mobilis.mxa.callbacks.IXMPPIQCallback;
import de.tudresden.inf.rn.mobilis.mxa.parcelable.XMPPIQ;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.groups.GroupDeleteBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.groups.GroupInfoBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.groups.GroupInviteBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.groups.GroupLeaveBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.groups.GroupMemberInfoBean;

public class GroupsActivity extends ListActivity {

    /** The TAG for the Log. */
    private static final String TAG = "GroupsActivity";

    IXMPPService xmppService;

    private GroupsActivity groupsActivity;

    private GroupMemberInfoBean groupMemberInfoBean = null;

    private GroupInfoBean groupInfoBean = null;

    private String textToShowInToast = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupsActivity = this;
        xmppService = MXAController.get().getXMPPService();
        sendGroupMemberInfoBean();
        this.setTitle(R.string.app_name_groupsactivity);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        TextView headerTextView = new TextView(this);
        headerTextView.setText(Html.fromHtml("<b>My Groups:</b>"));
        lv.addHeaderView(headerTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_back_and_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_backtomap:
                this.finish();
                return true;
            case R.id.menu_refresh:
                sendGroupMemberInfoBean();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstMGroups.REQUEST_CODE_UPDATE) {
            if (resultCode == RESULT_OK) {
                sendGroupMemberInfoBean();
            } else if (resultCode == RESULT_CANCELED) {
                this.finish();
            }
        }
    }

    private void sendGroupMemberInfoBean() {
        String jid = null;
        try {
            jid = xmppService.getUsername();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        if (jid != null) {
            xmppService = MXAController.get().getXMPPService();
            if (xmppService != null) {
                try {
                    xmppService.registerIQCallback(groupMemberInfoCallback, GroupMemberInfoBean.CHILD_ELEMENT, GroupMemberInfoBean.NAMESPACE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "onCreate() --> xmppService is still null");
            }
            XMPPManager.getInstance().sendGroupMemberInfoBeanGet(jid);
        } else {
            Log.e(TAG, "Could not get Username. You are probably not connected to the XMPP server.");
        }
    }

    private void onGroupInfoBeanArrival() {
        if (groupInfoBean != null) {
            Intent i = new Intent(getApplicationContext(), GroupCreateActivity.class);
            i.putExtra("group_groupId", groupInfoBean.groupId);
            i.putExtra("group_name", groupInfoBean.name);
            i.putExtra("group_description", groupInfoBean.description);
            i.putExtra("group_address", groupInfoBean.address);
            if (groupInfoBean.latitude_e6 > Integer.MIN_VALUE) i.putExtra("group_latitude", groupInfoBean.latitude_e6);
            if (groupInfoBean.longitude_e6 > Integer.MIN_VALUE) i.putExtra("group_longitude", groupInfoBean.longitude_e6);
            if (groupInfoBean.visibilityRadius > Integer.MIN_VALUE) i.putExtra("group_visibilityradius", groupInfoBean.visibilityRadius);
            if (groupInfoBean.joinRadius > Integer.MIN_VALUE) i.putExtra("group_joinradius", groupInfoBean.joinRadius);
            if (groupInfoBean.startTime > Long.MIN_VALUE) i.putExtra("group_starttime", groupInfoBean.startTime);
            if (groupInfoBean.endTime > Long.MIN_VALUE) i.putExtra("group_endtime", groupInfoBean.endTime);
            if (groupInfoBean.joinStartTime > Long.MIN_VALUE) i.putExtra("group_joinstarttime", groupInfoBean.joinStartTime);
            if (groupInfoBean.joinEndTime > Long.MIN_VALUE) i.putExtra("group_joinendtime", groupInfoBean.joinEndTime);
            i.putExtra("group_privacy", groupInfoBean.privacy);
            i.putExtra("group_link", groupInfoBean.link);
            startActivityForResult(i, ConstMGroups.REQUEST_CODE_UPDATE);
        }
    }

    private void onGroupMemberInfoBeanArrival() {
        Log.i(TAG, "onGroupMemberInfoBeanArrival() --> ");
        final List<String> groupIdList = new ArrayList<String>();
        final List<String> groupNamesList = new ArrayList<String>();
        if (groupMemberInfoBean.groups != null) for (String groupId : groupMemberInfoBean.groups.keySet()) {
            groupIdList.add(groupId);
            groupNamesList.add(groupMemberInfoBean.groups.get(groupId));
        }
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groupNamesList));
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position > 0) {
                    AlertDialog.Builder builder;
                    AlertDialog alertDialog;
                    final Intent i = new Intent(getApplicationContext(), GroupInfoActivity.class);
                    i.putExtra("group_id", groupIdList.get(position - 1));
                    final CharSequence[] items = { "View Details", "Invite a friend", "Group chat", "Leave group", "Update group", "Delete group" };
                    builder = new AlertDialog.Builder(GroupsActivity.this);
                    builder.setTitle(groupNamesList.get(position - 1)).setCancelable(true).setIcon(R.drawable.group_marker_24).setItems(items, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int item) {
                            switch(item) {
                                case 0:
                                    startActivity(i);
                                    groupsActivity.finish();
                                    break;
                                case 1:
                                    dialog.cancel();
                                    onInviteClicked(groupIdList.get(position - 1));
                                    break;
                                case 2:
                                    dialog.cancel();
                                    Intent i = new Intent(getApplicationContext(), MUCActivity.class);
                                    i.putExtra("title", groupNamesList.get(position - 1));
                                    i.putExtra("group-id", groupIdList.get(position - 1));
                                    startActivityForResult(i, ConstMGroups.REQUEST_CODE_MUC);
                                    break;
                                case 3:
                                    dialog.cancel();
                                    onLeaveGroupClicked(groupIdList.get(position - 1));
                                    break;
                                case 4:
                                    dialog.cancel();
                                    onUpdateGroupClicked(groupIdList.get(position - 1));
                                    break;
                                case 5:
                                    dialog.cancel();
                                    onDeleteGroupClicked(groupIdList.get(position - 1));
                                    break;
                            }
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void onDeleteGroupClicked(String groupId) {
        try {
            xmppService.registerIQCallback(groupDeleteCallback, GroupDeleteBean.CHILD_ELEMENT, GroupDeleteBean.NAMESPACE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        XMPPManager.getInstance().sendGroupDeleteBeanSet(groupId);
    }

    private void onUpdateGroupClicked(String groupId) {
        try {
            xmppService.registerIQCallback(groupInfoCallback, GroupInfoBean.CHILD_ELEMENT, GroupInfoBean.NAMESPACE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        XMPPManager.getInstance().sendGroupInfoBeanGet(groupId);
    }

    private void onLeaveGroupClicked(String groupId) {
        try {
            xmppService.registerIQCallback(groupLeaveCallback, GroupLeaveBean.CHILD_ELEMENT, GroupLeaveBean.NAMESPACE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        XMPPManager.getInstance().sendGroupLeaveBeanSet(groupId);
    }

    private Set<String> invitees = new HashSet<String>();

    private void onInviteClicked(final String groupId) {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        Cursor mFriendsCursor = getContentResolver().query(RosterItems.CONTENT_URI, null, null, null, RosterItems.DEFAULT_SORT_ORDER);
        startManagingCursor(mFriendsCursor);
        mFriendsCursor.moveToFirst();
        CharSequence[] nameArray = new CharSequence[mFriendsCursor.getCount()];
        final String[] jidArray = new String[mFriendsCursor.getCount()];
        int colJid = mFriendsCursor.getColumnIndex(RosterItems.XMPP_ID);
        int colName = mFriendsCursor.getColumnIndex(RosterItems.NAME);
        int i = 0;
        while (!mFriendsCursor.isAfterLast()) {
            jidArray[i] = mFriendsCursor.getString(colJid);
            nameArray[i] = mFriendsCursor.getString(colName);
            i++;
            mFriendsCursor.moveToNext();
        }
        builder = new AlertDialog.Builder(GroupsActivity.this);
        builder.setTitle("Choose the friends to invite.").setCancelable(true).setMultiChoiceItems(nameArray, null, new OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) invitees.add(jidArray[which]); else invitees.remove(jidArray[which]);
            }
        }).setPositiveButton("Invite", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                try {
                    xmppService.registerIQCallback(groupInviteCallback, GroupInviteBean.CHILD_ELEMENT, GroupInviteBean.NAMESPACE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                XMPPManager.getInstance().sendGroupInviteBeanSet(invitees, groupId);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    /** Callback which is informed about arrival of a GroupDeleteIQ. */
    private IXMPPIQCallback groupDeleteCallback = new IXMPPIQCallback.Stub() {

        @Override
        public void processIQ(XMPPIQ iq) throws RemoteException {
            Log.i(TAG, "groupDeleteCallback --> processIQ --> iqPacketId:" + iq.packetID);
            XMPPBean b = Parceller.getInstance().convertXMPPIQToBean(iq);
            Log.i(TAG, "groupDeleteCallback --> processIQ --> beanPacketId:" + b.getId());
            if (b instanceof GroupDeleteBean) {
                xmppService.unregisterIQCallback(groupDeleteCallback, GroupDeleteBean.CHILD_ELEMENT, GroupDeleteBean.NAMESPACE);
                GroupDeleteBean bb = (GroupDeleteBean) b;
                if (b.getType() == XMPPBean.TYPE_RESULT) {
                    textToShowInToast = "Successfully deleted the group.";
                    showToastHandler.sendEmptyMessage(0);
                    onGroupDeleteBeanArrivalHandler.sendEmptyMessage(0);
                } else if (b.getType() == XMPPBean.TYPE_ERROR) {
                    Log.e(TAG, "GroupDeleteBean type=ERROR arrived. IQ-Payload:" + iq.payload);
                    Log.e(TAG, "GroupDeleteBean type=ERROR arrived. Error type: " + bb.errorType);
                    Log.e(TAG, "GroupDeleteBean type=ERROR arrived. Error condition: " + bb.errorCondition);
                    Log.e(TAG, "GroupDeleteBean type=ERROR arrived. Error text: " + bb.errorText);
                    textToShowInToast = "ERROR: " + bb.errorText;
                    showToastHandler.sendEmptyMessage(0);
                }
            }
        }
    };

    private Handler onGroupDeleteBeanArrivalHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            sendGroupMemberInfoBean();
        }
    };

    /** Callback which is informed about arrival of a GroupInfoIQ. */
    private IXMPPIQCallback groupInfoCallback = new IXMPPIQCallback.Stub() {

        @Override
        public void processIQ(XMPPIQ iq) throws RemoteException {
            Log.i(TAG, "groupInfoCallback --> processIQ --> iqPacketId:" + iq.packetID);
            XMPPBean b = Parceller.getInstance().convertXMPPIQToBean(iq);
            Log.i(TAG, "groupInfoCallback --> processIQ --> beanPacketId:" + b.getId());
            if (b instanceof GroupInfoBean) {
                xmppService.unregisterIQCallback(groupInfoCallback, GroupInfoBean.CHILD_ELEMENT, GroupInfoBean.NAMESPACE);
                GroupInfoBean bb = (GroupInfoBean) b;
                if (b.getType() == XMPPBean.TYPE_RESULT) {
                    groupInfoBean = bb;
                    onGroupInfoBeanArrivalHandler.sendEmptyMessage(0);
                } else if (b.getType() == XMPPBean.TYPE_ERROR) {
                    Log.e(TAG, "GrouInfoBean type=ERROR arrived. IQ-Payload:" + iq.payload);
                    Log.e(TAG, "GrouInfoBean type=ERROR arrived. Error type: " + bb.errorType);
                    Log.e(TAG, "GrouInfoBean type=ERROR arrived. Error condition: " + bb.errorCondition);
                    Log.e(TAG, "GrouInfoBean type=ERROR arrived. Error text: " + bb.errorText);
                    textToShowInToast = "ERROR: " + bb.errorText;
                    showToastHandler.sendEmptyMessage(0);
                    groupsActivity.finish();
                }
            }
        }
    };

    private Handler onGroupInfoBeanArrivalHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            onGroupInfoBeanArrival();
        }
    };

    /** Callback which is informed about arrival of a GroupMemberInfoIQ. */
    private IXMPPIQCallback groupMemberInfoCallback = new IXMPPIQCallback.Stub() {

        @Override
        public void processIQ(XMPPIQ iq) throws RemoteException {
            Log.i(TAG, "groupMemberInfoCallback --> processIQ --> iqPacketId:" + iq.packetID);
            XMPPBean b = Parceller.getInstance().convertXMPPIQToBean(iq);
            Log.i(TAG, "groupMemberInfoCallback --> processIQ --> beanPacketId:" + b.getId());
            if (b instanceof GroupMemberInfoBean) {
                xmppService.unregisterIQCallback(groupMemberInfoCallback, GroupMemberInfoBean.CHILD_ELEMENT, GroupMemberInfoBean.NAMESPACE);
                GroupMemberInfoBean bb = (GroupMemberInfoBean) b;
                if (b.getType() == XMPPBean.TYPE_RESULT) {
                    groupMemberInfoBean = bb;
                    onGroupMemberInfoBeanArrivalHandler.sendEmptyMessage(0);
                } else if (b.getType() == XMPPBean.TYPE_ERROR) {
                    Log.e(TAG, "GroupMemberInfoBean type=ERROR arrived. IQ-Payload:" + iq.payload);
                    Log.e(TAG, "GroupMemberInfoBean type=ERROR arrived. Error type: " + bb.errorType);
                    Log.e(TAG, "GroupMemberInfoBean type=ERROR arrived. Error condition: " + bb.errorCondition);
                    Log.e(TAG, "GroupMemberInfoBean type=ERROR arrived. Error text: " + bb.errorText);
                    textToShowInToast = "ERROR: " + bb.errorText;
                    showToastHandler.sendEmptyMessage(0);
                    groupsActivity.finish();
                }
            }
        }
    };

    private Handler onGroupMemberInfoBeanArrivalHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            onGroupMemberInfoBeanArrival();
        }
    };

    /** Callback which is informed about arrival of a GroupInviteIQ. */
    private IXMPPIQCallback groupInviteCallback = new IXMPPIQCallback.Stub() {

        @Override
        public void processIQ(XMPPIQ iq) throws RemoteException {
            Log.i(TAG, "groupInviteCallback --> processIQ --> iqPacketId:" + iq.packetID);
            XMPPBean b = Parceller.getInstance().convertXMPPIQToBean(iq);
            Log.i(TAG, "groupInviteCallback --> processIQ --> beanPacketId:" + b.getId());
            if (b instanceof GroupInviteBean) {
                xmppService.unregisterIQCallback(groupInviteCallback, GroupInviteBean.CHILD_ELEMENT, GroupInviteBean.NAMESPACE);
                GroupInviteBean bb = (GroupInviteBean) b;
                if (b.getType() == XMPPBean.TYPE_RESULT) {
                    textToShowInToast = "Invitations received";
                    showToastHandler.sendEmptyMessage(0);
                } else if (b.getType() == XMPPBean.TYPE_ERROR) {
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. IQ-Payload:" + iq.payload);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error type: " + bb.errorType);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error condition: " + bb.errorCondition);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error text: " + bb.errorText);
                    textToShowInToast = "ERROR: " + bb.errorText;
                    showToastHandler.sendEmptyMessage(0);
                }
            }
        }
    };

    /** Callback which is informed about arrival of a GroupInviteIQ. */
    private IXMPPIQCallback groupLeaveCallback = new IXMPPIQCallback.Stub() {

        @Override
        public void processIQ(XMPPIQ iq) throws RemoteException {
            Log.i(TAG, "groupLeaveCallback --> processIQ --> iqPacketId:" + iq.packetID);
            XMPPBean b = Parceller.getInstance().convertXMPPIQToBean(iq);
            Log.i(TAG, "groupLeaveCallback --> processIQ --> beanPacketId:" + b.getId());
            if (b instanceof GroupLeaveBean) {
                xmppService.unregisterIQCallback(groupLeaveCallback, GroupLeaveBean.CHILD_ELEMENT, GroupLeaveBean.NAMESPACE);
                GroupLeaveBean bb = (GroupLeaveBean) b;
                if (b.getType() == XMPPBean.TYPE_RESULT) {
                    textToShowInToast = "You successfully left the group";
                    showToastHandler.sendEmptyMessage(0);
                    onGroupLeaveBeanArrivalHandler.sendEmptyMessage(0);
                } else if (b.getType() == XMPPBean.TYPE_ERROR) {
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. IQ-Payload:" + iq.payload);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error type: " + bb.errorType);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error condition: " + bb.errorCondition);
                    Log.e(TAG, "GroupInviteBean type=ERROR arrived. Error text: " + bb.errorText);
                    textToShowInToast = "ERROR: " + bb.errorText;
                    showToastHandler.sendEmptyMessage(0);
                }
            }
        }
    };

    private Handler onGroupLeaveBeanArrivalHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            sendGroupMemberInfoBean();
        }
    };

    private Handler showToastHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            makeToast(textToShowInToast);
        }
    };

    /** Shows a short Toast message on the map */
    public void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
