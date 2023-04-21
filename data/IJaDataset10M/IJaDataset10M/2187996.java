package logic;

public class Helper {

    public Helper() {
    }

    public String[] realLength(String[] a) {
        String[] result;
        int i = 0;
        int j;
        while (a[i] != null) {
            i = i + 1;
        }
        result = new String[i];
        for (j = 0; j < i; j++) {
            result[j] = a[j];
        }
        return result;
    }

    public String[] sortSelected(gui.components.CheckTextBox[] a) {
        String[] result;
        String s;
        int i = 0;
        int j = 0;
        for (i = 0; i < a.length; i++) {
            if (a[i].isSelected()) {
                j = j + 1;
            }
        }
        result = new String[j];
        j = 0;
        for (i = 0; i < a.length; i++) {
            if (a[i].isSelected()) {
                if ((a[i].getText().endsWith(".ebuild"))) {
                    result[j] = "/usr/portage" + a[i].getText();
                    j = j + 1;
                } else {
                    s = a[i].getText();
                    if (a[i].getText().indexOf("[") != -1) s = a[i].getText().substring(0, a[i].getText().indexOf("[") - 1);
                    result[j] = "=" + s.substring(s.lastIndexOf(" ") + 1, s.length());
                    j = j + 1;
                }
            }
        }
        return result;
    }

    public String[] getCategories() {
        String[] result = new String[500];
        String line;
        int counter = 0;
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader("/usr/portage/profiles/categories"));
            while ((line = in.readLine()) != null) {
                result[counter] = line.trim();
                counter = counter + 1;
            }
            result = realLength(result);
        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
        return result;
    }

    public boolean search(String[] array, String expression) {
        boolean result = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(expression)) {
                result = true;
            }
        }
        return result;
    }

    public String cleanUp(String s) {
        String theChar = new Character((char) 34).toString();
        s = s.substring(s.indexOf(theChar) + 1, s.length() - 1);
        return s;
    }

    public String getFullEbuildPath(String s) {
        String category, name, version;
        int test, n;
        if (s.indexOf("[") != -1) s = s.substring(0, s.indexOf("[") - 1);
        s = s.substring(s.lastIndexOf(" ") + 1, s.length());
        n = s.indexOf("/");
        category = s.substring(0, n);
        s = s.substring(n + 1);
        n = s.indexOf("-");
        name = s.substring(0, n);
        try {
            test = java.lang.Integer.parseInt(s.substring(n + 1, n + 2));
            version = s.substring(n + 1);
        } catch (java.lang.NumberFormatException e) {
            s = s.substring(n + 1);
            n = s.indexOf("-");
            name = name + "-" + s.substring(0, n);
            try {
                test = java.lang.Integer.parseInt(s.substring(n + 1, n + 2));
                version = s.substring(n + 1);
            } catch (java.lang.NumberFormatException e1) {
                s = s.substring(n + 1);
                n = s.indexOf("-");
                name = name + "-" + s.substring(0, n);
                try {
                    test = java.lang.Integer.parseInt(s.substring(n + 1, n + 2));
                    version = s.substring(n + 1);
                } catch (java.lang.NumberFormatException e2) {
                    s = s.substring(n + 1);
                    n = s.indexOf("-");
                    name = name + "-" + s.substring(0, n);
                    try {
                        test = java.lang.Integer.parseInt(s.substring(n + 1, n + 2));
                        version = s.substring(n + 1);
                    } catch (java.lang.NumberFormatException e3) {
                        version = "ccccc";
                    }
                }
            }
        }
        return ("/usr/portage/" + category + "/" + name + "/" + name + "-" + version + ".ebuild");
    }

    public String getPortDirOverlay() {
        String line;
        String result = "not defined";
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader("/etc/make.conf"));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("PORTDIR_OVERLAY")) {
                    result = line.substring(line.indexOf("=") + 1, line.length());
                }
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
