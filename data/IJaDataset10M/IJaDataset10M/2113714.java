package org.w4tj.controller;

import java.util.Map;

public interface OKListener {

    abstract boolean onOK(Class<?> sender, Map<String, Object> args);
}
