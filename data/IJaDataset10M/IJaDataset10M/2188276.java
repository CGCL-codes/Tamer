package com.adserversoft.flexfuse.server.api.ui;

import com.adserversoft.flexfuse.server.api.User;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 */
public interface IUserService {

    public ServerResponse create(ServerRequest sr, User user);

    public ServerResponse read(ServerRequest sr, Integer id);

    public ServerResponse update(ServerRequest sr, User user);

    public ServerResponse deleteUser(ServerRequest sr, Integer id);

    public ServerResponse getList(ServerRequest sr);

    public ServerResponse logoutUser(ServerRequest sr, User user);

    public ServerResponse loginUser(ServerRequest sr, User user);
}
