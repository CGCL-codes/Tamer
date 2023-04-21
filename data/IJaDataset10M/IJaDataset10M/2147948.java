package org.marre.sms.nokia;

/**
 * Used in NokiaMultipartMessages
 *
 * @author Markus Eriksson
 * @version $Id: NokiaPart.java 410 2006-03-13 19:48:31Z c95men $
 */
class NokiaPart {

    static final byte ITEM_TEXT_ISO_8859_1 = 0x00;

    static final byte ITEM_TEXT_UNICODE = 0x01;

    static final byte ITEM_OTA_BITMAP = 0x02;

    static final byte ITEM_RINGTONE = 0x03;

    static final byte ITEM_PROFILE_NAME = 0x04;

    static final byte ITEM_SCREEN_SAVER = 0x06;

    private byte itemType_;

    private byte[] itemData_;

    /**
     *
     * @param itemType
     * @param itemData
     */
    NokiaPart(byte itemType, byte[] itemData) {
        itemType_ = itemType;
        itemData_ = itemData;
    }

    /**
     *
     * @return
     */
    byte getItemType() {
        return itemType_;
    }

    /**
     *
     * @return
     */
    byte[] getData() {
        return itemData_;
    }
}
