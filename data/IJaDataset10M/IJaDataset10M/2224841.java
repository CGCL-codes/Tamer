package com.android.mms.ui;

import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.model.TextModel;
import com.android.mms.ui.MessageListAdapter.ColumnsMap;
import com.android.mms.util.AddressUtils;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.MultimediaMessagePdu;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.RetrieveConf;
import com.google.android.mms.pdu.SendReq;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.text.TextUtils;
import android.util.Log;

/**
 * Mostly immutable model for an SMS/MMS message.
 *
 * <p>The only mutable field is the cached formatted message member,
 * the formatting of which is done outside this model in MessageListItem.
 */
public class MessageItem {

    private static String TAG = "MessageItem";

    public enum DeliveryStatus {

        NONE, INFO, FAILED, PENDING, RECEIVED
    }

    final Context mContext;

    final String mType;

    final long mMsgId;

    final int mBoxId;

    DeliveryStatus mDeliveryStatus;

    boolean mReadReport;

    boolean mLocked;

    String mTimestamp;

    String mAddress;

    String mContact;

    String mBody;

    String mHighlight;

    CharSequence mCachedFormattedMessage;

    Uri mMessageUri;

    int mMessageType;

    int mAttachmentType;

    String mSubject;

    SlideshowModel mSlideshow;

    int mMessageSize;

    int mErrorType;

    MessageItem(Context context, String type, Cursor cursor, ColumnsMap columnsMap, String highlight) throws MmsException {
        mContext = context;
        mMsgId = cursor.getLong(columnsMap.mColumnMsgId);
        mHighlight = highlight != null ? highlight.toLowerCase() : null;
        mType = type;
        if ("sms".equals(type)) {
            mReadReport = false;
            long status = cursor.getLong(columnsMap.mColumnSmsStatus);
            if (status == Sms.STATUS_NONE) {
                mDeliveryStatus = DeliveryStatus.NONE;
            } else if (status >= Sms.STATUS_FAILED) {
                mDeliveryStatus = DeliveryStatus.FAILED;
            } else if (status >= Sms.STATUS_PENDING) {
                mDeliveryStatus = DeliveryStatus.PENDING;
            } else {
                mDeliveryStatus = DeliveryStatus.RECEIVED;
            }
            mMessageUri = ContentUris.withAppendedId(Sms.CONTENT_URI, mMsgId);
            mBoxId = cursor.getInt(columnsMap.mColumnSmsType);
            mAddress = cursor.getString(columnsMap.mColumnSmsAddress);
            if (Sms.isOutgoingFolder(mBoxId)) {
                String meString = context.getString(R.string.messagelist_sender_self);
                mContact = meString;
            } else {
                mContact = Contact.get(mAddress, false).getName();
            }
            mBody = cursor.getString(columnsMap.mColumnSmsBody);
            if (!isOutgoingMessage()) {
                long date = cursor.getLong(columnsMap.mColumnSmsDate);
                mTimestamp = String.format(context.getString(R.string.sent_on), MessageUtils.formatTimeStampString(context, date));
            }
            mLocked = cursor.getInt(columnsMap.mColumnSmsLocked) != 0;
        } else if ("mms".equals(type)) {
            mMessageUri = ContentUris.withAppendedId(Mms.CONTENT_URI, mMsgId);
            mBoxId = cursor.getInt(columnsMap.mColumnMmsMessageBox);
            mMessageType = cursor.getInt(columnsMap.mColumnMmsMessageType);
            mErrorType = cursor.getInt(columnsMap.mColumnMmsErrorType);
            String subject = cursor.getString(columnsMap.mColumnMmsSubject);
            if (!TextUtils.isEmpty(subject)) {
                EncodedStringValue v = new EncodedStringValue(cursor.getInt(columnsMap.mColumnMmsSubjectCharset), PduPersister.getBytes(subject));
                mSubject = v.getString();
            }
            mLocked = cursor.getInt(columnsMap.mColumnMmsLocked) != 0;
            long timestamp = 0L;
            PduPersister p = PduPersister.getPduPersister(mContext);
            if (PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND == mMessageType) {
                mDeliveryStatus = DeliveryStatus.NONE;
                NotificationInd notifInd = (NotificationInd) p.load(mMessageUri);
                interpretFrom(notifInd.getFrom(), mMessageUri);
                mBody = new String(notifInd.getContentLocation());
                mMessageSize = (int) notifInd.getMessageSize();
                timestamp = notifInd.getExpiry() * 1000L;
            } else {
                MultimediaMessagePdu msg = (MultimediaMessagePdu) p.load(mMessageUri);
                mSlideshow = SlideshowModel.createFromPduBody(context, msg.getBody());
                mAttachmentType = MessageUtils.getAttachmentType(mSlideshow);
                if (mMessageType == PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF) {
                    RetrieveConf retrieveConf = (RetrieveConf) msg;
                    interpretFrom(retrieveConf.getFrom(), mMessageUri);
                    timestamp = retrieveConf.getDate() * 1000L;
                } else {
                    mContact = mAddress = context.getString(R.string.messagelist_sender_self);
                    timestamp = ((SendReq) msg).getDate() * 1000L;
                }
                String report = cursor.getString(columnsMap.mColumnMmsDeliveryReport);
                if ((report == null) || !mAddress.equals(context.getString(R.string.messagelist_sender_self))) {
                    mDeliveryStatus = DeliveryStatus.NONE;
                } else {
                    int reportInt;
                    try {
                        reportInt = Integer.parseInt(report);
                        if (reportInt == PduHeaders.VALUE_YES) {
                            mDeliveryStatus = DeliveryStatus.INFO;
                        } else {
                            mDeliveryStatus = DeliveryStatus.NONE;
                        }
                    } catch (NumberFormatException nfe) {
                        Log.e(TAG, "Value for delivery report was invalid.");
                        mDeliveryStatus = DeliveryStatus.NONE;
                    }
                }
                report = cursor.getString(columnsMap.mColumnMmsReadReport);
                if ((report == null) || !mAddress.equals(context.getString(R.string.messagelist_sender_self))) {
                    mReadReport = false;
                } else {
                    int reportInt;
                    try {
                        reportInt = Integer.parseInt(report);
                        mReadReport = (reportInt == PduHeaders.VALUE_YES);
                    } catch (NumberFormatException nfe) {
                        Log.e(TAG, "Value for read report was invalid.");
                        mReadReport = false;
                    }
                }
                SlideModel slide = mSlideshow.get(0);
                if ((slide != null) && slide.hasText()) {
                    TextModel tm = slide.getText();
                    if (tm.isDrmProtected()) {
                        mBody = mContext.getString(R.string.drm_protected_text);
                    } else {
                        mBody = tm.getText();
                    }
                }
                mMessageSize = mSlideshow.getCurrentMessageSize();
            }
            if (!isOutgoingMessage()) {
                mTimestamp = context.getString(getTimestampStrId(), MessageUtils.formatTimeStampString(context, timestamp));
            }
        } else {
            throw new MmsException("Unknown type of the message: " + type);
        }
    }

    private void interpretFrom(EncodedStringValue from, Uri messageUri) {
        if (from != null) {
            mAddress = from.getString();
        } else {
            mAddress = AddressUtils.getFrom(mContext, messageUri);
        }
        mContact = TextUtils.isEmpty(mAddress) ? "" : Contact.get(mAddress, false).getName();
    }

    private int getTimestampStrId() {
        if (PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND == mMessageType) {
            return R.string.expire_on;
        } else {
            return R.string.sent_on;
        }
    }

    public boolean isMms() {
        return mType.equals("mms");
    }

    public boolean isSms() {
        return mType.equals("sms");
    }

    public boolean isDownloaded() {
        return (mMessageType != PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND);
    }

    public boolean isOutgoingMessage() {
        boolean isOutgoingMms = isMms() && (mBoxId == Mms.MESSAGE_BOX_OUTBOX);
        boolean isOutgoingSms = isSms() && ((mBoxId == Sms.MESSAGE_TYPE_FAILED) || (mBoxId == Sms.MESSAGE_TYPE_OUTBOX) || (mBoxId == Sms.MESSAGE_TYPE_QUEUED));
        return isOutgoingMms || isOutgoingSms;
    }

    public void setCachedFormattedMessage(CharSequence formattedMessage) {
        mCachedFormattedMessage = formattedMessage;
    }

    public CharSequence getCachedFormattedMessage() {
        return mCachedFormattedMessage;
    }

    public int getBoxId() {
        return mBoxId;
    }

    @Override
    public String toString() {
        return "type: " + mType + " box: " + mBoxId + " uri: " + mMessageUri + " address: " + mAddress + " contact: " + mContact + " read: " + mReadReport + " delivery status: " + mDeliveryStatus;
    }
}
