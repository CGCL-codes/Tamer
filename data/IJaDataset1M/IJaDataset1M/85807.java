package net.f.app;

import java.util.Hashtable;
import net.f.Auth;
import net.f.Content;
import net.f.Global;
import net.f.Item;
import net.f.Server;
import net.f.UserAuth;
import net.f.UserId;
import net.f.auth.Digest;
import net.f.error.AppException;
import net.f.error.AuthException;
import net.f.math.EncKey;
import net.f.query.AdQuery;
import net.f.query.ItemData;
import net.f.query.Query;
import net.f.query.TextDisplay;
import net.f.ui.MapLocation;

/**
 *
 * @author dahgdevash@gmail.com
 */
public class AppContext {

    public static final int ADV_QUERY = 8;

    public static final int TYPE_QUERY = 1;

    public static final int TYPE_UPLOAD = 2;

    public static final int TYPE_ADMIN = 4;

    public static String SERVER = "w1-fi.com";

    private static AppContext instance;

    private Long sync;

    private String locale;

    private Integer itemId;

    private Auth auth;

    private UserId userId;

    private Integer serverId;

    private Query query;

    private Server[] servers;

    private String[] licenses;

    private String[] locales;

    private ItemData[] results;

    private EncKey serverKey;

    private Store store;

    private int queryType;

    private Global global;

    private MapLocation mapLocation;

    private Hashtable auths;

    private boolean editAccts;

    private String fileUrl;

    public static AppContext instance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public AppContext() {
        query = new Query();
        global = new Global();
        userId = new UserId();
        auth = new Auth();
        store = new Store();
        mapLocation = new MapLocation();
        this.queryType = AppContext.TYPE_QUERY;
        this.setLocale("en");
    }

    public UserAuth getUserAuth(boolean authenticate) {
        UserAuth userAuth = new UserAuth();
        if (isUserActive()) {
            userAuth.setItemId(itemId);
            userAuth.setUserId(userId);
            if (authenticate) {
                userAuth.setAuth(generateAuth());
            }
        }
        return userAuth;
    }

    public EncKey getPublicKey() {
        if (serverKey == null) {
            synchronized (AppContext.class) {
                ServiceAdapter port = ServiceAdapter.instance();
                String[] values = port.getPublicKey();
                serverKey = new EncKey(values);
            }
        }
        return serverKey;
    }

    /**
     * prepare for submit
     *
     * @param auth 
     */
    public void prepare(Auth auth) {
        String passwd = auth.getPasswd();
        if (!passwd.startsWith("md5:")) {
            passwd = Digest.md5(passwd);
            EncKey key = getPublicKey();
            auth.setHash(key.getId());
            auth.setCreated(new Long(getSync()));
            auth.setPasswd(key.encrypt(passwd));
        } else {
            int ix = passwd.indexOf(":");
            if (ix + 1 < passwd.length()) {
                auth.setPasswd(passwd.substring(ix + 1));
            } else {
                auth.setPasswd("");
            }
        }
    }

    /**
     * generate unique authentication data
     *
     * @return Auth
     */
    public Auth generateAuth() {
        return generateAuth(auth);
    }

    public Auth generateAuth(Auth orig) {
        long localSync = getSync();
        Auth localAuth = new Auth();
        String sessionId = Integer.toHexString(userId.getId().intValue());
        String password = orig.getPasswd();
        localAuth.setHash(sessionId);
        localAuth.setCreated(new Long(localSync));
        String cr1 = Long.toString(localSync, 16);
        String gen = Digest.md5(sessionId + cr1 + password);
        localAuth.setPasswd(gen);
        return localAuth;
    }

    public long getSync() {
        long now = System.currentTimeMillis();
        return now + sync.longValue();
    }

    public void setSync(Long sync) {
        this.sync = sync;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Integer getItemId() {
        Integer ret = null;
        switch(queryType) {
            case TYPE_ADMIN:
                if (itemId == null && userId != null) {
                    itemId = userId.getId();
                }
            case TYPE_UPLOAD:
                ret = itemId;
                break;
        }
        return ret;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public String[] getLicenses() {
        return licenses;
    }

    public void setLicenses(String[] licenses) {
        this.licenses = licenses;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
        TextDisplay txt = query.getText();
        AdQuery ad = query.getAdquery();
        Global local = null;
        if (global != null) {
            global.setLocale(locale);
        }
        if (ad != null) {
            local = ad.getGlobal();
            local.setLocale(locale);
        }
        if (txt != null) {
            Content content = txt.getLabel();
            if (content != null) {
                content.setLocale(locale);
            }
        }
    }

    public String[] getLocales() {
        return locales;
    }

    public void setLocales(String[] locales) {
        this.locales = locales;
    }

    public Query getQuery() {
        return this.query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public ItemData[] getResults() {
        return results;
    }

    public void setResults(ItemData[] results) {
        this.results = results;
        if (results != null) {
            mapLocation.initResultMarkers(results);
        }
    }

    public Server[] getServers() {
        return servers;
    }

    public void setServers(Server[] servers) {
        this.servers = servers;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setQueryType(int queryType) {
        setQueryType(queryType, false);
    }

    public void setQueryType(int queryType, boolean reset) {
        if (this.queryType != queryType || reset) {
            this.resetQuery();
        }
        this.queryType = queryType;
    }

    public void resetQuery() {
        this.query = new Query();
    }

    public int getQueryType() {
        return queryType;
    }

    public MapLocation getMapLocation() {
        return mapLocation;
    }

    public boolean isSearch() {
        return (queryType & AppContext.TYPE_QUERY) != 0;
    }

    public boolean isUpload() {
        return queryType == AppContext.TYPE_UPLOAD;
    }

    public boolean isAdmin() {
        return queryType == AppContext.TYPE_ADMIN;
    }

    public void setEditAccts(boolean value) {
        this.editAccts = value;
    }

    public boolean editAccts() {
        return this.editAccts;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public Integer getItemType() {
        Integer ret = null;
        Query query = getQuery();
        Item item = query.getItem();
        if (item != null) {
            ret = item.getType();
        }
        return ret;
    }

    public boolean isUserActive() {
        return (this.userId != null && this.userId.getId() != null);
    }

    public void addAuth(Auth auth) {
        if (this.auths == null) {
            this.auths = new Hashtable(10);
        }
        if (this.auths.size() >= 10) {
            this.auths.clear();
        }
        this.auths.put(auth.getId(), auth);
    }

    public Auth getAuth(Integer authId, boolean create) {
        Auth ret = null;
        if (this.auths != null) {
            ret = (Auth) this.auths.get(authId);
        }
        if (ret == null && create) {
            ret = new Auth();
            ret.setId(authId);
        }
        return ret;
    }

    public void prepare(UserAuth userAuth, Integer itemId, Integer authId) throws AuthException, AppException {
        Auth auth = null;
        AppContext ctx = AppContext.instance();
        Store store = ctx.getStore();
        userAuth.setItemId(itemId);
        if (authId != null) {
            auth = store.getAuth(authId);
            if (auth == null) {
                auth = ctx.getAuth(authId, false);
            }
            if (auth == null) {
                auth = ctx.getQuery().getAuth();
            }
            if (auth == null || !authId.equals(auth.getId())) {
                throw new AuthException("Private Detail, please use Authentication.");
            }
            if (auth != null) {
                if (ctx.isUserActive()) {
                    auth = ctx.generateAuth(auth);
                } else {
                    throw new AppException("Login required");
                }
            }
            userAuth.setAuth(auth);
        }
    }

    public boolean advSearch() {
        return (this.queryType & ADV_QUERY) != 0;
    }

    public void setAdv(boolean adv) {
        if (adv) {
            this.queryType = TYPE_QUERY | ADV_QUERY;
        }
    }
}
