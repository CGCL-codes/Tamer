package com.website.at.fw.blogic;

public interface IBussiness<P, R> {

    public R execute(P param);
}
