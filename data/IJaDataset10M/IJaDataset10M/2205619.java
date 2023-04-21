package org.auroraide.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface FileBuilder extends RemoteService {

    /**
	 * Utility class for simplifying access to the instance of async service.
	 */
    public static class Util {

        private static FileBuilderAsync instance;

        public static FileBuilderAsync getInstance() {
            if (instance == null) {
                instance = (FileBuilderAsync) GWT.create(FileBuilder.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "FileBuilder");
            }
            return instance;
        }
    }

    ClassUnit[] getFiles();

    boolean createFile(ClassUnit classUnit, String type);

    boolean deleteFile(ClassUnit classUnit, String type);

    boolean modifyFile(ClassUnit classUnit, String type);
}
