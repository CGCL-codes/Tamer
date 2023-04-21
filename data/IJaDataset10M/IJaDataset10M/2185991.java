package com.mvu.banana.guest.server;

import com.mvu.banana.common.client.ServiceToken;
import com.mvu.banana.common.server.SessionManager;
import com.mvu.banana.domain.finder.CredentialFinder;
import com.mvu.banana.domain.stub.Credential;
import com.mvu.banana.guest.client.gen.UserTileDTO;
import com.mvu.banana.guest.server.gen.UserTileServletStub;

public class UserTileServlet extends UserTileServletStub {

    public UserTileDTO post(UserTileDTO dto) {
        if (dto.isSigningOut) {
            SessionManager.get().logout(request);
        } else {
            ServiceToken serviceToken = SessionManager.get().getServiceToken(request);
            String username = serviceToken.getUsername();
            Credential credential = CredentialFinder.find(username);
            dto.username = credential.getUsername();
        }
        return dto;
    }
}
