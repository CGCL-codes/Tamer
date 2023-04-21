package com.alibaba.cobar.client.support.execution;

import java.util.List;

public interface IConcurrentRequestProcessor {

    List<Object> process(List<ConcurrentRequest> requests);
}
