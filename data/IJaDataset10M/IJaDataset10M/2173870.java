package org.or5e.web.folder;

import java.util.List;
import java.util.Map;

public interface FolderServiceManager extends Service, FolderService {

    public Map<String, List<String>> getAudioResource(Long userID);

    public Map<String, List<String>> getVideoResource(Long userID);
}
