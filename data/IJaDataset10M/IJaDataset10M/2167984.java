package com.triplea.rolap.plugins.authentication;

import java.util.ArrayList;

/**
 * @author kononyhin
 *
 */
public class UserInfo {

    private String _username;

    private String _password;

    private ArrayList<String> _userGroups = null;

    public UserInfo(String _username, String _password, ArrayList<String> groups) {
        this._username = _username;
        this._password = _password;
        _userGroups = groups;
    }

    /**
	 * @return the _username
	 */
    public String getUsername() {
        return _username;
    }

    /**
	 * @param _username the _username to set
	 */
    public void setUsername(String _username) {
        this._username = _username;
    }

    /**
	 * @return the _password
	 */
    public String getPassword() {
        return _password;
    }

    /**
	 * @param _password the _password to set
	 */
    public void setPassword(String _password) {
        this._password = _password;
    }

    /**
	 * @return the _userGroups
	 */
    public ArrayList<String> getUserGroups() {
        return _userGroups;
    }

    /**
	 * @param groups the _userGroups to set
	 */
    public void setUserGroups(ArrayList<String> groups) {
        _userGroups.clear();
        for (int i = 0; i < groups.size(); i++) {
            _userGroups.add(groups.get(i));
        }
    }
}
