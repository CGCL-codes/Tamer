package com.guanghua.brick.html;

public interface IReplace {

    public String getTarget();

    public void setTarget(String target);

    public String getReplacement();

    public void setReplacement(String replacement);

    public boolean isAll();

    public void setAll(boolean all);

    public boolean isRegex();

    public void setRegex(boolean regex);

    public String replace(String data);
}
