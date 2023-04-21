package er.extensions;

import com.webobjects.foundation.*;

/** Interface to implement generic mutable containers */
public interface ERXMutableUserInfoHolderInterface {

    /** Returns the mutableUserInfo.*/
    public NSMutableDictionary mutableUserInfo();

    /** Set the mutableUserInfo */
    public void setMutableUserInfo(NSMutableDictionary userInfo);
}
