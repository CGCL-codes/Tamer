package vavi.sound.smaf.chunk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import vavi.sound.smaf.InvalidSmafDataException;
import vavi.sound.smaf.SmafMessage;
import vavi.sound.smaf.SysexMessage;
import vavi.sound.smaf.message.BankSelectMessage;
import vavi.sound.smaf.message.EndOfSequenceMessage;
import vavi.sound.smaf.message.ExpressionMessage;
import vavi.sound.smaf.message.MidiConvertibleMessage;
import vavi.sound.smaf.message.ModulationMessage;
import vavi.sound.smaf.message.NopMessage;
import vavi.sound.smaf.message.NoteMessage;
import vavi.sound.smaf.message.OctaveShiftMessage;
import vavi.sound.smaf.message.PanMessage;
import vavi.sound.smaf.message.PitchBendMessage;
import vavi.sound.smaf.message.ProgramChangeMessage;
import vavi.sound.smaf.message.UndefinedMessage;
import vavi.sound.smaf.message.VolumeMessage;
import vavi.util.Debug;
import vavi.util.StringUtil;

/**
 * SequenceData Chunk.
 * <pre>
 * "Mtsq"
 * </pre>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041227 nsano initial version <br>
 */
public class SequenceDataChunk extends Chunk {

    /** */
    public SequenceDataChunk(byte[] id, int size) {
        super(id, size);
        Debug.println("SequenceData: " + size + " bytes");
    }

    /** */
    public SequenceDataChunk() {
        System.arraycopy("Mtsq".getBytes(), 0, id, 0, 4);
        this.size = 0;
    }

    /** TODO how to get formatType from parent chunk ??? */
    protected void init(InputStream is, Chunk parent) throws InvalidSmafDataException, IOException {
        ScoreTrackChunk.FormatType formatType = ((TrackChunk) parent).getFormatType();
        switch(formatType) {
            case HandyPhoneStandard:
                readHandyPhoneStandard(is);
                break;
            case MobileStandard_Compress:
                readMobileStandard(new HuffmanDecodingInputStream(is));
                break;
            case MobileStandard_NoCompress:
                readMobileStandard(is);
                break;
        }
        Debug.println("messages: " + messages.size());
    }

    /**
     * internal use
     * Mtsq �̏ꍇ
     * @param gateTime should not be 0
     */
    protected SmafMessage getHandyPhoneStandardMessage(int duration, int data, int gateTime) {
        return new NoteMessage(duration, data, gateTime);
    }

    /** formatType 0 */
    protected void readHandyPhoneStandard(InputStream is) throws InvalidSmafDataException, IOException {
        SmafMessage smafMessage = null;
        while (available() > 0) {
            int duration = readOneToTwo(is);
            int e1 = read(is);
            if (e1 == 0xff) {
                int e2 = read(is);
                switch(e2) {
                    case 0xf0:
                        int messageSize = read(is);
                        byte[] data = new byte[messageSize];
                        read(is, data);
                        smafMessage = SysexMessage.Factory.getSysexMessage(duration, data);
                        break;
                    case 0x00:
                        smafMessage = new NopMessage(duration);
                        break;
                    default:
                        smafMessage = new UndefinedMessage(duration);
                        Debug.println("unknown 0xff, 0x" + StringUtil.toHex2(e2));
                        break;
                }
            } else if (e1 != 0x00) {
                int gateTime = readOneToTwo(is);
                smafMessage = getHandyPhoneStandardMessage(duration, e1, gateTime);
            } else {
                int e2 = read(is);
                if (e2 == 0x00) {
                    int e3 = read(is);
                    if (e3 == 0x00) {
                        smafMessage = new EndOfSequenceMessage(duration);
                    } else {
                        smafMessage = new UndefinedMessage(duration);
                        Debug.println("unknown 0x00, 0x00, 0x" + StringUtil.toHex2(e3));
                    }
                } else {
                    int channel = (e2 & 0xc0) >> 6;
                    int event = (e2 & 0x30) >> 4;
                    int data = e2 & 0x0f;
                    switch(event) {
                        case 3:
                            int value = read(is);
                            switch(data) {
                                case 0:
                                    smafMessage = new ProgramChangeMessage(duration, channel, value);
                                    break;
                                case 1:
                                    smafMessage = new BankSelectMessage(duration, channel, value);
                                    break;
                                case 2:
                                    smafMessage = new OctaveShiftMessage(duration, channel, value);
                                    break;
                                case 3:
                                    smafMessage = new ModulationMessage(duration, channel, value);
                                    break;
                                case 4:
                                    smafMessage = new PitchBendMessage(duration, channel, value << 7);
                                    break;
                                case 7:
                                    smafMessage = new VolumeMessage(duration, channel, value);
                                    break;
                                case 0x0a:
                                    smafMessage = new PanMessage(duration, channel, value);
                                    break;
                                case 0x0b:
                                    smafMessage = new ExpressionMessage(duration, channel, value);
                                    break;
                                default:
                                    smafMessage = new UndefinedMessage(duration);
                                    Debug.println("unknown 0x00, 0x" + StringUtil.toHex2(e2) + ", 3, " + StringUtil.toHex2(data));
                                    break;
                            }
                            break;
                        case 2:
                            smafMessage = new ModulationMessage(duration, channel, modulationTable[data]);
                            break;
                        case 1:
                            smafMessage = new PitchBendMessage(duration, channel, (data * 8) << 7);
                            break;
                        case 0:
                            smafMessage = new ExpressionMessage(duration, channel, data == 1 ? 0 : data * 8 + 15);
                            break;
                    }
                }
            }
            if (smafMessage != null) {
                messages.add(smafMessage);
            } else {
                assert false : "smafMessage is null";
            }
        }
    }

    /** for HandyPhoneStandard short */
    protected static final int[] modulationTable = { -1, 0x00, 0x08, 0x10, 0x18, 0x20, 0x28, 0x30, 0x38, 0x40, 0x48, 0x50, 0x60, 0x70, 0x7F, -1 };

    /** formatType 1, 2 */
    private void readMobileStandard(InputStream is) throws InvalidSmafDataException, IOException {
        SmafMessage smafMessage = null;
        while (available() > 0) {
            int duration = readOneToFour(is);
            int status = read(is);
            if (status >= 0x80 && status <= 0x8f) {
                int channel = status & 0x0f;
                int note = read(is);
                int gateTime = readOneToFour(is);
                smafMessage = new NoteMessage(duration, channel, note, gateTime);
            } else if (status >= 0x90 && status <= 0x9f) {
                int channel = status & 0x0f;
                int note = read(is);
                int velocity = read(is);
                int gateTime = readOneToFour(is);
                smafMessage = new NoteMessage(duration, channel, note, gateTime, velocity);
            } else if (status >= 0xa0 && status <= 0xaf) {
                int d1 = read(is);
                int d2 = read(is);
                smafMessage = null;
                Debug.println("reserved: 0xa_: " + StringUtil.toHex2(d1) + StringUtil.toHex2(d2));
            } else if (status >= 0xb0 && status <= 0xbf) {
                int channel = status & 0x0f;
                int control = read(is);
                int value = read(is);
                switch(control) {
                    case 0x00:
                        smafMessage = new BankSelectMessage(duration, channel, value, BankSelectMessage.Significant.Least);
                        break;
                    case 0x20:
                        smafMessage = new BankSelectMessage(duration, channel, value, BankSelectMessage.Significant.Most);
                        break;
                    case 0x01:
                        smafMessage = new ModulationMessage(duration, channel, value);
                        break;
                    case 0x07:
                        smafMessage = new VolumeMessage(duration, channel, value);
                        break;
                    case 0x0a:
                        smafMessage = new PanMessage(duration, channel, value);
                        break;
                    case 0x0b:
                        smafMessage = new ExpressionMessage(duration, channel, value);
                        break;
                    case 0x06:
                    case 0x26:
                    case 0x40:
                    case 0x47:
                    case 0x4a:
                    case 0x64:
                    case 0x65:
                    case 0x78:
                    case 0x79:
                    case 0x7b:
                    case 0x7e:
                    case 0x7f:
                        smafMessage = new MidiConvertibleMessage(duration, control, channel, value);
                        break;
                    default:
                        smafMessage = new UndefinedMessage(duration);
                        Debug.println("undefined control: " + StringUtil.toHex2(control) + ", " + StringUtil.toHex2(value));
                        break;
                }
            } else if (status >= 0xc0 && status <= 0xcf) {
                int channel = status & 0x0f;
                int program = read(is);
                smafMessage = new ProgramChangeMessage(duration, channel, program);
            } else if (status >= 0xd0 && status <= 0xdf) {
                int d1 = read(is);
                smafMessage = new UndefinedMessage(duration);
                Debug.println("reserved: 0xd_: " + StringUtil.toHex2(d1));
            } else if (status >= 0xe0 && status <= 0xef) {
                int channel = status & 0x0f;
                int lsb = read(is);
                int msb = read(is);
                smafMessage = new PitchBendMessage(duration, channel, (msb << 7) | lsb);
            } else if (status == 0xff) {
                int d1 = read(is);
                switch(d1) {
                    case 0x00:
                        smafMessage = new NopMessage(duration);
                        break;
                    case 0x2f:
                        int d2 = read(is);
                        if (d2 != 0) {
                            Debug.println("illegal state: " + StringUtil.toHex2(d2));
                        }
                        smafMessage = new EndOfSequenceMessage(duration);
                        break;
                    default:
                        smafMessage = new UndefinedMessage(duration);
                        Debug.println("unknown: 0xff: " + StringUtil.toHex2(d1));
                        break;
                }
            } else if (status == 0xf0) {
                int messageSize = readOneToFour(is);
                byte[] data = new byte[messageSize];
                read(is, data);
                smafMessage = SysexMessage.Factory.getSysexMessage(duration, data);
            } else {
                smafMessage = new UndefinedMessage(duration);
                Debug.println("reserved: " + StringUtil.toHex2(status));
            }
            if (smafMessage != null) {
                messages.add(smafMessage);
            } else {
                assert false : "smafMessage is null";
            }
        }
    }

    /** */
    public void writeTo(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.write(id);
        dos.writeInt(size);
        for (SmafMessage message : messages) {
            os.write(message.getMessage());
        }
    }

    /** */
    protected List<SmafMessage> messages = new ArrayList<SmafMessage>();

    /**
     * @return Returns the messages.
     */
    public List<SmafMessage> getSmafMessages() {
        return messages;
    }

    /** */
    public void addSmafMessage(SmafMessage smafMessage) {
        messages.add(smafMessage);
        size += smafMessage.getLength();
    }
}
