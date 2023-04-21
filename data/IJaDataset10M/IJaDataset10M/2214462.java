package org.thechiselgroup.choosel.protovis.client;

import com.google.gwt.user.client.ui.Widget;

public interface ProtovisExample {

    Widget asWidget();

    String getSourceCodeFile();

    String getProtovisExampleURL();

    String getDescription();
}
