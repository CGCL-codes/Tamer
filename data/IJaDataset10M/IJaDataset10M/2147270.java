package com.googlecode.janrain4j.samples.demo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.api.engage.response.profile.Name;
import com.googlecode.janrain4j.api.engage.response.profile.Profile;
import com.googlecode.janrain4j.springframework.security.JanrainAuthenticationToken;

public class DemoUserDetailsService implements AuthenticationUserDetailsService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DatastoreService datastoreService = null;

    @Autowired
    private EngageService engageService = null;

    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        if (token instanceof JanrainAuthenticationToken) {
            JanrainAuthenticationToken janrainAuthenticationToken = (JanrainAuthenticationToken) token;
            UserDataResponse userDataResponse = janrainAuthenticationToken.getUserDataResponse();
            Profile profile = userDataResponse.getProfile();
            String identifier = profile.getIdentifier();
            Name name = profile.getName();
            String formattedNameOrIdentifier = identifier;
            if (name != null && StringUtils.isNotBlank(name.getFormatted())) {
                formattedNameOrIdentifier = name.getFormatted();
            }
            Long primaryKey = null;
            try {
                primaryKey = Long.parseLong(profile.getPrimaryKey());
            } catch (NumberFormatException ignore) {
            }
            Entity account = null;
            if (primaryKey != null) {
                log.info("Primary key [" + primaryKey + "] in profile, retrieving account from datastore...");
                try {
                    account = datastoreService.get(KeyFactory.createKey("Account", primaryKey));
                    FlashScope.setAttribute("message", "Welcome back " + formattedNameOrIdentifier + "!");
                } catch (EntityNotFoundException e) {
                    log.info("Account not found in datastore for primary key [" + primaryKey + "]");
                }
            }
            if (account == null) {
                log.info("Creating new account...");
                account = new Entity("Account");
                datastoreService.put(account);
                primaryKey = account.getKey().getId();
                try {
                    log.info("Calling map for identifier [" + identifier + "], primary key [" + primaryKey + "]...");
                    engageService.map(identifier, String.valueOf(primaryKey));
                } catch (Exception ignore) {
                    log.error("Unable to map identifier", ignore);
                }
                FlashScope.setAttribute("message", "Thanks for registering " + formattedNameOrIdentifier + "!");
            }
            userDetails = new DemoUserDetails(primaryKey, userDataResponse);
        }
        return userDetails;
    }
}
