package jokeboxjunior.core.model.audio.lyrics;

import cb_commonobjects.datastore.AttributeTypeEnum;
import cb_commonobjects.util.StringFuncs;
import java.util.HashMap;
import java.util.Map;
import jokeboxjunior.core.controller.AbstractController;
import jokeboxjunior.core.controller.MainController;
import jokeboxjunior.core.masks.audio.lyrics.AudioLyricsOutputMask;
import jokeboxjunior.core.model.AbstractModelObject;

/**
 *
 * @author B1
 */
public class AudioLyrics extends AbstractModelObject {

    public static final String ATTRIB_LYRICS_CONTENT = "lyrics_content";

    public static final String ATTRIB_INDEX_LIST = "index_list";

    public AudioLyrics() {
        super("audio_lyrics");
    }

    @Override
    protected void _addAttribs() {
        _addAttrib(ATTRIB_LYRICS_CONTENT, AttributeTypeEnum.ATT_TYPE_STRING, 10000);
        _addAttrib(ATTRIB_INDEX_LIST, AttributeTypeEnum.ATT_TYPE_STRING, 5000);
    }

    public String getLyricsContent() {
        return this.getAttribString(ATTRIB_LYRICS_CONTENT);
    }

    public void setLyricsContent(String LyricsContent) {
        this.setAttrib(ATTRIB_LYRICS_CONTENT, LyricsContent);
    }

    protected String getIndexList() {
        return this.getAttribString(ATTRIB_INDEX_LIST);
    }

    public Map<Integer, Integer> getIndexListMap() {
        String myIndexList = getIndexList();
        Map<Integer, Integer> myIndexListMap = new HashMap<Integer, Integer>();
        if (!StringFuncs.isEmpty(myIndexList)) {
            for (String myLine : myIndexList.split(";")) {
                String[] myParts = myLine.split("=");
                myIndexListMap.put(Integer.parseInt(myParts[0]), Integer.parseInt(myParts[1]));
            }
        }
        return myIndexListMap;
    }

    protected void setIndexList(String IndexList) {
        this.setAttrib(ATTRIB_INDEX_LIST, IndexList);
    }

    public void setIndexListMap(Map<Integer, Integer> IndexListMap) {
        if (IndexListMap.size() == 0) {
            setIndexList("");
        } else {
            String myIndexList = "";
            for (Integer myCharPos : IndexListMap.keySet()) {
                myIndexList += myCharPos + "=" + IndexListMap.get(myCharPos) + ";";
            }
            setIndexList(myIndexList.substring(0, myIndexList.length() - 1));
        }
    }

    @Override
    protected AudioLyricsOutputMask getNewOutputMaskObj() {
        return new AudioLyricsOutputMask(this);
    }

    @Override
    public AbstractController getController() {
        return MainController.AudioLyricsController;
    }
}
