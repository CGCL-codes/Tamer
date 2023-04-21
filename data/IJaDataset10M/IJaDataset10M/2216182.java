package com.modelmetrics.cloudconverter.util;

import java.util.List;

public interface MigrationStatusSubscriber {

    public void publish(String migrationEvent);

    public List<String> getStatus();
}
