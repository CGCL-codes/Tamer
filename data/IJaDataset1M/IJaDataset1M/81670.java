package hu.sztaki.lpds.pgportal.wfeditor.client.utils;

/**
 *
 * @author  boci
 */
public class ResourceItem {

    private String contactString, jobManager, comboBoxItem;

    private boolean isMonitored;

    public ResourceItem(String pContactString, String pJobManager, String pIsMonitored) {
        comboBoxItem = new String("");
        contactString = pContactString;
        jobManager = pJobManager;
        if (pIsMonitored.equals("true")) isMonitored = true; else if (pIsMonitored.equals("false")) isMonitored = false;
        setComboBoxItem();
    }

    private void setComboBoxItem() {
        if (contactString.equals("")) comboBoxItem = "Automatic"; else {
            if (jobManager.equals("")) {
                comboBoxItem = contactString;
            } else {
                comboBoxItem = contactString + ":/" + jobManager;
            }
        }
    }

    public void setContactString(String cs) {
        contactString = cs;
    }

    public void setJobManager(String jm) {
        jobManager = jm;
    }

    public boolean getIsMonitored() {
        return isMonitored;
    }

    public String getContactString() {
        return contactString;
    }

    public String getComboBoxItem() {
        return comboBoxItem;
    }
}
