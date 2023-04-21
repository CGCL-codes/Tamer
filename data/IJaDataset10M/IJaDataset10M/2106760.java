package app;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings("serial")
public class MarkList extends DefaultComboBoxModel {

    public MarkList() {
        addElement("");
    }

    public void setConfString(String str) {
        String[] list = str.split(",");
        for (String s : list) {
            if (s.length() != 0) {
                addElement(s);
            }
        }
    }

    public String getConfString() {
        int n = getSize();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            String name = (String) getElementAt(i);
            if (name.equals("")) {
                continue;
            }
            if (str.length() != 0) {
                str.append(",");
            }
            str.append(name);
        }
        return str.toString();
    }

    public void add(String user) {
        if (find(user) == -1) {
            System.out.println(this.getClass() + ":add:" + user);
            addElement(user);
        }
    }

    public void remove(String user) {
        int index = find(user);
        if (index != -1) {
            System.out.println(this.getClass() + ":remove:" + user);
            removeElementAt(index);
        }
    }

    private int find(String user) {
        int n = getSize();
        for (int i = 0; i < n; ++i) {
            String name = (String) getElementAt(i);
            if (user.equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
