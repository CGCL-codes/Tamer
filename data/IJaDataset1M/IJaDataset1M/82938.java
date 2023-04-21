package edu.iu.iv.toolkits.vwtk.datamodel.user.impl;

import java.util.HashMap;
import java.util.Map;
import edu.iu.iv.toolkits.vwtk.datamodel.user.IUserAction;
import edu.iu.iv.toolkits.vwtk.datamodel.user.IUserActionKey;

public class UserStringKeysGenerator {

    private static final UserStringKeysGenerator INSTANCE = new UserStringKeysGenerator();

    private Map<User, Map<IUserActionKey<? extends IUserAction>, String>> userMap;

    private UserStringKeysGenerator() {
        initUserMap();
    }

    private void initUserMap() {
        this.userMap = new HashMap<User, Map<IUserActionKey<? extends IUserAction>, String>>();
    }

    public static UserStringKeysGenerator getInstance() {
        return INSTANCE;
    }

    public String getStringKey(User user, IUserActionKey<? extends IUserAction> action) {
        Map<IUserActionKey<? extends IUserAction>, String> actionToKeyMap = userMap.get(user);
        return actionToKeyMap.get(action);
    }

    public String[] getStringKeys(User[] users, IUserActionKey<? extends IUserAction> action) {
        String[] keys = new String[users.length];
        for (int i = 0; i < users.length; ++i) {
            String k = userMap.get(users[i]).get(action);
            if (k != null) keys[i] = k;
        }
        return keys;
    }

    private String generateStringKey(User user, IUserActionKey<? extends IUserAction> action) {
        return user.getName() + action;
    }

    public void generateStringKeys(User[] users, IUserActionKey<? extends IUserAction>[] actions) {
        if (userMap == null) initUserMap();
        for (int i = 0; i < users.length; ++i) {
            Map<IUserActionKey<? extends IUserAction>, String> actionToKeyMap = userMap.get(users[i]);
            if (actionToKeyMap == null) actionToKeyMap = new HashMap<IUserActionKey<? extends IUserAction>, String>();
            for (int j = 0; j < actions.length; ++j) {
                String key = generateStringKey(users[i], actions[j]);
                actionToKeyMap.put(actions[j], key);
            }
            userMap.put(users[i], actionToKeyMap);
        }
    }

    public void clear() {
        if (this.userMap != null) this.userMap.clear();
    }
}
