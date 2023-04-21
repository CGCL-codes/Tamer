package com.bardsoftware.foronuvolo.data;

import java.util.List;

public interface DiscussionChunk {

    List<Discussion> getDiscussions();

    String getID();

    String getNextChunkID();

    String getPrevChunkID();
}
