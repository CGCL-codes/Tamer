package mobilesearch.ui;

import java.util.ArrayList;
import mobilesearch.data.*;
import mobilesearch.receiver.*;
import mobilesearch.service.MobileSearchService;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MobileSearchEngine implements OnSearchResultListener {

    private static final String TAG = "MobileSearchEngine";

    public static final String FIRST_ITEM = "first";

    public static final String SECOND_ITEM = "second";

    private Context context;

    private BroadcastReceiver broadcastReceiver;

    private List<Map<String, String>> appList;

    private List<Map<String, String>> audioList;

    private List<Map<String, String>> bookmarkList;

    private List<Map<String, String>> contactsList;

    private List<Map<String, String>> imageList;

    private List<Map<String, String>> smsList;

    private List<Map<String, String>> videoList;

    private List<Map<String, String>> callLogList;

    private List<Map<String, String>> emailList;

    private List<Map<String, String>> fileList;

    private List<Map<String, String>> alarmList;

    public MobileSearchEngine(Context context) {
        this.context = context;
        createBroadcastReceiver();
        appList = new ArrayList<Map<String, String>>();
        audioList = new ArrayList<Map<String, String>>();
        bookmarkList = new ArrayList<Map<String, String>>();
        contactsList = new ArrayList<Map<String, String>>();
        imageList = new ArrayList<Map<String, String>>();
        smsList = new ArrayList<Map<String, String>>();
        videoList = new ArrayList<Map<String, String>>();
        callLogList = new ArrayList<Map<String, String>>();
        emailList = new ArrayList<Map<String, String>>();
        fileList = new ArrayList<Map<String, String>>();
        alarmList = new ArrayList<Map<String, String>>();
    }

    private void createBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive");
                String command = intent.getAction();
                if (command.compareTo(BroadcastCommand.SEARCH_ALARM) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_ALARM) == 0");
                    ArrayList<AlarmData> alarmData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_ALARM);
                    AlarmReceiver alarmReceiver = new AlarmReceiver(alarmData, MobileSearchEngine.this);
                    alarmReceiver.processAlarmData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_APP) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_APP) == 0");
                    ArrayList<AppData> appData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_APP);
                    AppReceiver appReceiver = new AppReceiver(appData, MobileSearchEngine.this);
                    appReceiver.processAppData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_BOOKMARK) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_BOOKMARK) == 0");
                    ArrayList<BookmarkData> bookmarkData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_BOOKMARK);
                    BookmarkReceiver bookmarkReceiver = new BookmarkReceiver(bookmarkData, MobileSearchEngine.this);
                    bookmarkReceiver.processBookmarkData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_CALLLOG) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_CALLLOG) == 0");
                    ArrayList<CallLogData> callLogData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_CALLLOG);
                    CallLogReceiver callLogReceiver = new CallLogReceiver(callLogData, MobileSearchEngine.this);
                    callLogReceiver.processCallLogData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_CONTACTS) == 0) {
                    ArrayList<ContactsData> contactsData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_CONTACTS);
                    ContactsReceiver contactsReceiver = new ContactsReceiver(contactsData, MobileSearchEngine.this);
                    contactsReceiver.processContactsData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_SMS) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_SMS) == 0");
                    ArrayList<SmsData> smsData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_SMS);
                    SmsReceiver smsReceiver = new SmsReceiver(smsData, MobileSearchEngine.this);
                    smsReceiver.processSmsData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_AUDIO) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_AUDIO) == 0");
                    ArrayList<AudioData> audioData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_AUDIO);
                    AudioReceiver audioReceiver = new AudioReceiver(audioData, MobileSearchEngine.this);
                    audioReceiver.processAudioData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_IMAGE) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_IMAGE) == 0");
                    ArrayList<ImageData> imageData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_IMAGE);
                    ImageReceiver imageReceiver = new ImageReceiver(imageData, MobileSearchEngine.this);
                    imageReceiver.processImageData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_VIDEO) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_VIDEO) == 0");
                    ArrayList<VideoData> videoData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_VIDEO);
                    VideoReceiver videoReceiver = new VideoReceiver(videoData, MobileSearchEngine.this);
                    videoReceiver.processVideoData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_EMAIL) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_EMAIL) == 0");
                    ArrayList<EmailData> emailData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_EMAIL);
                    EmailReceiver emailReceiver = new EmailReceiver(emailData, MobileSearchEngine.this);
                    emailReceiver.processEmailData();
                }
                if (command.compareTo(BroadcastCommand.SEARCH_FILE) == 0) {
                    Log.i(TAG, "command.compareTo(BroadcastCommand.SEARCH_FILE) == 0");
                    ArrayList<FileData> fileData = intent.getParcelableArrayListExtra(BroadcastCommand.SEARCH_FILE);
                    FileReceiver fileReceiver = new FileReceiver(fileData, MobileSearchEngine.this);
                    fileReceiver.processFileData();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastCommand.SEARCH_ALARM);
        filter.addAction(BroadcastCommand.SEARCH_APP);
        filter.addAction(BroadcastCommand.SEARCH_AUDIO);
        filter.addAction(BroadcastCommand.SEARCH_CALENDAR);
        filter.addAction(BroadcastCommand.SEARCH_CONTACTS);
        filter.addAction(BroadcastCommand.SEARCH_EMAIL);
        filter.addAction(BroadcastCommand.SEARCH_IMAGE);
        filter.addAction(BroadcastCommand.SEARCH_CALLLOG);
        filter.addAction(BroadcastCommand.SEARCH_MMS);
        filter.addAction(BroadcastCommand.SEARCH_SMS);
        filter.addAction(BroadcastCommand.SEARCH_VIDEO);
        filter.addAction(BroadcastCommand.SEARCH_BOOKMARK);
        filter.addAction(BroadcastCommand.SEARCH_FILE);
        this.context.registerReceiver(broadcastReceiver, filter);
    }

    public void startSearch(String keyword) {
        Intent intent = new Intent(this.context, MobileSearchService.class);
        Bundle bundle = new Bundle();
        ArrayList<String> commandList = new ArrayList<String>();
        commandList.add(BroadcastCommand.SEARCH_APP);
        commandList.add(BroadcastCommand.SEARCH_AUDIO);
        commandList.add(BroadcastCommand.SEARCH_BOOKMARK);
        commandList.add(BroadcastCommand.SEARCH_CONTACTS);
        commandList.add(BroadcastCommand.SEARCH_IMAGE);
        commandList.add(BroadcastCommand.SEARCH_SMS);
        commandList.add(BroadcastCommand.SEARCH_VIDEO);
        commandList.add(BroadcastCommand.SEARCH_CALLLOG);
        commandList.add(BroadcastCommand.SEARCH_EMAIL);
        commandList.add(BroadcastCommand.SEARCH_FILE);
        commandList.add(BroadcastCommand.SEARCH_ALARM);
        bundle.putStringArrayList(BroadcastCommand.SEARCH_COMMAND, commandList);
        intent.putExtras(bundle);
        this.context.startService(intent);
    }

    @Override
    public void onReceiveAppData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveAppData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            appList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveAudioData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveAudioData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            audioList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveContactsData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveContactsData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            contactsList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveImageData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveImageData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            imageList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveSmsData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveSmsData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            smsList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveVideoData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveVideoData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            videoList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveBookmarkData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveBookmarkData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            bookmarkList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveEmailData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveEmailData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            emailList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveFileData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveFileData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            fileList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveCallLogData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveCallLogData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            callLogList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    @Override
    public void onReceiveAlarmData(String firstItem, String secondItem) {
        Log.i(TAG, "onReceiveAlarmData");
        try {
            Map<String, String> item = new HashMap<String, String>();
            item.put(MobileSearchEngine.FIRST_ITEM, firstItem);
            item.put(MobileSearchEngine.SECOND_ITEM, secondItem);
            alarmList.add(item);
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
    }

    public List<Map<String, String>> getAppList() {
        return appList;
    }

    public List<Map<String, String>> getAudioList() {
        return audioList;
    }

    public List<Map<String, String>> getBookmarkList() {
        return bookmarkList;
    }

    public List<Map<String, String>> getContactsList() {
        return contactsList;
    }

    public List<Map<String, String>> getImageList() {
        return imageList;
    }

    public List<Map<String, String>> getSmsList() {
        return smsList;
    }

    public List<Map<String, String>> getCallLogList() {
        return callLogList;
    }

    public List<Map<String, String>> getEmailList() {
        return emailList;
    }

    public List<Map<String, String>> getFileList() {
        return fileList;
    }

    public List<Map<String, String>> getVideoList() {
        return videoList;
    }

    public List<Map<String, String>> getAlarmList() {
        return alarmList;
    }
}
