package vavi.sound.mfi.vavi.sequencer;

import vavi.sound.mfi.InvalidMfiDataException;
import vavi.sound.mfi.vavi.track.MachineDependMessage;

/**
 * Sub sequencer for machine depend system exclusive message.
 * <p>
 * ���̂Ƃ�������N���X�� bean �łȂ���΂Ȃ�Ȃ��D
 * (��Ȃ��̃R���X�g���N�^�����邱��)
 * {@link #process(MachineDependMessage)} �֘A�̓X�e�[�g���X�łȂ���΂Ȃ�Ȃ��B
 * </p>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030822 nsano initial version <br>
 */
public interface MachineDependFunction {

    final int VENDOR_NEC = 0x10;

    final int VENDOR_FUJITSU = 0x20;

    final int VENDOR_SONY = 0x30;

    final int VENDOR_PANASONIC = 0x40;

    final int VENDOR_NIHONMUSEN = 0x50;

    final int VENDOR_MITSUBISHI = 0x60;

    final int VENDOR_SHARP = 0x70;

    final int VENDOR_SANYO = 0x80;

    final int VENDOR_MOTOROLA = 0x90;

    final int CARRIER_AU = 0x00;

    final int CARRIER_DOCOMO = 0x01;

    /** */
    void process(MachineDependMessage message) throws InvalidMfiDataException;
}
