package org.iceinn.iceparser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.iceinn.iceparser.data.IceData;
import org.iceinn.iceparser.handler.IceTagHandler;
import org.iceinn.tools.Logger;

/**
 * 
 * @author Lionel FLAHAUT
 *
 */
public class IceParser {

    private static final String A_TAG_START = "a";

    private static final String A_TAG_STOP = "/a";

    private static final String P_TAG_START = "p";

    private static final String P_TAG_STOP = "/p";

    private static final String IMG_TAG_START = "img";

    private static final String DIV_TAG_START = "div";

    private static final String DIV_TAG_STOP = "/div";

    private static final String FORM_TAG_START = "form";

    private static final String FORM_TAG_STOP = "/form";

    private static final String SCRIPT_TAG_START = "script";

    private static final String SCRIPT_TAG_STOP = "/script";

    public static int P = 0;

    public static int A = 1;

    public static int DIV = 2;

    public static int TEXT = 3;

    public static int FORM = 4;

    public static int IMAGE = 5;

    public static int SCRIPT = 6;

    public static int COMMENT = 7;

    public static int NB_KNOWN_TAG = 8;

    public static int TAG_NONE = -1;

    private List<IceTagHandler> handlers[];

    private static final int NO_TAG = 0;

    private static final int TAG_OPEN_STOP = 1;

    private static final int TAG_OPEN_START = 2;

    private static final int TAG_CLOSE_STOP = 3;

    private static final int TAG_CLOSE_START = 4;

    private static final int TAG_COMMENT_STOP = 5;

    private static final int TAG_COMMENT_START = 6;

    private int state = NO_TAG;

    private int[] tagStart, tagStop;

    private int[] textStart, textStop;

    private int startGap = 0;

    private LinkedList<int[]> tagQueue[] = null;

    private ByteBuffer buffer = null;

    /** Creates a new instance of XMLReader */
    @SuppressWarnings("unchecked")
    public IceParser() {
        handlers = new List[NB_KNOWN_TAG];
        tagStart = new int[2];
        tagStop = new int[2];
        textStart = new int[2];
        textStop = new int[2];
        tagQueue = new LinkedList[NB_KNOWN_TAG];
        for (int i = 0; i < 8; i++) {
            tagQueue[i] = new LinkedList<int[]>();
        }
        buffer = ByteBuffer.allocate(4096);
    }

    /**
	 * @return Returns the startGap.
	 */
    public int getStartGap() {
        return startGap;
    }

    /**
	 * @param startGap
	 *            The startGap to set.
	 */
    public void setStartGap(int startGap) {
        this.startGap = startGap;
    }

    /**
	 * Register a new handler for a type of information
	 * 
	 * @param typeOfInformation
	 * @param handler
	 */
    public void registerHandler(int typeOfInformation, IceTagHandler handler) {
        if (handlers[typeOfInformation] == null) {
            handlers[typeOfInformation] = new ArrayList<IceTagHandler>();
        }
        handlers[typeOfInformation].add(handler);
    }

    /**
	 * Register a new handler for a type of information
	 * 
	 * @param typeOfInformation
	 * @param handler
	 */
    public void unregisterHandler(int typeOfInformation, IceTagHandler handler) {
        if (handlers[typeOfInformation] != null) {
            handlers[typeOfInformation].remove(handler);
        }
    }

    /**
	 * @return Returns the handlers.
	 */
    public List<IceTagHandler> getHandlers(int type) {
        return handlers[type];
    }

    /**
	 * Reset the parser.
	 * 
	 */
    public void reset() {
        if (Logger.isLoggingDebug()) {
            Logger.trace();
        }
        startGap = 0;
        tagStart[0] = 0;
        tagStart[1] = 0;
        tagStop[0] = 0;
        tagStop[1] = 0;
        textStart[0] = 0;
        textStart[1] = 0;
        textStop[0] = 0;
        textStop[1] = 0;
        state = NO_TAG;
        for (int i = 0; i < tagQueue.length; i++) {
            tagQueue[i].clear();
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
    }

    /**
	 * Reset all handlers.
	 * 
	 */
    public void resetHandlers() {
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] != null && handlers[i].size() > 0) {
                for (int j = 0; j < handlers[i].size(); j++) {
                    if (handlers[i].get(j) != null) ((IceTagHandler) handlers[i].get(j)).reset();
                }
            }
        }
    }

    /**
	 * Rewind the buffer of tag position to an amount. WARNING : all previous
	 * data added from this position before will be erased.
	 * 
	 * @param position
	 */
    public void rewindBufferPosition(int pad) {
        try {
            if (pad <= buffer.position()) buffer.position(buffer.position() - pad);
        } catch (RuntimeException e) {
            if (Logger.isLoggingError()) {
                Logger.eLog("Error while rewinding buffer", e);
            }
        }
    }

    /**
	 * Returns <code>true</code> if a tag is currently buffered.
	 * 
	 * @return
	 */
    public boolean isTagStarted() {
        return state != NO_TAG;
    }

    /**
	 * Parse a array of byte.
	 * 
	 * @param xmlData
	 * @param resetHandlers
	 *            reset handlers before parsing.
	 * @param resetOnFinish
	 *            reset parser after parsing.
	 */
    public synchronized void parse(byte[] xmlData, int length, boolean resetHandlers, boolean resetOnFinish) {
        if (xmlData == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        boolean[] requirement = new boolean[handlers.length];
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] != null && handlers[i].size() > 0) {
                for (int j = 0; j < handlers[i].size(); j++) {
                    if (handlers[i].get(j) != null && resetHandlers) {
                        ((IceTagHandler) handlers[i].get(j)).reset();
                    }
                }
                requirement[i] = true;
            } else {
                requirement[i] = false;
            }
        }
        if (state == TAG_OPEN_START) {
            if (2 <= length && ((char) xmlData[0]) == '!' && ((char) xmlData[1]) == '-') {
                state = TAG_COMMENT_START;
            } else if (1 <= length && ((char) xmlData[0]) == '/') {
                state = TAG_CLOSE_START;
            }
        }
        for (int index = 0; index < length; index++) {
            if (((char) xmlData[index]) == '<') {
                if (state != TAG_COMMENT_START) {
                    if (index + 2 < length && ((char) xmlData[index + 1]) == '!' && ((char) xmlData[index + 2]) == '-') {
                        state = TAG_COMMENT_START;
                        tagStart[0] = index;
                        tagStart[1] = startGap;
                    } else if (index + 1 < length && ((char) xmlData[index + 1]) == '/') {
                        state = TAG_CLOSE_START;
                        tagStart[0] = index;
                        tagStart[1] = startGap;
                        buffer.put(xmlData[index]);
                    } else {
                        state = TAG_OPEN_START;
                        tagStart[0] = index;
                        tagStart[1] = startGap;
                        buffer.put(xmlData[index]);
                    }
                }
            } else if (((char) xmlData[index]) == '>') {
                if (state == TAG_COMMENT_START && index > 0 && ((char) xmlData[index - 1]) == '-') {
                    state = TAG_COMMENT_STOP;
                    tagStop[0] = index + 1;
                    tagStop[1] = startGap;
                } else if (state == TAG_OPEN_START) {
                    state = TAG_OPEN_STOP;
                    tagStop[0] = index + 1;
                    tagStop[1] = startGap;
                    buffer.put(xmlData[index]);
                } else if (state == TAG_CLOSE_START) {
                    state = TAG_CLOSE_STOP;
                    tagStop[0] = index + 1;
                    tagStop[1] = startGap;
                    buffer.put(xmlData[index]);
                }
            } else {
                if (state != TAG_COMMENT_START && state != NO_TAG) {
                    buffer.put(xmlData[index]);
                }
            }
            switch(state) {
                case TAG_OPEN_START:
                case TAG_COMMENT_START:
                case TAG_CLOSE_START:
                    if (textStart[0] > 0) {
                        textStop[0] = index;
                        textStop[1] = startGap;
                        if ((textStop[0] + textStop[1] - textStart[0] - textStart[1]) > 0 && requirement[TEXT]) {
                            if (handlers[TEXT] != null) {
                                int offsets[] = new int[4];
                                offsets[0] = textStart[0] + textStart[1];
                                offsets[1] = textStop[0] + textStop[1];
                                offsets[2] = -1;
                                offsets[3] = -1;
                                IceData data = new IceData(offsets);
                                for (int i = 0; i < handlers[TEXT].size(); i++) {
                                    if (handlers[TEXT].get(i) != null) ((IceTagHandler) handlers[TEXT].get(i)).handleNewData(data);
                                }
                            }
                        }
                        textStart[0] = 0;
                        textStop[0] = 0;
                    }
                    break;
                case TAG_OPEN_STOP:
                    buffer.flip();
                    byte[] dataToCheck = buffer.array();
                    int typeOfTag = checkTag(dataToCheck, buffer.position(), buffer.limit());
                    buffer.limit(buffer.capacity());
                    if (typeOfTag >= 0 && requirement[typeOfTag]) {
                        int[] indexes = new int[4];
                        indexes[0] = tagStart[0] + tagStart[1];
                        indexes[1] = tagStop[0] + tagStop[1];
                        indexes[2] = -1;
                        indexes[3] = -1;
                        if (typeOfTag == IMAGE) {
                            if (handlers[IMAGE] != null) {
                                IceData data = new IceData(indexes);
                                for (int i = 0; i < handlers[IMAGE].size(); i++) {
                                    if (handlers[IMAGE].get(i) != null) ((IceTagHandler) handlers[IMAGE].get(i)).handleNewData(data);
                                }
                            }
                        } else {
                            tagQueue[typeOfTag].addLast(indexes);
                        }
                    }
                    tagStart[0] = 0;
                    tagStop[0] = 0;
                    textStart[0] = index + 1;
                    textStart[1] = startGap;
                    state = NO_TAG;
                    break;
                case TAG_COMMENT_STOP:
                    if (requirement[COMMENT]) {
                        int[] indexes = new int[4];
                        indexes[0] = tagStart[0] + tagStart[1];
                        indexes[1] = tagStop[0] + tagStop[1];
                        if (handlers[COMMENT] != null) {
                            IceData data = new IceData(indexes);
                            for (int i = 0; i < handlers[COMMENT].size(); i++) {
                                if (handlers[COMMENT].get(i) != null) ((IceTagHandler) handlers[COMMENT].get(i)).handleNewData(data);
                            }
                        }
                    }
                    tagStart[0] = 0;
                    tagStop[0] = 0;
                    textStart[0] = index + 1;
                    textStart[1] = startGap;
                    state = NO_TAG;
                    break;
                case TAG_CLOSE_STOP:
                    buffer.flip();
                    dataToCheck = buffer.array();
                    typeOfTag = checkTag(dataToCheck, buffer.position(), buffer.limit());
                    buffer.limit(buffer.capacity());
                    if (typeOfTag >= 0 && requirement[typeOfTag]) {
                        try {
                            if (typeOfTag == IMAGE) {
                                if (handlers[IMAGE] != null) {
                                    int[] indexes = new int[4];
                                    indexes[0] = tagStart[0] + tagStart[1];
                                    indexes[1] = tagStop[0] + tagStop[1];
                                    indexes[2] = -1;
                                    indexes[3] = -1;
                                    IceData data = new IceData(indexes);
                                    for (int i = 0; i < handlers[IMAGE].size(); i++) {
                                        if (handlers[IMAGE].get(i) != null) ((IceTagHandler) handlers[IMAGE].get(i)).handleNewData(data);
                                    }
                                }
                            } else {
                                int[] indexes = (int[]) tagQueue[typeOfTag].removeLast();
                                if (indexes != null) {
                                    indexes[2] = tagStart[0] + tagStart[1];
                                    indexes[3] = tagStop[0] + tagStop[1];
                                    if (handlers[typeOfTag] != null) {
                                        IceData data = new IceData(indexes);
                                        for (int i = 0; i < handlers[typeOfTag].size(); i++) {
                                            if (handlers[typeOfTag].get(i) != null) ((IceTagHandler) handlers[typeOfTag].get(i)).handleNewData(data);
                                        }
                                    }
                                } else {
                                    if (Logger.isLoggingError()) Logger.eLog("Oups. Close tag with no start : " + new String(xmlData, tagStart[0], tagStop[0] - tagStart[0] + 1));
                                }
                            }
                        } catch (RuntimeException e) {
                            if (Logger.isLoggingError()) Logger.eLog("Oups. Close tag with no start : " + new String(xmlData, tagStart[0], tagStop[0] - tagStart[0] + 1) + "" + Arrays.toString(tagStart));
                        }
                    }
                    tagStart[0] = 0;
                    tagStop[0] = 0;
                    textStart[0] = index + 1;
                    textStart[1] = startGap;
                    state = NO_TAG;
                    break;
                default:
                    break;
            }
        }
        startGap += length;
        if (resetOnFinish) {
            reset();
        }
    }

    /**
	 * Check the type of tag.
	 * 
	 * @param data
	 * @param start
	 * @param stop
	 * @return cf. type of tag.
	 */
    private int checkTag(byte[] data, int start, int stop) {
        int type = TAG_NONE;
        int wsIndex = start + 1;
        for (int i = start; i <= stop; i++) {
            if (Character.isWhitespace((char) data[i]) || ((char) data[i]) == '>') {
                wsIndex = i;
                break;
            }
        }
        if (wsIndex > start + 1) {
            if (state == TAG_OPEN_STOP) {
                String tag = new String(data, start + 1, wsIndex - start - 1);
                if (tag.equalsIgnoreCase(IMG_TAG_START)) {
                    type = IMAGE;
                } else if (tag.equalsIgnoreCase(P_TAG_START)) {
                    type = P;
                } else if (tag.equalsIgnoreCase(A_TAG_START)) {
                    type = A;
                } else if (tag.equalsIgnoreCase(SCRIPT_TAG_START)) {
                    type = SCRIPT;
                } else if (tag.equalsIgnoreCase(FORM_TAG_START)) {
                    type = FORM;
                } else if (tag.equalsIgnoreCase(DIV_TAG_START)) {
                    type = DIV;
                }
            } else if (state == TAG_CLOSE_STOP) {
                String tag = new String(data, start + 1, wsIndex - start - 1);
                if (tag.equalsIgnoreCase(IMG_TAG_START)) {
                    type = IMAGE;
                } else if (tag.equalsIgnoreCase(P_TAG_STOP)) {
                    type = P;
                } else if (tag.equalsIgnoreCase(A_TAG_STOP)) {
                    type = A;
                } else if (tag.equalsIgnoreCase(SCRIPT_TAG_STOP)) {
                    type = SCRIPT;
                } else if (tag.equalsIgnoreCase(FORM_TAG_STOP)) {
                    type = FORM;
                } else if (tag.equalsIgnoreCase(DIV_TAG_STOP)) {
                    type = DIV;
                }
            }
        }
        return type;
    }
}
