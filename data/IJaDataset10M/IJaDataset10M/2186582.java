package com.bizosys.oneline.services;

import com.bizosys.oneline.util.StringUtils;

public class ServiceMetaData {

    public String id = StringUtils.Empty;

    public String name = StringUtils.Empty;

    public String iconName = StringUtils.Empty;

    public String url = StringUtils.Empty;

    public String tags = StringUtils.Empty;

    public String[] getTags() {
        return StringUtils.getStrings(tags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("id:").append(id).append('-');
        sb.append("name:").append(name).append('-');
        sb.append("iconName:").append(iconName).append('-');
        sb.append("url:").append(url).append('-');
        sb.append("tags:").append(tags).append('-');
        return sb.toString();
    }
}
