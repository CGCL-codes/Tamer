package cn.saker.designpattern.gof.create.builder;

public interface Builder {

    public void buildFirstName();

    public void buildMiddleName();

    public void buildLastName();

    public String getFullName();
}
