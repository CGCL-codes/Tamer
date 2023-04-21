package com.codeberry.yws;

import java.io.IOException;

public interface ContextHandler {

    String getContextPath();

    Request handle(Request request, Response response) throws IOException;
}
