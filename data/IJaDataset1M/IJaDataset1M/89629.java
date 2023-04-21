package org.igeek.atomqq.widget;

import org.igeek.atomqq.AtomQQApplication;
import org.igeek.atomqq.R;
import org.igeek.atomqq.activity.BaseActivity;
import org.igeek.atomqq.domain.Friend;
import org.igeek.atomqq.net.Response;
import org.igeek.atomqq.task.FaceTask;
import org.igeek.atomqq.task.FaceTask.FaceResponse;
import org.igeek.atomqq.task.FriendSingleLongNickTask;
import org.igeek.atomqq.task.FriendSingleLongNickTask.LNickResponse;
import org.igeek.atomqq.task.TaskResultListener;
import org.igeek.atomqq.util.Utils;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/** 
 * 好友
 * @author <a href="http://hangxin1940.cnblogs.com">hangxin1940@gmail.com</a>
 * @time 2012-2-5 下午06:43:55 
 */
public class FriendItemView extends LinearLayout implements TaskResultListener {

    private static final String FLAG_NAME = "atomqq";

    private static final int FLAG_LNICK = 0x0;

    private static final int FLAG_FACE = 0x1;

    private Friend friend;

    private static long currentUin = 0;

    private BaseActivity context;

    private FriendFaceBadgeView ffbvBadge;

    private ImageView ivFace;

    private ImageView ivFaceBackground;

    private AlwaysMarqueeTextView amtvMarkname;

    private AlwaysMarqueeTextView amtvNickName;

    private AlwaysMarqueeTextView amtvLNick;

    private boolean flash = false;

    private boolean changeColor = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    changeColor = false;
                    if (flash) {
                        amtvLNick.setVisibility(View.VISIBLE);
                        amtvMarkname.setVisibility(View.VISIBLE);
                        amtvNickName.setVisibility(View.VISIBLE);
                        flash = false;
                        handler.sendEmptyMessageDelayed(0, 300);
                    } else {
                        amtvLNick.setVisibility(View.INVISIBLE);
                        amtvMarkname.setVisibility(View.INVISIBLE);
                        amtvNickName.setVisibility(View.INVISIBLE);
                        flash = true;
                        handler.sendEmptyMessageDelayed(0, 500);
                    }
                    break;
                case 1:
                    if (changeColor) {
                        amtvMarkname.setTextColor(R.color.blue);
                        amtvNickName.setTextColor(R.color.blue);
                        handler.sendEmptyMessageDelayed(2, 300);
                    } else {
                        setColor();
                    }
                    break;
                case 2:
                    if (changeColor) {
                        amtvMarkname.setTextColor(R.color.red);
                        amtvNickName.setTextColor(R.color.red);
                        handler.sendEmptyMessageDelayed(3, 300);
                    } else {
                        setColor();
                    }
                    break;
                case 3:
                    if (changeColor) {
                        amtvMarkname.setTextColor(R.color.green);
                        amtvNickName.setTextColor(R.color.green);
                        handler.sendEmptyMessageDelayed(4, 300);
                    } else {
                        setColor();
                    }
                    break;
                case 4:
                    if (changeColor) {
                        amtvMarkname.setTextColor(R.color.yellow);
                        amtvNickName.setTextColor(R.color.yellow);
                        handler.sendEmptyMessageDelayed(5, 300);
                    } else {
                        setColor();
                    }
                    break;
                case 5:
                    changeColor = false;
                    setColor();
                    break;
            }
        }
    };

    public FriendItemView(BaseActivity context) {
        super(context);
        this.context = context;
        if (currentUin == 0) currentUin = ((AtomQQApplication) context.getApplication()).getCurrentUser().getUin();
        LayoutInflater infater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infater.inflate(R.layout.widget_friend_item, this, true);
        ivFace = (ImageView) findViewById(R.id.widget_friend_item_ivFace);
        ivFaceBackground = (ImageView) findViewById(R.id.widget_friend_item_ivFaceBackbround);
        amtvMarkname = (AlwaysMarqueeTextView) findViewById(R.id.widget_friend_item_amtvFriendMarkname);
        amtvNickName = (AlwaysMarqueeTextView) findViewById(R.id.widget_friend_item_amtvFriendNickname);
        amtvLNick = (AlwaysMarqueeTextView) findViewById(R.id.widget_friend_item_amtvFriendLNick);
        ffbvBadge = (FriendFaceBadgeView) findViewById(R.id.widget_friend_item_badge);
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
        String markname = friend.getMarkname();
        String nickname = friend.getNick();
        String lnick = friend.getLnick();
        if (null == markname || "".equals(markname)) {
            if (null == nickname) {
                markname = Long.toString(friend.getUin());
                nickname = "";
            } else {
                markname = new String(nickname);
                nickname = "";
            }
        } else {
            if (null == nickname) nickname = "";
            nickname = "[" + nickname + "]";
        }
        if (null == lnick) {
            lnick = "";
            FriendSingleLongNickTask taskLNick = new FriendSingleLongNickTask(context);
            taskLNick.setOnTaskResult(this, FLAG_LNICK);
            taskLNick.setParams(friend.getUin());
            taskLNick.execute();
        }
        String face = Utils.getFriendFacePath(currentUin, friend.getUin());
        if ("".equals(face)) {
            ivFace.setImageResource(R.drawable.friendface);
            FaceTask taskFace = new FaceTask(context);
            taskFace.setOnTaskResult(this, FLAG_FACE);
            taskFace.setParams(currentUin, friend.getUin(), FaceTask.FRIEND_FACE);
            taskFace.execute();
        } else if ("sderr".equals(face)) {
            ivFace.setImageResource(R.drawable.friendface);
        } else {
            ivFace.setImageURI(Uri.parse(face));
        }
        amtvMarkname.setText(markname);
        amtvNickName.setText(nickname);
        amtvLNick.setText(lnick);
        setColor();
        if (friend.hasNewMessage() == 1) {
            flash = true;
            handler.sendEmptyMessageDelayed(0, 300);
        } else {
            flash = false;
            handler.removeMessages(0);
            amtvLNick.setVisibility(View.VISIBLE);
            amtvMarkname.setVisibility(View.VISIBLE);
            amtvNickName.setVisibility(View.VISIBLE);
        }
        if (friend.isJustOnline() == 1) {
            changeColor = true;
            handler.sendEmptyMessageDelayed(1, 300);
            friend.setJustOnline(0);
        } else {
            handler.removeMessages(1);
        }
    }

    private void setColor() {
        if (friend.isOnline() == 1) {
            ivFaceBackground.setImageResource(R.drawable.face_background_online);
            ivFace.setAlpha(255);
            amtvMarkname.setTextColor(getResources().getColor(R.color.dark_black));
            if (friend.getVip() == 1) amtvMarkname.setTextColor(getResources().getColor(R.color.red));
        } else {
            ivFaceBackground.setImageResource(R.drawable.face_background_offline);
            ivFace.setAlpha(70);
            ivFaceBackground.setAlpha(70);
            amtvMarkname.setTextColor(getResources().getColor(R.color.dark_gray));
            if (friend.getVip() == 1) amtvMarkname.setTextColor(getResources().getColor(R.color.darkred));
        }
    }

    public Friend getFriend() {
        return friend;
    }

    @Override
    public void onTaskResult(Response response, int flag) {
        if (null == response) return;
        switch(flag) {
            case FLAG_LNICK:
                Friend f = ((LNickResponse) response).getFriend();
                if (null == f) return;
                String lnick = f.getLnick();
                if (null == lnick) return;
                ((AtomQQApplication) context.getApplication()).addFriend(friend);
                if (f.getUin() == friend.getUin()) {
                    friend.setLnick(lnick);
                    amtvLNick.setText(lnick);
                }
                break;
            case FLAG_FACE:
                Friend face = ((FaceResponse) response).getFace();
                String facePath = face.getFace();
                if (face.getUin() == friend.getUin()) {
                    ivFace.setImageURI(Uri.parse(facePath));
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        amtvLNick.setMarquee(true);
        amtvMarkname.setMarquee(true);
        amtvNickName.setMarquee(true);
        String lnick = friend.getLnick();
        String markname = friend.getMarkname();
        String nickname = friend.getNick();
        if (null == markname || "".equals(markname)) {
            if (null == nickname) {
                markname = Long.toString(friend.getUin());
                nickname = "";
            } else {
                markname = new String(nickname);
                nickname = "";
            }
        } else {
            if (null == nickname) nickname = "";
            nickname = "[" + nickname + "]";
        }
        amtvLNick.setText(lnick);
        amtvMarkname.setText(markname);
        amtvNickName.setText(nickname);
        return super.onTouchEvent(event);
    }

    public View getBadgeView() {
        return ffbvBadge;
    }
}
