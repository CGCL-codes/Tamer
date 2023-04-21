package org.fb4j.users.impl;

import net.sf.json.JSONObject;
import org.fb4j.impl.AbstractJsonFacebookObject;
import org.fb4j.impl.AssertInitialized;
import org.fb4j.users.UserInfo;

/**
 * @author Mino Togna
 * 
 */
public class ProfilePictureImpl extends AbstractJsonFacebookObject implements UserInfo.ProfilePicture {

    private String defaultPic;

    private String big;

    private String bigWithLogo;

    private String small;

    private String smallWithLogo;

    private String square;

    private String squareWithLogo;

    private String defaultWithLogo;

    @Override
    protected void processJsonObject(JSONObject jsonObject) {
        defaultPic = jsonObject.optString("pic");
        big = jsonObject.optString("pic_big");
        bigWithLogo = jsonObject.optString("pic_big_with_logo");
        small = jsonObject.optString("pic_small");
        smallWithLogo = jsonObject.optString("pic_small_with_logo");
        square = jsonObject.optString("pic_square");
        squareWithLogo = jsonObject.optString("pic_square_with_logo");
        defaultWithLogo = jsonObject.optString("pic_with_logo");
    }

    @AssertInitialized("pic")
    public String getDefault() {
        return defaultPic;
    }

    @AssertInitialized("pic_big")
    public String getBig() {
        return big;
    }

    @AssertInitialized("pic_big_with_logo")
    public String getBigWithLogo() {
        return bigWithLogo;
    }

    @AssertInitialized("pic_small")
    public String getSmall() {
        return small;
    }

    @AssertInitialized("pic_small_with_logo")
    public String getSmallWithLogo() {
        return smallWithLogo;
    }

    @AssertInitialized("pic_square")
    public String getSquare() {
        return square;
    }

    @AssertInitialized("pic_square_with_logo")
    public String getSquareWithLogo() {
        return squareWithLogo;
    }

    @AssertInitialized("pic_with_logo")
    public String getDefaultWithLogo() {
        return defaultWithLogo;
    }
}
