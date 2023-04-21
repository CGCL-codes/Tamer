package ShareLife.gar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class PlayerInfo {

    public int ID = 0;

    public String name = null;

    public int xposition = 0;

    public int yposition = 0;

    public int phonenumber = 0;

    public Bitmap photoImage = null;

    public Bitmap mycontent = null;

    public boolean visbile;

    public Bitmap myreferenceImage = null;

    public int titlecolor = 0x90;

    public int backgroundcolor = 0xff;

    public static int contentSize = 100;

    public String myName;

    public String phoneNumber;

    public Vector<FriendItem> friendList;

    public PlayerInfo(String aName, int aID, int aXposition, int aYposition, Bitmap aPhotoImage, int color, String aPhoneNumber) {
        name = aName;
        ID = aID;
        xposition = aXposition;
        yposition = aYposition;
        photoImage = aPhotoImage;
        myreferenceImage = aPhotoImage;
        mycontent = Bitmap.createBitmap(contentSize, contentSize, Config.ARGB_8888);
        Matrix mMatrix = new Matrix();
        float tempwidth = aPhotoImage.getWidth();
        float tempheight = aPhotoImage.getHeight();
        mMatrix.postScale((contentSize / tempwidth), contentSize / tempheight);
        mMatrix.postTranslate(0, 30);
        myName = aName;
        Canvas background = new Canvas();
        background.setBitmap(mycontent);
        Paint backpaint = new Paint();
        titlecolor = titlecolor << 24 | color;
        backgroundcolor = backgroundcolor << 24 | color;
        backpaint.setColor(titlecolor);
        background.drawRect(0, 0, 100, 15, backpaint);
        backpaint.setColor(backgroundcolor);
        background.drawRect(0, 15, 100, 30, backpaint);
        backpaint.setColor(Color.argb(0xff, 0xff, 0xff, 0xff));
        background.drawText(aName, (100 - backpaint.measureText(aName)) / 2, 12, backpaint);
        Log.i("stringphonenumber", "number" + aPhoneNumber);
        if (aPhoneNumber != null) background.drawText(aPhoneNumber, (100 - backpaint.measureText(aPhoneNumber)) / 2, 27, backpaint);
        background.drawBitmap(photoImage, mMatrix, backpaint);
        mMatrix.reset();
        phoneNumber = aPhoneNumber;
        Random r = new Random();
        float randomSkewY = Math.abs((float) ((r.nextInt())) % 3 / 50);
        if (aID % 2 != 0) {
            randomSkewY = randomSkewY * (-1);
        }
        mMatrix.setSkew(0.0f, randomSkewY);
        photoImage = Bitmap.createBitmap(mycontent, 0, 0, mycontent.getWidth(), mycontent.getHeight(), mMatrix, true);
        aPhotoImage = null;
        r = null;
        mMatrix = null;
    }

    public void setLocation(int aX, int aY) {
        xposition = aX;
        yposition = aY;
    }

    public void writeinfor(DataOutputStream dos) throws IOException {
        if (phoneNumber == null) {
            phoneNumber = "10086";
        }
        if (name == null) {
            name = "steven";
        }
        dos.writeUTF(Tools.HandlePhonenumber(phoneNumber));
        dos.writeUTF(name);
        int tempWidth = myreferenceImage.getWidth();
        int tempHeight = myreferenceImage.getHeight();
        int imagedata[] = new int[tempWidth * tempHeight];
        myreferenceImage.getPixels(imagedata, 0, tempWidth, 0, 0, tempWidth, tempHeight);
        byte photoData[] = Tools.int2byte(imagedata);
        dos.writeInt(tempWidth);
        dos.writeInt(tempHeight);
        Tools.sendByteArray(dos, photoData);
    }

    public void getinfor(DataInputStream dis) throws IOException {
        friendList = new Vector<FriendItem>();
        int Friendnumber = dis.readInt();
        for (int i = 0; i < Friendnumber; i++) {
            FriendItem tempFriend = new FriendItem(this);
            tempFriend.readinfor(dis);
            friendList.addElement(tempFriend);
        }
    }

    public void releaseFriendList() {
        friendList.removeAllElements();
        friendList = null;
    }

    public void drawSelf(Canvas aGraphics, Paint aPaint) {
        if (photoImage != null) Tools.drawImage(aGraphics, photoImage, xposition, yposition); else aGraphics.drawRect(xposition, yposition, xposition + 30, yposition + 40, aPaint);
    }
}
