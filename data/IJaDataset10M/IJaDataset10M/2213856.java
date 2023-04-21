package com.guanghua.brick.html;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IContent {

    public static final String CONTENT_POOL = "globe.content.pool";

    public void setId(String id);

    public String getId();

    public List<Map<String, String>> buildContent(HttpServletRequest request, HttpServletResponse response);
}
