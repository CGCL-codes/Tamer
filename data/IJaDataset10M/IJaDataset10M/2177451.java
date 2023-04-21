package com.ergal.ezweb.example.pages;

import com.ergal.ezweb.annotation.ViewHtml;
import com.ergal.ezweb.annotation.ViewPath;

@ViewPath("/blog")
@ViewHtml("/example/Blog.html")
public class Blog {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String service(String para) {
        return null;
    }

    public String service() {
        return null;
    }
}
