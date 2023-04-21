package org.albianj.cached.service;

public interface IMemcacheGroup {

    public String getId();

    public void setId(String id);

    public boolean getEnable();

    public void setEnable(boolean enable);

    public String getAddresses();

    public void setAddresses(String addresses);

    public int[] getWeights();

    public void setWeights(int[] arr);

    public int getPoolSize();

    public void setPoolSize(int poolSize);
}
