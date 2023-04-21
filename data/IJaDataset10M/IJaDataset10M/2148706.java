package hoi.birthdaymgr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;

public class BMgrRecord {

    protected Boolean selected = false;

    protected String name = "";

    protected String birthday = "";

    protected String website = "";

    protected String notes = "";

    protected String time = "";

    public String[] getContents() {
        return new String[] { name, birthday, website, notes, time };
    }

    public void setContents(String[] contents) {
        if (contents.length > 0) name = contents[0];
        if (contents.length > 1) birthday = contents[1];
        if (contents.length > 2) website = contents[2];
        if (contents.length > 3) notes = contents[3];
        if (contents.length > 4) time = contents[4];
    }

    public String toString() {
        return String.format("%s %s %s %s %s %s", selected.toString(), name, birthday, website, notes, time);
    }

    public static BMgrRecord loadFromXMLString(String page) throws IOException {
        Properties props = new Properties();
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(pos));
        pos.connect(pis);
        bWriter.write(page.trim());
        bWriter.close();
        props.loadFromXML(pis);
        pis.close();
        BMgrRecord record = new BMgrRecord();
        record.setName(props.getProperty("name"));
        record.setBirthday(props.getProperty("birthday"));
        record.setWebsite(props.getProperty("website"));
        record.setNotes(props.getProperty("notes"));
        record.setTime(props.getProperty("time"));
        return record;
    }

    public static String saveAsXMLString(BMgrRecord record) throws IOException {
        Properties props = new Properties();
        props.setProperty("name", record.getName());
        props.setProperty("birthday", record.getBirthday());
        props.setProperty("website", record.getWebsite());
        props.setProperty("notes", record.getNotes());
        props.setProperty("time", record.getTime());
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(pis));
        pos.connect(pis);
        props.storeToXML(pos, null);
        pos.close();
        String page = "", line;
        while ((line = bReader.readLine()) != null) page += line.trim() + "\n";
        bReader.close();
        return page.trim();
    }

    public static void main(String[] args) throws IOException {
        BMgrRecord record = new BMgrRecord();
        record.setName("杨全海");
        System.out.println(record.getName());
        record = loadFromXMLString(saveAsXMLString(record));
        System.out.println(record.getName());
    }

    public boolean isEmptyRecord() {
        return name.trim().equals("") && birthday.trim().equals("") && website.trim().equals("") && notes.trim().equals("") && time.trim().equals("");
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
