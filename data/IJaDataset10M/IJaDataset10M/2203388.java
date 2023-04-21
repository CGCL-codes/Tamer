package net.sf.webwarp.samples.school;

import net.sf.webwarp.util.hibernate.dao.IDType;

public interface School extends IDType<Integer> {

    public String getName();

    public void setName(String name);

    public String getLocation();

    public void setLocation(String location);
}
