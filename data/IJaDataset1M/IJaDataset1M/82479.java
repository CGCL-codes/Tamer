package net.jwpa.dao;

import java.io.IOException;

public interface CacheDataProvider {

    public String getKey();

    public Cacheable getData() throws IOException;
}
