package com.maptales.mobile.ui;

import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStoreException;
import com.maptales.mobile.MaptalesMobileApp;
import com.maptales.mobile.bus.Post;
import com.maptales.mobile.bus.Story;
import com.maptales.mobile.util.Helpers;

public class PostEditor extends Form {

    MaptalesMobileApp app;

    TextField titleField;

    TextField textField;

    TextField tagsField;

    ChoiceGroup loggingChoice;

    ChoiceGroup locationChoice;

    ChoiceGroup storyChoice;

    CommandListener commandListener = null;

    Post currentPost = null;

    public static final Command CMD_CANCEL = new Command("Cancel", Command.CANCEL, 2);

    public static final Command CMD_STORE = new Command("Store", Command.OK, 2);

    public static final Command CMD_CREATE = new Command("Store", Command.OK, 2);

    public static final Command CMD_CHANGE_STORY = new Command("Change Story", Command.OK, 2);

    public PostEditor(MaptalesMobileApp app) {
        super(null);
        this.app = app;
        initialize();
    }

    protected void initialize() {
        titleField = new TextField("Title", "", 100, TextField.ANY);
        textField = new TextField("Text", "", 200, TextField.ANY);
        tagsField = new TextField("Tags", "", 100, TextField.ANY);
        append(titleField);
        append(textField);
        append(tagsField);
        storyChoice = new ChoiceGroup("Story", Choice.POPUP);
        append(storyChoice);
        locationChoice = new ChoiceGroup("Location", Choice.POPUP);
        append(locationChoice);
        loggingChoice = new ChoiceGroup(null, Choice.MULTIPLE);
        append(loggingChoice);
        addCommand(CMD_CANCEL);
    }

    public void setCommandListener(CommandListener l) {
        super.setCommandListener(l);
        this.commandListener = l;
    }

    public PostEditor update(Post post) {
        this.currentPost = post;
        if (post != null) {
            titleField.setString(post.getTitle());
            textField.setString(post.getText());
            String[] tags = post.getTags();
            if (tags.length > 0) {
                StringBuffer tagsBuf = new StringBuffer();
                for (int i = 0; i < tags.length; i++) {
                    tagsBuf.append(tags[i]);
                }
                tagsField.setString(tagsBuf.toString());
            } else {
                tagsField.setString("");
            }
        } else {
            titleField.setString("");
            textField.setString("");
            tagsField.setString("");
        }
        storyChoice.deleteAll();
        int storyIndex = 0;
        storyChoice.append("[No Story]", null);
        Story[] stories = app.getStories();
        Story currentStory = app.getCurrentStory();
        if (post != null) {
            try {
                currentStory = post.getStory();
            } catch (RecordStoreException e) {
                currentStory = null;
            }
        }
        for (int i = 0; i < stories.length; i++) {
            storyChoice.append(stories[i].getTitle(), null);
            if (stories[i] == currentStory) {
                storyIndex = i + 1;
            }
        }
        storyChoice.setSelectedIndex(storyIndex, true);
        locationChoice.deleteAll();
        if (app.getGPSStatus() == MaptalesMobileApp.GPS_CONNECTED) locationChoice.append("Current Location", null);
        if (app.getLastMarker() != null) {
            String lastPostName = "<empty>";
            if (app.getLastPost() != null) lastPostName = app.getLastPost().getTitle();
            locationChoice.append("Last Location (" + lastPostName + ")", null);
        }
        locationChoice.append("No Location", null);
        loggingChoice.deleteAll();
        if (app.getGPSStatus() == MaptalesMobileApp.GPS_CONNECTED) {
            if (app.isLogging()) {
                loggingChoice.append("Stop logging here", null);
            } else {
                loggingChoice.append("Start logging", null);
            }
        }
        if (post == null) {
            this.addCommand(CMD_CREATE);
            this.removeCommand(CMD_STORE);
        } else {
            this.addCommand(CMD_STORE);
            this.removeCommand(CMD_CREATE);
        }
        return this;
    }

    public Post getEditedPost() {
        if (this.currentPost == null) {
            this.currentPost = new Post();
        }
        String title = titleField.getString().trim();
        if (title.equals("")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            title = "[Quickmark] " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        }
        this.currentPost.setTitle(title);
        this.currentPost.setText(textField.getString());
        this.currentPost.setTags(Helpers.parseTags(tagsField.getString()));
        return this.currentPost;
    }

    public int getLocationMode() {
        int selectedLocation = locationChoice.getSelectedIndex();
        String locString = locationChoice.getString(selectedLocation);
        if (locString.startsWith("No ")) return MaptalesMobileApp.LOCATION_NONE; else if (locString.startsWith("Last ")) return MaptalesMobileApp.LOCATION_LAST;
        return MaptalesMobileApp.LOCATION_CURRENT;
    }

    public boolean getToggleLogging() {
        return (loggingChoice.size() > 0 && loggingChoice.isSelected(0));
    }

    public Story getStory() {
        if (storyChoice.getSelectedIndex() == 0) {
            return null;
        } else {
            return app.getStories()[storyChoice.getSelectedIndex() - 1];
        }
    }
}
