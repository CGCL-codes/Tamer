package net.sourceforge.oracle.jutils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import oracle.CartridgeServices.ContextManager;
import oracle.CartridgeServices.CountException;
import oracle.CartridgeServices.InvalidKeyException;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.omg.PortableInterceptor.SUCCESSFUL;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter.User;

/**
 *
 * @author didi
 */
public class JdbmsTwitterFriendsTableImpl extends JdbmsTwitter implements SQLData {

    String sql_type;

    private BigDecimal key;

    private static StoredCtx sctx;

    static final BigDecimal SUCCESS = new BigDecimal(0);

    static final BigDecimal ERROR = new BigDecimal(1);

    private static ResultSet rset;

    private static int nbFriendsEntries = 0;

    private static int nbProcessedFriendsEntries = 0;

    private static String ORACLE_TWITTER_FRIEND_TYPE_SET_TABLE_NAME = "T_TWITTER_FRIEND";

    private static String ORACLE_TWITTER_FRIEND_IMPLEMENT_NAME = "TWITTERFRIENDSTABLEIMPL";

    private static String ORACLE_TWITTER_FRIEND_TYPE = "TWITTER_FRIEND_TYPE";

    private static String ORACLE_TWITTER_FRIEND_TYPESET = "TWITTER_FRIEND_TYPESET";

    private static int ORACLE_TWITTER_FRIEND_TYPE_NB_COLUMNS = 30;

    private static Iterator<User> friendsIterator;

    public String getSQLTypeName() throws SQLException {
        return sql_type;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        key = stream.readBigDecimal();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(key);
    }

    public static BigDecimal ODCITableStart(STRUCT[] sctx, String iRootUrl, String iLogin, String iPassword) throws Exception {
        System.out.println("Entering in ODCITableStart ...");
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        Statement stmt = conn.createStatement();
        rset = stmt.executeQuery("SELECT * FROM " + ORACLE_TWITTER_FRIEND_TYPE_SET_TABLE_NAME + " where 1=0");
        StoredCtx ctx = new StoredCtx(rset);
        connect(iRootUrl, iLogin, iPassword);
        nbFriendsEntries = getTwitterConnection().getFriendIDs().size();
        System.out.println("Nb friends found : " + nbFriendsEntries);
        friendsIterator = getTwitterConnection().getFriends().iterator();
        if (nbFriendsEntries == 0) {
            System.out.println("No Friends could be found !");
        } else {
            System.out.println("<" + nbFriendsEntries + "> Friends could be found.");
        }
        nbProcessedFriendsEntries = 0;
        int key = 0;
        try {
            key = ContextManager.setContext(ctx);
        } catch (CountException ce) {
            return ERROR;
        }
        Object[] impAttr = new Object[1];
        impAttr[0] = new BigDecimal(key);
        System.out.println("Building Structure Descriptor ...");
        StructDescriptor sd = new StructDescriptor(ORACLE_TWITTER_FRIEND_IMPLEMENT_NAME, conn);
        System.out.println("Structure Descriptor built for " + ORACLE_TWITTER_FRIEND_IMPLEMENT_NAME);
        sctx[0] = new STRUCT(sd, conn, impAttr);
        System.out.print("Built stored context.");
        System.out.print("SUccessfully started ODCIStartTable.");
        return SUCCESS;
    }

    public BigDecimal ODCITableFetch(BigDecimal nrows, ARRAY[] outSet) throws SQLException {
        System.out.println("Starting to fetch ODCITableFetch ...");
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.getContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        int nrowsval = nrows.intValue();
        System.out.println("nrowsval = " + nrowsval);
        System.out.println("nrows = " + nrows);
        Vector v = new Vector(nbFriendsEntries);
        StructDescriptor outDesc = StructDescriptor.createDescriptor(ORACLE_TWITTER_FRIEND_TYPE, conn);
        Object[] out_attr = new Object[ORACLE_TWITTER_FRIEND_TYPE_NB_COLUMNS];
        System.out.println("nbProcessedFriendsEntries : " + nbProcessedFriendsEntries);
        System.out.println("nbFriendsEntries : " + nbFriendsEntries);
        User lFriend = null;
        Date l_CREATION_DATE;
        String l_DESCRIPTION;
        String l_LOCATION;
        String l_NAME;
        String l_PROFILE_BACKGROUND_COLOR;
        String l_PROFILE_LINK_COLOR;
        String l_PROFILE_SIDEBAR_BORDER_COLOR;
        String l_PROFILE_SIDEBAR_FILL_COLOR;
        String l_PROFILE_TEXT_COLOR;
        String l_SCREEN_NAME;
        String l_TIMEZONE;
        int l_TIMEZONE_OFFSET;
        int l_FAVORITES_COUNT;
        int l_FOLLOWERS_COUNT;
        int l_FRIENDS_COUNT;
        long l_ID;
        String l_PROFILE_BACKGROUND_IMAGE_URL;
        String l_PROFILE_IMAGE_URL;
        boolean l_IS_DUMMY_OBJECT;
        boolean l_IS_FOLLOWING;
        boolean l_IS_NOTIFICATIONS;
        boolean l_IS_PROFILE_BACKGROUND_TILE;
        boolean l_IS_PROTECTED_USER;
        boolean l_IS_VERIFIED;
        String l_STATUS_TEXT;
        long l_STATUS_ID;
        int l_STATUSES_COUNT;
        String l_WEBSITE;
        Date l_STATUS_CREATED_AT;
        String l_STATUS_MENTIONS;
        boolean l_STATUS_IS_FAVORITE;
        if (nbProcessedFriendsEntries < nbFriendsEntries) {
            while (friendsIterator.hasNext()) {
                lFriend = friendsIterator.next();
                l_CREATION_DATE = lFriend.getCreatedAt();
                l_DESCRIPTION = lFriend.getDescription();
                l_LOCATION = lFriend.getLocation();
                l_NAME = lFriend.getName();
                l_PROFILE_BACKGROUND_COLOR = lFriend.getProfileBackgroundColor();
                l_PROFILE_LINK_COLOR = lFriend.getProfileLinkColor();
                l_PROFILE_SIDEBAR_BORDER_COLOR = lFriend.getProfileSidebarBorderColor();
                l_PROFILE_SIDEBAR_FILL_COLOR = lFriend.getProfileSidebarFillColor();
                l_PROFILE_TEXT_COLOR = lFriend.getProfileTextColor();
                l_SCREEN_NAME = lFriend.getScreenName();
                l_TIMEZONE = lFriend.getTimezone();
                l_TIMEZONE_OFFSET = lFriend.getTimezoneOffSet();
                l_FAVORITES_COUNT = lFriend.getFavoritesCount();
                l_FOLLOWERS_COUNT = lFriend.getFollowersCount();
                l_FRIENDS_COUNT = lFriend.getFriendsCount();
                l_ID = lFriend.getId();
                l_PROFILE_BACKGROUND_IMAGE_URL = lFriend.getProfileBackgroundImageUrl().toString();
                l_PROFILE_IMAGE_URL = lFriend.getProfileImageUrl().toString();
                l_IS_DUMMY_OBJECT = lFriend.isDummyObject();
                l_IS_FOLLOWING = lFriend.isFollowing();
                l_IS_NOTIFICATIONS = lFriend.isNotifications();
                l_IS_PROFILE_BACKGROUND_TILE = lFriend.isProfileBackgroundTile();
                l_IS_PROTECTED_USER = lFriend.isProtectedUser();
                l_IS_VERIFIED = lFriend.isVerified();
                if (lFriend.getStatus() == null) {
                    System.out.println("WARNING : NULL Status found, will put empty contents in Status relative variables");
                    l_STATUS_TEXT = "";
                    l_STATUS_CREATED_AT = null;
                    l_STATUS_MENTIONS = "";
                    l_STATUS_IS_FAVORITE = false;
                    l_STATUS_ID = 0;
                } else {
                    System.out.println("Status is not null");
                    l_STATUS_TEXT = lFriend.getStatus().getText();
                    l_STATUS_CREATED_AT = lFriend.getStatus().getCreatedAt();
                    if (lFriend.getStatus().getMentions() != null) {
                        l_STATUS_MENTIONS = TwitterUtils.splitMentions(lFriend.getStatus().getMentions());
                    } else {
                        l_STATUS_MENTIONS = "";
                    }
                    l_STATUS_IS_FAVORITE = lFriend.getStatus().isFavorite();
                    l_STATUS_ID = lFriend.getStatus().getId();
                }
                l_STATUSES_COUNT = lFriend.getStatusesCount();
                if (lFriend.getWebsite() == null) {
                    l_WEBSITE = "";
                } else {
                    l_WEBSITE = lFriend.getWebsite().toString();
                }
                if (l_CREATION_DATE == null) {
                    out_attr[0] = (Object) null;
                } else {
                    out_attr[0] = (Object) new Timestamp(l_CREATION_DATE.getTime());
                }
                if (l_DESCRIPTION == null) {
                    out_attr[1] = (Object) null;
                } else {
                    out_attr[1] = (Object) new String(l_DESCRIPTION);
                }
                if (l_LOCATION == null) {
                    out_attr[2] = (Object) null;
                } else {
                    out_attr[2] = (Object) new String(l_LOCATION);
                }
                if (l_NAME == null) {
                    out_attr[3] = (Object) null;
                } else {
                    out_attr[3] = (Object) new String(l_NAME);
                }
                if (l_PROFILE_BACKGROUND_COLOR == null) {
                    out_attr[4] = (Object) null;
                } else {
                    out_attr[4] = (Object) new String(l_PROFILE_BACKGROUND_COLOR);
                }
                if (l_PROFILE_LINK_COLOR == null) {
                    out_attr[5] = (Object) null;
                } else {
                    out_attr[5] = (Object) new String(l_PROFILE_LINK_COLOR);
                }
                if (l_PROFILE_SIDEBAR_BORDER_COLOR == null) {
                    out_attr[6] = (Object) null;
                } else {
                    out_attr[6] = (Object) new String(l_PROFILE_SIDEBAR_BORDER_COLOR);
                }
                if (l_PROFILE_SIDEBAR_FILL_COLOR == null) {
                    out_attr[7] = (Object) null;
                } else {
                    out_attr[7] = (Object) new String(l_PROFILE_SIDEBAR_FILL_COLOR);
                }
                if (l_PROFILE_TEXT_COLOR == null) {
                    out_attr[8] = (Object) null;
                } else {
                    out_attr[8] = (Object) new String(l_PROFILE_TEXT_COLOR);
                }
                if (l_SCREEN_NAME == null) {
                    out_attr[9] = (Object) null;
                } else {
                    out_attr[9] = (Object) new String(l_SCREEN_NAME);
                }
                if (l_TIMEZONE == null) {
                    out_attr[10] = (Object) null;
                } else {
                    out_attr[10] = (Object) new String(l_TIMEZONE);
                }
                out_attr[11] = (Object) new Integer(l_TIMEZONE_OFFSET);
                out_attr[12] = (Object) new Integer(l_FAVORITES_COUNT);
                out_attr[13] = (Object) new Integer(l_FOLLOWERS_COUNT);
                out_attr[14] = (Object) new Integer(l_FRIENDS_COUNT);
                out_attr[15] = (Object) new Long(l_ID);
                if (l_PROFILE_BACKGROUND_IMAGE_URL == null) {
                    out_attr[16] = (Object) null;
                } else {
                    out_attr[16] = (Object) new String(l_PROFILE_BACKGROUND_IMAGE_URL);
                }
                if (l_PROFILE_IMAGE_URL == null) {
                    out_attr[17] = (Object) null;
                } else {
                    out_attr[17] = (Object) new String(l_PROFILE_IMAGE_URL);
                }
                if (l_IS_DUMMY_OBJECT) {
                    out_attr[18] = (Object) new Integer(1);
                } else {
                    out_attr[18] = (Object) new Integer(0);
                }
                if (l_IS_FOLLOWING) {
                    out_attr[19] = (Object) new Integer(1);
                } else {
                    out_attr[19] = (Object) new Integer(0);
                }
                if (l_IS_NOTIFICATIONS) {
                    out_attr[20] = (Object) new Integer(1);
                } else {
                    out_attr[20] = (Object) new Integer(0);
                }
                if (l_IS_PROFILE_BACKGROUND_TILE) {
                    out_attr[21] = (Object) new Integer(1);
                } else {
                    out_attr[21] = (Object) new Integer(0);
                }
                if (l_IS_PROTECTED_USER) {
                    out_attr[22] = (Object) new Integer(1);
                } else {
                    out_attr[22] = (Object) new Integer(0);
                }
                if (l_STATUS_TEXT == null) {
                    out_attr[23] = (Object) null;
                } else {
                    out_attr[23] = (Object) new String(l_STATUS_TEXT);
                }
                if (l_STATUS_CREATED_AT == null) {
                    out_attr[24] = (Object) null;
                } else {
                    out_attr[24] = (Object) new Timestamp(l_STATUS_CREATED_AT.getTime());
                }
                if (l_STATUS_MENTIONS == null) {
                    out_attr[25] = (Object) null;
                } else {
                    out_attr[25] = (Object) new String(l_STATUS_MENTIONS);
                }
                if (l_STATUS_IS_FAVORITE) {
                    out_attr[26] = (Object) new Integer(1);
                } else {
                    out_attr[26] = (Object) new Integer(0);
                }
                out_attr[27] = (Object) new Long(l_STATUS_ID);
                out_attr[28] = (Object) new Integer(l_STATUSES_COUNT);
                if (l_WEBSITE == null) {
                    out_attr[29] = (Object) null;
                } else {
                    out_attr[29] = (Object) new String(l_WEBSITE);
                }
                nbProcessedFriendsEntries++;
                v.add((Object) new STRUCT(outDesc, conn, out_attr));
            }
            nrows = new BigDecimal(0);
            nrowsval = 0;
            Object out_arr[] = v.toArray();
            ArrayDescriptor ad = new ArrayDescriptor(ORACLE_TWITTER_FRIEND_TYPESET, conn);
            outSet[0] = new ARRAY(ad, conn, out_arr);
        }
        return SUCCESS;
    }

    public BigDecimal ODCITableClose() throws SQLException {
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.clearContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        Statement stmt = ctx.getResultSet().getStatement();
        ctx.getResultSet().close();
        if (stmt != null) stmt.close();
        nbProcessedFriendsEntries = 0;
        return SUCCESS;
    }
}
