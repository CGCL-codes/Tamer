package clump.boot;

import opala.utils.Predicate;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Regression {

    private static class Entry {

        String reason;

        String location;

        private Entry(String reason, String location) {
            this.reason = reason.trim();
            this.location = location.trim();
        }

        public String toString() {
            return reason + "\n" + location + "\n";
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Predicate.isNullOfEquals(entry.reason, this.reason) && Predicate.isNullOfEquals(entry.location, this.location);
        }

        public int hashCode() {
            int result = reason != null ? reason.hashCode() : 0;
            result = 31 * result + (location != null ? location.hashCode() : 0);
            return result;
        }

        public void fixValue(String key, String value) {
            reason = reason.replaceAll(key, value);
            location = location.replaceAll(key, value);
        }
    }

    private static List<Entry> getEntries(InputStream stream) throws IOException {
        try {
            final List<Entry> entries = new ArrayList<Entry>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = ""; line != null; line = reader.readLine()) {
                if (line.startsWith("# ")) {
                    final String reason = line;
                    final String location = reader.readLine();
                    if (location == null) {
                        System.err.println("Skip line <" + line + ">");
                        line = null;
                    } else if (location.startsWith("# in ")) {
                        entries.add(new Entry(reason, location));
                    }
                }
            }
            return entries;
        } finally {
            stream.close();
        }
    }

    public static void main(String[] args) throws Throwable {
        if (args.length > 0) {
            final File directory = new File(args[0]).getCanonicalFile();
            final String[] nargs = new String[] { "-nb", directory.toString() };
            try {
                Locale.setDefault(Locale.ENGLISH);
                final PrintStream stderr = System.err;
                final ByteArrayOutputStream nerr = new ByteArrayOutputStream();
                System.setErr(new PrintStream(nerr));
                Class.forName("clump.language.boot.Compile").getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { nargs });
                System.setErr(stderr);
                final byte[] bytes = nerr.toByteArray();
                final List<Entry> detected = getEntries(new ByteArrayInputStream(bytes, 0, bytes.length));
                final File[] files = new File(directory, "traces").listFiles(new FileFilter() {

                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".err");
                    }
                });
                final List<Entry> waited = new ArrayList<Entry>();
                if (files != null) {
                    for (File file : files) {
                        waited.addAll(getEntries(new FileInputStream(file)));
                    }
                    for (Entry entry : waited) {
                        entry.fixValue("\\$DEADZONE\\$", directory.toString());
                    }
                }
                final int waitedLen = waited.size();
                final List<Entry> undefined = new ArrayList<Entry>();
                for (Entry entry : detected) {
                    if (waited.remove(entry)) {
                        System.err.println("[TEST> Managed error [" + entry.reason + "]");
                    } else {
                        undefined.add(entry);
                    }
                }
                if (waited.size() > 0) {
                    System.err.println("[TEST> Errors which has not been detected but waiting for:");
                    for (Entry entry : waited) {
                        System.err.println(entry);
                    }
                }
                if (undefined.size() > 0) {
                    System.err.println("[TEST> Errors which has been detected but not waiting for");
                    for (Entry entry : undefined) {
                        System.err.println(entry);
                    }
                }
                final int percentManaged = (100 * (waitedLen - waited.size())) / waitedLen;
                final int percentUnmanaged = (100 * undefined.size()) / detected.size();
                if (percentManaged == 100 && percentUnmanaged == 0) {
                    System.err.println("[Success] managed errors = " + percentManaged + "%");
                    System.exit(1);
                } else {
                    System.err.println("[Failure] managed errors = " + percentManaged + "%, unmanaged = " + percentUnmanaged + "%");
                    System.exit(1);
                }
            } catch (IllegalAccessException e) {
                System.err.println("main method in [" + args[0] + "] is not accessible");
            } catch (InvocationTargetException e) {
                throw e.getCause();
            } catch (NoSuchMethodException e) {
                System.err.println("main method in [" + args[0] + "] is not available");
            } catch (ClassNotFoundException e) {
                System.err.println("class [" + args[0] + "] does not exist");
            }
        }
    }
}
