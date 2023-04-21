package com.okythoos.vmidi;

import com.okythoos.utils.ByteUtils;
import com.okythoos.utils.UIUtils;

/**
 * Object is used to Parse the MIDI Header and Data chunk
 * <p>
 * This object is not to used, this is used internally by the sequencer
 * @see VMidiSequencer
 */
public class VMidiParser implements VMidiConst {

    /**
     * Current track that is being parsed
     */
    private int currTrack = 0;

    /**
     * Current byte being parsed
     */
    private int currBytePos = 0;

    /**
     * Returns the current byte that is being parsed from the file
     */
    public long getCurrBytePos() {
        return currBytePos;
    }

    /**
     * Provides the byte position in the MIDI that is currently parsed
     */
    public void setCurrBytePos(int currBytePos) {
        this.currBytePos = currBytePos;
    }

    /**
     * Retrieves current track being parsed
     * @return current track being parsed
     */
    public int getCurrTrack() {
        return currTrack;
    }

    /**
     * Sets the current track to be parsed (do not use)
     */
    public void setCurrTrack(int currTrack) {
        this.currTrack = currTrack;
    }

    /**
     * Constructs a MIDI parser
     */
    public VMidiParser() {
    }

    /**
     * Returns the description for an event type
     */
    public static String getEventTypeName(int eventType) {
        switch(eventType) {
            case META_EVENT:
                return META_EVENT_NAME;
            case SYS_EX:
                return SYS_EX_NAME;
            case NOTE_OFF:
                return NOTE_OFF_NAME;
            case NOTE_ON:
                return NOTE_ON_NAME;
            case NOTE_AFTERTOUCH:
                return NOTE_AFTERTOUCH_NAME;
            case CONTROLLER:
                return CONTROLLER_NAME;
            case PROGRAM_CHANGE:
                return PROGRAM_CHANGE_NAME;
            case CHANNEL_AFTERTOUCH:
                return CHANNEL_AFTERTOUCH_NAME;
            case PITCH_BEND:
                return PITCH_BEND_NAME;
            default:
                return null;
        }
    }

    /**
     * Returns the description for an meta event type
     */
    public static String getMetaEventTypeName(int metaEventType) {
        switch(metaEventType) {
            case SEQ_NUM:
                return SEQ_NUM_NAME;
            case COPYRIGHT_NOTICE:
                return COPYRIGHT_NOTICE_NAME;
            case TRACK_NAME:
                return TRACK_NAME_NAME;
            case END_OF_TRACK:
                return END_OF_TRACK_NAME;
            case SET_TEMPO:
                return SET_TEMPO_NAME;
            case TIME_SIGNATURE:
                return TIME_SIGNATURE_NAME;
            case KEY_SIGNATURE:
                return KEY_SIGNATURE_NAME;
            default:
                return null;
        }
    }

    /**
     * Returns a Header object after parsing the midi file contents.
     * <p>
     * @see VMidiHeader, VMidiFile
     */
    public VMidiHeader parseHeader(VMidiFile midifile) {
        byte[] timeDivisionArr = ByteUtils.copy(midifile.getMidiFileContents(), 12, 2);
        int timeDivisionIntArr[] = { timeDivisionArr[0] & 0xFF, timeDivisionArr[1] & 0xFF };
        if (DEBUG_PARSER == true) {
            UIUtils.log("Time Division Bytes: " + Integer.toBinaryString(timeDivisionIntArr[0]) + ":" + Integer.toBinaryString(timeDivisionIntArr[1]));
        }
        long ticksPerBeat = 0;
        long SMPTE = 0;
        long ticksPerFrame = 0;
        int timeDivisionMode = timeDivisionIntArr[0] & 0x80;
        timeDivisionIntArr[0] &= 0x7F;
        if (timeDivisionMode == TICKS_PER_BEAT) {
            ticksPerBeat = ByteUtils.wordToInt(timeDivisionIntArr);
        } else if (timeDivisionMode == FRAMES_PER_SECOND) {
            SMPTE = timeDivisionIntArr[0];
            ticksPerFrame = timeDivisionIntArr[1];
        }
        VMidiHeader myVMidiHeader = new VMidiHeader(ByteUtils.copy(midifile.getMidiFileContents(), 0, 4), ByteUtils.dwordToInt(ByteUtils.copy(midifile.getMidiFileContents(), 4, 4)), ByteUtils.wordToInt(ByteUtils.copy(midifile.getMidiFileContents(), 8, 2)), ByteUtils.wordToInt(ByteUtils.copy(midifile.getMidiFileContents(), 10, 2)), timeDivisionMode, ticksPerBeat, SMPTE, ticksPerFrame);
        myVMidiHeader.print();
        return myVMidiHeader;
    }

    /**
     * Parses a midi Event and returns a midi event object
     * <p>
     * This object is not to used, this is used internally by the sequencer
     * @see VMidiSequencer
     */
    public VMidiEvent parseEvent(VMidiFile midifile, int eventStart, VMidiEvent previousMidiEvent) {
        VMidiEvent midiEvent = null;
        byte[] data = midifile.getMidiFileContents();
        int k = 0;
        int[] VLVlength = new int[1];
        long deltaTimeInt = 0;
        deltaTimeInt = ByteUtils.getVLV(data, eventStart, VLVlength);
        k += VLVlength[0];
        byte eventTypeValue = data[eventStart + k];
        int eventTypeByte = eventTypeValue & FIRST_BYTE_MASK;
        int eventType = 0;
        int midiChannel = 0;
        int eventParam1 = 0;
        int eventParam2 = 0;
        int timeSigNom = 0;
        int timeSigDenom = 0;
        int timeSigMetro = 0;
        int timeSig32s = 0;
        byte metaEventType = 0;
        byte metaEventLength = 0;
        byte[] metaEventData = null;
        int[] metaEventDataInt = null;
        String metaEventDataStr = null;
        switch(eventTypeByte) {
            case 0xFF:
                {
                    eventType = eventTypeByte;
                    metaEventType = data[eventStart + ++k];
                    metaEventLength = data[eventStart + ++k];
                    metaEventData = ByteUtils.copy(data, eventStart + ++k, metaEventLength);
                    metaEventDataInt = new int[metaEventLength];
                    for (int i = 0; i < metaEventLength; i++) {
                        metaEventDataInt[i] = (metaEventData[i] & 0xFF);
                    }
                    switch(metaEventType) {
                        case 81:
                            metaEventDataStr = "Tempo: " + ByteUtils.toHex(metaEventData) + ":" + ByteUtils.byteArrayToInt(metaEventData, metaEventLength) + ":" + ByteUtils.toHex(metaEventDataInt) + ":" + ByteUtils.intArrayToInt(metaEventDataInt, metaEventLength);
                            break;
                        case 88:
                            timeSigNom = metaEventData[0] & 0xFF;
                            timeSigDenom = ByteUtils.pow(2, metaEventData[1] & 0xFF);
                            timeSigMetro = metaEventData[2] & 0xFF;
                            timeSig32s = metaEventData[3] & 0xFF;
                            metaEventDataStr = "Time Signature: " + ByteUtils.toHex(metaEventData) + ";" + timeSigNom + "/" + timeSigDenom + ";" + timeSigMetro + ";" + timeSig32s;
                            break;
                        case 89:
                            String Scale = null;
                            if (metaEventData[1] == 0) {
                                Scale = "Maj";
                            } else if (metaEventData[1] == 1) {
                                Scale = "Min";
                            }
                            String key = null;
                            if (metaEventData[0] < 0) {
                                key = "b" + Integer.toString(metaEventData[0]);
                            } else if (metaEventData[0] > 0) {
                                key = "#" + Integer.toString(metaEventData[0]);
                            }
                            metaEventDataStr = "Key Signature: " + key + ":" + Scale;
                            break;
                        case 47:
                            metaEventDataStr = "End of Track";
                            break;
                        case 0:
                            metaEventDataStr = "Sequence Number: MSB: " + metaEventData[0] + ",LSB: " + metaEventData[1];
                            break;
                        case 1:
                            metaEventDataStr = ByteUtils.ByteArrayToString(metaEventData);
                            break;
                        case 2:
                            metaEventDataStr = "Copyright: " + ByteUtils.ByteArrayToString(metaEventData);
                            break;
                        case 3:
                            metaEventDataStr = ByteUtils.ByteArrayToString(metaEventData);
                            break;
                    }
                    if (DEBUG_PARSER == true) {
                        UIUtils.log(deltaTimeInt + "," + ByteUtils.toHex((byte) eventTypeByte) + ";" + ",Meta,,,, " + metaEventType + "," + metaEventLength + "," + metaEventDataStr);
                    }
                    k += metaEventLength;
                    break;
                }
            default:
                {
                    eventType = eventTypeByte / 16;
                    String eventTypeStr = eventType + ";" + ByteUtils.toHex((byte) eventType);
                    if (eventType < 8 && previousMidiEvent != null) {
                        eventType = previousMidiEvent.getEventTypeValue();
                        midiChannel = previousMidiEvent.getMidiChannel();
                        k--;
                    } else {
                        midiChannel = eventTypeByte % 16;
                    }
                    eventParam1 = data[eventStart + ++k];
                    String eventParam1Str = eventParam1 + ";" + ByteUtils.toHex((byte) eventParam1);
                    switch(eventType) {
                        case NOTE_OFF:
                            eventTypeStr += ";" + NOTE_OFF_NAME;
                            eventParam2 = data[eventStart + ++k];
                            break;
                        case NOTE_ON:
                            eventTypeStr += ";" + NOTE_ON_NAME;
                            eventParam2 = data[eventStart + ++k];
                            break;
                        case NOTE_AFTERTOUCH:
                            eventTypeStr += ";" + NOTE_AFTERTOUCH;
                            eventParam2 = data[eventStart + ++k];
                            break;
                        case CONTROLLER:
                            eventTypeStr += ";" + CONTROLLER_NAME;
                            eventParam2 = data[eventStart + ++k];
                            switch(eventParam1) {
                                case (MAIN_VOLUME):
                                    eventParam1Str += ";" + MAIN_VOLUME;
                                    break;
                            }
                            break;
                        case PROGRAM_CHANGE:
                            eventTypeStr += ";" + PROGRAM_CHANGE_NAME;
                            break;
                        case CHANNEL_AFTERTOUCH:
                            eventTypeStr += ";" + CHANNEL_AFTERTOUCH_NAME;
                            break;
                        case PITCH_BEND:
                            eventParam2 = data[eventStart + ++k];
                            eventTypeStr += ";" + PITCH_BEND_NAME;
                            break;
                        default:
                            eventTypeStr += ";Unknown";
                    }
                    if (DEBUG_PARSER == true) {
                        UIUtils.log(deltaTimeInt + "," + eventTypeStr + "," + "Channel," + midiChannel + ";" + ByteUtils.toHex((byte) midiChannel) + ";" + ", " + eventParam1Str + "," + eventParam2 + ";" + Integer.toHexString(eventParam2) + "," + ",,");
                    }
                    k++;
                    break;
                }
        }
        midiEvent = new VMidiEvent(deltaTimeInt, eventType, midiChannel, eventParam1, eventParam2, (long) k, timeSigNom, timeSigDenom, timeSigMetro, timeSig32s, metaEventType, metaEventLength, metaEventDataInt, metaEventDataStr);
        return midiEvent;
    }

    /**
     * Parses midi track data and returns a midi track object
     * <p>
     * This object is not to used, this is used internally by the sequencer
     * @see VMidiSequencer
     */
    public synchronized VMidiTrack parseTrackMulti(VMidiFile midifile, int trackStart, int trackNum) {
        VMidiTrack midiTrack = null;
        byte[] data = midifile.getMidiFileContents();
        byte[] chunkID = ByteUtils.copy(data, trackStart, 4);
        boolean playable = false;
        String trackName = "";
        int totalTicks = 0;
        long trackByteSize = ByteUtils.dwordToInt(ByteUtils.copy(data, trackStart + 4, 4));
        int eventNum = 0;
        VMidiEvent[] midiEvent = new VMidiEvent[(int) trackByteSize];
        if (DEBUG_PARSER == true) {
            UIUtils.log("Track Chunk Id: " + ByteUtils.toHex(chunkID));
            UIUtils.log("TrackSize = " + trackByteSize);
            UIUtils.log("TrackStart: " + trackStart + ", TrackSize: = " + trackByteSize);
        }
        VMidiEvent previousMidiEvent = null;
        VMidiEvent lastChainedMidiEvent = null;
        int programNum = -1;
        int channelNum = 0;
        for (currBytePos = trackStart + 8; currBytePos < trackStart + trackByteSize + 8; ) {
            if (DEBUG_PARSER == true) {
                System.out.print(trackNum + "," + eventNum + "," + currBytePos + ",");
            }
            VMidiEvent midiEventCurr = parseEvent(midifile, currBytePos, previousMidiEvent);
            if (midiEventCurr != null) {
                totalTicks += midiEventCurr.getDeltaTime();
                if (midiEventCurr.getEventTypeValue() == PROGRAM_CHANGE) {
                    programNum = midiEventCurr.getEventParam1();
                    channelNum = midiEventCurr.getMidiChannel();
                }
            }
            if (midiEventCurr != null && (midiEventCurr.getEventTypeValue() == NOTE_ON || midiEventCurr.getEventTypeValue() == NOTE_OFF)) {
                playable = true;
            }
            if (midiEventCurr.getMetaEventType() == TRACK_NAME) {
                trackName = midiEventCurr.metaEventDataStr;
                if (DEBUG_PARSER) {
                    UIUtils.log("Track Name: " + trackName);
                }
            }
            if (midiEventCurr.getDeltaTime() == 0 && lastChainedMidiEvent != null) {
                lastChainedMidiEvent.nextMidiEvent = midiEventCurr;
                lastChainedMidiEvent = midiEventCurr;
            } else {
                midiEvent[eventNum] = midiEventCurr;
                lastChainedMidiEvent = midiEventCurr;
                eventNum++;
            }
            currBytePos += (int) midiEventCurr.getEventSize();
            previousMidiEvent = midiEventCurr;
        }
        midiTrack = new VMidiTrack(chunkID, trackName, trackByteSize, midiEvent, eventNum, playable, totalTicks, programNum, channelNum);
        return midiTrack;
    }

    /**
     * Parses the midi data and returns a midi data object
     * <p>
     * This object is not to used, this is used internally by the sequencer
     * @see VMidiSequencer
     */
    public VMidiData parseData(VMidiFile midifile) {
        VMidiData myVMidiData = null;
        int trackNum = midifile.getMidiHeader().getTracksNum();
        VMidiTrack[] midiTracks = new VMidiTrack[trackNum];
        int trackStart = 14;
        int playableTracksNum = 0;
        if (DEBUG_PARSER == true) {
            UIUtils.log("Track #, Event #, BytePos, Delta Time, Event Type, Event Type Group, Midi Channel, Event Param1, Event Param2, Meta Event Type, Meta Event Length, Meta Event Data");
        }
        for (currTrack = 0; currTrack < trackNum; currTrack++) {
            System.gc();
            VMidiTrack currMidiTrack = parseTrackMulti(midifile, trackStart, currTrack);
            midiTracks[playableTracksNum++] = currMidiTrack;
            trackStart += (currMidiTrack.getChunkSize() + 8);
        }
        myVMidiData = new VMidiData(midiTracks, playableTracksNum);
        return myVMidiData;
    }
}
