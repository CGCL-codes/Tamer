package com.tamtamy.jatta.custom.controls;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.tamtamy.jatta.TamTamyHelper;
import com.tamtamy.jttamobile.data.OnLineUsersList;
import com.tamtamy.jttamobile.data.OnlineUser;
import com.tamtamy.jttamobile.data.UserDetail;
import com.tamtamy.jttamobile.exception.JTTAException;

public class OnlineUsers extends LinearLayout {

    public static TamTamyHelper tth = null;

    public int ImageSize = 25;

    public OnlineUsers(Context context) {
        super(context);
        InflateLayout();
    }

    public OnlineUsers(Context context, AttributeSet attrs) {
        super(context, attrs);
        InflateLayout();
    }

    private void InflateLayout() {
        this.setOrientation(HORIZONTAL);
        int usersN = checkUsersNumber();
        try {
            tth = TamTamyHelper.getInstance();
            OnLineUsersList users = tth.getOnLineUsersList(0, usersN);
            for (OnlineUser user : users) {
                UserDetail details = tth.getUserDetail(user.getUserID());
                String avatarFilename = details.getAvatarFilename();
                AddAvatar(avatarFilename);
            }
        } catch (JTTAException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    private int checkUsersNumber() {
        Context context = getContext();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = context.getResources().getDisplayMetrics();
        if (metrics.widthPixels > 0) {
            int rtnValue = metrics.widthPixels / ImageSize;
            return rtnValue;
        }
        return 0;
    }

    private void AddAvatar(String filename) {
        Context context = getContext();
        Drawable image = ImageOperations(context, "http://a1.twimg.com/profile_images/57769483/LogoGekoQuadr_reasonably_small.png");
        ImageView imgView = new ImageView(context);
        imgView.setImageDrawable(image);
        imgView.setAdjustViewBounds(true);
        imgView.setMaxHeight(ImageSize);
        imgView.setMaxWidth(ImageSize);
        this.addView(imgView);
    }

    private Drawable ImageOperations(Context ctx, String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }
}
